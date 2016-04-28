/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.model;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.PropertyRegistry;

import com.funny.autocode.util.PropertyConfigurer;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class BaseRecordGenerator extends AbstractJavaGenerator {

    public BaseRecordGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        if (introspectedTable.isConstructorBased()) {
            addParameterizedConstructor(topLevelClass);

            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }

        String rootClass = getRootClass();
        // add serialVersionUID
        topLevelClass.addField(getSerialVersionUIDField());

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            Field field = getJavaBeansField(introspectedColumn);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());

            Method setmethod = getJavaBeansSetter(introspectedColumn);
            topLevelClass.addMethod(setmethod);

            Method getmethod = getJavaBeansGetter(introspectedColumn);
            topLevelClass.addMethod(getmethod);
        }

        String keyType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
        FullyQualifiedJavaType superClass = getSuperClass(keyType);
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    private FullyQualifiedJavaType getSuperClass(String keyType) {
        String parentclassname = PropertyConfigurer.config.getString("parent.model") + "<" + keyType + ">";
        return new FullyQualifiedJavaType(parentclassname);
    }

    private boolean includeBLOBColumns() {
        return !introspectedTable.getRules().generateRecordWithBLOBsClass() && introspectedTable.hasBLOBColumns();
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> constructorColumns =
                includeBLOBColumns() ? introspectedTable.getAllColumns() : introspectedTable.getNonBLOBColumns();

        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(),
                    introspectedColumn.getJavaProperty()));
        }

        StringBuilder sb = new StringBuilder();
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            boolean comma = false;
            sb.append("super("); //$NON-NLS-1$
            for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }
                sb.append(introspectedColumn.getJavaProperty());
            }
            sb.append(");"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        }

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this."); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = "); //$NON-NLS-1$
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
    }

    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns = new ArrayList<IntrospectedColumn>();
        // if (includePrimaryKeyColumns()) {
        // if (includeBLOBColumns()) {
        // introspectedColumns = introspectedTable.getAllColumns();
        // } else {
        // introspectedColumns = introspectedTable.getNonBLOBColumns();
        // }
        // } else {
        // if (includeBLOBColumns()) {
        // introspectedColumns = introspectedTable
        // .getNonPrimaryKeyColumns();
        // } else {
        // introspectedColumns = introspectedTable.getBaseColumns();
        // }
        // }
        // 去除CREATE_USER_ID CREATE_DATE UPDATE_USER_ID UPDATE_DATE IS_VALID
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            if (!column.getActualColumnName().equalsIgnoreCase(PropertyConfigurer.config.getString("key.id"))
                    && !column.getActualColumnName().equalsIgnoreCase(PropertyConfigurer.config.getString("is.valid"))
                    && !column.getActualColumnName()
                            .equalsIgnoreCase(PropertyConfigurer.config.getString("modify.date"))
                    && !column.getActualColumnName().equalsIgnoreCase(PropertyConfigurer.config.getString("modify.id"))
                    && !column.getActualColumnName().equalsIgnoreCase(PropertyConfigurer.config.getString("create.id"))
                    && !column.getActualColumnName()
                            .equalsIgnoreCase(PropertyConfigurer.config.getString("create.date"))) {
                introspectedColumns.add(column);
            }
        }

        return introspectedColumns;
    }
}

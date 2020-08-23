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
package com.funny.combo.codegen.core.javamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import com.funny.combo.codegen.core.javamapper.AutoCodeJavaGenerator;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 
 *
 */
public class AutoCodeJavaEntityGenerator extends AutoCodeJavaGenerator {

    public AutoCodeJavaEntityGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()+"Entity");
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

        GlobalConfig globalConfig = AutoCodeHolder.getConfig();
        ComboCodeConfig comboCodeConfig = globalConfig.getComboCodeConfig();
        String fullBaseEntityName = comboCodeConfig.getBaseEntityName();

        String baseEntityName  = fullBaseEntityName.substring(fullBaseEntityName.lastIndexOf(".")+1,fullBaseEntityName.length());

        FullyQualifiedJavaType superClass = new FullyQualifiedJavaType(baseEntityName);
        if (superClass != null) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(fullBaseEntityName));
            topLevelClass.setSuperClass(superClass);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
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

        GlobalConfig globalConfig = AutoCodeHolder.getConfig();
        ComboCodeConfig comboCodeConfig = globalConfig.getComboCodeConfig();

        String[] encrys =  comboCodeConfig.getEncryptColumns().split(",");
        String[] hashs =  comboCodeConfig.getHashColumns().split(",");

        List<IntrospectedColumn> introspectedColumns = new ArrayList<IntrospectedColumn>();
        // 去除创建时间，创建人,修改时间，修改人
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            if (column.getActualColumnName().equalsIgnoreCase(comboCodeConfig.getEntityId())
                    || column.getActualColumnName().equalsIgnoreCase(comboCodeConfig.getEntityIsDel())
                    || column.getActualColumnName().equalsIgnoreCase(comboCodeConfig.getEntityCreate())
                    || column.getActualColumnName().equalsIgnoreCase(comboCodeConfig.getEntityUpdate())) {
                continue;
            }

            for(String encry:encrys){
                if (column.getActualColumnName().endsWith(encry)){
                    column.setTypeHandler(comboCodeConfig.getEncryptHandler());
                    String columnName = column.getActualColumnName().replace(encry,"");
                    column.setJavaProperty(JavaBeansUtil.getCamelCaseString(columnName,false));
                }
            }
            for(String hash:hashs){
                if (column.getActualColumnName().endsWith(hash)){
                    column.setTypeHandler(comboCodeConfig.getHashHandler());
//                    String columnName = column.getActualColumnName().replace(hash,"");
//                    column.setJavaProperty(JavaBeansUtil.getCamelCaseString(columnName,false));
                }
            }

            introspectedColumns.add(column);
        }

        return introspectedColumns;
    }
}

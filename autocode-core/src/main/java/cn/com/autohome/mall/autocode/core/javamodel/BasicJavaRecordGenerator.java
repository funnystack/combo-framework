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
package cn.com.autohome.mall.autocode.core.javamodel;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

import cn.com.autohome.mall.autocode.core.javamapper.AutohomeJavaGenerator;

/**
 * 
 * @author fangli@autohome.com.cn
 * 
 */
public class BasicJavaRecordGenerator extends AutohomeJavaGenerator{

    public BasicJavaRecordGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType("cn.com.autohome.mall.base.BaseEntity");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Serializable"));
        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("Cloneable"));

        topLevelClass.addImportedType("java.util.Date");
        topLevelClass.addImportedType("java.io.Serializable");
        commentGenerator.addJavaFileComment(topLevelClass);

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

        // add serialVersionUID
        topLevelClass.addField(getSerialVersionUIDField());

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            Field field = getJavaBeansField(introspectedColumn);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());

            Method setmethod = getJavaBeansSetter(introspectedColumn);
            topLevelClass.addMethod(setmethod);

            Method getmethod = getJavaBeansGetter(introspectedColumn);
            topLevelClass.addMethod(getmethod);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    @Override
    protected Field getJavaBeansField(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(fqjt);
        field.setName(property);

        field.addJavaDocLine("/**");
        field.addJavaDocLine("* " + introspectedColumn.getRemarks());
        field.addJavaDocLine("*/");

        field.addFormattedJavadoc(new StringBuilder(), 1);
        context.getCommentGenerator().addFieldComment(field, introspectedTable, introspectedColumn);

        return field;
    }

    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns = new ArrayList<IntrospectedColumn>();
        IntrospectedColumn idColumn = new IntrospectedColumn();
        idColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Long"));
        idColumn.setJavaProperty("id");
        idColumn.setRemarks("主键id");
        introspectedColumns.add(idColumn);


        IntrospectedColumn createdColumn = new IntrospectedColumn();
        createdColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Date"));
        createdColumn.setJavaProperty("created");
        createdColumn.setRemarks("创建时间");
        introspectedColumns.add(createdColumn);


        IntrospectedColumn modifiedColumn = new IntrospectedColumn();
        modifiedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Date"));
        modifiedColumn.setJavaProperty("modified");
        modifiedColumn.setRemarks("修改时间");

        introspectedColumns.add(modifiedColumn);

        IntrospectedColumn statusColumn = new IntrospectedColumn();
        statusColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Integer"));
        statusColumn.setJavaProperty("status");
        statusColumn.setRemarks(" 1有效 0 删除");
        introspectedColumns.add(statusColumn);

        return introspectedColumns;
    }
}

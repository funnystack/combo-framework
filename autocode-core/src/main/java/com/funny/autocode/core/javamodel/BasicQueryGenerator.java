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
package com.funny.autocode.core.javamodel;

import java.util.ArrayList;
import java.util.List;

import com.funny.autocode.core.javamapper.AutoCodeJavaGenerator;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

/**
 * 
 *
 */
public class BasicQueryGenerator extends AutoCodeJavaGenerator {

    public BasicQueryGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {

        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType("cn.com.autohome.mall.entity.PageCondition");
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        commentGenerator.addJavaFileComment(topLevelClass);

        List<IntrospectedColumn> introspectedColumns = getColumnsInThisClass();

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


    private List<IntrospectedColumn> getColumnsInThisClass() {
        List<IntrospectedColumn> introspectedColumns = new ArrayList<IntrospectedColumn>();
        IntrospectedColumn idColumn = new IntrospectedColumn();
        idColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Integer"));
        idColumn.setJavaProperty("pageSize");
        idColumn.setDefaultValue("10");
        idColumn.setRemarks("条数");
        introspectedColumns.add(idColumn);


        IntrospectedColumn createdColumn = new IntrospectedColumn();
        createdColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType("Integer"));
        createdColumn.setJavaProperty("pageNumber");
        createdColumn.setDefaultValue("1");
        createdColumn.setRemarks("页码");
        introspectedColumns.add(createdColumn);

        return introspectedColumns;
    }
}

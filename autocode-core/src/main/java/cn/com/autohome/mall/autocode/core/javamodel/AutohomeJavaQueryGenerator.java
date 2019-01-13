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
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import cn.com.autohome.mall.autocode.core.javamapper.AutohomeJavaGenerator;

/**
 *
 * @author fangli@autohome.com.cn
 * 
 */
public class AutohomeJavaQueryGenerator extends AutohomeJavaGenerator {

    public AutohomeJavaQueryGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        String className = introspectedTable.getBaseRecordType().replace(".entity.",".query.")+"Query";
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(className);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);


        String rootClass = introspectedTable.getBaseRecordType();
        topLevelClass.addImportedType(rootClass);
        topLevelClass.setSuperClass(rootClass);
        topLevelClass.addField(getSerialVersionUIDField());
        topLevelClass.addImportedType("cn.com.autohome.mall.entity.PageCondition");



        Field field = getJavaField("PageCondition");
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());

        Method setmethod = getJavaSetter("PageCondition");
        topLevelClass.addMethod(setmethod);

        Method getmethod = getJavaGetter("PageCondition");
        topLevelClass.addMethod(getmethod);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    private Field getJavaField(String queryField) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(queryField);
        String property = "pageCondition";

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);

        field.addJavaDocLine("/**");
        field.addJavaDocLine("* 分页条件");
        field.addJavaDocLine("*/");

        field.addFormattedJavadoc(new StringBuilder(), 1);
        return field;
    }

    private Method getJavaGetter(String queryField) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(queryField);
        String property = "pageCondition";

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(JavaBeansUtil.getGetterMethodName(property, fqjt));

        StringBuilder sb = new StringBuilder();
        sb.append("return ");
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

    private Method getJavaSetter(String queryField) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(queryField);
        String property = "pageCondition";
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(JavaBeansUtil.getSetterMethodName(property));
        method.addParameter(new Parameter(fqjt, property));

        StringBuilder sb = new StringBuilder();
        sb.append("this."); //$NON-NLS-1$
        sb.append(property);
        sb.append(" = "); //$NON-NLS-1$
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }
}

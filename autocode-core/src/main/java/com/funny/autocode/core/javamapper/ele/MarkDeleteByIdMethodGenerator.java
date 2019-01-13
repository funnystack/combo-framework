/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.funny.autocode.core.javamapper.ele;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class MarkDeleteByIdMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    public MarkDeleteByIdMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();

        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("markDeleteById");
        method.addJavaDocLine("/**");
        method.addJavaDocLine("* 根据主键逻辑删除");
        method.addJavaDocLine("* ");
        method.addJavaDocLine("* @param id 对象ID");
        method.addJavaDocLine("* @return 受影响行数");
        method.addJavaDocLine("*/");


        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("Long");
        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, "id")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(interfaze, method);

        if (context.getPlugins().clientInsertMethodGenerated(method, interfaze,
                introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }
    
    public void addMapperAnnotations(Interface interfaze, Method method) {
    }
}

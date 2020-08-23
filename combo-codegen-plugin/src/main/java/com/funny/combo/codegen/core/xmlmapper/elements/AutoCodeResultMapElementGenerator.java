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
package com.funny.combo.codegen.core.xmlmapper.elements;

import java.util.List;

import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class AutoCodeResultMapElementGenerator extends AbstractXmlElementGenerator {

    public AutoCodeResultMapElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId()));
        answer.addAttribute(new Attribute("type", introspectedTable.getBaseRecordType()+"Entity"));


        XmlElement sql = new XmlElement("sql");
        sql.addAttribute(new Attribute("id", "sql_where_condition"));


        context.getCommentGenerator().addComment(answer);

        if (introspectedTable.isConstructorBased()) {
            addResultMapConstructorElements(answer);
        } else {
            addResultMapElements(answer);
            addSQLElements(sql);
        }
        if (context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
        parentElement.addElement(sql);
    }

    private void addResultMapElements(XmlElement answer) {
        GlobalConfig globalConfig = AutoCodeHolder.getConfig();
        ComboCodeConfig comboCodeConfig = globalConfig.getComboCodeConfig();
        String[] hashs =  comboCodeConfig.getHashColumns().split(",");

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("id"); 

            resultElement.addAttribute(new Attribute("column", 
                    MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));

            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));

            resultElement.addAttribute(new Attribute("jdbcType",
                    introspectedColumn.getJdbcTypeName()));

            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }

        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();

        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("result");

            resultElement.addAttribute(new Attribute("column", 
                    MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); 
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));

            boolean hashColumn = false;
            for(String hash:hashs){
                if (introspectedColumn.getActualColumnName().endsWith(hash)){
                    hashColumn = true;
                }
            }
            if (!hashColumn) {
                if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                    resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
                }
            }


            answer.addElement(resultElement);
        }
    }

    private void addResultMapConstructorElements(XmlElement answer) {
        XmlElement constructor = new XmlElement("constructor");

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("idArg");

            resultElement.addAttribute(new Attribute("column",
                    MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType",
                    introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType",
                    introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }

            constructor.addElement(resultElement);
        }

        List<IntrospectedColumn> columns;
        columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("arg");

            resultElement.addAttribute(new Attribute("column",
                    MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType",
                    introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType",
                    introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }

            constructor.addElement(resultElement);
        }

        answer.addElement(constructor);
    }


    private void addSQLElements(XmlElement answer) {
        List<IntrospectedColumn> columns = introspectedTable.getNonPrimaryKeyColumns();
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement iftest = new XmlElement("if");
            iftest.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty()+ "  != null "));

            sb.setLength(0);
            sb.append(" and ");
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

            iftest.addElement(new TextElement(sb.toString()));

            answer.addElement(iftest);
        }
    }
}

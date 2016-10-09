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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import com.funny.autocode.common.SystemConstants;
import com.funny.autocode.util.PropertyConfigurer;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DeleteByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {


    public DeleteByPrimaryKeyElementGenerator() {
        super();
    }

    @Override
    public XmlElement addElements() {
        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //

        String parameterClass = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
        
        answer.addAttribute(new Attribute("parameterType", parameterClass));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("set ");
        sb.append(PropertyConfigurer.config.getString("is.valid"));
        sb.append(" = 0");
        if (context.getDatabaseType().equals(SystemConstants.DB_MYSQL)) {
            sb.append(",");
            sb.append(PropertyConfigurer.config.getString("modify.date"));
            sb.append(" = now() ");
        } else if (context.getDatabaseType().equals(SystemConstants.DB_ORACLE)) {
            sb.append(",");
            sb.append(PropertyConfigurer.config.getString("modify.date"));
            sb.append(" = sysdate()");
        }

        answer.addElement(new TextElement(sb.toString()));

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            sb.append("where "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        return answer;
    }
}

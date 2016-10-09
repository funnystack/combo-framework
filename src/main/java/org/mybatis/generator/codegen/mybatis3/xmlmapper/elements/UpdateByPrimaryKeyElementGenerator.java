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

import java.util.Iterator;

import com.funny.autocode.common.SystemConstants;
import com.funny.autocode.util.PropertyConfigurer;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {

    public UpdateByPrimaryKeyElementGenerator() {
        super();
    }

    @Override
    public XmlElement addElements() {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$

        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator<IntrospectedColumn> iter = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            String columnName = introspectedColumn.getActualColumnName();
            if (columnName.equalsIgnoreCase(PropertyConfigurer.config.getString("create.date"))||
                    columnName.equalsIgnoreCase(PropertyConfigurer.config.getString("create.id"))) {
                continue;
            }

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            if (columnName.equalsIgnoreCase(PropertyConfigurer.config.getString("modify.date"))) {
                if (context.getDatabaseType().equals(SystemConstants.DB_MYSQL)) {
                    sb.append(" = now()");
                } else if (context.getDatabaseType().equals(SystemConstants.DB_ORACLE)) {
                    sb.append(" = sysdate()");
                } else {
                    sb.append(" = ");
                    sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                }
            } else {
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            }

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        return answer;
    }
}

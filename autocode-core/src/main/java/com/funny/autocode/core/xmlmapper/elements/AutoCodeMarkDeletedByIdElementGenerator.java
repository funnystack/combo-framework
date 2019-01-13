package com.funny.autocode.core.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * Define AutoCodeMarkDeletedByIdElementGenerator.
 * <p>Created with IntelliJ IDEA on 03/12/2016 15:00.</p>
 *
 * @version 1.0
 */
public class AutoCodeMarkDeletedByIdElementGenerator extends AbstractXmlElementGenerator {

    public AutoCodeMarkDeletedByIdElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", "markDeleteById")); //$NON-NLS-1$

        String parameterType;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterType = introspectedTable.getPrimaryKeyType();
        } else {
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterType = "map";
            } else {
                parameterType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
            }
        }

        answer.addAttribute(new Attribute("parameterType",parameterType));


        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        sb.append(" set is_delete = 1 ");
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if (context.getPlugins()
                .sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
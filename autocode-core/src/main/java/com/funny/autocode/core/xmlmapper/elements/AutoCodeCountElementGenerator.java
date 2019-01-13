package com.funny.autocode.core.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * <p>Created with IntelliJ IDEA on 3/17/2016 1:36 PM.</p>
 *
 * @version 1.0
 */
public class AutoCodeCountElementGenerator extends AbstractXmlElementGenerator {

    public AutoCodeCountElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        String className = introspectedTable.getBaseRecordType()+"Entity";

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", "count")); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", className));


        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) FROM ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" where 1 = 1");
        answer.addElement(new TextElement(sb.toString()));

        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}

package cn.com.autohome.mall.autocode.core.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * Define AutohomeFindListElementGenerator.
 * <p>Created with IntelliJ IDEA on 3/17/2016 1:36 PM.</p>
 *
 * @author eric yang [yangyanju@autohome.com.cn]
 * @version 1.0
 */
public class AutohomeFindListElementGenerator extends AbstractXmlElementGenerator {

    public AutohomeFindListElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        String className = introspectedTable.getBaseRecordType()+"Entity";

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", introspectedTable.getSelectAllStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", className));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");

        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());

        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(","));
            answer.addElement(getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("FROM ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" where 1 = 1");

        TextElement countElement = new TextElement(sb.toString());
        sb.setLength(0);
//        XmlElement iftest = new XmlElement("if"); //$NON-NLS-1$
//        iftest.addAttribute(new Attribute("test", " pageCondition != null ")); //$NON-NLS-1$
//        iftest.addElement(new TextElement(" limit ${pageNo},${pageSize} "));
        answer.addElement(countElement);
//        answer.addElement(iftest);
        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}

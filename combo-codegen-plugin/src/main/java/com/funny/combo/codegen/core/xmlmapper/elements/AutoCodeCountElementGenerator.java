package com.funny.combo.codegen.core.xmlmapper.elements;

import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
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
        GlobalConfig globalConfig = AutoCodeHolder.getConfig();
        ComboCodeConfig comboCodeConfig = globalConfig.getComboCodeConfig();

        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("id", comboCodeConfig.getSqlCount())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", className));


        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) FROM ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        sb.append(" where 1 = 1");
        answer.addElement(new TextElement(sb.toString()));

        XmlElement sql = new XmlElement("include");
        sql.addAttribute(new Attribute("refid", "sql_where_condition"));
        answer.addElement(sql);

        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}

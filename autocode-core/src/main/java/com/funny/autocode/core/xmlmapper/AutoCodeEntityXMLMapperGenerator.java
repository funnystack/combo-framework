package com.funny.autocode.core.xmlmapper;

import com.funny.autocode.core.xmlmapper.elements.*;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 * <p>
 * Created with IntelliJ IDEA on 03/12/2016 14:49.
 * </p>
 *
 * @version 1.0
 */
public class AutoCodeEntityXMLMapperGenerator extends XMLMapperGenerator {

    public AutoCodeEntityXMLMapperGenerator() {
        super();
    }

    @Override
    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));

        context.getCommentGenerator().addRootComment(answer);

        addBaseColumnListElement(answer);
        addBlobColumnListElement(answer);

        addResultMapElement(answer);

        addInsertElement(answer);

//        addDeleteByPrimaryKeyElement(answer);
//        addMarkAsDeletedElement(answer);

        addUpdateByPrimaryKeySelectiveElement(answer);

        addSelectByPrimaryKeyElement(answer);

        addFindListColumnElement(answer);
        addCountElement(answer);
        return answer;
    }

    protected void addInsertElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateInsert()) {
            AbstractXmlElementGenerator elementGenerator = new AutoCodeInsertElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addUpdateByPrimaryKeySelectiveElement(
            XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            AbstractXmlElementGenerator elementGenerator = new AutoCodeUpdateByPrimaryKeySelectiveElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addResultMapElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new AutoCodeResultMapElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addFindListColumnElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new AutoCodeFindListElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new AutoCodeSelectByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addMarkAsDeletedElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new AutoCodeMarkDeletedByIdElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addCountElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new AutoCodeCountElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
}

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
package org.mybatis.generator.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.BlobColumnListElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.CountElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectAllElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyElementGenerator;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class XMLMapperGenerator extends AbstractXmlGenerator {

    public XMLMapperGenerator() {
        super();
    }
    protected XmlElement getSqlMapElementWriteDao() {
        XmlElement roornode = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        roornode.addAttribute(new Attribute("namespace", namespace));

        context.getCommentGenerator().addRootComment(roornode);

        addResultMapWithoutBLOBsElement(roornode);
        addResultMapWithBLOBsElement(roornode);
        addBaseColumnListElement(roornode);
        addBlobColumnListElement(roornode);
        addInsertElement(roornode);

        if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
            addUpdateByPrimaryKeySelectiveElement(roornode);
            //addUpdateByPrimaryKeyWithBLOBsElement(roornode);
            //addUpdateByPrimaryKeyWithoutBLOBsElement(roornode);
            addDeleteByPrimaryKeyElement(roornode);
            //addSelectByPrimaryKeyElement(roornode);
        }
        //addCountElement(roornode);
        //addSelectAllElement(roornode);
        return roornode;
    }
    protected XmlElement getSqlMapElementReadDao() {
        XmlElement roornode = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        roornode.addAttribute(new Attribute("namespace", namespace));

        context.getCommentGenerator().addRootComment(roornode);

        //addResultMapWithoutBLOBsElement(roornode);
        //addResultMapWithBLOBsElement(roornode);
        //addBaseColumnListElement(roornode);
        //addBlobColumnListElement(roornode);
        //addInsertElement(roornode);

        if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
            //addUpdateByPrimaryKeySelectiveElement(roornode);
            //addUpdateByPrimaryKeyWithBLOBsElement(roornode);
            //addUpdateByPrimaryKeyWithoutBLOBsElement(roornode);
            //addDeleteByPrimaryKeyElement(roornode);
            addSelectByPrimaryKeyElement(roornode);
        }
        addCountElement(roornode);
        addSelectAllElement(roornode);

        return roornode;
    }
    protected XmlElement getSqlMapElementBasicDao() {
        XmlElement roornode = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        roornode.addAttribute(new Attribute("namespace", namespace));

        context.getCommentGenerator().addRootComment(roornode);

        addResultMapWithoutBLOBsElement(roornode);
        addResultMapWithBLOBsElement(roornode);
        addBaseColumnListElement(roornode);
        addBlobColumnListElement(roornode);
        addInsertElement(roornode);

        if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
            addUpdateByPrimaryKeySelectiveElement(roornode);
            addUpdateByPrimaryKeyElementGenerator(roornode);
            addDeleteByPrimaryKeyElement(roornode);
            addSelectByPrimaryKeyElement(roornode);
        }
        addCountElement(roornode);
        addSelectAllElement(roornode);

        return roornode;
    }
    protected XmlElement getSqlMapElementDao() {
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));

        answer.addElement(new Element() {
            @Override
            public String getFormattedContent(int indentLevel) {
                return "  ";
            }
        });
        context.getCommentGenerator().addRootComment(answer);
        return answer;
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseResultMap()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(false);
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addCountElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new CountElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addSelectAllElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectAllElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBaseColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        if (introspectedTable.getRules().generateBlobColumnList()) {
            AbstractXmlElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new SelectByPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new DeleteByPrimaryKeyElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addInsertElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new InsertElementGenerator(false);
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeySelectiveElementGenerator();
        initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addUpdateByPrimaryKeyElementGenerator(XmlElement parentElement) {
        if (introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            AbstractXmlElementGenerator elementGenerator = new UpdateByPrimaryKeyElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        parentElement.addElement(elementGenerator.addElements());
    }

    

    @Override
    public Document getDocumentDao() {
        Document document =
                new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElementDao());
        return document;
    }

    @Override
    public Document getDocumentBasicDao() {
        Document document =
                new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElementBasicDao());
        return document;
    }

    @Override
    public Document getDocumentReadDao() {
        Document document =
                new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElementReadDao());
        return document;
    }
    
    @Override
    public Document getDocumentWriteDao() {
        Document document =
                new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElementWriteDao());

        return document;
    }
}

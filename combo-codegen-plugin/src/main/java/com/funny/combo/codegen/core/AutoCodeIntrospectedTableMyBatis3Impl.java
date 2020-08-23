package com.funny.combo.codegen.core;

import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import com.funny.combo.codegen.core.javamapper.AutoCodeJavaMapperGenerator;
import com.funny.combo.codegen.core.javamodel.AutoCodeJavaEntityGenerator;
import com.funny.combo.codegen.core.javamodel.AutoCodeJavaRecordGenerator;
import com.funny.combo.codegen.core.xmlmapper.AutoCodeEntityXMLMapperGenerator;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.internal.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created with IntelliJ IDEA on 3/17/2016 9:54 AM.
 * </p>
 *
 * @version 1.0
 */
public class AutoCodeIntrospectedTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

    @Override
    protected void calculateXmlAttributes() {
        super.calculateXmlAttributes();

        GlobalConfig globalConfig = AutoCodeHolder.getConfig();
        ComboCodeConfig comboCodeConfig = globalConfig.getComboCodeConfig();
        setInsertSelectiveStatementId(comboCodeConfig.getSqlInsert());
        setSelectAllStatementId(comboCodeConfig.getSqlFindList());
        setSelectByPrimaryKeyStatementId(comboCodeConfig.getSqlFind());
        setUpdateByPrimaryKeySelectiveStatementId(comboCodeConfig.getSqlUpdate());
        setDeleteByPrimaryKeyStatementId(comboCodeConfig.getSqlDelete());
        setBaseResultMapId("BaseResultMap");
        setBaseColumnListId("Base_Column_List");
        setBlobColumnListId("Blob_Column_List");
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new AutoCodeJavaMapperGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) {
            javaGenerator = new AutoCodeJavaMapperGenerator();
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
        }

        return javaGenerator;
    }

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings,
            ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new AutoCodeEntityXMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }

        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaModelGenerator = new AutoCodeJavaEntityGenerator();
            initializeAbstractGenerator(javaModelGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaModelGenerator);

            AbstractJavaGenerator javaRecordGenerator = new AutoCodeJavaRecordGenerator();
            initializeAbstractGenerator(javaRecordGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaRecordGenerator);

//            AbstractJavaGenerator baseEntity = new BasicJavaRecordGenerator();
//            initializeAbstractGenerator(baseEntity, warnings, progressCallback);
//            javaModelGenerators.add(baseEntity);

//            AbstractJavaGenerator baseQuery = new BasicQueryGenerator();
//            initializeAbstractGenerator(baseQuery, warnings, progressCallback);
//            javaModelGenerators.add(baseQuery);

//            AbstractJavaGenerator baseMapper = new BasicJavaMapperGenerator();
//            initializeAbstractGenerator(baseMapper, warnings, progressCallback);
//            clientGenerators.add(baseMapper);
        }
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, getEntityXMLName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(), false, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }
        GeneratedXmlFile extra =
                new GeneratedXmlFile(getExtraDocument(), getExtraXMLName(), getMyBatis3XmlMapperPackage(),
                        context.getSqlMapGeneratorConfiguration().getTargetProject(), false, context.getXmlFormatter());
        if (context.getPlugins().sqlMapGenerated(extra, this)) {
            answer.add(extra);
        }
        return answer;
    }

    private String getEntityXMLName() {
        String fileName = getMyBatis3XmlMapperFileName();
//        String name = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        return "Basic"+ fileName;
    }

    private String getExtraXMLName() {
        return getMyBatis3XmlMapperFileName();
    }

    private Document getExtraDocument() {
        Document document =
                new Document(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = xmlMapperGenerator.getIntrospectedTable().getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        answer.addElement(new Element() {
            @Override
            public String getFormattedContent(int indentLevel) {
                return "   ";
            }
        });

        context.getCommentGenerator().addRootComment(answer);
        document.setRootElement(answer);
        return document;

    }
}

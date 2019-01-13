package com.funny.autocode.core;

import com.funny.autocode.core.javamapper.AutoCodeJavaMapperGenerator;
import com.funny.autocode.core.javamapper.BasicJavaMapperGenerator;
import com.funny.autocode.core.javamodel.AutoCodeJavaEntityGenerator;
import com.funny.autocode.core.javamodel.AutoCodeJavaRecordGenerator;
import com.funny.autocode.core.xmlmapper.AutoCodeEntityXMLMapperGenerator;
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
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
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
        setInsertSelectiveStatementId("insert"); //$NON-NLS-1$
        setSelectAllStatementId("findList"); //$NON-NLS-1$
        setSelectByPrimaryKeyStatementId("findById"); //$NON-NLS-1$
        setUpdateByPrimaryKeySelectiveStatementId("updateById"); //$NON-NLS-1$
        setBaseResultMapId("BaseResultMap"); //$NON-NLS-1$
        setBaseColumnListId("Base_Column_List"); //$NON-NLS-1$
        setBlobColumnListId("Blob_Column_List"); //$NON-NLS-1$
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AutoCodeJavaMapperGenerator();
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new MixedClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
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

            AbstractJavaGenerator baseMapper = new BasicJavaMapperGenerator();
            initializeAbstractGenerator(baseMapper, warnings, progressCallback);
            clientGenerators.add(baseMapper);
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

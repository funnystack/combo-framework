package com.funny.autocode.core.javamapper;

import com.funny.autocode.core.xmlmapper.AutoCodeEntityXMLMapperGenerator;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Created with IntelliJ IDEA on 03/12/2016 14:48.
 * </p>
 *
 * @version 1.0
 */
public class AutoCodeJavaMapperGenerator extends JavaMapperGenerator {

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(Messages.getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String baseObject = introspectedTable.getBaseRecordType()+"Entity";
        String rootInterface = "BaseMapper<" + baseObject + ">";
        if (!StringUtility.stringHasValue(rootInterface)) {
            rootInterface =
                    context.getJavaClientGeneratorConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (StringUtility.stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addImportedType(fqjt);
            interfaze.addImportedType(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
            interfaze.addImportedType(new FullyQualifiedJavaType("cn.com.autohome.mall.base.BaseMapper"));
            interfaze.addSuperInterface(fqjt);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new AutoCodeEntityXMLMapperGenerator();
    }
}

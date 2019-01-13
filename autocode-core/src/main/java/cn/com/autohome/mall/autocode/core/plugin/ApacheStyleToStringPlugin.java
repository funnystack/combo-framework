package cn.com.autohome.mall.autocode.core.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Define ApacheStyleToStringPlugin.
 * <p>Created with IntelliJ IDEA on 21/11/2016 21:50.</p>
 *
 * @author eric yang [yangyanju@autohome.com.cn]
 * @version 1.0
 */
public class ApacheStyleToStringPlugin extends AbstractToStringPlugin {

    @Override
    protected void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString"); //$NON-NLS-1$
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override"); //$NON-NLS-1$
        }

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
        topLevelClass.addStaticImport("org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE");

        method.addBodyLine("return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);"); //$NON-NLS-1$

        topLevelClass.addMethod(method);
    }
}

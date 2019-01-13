package com.funny.autocode.core.javamapper;

import java.util.Date;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * Created by funny on 2017/1/10.
 */
public class AutoCodeJavaGenerator extends AbstractJavaGenerator {
    protected Field getSerialVersionUIDField() {

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("long");
        String property = "serialVersionUID = " + new Date().getTime()+ "L";
        Field field = new Field();
        field.addFormattedJavadoc(new StringBuilder(), 1);
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setFinal(true);
        field.setStatic(true);
        field.setType(fqjt);
        field.setName(property);
        return field;
    }

    protected Field getJavaBeansField(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(fqjt);
        field.setName(property);

        field.addJavaDocLine("/**");
        field.addJavaDocLine("* " + introspectedColumn.getRemarks());
        field.addJavaDocLine("*/");

        field.addFormattedJavadoc(new StringBuilder(), 1);
        context.getCommentGenerator().addFieldComment(field, introspectedTable, introspectedColumn);

        return field;
    }

    protected Method getJavaBeansGetter(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(fqjt);
        method.setName(JavaBeansUtil.getGetterMethodName(property, fqjt));
        context.getCommentGenerator().addGetterComment(method, introspectedTable, introspectedColumn);

        StringBuilder sb = new StringBuilder();
        sb.append("return "); //$NON-NLS-1$
        sb.append(property);
        sb.append(';');
        method.addBodyLine(sb.toString());

        return method;
    }

    protected Method getJavaBeansSetter(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType fqjt = introspectedColumn.getFullyQualifiedJavaType();
        String property = introspectedColumn.getJavaProperty();

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(JavaBeansUtil.getSetterMethodName(property));
        method.addParameter(new Parameter(fqjt, property));
        context.getCommentGenerator().addSetterComment(method, introspectedTable, introspectedColumn);

        StringBuilder sb = new StringBuilder();
        //isTrimStringsEnabled() &&
        if (introspectedColumn.isStringColumn()) {
            sb.append("this."); //$NON-NLS-1$
            sb.append(property);
            sb.append(" = "); //$NON-NLS-1$
            sb.append(property);
            sb.append(" == null ? null : "); //$NON-NLS-1$
            sb.append(property);
            sb.append(".trim();"); //$NON-NLS-1$
            method.addBodyLine(sb.toString());
        } else {
            sb.append("this."); //$NON-NLS-1$
            sb.append(property);
            sb.append(" = "); //$NON-NLS-1$
            sb.append(property);
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        return method;
    }

    public List<CompilationUnit> getCompilationUnits(String parentClassName) {
        return null;
    }
    @Override
    public List<CompilationUnit> getCompilationUnits() {
        return null;
    }
}

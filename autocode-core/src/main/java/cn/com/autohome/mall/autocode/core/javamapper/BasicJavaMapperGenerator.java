package cn.com.autohome.mall.autocode.core.javamapper;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

import cn.com.autohome.mall.autocode.core.javamapper.ele.*;
import cn.com.autohome.mall.autocode.core.xmlmapper.AutohomeEntityXMLMapperGenerator;

/**
 * Define AutohomeJavaMapperGenerator.
 * <p>
 * Created with IntelliJ IDEA on 03/12/2016 14:48.
 * </p>
 *
 * @author yangyanju [yangyanju@autohome.com.cn]
 * @version 1.0
 */
public class BasicJavaMapperGenerator extends JavaMapperGenerator {

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(Messages.getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType("cn.com.autohome.mall.base.BaseMapper<T extends BaseEntity>");
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);


        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Date"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.io.Serializable"));
        interfaze.addImportedType(new FullyQualifiedJavaType("cn.com.autohome.mall.base.BaseEntity"));

        insert(interfaze);
        updateById(interfaze);
        findById(interfaze);
        deleteById(interfaze);
        markDeleteById(interfaze);
        findList(interfaze);
        count(interfaze);


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
    protected void insert(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new InsertMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void updateById(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new UpdateByIdMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void findById(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new FindByIdMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void deleteById(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new DeleteByIdMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
    protected void markDeleteById(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new MarkDeleteByIdMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void findList(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new FindListMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void count(Interface interfaze) {
        AbstractJavaMapperMethodGenerator methodGenerator = new CountMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }


    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new AutohomeEntityXMLMapperGenerator();
    }
}

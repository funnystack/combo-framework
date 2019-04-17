package com.funny.autocode.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import com.funny.autocode.core.resolver.JavaTypeResolverImpl;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

import com.funny.autocode.core.config.GlobalConfig;

public class AutoCodeConfigurationParser {
    private GlobalConfig globalConfig;

    public AutoCodeConfigurationParser(GlobalConfig config) {
        this.globalConfig = config;
    }

    public Configuration getConfiguration() throws IOException, XMLParserException {
        Configuration configuration = new Configuration();
        addContext(configuration);
        return configuration;
    }

    private void addContext(Configuration configuration) {
        Context context = new Context(null);
        context.setId("MySQLTables");
        context.setTargetRuntime(AutoCodeIntrospectedTableMyBatis3Impl.class.getName());
        configuration.addContext(context);

        addJavaFileEnCoding(context);
        parseJavaTypeResolver(context);
        // addPlugin(context);
        parseJdbcConnection(context);
        parseJavaComment(context);
        parseJavaModelGenerator(context);
        parseSqlMapGenerator(context);
        parseJavaClientGenerator(context);

        parseTable(context);
    }

    protected void addJavaFileEnCoding(Context context) {
        context.addProperty("javaFileEncoding", "UTF-8");
    }

    protected void addPlugin(Context context) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        context.addPluginConfiguration(pluginConfiguration);
        // type 为plugin对应的class的全名
        pluginConfiguration.setConfigurationType("type");
    }

    protected void parseTable(Context context) {
        TableConfiguration tc = new TableConfiguration(context);
        context.addTableConfiguration(tc);

        if (StringUtility.stringHasValue(globalConfig.getTableNames())) {
            StringTokenizer st = new StringTokenizer(globalConfig.getTableNames(), ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    tc.setTableName(s);
                    tc.setDomainObjectName(getDomainObject(s));
                    GeneratedKey gk = new GeneratedKey("id", "MySql", true, null);
                    tc.setGeneratedKey(gk);
                    tc.addProperty("useActualColumnNames", "false");
                }
            }
        }
    }

    private String getDomainObject(String table_name) {
        String name = "";
        if (StringUtility.stringHasValue(globalConfig.getIgnorePrefix())) {
            StringTokenizer st = new StringTokenizer(globalConfig.getIgnorePrefix(), ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (table_name.toUpperCase().contains(s.toUpperCase())) {
                    name = JavaBeansUtil.getCamelCaseString(table_name.replace(s, ""), true);
                } else {
                    name = JavaBeansUtil.getCamelCaseString(table_name, true);
                }
            }
        }
        return name;
    }

    protected void parseJavaTypeResolver(Context context) {
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();
        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);
        javaTypeResolverConfiguration.setConfigurationType(JavaTypeResolverImpl.class.getName());
        javaTypeResolverConfiguration.addProperty("forceBigDecimals", "false");
    }

    protected void parseJavaComment(Context context) {
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();
        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);
        commentGeneratorConfiguration.addProperty("suppressDate", "true");
        commentGeneratorConfiguration.addProperty("suppressAllComments", "true");

    }

    protected void parseJavaModelGenerator(Context context) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();
        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);
        javaModelGeneratorConfiguration
                .setTargetPackage(globalConfig.getPackageName() + "." + globalConfig.getModuleName() + ".entity");
        javaModelGeneratorConfiguration.setTargetProject("./" + globalConfig.getDomainDirectory() + "/src/main/java");
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "true");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        javaModelGeneratorConfiguration.addProperty("constructorBased", "false");
    }

    protected void parseSqlMapGenerator(Context context) {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();

        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        String targetPackage = "mysql." + globalConfig.getModuleName(); //$NON-NLS-1$
        String targetProject = "./" + globalConfig.getDaoDirectory() + "/src/main/resources"; //$NON-NLS-2$

        sqlMapGeneratorConfiguration.setTargetPackage(targetPackage);
        sqlMapGeneratorConfiguration.setTargetProject(targetProject);
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages", "true");
    }

    private void parseJavaClientGenerator(Context context) {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration
                .setTargetPackage(globalConfig.getPackageName() + ".dao." + globalConfig.getModuleName());
        javaClientGeneratorConfiguration.setTargetProject("./" + globalConfig.getDaoDirectory() + "/src/main/java");
        javaClientGeneratorConfiguration.addProperty("enableSubPackages", "true");
    }

    protected void parseJdbcConnection(Context context) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        jdbcConnectionConfiguration.setDriverClass(globalConfig.getJdbcDriver());
        jdbcConnectionConfiguration.setConnectionURL(globalConfig.getJdbcURL());
        jdbcConnectionConfiguration.setUserId(globalConfig.getJdbcUserId());
        jdbcConnectionConfiguration.setPassword(globalConfig.getJdbcPassword());
    }

    public static Set<String> getTables(String tableNames) {
        Set<String> fullyqualifiedTables = new HashSet<String>();
        if (StringUtility.stringHasValue(tableNames)) {
            StringTokenizer st = new StringTokenizer(tableNames, ","); //$NON-NLS-1$
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }
        return fullyqualifiedTables;
    }
}

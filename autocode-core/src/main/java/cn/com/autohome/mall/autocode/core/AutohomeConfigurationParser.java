/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
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
package cn.com.autohome.mall.autocode.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

import cn.com.autohome.mall.autocode.core.config.AutohomeConfig;

public class AutohomeConfigurationParser {
    private AutohomeConfig autohomeConfig;

    public AutohomeConfigurationParser(AutohomeConfig config) {
        this.autohomeConfig = config;
    }

    public Configuration getConfiguration() throws IOException, XMLParserException {
        Configuration configuration = new Configuration();
        addContext(configuration);
        return configuration;
    }

    private void addContext(Configuration configuration) {
        String targetRuntime = "cn.com.autohome.mall.autocode.core.AutohomeIntrospectedTableMyBatis3Impl"; //$NON-NLS-1$
        String id = "MySQLTables"; //$NON-NLS-1$
        Context context = new Context(null);
        context.setId(id);

        if (StringUtility.stringHasValue(targetRuntime)) {
            context.setTargetRuntime(targetRuntime);
        }

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

        if (StringUtility.stringHasValue(autohomeConfig.getTableNames())) {
            StringTokenizer st = new StringTokenizer(autohomeConfig.getTableNames(), ","); //$NON-NLS-1$
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
        if (StringUtility.stringHasValue(autohomeConfig.getIgnorePeffix())) {
            StringTokenizer st = new StringTokenizer(autohomeConfig.getIgnorePeffix(), ","); //$NON-NLS-1$
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
        javaTypeResolverConfiguration
                .setConfigurationType("cn.com.autohome.mall.autocode.core.resolver.JavaTypeResolverImpl");
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
                .setTargetPackage(autohomeConfig.getPackageName() + "." + autohomeConfig.getModuleName() + ".entity");
        javaModelGeneratorConfiguration.setTargetProject("./" + autohomeConfig.getDomainDirectory() + "/src/main/java");
        javaModelGeneratorConfiguration.addProperty("enableSubPackages", "true");
        javaModelGeneratorConfiguration.addProperty("trimStrings", "true");
        javaModelGeneratorConfiguration.addProperty("constructorBased", "false");
    }

    protected void parseSqlMapGenerator(Context context) {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();

        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);
        String targetPackage = "mysql." + autohomeConfig.getModuleName(); //$NON-NLS-1$
        String targetProject = "./" + autohomeConfig.getDaoDirectory() + "/src/main/resources"; //$NON-NLS-2$

        sqlMapGeneratorConfiguration.setTargetPackage(targetPackage);
        sqlMapGeneratorConfiguration.setTargetProject(targetProject);
        sqlMapGeneratorConfiguration.addProperty("enableSubPackages", "true");
    }

    private void parseJavaClientGenerator(Context context) {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();
        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);
        javaClientGeneratorConfiguration.setConfigurationType("XMLMAPPER");
        javaClientGeneratorConfiguration
                .setTargetPackage(autohomeConfig.getPackageName() + ".dao." + autohomeConfig.getModuleName());
        javaClientGeneratorConfiguration.setTargetProject("./" + autohomeConfig.getDaoDirectory() + "/src/main/java");
        javaClientGeneratorConfiguration.addProperty("enableSubPackages", "true");
    }

    protected void parseJdbcConnection(Context context) {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();
        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);
        jdbcConnectionConfiguration.setDriverClass(autohomeConfig.getJdbcDriver());
        jdbcConnectionConfiguration.setConnectionURL(autohomeConfig.getJdbcURL());
        jdbcConnectionConfiguration.setUserId(autohomeConfig.getJdbcUserId());
        jdbcConnectionConfiguration.setPassword(autohomeConfig.getJdbcPassword());
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

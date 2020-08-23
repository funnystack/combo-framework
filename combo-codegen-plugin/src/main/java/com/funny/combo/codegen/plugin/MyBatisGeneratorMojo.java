/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.funny.combo.codegen.plugin;

import com.alibaba.fastjson.JSON;
import com.funny.combo.codegen.core.AutoCodeConfigurationParser;
import com.funny.combo.codegen.core.AutoCodeMybatisGenerator;
import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.mybatis.generator.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Goal which generates MyBatis/iBATIS artifacts.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MyBatisGeneratorMojo extends AbstractMojo {

    /**
     * Maven Project.
     *
     */
    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject project;

    /**
     * projectDirectory
     */
    @Parameter(property = "mybatis.generator.projectDirectory")
    private String projectDirectory;

    /**
     * dao Directory.
     */
    @Parameter(property = "mybatis.generator.daoDirectory")
    private String daoDirectory;

    /**
     * model Directory.
     */
    @Parameter(property = "mybatis.generator.domainDirectory")
    private String domainDirectory;

    /**
     * packageName
     */
    @Parameter(property = "mybatis.generator.packageName", defaultValue = "com.funny.combo")
    private String packageName;

    /**
     * moduleName.
     */
    @Parameter(property = "mybatis.generator.moduleName", defaultValue = "test")
    private String moduleName;

    /**
     * Specifies whether the mojo writes progress messages to the log.
     */
    @Parameter(property = "mybatis.generator.verbose", defaultValue = "true")
    private boolean verbose;

    /**
     * Specifies whether the mojo overwrites existing files. Default is false.
     */
    @Parameter(property = "mybatis.generator.overwrite", defaultValue = "false")
    private boolean overwrite;

    /**
     * JDBC Driver to use if a sql.script.file is specified.
     */
    @Parameter(property = "mybatis.generator.jdbcDriver")
    private String jdbcDriver;

    /**
     * JDBC URL to use if a sql.script.file is specified.
     */
    @Parameter(property = "mybatis.generator.jdbcURL")
    private String jdbcURL;

    /**
     * JDBC user ID to use if a sql.script.file is specified.
     */
    @Parameter(property = "mybatis.generator.jdbcUserId")
    private String jdbcUserId;

    /**
     * JDBC password to use if a sql.script.file is specified.
     */
    @Parameter(property = "mybatis.generator.jdbcPassword")
    private String jdbcPassword;

    /**
     * Comma delimited list of table names to generate.
     */
    @Parameter(property = "mybatis.generator.tableNames")
    private String tableNames;

    /**
     * 忽略的表前缀 ERP,CC, 以分好结尾
     */
    @Parameter(property = "mybatis.generator.ignorePeffix",defaultValue = "ERP,CC")
    private String ignorePeffix;

    @Parameter(property = "mybatis.generator.baseDaoName", defaultValue = "com.funny.combo.core.base.BaseMapper")
    private String baseDaoName;

    @Parameter(property = "mybatis.generator.baseEntityName", defaultValue = "com.funny.combo.core.base.BaseEntity")
    private String baseEntityName;


    @Parameter(property = "mybatis.generator.sqlCount", defaultValue = "count")
    private String sqlCount;

    @Parameter(property = "mybatis.generator.sqlDelete", defaultValue = "deleteById")
    private String sqlDelete;

    @Parameter(property = "mybatis.generator.sqlFind", defaultValue = "findEntityById")
    private String sqlFind;

    @Parameter(property = "mybatis.generator.sqlFindList", defaultValue = "listByCondition")
    private String sqlFindList;

    @Parameter(property = "mybatis.generator.sqlInsert", defaultValue = "insertSelective")
    private String sqlInsert;

    @Parameter(property = "mybatis.generator.sqlUpdate", defaultValue = "updateSelectiveById")
    private String sqlUpdate;

    @Parameter(property = "mybatis.generator.encryptColumns", defaultValue = "_encryped,_encry,_encrypt")
    private String encryptColumns;

    @Parameter(property = "mybatis.generator.hashColumns", defaultValue = "_hash")
    private String hashColumns;

    @Parameter(property = "mybatis.generator.encryptHandler", defaultValue = "com.funny.combo.encrypt.handler.CryptTypeHandler")
    private String encryptHandler;

    @Parameter(property = "mybatis.generator.hashHandler", defaultValue = "com.funny.combo.encrypt.handler.HashTypeHandler")
    private String hashHandler;

    @Parameter(property = "mybatis.generator.entityId", defaultValue = "id")
    private String entityId;

    @Parameter(property = "mybatis.generator.entityCreate", defaultValue = "gmt_created")
    private String entityCreate;

    @Parameter(property = "mybatis.generator.entityUpdate", defaultValue = "gmt_modified")
    private String entityUpdate;

    @Parameter(property = "mybatis.generator.entityIsDel", defaultValue = "is_del")
    private String entityIsDel;


    public void execute() throws MojoExecutionException {
        LogFactory.setLogFactory(new MavenLogFactory(this));
        Log log = getLog();

        List<Resource> resources = project.getResources();
        List<String> resourceDirectories = new ArrayList<String>();
        for (Resource resource : resources) {
            resourceDirectories.add(resource.getDirectory());
        }
        ClassLoader cl = ClassloaderUtility.getCustomClassloader(resourceDirectories);
        ObjectFactory.addResourceClassLoader(cl);

        if(projectDirectory == null || "".equals(projectDirectory)){
            throw new RuntimeException("projectDirectory is null");
        }
        String daoModuleDir = projectDirectory;
        if(StringUtils.isNotBlank(daoDirectory)){
            daoModuleDir = projectDirectory + File.separator + daoDirectory;
        }
        File daoModule = new File(daoModuleDir);
        if (!daoModule.exists()) {
            createSubMoudle(daoDirectory);
        }

        String domainModuleDir = projectDirectory;
        if(StringUtils.isNotBlank(domainDirectory)){
            domainModuleDir = projectDirectory + File.separator + domainDirectory;
        }

        File domainModule = new File(domainModuleDir);
        if (!domainModule.exists()) {
            createSubMoudle(domainDirectory);
        }

        jdbcURL = jdbcURL +GlobalConfig.MYSQL_CONCAT_URL;

        List<String> warnings = new ArrayList<String>();
        Set<String> fullyqualifiedTables = AutoCodeConfigurationParser.getTables(tableNames);
        GlobalConfig globalConfig = new GlobalConfig(projectDirectory, daoDirectory,
                domainDirectory,jdbcDriver, jdbcURL, jdbcUserId, jdbcPassword, tableNames,
                ignorePeffix, packageName, moduleName);
        globalConfig.setDomainDirectory(domainModuleDir);
        globalConfig.setDaoDirectory(daoModuleDir);

        ComboCodeConfig comboCodeConfig = new ComboCodeConfig();
        comboCodeConfig.setBaseDaoName(baseDaoName);
        comboCodeConfig.setBaseEntityName(baseEntityName);
        comboCodeConfig.setSqlCount(sqlCount);
        comboCodeConfig.setSqlDelete(sqlDelete);
        comboCodeConfig.setSqlFind(sqlFind);
        comboCodeConfig.setSqlFindList(sqlFindList);
        comboCodeConfig.setSqlInsert(sqlInsert);
        comboCodeConfig.setSqlUpdate(sqlUpdate);
        comboCodeConfig.setPrefix(ignorePeffix);

        comboCodeConfig.setEncryptColumns(encryptColumns);
        comboCodeConfig.setHashColumns(hashColumns);
        comboCodeConfig.setEncryptHandler(encryptHandler);
        comboCodeConfig.setHashHandler(hashHandler);

        comboCodeConfig.setEntityId(entityId);
        comboCodeConfig.setEntityCreate(entityCreate);
        comboCodeConfig.setEntityUpdate(entityUpdate);
        comboCodeConfig.setEntityIsDel(entityIsDel);

        globalConfig.setComboCodeConfig(comboCodeConfig);
        AutoCodeHolder.setConfig(globalConfig);
        System.out.println(JSON.toJSONString(globalConfig));
        try {
            AutoCodeConfigurationParser cp = new AutoCodeConfigurationParser(globalConfig);
            Configuration config = cp.getConfiguration();

            ShellCallback callback = new MavenShellCallback(this, overwrite);

            AutoCodeMybatisGenerator myBatisGenerator = new AutoCodeMybatisGenerator(config, callback, warnings);

            myBatisGenerator.generate(new MavenProgressCallback(getLog(), verbose), null, fullyqualifiedTables);

        } catch (XMLParserException e) {
            for (String error : e.getErrors()) {
                log.error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (SQLException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            for (String error : e.getErrors()) {
                log.error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
        }

        for (String error : warnings) {
            log.warn(error);
        }
    }

    public String getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory(String projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    public MavenProject getProject() {
        return project;
    }

    public String getDaoDirectory() {
        return daoDirectory;
    }

    public String getDomainDirectory() {
        return domainDirectory;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    private void createSubMoudle(String dirName) {
        // 创建model层
        String mkdir_order = "mvn archetype:generate -DinteractiveMode=false -DgroupId=com.funny -DartifactId="
                + dirName + " -DarchetypeCatalog=internal";
        try {
            Process process = Runtime.getRuntime().exec(mkdir_order);
        } catch (IOException e) {
            getLog().error("create module " + dirName + " failed!");
        }
        getLog().info("create module " + dirName + " success !");
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(1000 * 15);
        } catch (InterruptedException e) {
            getLog().error(e);
        }
    }

}

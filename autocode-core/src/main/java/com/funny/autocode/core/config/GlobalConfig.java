package com.funny.autocode.core.config;

import java.io.Serializable;

/**
 * Created by funny on 2017/1/16.
 */
public class GlobalConfig implements Serializable {
    private String targetDirectory;
    private String daoDirectory;
    private String domainDirectory;
    private boolean verbose = true;
    private boolean overwrite = true;
    private String jdbcDriver;
    private String jdbcURL;
    private String jdbcUserId;
    private String jdbcPassword;
    private String tableNames;
    private String ignorePrefix;
    private String packageName;
    private String moduleName;

    private String entityName;
    private String daoName;

    public GlobalConfig(String absolutePath, String daoDirectory, String domainDirectory,String jdbcDriver, String jdbcURL, String jdbcUserId, String jdbcPassword, String tableNames, String ignorePeffix, String packageName, String moduleName) {
       this.targetDirectory = absolutePath;
       this.jdbcDriver = jdbcDriver;
       this.jdbcPassword = jdbcPassword;
       this.jdbcURL = jdbcURL;
       this.jdbcUserId = jdbcUserId;
       this.tableNames = tableNames;
       this.ignorePrefix = ignorePeffix;
       this.packageName = packageName;
       this.moduleName = moduleName;
       this.daoDirectory = daoDirectory;
       this.domainDirectory = domainDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getDaoDirectory() {
        return daoDirectory;
    }

    public void setDaoDirectory(String daoDirectory) {
        this.daoDirectory = daoDirectory;
    }

    public String getDomainDirectory() {
        return domainDirectory;
    }

    public void setDomainDirectory(String domainDirectory) {
        this.domainDirectory = domainDirectory;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String getJdbcUserId() {
        return jdbcUserId;
    }

    public void setJdbcUserId(String jdbcUserId) {
        this.jdbcUserId = jdbcUserId;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getTableNames() {
        return tableNames;
    }

    public void setTableNames(String tableNames) {
        this.tableNames = tableNames;
    }

    public String getIgnorePrefix() {
        return ignorePrefix;
    }

    public void setIgnorePrefix(String ignorePrefix) {
        this.ignorePrefix = ignorePrefix;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDaoName() {
        return daoName;
    }

    public void setDaoName(String daoName) {
        this.daoName = daoName;
    }
}
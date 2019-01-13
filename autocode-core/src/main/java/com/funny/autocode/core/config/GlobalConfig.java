package com.funny.autocode.core.config;

/**
 * Created by funny on 2017/1/16.
 */
public class GlobalConfig {
    private String targetDirectory;
    private String daoDirectory;
    private String domainDirectory;
    private boolean verbose;
    private boolean overwrite;
    private String jdbcDriver;
    private String jdbcURL;
    private String jdbcUserId;
    private String jdbcPassword;
    private String tableNames;
    private String ignorePeffix;
    private String packageName;
    private String moduleName;

    public GlobalConfig(String targetDirectory, String daoDirectory, String domainDirectory, boolean verbose,
                        boolean overwrite, String jdbcDriver, String jdbcURL, String jdbcUserId, String jdbcPassword,
                        String tableNames, String ignorePeffix, String packageName, String moduleName) {
        this.targetDirectory = targetDirectory;
        this.daoDirectory = daoDirectory;
        this.domainDirectory = domainDirectory;
        this.verbose = verbose;
        this.overwrite = overwrite;
        this.jdbcDriver = jdbcDriver;
        this.jdbcURL = jdbcURL;
        this.jdbcUserId = jdbcUserId;
        this.jdbcPassword = jdbcPassword;
        this.tableNames = tableNames;
        this.ignorePeffix = ignorePeffix;
        this.packageName = packageName;
        this.moduleName = moduleName;
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

    public String getIgnorePeffix() {
        return ignorePeffix;
    }

    public void setIgnorePeffix(String ignorePeffix) {
        this.ignorePeffix = ignorePeffix;
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
}
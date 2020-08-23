package com.funny.combo.codegen.core.config;

import java.io.Serializable;


public class ComboCodeConfig implements Serializable {
    private String baseDaoName;

    private String baseEntityName;

    private String prefix;

    private String sqlInsert;

    private String sqlUpdate;

    private String sqlFind;

    private String sqlFindList;

    private String sqlCount;

    private String sqlDelete;

    private String encryptColumns;
    private String hashColumns;
    private String encryptHandler;
    private String hashHandler;


    private String entityId;
    private String entityCreate;
    private String entityUpdate;
    private String entityIsDel;


    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityCreate() {
        return entityCreate;
    }

    public void setEntityCreate(String entityCreate) {
        this.entityCreate = entityCreate;
    }

    public String getEntityUpdate() {
        return entityUpdate;
    }

    public void setEntityUpdate(String entityUpdate) {
        this.entityUpdate = entityUpdate;
    }

    public String getEntityIsDel() {
        return entityIsDel;
    }

    public void setEntityIsDel(String entityIsDel) {
        this.entityIsDel = entityIsDel;
    }

    public String getEncryptHandler() {
        return encryptHandler;
    }

    public void setEncryptHandler(String encryptHandler) {
        this.encryptHandler = encryptHandler;
    }

    public String getHashHandler() {
        return hashHandler;
    }

    public void setHashHandler(String hashHandler) {
        this.hashHandler = hashHandler;
    }

    public String getEncryptColumns() {
        return encryptColumns;
    }

    public void setEncryptColumns(String encryptColumns) {
        this.encryptColumns = encryptColumns;
    }

    public String getHashColumns() {
        return hashColumns;
    }

    public void setHashColumns(String hashColumns) {
        this.hashColumns = hashColumns;
    }


    public String getBaseDaoName() {
        return baseDaoName;
    }

    public void setBaseDaoName(String baseDaoName) {
        this.baseDaoName = baseDaoName;
    }

    public String getBaseEntityName() {
        return baseEntityName;
    }

    public void setBaseEntityName(String baseEntityName) {
        this.baseEntityName = baseEntityName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSqlInsert() {
        return sqlInsert;
    }

    public void setSqlInsert(String sqlInsert) {
        this.sqlInsert = sqlInsert;
    }

    public String getSqlUpdate() {
        return sqlUpdate;
    }

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    public String getSqlFind() {
        return sqlFind;
    }

    public void setSqlFind(String sqlFind) {
        this.sqlFind = sqlFind;
    }

    public String getSqlFindList() {
        return sqlFindList;
    }

    public void setSqlFindList(String sqlFindList) {
        this.sqlFindList = sqlFindList;
    }

    public String getSqlCount() {
        return sqlCount;
    }

    public void setSqlCount(String sqlCount) {
        this.sqlCount = sqlCount;
    }

    public String getSqlDelete() {
        return sqlDelete;
    }

    public void setSqlDelete(String sqlDelete) {
        this.sqlDelete = sqlDelete;
    }
}

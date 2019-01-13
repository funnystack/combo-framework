package com.funny.autocode.po;

public class Table {
    private String tableName;
    private String tableDesc;
    private String key;
    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }
    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    /**
     * @return the tableDesc
     */
    public String getTableDesc() {
        return tableDesc;
    }
    /**
     * @param tableDesc the tableDesc to set
     */
    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    public Table(String tableName, String tableDesc, String key) {
        super();
        this.tableName = tableName;
        this.tableDesc = tableDesc;
        this.key = key;
    }
    public Table() {
        super();
    }
    
}

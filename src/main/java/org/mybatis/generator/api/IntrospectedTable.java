/*
 *  Copyright 2006 The Apache Software Foundation
 *
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

package org.mybatis.generator.api;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.funny.autocode.util.PropertyConfigurer;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.rules.ConditionalModelRules;
import org.mybatis.generator.internal.rules.FlatModelRules;
import org.mybatis.generator.internal.rules.HierarchicalModelRules;
import org.mybatis.generator.internal.rules.Rules;

/**
 * Base class for all code generator implementations. This class provides many
 * of the housekeeping methods needed to implement a code generator, with only
 * the actual code generation methods left unimplemented.
 * 
 * @author Jeff Butler
 * 
 */
public abstract class IntrospectedTable {
    public enum TargetRuntime {
        IBATIS2, MYBATIS3
    }

    protected enum InternalAttribute {
        ATTR_DAO_IMPLEMENTATION_TYPE,
        ATTR_DAO_INTERFACE_TYPE,
        ATTR_PRIMARY_KEY_TYPE,
        ATTR_BASE_RECORD_TYPE,
        ATTR_RECORD_TYPE,
        ATTR_RECORD_WITH_BLOBS_TYPE,
        ATTR_IBATIS2_SQL_MAP_PACKAGE,
        ATTR_IBATIS2_SQL_MAP_FILE_NAME,
        ATTR_IBATIS2_SQL_MAP_NAMESPACE,
        ATTR_MYBATIS3_XML_MAPPER_PACKAGE,
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME,
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_BASIC,
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_READ,
        ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_WRITE,
        /** also used as XML Mapper namespace if a Java mapper is generated */
        ATTR_MYBATIS3_JAVA_MAPPER_TYPE,
        /** used as XML Mapper namespace if no client is generated */
        ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE,
        ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
        ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
        ATTR_COUNT_STATEMENT_ID,
        ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID,
        ATTR_INSERT_STATEMENT_ID,
        ATTR_INSERT_SELECTIVE_STATEMENT_ID,
        ATTR_SELECT_ALL_STATEMENT_ID,
        ATTR_FIND_CONDITION_STATEMENT_ID,
        ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID,
        ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID,
        ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID,
        ATTR_BASE_RESULT_MAP_ID,
        ATTR_RESULT_MAP_WITH_BLOBS_ID,
        ATTR_BASE_COLUMN_LIST_ID,
        ATTR_BLOB_COLUMN_LIST_ID,
        ATTR_MYBATIS3_SQL_PROVIDER_TYPE
    }

    protected TableConfiguration tableConfiguration;
    protected FullyQualifiedTable fullyQualifiedTable;
    protected Context context;
    protected Rules rules;
    protected List<IntrospectedColumn> primaryKeyColumns;
    protected List<IntrospectedColumn> baseColumns;
    protected List<IntrospectedColumn> blobColumns;
    protected TargetRuntime targetRuntime;

    /**
     * Attributes may be used by plugins to capture table related state between
     * the different plugin calls.
     */
    protected Map<String, Object> attributes;

    /**
     * Internal attributes are used to store commonly accessed items by all code
     * generators
     */
    protected Map<InternalAttribute, String> internalAttributes;

    public IntrospectedTable(TargetRuntime targetRuntime) {
        super();
        this.targetRuntime = targetRuntime;
        primaryKeyColumns = new ArrayList<IntrospectedColumn>();
        baseColumns = new ArrayList<IntrospectedColumn>();
        blobColumns = new ArrayList<IntrospectedColumn>();
        attributes = new HashMap<String, Object>();
        internalAttributes = new HashMap<InternalAttribute, String>();
    }

    public FullyQualifiedTable getFullyQualifiedTable() {
        return fullyQualifiedTable;
    }

    public String getSelectByPrimaryKeyQueryId() {
        return tableConfiguration.getSelectByPrimaryKeyQueryId();
    }

    public GeneratedKey getGeneratedKey() {
        return tableConfiguration.getGeneratedKey();
    }

    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            // search primary key columns
            for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search base columns
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            // search blob columns
            for (IntrospectedColumn introspectedColumn : blobColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getActualColumnName().equals(
                            columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getActualColumnName()
                            .equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Returns true if any of the columns in the table are JDBC Dates (as
     * opposed to timestamps).
     * 
     * @return true if the table contains DATE columns
     */
    public boolean hasJDBCDateColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCDateColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns true if any of the columns in the table are JDBC Times (as
     * opposed to timestamps).
     * 
     * @return true if the table contains TIME columns
     */
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;

        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }

        if (!rc) {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isJDBCTimeColumn()) {
                    rc = true;
                    break;
                }
            }
        }

        return rc;
    }

    /**
     * Returns the columns in the primary key. If the generatePrimaryKeyClass()
     * method returns false, then these columns will be iterated as the
     * parameters of the selectByPrimaryKay and deleteByPrimaryKey methods
     * 
     * @return a List of ColumnDefinition objects for columns in the primary key
     */
    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }

    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    /**
     * Returns all columns in the table (for use by the select by primary key
     * and select by example with BLOBs methods)
     * 
     * @return a List of ColumnDefinition objects for all columns in the table
     */
    public List<IntrospectedColumn> getAllColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    /**
     * Returns all columns except BLOBs (for use by the select by example
     * without BLOBs method)
     * 
     * @return a List of ColumnDefinition objects for columns in the table that
     *         are non BLOBs
     */
    public List<IntrospectedColumn> getNonBLOBColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(primaryKeyColumns);
        answer.addAll(baseColumns);

        return answer;
    }

    public int getNonBLOBColumnCount() {
        return primaryKeyColumns.size() + baseColumns.size();
    }

    public List<IntrospectedColumn> getNonPrimaryKeyColumns() {
        List<IntrospectedColumn> answer = new ArrayList<IntrospectedColumn>();
        answer.addAll(baseColumns);
        answer.addAll(blobColumns);

        return answer;
    }

    public List<IntrospectedColumn> getBLOBColumns() {
        return blobColumns;
    }

    public boolean hasBLOBColumns() {
        return blobColumns.size() > 0;
    }

    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }

    public Rules getRules() {
        return rules;
    }

    public String getTableConfigurationProperty(String property) {
        return tableConfiguration.getProperty(property);
    }

    public String getPrimaryKeyType() {
        return internalAttributes.get(InternalAttribute.ATTR_PRIMARY_KEY_TYPE);
    }

    /**
     * 
     * @return the type for the record (the class that holds non-primary key and
     *         non-BLOB fields). Note that the value will be calculated
     *         regardless of whether the table has these columns or not.
     */
    public String getBaseRecordType() {
        return internalAttributes.get(InternalAttribute.ATTR_BASE_RECORD_TYPE);
    }
    
    public String getRecordType() {
        return internalAttributes.get(InternalAttribute.ATTR_RECORD_TYPE);
    }


    /**
     * 
     * @return the type for the record with BLOBs class. Note that the value
     *         will be calculated regardless of whether the table has BLOB
     *         columns or not.
     */
    public String getRecordWithBLOBsType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE);
    }

    /**
     * Calculates an SQL Map file name for the table. Typically the name is
     * "XXXX_SqlMap.xml" where XXXX is the fully qualified table name (delimited
     * with underscores).
     * 
     * @return the name of the SqlMap file
     */
    public String getIbatis2SqlMapFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME);
    }

    public String getIbatis2SqlMapNamespace() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE);
    }

    public String getMyBatis3SqlMapNamespace() {
        String namespace = getMyBatis3JavaMapperType();
        if (namespace == null) {
            namespace = getMyBatis3FallbackSqlMapNamespace();
        }
        
        return namespace;
    }
    
    public String getMyBatis3FallbackSqlMapNamespace() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE);
    }
    
    /**
     * Calculates the package for the current table.
     * 
     * @return the package for the SqlMap for the current table
     */
    public String getIbatis2SqlMapPackage() {
        return internalAttributes
                .get(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE);
    }

    public String getDAOImplementationType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE);
    }

    public String getDAOInterfaceType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DAO_INTERFACE_TYPE);
    }

    public boolean hasAnyColumns() {
        return primaryKeyColumns.size() > 0 || baseColumns.size() > 0
                || blobColumns.size() > 0;
    }

    public void setTableConfiguration(TableConfiguration tableConfiguration) {
        this.tableConfiguration = tableConfiguration;
    }

    public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
        this.fullyQualifiedTable = fullyQualifiedTable;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void addColumn(IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.isBLOBColumn()) {
            blobColumns.add(introspectedColumn);
        } else {
            baseColumns.add(introspectedColumn);
        }

        introspectedColumn.setIntrospectedTable(this);
    }

    public void addPrimaryKeyColumn(String columnName) {
        boolean found = false;
        // first search base columns
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getActualColumnName().equals(columnName)) {
                primaryKeyColumns.add(introspectedColumn);
                iter.remove();
                found = true;
                break;
            }
        }

        // search blob columns in the weird event that a blob is the primary key
        if (!found) {
            iter = blobColumns.iterator();
            while (iter.hasNext()) {
                IntrospectedColumn introspectedColumn = iter.next();
                if (introspectedColumn.getActualColumnName().equals(columnName)) {
                    primaryKeyColumns.add(introspectedColumn);
                    iter.remove();
                    found = true;
                    break;
                }
            }
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void initialize() {
        calculateJavaClientAttributes();
        calculateModelAttributes();
        calculateXmlAttributes();

        if (tableConfiguration.getModelType() == ModelType.HIERARCHICAL) {
            rules = new HierarchicalModelRules(this);
        } else if (tableConfiguration.getModelType() == ModelType.FLAT) {
            rules = new FlatModelRules(this);
        } else {
            rules = new ConditionalModelRules(this);
        }

        //context.getPlugins().initialized(this);
    }

    /**
     * 
     */
    protected void calculateXmlAttributes() {
        setBasicMyBatis3XmlMapperFileName(calculateBasicMyBatis3XmlMapperFileName());
        setMyBatis3XmlMapperFileName(calculateMyBatis3XmlMapperFileName());
        setMyBatis3XmlMapperPackage(calculateSqlMapPackage());
        setMyBatis3FallbackSqlMapNamespace(calculateMyBatis3FallbackSqlMapNamespace());
        setSqlMapFullyQualifiedRuntimeTableName(calculateSqlMapFullyQualifiedRuntimeTableName());
        setSqlMapAliasedFullyQualifiedRuntimeTableName(calculateSqlMapAliasedFullyQualifiedRuntimeTableName());
        setBaseResultMapId("BaseResultMap"); //$NON-NLS-1$
        setResultMapWithBLOBsId("ResultMapWithBLOBs"); //$NON-NLS-1$
        setBaseColumnListId("Base_Column_List"); //$NON-NLS-1$
        setBlobColumnListId("Blob_Column_List"); //$NON-NLS-1$
        setInsertStatementId("insert"); //$NON-NLS-1$
        setSelectByPrimaryKeyStatementId(PropertyConfigurer.config.getString("find.id")); //$NON-NLS-1$
        setUpdateByPrimaryKeyStatementId(PropertyConfigurer.config.getString("update.id")); //$NON-NLS-1$
        setUpdateByPrimaryKeySelectiveStatementId(PropertyConfigurer.config.getString("update.id.selected")); //$NON-NLS-1$
        //setDeleteByPrimaryKeyStatementId(PropertyConfigurer.config.getString("delete.id")); //$NON-NLS-1$
        setCountStatementId("count"); //$NON-NLS-1$
        setSelectAllStatementId("findAll"); //$NON-NLS-1$
        setFindConditionStatementId(PropertyConfigurer.config.getString("find.condition")); //$NON-NLS-1$

    }

    public void setBlobColumnListId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID, s);
    }

    public void setBaseColumnListId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID, s);
    }

  
    public void setResultMapWithBLOBsId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID,
                s);
    }

    public void setBaseResultMapId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RESULT_MAP_ID, s);
    }


    public void setUpdateByPrimaryKeySelectiveStatementId(String s) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID,
                        s);
    }

    public void setUpdateByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

    

   

    public void setSelectByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

   

    public void setSelectAllStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID, s);
    }

  
    public void setInsertSelectiveStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID, s);
    }

    public void setInsertStatementId(String s) {
        internalAttributes.put(InternalAttribute.ATTR_INSERT_STATEMENT_ID, s);
    }

    public void setDeleteByPrimaryKeyStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID, s);
    }

    public String getFindConditionStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_FIND_CONDITION_STATEMENT_ID);
    }

    public String setFindConditionStatementId(String s) {
        return internalAttributes
                .put(InternalAttribute.ATTR_FIND_CONDITION_STATEMENT_ID, s);
    }
   
    public void setCountStatementId(String s) {
        internalAttributes.put(
                InternalAttribute.ATTR_COUNT_STATEMENT_ID, s);
    }

    public String getBlobColumnListId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BLOB_COLUMN_LIST_ID);
    }

    public String getBaseColumnListId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BASE_COLUMN_LIST_ID);
    }

  
    public String getResultMapWithBLOBsId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_RESULT_MAP_WITH_BLOBS_ID);
    }

    public String getBaseResultMapId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_BASE_RESULT_MAP_ID);
    }

    public String getUpdateByPrimaryKeySelectiveStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID);
    }

    public String getUpdateByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_UPDATE_BY_PRIMARY_KEY_STATEMENT_ID);
    }

   


    public String getSelectByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_BY_PRIMARY_KEY_STATEMENT_ID);
    }

    

    public String getSelectAllStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_SELECT_ALL_STATEMENT_ID);
    }

   
    public String getInsertSelectiveStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_INSERT_SELECTIVE_STATEMENT_ID);
    }

    public String getInsertStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_INSERT_STATEMENT_ID);
    }

    public String getDeleteByPrimaryKeyStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID);
    }

    public String getCountStatementId() {
        return internalAttributes
                .get(InternalAttribute.ATTR_COUNT_STATEMENT_ID);
    }

    protected String calculateJavaClientImplementationPackage() {
        JavaClientGeneratorConfiguration config = context
                .getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(config.getImplementationPackage())) {
            sb.append(config.getImplementationPackage());
        } else {
            sb.append(config.getTargetPackage());
        }

        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }
    
    private boolean isSubPackagesEnabled(PropertyHolder propertyHolder) {
        return isTrue(propertyHolder.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES));
    }

    protected String calculateJavaClientInterfacePackage() {
        JavaClientGeneratorConfiguration config = context
                .getJavaClientGeneratorConfiguration();
        if (config == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());

        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }

    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(calculateJavaClientInterfacePackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper"); //$NON-NLS-1$
        setMyBatis3JavaMapperType(sb.toString());
    }
    
    protected String calculateMyBatis3FallbackSqlMapNamespace() {
        StringBuilder sb = new StringBuilder();
        sb.append(calculateSqlMapPackage());
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Dao"); //$NON-NLS-1$
        return sb.toString();
    }

    protected String calculateJavaModelPackage() {
        JavaModelGeneratorConfiguration config = context
                .getJavaModelGeneratorConfiguration();

        StringBuilder sb = new StringBuilder();
        sb.append(config.getTargetPackage());
        sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));

        return sb.toString();
    }

    protected void calculateModelAttributes() {
        String pakkage = calculateJavaModelPackage();

        StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getBasicDomainObjectName());
        setBaseRecordType(sb.toString());
        
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(fullyQualifiedTable.getDomainObjectName());
        setRecordType(sb.toString());
    }

    protected String calculateSqlMapPackage() {
        StringBuilder sb = new StringBuilder();
        SqlMapGeneratorConfiguration config = context
                .getSqlMapGeneratorConfiguration();
        
        // config can be null if the Java client does not require XML
        if (config != null) {
            sb.append(config.getTargetPackage());
            sb.append(fullyQualifiedTable.getSubPackage(isSubPackagesEnabled(config)));
        }

        return sb.toString();
    }

    protected String calculateBasicMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getBasicDomainObjectName());
        sb.append("Mapper.xml"); //$NON-NLS-1$
        return sb.toString();
    }
    
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("Mapper.xml"); //$NON-NLS-1$
        return sb.toString();
    }
    
    protected String calculateReadMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("ReadMapper.xml"); //$NON-NLS-1$
        return sb.toString();
    }
    
    protected String calculateWriteMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(fullyQualifiedTable.getDomainObjectName());
        sb.append("WriteMapper.xml"); //$NON-NLS-1$
        return sb.toString();
    }

    protected String calculateIbatis2SqlMapNamespace() {
        return fullyQualifiedTable.getIbatis2SqlMapNamespace();
    }
    


    protected String calculateSqlMapFullyQualifiedRuntimeTableName() {
        return fullyQualifiedTable.getFullyQualifiedTableNameAtRuntime();
    }

    protected String calculateSqlMapAliasedFullyQualifiedRuntimeTableName() {
        return fullyQualifiedTable.getAliasedFullyQualifiedTableNameAtRuntime();
    }

    public String getFullyQualifiedTableNameAtRuntime() {
        return internalAttributes
                .get(InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }

    public String getAliasedFullyQualifiedTableNameAtRuntime() {
        return internalAttributes
                .get(InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME);
    }

    /**
     * This method can be used to initialize the generators before they will be
     * called.
     * 
     * This method is called after all the setX methods, but before
     * getNumberOfSubtasks(), getGeneratedJavaFiles, and getGeneratedXmlFiles.
     * 
     */
    public abstract void calculateGenerators();

    /**
     * This method should return a list of generated Java files related to this
     * table. This list could include various types of model classes, as well as
     * DAO classes.
     * 
     * @return the list of generated Java files for this table
     */
    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();

    /**
     * This method should return a list of generated XML files related to this
     * table. Most implementations will only return one file - the generated
     * SqlMap file.
     * 
     * @return the list of generated XML files for this table
     */
    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();

    /**
     * This method exists to give plugins the opportunity to replace the
     * calculated rules if necessary.
     * 
     * @param rules
     */
    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public TableConfiguration getTableConfiguration() {
        return tableConfiguration;
    }

    public void setDAOImplementationType(String DAOImplementationType) {
        internalAttributes.put(InternalAttribute.ATTR_DAO_IMPLEMENTATION_TYPE,
                DAOImplementationType);
    }

    public void setDAOInterfaceType(String DAOInterfaceType) {
        internalAttributes.put(InternalAttribute.ATTR_DAO_INTERFACE_TYPE,
                DAOInterfaceType);
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        internalAttributes.put(InternalAttribute.ATTR_PRIMARY_KEY_TYPE,
                primaryKeyType);
    }

    public void setBaseRecordType(String baseRecordType) {
        internalAttributes.put(InternalAttribute.ATTR_BASE_RECORD_TYPE,
                baseRecordType);
    }
    
    public void setRecordType(String recordType) {
        internalAttributes.put(InternalAttribute.ATTR_RECORD_TYPE,
                recordType);
    }

    public void setRecordWithBLOBsType(String recordWithBLOBsType) {
        internalAttributes.put(InternalAttribute.ATTR_RECORD_WITH_BLOBS_TYPE,
                recordWithBLOBsType);
    }

    public void setIbatis2SqlMapPackage(String sqlMapPackage) {
        internalAttributes.put(InternalAttribute.ATTR_IBATIS2_SQL_MAP_PACKAGE,
                sqlMapPackage);
    }

    public void setIbatis2SqlMapFileName(String sqlMapFileName) {
        internalAttributes.put(
                InternalAttribute.ATTR_IBATIS2_SQL_MAP_FILE_NAME,
                sqlMapFileName);
    }

    public void setIbatis2SqlMapNamespace(String sqlMapNamespace) {
        internalAttributes.put(
                InternalAttribute.ATTR_IBATIS2_SQL_MAP_NAMESPACE,
                sqlMapNamespace);
    }
    
    public void setMyBatis3FallbackSqlMapNamespace(String sqlMapNamespace) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_FALLBACK_SQL_MAP_NAMESPACE,
                sqlMapNamespace);
    }

    public void setSqlMapFullyQualifiedRuntimeTableName(
            String fullyQualifiedRuntimeTableName) {
        internalAttributes.put(
                InternalAttribute.ATTR_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                fullyQualifiedRuntimeTableName);
    }

    public void setSqlMapAliasedFullyQualifiedRuntimeTableName(
            String aliasedFullyQualifiedRuntimeTableName) {
        internalAttributes
                .put(
                        InternalAttribute.ATTR_ALIASED_FULLY_QUALIFIED_TABLE_NAME_AT_RUNTIME,
                        aliasedFullyQualifiedRuntimeTableName);
    }

    public String getMyBatis3XmlMapperPackage() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE);
    }

    public void setMyBatis3XmlMapperPackage(String mybatis3XmlMapperPackage) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_PACKAGE,
                mybatis3XmlMapperPackage);
    }

    public String getMyBatis3XmlMapperFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME);
    }

    public void setMyBatis3XmlMapperFileName(String mybatis3XmlMapperFileName) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME,
                mybatis3XmlMapperFileName);
    }
    
    
    public String getBasicMyBatis3XmlMapperFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_BASIC);
    }
    
    public void setBasicMyBatis3XmlMapperFileName(String mybatis3XmlMapperFileNameDao) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_BASIC,
                mybatis3XmlMapperFileNameDao);
    }
    
    public String getReadMyBatis3XmlMapperFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_READ);
    }
    
    public void setReadMyBatis3XmlMapperFileName(String mybatis3XmlMapperFileNameDao) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_READ,
                mybatis3XmlMapperFileNameDao);
    }
    
    
    public String getWriteMyBatis3XmlMapperFileName() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_WRITE);
    }
    
    public void setWriteMyBatis3XmlMapperFileName(String mybatis3XmlMapperFileNameDao) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_XML_MAPPER_FILE_NAME_WRITE,
                mybatis3XmlMapperFileNameDao);
    }

    public String getMyBatis3JavaMapperType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE);
    }

    public void setMyBatis3JavaMapperType(String mybatis3JavaMapperType) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_JAVA_MAPPER_TYPE,
                mybatis3JavaMapperType);
    }

    public String getMyBatis3SqlProviderType() {
        return internalAttributes
                .get(InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE);
    }

    public void setMyBatis3SqlProviderType(String mybatis3SqlProviderType) {
        internalAttributes.put(
                InternalAttribute.ATTR_MYBATIS3_SQL_PROVIDER_TYPE,
                mybatis3SqlProviderType);
    }
    
    public TargetRuntime getTargetRuntime() {
        return targetRuntime;
    }
    
    public boolean isImmutable() {
        Properties properties;
        
        if (tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_IMMUTABLE)) {
            properties = tableConfiguration.getProperties();
        } else {
            properties = context.getJavaModelGeneratorConfiguration().getProperties();
        }
        
        return isTrue(properties.getProperty(PropertyRegistry.ANY_IMMUTABLE));
    }
    
    public boolean isConstructorBased() {
        if (isImmutable()) {
            return true;
        }
        
        Properties properties;
        
        if (tableConfiguration.getProperties().containsKey(PropertyRegistry.ANY_CONSTRUCTOR_BASED)) {
            properties = tableConfiguration.getProperties();
        } else {
            properties = context.getJavaModelGeneratorConfiguration().getProperties();
        }
        
        return isTrue(properties.getProperty(PropertyRegistry.ANY_CONSTRUCTOR_BASED));
    }


    public Context getContext() {
        return context;
    }
}

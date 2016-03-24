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

package org.mybatis.generator.internal;

import org.mybatis.generator.api.DAOMethodNameCalculator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.internal.rules.Rules;

/**
 * @author Jeff Butler
 * 
 */
public class ExtendedDAOMethodNameCalculator implements DAOMethodNameCalculator {

    /**
     * 
     */
    public ExtendedDAOMethodNameCalculator() {
        super();
    }

    public String getInsertMethodName(IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        return sb.toString();
    }

    /**
     * 1. if this will be the only updateByPrimaryKey, then the result should be
     * updateByPrimaryKey. 2. If the other method is enabled, but there are
     * seperate base and blob classes, then the method name should be
     * updateByPrimaryKey 3. Else the method name should be
     * updateByPrimaryKeyWithoutBLOBs
     */
    public String getUpdateByPrimaryKeyWithoutBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();

        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

        if (!rules.generateUpdateByPrimaryKeyWithBLOBs()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else {
            sb.append("ByPrimaryKeyWithoutBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    /**
     * 1. if this will be the only updateByPrimaryKey, then the result should be
     * updateByPrimaryKey. 2. If the other method is enabled, but there are
     * seperate base and blob classes, then the method name should be
     * updateByPrimaryKey 3. Else the method name should be
     * updateByPrimaryKeyWithBLOBs
     */
    public String getUpdateByPrimaryKeyWithBLOBsMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        Rules rules = introspectedTable.getRules();

        if (!rules.generateUpdateByPrimaryKeyWithoutBLOBs()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else if (rules.generateRecordWithBLOBsClass()) {
            sb.append("ByPrimaryKey"); //$NON-NLS-1$
        } else {
            sb.append("ByPrimaryKeyWithBLOBs"); //$NON-NLS-1$
        }

        return sb.toString();
    }

   

    public String getDeleteByPrimaryKeyMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("delete"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$

        return sb.toString();
    }

  
    public String getSelectByPrimaryKeyMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKey"); //$NON-NLS-1$

        return sb.toString();
    }

    public String getUpdateByPrimaryKeySelectiveMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("update"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());
        sb.append("ByPrimaryKeySelective"); //$NON-NLS-1$

        return sb.toString();
    }

    public String getCountMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("count"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        return sb.toString();
    }
    
    public String getSelectAllMethodName(
            IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("selectAll"); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable()
                .getDomainObjectName());

        return sb.toString();
    }
}

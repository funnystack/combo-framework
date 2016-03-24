/*
 *  Copyright 2008 The Apache Software Foundation
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

import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

/**
 * This interface defines methods that will be called at different times during
 * the code generation process. These methods can be used to extend or modify
 * the generated code. Clients may implement this interface in its entirety, or
 * extend the PluginAdapter (highly recommended).
 * <p>
 * Plugins have a lifecycle. In general, the lifecycle is this:
 * 
 * <ol>
 * <li>The setXXX methods are called one time</li>
 * <li>The validate method is called one time</li>
 * <li>The initialized method is called for each introspected table</li>
 * <li>The clientXXX methods are called for each introspected table</li>
 * <li>The providerXXX methods are called for each introspected table</li>
 * <li>The modelXXX methods are called for each introspected table</li>
 * <li>The sqlMapXXX methods are called for each introspected table</li>
 * <li>The contextGenerateAdditionalJavaFiles(IntrospectedTable) method is
 * called for each introspected table</li>
 * <li>The contextGenerateAdditionalXmlFiles(IntrospectedTable) method is called
 * for each introspected table</li>
 * <li>The contextGenerateAdditionalJavaFiles() method is called one time</li>
 * <li>The contextGenerateAdditionalXmlFiles() method is called one time</li>
 * </ol>
 * 
 * Plugins are related to contexts - so each context will have its own set of
 * plugins. If the same plugin is specified in multiple contexts, then each
 * context will hold a unique instance of the plugin.
 * <p>
 * Plugins are called, and initialized, in the same order they are specified in
 * the configuration.
 * <p>
 * The clientXXX, modelXXX, and sqlMapXXX methods are called by the code
 * generators. If you replace the default code generators with other
 * implementations, these methods may not be called.
 * 
 * @author Jeff Butler
 * @see PluginAdapter
 * 
 */
public interface Plugin {
    public enum ModelClassType {
        PRIMARY_KEY, BASE_RECORD, RECORD_WITH_BLOBS
    }

    /**
     * Set the context under which this plugin is running
     * 
     * @param context
     */
    void setContext(Context context);

    /**
     * Set properties from the plugin configuration
     * 
     * @param properties
     */
    void setProperties(Properties properties);

    /**
     * This method is called just before the getGeneratedXXXFiles methods are
     * called on the introspected table. Plugins can implement this method to
     * override any of the default attributes, or change the results of database
     * introspection, before any code generation activities occur. Attributes
     * are listed as static Strings with the prefix ATTR_ in IntrospectedTable.
     * <p>
     * A good example of overriding an attribute would be the case where a user
     * wanted to change the name of one of the generated classes, change the
     * target package, or change the name of the generated SQL map file.
     * <p>
     * <b>Warning:</b> Anything that is listed as an attribute should not be
     * changed by one of the other plugin methods. For example, if you want to
     * change the name of a generated example class, you should not simply
     * change the Type in the <code>modelExampleClassGenerated()</code> method.
     * If you do, the change will not be reflected in other generated artifacts.
     * 
     * @param introspectedTable
     */
    void initialized(IntrospectedTable introspectedTable);

    /**
     * This method is called after all the setXXX methods are called, but before
     * any other method is called. This allows the plugin to determine whether
     * it can run or not. For example, if the plugin requires certain properties
     * to be set, and the properties are not set, then the plugin is invalid and
     * will not run.
     * 
     * @param warnings
     *            add strings to this list to specify warnings. For example, if
     *            the plugin is invalid, you should specify why. Warnings are
     *            reported to users after the completion of the run.
     * @return true if the plugin is in a valid state. Invalid plugins will not
     *         be called
     */
    boolean validate(List<String> warnings);

    /**
     * This method can be used to generate any additional Java file needed by
     * your implementation. This method is called once, after all other Java
     * files have been generated.
     * 
     * @return a List of GeneratedJavaFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles();

    /**
     * This method can be used to generate additional Java files needed by your
     * implementation that might be related to a specific table. This method is
     * called once for every table in the configuration.
     * 
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return a List of GeneratedJavaFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
            IntrospectedTable introspectedTable);

    /**
     * This method can be used to generate any additional XML file needed by
     * your implementation. This method is called once, after all other XML
     * files have been generated.
     * 
     * @return a List of GeneratedXmlFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles();

    /**
     * This method can be used to generate additional XML files needed by your
     * implementation that might be related to a specific table. This method is
     * called once for every table in the configuration.
     * 
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return a List of GeneratedXmlFiles - these files will be saved
     *         with the other files from this run.
     */
    List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
            IntrospectedTable introspectedTable);

    /**
     * This method is called when the entire client has been generated.
     * Implement this method to add additional methods or fields to a generated
     * client interface or implementation.
     * 
     * @param interfaze
     *            the generated interface if any, may be null
     * @param topLevelClass
     *            the generated implementation class if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return true if the interface should be generated, false if the generated
     *         interface should be ignored. In the case of multiple plugins, the
     *         first plugin returning false will disable the calling of further
     *         plugins.
     */
    boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);


   
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientInsertMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
    boolean clientInsertMethodGenerated(Method method, Interface interfaze,
            IntrospectedTable introspectedTable);
    
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
    boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);

    boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    /**
     * 
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    
    boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
   
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    
    
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientCountMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
    boolean clientCountMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientSelectAllMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    
    boolean clientSelectAllMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    
   
    /**
     * 
     * @param method
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
    boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
            Interface interfaze, IntrospectedTable introspectedTable);
    
  

    

    boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

    
    boolean modelGetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

    
    boolean modelSetterMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable, ModelClassType modelClassType);

  
    boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    
    boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    
    boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    

    
    boolean sqlMapGenerated(GeneratedXmlFile sqlMap,
            IntrospectedTable introspectedTable);

    
    boolean sqlMapDocumentGenerated(Document document,
            IntrospectedTable introspectedTable);

    
    boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);


    
    boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);
   
    boolean sqlMapBaseColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    
    boolean sqlMapBlobColumnListElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    
    boolean sqlMapInsertElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

   
    boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    boolean sqlMapCountElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);
    
    boolean sqlMapSelectAllElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    
    boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
            IntrospectedTable introspectedTable);

    boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    
    boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    
    boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable);

    
    
    
    
    boolean providerGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    
    boolean providerApplyWhereMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);


    boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
            TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
}

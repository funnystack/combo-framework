package com.funny.combo.codegen.core.resolver;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * Define JavaTypeResolverImpl.
 * <p>Created with IntelliJ IDEA on 03/12/2016 14:21.</p>
 *
 * @version 1.0
 */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {
    public JavaTypeResolverImpl() {
        super();
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("INTEGER",
                new FullyQualifiedJavaType(Integer.class.getName())));

        typeMap.put(Types.BIT, new JdbcTypeInformation("INTEGER",
                new FullyQualifiedJavaType(Integer.class.getName())));

        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("INTEGER",
                new FullyQualifiedJavaType(Integer.class.getName())));

        typeMap.put(Types.BLOB, new JdbcTypeInformation("VARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));

        typeMap.put(Types.CLOB, new JdbcTypeInformation("VARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));


    }



}

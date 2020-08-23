package com.funny.combo.codegen.service;

import com.funny.combo.codegen.core.config.GlobalConfig;
import com.funny.combo.codegen.service.impl.MySQLTableHandler;
import com.funny.combo.codegen.service.impl.OracleTableHandler;
import com.funny.combo.core.exception.SysException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class CodegenServiceFactory {

    @Resource
    private MySQLTableHandler mySQLTableHandler;

    @Resource
    private OracleTableHandler oracleTableHandler;

    public AbstractQueryTable getHandlerByType(String databaseType) {
        switch (databaseType) {
            case GlobalConfig.DB_ORACLE:
                return oracleTableHandler;
            case GlobalConfig.DB_MYSQL:
                return mySQLTableHandler;
            default:
                throw new SysException("not support database type");
        }
    }

}

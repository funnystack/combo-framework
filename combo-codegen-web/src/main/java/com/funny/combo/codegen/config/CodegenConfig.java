package com.funny.combo.codegen.config;

import com.funny.combo.codegen.core.config.ComboCodeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodegenConfig {

    @Value("${combo.base-dao}")
    private String baseDaoName;

    @Value("${combo.base-entity}")
    private String baseEntityName;

    @Value("${ignored.prefix}")
    private String prefix;

    @Value("${combo.sql.insert}")
    private String sqlInsert;

    @Value("${combo.sql.update}")
    private String sqlUpdate;

    @Value("${combo.sql.find}")
    private String sqlFind;

    @Value("${combo.sql.findList}")
    private String sqlFindList;

    @Value("${combo.sql.count}")
    private String sqlCount;

    @Value("${combo.sql.delete}")
    private String sqlDelete;

    @Value("${combo.encrypt.columns}")
    private String encryptColumns;
    @Value("${combo.hash.columns}")
    private String hashColumns;

    @Value("${combo.encrypt.handler}")
    private String encryptHandler;
    @Value("${combo.hash.handler}")
    private String hashHandler;

    @Value("${combo.entity.id}")
    private String entityId;
    @Value("${combo.entity.create-time}")
    private String entityCreate;
    @Value("${combo.entity.modify-time}")
    private String entityUpdate;
    @Value("${combo.entity.is-del}")
    private String entityIsDel;


    @Bean
    public ComboCodeConfig comboCodeConfig(){
        ComboCodeConfig comboCodeConfig = new ComboCodeConfig();
        comboCodeConfig.setBaseDaoName(baseDaoName);
        comboCodeConfig.setBaseEntityName(baseEntityName);
        comboCodeConfig.setSqlCount(sqlCount);
        comboCodeConfig.setSqlDelete(sqlDelete);
        comboCodeConfig.setSqlFind(sqlFind);
        comboCodeConfig.setSqlFindList(sqlFindList);
        comboCodeConfig.setSqlInsert(sqlInsert);
        comboCodeConfig.setSqlUpdate(sqlUpdate);
        comboCodeConfig.setPrefix(prefix);

        comboCodeConfig.setEncryptColumns(encryptColumns);
        comboCodeConfig.setHashColumns(hashColumns);
        comboCodeConfig.setEncryptHandler(encryptHandler);
        comboCodeConfig.setHashHandler(hashHandler);

        comboCodeConfig.setEntityId(entityId);
        comboCodeConfig.setEntityCreate(entityCreate);
        comboCodeConfig.setEntityUpdate(entityUpdate);
        comboCodeConfig.setEntityIsDel(entityIsDel);

        return comboCodeConfig;





    }


}

package com.funny.autocode.service;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.funny.autocode.common.SystemConstants.*;

@Service
public class InitService implements ApplicationContextAware {

    @Value("${name.domain}")
    private String nameDomain;

    @Value("${name.dao}")
    private String nameDao;

    @Value("${name.mapper}")
    private String nameMapper;

    @Value("${parent.dao}")
    private String parentDao;

    @Value("${parent.model}")
    private String parentDomain;

    @Value("${ignored.prefix}")
    private String prefix;

    @Value("${sql.insert}")
    private String sqlInsert;

    @Value("${sql.update}")
    private String sqlUpdate;

    @Value("${sql.find}")
    private String sqlFind;

    @Value("${sql.findList}")
    private String sqlFindList;

    @Value("${sql.count}")
    private String sqlCount;

    @Value("${sql.delete}")
    private String sqlDelete;


    public static Map<String, String> propMap = Maps.newConcurrentMap();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        propMap.put(NAME_DAO, nameDao);
        propMap.put(NAME_MAPPER, nameMapper);
        propMap.put(NAME_DOMAIN, nameDomain);

        propMap.put(PARENT_DOMAIN, parentDao);
        propMap.put(PARENT_DAO, parentDomain);

        propMap.put(PREFIX, prefix);
        propMap.put(SQL_INSERT, sqlInsert);
        propMap.put(SQL_UPDATE, sqlUpdate);
        propMap.put(SQL_FIND, sqlFind);
        propMap.put(SQL_FIND_LIST, sqlFindList);
        propMap.put(SQL_COUNT, sqlCount);
        propMap.put(SQL_DELETE, sqlDelete);


        propMap.put(TEMP_PATH, sqlDelete);

    }
}

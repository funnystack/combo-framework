package com.auto.mybatis.code.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.mybatis.generator.config.Context;
import org.springframework.stereotype.Service;

import com.auto.mybatis.code.po.Table;

@Service
public interface CodeService {
    /**
     * 根据数据库连接，用户名，密码查询数据库的表
     * 
     * @param url
     * @param usr
     * @param pas
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    List<Table> getOracleTables(String url, String usr, String pas);

    List<Table> getMysqlTables(String url, String usr, String pas);

    /**
     * 按普通方式生产
     * 
     * @param context
     * @return
     */
    Map<File, String> generateCommonFiles(Context context);

    /**
     * 按读写方式生产
     * 
     * @param context
     * @return
     */
    Map<File, String> generateReadWriteFiles(Context context);
}

package cn.com.autohome.mall.autocode.service;

import java.sql.SQLException;
import java.util.List;

import cn.com.autohome.mall.autocode.po.Table;

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
}

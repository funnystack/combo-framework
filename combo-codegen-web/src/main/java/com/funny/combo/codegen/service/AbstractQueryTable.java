package com.funny.combo.codegen.service;

import com.funny.combo.codegen.po.CodeURL;
import com.funny.combo.codegen.po.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractQueryTable {
    /**
     * 根据数据库连接，用户名，密码查询数据库的表
     * @param codeURL
     * @return
     */
    public abstract List<Table> getDatabaseTable(CodeURL codeURL);

    protected abstract boolean checkUrl(String url);

    protected abstract String getQueryTableSQL(String url);

    protected void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

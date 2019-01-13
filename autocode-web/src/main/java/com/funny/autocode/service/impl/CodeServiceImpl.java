package com.funny.autocode.service.impl;

import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import com.funny.autocode.po.Table;
import com.funny.autocode.service.CodeService;
import com.funny.autocode.util.ContextUtils;

@Service
public class CodeServiceImpl implements CodeService {

    public List<Table> getOracleTables(String url, String usr, String pas) {
        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url, usr, pas);
            PreparedStatement st = con.prepareStatement(ContextUtils.getOracleSql(usr));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table =
                        new Table(rs.getString("TABLE_NAME"), rs.getString("COMMENTS"), rs.getString("COLUMN_NAME"));
                tables.add(table);
            }
            closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Table> getMysqlTables(String url, String usr, String pas) {

        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, usr, pas);
            PreparedStatement st = con.prepareStatement(ContextUtils.getMysqlSql(url));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table = new Table(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT"),
                        rs.getString("COLUMN_NAME"));
                tables.add(table);
            }
            closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tables;

    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

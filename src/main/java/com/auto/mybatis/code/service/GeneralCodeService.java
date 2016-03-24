package com.auto.mybatis.code.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.db.ConnectionFactory;

import com.auto.mybatis.code.po.Table;
import com.auto.mybatis.code.util.ContextUtils;
import com.google.common.collect.Lists;

public class GeneralCodeService {
    protected Connection con;

    protected Connection getConnection(JDBCConnectionConfiguration jdbcConnectionConfiguration) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection(jdbcConnectionConfiguration);
        return connection;
    }

    protected void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected List<Table> getOracleTables(String url, String usr, String pas) throws ClassNotFoundException,
            SQLException {
        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();
        Class.forName("oracle.jdbc.driver.OracleDriver");
        try {
            con = DriverManager.getConnection(url, usr, pas);
            PreparedStatement st = con.prepareStatement(ContextUtils.getOracleSql(usr));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table =
                        new Table(rs.getString("TABLE_NAME"), rs.getString("COMMENTS"), rs.getString("COLUMN_NAME"));
                tables.add(table);
            }
            con.close();
        } catch (SQLException e) {
            con.close();
            e.printStackTrace();
        }
        return tables;
    }

    protected List<Table> getMysqlTables(String url, String usr, String pas) throws ClassNotFoundException,
            SQLException {
        ResultSet rs = null;
        List<Table> tables = Lists.newArrayList();
        Class.forName("com.mysql.jdbc.Driver");
        try {
            con = DriverManager.getConnection(url, usr, pas);
            PreparedStatement st = con.prepareStatement(ContextUtils.getMysqlSql(url));
            // 查询，得出结果集
            rs = st.executeQuery();
            while (rs.next()) {
                Table table =
                        new Table(rs.getString("TABLE_NAME"), rs.getString("TABLE_COMMENT"), rs.getString("COLUMN_NAME"));
                tables.add(table);
            }
            con.close();
        } catch (SQLException e) {
            con.close();
            e.printStackTrace();
        }
        return tables;
    }

   
}

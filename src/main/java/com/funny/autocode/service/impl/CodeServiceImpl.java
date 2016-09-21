package com.funny.autocode.service.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.funny.autocode.service.CodeService;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.db.DatabaseIntrospector;
import org.springframework.stereotype.Service;

import com.funny.autocode.po.Table;
import com.funny.autocode.util.ContextUtils;
import com.google.common.collect.Lists;

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

    public Map<File, String> generateCommonFiles(Context context) {
        Map<File, String> files = new HashMap<File, String>();
        IntrospectedTable introspectedTable = getIntrospectedTables(context);
        if (introspectedTable != null) {
            introspectedTable.initialize();
            introspectedTable.calculateGenerators();
        }
        List<GeneratedJavaFile> generatedJavaFiles = generatedJavaFiles(introspectedTable);
        List<GeneratedXmlFile> generatedXmlFiles = generatedXmlFiles(introspectedTable);

        Set<String> projects = new HashSet<String>();
        for (GeneratedXmlFile gxf : generatedXmlFiles) {
            projects.add(gxf.getTargetProject());
            File targetFile;
            String source;
            try {
                File directory = getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
                targetFile = new File(directory, gxf.getFileName());
                source = gxf.getFormattedContent();
            } catch (ShellException e) {
                e.printStackTrace();
                continue;
            }
            files.put(targetFile, source);
        }

        for (GeneratedJavaFile gjf : generatedJavaFiles) {
            projects.add(gjf.getTargetProject());
            File targetFile;
            String source;
            try {
                File directory = getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
                targetFile = new File(directory, gjf.getFileName());
                source = gjf.getFormattedContent();
                files.put(targetFile, source);
            } catch (ShellException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public Map<File, String> generateReadWriteFiles(Context context) {
        return null;
    }

    private IntrospectedTable getIntrospectedTables(Context context) {
        IntrospectedTable table = null;
        JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(context);
        Connection connection = null;
        try {
            connection = getConnection(context.getJdbcConnectionConfiguration());
            DatabaseIntrospector databaseIntrospector =
                    new DatabaseIntrospector(context, connection.getMetaData(), javaTypeResolver);
            table = databaseIntrospector.introspectTables(context.getTabconfig());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return table;
    }

    private Connection getConnection(JDBCConnectionConfiguration jdbcConnectionConfiguration) throws SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection(jdbcConnectionConfiguration);
        return connection;
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

    private List<GeneratedJavaFile> generatedJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
        generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        return generatedJavaFiles;
    }

    private List<GeneratedXmlFile> generatedXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<GeneratedXmlFile>();
        generatedXmlFiles = introspectedTable.getGeneratedXmlFiles();
        return generatedXmlFiles;
    }

    private List<GeneratedXmlFile> readWriteXmlFiles() {
        List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<GeneratedXmlFile>();

        return generatedXmlFiles;
    }

    private List<GeneratedJavaFile> readWriteJavaFiles() {
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<GeneratedJavaFile>();

        return generatedJavaFiles;
    }

    protected File getDirectory(String targetProject, String targetPackage) throws ShellException {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            project.mkdir();
        }
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException("创建失败" + project);
            }
        }

        return directory;
    }


}

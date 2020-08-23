package com.funny.combo.codegen.controller;

import com.funny.combo.codegen.core.AutoCodeConfigurationParser;
import com.funny.combo.codegen.core.AutoCodeMybatisGenerator;
import com.funny.combo.codegen.core.config.AutoCodeHolder;
import com.funny.combo.codegen.core.config.ComboCodeConfig;
import com.funny.combo.codegen.core.config.GlobalConfig;
import com.funny.combo.codegen.po.CodeURL;
import com.funny.combo.codegen.po.Table;
import com.funny.combo.codegen.service.AbstractQueryTable;
import com.funny.combo.codegen.service.CodegenServiceFactory;
import com.funny.combo.codegen.util.ContextUtils;
import com.funny.combo.codegen.util.FileUtils;
import com.funny.combo.codegen.util.ZipUtil;
import com.funny.combo.core.result.CommonResult;
import com.google.common.collect.Maps;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alibaba.druid.util.JdbcConstants.MYSQL_DRIVER_6;

@RestController
public class CodeController {

    @Resource
    private CodegenServiceFactory codegenServiceFactory;

    @Resource
    private ComboCodeConfig comboCodeConfig;

    @RequestMapping("/getTargetDatabaseTables")
    public CommonResult getConnection(CodeURL codeURL) throws ClassNotFoundException, SQLException {
        if (codeURL.getUsr() == null || codeURL.getPas() == null) {
            return CommonResult.fail("usr is null or pas is null");
        }

        if (!ContextUtils.checkDataBase(codeURL.getUrl())) {
            return CommonResult.fail("目前只支持oracle和mysql数据库！");
        }
        try {
            String databaseType = ContextUtils.getDatabaseType(codeURL.getUrl());
            AbstractQueryTable abstractQueryTable = codegenServiceFactory.getHandlerByType(databaseType);
            List<Table> tablelist = abstractQueryTable.getDatabaseTable(codeURL);
            return CommonResult.succ(tablelist);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.fail(e.getMessage());
        }
    }

    @RequestMapping("/getNames")
    public Map<String, Object> getNames(String packageName, String moduleName) {
        Map<String, Object> res = Maps.newHashMap();
        String entityName =
                packageName + "." + moduleName + ".entity.XXXX";
        String daoName =
                packageName + "." + moduleName + ".dao.XXXX";
        res.put("entityName", entityName);
        res.put("daoName", daoName);
        return res;

    }

    /*
     * @param request
     * @param response
     * @param globalConfig
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws XMLParserException
     * @throws InterruptedException
     * @throws InvalidConfigurationException
     */
    @RequestMapping("/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response, GlobalConfig globalConfig) throws ClassNotFoundException,
            SQLException, IOException, XMLParserException, InterruptedException, InvalidConfigurationException {

        File path = new File(System.getProperty("java.io.tmpdir")+ File.separator+ "code");
        String targetpath = path.getAbsolutePath();

        Set<String> fullyqualifiedTables = AutoCodeConfigurationParser.getTables(globalConfig.getTableNames());


        globalConfig.setVerbose(true);
        globalConfig.setOverwrite(true);
        globalConfig.setTargetDirectory(targetpath);

        String databaseType = ContextUtils.getDatabaseType(globalConfig.getJdbcURL());
        if(GlobalConfig.DB_MYSQL.equalsIgnoreCase(databaseType)){
            globalConfig.setJdbcDriver(MYSQL_DRIVER_6);
            globalConfig.setJdbcURL(globalConfig.getJdbcURL()+GlobalConfig.MYSQL_CONCAT_URL);
        }
        globalConfig.setComboCodeConfig(comboCodeConfig);
        AutoCodeHolder.setConfig(globalConfig);
        AutoCodeConfigurationParser cp = new AutoCodeConfigurationParser(globalConfig);
        Configuration config = cp.getConfiguration();
        AutoCodeMybatisGenerator myBatisGenerator = new AutoCodeMybatisGenerator(config, null, null);
        myBatisGenerator.generate(null, null, fullyqualifiedTables);

        Map<File, String> files = Maps.newHashMap();
        List<GeneratedJavaFile> generatedJavaFileList = myBatisGenerator.getGeneratedJavaFiles();
        List<GeneratedXmlFile> generatedXmlFileList = myBatisGenerator.getGeneratedXmlFiles();
        File directory = new File(targetpath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        for (GeneratedXmlFile gxf : generatedXmlFileList) {
            File targetFile = new File(directory, gxf.getFileName());
            String source = gxf.getFormattedContent();
            files.put(targetFile, source);
        }
        for (GeneratedJavaFile gjf : generatedJavaFileList) {
            File targetFile = new File(directory, gjf.getFileName());
            String source = gjf.getFormattedContent();
            files.put(targetFile, source);
        }
        try {
            writeFile(files);
            downloadFile(globalConfig.getModuleName(), targetpath, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(Map<File, String> files) throws IOException {
        for (File file : files.keySet()) {
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter osw;
            osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(files.get(file));
            bw.close();
        }
    }

    private void downloadFile(String zipfilename, String path, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        FileUtils.setFileDownloadHeader(request, response, zipfilename + ".zip");
        OutputStream zos = response.getOutputStream();

        String zippath = path + File.separator;
        File zippathFile = new File(zippath);
        if (!zippathFile.exists()) {
            zippathFile.mkdir();
        }

        String zipfilepath = "code";
        ZipUtil.zip(zippath, zipfilepath);

        // response读取压缩文件
        File temp = new File(zipfilepath);
        InputStream in = new FileInputStream(temp);
        byte[] buffer = new byte[1024];
        int r = 0;
        while ((r = in.read(buffer)) != -1) {
            zos.write(buffer, 0, r);
        }
        in.close();
        // 删除压缩文件
        temp.delete();
        FileUtils.deleteDirectory(zippath);
        zos.flush();
        zos.close();
    }
}

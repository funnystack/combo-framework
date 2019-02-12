package com.funny.autocode.controller;

import com.funny.autocode.common.JsonResult;
import com.funny.autocode.common.SystemConstants;
import com.funny.autocode.core.AutoCodeConfigurationParser;
import com.funny.autocode.core.AutoCodeMybatisGenerator;
import com.funny.autocode.core.config.GlobalConfig;
import com.funny.autocode.po.Table;
import com.funny.autocode.service.CodeService;
import com.funny.autocode.util.ContextUtils;
import com.funny.autocode.util.FileUtils;
import com.funny.autocode.util.ZipUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.funny.autocode.common.SystemConstants.*;
import static com.funny.autocode.service.InitService.propMap;

@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;

    @RequestMapping("/getTargetDatabaseTables")
    @ResponseBody
    public JsonResult getConnection(String url, String usr, String pas) throws ClassNotFoundException, SQLException {
        JsonResult jsonResult = new JsonResult();
        if (!ContextUtils.checkDataBase(url)) {
            jsonResult.setFail("目前只支持oracle和mysql数据库！");
            return jsonResult;
        }

        String databaseType = ContextUtils.getDatabaseType(url);
        List<Table> tablelist = Lists.newArrayList();
        if (databaseType.equals(SystemConstants.DB_ORACLE)) {
            if (!ContextUtils.checkOracleUrl(url)) {
                jsonResult.setFail("请检查oracle数据库连接错误");
                return jsonResult;
            }
            tablelist = codeService.getOracleTables(url, usr, pas);
            jsonResult.setSuccess(tablelist);
        }
        if (databaseType.equals(SystemConstants.DB_MYSQL)) {
            if (!ContextUtils.checkMysqlUrl(url)) {
                jsonResult.setFail("请检查mysql数据库连接");
                return jsonResult;
            }
            tablelist = codeService.getMysqlTables(url, usr, pas);
            jsonResult.setSuccess(tablelist);
        }
        return jsonResult;

    }

    @RequestMapping("/getNames")
    @ResponseBody
    public Map<String, Object> getNames(String packageName, String moduleName) {
        Map<String, Object> res = Maps.newHashMap();
        String entityName =
                packageName + "." + propMap.get(NAME_DOMAIN) + "." + moduleName + ".XXXX";
        String daoName =
                packageName + "." + propMap.get(NAME_DAO) + "." + moduleName + ".XXXX";
        res.put("entityName", entityName);
        res.put("daoName", daoName);
        return res;

    }

    @RequestMapping("/getCode")
    @ResponseBody
    public void getCode(HttpServletRequest request, HttpServletResponse response, GlobalConfig globalConfig) throws ClassNotFoundException,
            SQLException, IOException, XMLParserException, InterruptedException, InvalidConfigurationException {

        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        System.out.println(path.getAbsolutePath());
        String targetpath = path.getAbsolutePath();

        Set<String> fullyqualifiedTables = AutoCodeConfigurationParser.getTables(globalConfig.getTableNames());

        globalConfig.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        globalConfig.setVerbose(true);
        globalConfig.setOverwrite(true);
        globalConfig.setTargetDirectory(targetpath);


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

        //todo
        String zipfilepath = propMap.get(TEMP_PATH);
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

package cn.com.autohome.mall.autocode.controller;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.autohome.mall.autocode.core.AutoHomeMybatisGenerator;
import cn.com.autohome.mall.autocode.core.AutohomeConfigurationParser;
import cn.com.autohome.mall.autocode.core.config.AutohomeConfig;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.com.autohome.mall.autocode.common.JsonResult;
import cn.com.autohome.mall.autocode.common.SystemConstants;
import cn.com.autohome.mall.autocode.po.Table;
import cn.com.autohome.mall.autocode.service.CodeService;
import cn.com.autohome.mall.autocode.util.ContextUtils;
import cn.com.autohome.mall.autocode.util.FileUtils;
import cn.com.autohome.mall.autocode.util.PropertyConfigurer;
import cn.com.autohome.mall.autocode.util.ZipUtil;

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
    public Map<String, Object> getNames(HttpServletRequest request, String packagename, String modelname) {
        Map<String, Object> res = Maps.newHashMap();
        String modelName =
                packagename + "." + PropertyConfigurer.config.getString("domain.name") + "." + modelname + ".XXXX";
        String daoName =
                packagename + "." + PropertyConfigurer.config.getString("dao.name") + "." + modelname + ".XXXX";
        res.put("modelName", modelName);
        res.put("daoName", daoName);
        return res;

    }

    @RequestMapping("/getCode")
    @ResponseBody
    public void getCode(HttpServletRequest request, HttpServletResponse response, String c_url, String c_user,
                        String c_pass, String packagename, String modelname, String c_table) throws ClassNotFoundException,
            SQLException, IOException, XMLParserException, InterruptedException, InvalidConfigurationException {
        String targetpath = PropertyConfigurer.config.getString("temp_path");
        File targetPathFile = new File(targetpath);
        if (!targetPathFile.exists()) {
            targetPathFile.mkdir();
        }
        Set<String> fullyqualifiedTables = AutohomeConfigurationParser.getTables(c_table);

        AutohomeConfig autohomeConfig = new AutohomeConfig(targetpath, null, null, true, true, "com.mysql.jdbc.Driver",
                c_url, c_user, c_pass, c_table, "ERP,CC", packagename, modelname);

        AutohomeConfigurationParser cp = new AutohomeConfigurationParser(autohomeConfig);
        Configuration config = cp.getConfiguration();
        AutoHomeMybatisGenerator myBatisGenerator = new AutoHomeMybatisGenerator(config, null, null);
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
            downloadFile(modelname, targetpath, request, response);
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

        String zipfilepath = PropertyConfigurer.config.getString("temp_zip_file");
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

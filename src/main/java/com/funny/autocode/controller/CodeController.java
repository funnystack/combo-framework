package com.funny.autocode.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.funny.autocode.common.JsonResult;
import com.funny.autocode.common.SystemConstants;
import com.funny.autocode.util.FileUtils;
import org.mybatis.generator.config.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funny.autocode.po.Table;
import com.funny.autocode.service.CodeService;
import com.funny.autocode.util.ContextUtils;
import com.funny.autocode.util.PropertyConfigurer;
import com.funny.autocode.util.ZipUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;

    @RequestMapping("/getTargetDatabaseTables")
    @ResponseBody
    public JsonResult getConnection(HttpServletRequest request, String url, String usr, String pas)
            throws ClassNotFoundException, SQLException {
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
            String c_pass, String packagename, String modelname, String c_table, String c_style, String c_id)
                    throws ClassNotFoundException, SQLException, IOException {
        String targetpath = PropertyConfigurer.config.getString("temp_path");
        Context context =
                ContextUtils.initContext(c_url, c_user, c_pass, c_table, packagename, modelname, targetpath, c_id);
        Map<File, String> files = codeService.generateCommonFiles(context);
        writeFile(files);
        try {
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

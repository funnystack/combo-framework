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

import org.mybatis.generator.config.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funny.autocode.po.Table;
import com.funny.autocode.service.CodeService;
import com.funny.autocode.util.ContextUtils;
import com.funny.autocode.util.FileUtils;
import com.funny.autocode.util.PropertyConfigurer;
import com.funny.autocode.util.ZipUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
public class CodeController {
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";
    private static final String RESULT = "result";

    @Autowired
    private CodeService codeService;

    @RequestMapping("/getTargetDatabaseTables")
    @ResponseBody
    public Map<String, Object> getConnection(HttpServletRequest request, String url, String usr, String pas)
            throws ClassNotFoundException, SQLException {
        Map<String, Object> result = Maps.newHashMap();
        if (!ContextUtils.checkDataBase(url)) {
            result.put(SUCCESS, false);
            result.put(MESSAGE, "目前只支持oracle和mysql数据库！");
            return result;
        }

        String db = ContextUtils.getDatabaseType(url);
        List<Table> tablelist = Lists.newArrayList();
        if (db.equals("oracle") && ContextUtils.checkOracleUrl(url)) {
            tablelist = codeService.getOracleTables(url, usr, pas);
            result.put(SUCCESS, true);
            result.put(RESULT, tablelist);
        }
        if (db.equals("mysql") && ContextUtils.checkMysqlUrl(url)) {
            tablelist = codeService.getMysqlTables(url, usr, pas);
            result.put(SUCCESS, true);
            result.put(RESULT, tablelist);
        }
        return result;

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
        Map<File, String> files = null;
        if ("1".equals(c_style)) {
            files = codeService.generateCommonFiles(context);
        } else if ("2".equals(c_style)) {
            files = codeService.generateReadWriteFiles(context);
        } else {
            System.out.println("未选择生产代码方式");
            return;
        }
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

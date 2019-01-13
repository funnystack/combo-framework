package com.funny.autocode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具类将文件压缩到指定文件夹中
 * 
 * @author caizhiqin
 *
 */
public class ZipUtil {

    /**
     * 
     * @param inputFileName 输入一个文件夹
     * @param zipFileName 输出一个压缩文件夹，打包后文件名字
     * @throws Exception
     */
    public static void zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    private static void zip(String zipFileName, File inputFile) throws Exception {

        // 可以设置编码，必须是JDK1.7以上，否则出现文件名乱码
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName), Charset.forName("UTF-8"));
        zip(out, inputFile, "");
        out.flush();
        out.closeEntry();
        out.close();
    }

    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) { // 判断是否为目录
            File[] fl = f.listFiles();
            // out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        }

        else {
            // 压缩目录中的所有文件
            ZipEntry zip = new ZipEntry(base);
            out.putNextEntry(zip);
            FileInputStream in = null;
            try {
                in = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                f.createNewFile();
                in = new FileInputStream(f);
            }
            System.out.println(f.getPath());
            int length = 0;
            while ((length = in.read()) != -1) {
                out.write(length);
            }
            in.close();
        }
    }

}
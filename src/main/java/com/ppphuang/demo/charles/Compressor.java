package com.ppphuang.demo.charles;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Compressor {

    private static final int BUFFER = 8192;

    public static void compress(String pathName, String srcPathName) {
        File fileName = new File(pathName);
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            File[] sourceFiles = file.listFiles();
            if (null == sourceFiles || sourceFiles.length < 1) {
                System.out.println("待压缩的文件目录：" + srcPathName + "里面不存在文件，无需压缩.");
            } else {
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                        new CRC32());
                ZipOutputStream out = new ZipOutputStream(cos);
                String basedir = "";
                for (int i = 0; i < sourceFiles.length; i++) {
                    compress(sourceFiles[i], out, basedir);
                }
                out.close();
                System.out.println("压缩成功");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        } else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩目录
     *
     * @param dir
     * @param out
     * @param basedir
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩文件
     *
     * @param file
     * @param out
     * @param basedir
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            String filePath = basedir + file.getName();
            System.out.println("压缩文件:" + filePath);
            ZipEntry entry = new ZipEntry(filePath);
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

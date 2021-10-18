package com.ppphuang.demo.charles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//解压
public class Decompression {

    private static final int BUFFER = 8192;


    public static void uncompress(String jarFilePath, String tarDirPath) {
        File jarFile = new File(jarFilePath);
        File tarDir = new File(tarDirPath);
        if (!jarFile.exists())
            throw new RuntimeException(jarFilePath + "不存在！");
        try {
            JarFile jfInst = new JarFile(jarFile);
            Enumeration<JarEntry> enumEntry = jfInst.entries();
            while (enumEntry.hasMoreElements()) {
                JarEntry jarEntry = enumEntry.nextElement();
                File tarFile = new File(tarDir, jarEntry.getName());
                if (jarEntry.getName().contains("META-INF")) {
                    File miFile = new File(tarDir, "META-INF");
                    if (!miFile.exists()) {
                        miFile.mkdirs();
                    }

                }
                makeFile(jarEntry, tarFile);
                if (jarEntry.isDirectory()) {
                    continue;
                }
                FileChannel fileChannel = new FileOutputStream(tarFile).getChannel();
                InputStream ins = jfInst.getInputStream(jarEntry);
                transferStream(ins, fileChannel);
            }
            System.out.println("解压成功!");
        } catch (FileNotFoundException e) {
            System.out.println("解压异常>>>" + e);
        } catch (IOException e) {
            System.out.println("解压异常>>>" + e);
        }
    }

    /**
     * 流交换操作
     *
     * @param ins     输入流
     * @param channel 输出流
     */
    private static void transferStream(InputStream ins, FileChannel channel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER);
        ReadableByteChannel rbcInst = Channels.newChannel(ins);
        try {
            while (-1 != (rbcInst.read(byteBuffer))) {
                byteBuffer.flip();
                channel.write(byteBuffer);
                byteBuffer.clear();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (null != rbcInst) {
                try {
                    rbcInst.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != channel) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建文件
     *
     * @param jarEntry jar实体
     * @param fileInst 文件实体
     * @throws IOException 抛出异常
     */
    public static void makeFile(JarEntry jarEntry, File fileInst) {
        if (!fileInst.exists()) {
            if (jarEntry.isDirectory()) {
                fileInst.mkdirs();
            } else {
                try {
                    if (!fileInst.getParentFile().exists()) {
                        fileInst.getParentFile().mkdirs();
                    }
                    fileInst.createNewFile();
                    System.out.println("解压文件：".concat(fileInst.getPath()));
                } catch (IOException e) {
                    System.out.println("创建文件失败>>>".concat(fileInst.getPath()));
                }
            }
        }
    }

}


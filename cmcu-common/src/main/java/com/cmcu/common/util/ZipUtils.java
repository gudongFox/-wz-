package com.cmcu.common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * https://www.cnblogs.com/zeng1994/p/7862288.html
 * 注意事项
 *（1）支持选择是否保留原来的文件目录结构，如果不保留，那么空文件夹直接不用处理。
 *（2）碰到空文件夹时，如果需要保留目录结构，则直接添加个ZipEntry就可以了，不过就是这个entry的名字后面需要带上一斜杠（/）表示这个是目录。
 *（3）递归时，不需要把zip输出流关闭，zip输出流的关闭应该是在调用完递归方法后面关闭
 *（4）递归时，如果是个文件夹且需要保留目录结构，那么在调用方法压缩他的子文件时，需要把文件夹的名字加一斜杠给添加到子文件名字前面，这样压缩后才有多级目录。
 */
public class ZipUtils {


    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);

            File sourceFile = new File(srcDir);

            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) + " ms");

        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 压缩成ZIP 方法2
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */

    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {

        long start = System.currentTimeMillis();

        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            for (File srcFile : srcFiles) {

                byte[] buf = new byte[BUFFER_SIZE];

                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int len;

                FileInputStream in = new FileInputStream(srcFile);

                while ((len = in.read(buf)) != -1) {

                    zos.write(buf, 0, len);

                }

                zos.closeEntry();

                in.close();

            }

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) + " ms");

        } catch (Exception e) {

            throw new RuntimeException("zip error from ZipUtils", e);

        } finally {

            if (zos != null) {

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }


    /**
     * 递归压缩方法
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {

            File[] listFiles = sourceFile.listFiles();

            if (listFiles == null || listFiles.length == 0) {

                // 需要保留原来的文件结构时,需要对空文件夹进行处理

                if (KeepDirStructure) {

                    // 空文件夹的处理

                    zos.putNextEntry(new ZipEntry(name + "/"));

                    // 没有文件，不需要文件的copy

                    zos.closeEntry();

                }


            } else {

                for (File file : listFiles) {

                    // 判断是否需要保留原来的文件结构

                    if (KeepDirStructure) {

                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,

                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了

                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);

                    } else {

                        compress(file, zos, file.getName(), KeepDirStructure);

                    }


                }

            }

        }

    }


    public static void main(String[] args) throws Exception {
        /** 测试压缩方法1  */
        FileOutputStream fos1 = new FileOutputStream(new File("d:\\logs.zip"));
        ZipUtils.toZip("D:/logs", fos1, true);
        /** 测试压缩方法2  */
        List<File> fileList = new ArrayList<>();
        fileList.add(new File("D:\\logs\\co\\error.log"));
        fileList.add(new File("D:\\logs\\info1.log"));
        FileOutputStream fos2 = new FileOutputStream(new File("d:\\logs2.zip"));
        ZipUtils.toZip(fileList, fos2);

    }

}
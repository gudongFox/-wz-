package com.cmcu.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {



    /**
     * 获取文件md5值
     * @param filePath
     * @return 失败返回空
     */
    public static String getFileMD5String(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest()).toLowerCase();
        }catch (Exception ex){
            return "";
        }
    }


    /**
     * 获取文件名称
     * @param size
     * @return
     */
    public static String getFileSizeName(long size)
    {
        try
        {
            if (size < 1024)
            {
                return size + " B";
            }else if (size < 1024 * 1024)
            {
                return  new BigDecimal(size / 1024.0).setScale(2, RoundingMode.UP).doubleValue()+" KB";
            }
            else
            {
                return  new BigDecimal(size / (1024*1024.0)).setScale(2, RoundingMode.UP).doubleValue()+" MB";
            }
        }catch(Exception ex)
        {
            return "0 B";
        }
    }




    public static String getGoodName(String name){
        String[] wrong=new String[]{"\\","/",":","*","?","\"","<",">","|"};
        for(String c :wrong) {
            if(name.contains(c)){
                name= StringUtils.replace(name,c,"");
            }
        }
        return name;
    }

    public static String getGoodFileName(String fileName){
        if(StringUtils.isNotEmpty(fileName)){
            int dot = fileName.lastIndexOf('.');
            if(dot==-1){
                return getGoodName(fileName);
            }
            String result=getGoodName(fileName.substring(0,dot))+"."+fileName.substring(dot + 1).toLowerCase();
            return result;
        }
        return fileName;
    }


    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1).toLowerCase();
            }
        }
        return filename.toLowerCase();
    }

    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        return file.delete();
    }


    public static String getContentType(String fileName) {
        String extensionName = getExtensionName(fileName);
        switch (extensionName) {
            case "zip":
                return "application/zip";
            case "dwg":
                return "application/x-dwg";
            case "dwf":
                return "Model/vnd.dwf";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
        }
        return "application/octet-stream";
    }

    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getWebServerPath(){
        String classpath = FileUtil.class.getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        return webappRoot;
    }
}

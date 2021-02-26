package com.yiguan.novel.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FileUtil {
    /**
     * 图片上传
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public static void uploadFiles(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 重命名
     *
     * @param fileName
     * @return
     */
    public static String renameToUUID(String fileName) {
        //lastIndexOf() 得到该字符最后一次出现的位置，substring截取字符串，得到文件后缀名
        // 去掉uuid的‘-’，最后得到uuid+fileName
        return UUID.randomUUID().toString().replace("-", "") + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 删除图片
     * @param filePath
     * @param fileName
     * @return
     */
    public static boolean deleteImg(String filePath,String fileName){
        File file =new File(filePath + fileName);
        if(file.delete()){
            return true;
        }else{
            return false;
        }
    }
}
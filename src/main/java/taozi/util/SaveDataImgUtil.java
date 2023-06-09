package taozi.util;

import cn.hutool.core.io.FileUtil;

import java.io.*;
import java.util.Base64;

/**
 * 把data:image的网页图片保存到本地
 * **/
public class SaveDataImgUtil {
    public static void main(String[] args) throws Exception {
        String imageStr="data:image/jpg;base64,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";
        String imageStr3="data:image/jpg;base64,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";

        if (imageStr.contains("data:image")) {
            imageStr = imageStr.substring(imageStr.indexOf(",") + 1);
        }
        if (imageStr3.contains("data:image")) {
            imageStr3 = imageStr3.substring(imageStr3.indexOf(",") + 1);
        }
        File file = base64ToFile(imageStr, "/bigImg.jpg");
        File file3 = base64ToFile(imageStr3, "/tarImg.jpg");
        InputStream inputStream = new FileInputStream(file);
        System.out.println("bg " + file.getAbsolutePath());

        System.out.println("mv " + file3.getAbsolutePath());

    }

    /**
     * 把data:image的网页图片保存到本地
     * **/
    public static String  saveToLocal(String imageStr, String filePath) throws Exception {
        if (imageStr.contains("data:image")) {
            imageStr = imageStr.substring(imageStr.indexOf(",") + 1);
        }
        File file = base64ToFile(imageStr,filePath);
        return filePath;
    }
    public static File base64ToFile(String base64FileStr, String filePath) throws Exception {
        base64FileStr = base64FileStr.replace("\r\n", "");
        // 在用户temp目录下创建临时文件
        //File file = File.createTempFile(fileName, fileType,new File(dir));
        File file = FileUtil.file(filePath);//新覆盖旧图
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            // 用Base64进行解码后获取的字节数组可以直接转换为文件
            byte[] bytes = Base64.getDecoder().decode(base64FileStr);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    System.err.println("base64ToFile---1-- 报错 ");
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    System.err.println("base64ToFile---2-- 报错 ");
                }
            }
        }
        return file;
    }


}

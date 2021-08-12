package com.toone.v3.platform.function;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @Author xugang
 * @Date 2021/6/1 10:59
 */
public class FileType {

    public static final HashMap<String, String> fileTypeMap = new HashMap<String, String>();

    static {
        // images
        fileTypeMap.put("FFD8FFE0", ".jpg");
        fileTypeMap.put("89504E47", ".png");
        fileTypeMap.put("47494638", ".gif");
        fileTypeMap.put("49492A00", ".tif");
        fileTypeMap.put("424D", ".bmp");
        fileTypeMap.put("EFBBBF3C", ".xml");
        fileTypeMap.put("62142365", ".txt");
        fileTypeMap.put("5B4D616E", ".cfg");
        fileTypeMap.put("49545346", ".chm");
        fileTypeMap.put("EFBBBF76", ".js");
        fileTypeMap.put("3C21444F", ".html");
        fileTypeMap.put("25504446", ".pdf");
        fileTypeMap.put("0D0AD2BB", ".txt");

        //
        fileTypeMap.put("41433130", ".dwg"); // CAD
        fileTypeMap.put("38425053", ".psd");
        fileTypeMap.put("7B5C727466", ".rtf"); // 日记本
        fileTypeMap.put("3C3F786D6C", ".xml");
        fileTypeMap.put("68746D6C3E", ".html");
        fileTypeMap.put("44656C69766572792D646174653A", ".eml"); // 邮件
        fileTypeMap.put("D0CF11E0", ".doc");
        fileTypeMap.put("D0CF11E0", ".xls");//excel2003版本文件
        fileTypeMap.put("5374616E64617264204A", ".mdb");
        fileTypeMap.put("252150532D41646F6265", ".ps");
        fileTypeMap.put("255044462D312E", ".pdf");
        fileTypeMap.put("504B0304", ".zip");  //xlsx和zip 一样
        //  mFileTypes.put("504B0304", ".xlsx");//excel2007以上版本文件
        fileTypeMap.put("52617221", ".rar");
        fileTypeMap.put("57415645", ".wav");
        fileTypeMap.put("41564920", ".avi");
        fileTypeMap.put("2E524D46", ".rm");
        fileTypeMap.put("000001BA", ".mpg");
        fileTypeMap.put("000001B3", ".mpg");
        fileTypeMap.put("6D6F6F76", ".mov");
        fileTypeMap.put("3026B2758E66CF11", ".asf");
        fileTypeMap.put("4D546864", ".mid");
        fileTypeMap.put("1F8B08", ".gz");
    }


    public static String getFileType(String filePath) {
        return fileTypeMap.get(getFileHeader(filePath));
    }

    public static String getFileTypeByFileInputStream(InputStream is) {
        String type = getFileHeaderByFileInputStream(is);
        //System.out.println(type);
        if (type != null && fileTypeMap.get(type) != null) {
            return fileTypeMap.get(type);
        } else {
            return "";
        }
    }


    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];

            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }

        return value;
    }

    public static String getFileHeaderByFileInputStream(InputStream is) {
        String value = null;
        try {
            byte[] b = new byte[4];

            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}

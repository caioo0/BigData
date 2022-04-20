package com.atguigu.util;

public class ETLUtil {
    public static String etlStr(String oriStr) {
        String[] fields = oriStr.split("\t");
        StringBuffer sb = new StringBuffer();
        if (fields.length < 9) {
            return null;
        }
        //去掉类别字段中的空格
        fields[3] = fields[3].replaceAll(" ","");
        for(int i = 0; i < fields.length; ++i) {
            if (i < 9) {
                if (i == fields.length - 1) {
                    sb.append(fields[i]);
                } else {
                    sb.append(fields[i]).append("\t");
                }
            } else if (i == fields.length - 1) {
                sb.append(fields[i]);
            } else {
                sb.append(fields[i]).append("&");
            }
        }
        return sb.toString();
    }
}

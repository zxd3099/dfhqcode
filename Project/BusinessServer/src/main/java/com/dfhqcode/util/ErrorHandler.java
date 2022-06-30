package com.dfhqcode.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zxd3099
 * @create 2022-06-29-10:22
 */
public class ErrorHandler {
    /**
     * @param exceptionMessage 移动云异常详细信息
     * @return
     */
    public static String getErrorMsg(String exceptionMessage)
    {
        String regexp = "message:(.*?)]";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(exceptionMessage);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

}

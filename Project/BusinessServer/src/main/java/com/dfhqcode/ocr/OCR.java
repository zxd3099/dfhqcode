package com.dfhqcode.ocr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ocr.ECloudServerException;
import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;
import com.dfhqcode.util.ErrorHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author zxd3099
 * @create 2022-07-02-16:27
 */
public class OCR {
    private final static String USER_AK;
    private final static String USER_SK;
    private static ECloudDefaultClient client;

    static {
        USER_AK = "7fb8ade4b3354580b3fda59a5f92edf0";
        USER_SK = "a3b199fefaf24a809afad6c4e3061d55";
        Credential credential = new Credential(USER_AK, USER_SK);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
    }

    /**
     * 通用文字识别-通用印刷体识别
     * 目前图片格式支持jpg、jpeg、png、bmp，图片的长和宽要求最大边不超4000px，部分接口图片的要求为分辨率建议600*800以上。
     * 图片编码后大小小于10M
     * @param imgBase64 图片Base64编码
     * @return 识别成功，返回{"state":"success","info":{"text":识别结果,"conf":置信度,由每个文本行置信度相乘}},识别失败，返回{"state":"error", "info":...(错误信息)}
     * @throws IOException
     */
    public static JSONObject general(String imgBase64) throws IOException {
        HashMap<String, Object> generalParams = new HashMap<>();
        JSONObject generalOptions = new JSONObject();
        generalOptions.put("rotate_180", true);
        generalOptions.put("language", "zh");
        generalParams.put("options", generalOptions);

        //参数为图片的base64编码
        IECloudRequest generalRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/general",
                imgBase64,generalParams);

        JSONObject res = new JSONObject();
        StringBuilder text = new StringBuilder();
        try{
            JSONObject responseBase64 = (JSONObject) client.call(generalRequestBase64);
            if( "OK".equals(responseBase64.getString("errormsg"))) {
                // 识别正确
                JSONArray jsonArray = responseBase64.getJSONArray("items");
                double conf = 1.0;//置信度
                for(int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    text.append(jo.getString("itemstring")+"\n");
                    conf *= jo.getDouble("itemconf");
                }
                res.put("state","success");
                JSONObject info = new JSONObject();
                info.put("text",text.toString());
                info.put("conf",conf);
                res.put("info",info);
                System.out.println(res);
                return res;

            } else {
                res.put("state","error");
                res.put("info","移动云无法识别");
                return res;
            }
        } catch (ECloudServerException e) {
            String str = e.getMessage();
            res.put("state", "error");
            res.put("info", ErrorHandler.getErrorMsg(str));
            return res;
        }
    }

    /**
     * 通用文字识别-通用手写体识别
     * 目前图片格式支持jpg、jpeg、png、bmp，图片的长和宽要求最大边不超4000px，部分接口图片的要求为分辨率建议600*800以上。
     * 图片编码后大小小于10M
     * @param imgBase64 图片Base64
     * @return 识别成功，返回{"state":"success","info":...(识别结果)},识别失败，返回{"state":"error", "info":...(错误信息)}
     * @throws IOException
     */
    public static JSONObject handWriting(String imgBase64) throws IOException {
        HashMap<String, Object> handwritingParams = new HashMap<>();
        JSONObject handwritingOptions = new JSONObject();
        handwritingOptions.put("preprocess", true);
        handwritingParams.put("options", handwritingOptions);

        //参数为图片的base64编码
        IECloudRequest handWritingRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/handwriting",
                imgBase64, handwritingParams);

        JSONObject res = new JSONObject();
        StringBuilder text = new StringBuilder();

        try {
            JSONObject responseBase64 = (JSONObject) client.call(handWritingRequestBase64);
            System.out.println("----response:"+responseBase64);
            if ( "OK".equals(responseBase64.getString("errormsg"))) {
                double conf = 1.0;
                // 识别正确
                JSONArray jsonArray = responseBase64.getJSONArray("items");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    text.append(jo.getString("itemstring") + "\n");
                    conf *= jo.getDouble("itemconf");
                }
                res.put("state", "success");
                JSONObject info = new JSONObject();
                info.put("text",text.toString());
                info.put("conf",conf);
                res.put("info", info);

                return res;
            } else {
                res.put("state","error");
                res.put("info","移动云无法识别");
                return res;
            }
        } catch (ECloudServerException e) {
            String str = e.getMessage();
            res.put("state", "error");
            res.put("info", ErrorHandler.getErrorMsg(str));
            return res;
        }
    }
}
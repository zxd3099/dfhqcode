package com.dfhqcode.faceRecognition;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.face.ECloudServerException;
import com.chinamobile.cmss.sdk.image.IECloudClient;
import com.chinamobile.cmss.sdk.image.request.EngineImageLivenessDetectPostRequest;
import com.chinamobile.cmss.sdk.image.request.EngineImagePersonDetectPostRequest;
import com.chinamobile.cmss.sdk.image.response.EngineImageLivenessDetectResponse;
import com.chinamobile.cmss.sdk.image.response.EngineImagePersonDetectResponse;
import com.chinamobile.cmss.sdk.image.response.bean.EngineLiveness;
import com.chinamobile.cmss.sdk.image.response.bean.EnginePerson;
import com.dfhqcode.config.CloudConfig;
import com.dfhqcode.util.ErrorHandler;

import java.io.IOException;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-29-11:05
 */
public class BodyRecognition {
    private static IECloudClient client = (IECloudClient) CloudConfig.client;

    /**
     * 人体识别-人体检测与属性识别
     * @param imgBase64  Base64编码后的图片文件
     * @return 识别成功,{"state":"success","info":...(识别信息，包括年龄，性别)}，识别失败，{"state":"error","info":...(错误信息)}
     * @throws IOException
     */
    public static JSONObject personDetect(String imgBase64) throws IOException, IllegalAccessException {
        EngineImagePersonDetectPostRequest request = new EngineImagePersonDetectPostRequest();
        // 图片base64
        request.setImage(imgBase64);

        JSONObject res = new JSONObject();
        try{
            EngineImagePersonDetectResponse response = client.call(request);
            if("OK".equals(response.getState()))
            {
                List<EnginePerson> body = response.getBody();
                String age = body.get(0).getAttribute().getAge().getDesc();
                String gender = body.get(0).getAttribute().getGenderCode().getDesc();
                JSONObject info = new JSONObject();
                info.put("age",age);
                info.put("gender",gender);

                res.put("state","success");
                res.put("info",info);
                return res;
            }
        } catch(ECloudServerException e) {
            res.put("state","error");
            res.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            return res;
        }
        return res;
    }

    /**
     * 人体识别-活体检测
     * @param imgBase64  Base64编码后的图片文件
     * @return 识别成功,{"state":"success","info":...(活体/视频回放攻击/无法判断/光线太暗/光线太亮...）}，识别失败，{"state":"error","info":...(错误信息)}
     * @throws IOException
     */
    public static JSONObject livelinessDetect(String imgBase64) throws IOException, IllegalAccessException {
        EngineImageLivenessDetectPostRequest request = new EngineImageLivenessDetectPostRequest();
        // 图片base64
        request.setImage(imgBase64);

        JSONObject res = new JSONObject();
        try{
            EngineImageLivenessDetectResponse response = client.call(request);
            if("OK".equals(response.getState()))
            {
                EngineLiveness body = response.getBody();
                res.put("state","success");
                res.put("livenessCode",body.getLivenessCode());
                res.put("info", body.getLivenessInfo());
                return res;
            }
        } catch(ECloudServerException e) {
            res.put("state","error");
            res.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            System.out.println(res);
            return res;
        }
        return res;
    }
}

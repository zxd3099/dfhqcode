package com.dfhqcode.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.faceRecognition.BodyRecognition;
import com.dfhqcode.faceRecognition.FaceRecognition;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zxd3099
 * @create 2022-06-29-11:27
 */
@Service
public class FaceRecognitionService {
    /**
     * 人脸库ID
     */
    private final String faceStoreId = "62bab68e61146b000132142b";
    /**
     * 置信度
     */
    private final double confidence = 0.735;

    /**
     * 人脸识别-注册
     * @param imageBase64 注册图片的base64编码
     * @param name 用户名
     * @return 返回结果为JSON, 成功:{"state":"success","info":{"userId":""}}, 失败:{"state":"error","info":失败原因}
     *      * 失败原因有:活体检测失败,(系统无法识别,请用户换个角度拍照),人脸检测失败,人体识别失败,创建人员失败(系统问题或网络问题)
     * @throws IOException
     * @throws IllegalAccessException
     */
    public JSONObject register(String imageBase64, String name) throws IOException, IllegalAccessException {
        JSONObject result = new JSONObject();
        // 活体检测
        JSONObject liveRes = BodyRecognition.livelinessDetect(imageBase64);
        if ("success".equals(liveRes.getString("state"))) {
            if (liveRes.getIntValue("livenessCode") != 1) {
                // 活体检测成功,但检测结果不是活体
                result.put("state", "error");
                result.put("info", liveRes.getString("info"));
                return result;
            }
        } else {
            // 活体检测失败,系统问题或网络问题
            result.put("state", "error");
            result.put("info", liveRes.getString("info"));
            return result;
        }

        // 人脸检测
        JSONObject faceDetectRes = FaceRecognition.faceDetect(imageBase64);
        String faceToken = "";
        if (!"success".equals(faceDetectRes.getString("state"))) {
            // 人脸检测失败,系统问题或网络问题
            result.put("state", "error");
            result.put("info", faceDetectRes.getString("info"));
            return result;
        }
        faceToken = faceDetectRes.getJSONObject("info").getString("faceToken");


        // 人体识别-人体检测与属性识别
        JSONObject exInfo = new JSONObject();

        Map<String, String> exDescriptionInfos = new HashMap<>(10);
        JSONObject sigRes = BodyRecognition.personDetect(imageBase64);
        if ("success".equals(sigRes.getString("state"))) {
            // 人体识别成功,从人脸中获得人员的exInfo
            exInfo = sigRes.getJSONObject("info");
            exDescriptionInfos.put("age", exInfo.getString("age"));
            exDescriptionInfos.put("gender", exInfo.getString("gender"));
            exDescriptionInfos.put("faceToken", faceToken);
        } else {
            // 人体识别失败,系统问题或网络问题
            result = sigRes;
            return result;
        }

        // 人体识别成功,进行注册,创建人员
        JSONObject faceRes = FaceRecognition.createFace(faceStoreId, name, imageBase64, exDescriptionInfos);
        if("success".equals(faceRes.getString("state"))) {
            // 创建人员成功,成功注册
            result.put("state","success");
            JSONObject info = new JSONObject();
            info.put("userId", faceRes.getJSONObject("info").getString("id"));
            result.put("info", info);
            return result;
        } else {
            // 创建人员失败,系统问题或网络问题
            result.put("state", "error");
            result.put("info", faceRes.getString("info"));
            return result;
        }
    }

    /**
     *
     * @param imageBase64
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    public JSONObject login(String imageBase64) throws IOException, IllegalAccessException {
        JSONObject result = new JSONObject();
        // 活体检测
        JSONObject liveRes = BodyRecognition.livelinessDetect(imageBase64);
        if("success".equals(liveRes.getString("state")))
        {
            if(liveRes.getIntValue("livenessCode") != 1)
            {
                // 活体检测成功,但检测结果为不是活体
                result.put("state","error");
                result.put("info",liveRes.getString("info"));
                return result;
            }
        } else
        {
            // 活体检测失败,可能是网络原因或系统原因
            result.put("state","error");
            result.put("info",liveRes.getString("info"));
            return result;
        }

        // 人脸检测
        JSONObject faceDetectRes = FaceRecognition.faceDetect(imageBase64);
        String faceToken = "";
        if(!"success".equals(faceDetectRes.getString("state")))
        {
            // 人脸检测失败,可能是网络原因或系统原因
            result.put("state","error");
            result.put("info",faceDetectRes.getString("info"));
            return result;
        }
        faceToken = faceDetectRes.getJSONObject("info").getString("faceToken");

        // 查询人脸库的人员列表
        JSONObject faceRes = FaceRecognition.getMemberList(faceStoreId);
        if("success".equals(faceRes.getString("state")))
        {
            // 查询人脸库的人员列表成功
            JSONArray info = faceRes.getJSONArray("info");

            for(int i = 0 ; i < info.size() ; ++i) {
                JSONObject jsonObject = info.getJSONObject(i);
                String userId = jsonObject.getString("id");
                String faceToken2 = jsonObject.getJSONObject("exDescriptionInfos").getString("faceToken");

                // 人脸比对
                JSONObject matchRes = FaceRecognition.match(faceToken, faceToken2);
                if("success".equals(matchRes.getString("state")))
                {
                    // 人脸比对成功
                    double confidence = matchRes.getJSONObject("info").getDouble("confidence");
                    if(confidence > confidence) {
                        result.put("state","success");
                        result.put("userId", userId);
                        return result;
                    }
                }

            }
            // 人脸比对不成功, 需要用户注册或换个角度拍照
            result.put("state","error");
            result.put("info","人脸比对不成功");
            return result;
        } else
        {
            // 没有得到人脸列表, 系统问题或网络问题
            result.put("state","error");
            result.put("info",faceRes.getJSONObject("info"));
            return result;
        }
    }
}

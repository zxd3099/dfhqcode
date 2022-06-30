package com.dfhqcode.faceRecognition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.face.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.face.ECloudServerException;
import com.chinamobile.cmss.sdk.face.request.IECloudRequest;
import com.chinamobile.cmss.sdk.face.request.face.FaceRequestFactory;
import com.dfhqcode.config.CloudConfig;
import com.dfhqcode.util.ErrorHandler;

import java.io.IOException;
import java.util.Map;

/**
 * @author zxd3099
 * @create 2022-06-29-10:00
 */
public class FaceRecognition {
    private static ECloudDefaultClient client = CloudConfig.client;

    /**
     * 创建人脸，向人脸库中添加记录
     * @param faceStoreId  人脸库ID
     * @param name  用户名
     * @param imgBase64 Base64格式化后的人脸图片
     * @param exDescriptionInfos  其他信息（若无，传入null）
     * @return  {"state":"success","info":...} OR {"state":"error","info":...(失败原因)}
     * @throws IOException
     */
    public static JSONObject createFace(String faceStoreId, String name, String imgBase64, Map<String, String> exDescriptionInfos) throws IOException {
        JSONObject params = new JSONObject();
        params.put("faceStoreId", faceStoreId);
        params.put("name", name);
        params.put("image", imgBase64);

        params.put("exDescriptionInfos",exDescriptionInfos);
        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/member/create", params);

        return getResponse(request);
    }

    /**
     * 根据人员ID在人脸库中查找人脸记录
     * @param memberId  人员ID
     * @return {"state":"success","info":...} OR {"state":"error","info":...(失败原因)}
     * @throws IOException
     */
    public static JSONObject getFace(String memberId) throws IOException{
        JSONObject params = new JSONObject();
        params.put("memberId", memberId);
        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/member/get", params);

        return getResponse(request);
    }

    /**
     * 根据人员ID在人脸库中删除人脸记录
     * @param memberId  人员id
     * @return {"state":"success","info":...} OR {"state":"error","info":...(失败原因)}
     * @throws IOException
     */
    public static JSONObject deleteFace(String memberId) throws IOException {
        JSONObject params = new JSONObject();
        params.put("memberId", memberId);

        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/member/delete", params);

        return getResponse(request);
    }

    /**
     * 人脸比对，如果人脸比对置信度为0.735，可以认为是同一人
     * @param imgBase641
     * @param imgBase642
     * @return
     * @throws IOException
     */
    public static JSONObject match(String imgBase641, String imgBase642) throws IOException{
        JSONObject params = new JSONObject();
        params.put("image1",imgBase641);
        params.put("imageType1", "BASE64");
        params.put("image2", imgBase642);
        params.put("imageType2", "BASE64");

        IECloudRequest request = FaceRequestFactory.getFaceRequest("/api/human/face/v1/match", params);
        JSONObject res  = new JSONObject();

        try{
            JSONObject response =(JSONObject) client.call(request);
            if("OK".equals(response.getString("state")))
            {
                JSONObject confidence = new JSONObject();
                confidence.put("confidence", response.getJSONObject("body").getDoubleValue("confidence"));
                res.put("state", "success");
                res.put("info", confidence);  // 可信度
                return res;
            }
        } catch(ECloudServerException e) {
            res.put("state", "error");
            res.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            return res;
        }
        return res;
    }

    /**
     * 获得人员列表
     * @param faceStoreId  人脸库ID
     * @return 成功:{"state":"success","info",人员列表},失败:{"state":"success","info":失败原因}
     * 成功时人员列表例子:{
     *                 "id": "60a754ac634ba154596e8c6d",
     *                 "name": "aaa",
     *                 "exDescriptionInfos": {
     *                     "姓名": "aaa",
     *                     "年龄": "30",
     *                     "部门": "paas"
     *                 }
     *                 "imageIds": [
     *                     "60a754ad634ba154596e8c6e"
     *                 ]
     *     }
     * @throws IOException
     */
    public static JSONObject getMemberList(String faceStoreId) throws IOException{
        JSONObject request1 = new JSONObject();
        request1.put("faceStoreId",faceStoreId);
        IECloudRequest request2 = FaceRequestFactory.getFaceRequest("/api/human/face/v1/store/member/list", request1);

        JSONObject res  = new JSONObject();
        try{
            JSONObject response =(JSONObject)client.call(request2);
            if("OK".equals(response.getString("state")))
            {
                JSONArray list = response.getJSONObject("body").getJSONArray("rows");

                res.put("state", "success");
                res.put("info", list);
                return res;
            }
        } catch(ECloudServerException e) {
            res.put("state", "error");
            res.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            return res;
        }
        return res;
    }

    /**
     * 人脸检测,检测图片中的人脸,返回faceToken,图片的唯一标识
     * @param imageBase64
     * @return
     * @throws IOException
     */
    public static JSONObject faceDetect(String imageBase64) throws IOException{
        JSONObject params = new JSONObject();
        params.put("image",imageBase64);
        params.put("imageType","BASE64");
        params.put("maxFaceNum","1");
        JSONObject result = new JSONObject();
        IECloudRequest faceDetectRequest = FaceRequestFactory.getFaceRequest("/api/human/face/v1/detect", params);

        try{
            JSONObject response = (JSONObject) client.call(faceDetectRequest);

            System.out.println(response.toString());
            if("OK".equals(response.getString("state"))) {
                // 人脸检测成功
                result.put("state", "success");
                JSONObject info = new JSONObject();
                JSONObject face = response.getJSONObject("body").getJSONArray("faces").getJSONObject(0);
                info.put("faceToken", face.getString("faceToken"));
                result.put("info", info);
                return result;
            }
        }catch (ECloudServerException e){
            // 人脸检测失败
            result.put("state", "error");
            result.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            return result;
        }
        return result;
    }

    /**
     * @param request 请求
     * @throws IOException
     * @return
     */
    private static JSONObject getResponse(IECloudRequest request) throws IOException
    {
        JSONObject res  = new JSONObject();

        try{
            JSONObject response =(JSONObject) client.call(request);
            if("OK".equals(response.getString("state")))
            {
                JSONObject body = response.getJSONObject("body");

                res.put("state", "success");
                res.put("info", body);
                return res;
            }
        } catch(ECloudServerException e) {
            res.put("state", "error");
            res.put("info", ErrorHandler.getErrorMsg(e.getMessage()));
            return res;
        }
        return res;
    }
}

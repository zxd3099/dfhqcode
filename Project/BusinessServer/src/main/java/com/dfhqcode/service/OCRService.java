package com.dfhqcode.service;

import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.ocr.OCR;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OCRService {
    /**
     * OCR识别base64编码后的图片
     * @param imgBase64
     * @return
     * @throws IOException
     */
    public JSONObject ocr(String imgBase64) throws IOException {
        JSONObject[] res = new JSONObject[2];
        res[0] = OCR.general(imgBase64);
        res[1] = OCR.handWriting(imgBase64);
        JSONObject result = new JSONObject();
        boolean isSuccess[] = new boolean[2];
        for(int i = 0; i < 2; ++i) {
            isSuccess[i] = "success".equals(res[i].getString("state"));
        }

        if(!isSuccess[0] && !isSuccess[1] ) {
            // 都无法识别
            result.put("state","error");
            result.put("info","移动云无法识别");
            return result;
        }
        double maxConf  = 0.0;
        String text = "";
        for(int i = 0; i < 2; ++i) {
            if(isSuccess[i]) {
                double conf = res[i].getJSONObject("info").getDouble("conf");
                if(conf > maxConf) {
                    maxConf = conf;
                    text = res[i].getJSONObject("info").getString("text");
                }
            }
        }
        result.put("state","success");
        result.put("info",text);
        return result;
    }
}
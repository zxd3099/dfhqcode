package com.dfhqcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.service.OCRService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author zxd3099
 * @create 2022-06-30-14:29
 */
@Api(tags = "OCR识别")
@RestController
@RequestMapping("/ocr")
public class OCRController {
    @Autowired
    private OCRService ocrService;

    @GetMapping("/ocr")
    @ApiOperation(value = "OCR识别")
    public JsonResult<Object> ocr(@RequestParam @Valid String imgBase64)
    {
        try {
            JSONObject ocr = ocrService.ocr(imgBase64);
            if ("success".equals(ocr.getString("state"))) {
                return JsonResult.success(ocr.getString("info"), "识别成功");
            } else {
                return JsonResult.error(ocr.getString("info"), "识别失败");
            }
        } catch (IOException e) {
            return JsonResult.error("系统错误");
        }
    }
}

package com.dfhqcode.service;

import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** 
* OCRService Tester. 
* 
* @author <Authors name> 
* @since <pre>Jul 4, 2022</pre> 
* @version 1.0 
*/ 
public class OCRServiceTest extends BaseTest {

@Autowired
private OCRService ocrService;

/** 
* 
* Method: ocr(String imgBase64) 
* 
*/ 
@Test
public void testOcr() throws Exception { 
    JSONObject jo = ocrService.ocr("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");
    System.out.println(jo.toString());
} 


} 

package com.peashoot.blog.controller;

import com.peashoot.blog.BlogApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserControllerTests {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        String requestJson = "{\n" +
                "\t\"visitorId\":\"1\",\n" +
                "\t\"email\":\"abc@gmail.com\",\n" +
                "\t\"username\":\"admin\",\n" +
                "\t\"password\":\"21232f297a57a5a743894a0e4a801fc3\",\n" +
                "\t\"nickName\":\"管理员\",\n" +
                "\t\"location\":\"圣彼得堡\",\n" +
                "\t\"contact\":\"zzzzzz\",\n" +
                "\t\"socialAccount\":\"8sawdaxza\",\n" +
                "\t\"headPortrait\":\"http://www.baidu.com/icon.ico\",\n" +
                "\t\"gender\":\"0\",\n" +
                "\t\"personalProfile\":\"boboboboobobo\"\n" +
                "}";
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/system/register")
                //请求编码和数据格式为json和UTF8
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJson))
                //期望的返回值 或者返回状态码
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                //返回请求的字符串信息
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void testUserLogin() throws Exception {
        String requestJson = "{\n" +
                "\t\"username\":\"admin\",\n" +
                "\t\"password\":\"21232f297a57a5a743894a0e4a801fc3\"\n" +
                "}";
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/system/login")
                //请求编码和数据格式为json和UTF8
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJson))
                //期望的返回值 或者返回状态码
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                //返回请求的字符串信息
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}

package com.peashoot.blog.controller;

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
public class RoleControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testModify() throws Exception {
        String requestJson = "{\n" +
                "\t\"roleName\":\"ROLE_Peashoot\",\n" +
                "\t\"permissions\":[\"role_modify\", \"change_detail\", \"apply_reset_pwd\", \"apply_retrieve\", \"logout\"]\n" +
                "}";
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/system/register")
                //请求编码和数据格式为json和UTF8
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(requestJson).header("token", "BearereyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImFkZHJlc3MiOm51bGwsImV4cCI6MTU5NDI2MDEyMSwiYmZwIjpudWxsLCJjcmVhdGVkIjoxNTkxNjY4MTIxMDkyfQ.95ckizXiln8J-G9sjXapjOz9u2fWRk1zbTgXczaA5sdz1WA9ZUUDe1oUkretI3Mj2U6ABu8fGypgCBSf0qiaUw"))
                //期望的返回值 或者返回状态码
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                //返回请求的字符串信息
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}

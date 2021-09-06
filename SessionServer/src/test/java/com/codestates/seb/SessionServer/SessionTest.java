package com.codestates.seb.SessionServer;

import com.codestates.seb.SessionServer.CodeStatesSubmit.Submit;
import com.codestates.seb.SessionServer.Const.SessionConst;
import com.codestates.seb.SessionServer.Controller.SessionController;
import com.codestates.seb.SessionServer.Domain.LoginResponse;
import com.codestates.seb.SessionServer.Domain.Userdata;
import com.codestates.seb.SessionServer.Entity.UserList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;

@AutoConfigureMockMvc
@SpringBootTest
public class SessionTest {

    private static Submit submit = new Submit();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private EntityManager entityManager;

    @AfterAll
    static void after() throws Exception {
        submit.SubmitJson("im-sprint-spring-session", 4);
        submit.ResultSubmit();
    }

    @BeforeEach
    public void beforEach() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .build();

        objectMapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .build();
    }

    @Test
    @DisplayName(value = "유저 정보가 유효하지 않으면 세션 생성을 거절합니다.")
    void CreateSessionCheckUser() throws Exception{
        MvcResult result = null;
        String url = "/users/login";
        String standard = "{\"data\":null,\"message\":\"not authorized\"}";
        String BodyData = null;

        Userdata userdata = new Userdata();
        userdata.setUserId("kimcoding");
        userdata.setPassword("12345");

        try{
            String content = objectMapper.writeValueAsString(userdata);
            result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            BodyData = objectMapper.writeValueAsString(objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class));
            submit.ResultSave(BodyData.equals(standard));

        }catch (Exception e){
            System.out.println(e);
        }finally {
            Assertions.assertEquals(BodyData,standard);
        }

    }

    @Test
    @DisplayName(value = "로그인 세션이 생성 되야합니다.")
    void UserLogin()throws Exception{
        MvcResult result = null;
        String url = "/users/login";
        String sessionData = null;
        String BodyData = "";

        Userdata userdata = new Userdata();
        userdata.setUserId("kimcoding");
        userdata.setPassword("1234");

        try{
            String content = objectMapper.writeValueAsString(userdata);
            result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            sessionData = objectMapper.writeValueAsString(SessionController.session.getAttribute("LoginMember"));
            BodyData = objectMapper.writeValueAsString(objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class).getData());
            submit.ResultSave(sessionData.equals(BodyData));

        }catch (Exception e){
            System.out.println(e);
        }finally {
            Assertions.assertEquals(sessionData,BodyData);
        }
    }

    @Test
    @DisplayName(value = "모든 세션 데이터가 지워져야합니다.")
    void DeleteSession()throws Exception{
        MvcResult result = null;
        String url = "/users/logout";
        String sessionData = "";

        UserList userdata = new UserList();
        userdata.setUserId("kimcoding");
        userdata.setPassword("1234");

        try{
            SessionController.session.setAttribute(SessionConst.LOGIN_STATES, userdata);

            result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            sessionData = objectMapper.writeValueAsString(SessionController.session.getAttribute("LoginMember"));
            System.out.println(sessionData);

        }catch (Exception e){
            System.out.println(e);
        }finally {
            submit.ResultSave(sessionData.equals(""));
            Assertions.assertEquals(sessionData,"");
        }
    }

    @Test
    @DisplayName(value = "세션이 유효하면 유저 정보를 리턴합니다.")
    void CheckSessionLogin() throws Exception {
        MvcResult result = null;
        String url = "/users/userinfo";
        String sessionData = null;
        String BodyData = "";

        Userdata userdata = new Userdata();
        userdata.setUserId("kimcoding");
        userdata.setPassword("1234");

        try{
            String content = objectMapper.writeValueAsString(userdata);
            result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            result = mockMvc.perform(MockMvcRequestBuilders.get(url)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            sessionData = objectMapper.writeValueAsString(SessionController.session.getAttribute("LoginMember"));
            BodyData = objectMapper.writeValueAsString(objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class).getData());
            submit.ResultSave(sessionData.equals(BodyData));

        }catch (Exception e){
            System.out.println(e);
        }finally {
            Assertions.assertEquals(sessionData,BodyData);
        }
    }

}

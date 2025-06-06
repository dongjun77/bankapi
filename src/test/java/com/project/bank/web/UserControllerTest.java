package com.project.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bank.config.dummy.DummyObject;
import com.project.bank.domain.user.UserRepository;
import com.project.bank.dto.user.UserRequestDto.JoinRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userRepository.save(newMockUser("ssar","쌀"));
    }

    @Test
    public void join_success_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("love");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("love@gmail.com");
        joinRequestDto.setFullname("러브");

        String requestBody = objectMapper.writeValueAsString(joinRequestDto);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("ssar");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("ssar@gmail.com");
        joinRequestDto.setFullname("쌀");

        String requestBody = objectMapper.writeValueAsString(joinRequestDto);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
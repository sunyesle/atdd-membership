package com.sunyesle.atddmembership;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.controller.MembershipController;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.exception.ErrorResponse;
import com.sunyesle.atddmembership.service.MembershipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static com.sunyesle.atddmembership.constants.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MembershipController.class)
class MembershipControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MembershipService membershipService;

    @Test
    void 유효성_검사를_실패하면_오류필드정보가_포함된_응답을_리턴한다() throws Exception {
                        MembershipRequest membershipRequest = new MembershipRequest(null, -1);
        MockHttpServletRequestBuilder builder = post("/api/v1/memberships")
                .header(USER_ID_HEADER, "testUser")
                .content(objectMapper.writeValueAsString(membershipRequest))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(builder)
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertThat(response.getFieldErrors()).hasSize(2);
    }
}
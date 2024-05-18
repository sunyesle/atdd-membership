package com.sunyesle.atddmembership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyesle.atddmembership.config.SecurityConfig;
import com.sunyesle.atddmembership.controller.MembershipController;
import com.sunyesle.atddmembership.dto.MembershipAccumulateRequest;
import com.sunyesle.atddmembership.dto.MembershipRequest;
import com.sunyesle.atddmembership.exception.ErrorResponse;
import com.sunyesle.atddmembership.service.MembershipService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.sunyesle.atddmembership.constants.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MembershipController.class)
@Import(SecurityConfig.class)
class MembershipControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MembershipService membershipService;

    @ParameterizedTest
    @MethodSource("provideInvalidRequest")
    void 유효성_검사를_실패하면_오류필드정보가_포함된_응답을_리턴한다(MockHttpServletRequestBuilder builder) throws Exception {
        MvcResult result = mvc.perform(builder)
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse response = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertThat(response.getFieldErrors()).isNotEmpty();
    }

    static Stream<Arguments> provideInvalidRequest() throws JsonProcessingException {
        return Stream.of(
                Arguments.of(createPostRequest("/api/v1/memberships", new MembershipRequest(null, -1))),
                Arguments.of(createPostRequest("/api/v1/memberships/1/accumulate", new MembershipAccumulateRequest(-1)))
        );
    }

    private static MockHttpServletRequestBuilder createPostRequest(String path, Object content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .header(USER_ID_HEADER, "testUser")
                .content(objectMapper.writeValueAsString(content));
    }
}
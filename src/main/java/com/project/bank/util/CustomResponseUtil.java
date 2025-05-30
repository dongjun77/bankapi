package com.project.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bank.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDTO = new ResponseDto<>(1, "로그인성공", dto);
            String responseBody = om.writeValueAsString(responseDTO);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody); // 공통적인 응답 DTO
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }
    public static void unAuthentication(HttpServletResponse response, String msg) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDTO = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDTO);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody); // 공통적인 응답 DTO
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }
}

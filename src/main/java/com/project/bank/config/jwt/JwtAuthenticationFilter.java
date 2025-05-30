package com.project.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bank.config.auth.LoginUser;
import com.project.bank.dto.user.UserRequestDto.LoginRequestDto;
import com.project.bank.dto.user.UserResponseDto.LoginResponseDto;
import com.project.bank.util.CustomResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // Post : /api/login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            LoginRequestDto loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(), loginRequestDto.getPassword()
            );

            // UserDetailsService의 loadUserByUsername 호출
            // JWT 를 쓴다 하더라도, 컨트롤러 진입을 하면 시큐리티의 권한체크, 인증체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request하고, response 하면 끝 !!
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            // authenticationEntryPoint에 걸린다.
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // return authentication 잘 작동하면 successfulAuthentication 메서드 호출됩니다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(loginUser);
        response.addHeader(JwtVO.HEADER, jwtToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginResponseDto);
    }
}

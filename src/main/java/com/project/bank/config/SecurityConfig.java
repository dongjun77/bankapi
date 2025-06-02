package com.project.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bank.config.jwt.JwtAuthenticationFilter;
import com.project.bank.domain.user.UserEnum;
import com.project.bank.dto.ResponseDto;
import com.project.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//@Slf4j Junit 테스트시 문제가 생김
@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // Configuration에서만 Bean 등록이함가능
    @Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 등록이 필요함
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }

        public HttpSecurity build() {
            return getBuilder();
        }

    }

    // JWT 서버를 만들 예정 !! Session 사용안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        // iframe 허용 안 함
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        // csrf 설정 해제 (JWT 서버에서는 enable해도 post에서 작동 안 함)
        http.csrf(csrf -> csrf.disable());

        // 프론트엔드의 요청 설정 (react 등에서 요청 예정)
        http.cors(cors -> cors.configurationSource(configurationSource()));

        // JSESSIONID를 서버 쪽에서 관리 안 하겠다는 뜻 !!
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // react 앱으로 요청할 예정이므로 formLogin 미사용
        http.formLogin(login -> login.disable());

        // https 쓰면 안쓸까? httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic(basic -> basic.disable());

        // 필터 적용
        http.with(new CustomSecurityFilterManager(), c -> c.build());

        // Exception 가로채기
        http.exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.unAuthentication(response,"로그인을 진행해 주세요");
        }));

        // 인가 규칙 설정
        http.authorizeHttpRequests(auth -> auth
                // /api/s/** 경로는 인증된 사용자만 접근 가능
                .requestMatchers("/api/s/**").authenticated()
                // /api/admin/** 경로는 ADMIN 권한만 접근 가능
                .requestMatchers("/api/admin/**").hasRole(UserEnum.ADMIN.name()) // ROLE_ 없이도 가능
                // 나머지 요청은 모두 허용
                .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 ( 또는 프론트 엔드 IP만 허용 react 등 )
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 주소 요청에 configuration의 설정을 세팅합니다.
        return source;
    }

}

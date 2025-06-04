package com.project.bank.config.jwt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.bank.config.auth.LoginUser;
import com.project.bank.domain.user.User;
import com.project.bank.domain.user.UserEnum;
import org.junit.jupiter.api.Test;

class JwtProcessTest {

    @Test
    public void create_test() throws Exception {
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        // when
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("테스트 : " + jwtToken);

        // then
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));

    }
    @Test
    public void verify_test() throws Exception {
        // given
        String jwtToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJiYW5rIiwiZXhwIjoxNzQ5NjI4ODEwLCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0.oUjyvV9vupoZS8WIlAgH2YgZ_T-ws91lYw5oTd2a17J4Vx5VGCLQEsEw46bprGBZTLndqIT_7VOJExsKOYCY2A";

        // when
        LoginUser loginUser = JwtProcess.verify(jwtToken);
        System.out.println("테스트 : " + loginUser.getUser().getId());

        // then
        assertThat(loginUser.getUser().getId() == 1L);
        assertThat(loginUser.getUser().getRole() == UserEnum.ADMIN);

    }

}
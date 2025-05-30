package com.project.bank.dto.user;

import com.project.bank.domain.user.User;
import com.project.bank.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {

    @Setter
    @Getter
    public static class LoginResponseDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginResponseDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @Getter
    @Setter
    public static class JoinResponseDto{
        private Long id;
        private String username;
        private String fullname;

        public JoinResponseDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }
}

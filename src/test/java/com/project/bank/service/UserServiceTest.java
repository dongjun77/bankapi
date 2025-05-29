package com.project.bank.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.bank.config.dummy.DummyObject;
import com.project.bank.domain.user.User;
import com.project.bank.domain.user.UserRepository;
import com.project.bank.dto.user.UserRequestDto.JoinRequestDto;
import com.project.bank.dto.user.UserResponseDto.JoinResponseDto;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock // 가짜
    private UserRepository userRepository;

    @Spy // 스파이
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 회원가입_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.setUsername("ssar");
        joinRequestDto.setPassword("1234");
        joinRequestDto.setEmail("ssar@gmail.com");
        joinRequestDto.setFullname("쌀");

        // stub 1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
//        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        // sutb 2
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.save(any())).thenReturn(ssar);

        // when
        JoinResponseDto joinResponseDto = userService.회원가입(joinRequestDto);
        System.out.println("테스트 : " + joinResponseDto);

        // then
        assertThat(joinResponseDto.getId()).isEqualTo(1L);
        assertThat(joinResponseDto.getUsername()).isEqualTo("ssar");
    }

}
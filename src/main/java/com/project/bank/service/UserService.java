package com.project.bank.service;

import com.project.bank.domain.user.User;
import com.project.bank.domain.user.UserEnum;
import com.project.bank.domain.user.UserRepository;
import com.project.bank.dto.user.UserRequestDto.JoinRequestDto;
import com.project.bank.dto.user.UserResponseDto.JoinResponseDto;
import com.project.bank.handler.ex.CustomApiException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 서비스는 dto를 입력받고 dto로 응답한다
    @Transactional // 트랜잭션이 메서드 시작할 때 시작되고 종료될 때 함께 종료
    public JoinResponseDto 회원가입(JoinRequestDto joinRequestDto){
        // 1. 동일 유저네임 존재 검사
        Optional<User> userOP = userRepository.findByUsername(joinRequestDto.getUsername());
        if(userOP.isPresent()){
            // 유저네임 중복되었다는 뜻
            throw new CustomApiException("동일한 username이 존재합니다.");
        }

        // 2. 패스워드 인코딩
        User userPS = userRepository.save(joinRequestDto.toEntity(passwordEncoder));

        // 3. dto 응답
        return new JoinResponseDto(userPS);
    }



}

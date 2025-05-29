package com.project.bank.web;

import com.project.bank.dto.ResponseDto;
import com.project.bank.dto.user.UserRequestDto.JoinRequestDto;
import com.project.bank.dto.user.UserResponseDto.JoinResponseDto;
import com.project.bank.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequestDto joinRequestDto, BindingResult bindingResult) { // String이 아니라 json으로 받을려면 RequestBody

        JoinResponseDto joinResponseDto = userService.회원가입(joinRequestDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원가입 성공", joinResponseDto), HttpStatus.CREATED);
    }



}

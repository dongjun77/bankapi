package com.project.bank.temp;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class RegexTest {

    @Test
    public void 한글만된다_test() throws Exception {
        String value = "한글";
        boolean result = Pattern.matches("^[가-힣]+$",value);
        System.out.println("테스트 : " + result);
    }
    @Test
    public void 한글만안된다_test() throws Exception {
        String value = "trdㅇ";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$",value);
        System.out.println("테스트 : " + result);

    }
    @Test
    public void 영어만된다_test() throws Exception {
        String value = "ssarddddㅇ";
        boolean result = Pattern.matches("^[a-zA-Z]+$",value);
        System.out.println("테스트 : " + result);
    }
    @Test
    public void 영어만안된다_test() throws Exception {
        String value = "ㄹ123";
        boolean result = Pattern.matches("^[^a-zA-Z]*$",value);
        System.out.println("테스트 : " + result);

    }
    @Test
    public void 영어와숫자만만된다_test() throws Exception {
        String value = "ssardddd123ㄴ";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$",value);
        System.out.println("테스트 : " + result);

    }
    @Test
    public void 영어만되고_길이는최소2최대4이다_test() throws Exception {
        String value = "ss";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$",value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_username_test() throws Exception {
        String username = "sd";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$",username);
        System.out.println("테스트 : "+result);
    }
    @Test
    public void user_fullname_test() throws Exception {
        String username = "쌀ㄱㄷㅂㅈㄱㅂㄷㅈㄱㅂㅈㄷㄱㅂㅈㄷㄱㅂㅈㄷㄱㅂㄷㅈㄱㅂㅈㄷㄱㅂㅈㄷㄱㅂㅈㄷㄱ";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$",username);
        System.out.println("테스트 : "+result);
    }

    @Test
    public void user_email_test() throws Exception {
        String username = "ssardddd@gmail.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",username);
        System.out.println("테스트 : "+result);
    }
}

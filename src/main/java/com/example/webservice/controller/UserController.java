package com.example.webservice.controller;

import com.example.webservice.dto.UserDto;
import com.example.webservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
@Slf4j

public class UserController {

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserService userService, JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JavaMailSender javaMailSender;

    @GetMapping("webservice/certification/conform/{email}")
    public void conform(@PathVariable("email") String email) {
        userService.conform(email);
    }

    @GetMapping("/webservice/Mypage/{id}")
    public ResponseEntity change(
            @PathVariable("id") String id,
            @RequestBody UserDto userDto){
        userDto.setId(id);
        userService.change(userDto);
        return ResponseEntity.status(HttpStatus.OK).body("changed Id = "+ id);
    }


    @PostMapping("/webservice")
    public ResponseEntity<String> retrieveAllUser(@RequestBody UserDto userDto){

        String ResponseEmail = userDto.getEmail(); // 회원가입 할 때 적는 email 값 가져오기

        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setSubject("이메일 인증");
        simpleMessage.setText("url : localhost:8080/webservice/certification/conform/"
                + userDto.getEmail() + " 링크로 이동해 회원가입을 해주세요");
        simpleMessage.setTo(ResponseEmail);
        javaMailSender.send(simpleMessage);
        String userPassword = userDto.getPassword();
        String validPassword = userDto.getVerifyPassword();

        if ( userPassword.equals(validPassword)){
            userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 확인하세요");
        }
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


    @PostMapping (value = "/webservice/login")
    public String retrieveAllUsers(@RequestBody UserDto userDto){
        log.info(userDto.toString());
        String ResponsePw = userDto.getPassword();
        String encodePassword = (userService.findId(userDto)).getPassword(); //userService의 로직에 따라 DB에 저장되어 있던 암호화 된 password 불러오기.
        Integer loginkey = (userService.findId(userDto)).getLoginKey();

        if (passwordEncoder.matches(ResponsePw,encodePassword) && loginkey == 1){ //matches를 통해 입력받은 password와 암호화된 패스워드가 같은지 비교
//            return ResponseEntity.status(HttpStatus.OK).header("Access-Control-Allow-Origin","*").body("환영합니다!"); // 성공한다면 OK!
            return "1"; // 1 = 마이페이지 (로그인 성공 시)
        } else if (loginkey != 1){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원가입을 해주세요! 만약 했다면 이메일 인증을 해주세요!"); // 실패한다면 실패!
            return "0"; // 2 = 새로운 페이지 (User의 이메일에 보낸 주소를 다시 보여주며 인증을 해달라고 하기) (이메일 인증 안했을 때)
        }
        else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다!");
            return "2"; // 0 = 로그인페이지 (비밀번호가 틀렸을 때)
        }
    }
}

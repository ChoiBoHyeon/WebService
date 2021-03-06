package com.example.webservice.controller;

import com.example.webservice.dto.UserDto;
import com.example.webservice.service.SecurityService;
import com.example.webservice.service.UserService;
import com.example.webservice.vo.GetUserInfoVo;
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
    public UserController(PasswordEncoder passwordEncoder, UserService userService, JavaMailSender javaMailSender, SecurityService securityService) {
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.securityService = securityService;
    }

    private SecurityService securityService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private JavaMailSender javaMailSender;
    String token;

    @GetMapping("/webservice/certification/conform/{email}")
    public void conform(@PathVariable("email") String email) {
        userService.conform(email);
    }

    @GetMapping("/webservice/Mypage/{id}")
    public ResponseEntity change(
            @PathVariable("id") String id,
            @RequestBody UserDto userDto){
        //securityService.getSubject(token);
        userDto.setId(id);
        userService.change(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userDto); //vo
    }


    @PostMapping("/webservice")
    public ResponseEntity<String> retrieveAllUser(@RequestBody UserDto userDto){

        String ResponseEmail = userDto.getEmail(); // 회원가입 할 때 적는 email 값 가져오기
        String ResponseId = userDto.getId();
        log.info("회원가입 시도");

        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setSubject("이메일 인증");
        simpleMessage.setText("url : http://203.250.32.29:8080/api/webservice/certification/conform/"
                + userDto.getEmail() + " 링크로 이동해 email인증을 해주세요");
        simpleMessage.setTo(ResponseEmail);
        javaMailSender.send(simpleMessage);
        String userPassword = userDto.getPassword();
        String validPassword = userDto.getVerifyPassword();

        if ( userPassword.equals(validPassword)){
            userService.createUser(userDto);

            userService.create(ResponseId);
            log.info("회원가입 완료");
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
        } else {
            log.info("비밀번호 확인하세요");
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
        String encodePassword;
        String RespinseId = userDto.getId();
        try {
            encodePassword = (userService.findId(userDto)).getPassword(); //userService의 로직에 따라 DB에 저장되어 있던 암호화 된 password 불러오기.
            }
        catch (java.lang.NullPointerException e1) {
            return "2"; // 2 = DB에 저장된 아이디가 없을 경우 (아이디가 없을 때)
        }
        Integer loginkey = (userService.findId(userDto)).getLoginKey();

        log.info("로그인 시도");

        if (passwordEncoder.matches(ResponsePw,encodePassword) && loginkey != 1) { //matches를 통해 입력받은 password와 암호화된 패스워드가 같은지 비교
//            return ResponseEntity.status(HttpStatus.OK).header("Access-Control-Allow-Origin","*").body("환영합니다!"); // 성공한다면 OK!
            log.info("이메일 인증 안함");
            return "0"; // 0 = 새로운 페이지 (User의 이메일에 보낸 주소를 다시 보여주며 인증을 해달라고 하기) (이메일 인증 안했을 때)
        } else if (passwordEncoder.matches(ResponsePw,encodePassword) && loginkey == 1){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원가입을 해주세요! 만약 했다면 이메일 인증을 해주세요!"); // 실패한다면 실패!

            token = securityService.createToken(RespinseId, (2*60*1000)); //토큰 만료시간 2분
            log.info("로그인 성공");
            return "1 "; //+ token; // 1 = 마이페이지 (로그인 성공 시)
        }
        else {
            log.info("비밀번호 틀림");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 확인해주세요!");
            return "2"; // 2 = 로그인페이지 (비밀번호가 틀렸을 때)
        }
    }


    @PostMapping (value = "/webservice/findUserid")
    public String FindUserId (@RequestBody UserDto userDto){
        log.info(userDto.toString());
//        String ResponseName = userDto.getName();
//        String ResponseEmail = userDto.getEmail();
        String DBName;
        String DBEmail;
        try {
            DBName = (userService.findName(userDto)).getName();
            }
        catch (java.lang.NullPointerException e) {
            //throw new NullPointerException("유저님의 이름으로 된 아이디가 없습니다.");
            return "0"; // 0 = 검색한 Name이 DB에 저장되지 않았을 경우
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저님의 이름으로 된 아이디가 없습니다.");
        }
        try {
            DBEmail = (userService.findEmail(userDto)).getEmail();
            }
        catch (java.lang.NullPointerException e) {
            //throw new NullPointerException("유저님의 이메일로 된 아이디가 없습니다.");
            return "1"; // 1 = 검색한 Email이 DB에 저장되지 않았을 경우
            }
        String DBId = (userService.findName(userDto)).getId();

        if(DBName != null && DBEmail != null ) {
            return DBId;
        }
        return null;
    }

    @PostMapping (value = "/webservice/findUserpassword")
    public String FindUserPassword (@RequestBody UserDto userDto){
        String ResponseEmail = userDto.getEmail(); // 입력한 email 값 가져오기
        String DBEmail;
        String DBId;
        String ChangeUserPassword = userService.RendomPassword();

        try {
            DBId = (userService.findId(userDto)).getId();
            }
        catch (java.lang.NullPointerException e) {
            return "3"; // 3 = 입력한 ID가 없을 때
        }

        try {
            DBEmail = (userService.findEmail(userDto).getEmail());
        }
        catch (java.lang.NullPointerException e) {
            return "0"; // 0 = 입력한 ID가 없을 때
        }
        if (ResponseEmail.matches(DBEmail) && DBId != null ) {
            SimpleMailMessage simpleMessage = new SimpleMailMessage();
            simpleMessage.setSubject("비밀번호 초기화");
            simpleMessage.setText("비밀번호가 초기화 되었습니다. 초기화 된 비밀번호는 ["
                    + ChangeUserPassword + "] 입니다.");
            simpleMessage.setTo(ResponseEmail);
            javaMailSender.send(simpleMessage);
            userDto.setPassword(ChangeUserPassword);
            userDto.setId(DBId);
            userService.changeRendomPassword(userDto);

            return "1"; // 회원님의 이메일로 임시 비밀번호를 보냈습니다.
        }
        return null;
    }

    @GetMapping("/webservice/test/{id}")
    public ResponseEntity<GetUserInfoVo> getUserInfo(
            @PathVariable("id") String id){
        GetUserInfoVo getUserInfoVo = userService.getMypageInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body(getUserInfoVo); //vo
    }

}


package com.example.webservice.service;

import com.example.webservice.controller.UserNotFoundException;
import com.example.webservice.dto.UserDto;
import com.example.webservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;

    }

    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
//    LocalDate localDate = LocalDate.now();
    public void createUser(UserDto userDto) {

        //해싱
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);

        //날짜 생성
//        userDto.setBirthDate(localDate);

        userMapper.createUser(userDto);
    }

    public void create(String id){
        userMapper.create(id);
    }

    public UserDto findId(UserDto userDto){
        //NPE 예외처리

        return userMapper.findId(userDto);
    }

    public void conform(String email){
        userMapper.conform(email);
    }


    public void change(UserDto userDto){
        userMapper.change(userDto);
    }

    public UserDto findName(UserDto userDto) {
        return userMapper.findName(userDto);
    }

    public UserDto findEmail(UserDto userDto) {
        return userMapper.findEmail(userDto);
    }

}

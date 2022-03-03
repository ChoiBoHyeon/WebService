package com.example.webservice.service;

import com.example.webservice.dto.UserDto;
import com.example.webservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void createUser(UserDto userDto) {

        //해싱
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);

        userMapper.createUser(userDto);
    }

    public UserDto findId(UserDto userDto){
        return userMapper.findId(userDto);
    }

    public void conform(String email){
        userMapper.conform(email);
    }

    public void change(UserDto userDto){
        userMapper.change(userDto);
    }

}

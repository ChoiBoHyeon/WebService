package com.example.webservice.mapper;


import com.example.webservice.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void createUser(UserDto userDto);

    UserDto findId(UserDto userDto);

    void conform(String email);

    void change(UserDto userDto);
}

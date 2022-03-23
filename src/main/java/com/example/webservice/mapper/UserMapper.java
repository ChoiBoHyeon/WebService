package com.example.webservice.mapper;


import com.example.webservice.dto.UserDto;
import com.example.webservice.vo.GetUserInfoVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void createUser(UserDto userDto);

    UserDto findId(UserDto userDto);

    void conform(String email);

    void create(String id);

    void change(UserDto userDto);

    GetUserInfoVo getMypageInfo(String id);

    UserDto findName(UserDto userDto);

    UserDto findEmail(UserDto userDto);

    void changeRendomPassword(UserDto userDto);

}

package com.example.webservice.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class UserDto {

    private String id;
    private String password;
    private String email;
    private String name;
    private String nickName;
    private Integer loginKey;
    private String verifyPassword;
    private LocalDate birthDate;


}

package com.example.webservice.dto;

import lombok.Data;
import javax.persistence.Id;


@Data
public class UserDto {
    @Id
    private Integer id;

    private String password;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private String nickName;
    private String profileImage;
    private Integer loginKey;
    private String verifyPassword;
    private String birthDate;
    private String gender;

    public UserDto(Integer id, String password, String email, String name, String address, String phoneNumber, String nickName,
                   String profileImage, Integer loginKey, String verifyPassword, String birthDate, String gender){
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.loginKey = loginKey;
        this.verifyPassword = verifyPassword;
        this.birthDate = birthDate;
        this.gender = gender;
    }

}

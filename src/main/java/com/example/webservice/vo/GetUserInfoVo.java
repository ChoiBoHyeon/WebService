package com.example.webservice.vo;

import lombok.Data;

@Data
public class GetUserInfoVo {
    private String id;
    private String nickName;
    private String password;
    private String name;
    private String email;
}

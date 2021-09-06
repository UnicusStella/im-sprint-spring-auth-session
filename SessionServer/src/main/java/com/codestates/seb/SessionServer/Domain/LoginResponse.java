package com.codestates.seb.SessionServer.Domain;

import com.codestates.seb.SessionServer.Entity.UserList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    // API 요청에 따른 응답 객체 압니다.
    private UserList data;
    private String message;
}

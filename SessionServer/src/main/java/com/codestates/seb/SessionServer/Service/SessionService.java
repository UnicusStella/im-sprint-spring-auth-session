package com.codestates.seb.SessionServer.Service;

import com.codestates.seb.SessionServer.Domain.LoginResponse;
import com.codestates.seb.SessionServer.Domain.Userdata;
import com.codestates.seb.SessionServer.Entity.UserList;
import com.codestates.seb.SessionServer.Repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final static LoginResponse LOGIN_RESPONSE = new LoginResponse();

    @Autowired
    public SessionService(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    public UserList UserCheck(Userdata userdata){
        for(UserList i : sessionRepository.UserFindByUserId(userdata.getUserId())){
            if(i.getPassword().equals(userdata.getPassword())){
                return i;
            }
        }
        return null;
    }

    public LoginResponse ResponseLog(UserList userList, String log){
        LOGIN_RESPONSE.setData(userList);
        LOGIN_RESPONSE.setMessage(log);
        return LOGIN_RESPONSE;
    }

}

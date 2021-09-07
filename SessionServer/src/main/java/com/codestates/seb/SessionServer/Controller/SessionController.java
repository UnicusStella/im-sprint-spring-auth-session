package com.codestates.seb.SessionServer.Controller;

import com.codestates.seb.SessionServer.Const.SessionConst;
import com.codestates.seb.SessionServer.Domain.Userdata;
import com.codestates.seb.SessionServer.Entity.UserList;
import com.codestates.seb.SessionServer.Service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin("*")
public class SessionController {

    private final SessionService sessionService;
    public static HttpSession session;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping(value = "/")
    public String SessionIndex() {
        return "Hello CodeStates!";
    }

    @PostMapping(value = "/users/login")
    public ResponseEntity<?> UserLogin(@RequestBody(required = true) Userdata user,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        //DB에 유저 정보를 불러옵니다.
        System.out.println(user.getUserId());
        UserList userList = sessionService.UserCheck(user);

        // 전역에 생성 된 session 객체를 사용해야합니다.
        // 유저 정보가 있다면 세션을 생성하고 없다면 로그인을 거절합니다.
        // TODO :
        if (userList != null) {
            session = request.getSession(true);
            session.setAttribute(SessionConst.LOGIN_STATES, userList);
            return ResponseEntity.ok().body(sessionService.ResponseLog(userList, "ok"));
        } else {
            return ResponseEntity.badRequest().body(sessionService.ResponseLog(null, "not authorized"));
        }
    }

    @GetMapping(value = "/users/logout")
    public ResponseEntity<?> UserLogOut() {
        //전역에 생성 된 session 객체를 사용해야합니다.
        //세션을 찾고 세션이 있다면 세션을 지웁니다.
        // TODO :
        if (session == null) {
            return ResponseEntity.badRequest().body(sessionService.ResponseLog(null, "not authorized"));
        } else {
            session.invalidate();
            return ResponseEntity.ok().body(sessionService.ResponseLog(null, "ok"));
        }
    }

    @GetMapping(value = "/users/userinfo")
    public ResponseEntity<?> UserInfo() {
        //전역에 생성 된 session 객체를 사용해야합니다.
        // 세션이 유효하면 유저 정보를 응답합니다.
        // TODO :
        return null;
    }
}

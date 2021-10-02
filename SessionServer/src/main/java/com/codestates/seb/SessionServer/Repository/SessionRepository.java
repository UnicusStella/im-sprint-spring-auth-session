package com.codestates.seb.SessionServer.Repository;

import com.codestates.seb.SessionServer.Entity.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class SessionRepository {

    private final EntityManager entityManager;

    @Autowired
    public SessionRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<UserList> UserFindByUserId(String id) {
        List<UserList> userList = entityManager.createQuery("SELECT user FROM UserList AS user WHERE user.userId ='" + id + "'", UserList.class)
                .getResultList();
        entityManager.close();
        return userList;
    }

}

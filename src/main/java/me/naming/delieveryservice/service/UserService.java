package me.naming.delieveryservice.service;

import me.naming.delieveryservice.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("UserService")
public class UserService {

    @Autowired
    private UserDao userDao;

    public String insertUserInfo(Map<String, String> userInfo) throws Exception{
        String id = userInfo.get("id");
        String password = userInfo.get("password");

        userDao.insertUserInfo(id, password);
        return "success";
    }

}

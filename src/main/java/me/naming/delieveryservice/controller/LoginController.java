package me.naming.delieveryservice.controller;

import me.naming.delieveryservice.dao.UserDao;
import me.naming.delieveryservice.dto.UserDTO;
import me.naming.delieveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/user/info")
    public List userInfo() throws Exception{
        List<UserDTO> objUserInfo = userDao.userInfo();
        return objUserInfo;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sign-in" )
    public String signInUserInfo(@RequestBody Map<String,String> userInfo) throws Exception {
        return userService.insertUserInfo(userInfo);
    }


}
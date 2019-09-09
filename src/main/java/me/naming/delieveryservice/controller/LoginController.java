package me.naming.delieveryservice.controller;

import me.naming.delieveryservice.config.RedisConfig;
import me.naming.delieveryservice.vo.RedisTestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    RedisTestVO redisTestVO = new RedisTestVO();

    @Autowired
    RedisTemplate redisTemplate;


    @RequestMapping(method = RequestMethod.GET,value = "/set-redis")
    public Object setValue() throws Exception {
        redisTemplate.opsForValue().set("testing123", "value");
        return "success";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/get-redis")
    public Object getValue() throws Exception {
        String redisValue = redisTemplate.opsForValue().get("testing123").toString();
        return redisValue;
    }


}

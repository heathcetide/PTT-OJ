package com.cetide.oj.job.cycle;


import com.cetide.oj.model.entity.User;
import com.cetide.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandUpdate {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String LEADERBOARD_KEY = "leaderboard";

    @Autowired
    private UserService userService;


    //每分钟更新一次
    @Scheduled(cron = "0 * * * * ? ")
    public void RandUpdateTask() {
        List<User> users = userService.list();
        for (User user :users){
            redisTemplate.opsForZSet().add(LEADERBOARD_KEY, user.toString(), user.getScore());
        }
        System.out.println("排行榜更新成功");
    }
}

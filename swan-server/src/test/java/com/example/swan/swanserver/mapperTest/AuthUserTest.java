package com.example.swan.swanserver.mapperTest;

import com.example.swan.swanserver.dao.AuthUserMapper;
import com.example.swan.swanserver.model.po.DefaultAuthUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthUserTest {


    @Autowired
    AuthUserMapper authUserMapper;


    @Test
    public void testInsert(){
        DefaultAuthUser defaultAuthUser=new DefaultAuthUser();
        defaultAuthUser.setUsername("111");
        defaultAuthUser.setUserId("111");
        defaultAuthUser.setPassword("111");
        authUserMapper.insert(defaultAuthUser);
    }
}

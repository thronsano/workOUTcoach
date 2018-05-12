package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserModelTest {

    @Autowired
    UserModel userModel;

    @Test
    public void getUserByEmail() {
        User user = userModel.getUserByEmail("sdoe@gmail.com");
        assertEquals(user.getEmail(), "sdoe@gmail.com");
    }
}
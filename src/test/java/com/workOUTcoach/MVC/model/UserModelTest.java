package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.User;
import com.workOUTcoach.security.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserModelTest {

    private User user;

    @Autowired
    UserModel userModel;

    @Before
    public void createUser() {
        user = userModel.getUserByEmail("wiktoria.malawska@wp.pl");
    }

    @Test
    public void getUserByEmail() {
        assertEquals(user.getEmail(), "wiktoria.malawska@wp.pl");
        assertEquals(user.getName(), "Wiktoria");
        assertTrue(user.getSurname().equals("Malawska"));
        assertFalse(user.getSurname().equals("Tomaszewska"));
    }
}
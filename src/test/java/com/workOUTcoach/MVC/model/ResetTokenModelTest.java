package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.ResetToken;
import com.workOUTcoach.entity.User;
import com.workOUTcoach.security.WebSecurityConfig;
import org.junit.After;
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
@Import(WebSecurityConfig.class)
public class ResetTokenModelTest {

    private ResetToken resetToken;

    @Autowired
    ResetTokenModel resetTokenModel;

    @Before
    public void createResetToken() {
        resetToken = new ResetToken("wiktoria.malawska@test.pl", "123");
        resetTokenModel.addResetToken(resetToken);
    }

    @After
    public void deleteResetToken() {
        resetTokenModel.deleteResetToken(resetToken.getEmail());
    }


    @Test
    public void getByResetToken() {
        ResetToken resetTokenTest = resetTokenModel.getByResetToken("123");
        assertEquals(resetTokenTest.getEmail(), resetToken.getEmail());
    }

    @Test
    public void getResetTokenByEmail(){
        ResetToken resetTokenTest = resetTokenModel.getResetTokenByEmail("wiktoria.malawska@test.pl");
        assertEquals(resetTokenTest.getResetToken(), resetToken.getResetToken());
    }

}
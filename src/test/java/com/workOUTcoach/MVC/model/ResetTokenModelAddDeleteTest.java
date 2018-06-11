package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.ResetToken;
import com.workOUTcoach.security.WebSecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResetTokenModelAddDeleteTest {

    private ResetToken resetToken;

    @Autowired
    ResetTokenModel resetTokenModel = null;

    @Test
    public void createResetToken() {
        resetToken = new ResetToken("wiktoria.malawska@test.pl", "123");
        assertTrue(resetTokenModel.addResetToken(resetToken));
    }

    @Test
    public void deleteResetTokenWhenNull() {
        assertTrue(resetTokenModel.deleteResetToken("wiktoria.malawska@test.pl"));
    }

    @Test
    public void deleteResetTokenWhenExists() {
        resetToken = new ResetToken("wiktoria.malawska@test.pl", "123");
        assertTrue(resetTokenModel.addResetToken(resetToken));
        assertTrue(resetTokenModel.deleteResetToken("wiktoria.malawska@test.pl"));
        assertTrue(resetTokenModel.deleteResetToken("wiktoria.malawska@test.pl"));
    }
}
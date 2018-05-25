package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ResetTokenModel;
import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.ResetToken;
import com.workOUTcoach.entity.User;
import com.workOUTcoach.security.EmailService;
import com.workOUTcoach.security.WebSecurityConfig;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordControllerTest {

    @Autowired
    ResetTokenModel resetTokenModel;

    @Autowired
    UserModel usermodel;

    @Autowired
    PasswordController passwordController;

    @Autowired
    SessionFactory sessionFactory;

    @Mock
    HttpServletRequest request;

    @Mock
    RedirectAttributes redir;

    ModelAndView modelAndView;

    @Test
    public void displayForgotPasswordPage() {

        modelAndView = new ModelAndView("forgotPassword");
        assertEquals(modelAndView.getViewName(), passwordController.displayForgotPasswordPage().getViewName());
    }

    @Test
    public void processForgotPasswordForm() {
        ResetToken resetToken = new ResetToken("wiktoria.malawska@test.pl", "123");
        assertTrue(resetTokenModel.addResetToken(resetToken));

        modelAndView = passwordController.processForgotPasswordForm(new ModelAndView("forgot"), "wiktoria.malawska@wp.pl", request);
        assertTrue((Boolean) modelAndView.getModel().get("successEmail"));
    }

    @Test
    public void displayResetPasswordPage() {
        ModelAndView modelAndView = new ModelAndView("resetPassword");
        assertEquals(modelAndView.getViewName(), passwordController.displayResetPasswordPage(new ModelAndView()).getViewName());
    }

    @Test
    public void setNewPassword() {
        //@TODO: this functionality needs to be tested
        modelAndView = passwordController.setNewPassword(new ModelAndView("reset"), "123", "password", "password", redir);

    }

}
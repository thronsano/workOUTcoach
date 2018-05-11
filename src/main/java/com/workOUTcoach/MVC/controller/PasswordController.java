package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.User;
import com.workOUTcoach.security.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class PasswordController {

    @Autowired
    private UserModel userModel;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Display forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    // Process form submission from forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

        // Lookup user in database by e-mail
        User user = userModel.getUserByEmail(userEmail);

        if (user == null) {
            modelAndView.addObject("errorUser", true);
        } else {
            // Generate random 36-character string token for reset password
            user.setResetToken(UUID.randomUUID().toString());
            userModel.saveUsersResetToken(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("workOUT.coach@wp.pl");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":8080"
                    + "/reset?token=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);
            modelAndView.addObject("successEmail", true);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;

    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView) {

        modelAndView.setViewName("resetPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView, @RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirm") String confirmPassword, RedirectAttributes redir) {
        User user;
        if (token != null && !token.equals("")) {
            user = userModel.getUserByResetToken(token);

            if (user != null) {
                if(password.equals(confirmPassword)) {
                    user.setPassword(bCryptPasswordEncoder.encode(password));
                    user.setResetToken(null);
                    userModel.saveUsersPasswordAndToken(user);

                    //@TODO: sending object to "login" to display message
                    redir.addFlashAttribute("successMessage", true);
                    modelAndView.setViewName("redirect:login");
                    return modelAndView;
                }else {
                    modelAndView.addObject("errorDifferentPasswords", true);
                }
            } else {
                modelAndView.addObject("errorUser", true);
            }
        } else {
            modelAndView.addObject("errorTokenLost", true);
            modelAndView.setViewName("resetPassword");
        }

        return modelAndView;
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }
}


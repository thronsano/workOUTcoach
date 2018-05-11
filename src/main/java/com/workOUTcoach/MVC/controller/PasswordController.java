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

            // Save token to database
            userModel.saveUser(user);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom("workOUT.coach@wp.pl");
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":8080"
                    + "/reset?token=" + user.getResetToken());

            emailService.sendEmail(passwordResetEmail);

            // Add success message to view
            modelAndView.addObject("successMessage", "A password reset link has been sent to " + userEmail);
        }

        modelAndView.setViewName("forgotPassword");
        return modelAndView;

    }

    // Display form to reset password
    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView, @RequestParam("token") String token) {

        User user = userModel.getUserByResetToken(token);

        if (user != null) { // Token found in DB
            modelAndView.addObject("token", user.getResetToken());
        } else { // Token not found in DB
            modelAndView.addObject("errorUser", true);
        }
        modelAndView.setViewName("resetPassword");
        return modelAndView;
    }

    // Process reset password form
    @RequestMapping(value = "/reset{token}", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView, @PathVariable(value="token") String token, @RequestParam("password") String password, RedirectAttributes redir) {

        //String token = (String)modelAndView.getModelMap().get("token");

        System.out.println("---------------------------------------------------------------------------" + token);

        // Find the user associated with the reset token
        User user;
        if (token != null && !token.equals("")) {
            user = userModel.getUserByResetToken(token);

            // This should always be non-null but we check just in case
            if (user != null) {

                // Set new password
                user.setPassword(bCryptPasswordEncoder.encode(password));

                // Set the reset token to null so it cannot be used again
                user.setResetToken(null);

                // Save user
                userModel.saveUser(user);

                // In order to set a model attribute on a redirect, we must use
                // RedirectAttributes
                redir.addFlashAttribute("successMessage", true);

                modelAndView.setViewName("redirect:login");
                return modelAndView;
            } else {
                modelAndView.addObject("errorMessage", true);

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


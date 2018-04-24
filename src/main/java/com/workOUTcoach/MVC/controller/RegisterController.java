package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.Authority;
import com.workOUTcoach.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    UserModel userModel = new UserModel();

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register")
    public String getRegisterPage(){
        return "register";
    }

    @RequestMapping(value="/login_featured")
    public String createAccount(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("securityQuestion") String sQuestion,
            @RequestParam("securityAnswer") String sAnswer,
            Model model
            ){

        if(userModel.getUserByEmail(email)!=null) {
            model.addAttribute("error", "emailError");
            return "register";
        }

        if(validateString(email) && validateString(password) && validateString(name) && validateString(surname) && validateString(sQuestion) && validateString(sAnswer)) {
            if(password.equals(password2)) {
                password = passwordEncoder.encode(password);
                User user = new User(email, password, name, surname, sQuestion, sAnswer);
                Authority authority = new Authority(user);

                if (userModel.addUser(user, authority)) {
                    model.addAttribute("success", true);
                    return "login";
                }
            }else{
                model.addAttribute("error","passwordError");
                return "register";
            }
        } else
            model.addAttribute("error", "dataError");
        return "register";
    }

    private boolean validateString(String text){
        if(text==null||text.isEmpty())
            return false;
        return true;
    }
}

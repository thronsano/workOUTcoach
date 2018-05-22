package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.Authority;
import com.workOUTcoach.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    UserModel userModel = new UserModel();

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getRegisterPage(ModelAndView modelAndView) {
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createAccount(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        String result = userModel.createUser(email, password, password2, name, surname, passwordEncoder);

        if (result.equals("correct")) {
            redirectAttributes.addFlashAttribute("userCreated", true);
            modelAndView.setViewName("redirect:/");
        } else {
            modelAndView.addObject("error", result);
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }
}

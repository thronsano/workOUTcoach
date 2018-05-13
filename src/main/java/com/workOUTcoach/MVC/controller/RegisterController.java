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
public class RegisterController {

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

        if (userModel.getUserByEmail(email) != null) {
            modelAndView.addObject("error", "emailError");
            modelAndView.setViewName("register");
            return modelAndView;
        }

        if (validateString(email) && validateString(password) && validateString(name) && validateString(surname)) {
            if (password.equals(password2)) {
                password = passwordEncoder.encode(password);
                User user = new User(email, password, name, surname);
                Authority authority = new Authority(user);

                if (userModel.addUser(user, authority)) {
                    redirectAttributes.addFlashAttribute("userCreated", true);
                    modelAndView.setViewName("redirect:/");
                }
            } else {
                modelAndView.addObject("error", "passwordError");
                modelAndView.setViewName("register");
            }
        } else {
            modelAndView.addObject("error", "dataError");
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }
}

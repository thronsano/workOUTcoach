package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ClientModel;
import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.Client;
import com.workOUTcoach.entity.Client;
import com.workOUTcoach.utility.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientController {

    @Autowired
    ClientModel clientModel;

    @Autowired
    UserModel userModel;

    @RequestMapping(value = "/clientList", method = RequestMethod.GET)
    public String ListClients(Model model) {
        model.addAttribute("clients", clientModel.getAllUserClients());
        return "clientList";
    }

    @RequestMapping(value = "/clientProfile", method = RequestMethod.GET)
    public String getClient(@RequestParam(value = "id") String id, Model model) {
        Client client = clientModel.getClient(id);
        model.addAttribute("client",client);
        return ("/clientProfile");
    }

    @RequestMapping(value = "/clientList/addClient", method = RequestMethod.GET)
    public ModelAndView addClient(ModelAndView modelAndView){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
        modelAndView.setViewName("addClient");
        return modelAndView;
    }

    @RequestMapping(value = "/clientList/addClient", method = RequestMethod.POST)
    public ModelAndView addClient(
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("gymName") String gymName,
            @RequestParam("goal") String goal,
            @RequestParam("condition") String condition,
            @RequestParam("phoneNumber") String phoneNumber,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (validateString(name) && validateString(surname)) {
                Client client = new Client(name, surname, auth.getName(), gymName, goal, condition, true, phoneNumber);

                if (clientModel.addClient(client)) {
                    redirectAttributes.addFlashAttribute("clientCreated", true);
                    modelAndView.setViewName("redirect:/clientList");
                }

        } else {
            modelAndView.addObject("error", "dataError");
            modelAndView.setViewName("addClient");
        }
        return modelAndView;
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }
}

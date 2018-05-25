package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ClientModel;
import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.Client;
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

@Controller
public class ClientController {

    @Autowired
    ClientModel clientModel;

    @Autowired
    UserModel userModel;

    @RequestMapping(value = "/clientList", method = RequestMethod.GET)
    public String ListClients(Model model) {
        model.addAttribute("clients", clientModel.getActiveUserClients());
        return "clientList";
    }

    @RequestMapping(value = "/clientProfile", method = RequestMethod.GET)
    public ModelAndView getClient(@RequestParam(value = "id") String id, Model model, ModelAndView modelAndView) {
        try {
            Client client = clientModel.getClient(id);
            model.addAttribute("client", client);
            boolean isActive = clientModel.isActive(id);

            if (isActive) {
                modelAndView.addObject("isActive", isActive);
                modelAndView.setViewName("clientProfile");
            } else {
                modelAndView.addObject("isActive", isActive);
                modelAndView.setViewName("clientProfile");
            }
        } catch (NullPointerException ex) {
            modelAndView.addObject("error", "noClientError");
            modelAndView.setViewName("clientProfile");
        } catch (Exception ex) {
            modelAndView.addObject("error", "incorrectIDError");
            modelAndView.setViewName("clientProfile");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/clientList/addClient", method = RequestMethod.GET)
    public ModelAndView addClient(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
        modelAndView.setViewName("addClient");
        return modelAndView;
    }

    @RequestMapping(value = "/clientList/archived", method = RequestMethod.GET)
    public ModelAndView showArchivedClients(ModelAndView modelAndView, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
        modelAndView.setViewName("archived");
        model.addAttribute("archivedClients", clientModel.getArchivedUserClients());
        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile/makeActive", method = RequestMethod.POST)
    public ModelAndView makeActive(ModelAndView modelAndView, @RequestParam(value = "clientID") String id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean result = clientModel.setActive(id);
        if (result) {
            redirectAttributes.addFlashAttribute("activationSuccess", true);
            redirectAttributes.addFlashAttribute("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.setViewName("redirect:/clientProfile?id=" + id);
        } else {
            modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.addObject("id", id);
            modelAndView.addObject("error", "activationError");
            modelAndView.setViewName("clientProfile");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile/makeArchived", method = RequestMethod.POST)
    public ModelAndView makeArchived(ModelAndView modelAndView, @RequestParam(value = "clientID") String id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean result = clientModel.archive(id);
        if (result) {
            redirectAttributes.addFlashAttribute("archivingSuccess", true);
            redirectAttributes.addFlashAttribute("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.setViewName("redirect:/clientProfile?id=" + id);
        } else {
            modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.addObject("id", id);
            modelAndView.addObject("error", "archivingError");
            modelAndView.setViewName("clientProfile");
        }

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
        String result = clientModel.createClient(name, surname, auth.getName(), gymName, goal, condition, phoneNumber);

        if (result.equals("correct")) {
            redirectAttributes.addFlashAttribute("clientCreated", true);
            modelAndView.setViewName("redirect:/clientList");
        } else {
            modelAndView.addObject("error", result);
            modelAndView.setViewName("addClient");
        }

        return modelAndView;
    }
}

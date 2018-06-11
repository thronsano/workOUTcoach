package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ClientModel;
import com.workOUTcoach.MVC.model.SchemeModel;
import com.workOUTcoach.MVC.model.UserModel;
import com.workOUTcoach.entity.Client;
import com.workOUTcoach.utility.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class ClientController {

    @Autowired
    private ClientModel clientModel;

    @Autowired
    private UserModel userModel;

    @Autowired
    private SchemeModel schemeModel;

    @RequestMapping(value = "/clientList", method = RequestMethod.GET)
    public ModelAndView ListClients(Model model, ModelAndView modelAndView) {
        model.addAttribute("clients", clientModel.getActiveUserClients());
        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile", method = RequestMethod.GET)
    public ModelAndView getClient(@RequestParam(value = "id") String id, Model model, ModelAndView modelAndView) {
        modelAndView.addObject("usedSchemeList", schemeModel.getUsedSchemeListByClient(Integer.parseInt(id)));
        modelAndView.addObject("unusedSchemeList", schemeModel.getUnusedSchemeListByClient(Integer.parseInt(id)));

        try {
            Client client = clientModel.getClientById(Integer.parseInt(id));
            model.addAttribute("client", client);
            boolean isActive = clientModel.isActiveById(Integer.parseInt(id));

            if (isActive) {
                modelAndView.addObject("isActive", isActive);
                modelAndView.setViewName("clientProfile");
            } else {
                modelAndView.addObject("isActive", isActive);
                modelAndView.setViewName("clientProfile");
            }
        } catch (NullPointerException ex) {
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", "Client not found!");
            modelAndView.setViewName("clientProfile");
        } catch (Exception ex) {
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", "Client ID is incorrect!");
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

    /**
     * @RequestMapping(value = "/clientProfile/goal", method = RequestMethod.GET)
     * public ModelAndView editGoal(@RequestParam(value = "clientID") String id,
     * @RequestParam(value = "goal", required = false) String goal,
     * ModelAndView modelAndView) {
     * try {
     * clientModel.editClientDetails(Integer.parseInt(id), goal, "goal");
     * } catch (Exception ex) {
     * ex.getMessage();
     * }
     * <p>
     * modelAndView.setViewName("redirect:/clientProfile?id=" + id);
     * return modelAndView;
     * }
     * @RequestMapping(value = "/clientProfile/health", method = RequestMethod.GET)
     * public ModelAndView editHealth(@RequestParam(value = "clientID") String id,
     * @RequestParam(value = "health", required = false) String health,
     * ModelAndView modelAndView) {
     * try {
     * clientModel.editClientDetails(Integer.parseInt(id), health, "health");
     * } catch (Exception ex) {
     * ex.getMessage();
     * }
     * <p>
     * modelAndView.setViewName("redirect:/clientProfile?id=" + id);
     * return modelAndView;
     * }
     * @RequestMapping(value = "/clientProfile/gym", method = RequestMethod.GET)
     * public ModelAndView editGym(@RequestParam(value = "clientID") String id,
     * @RequestParam(value = "gym", required = false) String gym,
     * ModelAndView modelAndView) {
     * try {
     * clientModel.editClientDetails(Integer.parseInt(id), gym, "gym");
     * } catch (Exception ex) {
     * ex.getMessage();
     * }
     * <p>
     * modelAndView.setViewName("redirect:/clientProfile?id=" + id);
     * return modelAndView;
     * }
     * @RequestMapping(value = "/clientProfile/phone", method = RequestMethod.GET)
     * public ModelAndView editPhone(@RequestParam(value = "clientID") String id,
     * @RequestParam(value = "phone", required = false) String phone,
     * ModelAndView modelAndView) {
     * try {
     * clientModel.editClientDetails(Integer.parseInt(id), phone, "phone");
     * } catch (Exception ex) {
     * ex.getMessage();
     * }
     * <p>
     * modelAndView.setViewName("redirect:/clientProfile?id=" + id);
     * return modelAndView;
     * }
     **/

    @RequestMapping(value = "/clientProfile/edit", method = RequestMethod.POST)
    public ModelAndView editClient(@RequestParam("clientID") String id,
                                   @RequestParam("goal") String goal,
                                   @RequestParam("healthCondition") String healthCondition,
                                   @RequestParam("phoneNumber") String phoneNumber,
                                   @RequestParam("gymName") String gymName,
                                   ModelAndView modelAndView) {
        try {
            clientModel.editClientDetails(Integer.parseInt(id), goal, healthCondition, phoneNumber, gymName);
        } catch (Exception ex) {
            modelAndView.addObject("error", "failed");
            modelAndView.addObject("reason", ex.getMessage());
            modelAndView.setViewName("clientProfile");
        }

        modelAndView.setViewName("redirect:/clientProfile?id=" + id);
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
    public ModelAndView makeActive(ModelAndView modelAndView,
                                   @RequestParam(value = "clientID") String id,
                                   RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            clientModel.activateById(Integer.parseInt(id));

            redirectAttributes.addFlashAttribute("activationSuccess", true);
            redirectAttributes.addFlashAttribute("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.setViewName("redirect:/clientProfile?id=" + id);
        } catch (Exception ex) {
            modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.addObject("id", id);
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", ex.getMessage());
            modelAndView.setViewName("clientProfile");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile/deleteClient", method = RequestMethod.POST)
    public ModelAndView deleteClient(ModelAndView modelAndView, @RequestParam(value = "clientID2") String id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            clientModel.deleteById(Integer.parseInt(id));
            redirectAttributes.addFlashAttribute("deleteSuccess", true);
            redirectAttributes.addFlashAttribute("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.setViewName("redirect:/clientList");
        } catch (Exception ex) {
            modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.addObject("id", id);
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", ex.getMessage());
            modelAndView.setViewName("clientProfile");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile/makeArchived", method = RequestMethod.POST)
    public ModelAndView makeArchived(ModelAndView modelAndView, @RequestParam(value = "clientID") String id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {
            clientModel.archiveById(Integer.parseInt(id));

            redirectAttributes.addFlashAttribute("archivingSuccess", true);
            redirectAttributes.addFlashAttribute("user", userModel.getUserByEmail(auth.getName()));
            modelAndView.setViewName("redirect:/clientProfile?id=" + id);
        } catch (Exception ex) {

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
        try {
            clientModel.createClient(name, surname, auth.getName(), gymName, goal, condition, phoneNumber);
            redirectAttributes.addFlashAttribute("clientCreated", true);
            modelAndView.setViewName("redirect:/clientList");
        } catch (Exception ex) {
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", ex.getMessage());
            modelAndView.setViewName("addClient");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/clientProfile/progress", method = RequestMethod.POST)
    public ModelAndView editProgress(@RequestParam("clientID") String id,
                                     @RequestParam("goalValue") int goalValue,
                                     ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        try {
            clientModel.editProgress(Integer.parseInt(id), goalValue);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("schemeRemoved", "failed");
            redirectAttributes.addFlashAttribute("reason", ex.getMessage());
        }
        modelAndView.setViewName("redirect:/clientProfile?id=" + id);
        return modelAndView;
    }
}

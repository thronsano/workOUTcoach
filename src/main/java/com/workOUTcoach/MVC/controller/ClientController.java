package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ClientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/clientList")
public class ClientController {

    @Autowired
    ClientModel clientModel;

    @RequestMapping(method = RequestMethod.GET)
    public String ListClients(Model model){
        model.addAttribute("clients", clientModel.getAllClients());
        return "clientList";
    }

}

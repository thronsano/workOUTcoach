package com.workOUTcoach.controller;

import com.workOUTcoach.entity.Client;
import com.workOUTcoach.model.ClientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientModel clientModel;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Client> getAllClients() {
        return clientModel.getAllClients();
    }

}

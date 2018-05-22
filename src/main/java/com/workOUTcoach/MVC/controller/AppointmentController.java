package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.utility.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppointmentController {

    @RequestMapping(value="/addAppointment", method = RequestMethod.GET)
    public ModelAndView setAppointment(@RequestParam("id") String id, ModelAndView modelAndView) {
        Logger.log(id);

        return modelAndView;
    }
}

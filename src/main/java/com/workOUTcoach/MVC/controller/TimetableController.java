package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.TimetableModel;
import com.workOUTcoach.utility.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

@Controller
public class TimetableController {

    @Autowired
    TimetableModel timetableModel;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView listAppointments(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         @RequestParam(value = "showCancelled", required = false, defaultValue = "false") boolean showCancelled,
                                         Model model,
                                         ModelAndView modelAndView) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        model.addAttribute("timetable", timetableModel.listAppointments(offset, showCancelled));
        model.addAttribute("startingDate", (timetableModel.setBeginningDate(offset).format(dateFormatter)));
        model.addAttribute("endingDate", (timetableModel.setEndingDate(offset)).format(dateFormatter));
        model.addAttribute("showCancelled", showCancelled);
        return modelAndView;
    }

}

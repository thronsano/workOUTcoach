package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.AppointmentModel;
import com.workOUTcoach.MVC.model.SchemeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping(value = "/addAppointment")
public class AppointmentController {

    @Autowired
    private AppointmentModel appointmentModel;

    @Autowired
    private SchemeModel schemeModel;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAppointmentPage(ModelAndView modelAndView) {
        modelAndView.setViewName("addAppointment");
        modelAndView.addObject("schemeList", schemeModel.schemeList());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView setAppointment(@RequestParam("id") String id,
                                       @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                       @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                       @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                       @RequestParam(value = "repeat", required = false, defaultValue = "false") boolean cyclic,
                                       @RequestParam(value = "repeatAmount", required = false, defaultValue = "0") int repeatAmount,
                                       @RequestParam(value = "cycle", required = false, defaultValue = "false") boolean partOfCycle,
                                       @RequestParam(value = "schemeId", required = false, defaultValue = "-1") int schemeId,
                                       ModelAndView modelAndView) {
        try {
            LocalDateTime localDateTimeStart = LocalDateTime.of(startDate, startTime);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(startDate, endTime);

            appointmentModel.setAppointment(Integer.parseInt(id), localDateTimeStart, localDateTimeEnd, cyclic, repeatAmount, partOfCycle, schemeId);

            modelAndView.addObject("status", "successful");
            modelAndView.addObject("schemeList", schemeModel.schemeList());
        } catch (Exception ex) {
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", ex.getMessage());
            modelAndView.addObject("schemeList", schemeModel.schemeList());
        }

        return modelAndView;
    }
}

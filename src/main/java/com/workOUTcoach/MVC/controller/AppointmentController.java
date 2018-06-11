package com.workOUTcoach.MVC.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.workOUTcoach.MVC.model.AppointmentModel;
import com.workOUTcoach.MVC.model.SchemeModel;
import com.workOUTcoach.entity.Appointment;
import com.workOUTcoach.utility.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentModel appointmentModel;

    @Autowired
    private SchemeModel schemeModel;

    @RequestMapping(value = "/addAppointment", method = RequestMethod.GET)
    public ModelAndView getAppointmentPage(ModelAndView modelAndView) {
        modelAndView.setViewName("addAppointment");
        modelAndView.addObject("schemeList", schemeModel.schemeList());
        return modelAndView;
    }

    @RequestMapping(value = "/addAppointment", method = RequestMethod.POST)
    public ModelAndView setAppointment(@RequestParam("id") String id,
                                       @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                       @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                       @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                       @RequestParam(value = "repeat", required = false, defaultValue = "false") boolean cyclic,
                                       @RequestParam(value = "repeatAmount", required = false, defaultValue = "1") int repeatAmount,
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

    @RequestMapping(value = "/appointmentPage", method = RequestMethod.GET)
    public ModelAndView getAppointment(@RequestParam(value = "id") String id, ModelAndView modelAndView) {
        try {
            modelAndView.addObject("appointment", appointmentModel.getAppointment(Integer.parseInt(id)));
            modelAndView.addObject("exercises", schemeModel.listExercise(Integer.parseInt(id)));
            modelAndView.addObject("schemeList", schemeModel.listSchemeByAppointmentId(Integer.parseInt(id)));
        } catch (NullPointerException ex) {
            appointmentNotFound(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/appointmentPage/cancel", method = RequestMethod.POST)
    public ModelAndView cancelAppointment(@RequestParam(value = "appointmentID") String id, ModelAndView modelAndView) {
        try {
            appointmentModel.setCancelledValue(true, Integer.parseInt(id));
            modelAndView.setViewName("redirect:/appointmentPage?id=" + id);
        } catch (NullPointerException ex) {
            appointmentNotFound(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/appointmentPage/active", method = RequestMethod.POST)
    public ModelAndView activeAppointment(@RequestParam(value = "appointmentID") String id, ModelAndView modelAndView) {
        try {
            appointmentModel.setCancelledValue(false, Integer.parseInt(id));
            modelAndView.setViewName("redirect:/appointmentPage?id=" + id);
        } catch (NullPointerException ex) {
            appointmentNotFound(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/appointmentPage/delete", method = RequestMethod.POST)
    public ModelAndView deleteAppointment(@RequestParam(value = "appointmentID") String id, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {
        try {
            appointmentModel.deleteAppointment(Integer.parseInt(id));
            redirectAttributes.addFlashAttribute("deleteSuccess", true);
            modelAndView.setViewName("redirect:/home");
        } catch (Exception ex) {
            appointmentNotFound(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/appointmentPage/edit", method = RequestMethod.POST)
    public ModelAndView editAppointment(@RequestParam("appointmentID") String id,
                                        @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                        @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                        @RequestParam(value = "schemeId", required = false, defaultValue = "-1") int schemeId,
                                        ModelAndView modelAndView) {

        try {
            LocalDateTime localDateTimeStart = LocalDateTime.of(startDate, startTime);
            LocalDateTime localDateTimeEnd = LocalDateTime.of(startDate, endTime);

            appointmentModel.updateAppointment(Integer.parseInt(id), localDateTimeStart, localDateTimeEnd, schemeId);
        } catch (Exception ex) {
            modelAndView.addObject("status", "failed");
            modelAndView.addObject("reason", ex.getMessage());
        }

        modelAndView.setViewName("redirect:/appointmentPage?id=" + id);
        return modelAndView;
    }

    private void appointmentNotFound(ModelAndView modelAndView) {
        modelAndView.addObject("status", "failed");
        modelAndView.addObject("reason", "Appointment not found!");
        modelAndView.setViewName("appointmentPage");
    }
}

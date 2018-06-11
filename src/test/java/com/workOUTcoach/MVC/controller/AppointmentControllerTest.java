package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.entity.Appointment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerTest {

    @Autowired
    AppointmentController appointmentController;

    ModelAndView modelAndView;

    @Before
    public void setUp() {
        modelAndView = new ModelAndView("addAppointment");
    }

    @Test
    public void getRegisterPage() {
        //assertEquals(modelAndView.getViewName(), appointmentController.getAppointmentPage(new ModelAndView()).getViewName());
    }
}

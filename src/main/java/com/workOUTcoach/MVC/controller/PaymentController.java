package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.PaymentModel;
import com.workOUTcoach.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    PaymentModel paymentModel;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPayments(ModelAndView modelAndView, Model model){

        for (Payment p : paymentModel.getPaymentsByUser()) {
            System.out.println("------------------------------------" + p.getId());
            
        }

        model.addAttribute("payments", paymentModel.getPaymentsByUser());

        modelAndView.setViewName("payments");
        return modelAndView;
    }
}

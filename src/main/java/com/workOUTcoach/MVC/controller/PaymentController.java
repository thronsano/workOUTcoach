package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.PaymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/payments")
public class PaymentController {

    @Autowired
    PaymentModel paymentModel;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPayments(ModelAndView modelAndView, Model model) {
        model.addAttribute("payments", paymentModel.getPaymentsByUser());

        modelAndView.setViewName("payments");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/changePaidValue")
    public ModelAndView postChangePayment(
            @RequestParam(value = "paymentID") int paymentID,
            ModelAndView modelAndView, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            paymentModel.editIsPaid(paymentID);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        redirectAttributes.addFlashAttribute("payments", paymentModel.getPaymentsByUser());
        modelAndView.setViewName("redirect:/payments");

        return modelAndView;
    }

}

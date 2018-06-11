package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ExerciseModel;
import com.workOUTcoach.MVC.model.SchemeModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "clientProfile/training/scheme")
public class ExerciseController {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    ExerciseModel exerciseModel;

    @Autowired
    SchemeModel schemeModel;

    @RequestMapping(value = "/newExercise", method = RequestMethod.GET)
    public ModelAndView getNewExercise(ModelAndView modelAndView,
                                       @RequestParam(value = "schemeId") int schemeID) {
        modelAndView.addObject("scheme", schemeModel.getSchemeById(schemeID));
        modelAndView.setViewName("newExercise");
        return modelAndView;
    }


    @RequestMapping(value = "/newExercise", method = RequestMethod.POST)
    public ModelAndView postNewExercise(ModelAndView modelAndView,
                                        @RequestParam(value = "schemeID") int schemeID,
                                        @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                        @RequestParam(value = "repetitions", required = false, defaultValue = "0") int repetitions,
                                        RedirectAttributes redirectAttributes) {
        exerciseModel.addNewExercise(schemeID, name, repetitions);
        redirectAttributes.addFlashAttribute("scheme", schemeModel.getSchemeById(schemeID));
        modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId=" + schemeID);
        return modelAndView;
    }
}

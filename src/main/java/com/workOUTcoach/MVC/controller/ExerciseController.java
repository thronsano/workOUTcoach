package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ExerciseModel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "clientProfile/training/scheme")
public class ExerciseController {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    ExerciseModel exerciseModel;

    @RequestMapping(value = "/newExercise", method = RequestMethod.GET)
    public ModelAndView editTitle(ModelAndView modelAndView,
                                  @RequestParam(value = "schemeId") int schemeID){
        modelAndView.setViewName("newExercise");
        return modelAndView;
    }

}

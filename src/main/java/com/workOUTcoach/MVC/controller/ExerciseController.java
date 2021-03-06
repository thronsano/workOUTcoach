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
        try {
            exerciseModel.addNewExercise(schemeID, name, repetitions);
            modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId=" + schemeID);
        }catch (Exception e){
            modelAndView.setViewName("redirect:/clientProfile/training/scheme/newExercise?schemeId=" + schemeID);
            redirectAttributes.addFlashAttribute("dataError", true);
        }
        redirectAttributes.addFlashAttribute("scheme", schemeModel.getSchemeById(schemeID));
        return modelAndView;
    }

    @RequestMapping(value = "/editExercise", method = RequestMethod.GET)
    public ModelAndView getEditExercise(ModelAndView modelAndView,
                                       @RequestParam(value = "exerciseId") int exerciseId) {
        modelAndView.addObject("exercise", exerciseModel.getExerciseById(exerciseId));
        modelAndView.setViewName("editExercise");
        return modelAndView;
    }

    @RequestMapping(value = "/editExercise", method = RequestMethod.POST)
    public ModelAndView postEditExercise(ModelAndView modelAndView,
                                        @RequestParam(value = "schemeID") int schemeID,
                                        @RequestParam(value = "exerciseID") int exerciseID,
                                        @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                        @RequestParam(value = "repetitions", required = false, defaultValue = "0") int repetitions,
                                        RedirectAttributes redirectAttributes) {
        try {
            exerciseModel.editExerciseById(exerciseID, name, repetitions);
            modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId=" + schemeID);
            redirectAttributes.addFlashAttribute("editSuccess", true);
        }catch (Exception e){
            modelAndView.setViewName("redirect:/clientProfile/training/scheme/editExercise?exerciseId=" + schemeID);
            redirectAttributes.addFlashAttribute("dataError", true);
        }
        redirectAttributes.addFlashAttribute("scheme", schemeModel.getSchemeById(schemeID));
        return modelAndView;
    }
}

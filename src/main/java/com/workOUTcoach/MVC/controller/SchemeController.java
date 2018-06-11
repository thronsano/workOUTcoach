package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.ExerciseModel;
import com.workOUTcoach.MVC.model.SchemeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/clientProfile/training")
public class SchemeController {

    @Autowired
    SchemeModel schemeModel;

    @Autowired
    ExerciseModel exerciseModel;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSettings(ModelAndView modelAndView,
                                    @RequestParam("id") int clientID) {
        modelAndView.addObject("usedSchemeList", schemeModel.getUsedSchemeListByClient(clientID));
        modelAndView.addObject("unusedSchemeList", schemeModel.getUnusedSchemeListByClient(clientID));
        modelAndView.addObject("schemes", schemeModel.getSchemeListByClientId(clientID));


        modelAndView.setViewName("schemeList");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scheme")
    public ModelAndView getScheme(ModelAndView modelAndView,
                                  @RequestParam("schemeId") int schemeID,
                                  @RequestParam(value = "edit", required = false, defaultValue = "0") boolean edit) {

        modelAndView.addObject("scheme", schemeModel.getSchemeById(schemeID));
        modelAndView.addObject("edit", edit);
        modelAndView.addObject("exercises", exerciseModel.getExerciseListBySchemeId(schemeID));
        modelAndView.setViewName("schemeSettings");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/scheme/deleteExercise")
    public ModelAndView postDeleteExercise(ModelAndView modelAndView,
                                           @RequestParam(value = "exerciseId", required = false, defaultValue = "-1") int exerciseID,
                                           @RequestParam("id") int schemeID,
                                           RedirectAttributes redirectAttributes) {

        try {
            exerciseModel.deleteExercise(exerciseID);
            redirectAttributes.addFlashAttribute("exerciseDeleted", "successful");
            redirectAttributes.addFlashAttribute("exercises", exerciseModel.getExerciseListBySchemeId(schemeID));
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("exerciseDeleted", "failed");
            redirectAttributes.addFlashAttribute("reason", e.getMessage());
            redirectAttributes.addFlashAttribute("exercises", exerciseModel.getExerciseListBySchemeId(schemeID));
        }

        modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId="+schemeID);
        return modelAndView;
    }

    @RequestMapping(value = "/scheme/edit", method = RequestMethod.POST)
    public ModelAndView editTitle(@RequestParam(value = "schemeID") int schemeID,
                                  @RequestParam(value = "title", required = false) String title,
                                  ModelAndView modelAndView) {
        try {
            schemeModel.editTitleById(schemeID, title);
        } catch (Exception e) {
            e.getMessage();
        }

        modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId=" + schemeID);
        return modelAndView;
    }
}

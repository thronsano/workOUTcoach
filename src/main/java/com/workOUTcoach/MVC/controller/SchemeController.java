package com.workOUTcoach.MVC.controller;

import com.workOUTcoach.MVC.model.CycleModel;
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

    @Autowired
    CycleModel cycleModel;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSettings(ModelAndView modelAndView,
                                    @RequestParam("id") int clientID) {
        modelAndView.addObject("usedSchemeList", schemeModel.getUsedSchemeListByClient(clientID));
        modelAndView.addObject("unusedSchemeList", schemeModel.getUnusedSchemeListByClient(clientID));
        modelAndView.addObject("schemes", schemeModel.getSchemeListByClientId(clientID));
        modelAndView.addObject("allSchemes", schemeModel.getSchemeListByClientId(clientID));



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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("exerciseDeleted", "failed");
            redirectAttributes.addFlashAttribute("reason", e.getMessage());
            redirectAttributes.addFlashAttribute("exercises", exerciseModel.getExerciseListBySchemeId(schemeID));
        }

        modelAndView.setViewName("redirect:/clientProfile/training/scheme?schemeId=" + schemeID);
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

    @RequestMapping(value = "/newScheme", method = RequestMethod.GET)
    public ModelAndView getNewExercise(ModelAndView modelAndView,
                                       @RequestParam(value = "id") int clientID) {
        modelAndView.addObject("cycle", cycleModel.getCycleByClientId(clientID));
        modelAndView.setViewName("newScheme");
        return modelAndView;
    }


    @RequestMapping(value = "/newScheme", method = RequestMethod.POST)
    public ModelAndView postNewExercise(ModelAndView modelAndView,
                                        @RequestParam(value = "clientID") int clientID,
                                        @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                        @RequestParam(value = "repetitions", required = false, defaultValue = "0") int repetitions,
                                        RedirectAttributes redirectAttributes) {
        try {
            schemeModel.addNewScheme(name, clientID);
            modelAndView.setViewName("redirect:/clientProfile/training?id=" + clientID);

        } catch (Exception e) {
            modelAndView.setViewName("redirect:/clientProfile/training/newScheme?id=" + clientID);
            redirectAttributes.addFlashAttribute("dataError", true);
        }
        redirectAttributes.addFlashAttribute("cycle", cycleModel.getCycleByClientId(clientID));
        return modelAndView;
    }

    @RequestMapping(value = "/editCycle", method = RequestMethod.POST)
    public ModelAndView addSchemeToCycle(@RequestParam("id") String id,
                                         @RequestParam(value = "schemeId", required = false, defaultValue = "-1") int schemeId,
                                         ModelAndView modelAndView,
                                         RedirectAttributes redirectAttributes) {
        try {
            schemeModel.addSchemeToCycle(Integer.parseInt(id), schemeId);
            redirectAttributes.addFlashAttribute("schemeAdded", "successful");
            redirectAttributes.addFlashAttribute("unusedSchemeList", schemeModel.getUnusedSchemeListByClient(Integer.parseInt(id)));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("schemeAdded", "failed");
            redirectAttributes.addFlashAttribute("reason", ex.getMessage());
            redirectAttributes.addFlashAttribute("unusedSchemeList", schemeModel.getUnusedSchemeListByClient(Integer.parseInt(id)));
        }
        modelAndView.setViewName("redirect:/clientProfile/training?id=" + id);
        return modelAndView;
    }

    @RequestMapping(value = "/removeFromCycle", method = RequestMethod.POST)
    public ModelAndView removeSchemeFromCycle(@RequestParam("id") String id,
                                              @RequestParam(value = "schemeId", required = false, defaultValue = "-1") int schemeId,
                                              ModelAndView modelAndView,
                                              RedirectAttributes redirectAttributes) {
        try {
            schemeModel.removeSchemeFromCycle(Integer.parseInt(id), schemeId);
            redirectAttributes.addFlashAttribute("schemeRemoved", "successful");
            redirectAttributes.addFlashAttribute("schemeList", schemeModel.getUsedSchemeListByClient(Integer.parseInt(id)));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("schemeRemoved", "failed");
            redirectAttributes.addFlashAttribute("reason", ex.getMessage());
            redirectAttributes.addFlashAttribute("schemeList", schemeModel.getUsedSchemeListByClient(Integer.parseInt(id)));
        }
        modelAndView.setViewName("redirect:/clientProfile/training?id=" + id);
        return modelAndView;
    }

    @RequestMapping(value = "/deleteScheme", method = RequestMethod.POST)
    public ModelAndView postDeleteScheme(@RequestParam("id") int clientId,
                                         @RequestParam(value = "schemeId", required = false, defaultValue = "-1") int schemeId,
                                         ModelAndView modelAndView,
                                         RedirectAttributes redirectAttributes) {
        try {
            schemeModel.deleteSchemeById(schemeId);
            redirectAttributes.addFlashAttribute("schemeDeleted", "successful");
            redirectAttributes.addFlashAttribute("allSchemes", schemeModel.getSchemeListByClientId(clientId));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("schemeDeleted", "failed");
            redirectAttributes.addFlashAttribute("reason", ex.getMessage());
            redirectAttributes.addFlashAttribute("allSchemes", schemeModel.getSchemeListByClientId(clientId));
        }
        modelAndView.setViewName("redirect:/clientProfile/training?id=" + clientId);
        return modelAndView;
    }

}

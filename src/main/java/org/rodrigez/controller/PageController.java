package org.rodrigez.controller;

import org.rodrigez.model.PageForm;
import org.rodrigez.model.Page;
import org.rodrigez.service.PageLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class PageController {
    @Autowired
    PageLoader pageLoader;

    @GetMapping("/pages")
    public String pageForm(Model model){
        Set<Page> pages =  pageLoader.getPageSet();
        model.addAttribute("form", new PageForm());
        model.addAttribute("statusBarPercent", pageLoader.getStatusBarPercent());
        model.addAttribute("pages", pages);
        return "/main.html";
    }

    @PostMapping("/pages")
    public String pageFormSubmit(@Valid @ModelAttribute("form") PageForm form, BindingResult bindingResult){
        if(!bindingResult.hasErrors()){
            pageLoader.run(form);
        }
        return "redirect:/pages";
    }

    @GetMapping("/pause")
    public ModelAndView pause(){
        pageLoader.pause();
        ModelAndView modelAndView = new ModelAndView("redirect:/pages");
        modelAndView.addObject("statusBarPercent", pageLoader.getStatusBarPercent());
        return modelAndView;
    }

    @GetMapping("/refresh")
    public ModelAndView refresh(){
        Set<Page> pages =  pageLoader.getPageSet();
        ModelAndView modelAndView = new ModelAndView("redirect:/pages");
        modelAndView.addObject("statusBarPercent", pageLoader.getStatusBarPercent());
        modelAndView.addObject("pages", pages);
        return modelAndView;
    }

    @GetMapping("/stop")
    public ModelAndView stop(){
        pageLoader.stop();
        ModelAndView modelAndView = new ModelAndView("redirect:/pages");
        modelAndView.addObject("statusBarPercent", pageLoader.getStatusBarPercent());
        return modelAndView;
    }

    @GetMapping("/resume")
    public ModelAndView resume(){
        pageLoader.resume();
        ModelAndView modelAndView = new ModelAndView("redirect:/pages");
        modelAndView.addObject("statusBarPercent", pageLoader.getStatusBarPercent());
        return modelAndView;
    }
}
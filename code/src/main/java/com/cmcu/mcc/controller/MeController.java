package com.cmcu.mcc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/me")
public class MeController {

    @RequestMapping("/edProject")
    public ModelAndView edProject(){
        ModelAndView modelAndView=new ModelAndView("/me/edProject");
        return modelAndView;
    }

    @RequestMapping("/cpStep")
    public ModelAndView cpStep(){
        ModelAndView modelAndView=new ModelAndView("me/cpStep");
        return modelAndView;
    }

    @RequestMapping("/stampAll")
    public ModelAndView stampAll(){
        ModelAndView modelAndView=new ModelAndView("me/stampAll");
        return modelAndView;
    }
    @RequestMapping("/stampAllDetail")
    public ModelAndView stampAllDetail(){
        ModelAndView modelAndView=new ModelAndView("me/stampAllDetail");
        return modelAndView;
    }
    @RequestMapping("/stampNoDetail")
    public ModelAndView stampNoDetail(){
        ModelAndView modelAndView=new ModelAndView("me/stampNoDetail");
        return modelAndView;
    }
    @RequestMapping("/all")
    public ModelAndView allProject(){
        ModelAndView modelAndView=new ModelAndView("me/all");
        return modelAndView;
    }


}

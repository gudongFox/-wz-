package com.cmcu.mcc.sys.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys")
public class SysController {

    @RequestMapping("/plugin")
    public ModelAndView plugin(){
        ModelAndView modelAndView=new ModelAndView("/sys/plugin");
        return modelAndView;
    }

    @RequestMapping("/code")
    public ModelAndView sysCode(){
        ModelAndView modelAndView=new ModelAndView("/sys/sysCode");
        return modelAndView;
    }
    @RequestMapping("/dwgPicture")
    public ModelAndView dwgPicture(){
        ModelAndView modelAndView=new ModelAndView("/sys/dwgPicture");
        return modelAndView;
    }

    @RequestMapping("/attach")
    public ModelAndView attach(){
        ModelAndView modelAndView=new ModelAndView("/sys/attach");
        return modelAndView;
    }

    @RequestMapping("/config")
    public ModelAndView config(){
        ModelAndView modelAndView=new ModelAndView("/sys/config");
        return modelAndView;
    }
    @RequestMapping("/software")
    public ModelAndView software(){
        ModelAndView modelAndView=new ModelAndView("/sys/software");
        return modelAndView;
    }
    @RequestMapping("/recoverFile")
    public ModelAndView recoverFile(){
        ModelAndView modelAndView=new ModelAndView("/sys/recoverFile");
        return modelAndView;
    }
    @RequestMapping("/recoverCpFile")
    public ModelAndView recoverCpFile(){
        ModelAndView modelAndView=new ModelAndView("/sys/recoverCpFile");
        return modelAndView;
    }


    @RequestMapping("/me")
    public ModelAndView me(){
        ModelAndView modelAndView=new ModelAndView("/sys/me");
        return modelAndView;
    }
    @RequestMapping("/schedule")
    public ModelAndView schedule(){
        ModelAndView modelAndView=new ModelAndView("/sys/schedule");
        return modelAndView;
    }
}

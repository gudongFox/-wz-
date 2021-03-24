package com.cmcu.mcc.supervise.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/five/supervise")
public class OaSuperviseController {


    @RequestMapping("/supervise")
    public ModelAndView supervise(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/supervise");
        return modelAndView;
    }

    @RequestMapping("/superviseDetail")
    public ModelAndView superviseDetail(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseDetail");
        return modelAndView;
    }

    @RequestMapping("/superviseDetailChild")
    public ModelAndView superviseDetailChild(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseDetailChild");
        return modelAndView;
    }
    //文件督办
    @RequestMapping("/superviseFile")
    public ModelAndView superviseFile(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseFile");
        return modelAndView;
    }

    @RequestMapping("/superviseFileDetail")
    public ModelAndView superviseFileDetail(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseFileDetail");
        return modelAndView;
    }
   //年度重点任务督办
    @RequestMapping("/superviseYear")
    public ModelAndView superviseYear(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseYear");
        return modelAndView;
    }

    @RequestMapping("/superviseYearDetail")
    public ModelAndView superviseYearDetail(){
        ModelAndView modelAndView=new ModelAndView("five/supervise/superviseYearDetail");
        return modelAndView;
    }

}

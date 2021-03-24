package com.cmcu.mcc.five.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/act")
public class FiveActController {
    @RequestMapping("/plotIndex")
    public ModelAndView plotIndex(){
        return new ModelAndView("/plotIndex");
    }

    @RequestMapping("/oaDispatch")
    public ModelAndView oaDispatch(){
        return new ModelAndView("five/print/print-oaDispatchDetail");
    }

}

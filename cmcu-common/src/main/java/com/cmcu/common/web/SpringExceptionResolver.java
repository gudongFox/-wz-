package com.cmcu.common.web;

import com.common.model.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {
        String url = httpServletRequest.getRequestURL().toString();
        if(!(ex instanceof IllegalStateException)){
            ex.printStackTrace();
        }
        ModelAndView mv;
        String defaultMsg = "System error";
        if (url.endsWith(".json")||url.endsWith(".do")||url.contains("/common/attach/download")) {
            JsonData.fail(ex.getMessage()).toMap();
            mv = new ModelAndView("jsonView", JsonData.fail(ex.getMessage()).toMap());
        } else {
            log.error("unknown exception, url:" + url, ex);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("500", result.toMap());
        }
        return mv;
    }

}

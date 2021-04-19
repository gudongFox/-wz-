package com.cmcu.mcc.controller;

import com.common.model.JsonData;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.CookieSessionUtils;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.JwtUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.service.EdFormService;
import com.cmcu.mcc.ed.service.EdProjectStepService;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/cad")
public class CadController {

    @Resource
    EdProjectStepService edProjectStepService;

    @Resource
    EdProjectTreeService edProjectTreeService;

    @Resource
    EdFormService edFormService;

    @Resource
    CommonUserService commonUserService;
    @Resource
    HandleFormService handleFormService;

    @RequestMapping("/redirect")
    public ModelAndView redirect(String jwt) {
        String result = JwtUtil.decodeJwtToken(jwt);
        if (StringUtils.isNotEmpty(result)) {
            Map data = com.common.util.JsonMapper.string2Map(result);
            if (data.containsKey(MccConst.EN_LOGIN)) {
                CookieSessionUtils.addSession(MccConst.EN_LOGIN, data.get(MccConst.EN_LOGIN));
                return new ModelAndView("redirect:/index");
            }
        }
        return new ModelAndView("redirect:/login");
    }



    @RequestMapping("/home")
    public ModelAndView home(String enLogin) {
        if (StringUtils.isEmpty(CookieSessionUtils.getSession(MccConst.EN_LOGIN))) {
            if (StringUtils.isNotEmpty(enLogin)) {
                CookieSessionUtils.addSession(MccConst.EN_LOGIN, enLogin);
            }
        }
        if (StringUtils.isEmpty(CookieSessionUtils.getSession(MccConst.EN_LOGIN))) {
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/index");
    }



    @RequestMapping("/ed")
    public ModelAndView ed(String enLogin,int projectId,Integer stepId,Integer nodeId) {
        if (StringUtils.isEmpty(enLogin)) {
            return new ModelAndView("login");
        } else {
            CookieSessionUtils.addSession(MccConst.EN_LOGIN, enLogin);
            edProjectTreeService.setSelectedNode(projectId, stepId, nodeId, enLogin);
            String url ="/index#/five/detail?id="+projectId;
            return new ModelAndView("redirect:" + url);
        }
    }


    @RequestMapping("/dir")
    public ModelAndView dir() {
        ModelAndView modelAndView = new ModelAndView("cad/dir");
        return modelAndView;
    }

    @RequestMapping("/file")
    public ModelAndView file() {
        ModelAndView modelAndView = new ModelAndView("cad/file");
        return modelAndView;
    }

    @RequestMapping("/task")
    public ModelAndView task() {
        ModelAndView modelAndView = new ModelAndView("cad/task");
        return modelAndView;
    }



    @ResponseBody
    @RequestMapping("/listProjectByEnLogin.json")
    public JsonData listProjectByEnLogin(String enLogin,String q) {
        return JsonData.success(edProjectStepService.listCadProjectByEnLogin(enLogin,q));
    }


    @ResponseBody
    @PostMapping("/listCadEdTree.json")
    public JsonData listCadEdTree(int stepId,String enLogin) {
        return JsonData.success(edProjectTreeService.listCadEdTree(stepId,enLogin));
    }


    @ResponseBody
    @PostMapping("/listFormData.json")
    public JsonData listFormData(int nodeId,String enLogin,String excludeInfo) {
        return JsonData.success(edFormService.listDataByNodeId(nodeId,enLogin,excludeInfo));
    }


    /**
     * todo 实现这个方法
     * @param nodeId
     * @param enLogin
     * @return
     */
    @ResponseBody
    @RequestMapping("/getNewEdFormData.json")
    public JsonData getNewEdFormData(int nodeId,String enLogin) {
        return JsonData.success(handleFormService.getNewEdFormData(nodeId, enLogin));
    }

}
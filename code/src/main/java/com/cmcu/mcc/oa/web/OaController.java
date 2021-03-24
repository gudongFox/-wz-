package com.cmcu.mcc.oa.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/oa")
public class OaController {
    //制度 更多
    @RequestMapping("/articleRegime")
    public ModelAndView articleRegime(){
        ModelAndView modelAndView=new ModelAndView("/oa/articleRegime");
        return modelAndView;
    }
    //部门公告 更多
    @RequestMapping("/articleDept")
    public ModelAndView articleDept(){
        ModelAndView modelAndView=new ModelAndView("/oa/articleDept");
        return modelAndView;
    }
    //企业动态 更多
    @RequestMapping("/article")
    public ModelAndView article(){
        ModelAndView modelAndView=new ModelAndView("/oa/article");
        return modelAndView;
    }
    //详情
    @RequestMapping("/articleDetail")
    public ModelAndView articleDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/articleDetail");
        return modelAndView;
    }
    //详情
    @RequestMapping("/mainWordOWA")
    public ModelAndView mainWordOWA(){
        ModelAndView modelAndView=new ModelAndView("/oa/mainWordOWA");
        return modelAndView;
    }
    @RequestMapping("/articleDetailOWA")
    public ModelAndView articleDetailOWA(){
        ModelAndView modelAndView=new ModelAndView("/oa/articleDetailOWA");
        return modelAndView;
    }
    @RequestMapping("/notice")
    public ModelAndView notice(){
        ModelAndView modelAndView=new ModelAndView("/oa/notice");
        return modelAndView;
    }
    @RequestMapping("/noticeDetail")
    public ModelAndView noticeDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/noticeDetail");
        return modelAndView;
    }
    //信息发布
    @RequestMapping("/noticeApply")
    public ModelAndView noticeApply(){
        ModelAndView modelAndView=new ModelAndView("/oa/noticeApply");
        return modelAndView;
    }
    @RequestMapping("/noticeApplyDetail")
    public ModelAndView noticeApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/noticeApplyDetail");
        return modelAndView;
    }
    @RequestMapping("/meetingRoomDetail")
    public ModelAndView meetingRoomDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/meetingRoomDetail");
        return modelAndView;
    }
    @RequestMapping("/meetingRoomApply")
    public ModelAndView meetingRoomApply(){
        ModelAndView modelAndView=new ModelAndView("/oa/meetingRoomApply");
        return modelAndView;
    }

    @RequestMapping("/meetingRoomApplyDetail")
    public ModelAndView meetingRoomApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/meetingRoomApplyDetail");
        return modelAndView;
    }

    @RequestMapping("/car")
    public ModelAndView car(){
        ModelAndView modelAndView=new ModelAndView("/oa/car");
        return modelAndView;
    }

    @RequestMapping("/carDetail")
    public ModelAndView carDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/carDetail");
        return modelAndView;
    }

    @RequestMapping("/carApply")
    public ModelAndView carApply(){
        ModelAndView modelAndView=new ModelAndView("/oa/carApply");
        return modelAndView;
    }

    @RequestMapping("/carApplyDetail")
    public ModelAndView carApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/carApplyDetail");
        return modelAndView;
    }






    @RequestMapping("/archive")
    public ModelAndView archive(){
        ModelAndView modelAndView=new ModelAndView("/oa/archive");
        return modelAndView;
    }

    @RequestMapping("/archiveDetail")
    public ModelAndView archiveDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/archiveDetail");
        return modelAndView;
    }

    @RequestMapping("/archiveApply")
    public ModelAndView archiveApply(){
        ModelAndView modelAndView=new ModelAndView("/oa/archiveApply");
        return modelAndView;
    }

    @RequestMapping("/archiveApplyDetail")
    public ModelAndView archiveApplyDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/archiveApplyDetail");
        return modelAndView;
    }






    @RequestMapping("/trainOut")
    public ModelAndView trainOut(){
        ModelAndView modelAndView=new ModelAndView("/oa/trainOut");
        return modelAndView;
    }


    @RequestMapping("/trainOutDetail")
    public ModelAndView trainOutDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/trainOutDetail");
        return modelAndView;
    }





    @RequestMapping("/software")
    public ModelAndView software(){
        ModelAndView modelAndView=new ModelAndView("/oa/software");
        return modelAndView;
    }

    @RequestMapping("/softwareDetail")
    public ModelAndView softwareDetail(){
        ModelAndView modelAndView=new ModelAndView("/oa/softwareDetail");
        return modelAndView;
    }


    /*功能入口界面重写 蒋*/
    @RequestMapping("/functionEntrance")
    public ModelAndView functionEntrance(){
        ModelAndView modelAndView=new ModelAndView("/functionEntrance");
        return modelAndView;
    }
    /*友情链接 蒋*/
    @RequestMapping("/link")
    public ModelAndView link(){
        ModelAndView modelAndView=new ModelAndView("/oa/link");
        return modelAndView;
    }
}

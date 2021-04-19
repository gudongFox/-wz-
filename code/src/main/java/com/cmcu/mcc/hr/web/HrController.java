package com.cmcu.mcc.hr.web;

import com.common.model.JsonData;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDetailService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.TempFormService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/hr")
public class HrController {

    @Resource
    SelectEmployeeService selectEmployeeService;

    @Resource
    HrDetailService hrDetailService;

    @RequestMapping("/employee")
    public ModelAndView employee(){
        return new ModelAndView("hr/employee");
    }


    @RequestMapping("/employee-detail")
    public ModelAndView partialContractDetail(){
        return new ModelAndView("hr/employeeDetail");
    }

    @RequestMapping("/incomeProof")
    public ModelAndView incomeProof(){
        return new ModelAndView("hr/partial-incomeProof");
    }
    @RequestMapping("/incomeProofDetail")
    public ModelAndView incomeProofDetail(){
        return new ModelAndView("hr/partial-incomeProofDetail");
    }


    @RequestMapping("/leave")
    public ModelAndView leave(){
        return new ModelAndView("hr/partial-leave");
    }
    @RequestMapping("/leaveDetail")
    public ModelAndView leaveDetail(){
        return new ModelAndView("hr/partial-leaveDetail");
    }

    @RequestMapping("/vacation")
    public ModelAndView vacation(){
        return new ModelAndView("hr/partial-vacation");
    }
    @RequestMapping("/vacationDetail")
    public ModelAndView vacationDetail(){
        return new ModelAndView("hr/partial-vacationDetail");
    }

    @RequestMapping("/education")
    public ModelAndView education(){
        return new ModelAndView("hr/partial-education");
    }
    @RequestMapping("/educationDetail")
    public ModelAndView educationDetail(){
        return new ModelAndView("hr/partial-educationDetail");
    }

    @RequestMapping("/contract")
    public ModelAndView contract(){
        return new ModelAndView("hr/partial-contract");
    }
    @RequestMapping("/contractDetail")
    public ModelAndView contractDetail(){
        return new ModelAndView("hr/partial-contractDetail");
    }
    @RequestMapping("/basic")
    public ModelAndView basic(){
        return new ModelAndView("hr/partial-basic");
    }
    @RequestMapping("/basicAdmin")
    public ModelAndView basicAdmin(){
        return new ModelAndView("hr/partial-basicAdmin");
    }

    @RequestMapping("/qualify")
    public ModelAndView qualify(){
        return new ModelAndView("hr/partial-qualify");
    }
    @RequestMapping("/myQualify")
    public ModelAndView myQualify(){
        return new ModelAndView("hr/partial-myQualify");
    }
    @RequestMapping("/qualifyDetail")
    public ModelAndView qualifyDetail(){
        return new ModelAndView("hr/partial-qualifyDetail");
    }

    @RequestMapping("/certification")
    public ModelAndView certification(){
        return new ModelAndView("hr/partial-certification");
    }
    @RequestMapping("/certificationDetail")
    public ModelAndView certificationDetail(){
        return new ModelAndView("hr/partial-certificationDetail");
    }

    @RequestMapping("/invent")
    public ModelAndView invent(){
        return new ModelAndView("hr/partial-invent");
    }
    @RequestMapping("/inventDetail")
    public ModelAndView inventDetail(){
        return new ModelAndView("hr/partial-inventDetail");
    }

    @RequestMapping("/position")
    public ModelAndView position(){
        return new ModelAndView("hr/position");
    }

    @RequestMapping("/dept")
    public ModelAndView dept(HttpServletRequest req){
        Object attribute = req.getSession().getAttribute(MccConst.EN_LOGIN);
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin((String) attribute);
        if (hrEmployeeDto.getRoleNames().contains("系统管理员")){
            return new ModelAndView("hr/deptSysAdmin");
        }else if (hrEmployeeDto.getRoleNames().contains("系统安全员")){
            return new ModelAndView("hr/deptSafety");
        }else {
            return new ModelAndView("hr/dept");
        }
    }

    @RequestMapping("/deptUser")
    public ModelAndView deptUser(){
        return new ModelAndView("hr/deptUser");
    }


    @RequestMapping("/register")
    public ModelAndView register(){
        return new ModelAndView("hr/partial-register");
    }
    @RequestMapping("/registerDetail")
    public ModelAndView registerDetail(){
        return new ModelAndView("hr/partial-registerDetail");
    }

    @RequestMapping("/employeeProof")
    public ModelAndView employeeProof(){
        return new ModelAndView("hr/partial-employeeProof");
    }
    @RequestMapping("/employeeProofDetail")
    public ModelAndView employeeProofDetail(){
        return new ModelAndView("hr/partial-employeeProofDetail");
    }

    @RequestMapping("/insure")
    public ModelAndView insure(){
        return new ModelAndView("hr/partial-insure");
    }
    @RequestMapping("/insureDetail")
    public ModelAndView insureDetail(){
        return new ModelAndView("hr/partial-insureDetail");
    }

    @RequestMapping("/apply/designQualifyApply")
    public ModelAndView designApply(){
        return new ModelAndView("five/hr/qualifyApply");
    }
    @RequestMapping("/apply/designQualifyApplyDetail")
    public ModelAndView designApplyDetail(){
        return new ModelAndView("five/hr/qualifyApplyDetail");
    }

    @RequestMapping("/apply/approveQualifyApply")
    public ModelAndView approveApply(){
        return new ModelAndView("five/hr/approveApply");
    }
    @RequestMapping("/apply/approveQualifyApplyDetail")
    public ModelAndView approveApplyDetail(){
        return new ModelAndView("five/hr/approveApplyDetail");
    }

    @RequestMapping("/apply/chiefQualifyApply")
    public ModelAndView qualifyApplyChief(){
        return new ModelAndView("five/hr/chiefApply");
    }
    @RequestMapping("/apply/chiefQualifyApplyDetail")
    public ModelAndView qualifyApplyChiefDetail(){
        return new ModelAndView("five/hr/chiefApplyDetail");
    }


    @RequestMapping("/apply/externalQualifyApply")
    public ModelAndView externalQualifyApply(){
        return new ModelAndView("five/hr/externalApply");
    }
    @RequestMapping("/apply/externalQualifyApplyDetail")
    public ModelAndView externalQualifyApplyDetail(){
        return new ModelAndView("five/hr/externalApplyDetail");
    }

    @RequestMapping("/honor")
    public ModelAndView honor(){
        return new ModelAndView("hr/partial-honor");
    }
    @RequestMapping("/honorDetail")
    public ModelAndView honorDetail(){
        return new ModelAndView("hr/partial-honorDetail");
    }


    @RequestMapping("/detail")
    public ModelAndView detail(){
        return new ModelAndView("/hr/detail");
    }

    @Resource
    TempFormService tempFormService;

    @RequestMapping("/detail/form")
    public ModelAndView detailForm(){
        return new ModelAndView("/hr/detail/form");
    }

    @RequestMapping("/detail/formDetail")
    public ModelAndView detailFormDetail(){
        return new ModelAndView("/hr/detail/formDetail");
    }

    @RequestMapping("/detail/basicDetail")
    public ModelAndView basicDetail(){
        return new ModelAndView("/hr/detail/basicDetail");
    }



    @RequestMapping("/form")
    public ModelAndView form() {
        return new ModelAndView("/hr/form");
    }

    @RequestMapping("/formDetail")
    public ModelAndView formDetail() {
        return new ModelAndView("/hr/formDetail");
    }





    @ResponseBody
    @RequestMapping("/uploadExcel.json")
    public JsonData uploadExcel(MultipartFile multipartFile, String enLogin) throws IOException {
//        String path="D:\\workspace\\wuzhou\\文档\\人力资源\\专利.xls";
//        Map data= MyPoiUtil.listSheetData(new FileInputStream( new File(path)),0,true);
//        hrDetailService.uploadExcelData(data, "luodong");
        Map data = MyPoiUtil.listSheetData(multipartFile.getInputStream(), 0, true);
        hrDetailService.uploadExcelData(data, enLogin);
        return JsonData.success();
    }



    @RequestMapping("/train")
    public ModelAndView train(){
        return new ModelAndView("hr/train");
    }

    @RequestMapping("/trainDetail")
    public ModelAndView trainDetail(){
        return new ModelAndView("hr/trainDetail");
    }



}



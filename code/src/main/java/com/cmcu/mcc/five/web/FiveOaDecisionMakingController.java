package com.cmcu.mcc.five.web;

import com.common.model.JsonData;
import com.cmcu.common.util.WebUtil;
import com.cmcu.mcc.comm.exportDoc.ExportUtils;
import com.cmcu.mcc.five.dto.FiveOaDecisionMakingDto;
import com.cmcu.mcc.five.dto.FiveOaDecisionMarkingDetailDto;
import com.cmcu.mcc.five.entity.FiveOaDecisionMarkingDetail;

import com.cmcu.mcc.five.service.FiveOaDecisionMakingDetailService;
import com.cmcu.mcc.five.service.FiveOaDecisionMakingService;
import com.cmcu.mcc.five.service.FiveOaSignQuoteService;
import com.deepoove.poi.XWPFTemplate;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/five/oa/decisionMaking")
public class FiveOaDecisionMakingController {
    @Autowired
    FiveOaDecisionMakingService fiveOaDecisionMakingService;
    @Autowired
    FiveOaDecisionMakingDetailService fiveOaDecisionMakingDetailService;
    @Autowired
    FiveOaSignQuoteService fiveOaSignQuoteService;
    @PostMapping(value = "/getModelById.json")
    public JsonData getModelById(int id){
        return JsonData.success(fiveOaDecisionMakingService.getModelById(id));
    }

    @PostMapping("/getNewModel.json")
    public JsonData getNewModel(String userLogin){
        return JsonData.success(fiveOaDecisionMakingService.getNewModel(userLogin));
    }
    //新增议题
    @PostMapping("/createDecisionBySignBusinessKey.json")
    public JsonData createDecisionBySignBusinessKey(String businessId){
        fiveOaDecisionMakingService.createDecisionBySignBusinessKey(businessId);
        return JsonData.success();
    }
    @PostMapping("/remove.json")
    public JsonData remove(int id,String userLogin){
        fiveOaDecisionMakingService.remove(id,userLogin);
        return JsonData.success();
    }
    @PostMapping("/update.json")
    public JsonData update(@RequestBody FiveOaDecisionMakingDto item){
        Assert.state(StringUtils.isNotEmpty(item.getOperateUserLogin()),"操作人不能为空!");
        fiveOaDecisionMakingService.update(item);
        return JsonData.success();
    }

    @PostMapping("/getModelByBusinessId.json")
    public JsonData getModelByBusinessId(String businessId){
        return JsonData.success(fiveOaDecisionMakingService.getModelByBusinessId(businessId));
    }

    @PostMapping("/listPagedData.json")
    public JsonData listPagedData(String userLogin,String uiSref, int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaDecisionMakingService.listPagedData(params, userLogin, uiSref, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }

    //region 决策会议子项部分代码
    @PostMapping("/listDetailPagedData.json")
    public JsonData listDetailPagedData(int pageNum, int pageSize) {
        Map params = WebUtil.getRequestParameters();
        PageInfo pageInfo = fiveOaDecisionMakingDetailService.listPageDate(params, pageNum, pageSize);
        return JsonData.success(pageInfo);
    }
    @PostMapping("/listDetails.json")
    public JsonData listDetails(String businessKey,String userLogin){
        int id=FiveOaDecisionMakingService.getIdFromBusinessKey(businessKey);
        FiveOaDecisionMakingDto fiveOaDecisionMakingDto=fiveOaDecisionMakingService.getModelById(id);//获取到决策会议
        //如果是 出席领导、列席领导、创建人则具有所有权限 否则只有议题权限
        if(fiveOaDecisionMakingDto.getCompanyLeader().contains(userLogin)||fiveOaDecisionMakingDto.getAttender().contains(userLogin)||StringUtils.equalsIgnoreCase(userLogin,fiveOaDecisionMakingDto.getCreator()))
            return JsonData.success(fiveOaDecisionMakingDetailService.getModelByMainBusiness(businessKey));
        else if (("2863,luodong").contains(userLogin)){//管理员权限查看所有议题
            return JsonData.success(fiveOaDecisionMakingDetailService.getModelByMainBusiness(businessKey));
        }else {
            return JsonData.success(fiveOaDecisionMakingDetailService.getModelByMainBusiness(businessKey,userLogin));
        }

    }

    @PostMapping("/updateDetail.json")
    public JsonData updateDetail(@RequestBody FiveOaDecisionMarkingDetail item){

        if(item.getId()==null)
        {
            FiveOaDecisionMarkingDetail defualtItem=setDefualtValue(item);
            fiveOaDecisionMakingDetailService.add(defualtItem);
        }
        else
        {
            FiveOaDecisionMarkingDetail detail=fiveOaDecisionMakingDetailService.getModelById(item.getId());
            if(detail!=null)
                fiveOaDecisionMakingService.updateDecisionDetail(item);
            else
            {
                FiveOaDecisionMarkingDetail defualtItem=setDefualtValue(item);
                fiveOaDecisionMakingDetailService.add(defualtItem);
            }
        }
        return JsonData.success();
    }

    private FiveOaDecisionMarkingDetail setDefualtValue(FiveOaDecisionMarkingDetail item){
        int seq=0;
        List<FiveOaDecisionMarkingDetailDto> list=fiveOaDecisionMakingDetailService.getModelByMainBusiness(item.getMainBusiness());
        for (FiveOaDecisionMarkingDetailDto detail:list) {
            int temporySeq=detail.getSeq();
            if(seq<temporySeq)
                seq=temporySeq;
        }
        item.setSeq(seq+1);
        return item;
    }

    @PostMapping("/removeDetail.json")
    public JsonData removeDetail(int id,boolean flag){
        FiveOaDecisionMarkingDetail item=fiveOaDecisionMakingDetailService.getModelById(id);
        if(item!=null && flag == false) {
            item.setMainBusiness("");
            //item.setDeleted(true);
            item.setIssueStatus("待办中");
            item.setArrangeMan("");
            item.setArrangeManName("");
            fiveOaDecisionMakingService.updateDecisionDetail(item);
        }else if (item!=null && flag == true){
            item.setMainBusiness("");
            if (!StringUtils.isNotEmpty(item.getLinkedBusiness())){
                item.setDeleted(true);
            }
            item.setIssueStatus("待办中");
            item.setArrangeMan("");
            item.setArrangeManName("");
            fiveOaDecisionMakingService.updateDecisionDetail(item);
        }
        return JsonData.success();
    }

    @PostMapping("/getNewModelDetail.json")
    public JsonData getNewModelDetail(String mainBusiness,String userLogin){
        if ("".equals(mainBusiness)){
            return JsonData.success(fiveOaDecisionMakingDetailService.getNewModelDetailByType(userLogin));
        }else {
            return JsonData.success(fiveOaDecisionMakingDetailService.getNewModelDetail(mainBusiness,userLogin));
        }
    }

    //endregion
    //region 流程关联部分代码
    //获取签报流程
    @PostMapping("/listNotCompletedDetail.json")
    public JsonData listNotCompletedDetail(String userLogin,int pageNum, int pageSize,String keyWord) {
        Map params = WebUtil.getRequestParameters();
        params.put("keyWord",keyWord);
        params.put("creator", userLogin);
        params.put("deleted",false);
        params.put("issueStatus", "待办中");
        PageInfo pageInfo = fiveOaDecisionMakingDetailService.getNotCompleted(params,pageNum,pageSize);
        return JsonData.success(pageInfo);
    }

    //endregion

    @PostMapping("/getPrintData.json")
    public JsonData getPrintData(int id){
       return JsonData.success(fiveOaDecisionMakingService.getPrintData(id));
    }

    /**
     * 签报 查询议题列
     * @param linkedBusiness
     * @return
     */
    @PostMapping("/getModelByLinkBusiness.json")
    public JsonData getModelByLinkBusiness(String linkedBusiness){
        return JsonData.success(fiveOaDecisionMakingDetailService.getModelByLinkBusiness(linkedBusiness));
    }

    @RequestMapping("/getXWPFTemplateNotice.json")
    public void getXWPFTemplateNotice(HttpServletResponse response, int id) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        FiveOaDecisionMakingDto decisionMakingDto = fiveOaDecisionMakingService.getModelById(id);
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath =webappRoot+"assets/doc/oa/会议通知-模板.docx";

        XWPFTemplate xwpfTemplate = fiveOaDecisionMakingService.getXWPFTemplate(filePath,id);

        ExportUtils.sendResponse(response, decisionMakingDto.getItem()+"_"+decisionMakingDto.getStages()+".docx", xwpfTemplate);

    }
    @RequestMapping("/getXWPFTemplateSummary.json")
    public void getXWPFTemplateSummary(HttpServletResponse response, int id) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        FiveOaDecisionMakingDto decisionMakingDto = fiveOaDecisionMakingService.getModelById(id);
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath="";
        if (decisionMakingDto.getMeetingType().equals("董事")){
            filePath =webappRoot+"assets/doc/oa/董事会.docx";
        }else {
             filePath =webappRoot+"assets/doc/oa/董事会.docx";
        }


        XWPFTemplate xwpfTemplate = fiveOaDecisionMakingService.getXWPFTemplate(filePath,id);

        ExportUtils.sendResponse(response, decisionMakingDto.getItem()+"_"+decisionMakingDto.getStages()+".docx", xwpfTemplate);

    }
}

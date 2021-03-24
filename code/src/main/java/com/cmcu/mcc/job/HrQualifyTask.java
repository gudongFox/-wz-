package com.cmcu.mcc.job;

import com.cmcu.common.dao.CommonMessageMapper;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveHrQualifyApplyDetailMapper;
import com.cmcu.mcc.five.dao.FiveHrQualifyApplyMapper;
import com.cmcu.mcc.five.dao.FiveHrQualifyChiefMapper;
import com.cmcu.mcc.five.entity.FiveHrQualifyApply;
import com.cmcu.mcc.five.entity.FiveHrQualifyApplyDetail;
import com.cmcu.mcc.five.entity.FiveHrQualifyChief;
import com.cmcu.mcc.hr.dao.HrQualifyMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrQualify;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HrQualifyTask {


    @Resource
    FiveHrQualifyApplyMapper fiveHrQualifyApplyMapper;

    @Resource
    FiveHrQualifyApplyDetailMapper fiveHrQualifyApplyDetailMapper;

    @Resource
    FiveHrQualifyChiefMapper fiveHrQualifyChiefMapper;

    @Resource
    HrQualifyMapper hrQualifyMapper;

    @Resource
    HrEmployeeService hrEmployeeService;
    @Autowired
    CommonCodeService commonCodeService;

    @Resource
    CommonMessageMapper commonMessageMapper;


    @Scheduled(cron = "* 0/120 * * * ? ")
    public void SyncQualifyApplyData() {

        try {
            Map params= Maps.newHashMap();
            params.put("deleted",false);
            params.put("handled",false);
            params.put("processEnd",true);
            List<FiveHrQualifyApply> fiveHrQualifyApplyList=fiveHrQualifyApplyMapper.selectAll(params);
            for(FiveHrQualifyApply fiveHrQualifyApply:fiveHrQualifyApplyList){
                Map detailParams=Maps.newHashMap();
                detailParams.put("qualifyApplyId",fiveHrQualifyApply.getId());
                detailParams.put("deleted",false);
                List<FiveHrQualifyApplyDetail> details=fiveHrQualifyApplyDetailMapper.selectAll(detailParams);
                for(FiveHrQualifyApplyDetail detail:details){
                    HrQualify model=getHrQualify(Integer.parseInt(fiveHrQualifyApply.getCheckYear()),detail.getUserLogin());
                    if(fiveHrQualifyApply.getApplyType().contains("审定")){
                        model.setApprove(detail.getApprove());
                    }else{
                        model.setMajorName(detail.getMajorName());
                        model.setProjectType(detail.getProjectType());
                        model.setDesign(detail.getDesign());
                        model.setProofread(detail.getProofread());
                        model.setAudit(detail.getAudit());
                        model.setProChief(detail.getProChief());
                    }
                    model.setGmtModified(new Date());
                    hrQualifyMapper.updateByPrimaryKey(model);
                }
                fiveHrQualifyApply.setHandled(true);
                fiveHrQualifyApply.setGmtModified(new Date());
                fiveHrQualifyApplyMapper.updateByPrimaryKey(fiveHrQualifyApply);
            }
        } catch (Exception ex) {


        }


        try{
            Map params= Maps.newHashMap();
            params.put("deleted",false);
            params.put("handled",false);
            params.put("processEnd",true);
            params.put("type","总设计师");
            List<FiveHrQualifyChief> fiveHrQualifyApplyList=fiveHrQualifyChiefMapper.selectAll(params);
            for(FiveHrQualifyChief fiveHrQualifyChief:fiveHrQualifyApplyList){
                HrQualify model=getHrQualify(Integer.parseInt(fiveHrQualifyChief.getCheckYear()),fiveHrQualifyChief.getChiefUserLogin());
                if(!model.getChiefDesigner()) {
                    model.setChiefDesigner(true);
                    model.setGmtModified(new Date());
                    hrQualifyMapper.updateByPrimaryKey(model);
                }
                fiveHrQualifyChief.setHandled(true);
                fiveHrQualifyChief.setGmtModified(new Date());
                fiveHrQualifyChiefMapper.updateByPrimaryKey(fiveHrQualifyChief);
            }

        }catch (Exception ex){



        }


    }


    public HrQualify getHrQualify(int checkYear,String userLogin){
        Map params=Maps.newHashMap();
        params.put("userLogin",userLogin);
        params.put("deleted",false);
        params.put("checkYear",checkYear);
        if(PageHelper.count(()->hrQualifyMapper.selectAll(params))>0){
            return hrQualifyMapper.selectAll(params).get(0);
        }
        List<String> majorNames=commonCodeService.listDataByCatalog(MccConst.APP_CODE,"设计专业").stream().map(CommonCode::getName).collect(Collectors.toList());
        HrEmployeeDto hrEmployeeDto=hrEmployeeService.getModelByUserLogin(userLogin);
        HrQualify model=new HrQualify();
        model.setUserLogin(hrEmployeeDto.getUserLogin());
        model.setUserName(hrEmployeeDto.getUserName());
        model.setDeptId(hrEmployeeDto.getDeptId());
        model.setDeptName(hrEmployeeDto.getDeptName());
        if(majorNames.contains(hrEmployeeDto.getSpecialty())){
            model.setMajorName(hrEmployeeDto.getSpecialty());
        }else {
            model.setMajorName("全部");
        }
        model.setCheckYear(checkYear);
        model.setProjectCharge(true);
        model.setProjectExeCharge(true);
        model.setMajorCharge(true);
        model.setDesign(true);
        model.setProofread(true);
        model.setAudit(true);
        model.setApprove(false);
        model.setCompanyApprove(false);
        model.setProjectType("全部");
        model.setDeleted(false);
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setProChief(false);
        model.setChiefDesigner(false);
        model.setRemark("");
        model.setBusinessKey("");
        ModelUtil.setNotNullFields(model);
        hrQualifyMapper.insert(model);
        model.setBusinessKey("hrQualify_"+model.getId());
        hrQualifyMapper.updateByPrimaryKey(model);
        return model;
    }







}

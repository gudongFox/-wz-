package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.*;
import com.cmcu.mcc.act.service.MyActService;

import com.cmcu.mcc.comm.EdConst;


import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaDispatchMapper;
import com.cmcu.mcc.five.dto.FiveOaDispatchDto;
import com.cmcu.mcc.five.entity.FiveOaDispatch;

import com.cmcu.mcc.five.entity.FiveOaExternalResearchProjectApply;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaDispatchService extends BaseService {

    @Resource
    FiveOaDispatchMapper fiveOaDispatchMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
   ProcessQueryService processQueryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;

    @Resource
    HandleFormService handleFormService;


   public void remove(int id,String userLogin){
       FiveOaDispatch item = fiveOaDispatchMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

       //????????????
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        //??????????????????
       fiveOaWordSizeService.removeWordByBusinessKey(item.getBusinessKey());

   }

   public void update(FiveOaDispatchDto fiveOaDispatchDraftDto){

       FiveOaDispatch model = fiveOaDispatchMapper.selectByPrimaryKey(fiveOaDispatchDraftDto.getId());
       model.setFileId(fiveOaDispatchDraftDto.getFileId());
       model.setDispatchType(fiveOaDispatchDraftDto.getDispatchType());
       model.setSigner(fiveOaDispatchDraftDto.getSigner());
       model.setSignerName(fiveOaDispatchDraftDto.getSignerName());

       model.setCountersign(fiveOaDispatchDraftDto.getCountersign());
       model.setCountersignName(fiveOaDispatchDraftDto.getCountersignName());
       model.setSecretGrade(fiveOaDispatchDraftDto.getSecretGrade());
       model.setAllottedTime(fiveOaDispatchDraftDto.getAllottedTime());
       model.setCompanyOffice(fiveOaDispatchDraftDto.getCompanyOffice());
       model.setCompanyOfficeName(fiveOaDispatchDraftDto.getCompanyOfficeName());
       model.setCompanySecurity(fiveOaDispatchDraftDto.getCompanySecurity());
       model.setCompanySecurityName(fiveOaDispatchDraftDto.getCompanySecurityName());
       model.setAuditMan(fiveOaDispatchDraftDto.getAuditMan());
       model.setAuditManName(fiveOaDispatchDraftDto.getAuditManName());

       model.setDrafter(fiveOaDispatchDraftDto.getDrafter());
       model.setDrafterName(fiveOaDispatchDraftDto.getDrafterName());



       //??????????????????
       Map map = Maps.newHashMap();
       map.put("deleted", false);
       map.put("dispatch",fiveOaDispatchDraftDto.getDispatch());
       if(fiveOaDispatchDraftDto.getDispatch()!=null&&fiveOaDispatchDraftDto.getDispatch()!=""&&!fiveOaDispatchDraftDto.getDispatch().equals(model.getDispatch())){
           if (fiveOaDispatchMapper.selectAll(map).size()>0){
               Assert.state(false,"???????????????????????????????????????");
           }
       }
       model.setDispatch(fiveOaDispatchDraftDto.getDispatch());

       model.setDispatchTitle(fiveOaDispatchDraftDto.getDispatchTitle());
       model.setRealSendMan(fiveOaDispatchDraftDto.getRealSendMan());
       model.setRealSendManName(fiveOaDispatchDraftDto.getRealSendManName());
       model.setCopySendMan(fiveOaDispatchDraftDto.getCopySendMan());
       model.setCopySendManName(fiveOaDispatchDraftDto.getCopySendManName());
       model.setSubjectTerm(fiveOaDispatchDraftDto.getSubjectTerm());
       model.setTypist(fiveOaDispatchDraftDto.getTypist());
       model.setTypistName(fiveOaDispatchDraftDto.getTypistName());
       model.setProofreader(fiveOaDispatchDraftDto.getProofreader());
       model.setProofreaderName(fiveOaDispatchDraftDto.getProofreaderName());
       model.setPrintNumber(fiveOaDispatchDraftDto.getPrintNumber());
       model.setWord(fiveOaDispatchDraftDto.getWord());
       model.setYear(fiveOaDispatchDraftDto.getYear());
       model.setMark(fiveOaDispatchDraftDto.getMark());


       model.setCopyMen(fiveOaDispatchDraftDto.getCopyMen());
       model.setCopyMenName(fiveOaDispatchDraftDto.getCopyMenName());


       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();
      if (fiveOaDispatchDraftDto.getDeptId()!=model.getDeptId()){
          model.setDeptName(fiveOaDispatchDraftDto.getDeptName());
          model.setDeptId(fiveOaDispatchDraftDto.getDeptId());
          variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));
      }
       fiveOaDispatchMapper.updateByPrimaryKey(model);
      if (model.getCountersign().equals("")){
          variables.put("sign", 0);
      }else {
          variables.put("sign", 1);
          variables.put("countersignList", MyStringUtil.getStringList(model.getCountersign()));
      }
       if (StringUtils.isNotEmpty(model.getCopyMen())){
           variables.put("copyMen", model.getCopyMen());
       }
       variables.put("officeChargeMan", MyStringUtil.getStringList(model.getCompanyOffice()));
       variables.put("companyLeader", MyStringUtil.getStringList(model.getSigner()));
       variables.put("processDescription","??????:"+ model.getDispatchTitle());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaDispatchDto getModelById(int id){

           return getDto(fiveOaDispatchMapper.selectByPrimaryKey(id));
    }

    public FiveOaDispatchDto getDto(FiveOaDispatch item) {
        FiveOaDispatchDto dto=FiveOaDispatchDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaDispatchMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

   public int getNewModel(String userLogin){
       List<String> secretary =  hrEmployeeService.selectUserByRoleNames("????????????");
       Assert.state(secretary.size()>0,"?????????????????? ???????????? ??????");

       FiveOaDispatch item=new FiveOaDispatch();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
       item.setDispatchType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
       item.setDeptId(hrEmployeeDto.getDeptId());
       item.setDeptName(hrEmployeeDto.getDeptName());
       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());
       item.setSecretGrade("???");
       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       item.setDispatchTitle("");
       item.setCompanyOffice(StringUtils.join(selectEmployeeService.getParentDeptChargeMen(59,1,false),","));//??????????????? ???????????? 2020-12-22HNZ ??????2??????????????????
       item.setCompanyOfficeName(selectEmployeeService.getNameByUserLogins(item.getCompanyOffice()));

   /*    item.setSigner(StringUtils.join(selectEmployeeService.getDeptChargeMen(item.getDeptId()),","));
       item.setSignerName(selectEmployeeService.getNameByUserLogins(item.getSigner()));*/

       ModelUtil.setNotNullFields(item);
       fiveOaDispatchMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_DISPATCH+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription", item.getDispatchType());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
       variables.put("secretary",secretary);
       item.setBusinessKey(businessKey);
       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DISPATCH,item.getBusinessKey(), variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaDispatchMapper.updateByPrimaryKey(item);
       return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        /* List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        //tth ???????????????
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDispatchMapper.selectAll(params));
        ////??? ???????????????????????????
        //PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDispatchMapper.fuzzySearch(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDispatch item=(FiveOaDispatch)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    //??????
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaDispatch item = fiveOaDispatchMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("dispatchTitle",item.getDispatchTitle());
        data.put("countersignName",item.getCountersignName());
        data.put("countersign",StringUtils.split(item.getCountersign(),","));
        data.put("secretGrade",item.getSecretGrade());
        data.put("allottedTime",item.getAllottedTime());
        data.put("companyOfficeName",item.getCompanyOfficeName());
        data.put("companySecurityName",item.getCompanySecurityName());
        data.put("subjectTerm",item.getSubjectTerm());
        data.put("deptName",item.getDeptName());
        data.put("dispatch",item.getDispatch());
        data.put("dispatchType",item.getDispatchType());
        data.put("realSendManName",item.getRealSendManName());
        data.put("copySendManName",item.getCopySendManName());
        data.put("creator",item.getCreator());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<ActHistoryDto> countersignList=Lists.newArrayList();//??????

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if (dto.getActivityName().contains("??????")){
                data.put("audit",dto);
            }
            if (dto.getActivityName().contains("??????")){
                data.put("signer",dto);
            }
            if (dto.getActivityName().contains("??????")){
                countersignList.add(dto);
            }
        }
        data.put("counterSigners",countersignList);
        data.put("companySecurityName",item.getCompanySecurityName());
        data.put("companySecurity",item.getCompanySecurity());



        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * ??????excl
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map>list=new ArrayList<>();

        //??????????????????
        params.put("isLikeSelect",true);
        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        params.put("startTime",startTime1);
        params.put("endTime",endTime1);

        List<FiveOaDispatch>fiveOaDispatches=fiveOaDispatchMapper.selectAll(params);
        for (FiveOaDispatch dto:fiveOaDispatches) {

            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getDispatchTitle());
            //map1.put("????????????",);
            map1.put("??????",dto.getDispatch());
            map1.put("????????????",dto.getDispatchType());
            map1.put("?????????",dto.getDeptName());
            map1.put("????????????",dto.getSecretGrade());
            map1.put("???????????????", dto.getCompanyOfficeName());
            map1.put("?????????",dto.getSubjectTerm());
            map1.put("????????????",dto.getDeptName());
            map1.put("??????",dto.getRealSendManName());
            map1.put("??????",dto.getCopySendManName());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????",dto.getGmtCreate());

            list.add(map1);
        }
//???????????????????????????????????? ????????????


        return list;
    }
}

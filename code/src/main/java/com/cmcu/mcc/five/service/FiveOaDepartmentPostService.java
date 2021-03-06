package com.cmcu.mcc.five.service;


import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;

import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaDepartmentPostMapper;
import com.cmcu.mcc.five.dto.FiveOaDepartmentPostDto;
import com.cmcu.mcc.five.entity.FiveOaDepartmentPost;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
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
public class FiveOaDepartmentPostService extends BaseService {

    @Resource
    FiveOaDepartmentPostMapper fiveOaDepartmentPostMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
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
    ActService actService;
    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;

    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    CommonFileService commonFileService;
    @Resource
    HandleFormService handleFormService;

   public void remove(int id,String userLogin){
       FiveOaDepartmentPost item = fiveOaDepartmentPostMapper.selectByPrimaryKey(id);
       Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

       //????????????
       handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
       //??????????????????
       fiveOaWordSizeService.removeWordByBusinessKey(item.getBusinessKey());
   }

   public void update(FiveOaDepartmentPostDto item){

       FiveOaDepartmentPost model = fiveOaDepartmentPostMapper.selectByPrimaryKey(item.getId());
       model.setDispatch(item.getDispatch());
       model.setProofreadMan(item.getProofreadMan());
       model.setProofreadManName(item.getProofreadManName());
       model.setDrafter(item.getDrafter());
       model.setDrafterName(item.getDrafterName());
       model.setDeptId(item.getDeptId());
       model.setDeptName(item.getDeptName());
       model.setSubjectTerms(item.getSubjectTerms());
       model.setTitle(item.getTitle());
       model.setTyper(item.getTyper());
       model.setTyperName(item.getTyperName());
       model.setCheckMan(item.getCheckMan());
       model.setCheckManName(item.getCheckManName());
       model.setRealSendMan(item.getRealSendMan());
       model.setRealSendManName(item.getRealSendManName());
       model.setCopySendMan(item.getCopySendMan());
       model.setCopySendManName(item.getCopySendManName());

       model.setPrintNumber(item.getPrintNumber());

       model.setGmtModified(new Date());
       BeanValidator.validateObject(model);
       ModelUtil.setNotNullFields(model);
       Map variables = Maps.newHashMap();

       fiveOaDepartmentPostMapper.updateByPrimaryKey(model);
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(model.getDeptId()));//????????????
       variables.put("processDescription","????????????:"+item.getTitle());
       BeanValidator.check(model);
       myActService.setVariables(model.getProcessInstanceId(),variables);


   }


    public FiveOaDepartmentPostDto getModelById(int id){

           return getDto(fiveOaDepartmentPostMapper.selectByPrimaryKey(id));
    }

    public FiveOaDepartmentPostDto getDto(FiveOaDepartmentPost item) {
        FiveOaDepartmentPostDto dto=FiveOaDepartmentPostDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if (customProcessInstance == null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                fiveOaDepartmentPostMapper.updateByPrimaryKey(dto);
            }
            if (customProcessInstance != null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())) {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }
        dto.setBusinessKey(EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId());
        return dto;
    }

   public int getNewModel(String userLogin){
       FiveOaDepartmentPost item=new FiveOaDepartmentPost();
       HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        //2020-12-24 HNZ?????????????????????????????????????????? ????????????
       item.setDeptId(selectEmployeeService.getHeadDeptId(hrEmployeeDto.getDeptId()));
       item.setDeptName(hrDeptService.getModelById(item.getDeptId()).getName());

       item.setCreator(hrEmployeeDto.getUserLogin());
       item.setCreatorName(hrEmployeeDto.getUserName());

       item.setDrafter(hrEmployeeDto.getUserLogin());//?????????
       item.setDrafterName(hrEmployeeDto.getUserName());

       item.setCheckMan(StringUtils.join(selectEmployeeService.getDeptChargeMen(item.getDeptId()),","));//??????????????????????????????
       item.setCheckManName(selectEmployeeService.getNameByUserLogins(item.getCheckMan()));

       item.setDeleted(false);
       item.setProcessEnd(false);
       item.setCreator(userLogin);
       item.setGmtModified(new Date());
       item.setGmtCreate(new Date());
       ModelUtil.setNotNullFields(item);
       fiveOaDepartmentPostMapper.insert(item);

       String businessKey= EdConst.FIVE_OA_DEPATRMENTPOST+ "_" + item.getId();
       Map variables = Maps.newHashMap();
       variables.put("userLogin", userLogin);
       variables.put("processDescription","????????????:"+item.getDeptName());
       variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));//????????????

       String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_DEPATRMENTPOST,businessKey, variables, MccConst.APP_CODE);
       item.setProcessInstanceId(processInstanceId);
       item.setBusinessKey(businessKey);
       fiveOaDepartmentPostMapper.updateByPrimaryKey(item);
       return item.getId();
   }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaDepartmentPostMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaDepartmentPost item=(FiveOaDepartmentPost)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaDepartmentPost item = fiveOaDepartmentPostMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("checkManName",item.getCheckManName());
        data.put("deptName",item.getDeptName());
        data.put("drafterName",item.getDrafterName());
        data.put("drafter",item.getDrafter());
        data.put("dispatch",item.getDispatch());
        data.put("title",item.getTitle());
        data.put("realSendManName",item.getRealSendManName());
        data.put("copySendManName",item.getCopySendManName());
        data.put("subjectTerms",item.getSubjectTerms());
        data.put("typerName",item.getTyperName());
        data.put("proofreadManName",item.getProofreadManName());
        data.put("printNumber",item.getPrintNumber());
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<CommonFileDto> commonFileDtos = commonFileService.listData(item.getBusinessKey(),0,"");
        data.put("fileList",commonFileDtos);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("???????????????-????????????".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }
    /**
     * ??????excel
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
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
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);


        List<FiveOaDepartmentPost> fiveOaDepartmentPosts=fiveOaDepartmentPostMapper.selectAll(params);
        for (FiveOaDepartmentPost dto:fiveOaDepartmentPosts){

            Map map1=new LinkedHashMap();
            map1.put("??????",dto.getTitle());
            map1.put("??????",dto.getDispatch());
            map1.put("?????????",dto.getDrafterName());
            map1.put("?????????",dto.getCheckManName());
            map1.put("????????????",dto.getDeptName());
            map1.put("?????????",dto.getSubjectTerms());
            map1.put("??????", dto.getRealSendManName());
            map1.put("??????",dto.getCopySendManName());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????",dto.getGmtCreate());

            list.add(map1);
        }


        return list;
    }


}

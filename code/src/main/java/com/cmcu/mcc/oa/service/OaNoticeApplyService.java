package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonFileMapper;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaNoticeApplyMapper;
import com.cmcu.mcc.oa.dao.OaNoticeMapper;
import com.cmcu.mcc.oa.dto.OaNoticeApplyDto;
import com.cmcu.mcc.oa.entity.OaNotice;
import com.cmcu.mcc.oa.entity.OaNoticeApply;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OaNoticeApplyService {
    @Resource
    OaNoticeApplyMapper oaNoticeApplyMapper;
    @Resource
    OaNoticeMapper oaNoticeMapper;
    @Resource
    CommonFileMapper commonFileMapper;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    MyActService myActService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    CommonFileService commonFileService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id, String userLogin) {
        OaNoticeApply item = oaNoticeApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");

        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(OaNoticeApplyDto oaNoticeDto) {
        OaNoticeApply item = oaNoticeApplyMapper.selectByPrimaryKey(oaNoticeDto.getId());
        item.setNoticeTitle(oaNoticeDto.getNoticeTitle());

        item.setNoticeDesc(oaNoticeDto.getNoticeDesc());
        item.setPicUrl(oaNoticeDto.getPicUrl());

        item.setDeptName(oaNoticeDto.getDeptName());
        item.setPublishUserName(oaNoticeDto.getPublishUserName());
        item.setTop(oaNoticeDto.getTop());
        item.setPublish(oaNoticeDto.getPublish());
        item.setRemark(oaNoticeDto.getRemark());
        item.setNoticeContent(oaNoticeDto.getNoticeContent());
        item.setGmtModified(new Date());
        item.setPublishDeptName(oaNoticeDto.getPublishDeptName());
        item.setDeptId(oaNoticeDto.getDeptId());
        item.setNoticeType(oaNoticeDto.getNoticeType());
        item.setNoticeLevel(oaNoticeDto.getNoticeLevel());
        item.setAttachIds(oaNoticeDto.getAttachIds());
        item.setAttachName(oaNoticeDto.getAttachName());
        item.setViewMans(oaNoticeDto.getViewMans());
        item.setViewMansName(oaNoticeDto.getViewMansName());
        item.setNoticeSystemType(oaNoticeDto.getNoticeSystemType());
        item.setPhotoAttachId(oaNoticeDto.getPhotoAttachId());
        item.setPhotoAttachName(oaNoticeDto.getPhotoAttachName());

        item.setPhotoAttachId(oaNoticeDto.getPhotoAttachId());
        item.setPhotoAttachName(oaNoticeDto.getPhotoAttachName());

        if("????????????".equals(item.getNoticeType())){//????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        }

        if (item.getNoticeType().equals("????????????")){
            if (item.getPhotoAttachId()==""){
                Assert.state(false,"????????????????????????????????????");
            }
        }

        BeanValidator.check(item);
        oaNoticeApplyMapper.updateByPrimaryKey(item);

        Map variables = Maps.newHashMap();
        variables.put("processDescription", "???????????????"+item.getNoticeTitle());

        myActService.setVariables(item.getProcessInstanceId(),variables);
    }

    public OaNoticeApplyDto getModelById(int id) {
        OaNoticeApply oaNotice = oaNoticeApplyMapper.selectByPrimaryKey(id);

        return getDto(oaNotice);
    }

    public OaNoticeApplyDto getDto(OaNoticeApply item) {
        OaNoticeApplyDto dto=OaNoticeApplyDto.adapt(item);
        MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName(processInstanceDto.getProcessName());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            AutoTurnNotice(item.getId());
            oaNoticeApplyMapper.updateByPrimaryKey(dto);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("?????????");
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        Map<String, Object> defaultDept = hrDeptService.getDefaultDept2(hrEmployeeDto.getDeptId());
        OaNoticeApply item= new OaNoticeApply();

        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());

        item.setDeptId(Integer.parseInt(defaultDept.get("deptId").toString()));//??????????????????
        item.setDeptName(defaultDept.get("deptName").toString());

       // item.setViewMans(","+hrEmployeeDto.getDeptId().toString()+",");//??????????????????
       // item.setViewMansName(hrEmployeeDto.getDeptName());
        item.setViewMans(",1,");
        item.setViewMansName("????????????????????????????????????");

        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel("");
        item.setNoticeType("");
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(true);
        item.setFirm(false);

        ModelUtil.setNotNullFields(item);
        oaNoticeApplyMapper.insert(item);
        String businessKey= EdConst.OA_NOTICE_APPLY+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "???????????????"+item.getCreatorName());
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_NOTICE_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        oaNoticeApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public int getNewModelByType(String userLogin,String type) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaNoticeApply item= new OaNoticeApply();
        Map<String, Object> defaultDept = hrDeptService.getDefaultDept2(hrEmployeeDto.getDeptId());
        item.setPublishUserName(hrEmployeeDto.getUserName());
        item.setPublishDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(Integer.parseInt(defaultDept.get("deptId").toString()));//??????????????????
        item.setDeptName(defaultDept.get("deptName").toString());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setNoticeLevel(type);
        if ("????????????,????????????,????????????,????????????,????????????,????????????".contains(type)){
            item.setNoticeType(type);
        }else {
           item.setNoticeType("????????????");
        }

/*        item.setViewMans(defaultDept.get("deptId").toString());
        item.setViewMansName(defaultDept.get("deptName").toString());*/
        item.setViewMans(",1,");
        item.setViewMansName("????????????????????????????????????");

        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(false);
        item.setPublish(true);
        item.setFirm(false);

        //xxin
        item.setNoticeDesc("???????????????"+item.getDeptName());
        if("????????????".contains(type)){//????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice2.jpg");
        }else{ //????????????????????????
            item.setPicUrl("/nochange/wuzhou_notice1.jpg");
        }

        ModelUtil.setNotNullFields(item);
        oaNoticeApplyMapper.insert(item);
        String businessKey= EdConst.OA_NOTICE_APPLY+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", type+"???"+item.getCreatorName());
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_NOTICE_APPLY,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        oaNoticeApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        //params.put("creator",userLogin);//isLikeSelect
        params.put("isLikeSelect",true);

        /*if ("????????????,????????????,????????????,????????????,????????????,????????????".contains(params.get("noticeTypes").toString())){
            params.put("noticeType",params.get("noticeTypes"));
        }else {
            params.put("deptName",params.get("noticeTypes"));
            params.put("noticeLevel",params.get("noticeTypes"));
        }*/

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaNoticeApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaNoticeApply item=(OaNoticeApply)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**????????????*/
    public int getNewModelByKeyLevel(OaNoticeApply oaNoticeApply,String type ) {
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("noticeTitle",oaNoticeApply.getNoticeTitle());
        map.put("noticeLevel",type);
        //??????????????????????????? ?????????????????????
        if (oaNoticeMapper.selectAll(map).size()>0){
            return 0;
        }
        OaNotice item=new OaNotice();
        item.setPublishUserName(oaNoticeApply.getPublishUserName());
        item.setPublishDeptName(oaNoticeApply.getPublishDeptName());
        item.setDeptId(oaNoticeApply.getDeptId());
        item.setNoticeType(type);
        item.setNoticeLevel(type);
        item.setCreator(oaNoticeApply.getCreator());
        item.setCreatorName(oaNoticeApply.getCreatorName());
        item.setDeptName(oaNoticeApply.getDeptName());
        item.setNoticeTitle(oaNoticeApply.getNoticeTitle());
        item.setNoticeContent(oaNoticeApply.getNoticeContent());
        item.setNoticeDesc(oaNoticeApply.getNoticeDesc());
        item.setPicUrl(oaNoticeApply.getPicUrl());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(oaNoticeApply.getTop());//??????
        item.setPublish(oaNoticeApply.getPublish());//??????
        item.setFirm(false);//?????????
        item.setPass(false);//?????????
        item.setViewMans(oaNoticeApply.getViewMans());//????????????
        item.setViewMansName(oaNoticeApply.getViewMansName());
        if ("????????????".equals(type)){
            item.setFirm(true);
        }
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        getCommonFile(item.getBusinessKey(),oaNoticeApply.getBusinessKey());//????????????
        return item.getId();
    }

    /**???????????????*/
    public int getNewModelByKeyDept(OaNoticeApply oaNoticeApply,String type) {
        OaNotice item=new OaNotice();
        Map map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("noticeTitle",oaNoticeApply.getNoticeTitle());
        map.put("noticeLevel","????????????");
        //??????????????????????????? ?????????????????????
        if (oaNoticeMapper.selectAll(map).size()>0){
            return 0;
        }
        item.setPublishUserName(oaNoticeApply.getPublishUserName());
        item.setPublishDeptName(oaNoticeApply.getPublishDeptName());
        item.setDeptId(hrDeptService.getModelByName(type).getId());
        item.setDeptName(type);
        item.setNoticeLevel("????????????");
        item.setNoticeType("?????????");
        item.setCreator(oaNoticeApply.getCreator());
        item.setCreatorName(oaNoticeApply.getCreatorName());
        item.setNoticeLevel("????????????");
        item.setNoticeTitle(oaNoticeApply.getNoticeTitle());
        item.setNoticeContent(oaNoticeApply.getNoticeContent());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setTop(oaNoticeApply.getTop());
        item.setPublish(oaNoticeApply.getPublish());
        item.setFirm(oaNoticeApply.getFirm());
        item.setPass(false);
        ModelUtil.setNotNullFields(item);
        oaNoticeMapper.insert(item);
        item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
        oaNoticeMapper.updateByPrimaryKey(item);
        getCommonFile(item.getBusinessKey(),oaNoticeApply.getBusinessKey());//????????????
        return item.getId();
    }

    /**
     * ????????????
     * @param id
     */
    public void AutoTurnNotice(int id){
        OaNoticeApply oaNoticeApply = oaNoticeApplyMapper.selectByPrimaryKey(id);

        if (!oaNoticeApply.getDeleted()){
            OaNotice item=new OaNotice();
            item.setNoticeType(oaNoticeApply.getNoticeType());
            item.setNoticeTitle(oaNoticeApply.getNoticeTitle());
            item.setPublishUserName(oaNoticeApply.getPublishUserName());
            item.setTop(oaNoticeApply.getTop());
            item.setPublish(oaNoticeApply.getPublish());
            item.setRemark(oaNoticeApply.getRemark());
            item.setNoticeContent(oaNoticeApply.getNoticeContent());
            item.setPublishDeptName(oaNoticeApply.getPublishDeptName());
            item.setDeptId(oaNoticeApply.getDeptId());
            item.setDeptName(oaNoticeApply.getDeptName());
            item.setFirm(oaNoticeApply.getFirm());
            item.setNoticeLevel(oaNoticeApply.getNoticeLevel());
            item.setAttachIds(oaNoticeApply.getAttachIds());
            item.setAttachName(oaNoticeApply.getAttachName());
            item.setViewMans(oaNoticeApply.getViewMans());
            item.setViewMansName(oaNoticeApply.getViewMansName());
            item.setNoticeSystemType(oaNoticeApply.getNoticeSystemType());
            //????????????
            item.setPhotoAttachId(oaNoticeApply.getPhotoAttachId());
            item.setPhotoAttachName(oaNoticeApply.getPhotoAttachName());
            //??????????????? ??????????????????
            if (!"????????????,????????????,????????????,????????????,????????????,????????????".contains(item.getNoticeType())){
                item.setDeptId(oaNoticeApply.getDeptId());
                item.setDeptName(oaNoticeApply.getDeptName());
                item.setNoticeType("????????????");

            }
            item.setCreator(oaNoticeApply.getCreator());
            item.setCreatorName(oaNoticeApply.getCreatorName());
            item.setPageLoad(0);
            item.setPass(false);
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());

            //xxin ???????????? ??????????????????
            item.setPicUrl(oaNoticeApply.getPicUrl());
            //
            item.setNoticeDesc("???????????????"+item.getPublishDeptName());

            ModelUtil.setNotNullFields(item);
            oaNoticeMapper.insert(item);
            item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
            oaNoticeMapper.updateByPrimaryKey(item);
            getCommonFile(item.getBusinessKey(),oaNoticeApply.getBusinessKey()); //????????????
            //?????????????????????????????? ??????????????????????????????
            if (item.getNoticeType().equals("????????????")){
                item.setNoticeType("????????????");
                item.setNoticeLevel("????????????");
                oaNoticeMapper.insert(item);
                item.setBusinessKey(EdConst.OA_NOTICE+"_"+item.getId());
                oaNoticeMapper.updateByPrimaryKey(item);
                getCommonFile(item.getBusinessKey(),oaNoticeApply.getBusinessKey()); //????????????
            }

        }
    }

    /**
     * ?????? ????????????????????????
     * @param businessKey ??????????????????
     * @param businessId ????????????????????????
     */
    public void getCommonFile(String businessKey,String businessId){
        List<CommonFileDto> commonFileDtos = commonFileService.listData(businessId,0,"");
        for (CommonFileDto dto:commonFileDtos){
            dto.setId(0);
            dto.setBusinessKey(businessKey);
            dto.setGmtModified(new Date());
            commonFileMapper.insert(dto);
        }
    }


    /**
     * ????????????
     * @param id
     * @param types
     */
    public void manualTurnNotice(int id,String types){
        OaNoticeApply oaNoticeApply = oaNoticeApplyMapper.selectByPrimaryKey(id);
        if (!oaNoticeApply.getDeleted()){
            List<String> typeList = MyStringUtil.getStringList(types);
            for (String type:typeList){
                if ("????????????,????????????,????????????,????????????,????????????".contains(type)){
                    getNewModelByKeyLevel(oaNoticeApply,type);
                }else {
                    getNewModelByKeyDept(oaNoticeApply,type);
                }
            }
        }
    }

}

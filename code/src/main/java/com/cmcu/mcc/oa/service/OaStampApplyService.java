package com.cmcu.mcc.oa.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaStampApplyMapper;
import com.cmcu.mcc.oa.dto.OaStampApplyDto;
import com.cmcu.mcc.oa.entity.OaStampApply;
import com.cmcu.mcc.service.ActService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class OaStampApplyService {

    @Resource
    OaStampApplyMapper oaStampApplyMapper;
    @Autowired
    ActService actService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Resource
    CommonCodeService commonCodeService;
    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    TaskHandleService taskHandleService;


    public void remove(int id, String userLogin) {
        OaStampApply item = oaStampApplyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaStampApplyMapper.updateByPrimaryKey(item);
    }

    public void update(OaStampApplyDto oaStampApplyDto) {
        OaStampApply item = oaStampApplyMapper.selectByPrimaryKey(oaStampApplyDto.getId());
        item.setStampName(oaStampApplyDto.getStampName());
        item.setStampDate(oaStampApplyDto.getStampDate());
        item.setFileName(oaStampApplyDto.getFileName());
        item.setFileCount(oaStampApplyDto.getFileCount());
        item.setLegalReview(oaStampApplyDto.getLegalReview());
        item.setLegalMan(oaStampApplyDto.getLegalMan());
        item.setLegalManName(oaStampApplyDto.getLegalManName());
        item.setFunctionDeptId(oaStampApplyDto.getFunctionDeptId());
        item.setFunctionDeptName(oaStampApplyDto.getFunctionDeptName());
        item.setCompanyLevel(oaStampApplyDto.getCompanyLevel());
        item.setViceLeader(oaStampApplyDto.getViceLeader());
        item.setViceLeaderName(oaStampApplyDto.getViceLeaderName());
        item.setQualityCompanyMan(oaStampApplyDto.getQualityCompanyMan());
        item.setQualityCompanyManName(oaStampApplyDto.getQualityCompanyManName());
        item.setOnline(oaStampApplyDto.getOnline());
        item.setFinishedMan(oaStampApplyDto.getFinishedMan());
        item.setFinishedManName(oaStampApplyDto.getFinishedManName());
        item.setContractSealMan(oaStampApplyDto.getContractSealMan());
        item.setContractSealManName(oaStampApplyDto.getContractSealManName());
        item.setGmtModified(new Date());
        //region 设置变量
        List<String> copyList=Lists.newArrayList();

        Map variables = Maps.newHashMap();
        variables.put("sign", 0);

        if ( item.getStampName().contains("公司章")||item.getStampName().contains("法人章")||item.getStampName().contains("法人签名")){
            copyList.add("0738");//公司办盖章抄送  秦秀霞
            copyList.add("2766");//公司办盖章抄送  伍群峰
        }
        if (item.getStampName().contains("合同章")){
            if (item.getCompanyLevel().contains("公司级")){
                variables.put("companyLevel", 1);//是否需要合同评审人员
                variables.put("contractSealMan", item.getContractSealMan());
            }else {
                variables.put("companyLevel",0);//公司章
            }
            variables.put("viceLeader", item.getViceLeader());//副总审批
        }

        if (item.getStampName().contains("施工")){
            copyList.add("2623");//经营发展部（盖章）：封金成
        }

        if (item.getStampName().contains("压力")){
            copyList.add("2626");//信息化建设与管理部（盖章）：沈晓晨
            variables.put("qualityCompanyMan", item.getQualityCompanyMan());//压力管道公司技术人员
            if (item.getOnline().equals("线上")){
                variables.put("online", 1);
                variables.put("finishedDeptChargeMen",selectEmployeeService.getDeptChargeMen(13));//成品室负责人
            }else {
                variables.put("online", 0);
            }
        }
         if (item.getLegalReview()){
             variables.put("sign", 1);
             List<String> secretary =  hrEmployeeService.selectUserByRoleNames("法律审查");
             copyList.add("2055");//经过法律审查需要 抄送林芳
             variables.put("legalReviewMan",secretary);
         }
        variables.put("copyMenList",copyList);
        //endregion
        item.setRemark(oaStampApplyDto.getRemark());
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaStampApplyMapper.updateByPrimaryKey(item);
        myActService.setVariables(item.getProcessInstanceId(),variables);

    }

    public OaStampApplyDto getModelById(int id) {
        return getDto(oaStampApplyMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaStampApply item = new OaStampApply();
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setOnline(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审方式").toString());
        item.setCompanyLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审级别").toString());
        item.setLegalReview(false);
        ModelUtil.setNotNullFields(item);
        oaStampApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("description", "用印申请："+item.getCreatorName());
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        String businessKey = EdConst.OA_STAMPAPPLY+"_"+item.getId();
        String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMPAPPLY,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        oaStampApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * 通过
     * @param userLogin
     * @param stampName
     * @return
     */
    public int getNewModelByStampName(String userLogin,String stampName) {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaStampApply item = new OaStampApply();
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setStampName(stampName);
        item.setOnline(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审方式").toString());
        item.setCompanyLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"评审级别").toString());
        item.setLegalReview(false);
        ModelUtil.setNotNullFields(item);
        oaStampApplyMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("description", "用印申请："+stampName+"-"+item.getCreatorName());
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDeptId()));


        //公司章
        if ( item.getStampName().contains("公司章")||item.getStampName().contains("法人章")||item.getStampName().contains("法人签名")){
            variables.put("officeMen", selectEmployeeService.getDeptChargeMen(59));//公司办公室负责人

            String businessKey = EdConst.OA_STAMP_APPLY_OFFICE+"_"+item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMP_APPLY_OFFICE,businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
        }
        //合同章
        if (item.getStampName().contains("合同章")){

            String businessKey = EdConst.OA_STAMP_APPLY_CONTRACT+"_"+item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMP_APPLY_CONTRACT,businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
        }
        //施工章
        if (item.getStampName().contains("施工")){
            variables.put("businessSeal", 1);
            variables.put("businessChargeMan", selectEmployeeService.getDeptChargeMen(48));//经营发展部负责人
            String businessKey = EdConst.OA_STAMP_APPLY_CONSTRUCTION+"_"+item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMP_APPLY_CONSTRUCTION,businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
        }
        //压力章
        if (item.getStampName().contains("压力")){
            variables.put("qualityMen", selectEmployeeService.getDeptChargeMen(11));//技术质量部负责人
            String businessKey = EdConst.OA_STAMP_APPLY_QUALITY+"_"+item.getId();
            String processInstanceId = taskHandleService.startProcess(EdConst.OA_STAMP_APPLY_QUALITY,businessKey, variables, MccConst.APP_CODE);
            item.setProcessInstanceId(processInstanceId);
        }

        oaStampApplyMapper.updateByPrimaryKey(item);
        return item.getId();
    }


    public OaStampApplyDto getDto(OaStampApply item) {
        OaStampApplyDto oaNoticeDto = OaStampApplyDto.adapt(item);
        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(item.getProcessInstanceId());
        oaNoticeDto.setProcessName(processInstanceDto.getProcessName());
        oaNoticeDto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            item.setProcessEnd(true);
            oaStampApplyMapper.updateByPrimaryKey(item);
        }
        return oaNoticeDto;
    }

    //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin,String uiSref,String types,int pageNum, int pageSize) {
        params.put("deleted", false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        /*分类查看*/
        List<String> stringList = MyStringUtil.getStringList(types);
        params.put("typeList",stringList);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> oaStampApplyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            OaStampApply item = (OaStampApply) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);

        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        OaStampApply item = oaStampApplyMapper.selectByPrimaryKey(id);
        data.put("remark",item.getRemark());
        data.put("fileName",item.getFileName());
        data.put("stampDate",item.getStampDate());
        data.put("fileCount",item.getFileCount());
        data.put("stampName",item.getStampName());
        if(item.getLegalReview()){
            data.put("legalReview","是");
        }else{
            data.put("legalReview","否");
        }
        data.put("companyLevel",item.getCompanyLevel());
        data.put("viceLeaderName",item.getViceLeaderName());
        data.put("functionDeptName",item.getFunctionDeptName());
        data.put("qualityCompanyManName",item.getQualityCompanyManName());
        data.put("online",item.getOnline());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }


}

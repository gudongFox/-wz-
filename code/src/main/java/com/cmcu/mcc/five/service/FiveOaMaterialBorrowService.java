package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaMaterialBorrowDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaMaterialBorrowMapper;
import com.cmcu.mcc.five.dto.FiveOaMaterialBorrowDto;
import com.cmcu.mcc.five.entity.FiveOaMaterialBorrow;
import com.cmcu.mcc.five.entity.FiveOaMaterialBorrowDetail;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaMaterialBorrowService extends BaseService {
    @Resource
    FiveOaMaterialBorrowMapper fiveOaMaterialBorrowMapper;
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
    FiveOaMaterialBorrowDetailMapper fiveOaMaterialBorrowDetailMapper;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaMaterialBorrow item = fiveOaMaterialBorrowMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaMaterialBorrowDto fiveOaMaterialBorrowDto){
        FiveOaMaterialBorrow model = fiveOaMaterialBorrowMapper.selectByPrimaryKey(fiveOaMaterialBorrowDto.getId());
        model.setApplicantName(fiveOaMaterialBorrowDto.getApplicantName());
        model.setApplicant(fiveOaMaterialBorrowDto.getApplicant());
        model.setApplicantNo(fiveOaMaterialBorrowDto.getApplicantNo());
        model.setApplicantTel(fiveOaMaterialBorrowDto.getApplicantTel());
        model.setApplicantMajor(fiveOaMaterialBorrowDto.getApplicantMajor());
        model.setDeptId(fiveOaMaterialBorrowDto.getDeptId());
        model.setDeptName(fiveOaMaterialBorrowDto.getDeptName());
        model.setBorrow(fiveOaMaterialBorrowDto.getBorrow());
        model.setConsult(fiveOaMaterialBorrowDto.getConsult());
        model.setCopy(fiveOaMaterialBorrowDto.getCopy());
        model.setCount(fiveOaMaterialBorrowDto.getCount());
        model.setApplicantComment(fiveOaMaterialBorrowDto.getApplicantComment());
        model.setBorrowerComment(fiveOaMaterialBorrowDto.getBorrowerComment());
        model.setFileTransferComment(fiveOaMaterialBorrowDto.getFileTransferComment());
        model.setFileTransferRecieve(fiveOaMaterialBorrowDto.getFileTransferRecieve());
        model.setBorrowerReturn(fiveOaMaterialBorrowDto.getBorrowerReturn());
        model.setRemark(fiveOaMaterialBorrowDto.getRemark());
        model.setGmtModified(new Date());
        BeanValidator.check(model);
        fiveOaMaterialBorrowMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(fiveOaMaterialBorrowDto.getDeptId()));
        variables.put("blueprint",0);//蓝图
        variables.put("draft",0);//底图
        variables.put("word",0);//文书
        variables.put("elecword",0);//电子档案
        List<FiveOaMaterialBorrowDetail> listDetail = listDetail(model.getId());
        for (FiveOaMaterialBorrowDetail detail:listDetail) {
            if (detail.getBlueprint()){
                variables.put("blueprint",1);
            }
            if (detail.getDraft()){
                variables.put("draft",1);
            }
            if (detail.getWord()){
                variables.put("word",1);
            }
            if (detail.getPdf()&&detail.getDwg()){
                variables.put("elecword",1);
            }
        }
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaMaterialBorrowDto getModelById(int id){

        return getDto(fiveOaMaterialBorrowMapper.selectByPrimaryKey(id));
    }

    public FiveOaMaterialBorrowDto getDto(FiveOaMaterialBorrow item) {
        FiveOaMaterialBorrowDto dto=FiveOaMaterialBorrowDto.adapt(item);
        dto.setProcessName("已完成");
        if (!dto.getProcessEnd()) {

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaMaterialBorrowMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaMaterialBorrow item=new FiveOaMaterialBorrow();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setApplicantName(hrEmployeeDto.getUserName());
        item.setApplicantNo(hrEmployeeDto.getUserLogin());
        item.setApplicantTel(hrEmployeeDto.getMobile());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setApplicantMajor(hrEmployeeDto.getSpecialty());

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaMaterialBorrowMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_MATERIAL_BORROW+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));//发起者部门领导
        variables.put("processDescription", "档案资料借阅电子复制申请表："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_MATERIAL_BORROW,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveOaMaterialBorrowMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaMaterialBorrowMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaMaterialBorrow item=(FiveOaMaterialBorrow)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public void updateDetail(FiveOaMaterialBorrowDetail item){
        FiveOaMaterialBorrowDetail model = fiveOaMaterialBorrowDetailMapper.selectByPrimaryKey(item.getId());
        model.setFileName(item.getFileName());
        model.setFileNo(item.getFileNo());
        model.setFileType(item.getFileType());
        model.setMajor(item.getMajor());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setFileLevel(item.getFileLevel());
        model.setDraft(item.getDraft());
        model.setBlueprint(item.getBlueprint());
        model.setWord(item.getWord());
        model.setDwg(item.getDwg());
        model.setPdf(item.getPdf());
        model.setCount(item.getCount());
        model.setRemark(item.getRemark());
        fiveOaMaterialBorrowDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaMaterialBorrowDetail getNewModelDetail(int  id){
        FiveOaMaterialBorrowDetail item=new FiveOaMaterialBorrowDetail();
        item.setDraft(false);
        item.setBlueprint(false);
        item.setWord(false);
        item.setDwg(false);
        item.setPdf(false);
        item.setMaterialBorrowId(id);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaMaterialBorrowDetailMapper.insert(item);
        return item;

    }

    public FiveOaMaterialBorrowDetail getModelDetailById(int id){
        return fiveOaMaterialBorrowDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaMaterialBorrowDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("materialBorrowId",id);//小写
        List<FiveOaMaterialBorrowDetail> list = fiveOaMaterialBorrowDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveOaMaterialBorrowDetail model = fiveOaMaterialBorrowDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaMaterialBorrowDetailMapper.updateByPrimaryKey(model);
    }
    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaMaterialBorrow item = fiveOaMaterialBorrowMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("applicantName",item.getApplicantName());
        data.put("applicantNo",item.getApplicantNo());
        data.put("deptName",item.getDeptName());
        data.put("applicantMajor",item.getApplicantMajor());
        data.put("applicantTel",item.getApplicantTel());
        data.put("borrow",item.getBorrow());
        data.put("consult",item.getConsult());
        data.put("copy",item.getCopy());
        data.put("count",item.getCount());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialBorrowId",item.getId());
        map.put("deleted",false);
        List<FiveOaMaterialBorrowDetail> materialBorrow = fiveOaMaterialBorrowDetailMapper.selectAll (map);
        data.put("materialBorrow",materialBorrow);

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("发起者".equals(dto.getActivityName())){
                data.put("startMan",dto);
            }
            if ("部门领导-协同".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("蓝图管理".equals(dto.getActivityName())){
                data.put("blueprint",dto);
            }
            if ("底图管理".equals(dto.getActivityName())){
                data.put("baseMap",dto);
            }
            if ("文书管理".equals(dto.getActivityName())){
                data.put("officeCopy",dto);
            }
            if ("电子档案管理".equals(dto.getActivityName())){
                data.put("electronicRecord",dto);
            }
            if ("申请人-归还".equals(dto.getActivityName())){
                data.put("returnMan",dto);
            }
            if ("蓝图管理-归还".equals(dto.getActivityName())){
                data.put("returnBlueprint",dto);
            }
            if ("底图管理-归还".equals(dto.getActivityName())){
                data.put("returnBaseMap",dto);
            }
            if ("文书管理-归还".equals(dto.getActivityName())){
                data.put("returnOfficeCopy",dto);
            }
            if ("电子档案管理-归还".equals(dto.getActivityName())){
                data.put("returnElectronicRecord",dto);
            }
        }

        data.put("nodes",actHistoryDtos);

        return data;
    }
}

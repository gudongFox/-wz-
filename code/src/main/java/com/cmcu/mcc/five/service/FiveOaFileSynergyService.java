package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaFileSynergyMapper;
import com.cmcu.mcc.five.dto.FiveOaFileSynergyDto;
import com.cmcu.mcc.five.entity.FiveOaFileSynergy;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveOaFileSynergyService extends BaseService {
    @Resource
    HandleFormService handleFormService;
    @Resource
    FiveOaFileSynergyMapper fiveOaFileSynergyMapper;
    @Resource
    MyActService myActService;
    @Resource
    SelectEmployeeService selectEmployeeService;
    @Resource
    ProcessQueryService processQueryService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    TaskHandleService taskHandleService;
    @Resource
    HrEmployeeSysService hrEmployeeSysService;

    public void remove(int id,String userLogin){
        FiveOaFileSynergy item = fiveOaFileSynergyMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveOaFileSynergyDto item){
        FiveOaFileSynergy model = fiveOaFileSynergyMapper.selectByPrimaryKey(item.getId());

        model.setContent(item.getContent());
        model.setChargeMen(item.getChargeMen());
        model.setChargeMenName(item.getChargeMenName());
        model.setSigner(item.getSigner());
        model.setSignerName(item.getSignerName());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setRemark(item.getRemark());
        model.setGmtModified(item.getGmtModified());
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        ModelUtil.setNotNullFields(model);
        Map variables = Maps.newHashMap();
        variables.put("flag",false);
        variables.put("sign",false);
        if (!"".equals(model.getSigner())){
            variables.put("sign",true);
            variables.put("signerList", MyStringUtil.getStringList(model.getSigner()));
        }
        if (!"".equals(model.getChargeMen())){
            variables.put("flag",true);
            variables.put("chargeMenList", MyStringUtil.getStringList(model.getChargeMen()));
        }

        fiveOaFileSynergyMapper.updateByPrimaryKey(model);
        variables.put("processDescription","协同文件："+item.getContent());
        BeanValidator.check(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }

    public FiveOaFileSynergyDto getModelById(int id){

        return getDto(fiveOaFileSynergyMapper.selectByPrimaryKey(id));
    }

    public FiveOaFileSynergyDto getDto(FiveOaFileSynergy item) {
        FiveOaFileSynergyDto dto=FiveOaFileSynergyDto.adapt(item);
        dto.setProcessName("已完成");

        if (!dto.getProcessEnd()){

            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaFileSynergyMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveOaFileSynergy item=new FiveOaFileSynergy();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveOaFileSynergyMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_FILE_SYNERGY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","协同文件："+item.getCreatorName());

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_FILE_SYNERGY,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveOaFileSynergyMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
      /*  List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }*/
        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

      PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaFileSynergyMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaFileSynergy item=(FiveOaFileSynergy)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


}

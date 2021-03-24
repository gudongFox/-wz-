package com.cmcu.mcc.hr.service;


import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.hr.dao.HrInventMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrInventDto;
import com.cmcu.mcc.hr.entity.HrInvent;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HrInventService {

    @Resource
    ActService actService;

    @Resource
    HrInventMapper hrInventMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Resource
    TaskHandleService taskHandleService;

    @Resource
    SelectEmployeeService  selectEmployeeService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    MyActService myActService;
    @Autowired
    CommonFileService commonFileService;


    private HrInventDto getDto(HrInvent item) {
        HrInventDto dto=HrInventDto.adapt(item);

        MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        dto.setProcessName(processInstanceDto.getProcessName());


        if(!item.getProcessEnd()&&processInstanceDto.isProcessEnd()){
            dto.setProcessEnd(true);
            hrInventMapper.updateByPrimaryKey(dto);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    public int getNewModel(String userLogin) {
        Date now = new Date();

        HrInvent item = new HrInvent();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setUserLogin(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setGmtModified(now);
        item.setGmtCreate(now);
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        hrInventMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_INVENT_APPLY+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "专利申请："+item.getCreatorName());
        variables.put("deptChargeMen", selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("specialistChargeMen",selectEmployeeService.getUserListByRoleName("专家委负责人"));//专家委负责人
        variables.put("specialistOtherChargeMen",selectEmployeeService.getUserListByRoleName("专家委分管副总"));//专家委分管副总


        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INVENT_APPLY,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        hrInventMapper.updateByPrimaryKey(item);

        return item.getId();
    }

    public void update(HrInventDto item){

        HrInvent model=hrInventMapper.selectByPrimaryKey(item.getId());

        model.setUserLogin(item.getUserLogin());
        model.setUserName(item.getUserName());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setCooperator(item.getCooperator());
        model.setApplyNameSort(item.getApplyNameSort());
        model.setInventName(item.getInventName());
        model.setMajorName(item.getMajorName());
        model.setFirstInventMan(item.getFirstInventMan());
        model.setFirstInventManName(item.getFirstInventManName());
        model.setOtherInventMen(item.getOtherInventMen());
        model.setIdNumber(item.getIdNumber());
        model.setSecurityDec(item.getSecurityDec());
        model.setTechnologyAndInnovate(item.getTechnologyAndInnovate());
        model.setRetruevalDec(item.getRetruevalDec());
        model.setTechnologyMarket(item.getTechnologyMarket());
        model.setApplyTime(item.getApplyTime());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());

        ModelUtil.setNotNullFields(model);
        hrInventMapper.updateByPrimaryKey(model);
    }

    public void remove(int id,String userLogin) {
        HrInvent model = hrInventMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"该数据是用户"+model.getCreatorName()+"创建");

        //流程作废
        myActService.delete(model.getProcessInstanceId(),"删除",userLogin);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        hrInventMapper.updateByPrimaryKey(model);
    }

    public HrInventDto getModelById(int id) {
        return getDto(hrInventMapper.selectByPrimaryKey(id));
    }

    /**
     * 查询个人申请专利
     * @param userLogin
     * @return
     */
    public List<HrInvent> listData(String userLogin) {
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("creator",userLogin);
        List<HrInvent> Invents =hrInventMapper.selectAll(map);
        List<HrInvent> list = Lists.newArrayList();
        Invents.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrInventMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrInvent item=(HrInvent) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        HrInvent item = hrInventMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("cooperator",item.getCooperator());
        data.put("applyNameSort",item.getApplyNameSort());
        data.put("inventName",item.getInventName());
        data.put("majorName",item.getMajorName());
        data.put("firstInventManName",item.getFirstInventManName());
        data.put("otherInventMen",item.getOtherInventMen());
        data.put("idNumber",item.getIdNumber());
        data.put("applyTime",item.getApplyTime());
        data.put("securityDec",item.getSecurityDec());
        data.put("technologyAndInnovate",item.getTechnologyAndInnovate());
        data.put("retruevalDec",item.getRetruevalDec());
        data.put("technologyMarket",item.getTechnologyMarket());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        List<CommonFileDto> commonFileDtos = commonFileService.listData(item.getBusinessKey(),0,"");
        data.put("fileList",commonFileDtos);


        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());
        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("部门领导-审批".equals(dto.getActivityName())){
                data.put("deptChargeMen",dto);
            }
            if ("专家委-审批".equals(dto.getActivityName())){
                data.put("expertCommittee,",dto);
            }
            if ("专家委分管副总".equals(dto.getActivityName())){
                data.put("deputyManager",dto);
            }
        }
        data.put("nodes",actHistoryDtos);

        return data;
    }

}

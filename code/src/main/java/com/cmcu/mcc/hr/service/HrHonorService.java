package com.cmcu.mcc.hr.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.ed.dto.EdFileDto;
import com.cmcu.mcc.ed.service.EdFileService;
import com.cmcu.mcc.hr.dao.HrHonorMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrHonorDto;
import com.cmcu.mcc.hr.entity.HrHonor;
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
public class HrHonorService {
    @Resource
    HrHonorMapper hrHonorMapper;
    @Resource
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;

    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Resource
    TaskHandleService taskHandleService;
    @Resource
    EdFileService edFileService;
    @Autowired
    CommonCodeService commonCodeService;

    public void remove(int id, String userLogin) {
        HrHonor item = hrHonorMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        Assert.state(item.getUserLogin().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
       hrHonorMapper.updateByPrimaryKey(item);
    }
    public void update(HrHonorDto hrHonorDto) {
        HrHonor item = hrHonorMapper.selectByPrimaryKey(hrHonorDto.getId());
       item.setHonortNo(hrHonorDto.getHonortNo());
       item.setHonorType(hrHonorDto.getHonorType());
       item.setHonorName(hrHonorDto.getHonorName());
       item.setHonorUnits(hrHonorDto.getHonorUnits());
       item.setHonorLevel(hrHonorDto.getHonorLevel());
       item.setHonorDate(hrHonorDto.getHonorDate());
       item.setRemark(hrHonorDto.getRemark());
       item.setGmtModified(new Date());
       hrHonorMapper.updateByPrimaryKey(item);
    }
    public int getNewModel(String userLogin) {
        HrHonor item=new HrHonor();
        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        item.setUserLogin(hrEmployeeDto.getUserLogin());
        item.setUserName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setHonorLevel(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"个人荣誉").toString());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        hrHonorMapper.insert(item);


        //流程发起
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "参保证明申请:"+item.getUserName());
        List<String> deptChargeMen = selectEmployeeService.getDeptChargeMen(item.getDeptId());
        variables.put("deptChargeMen", deptChargeMen);
        String processInstanceId = taskHandleService.startProcess(EdConst.HR_HONOR,EdConst.HR_HONOR+"_"+item.getId(),variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        hrHonorMapper.updateByPrimaryKey(item);
        return item.getId();
    }
    public HrHonorDto getModelById(int id) {
        return getDto(hrHonorMapper.selectByPrimaryKey(id));
    }

    public HrHonorDto getDto(Object item) {
        HrHonorDto dto=HrHonorDto.adapt((HrHonor) item);
        HrHonor model= (HrHonor) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());
        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrHonorMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.remove("userLogin");
        boolean IsChangreMen = selectEmployeeService.getPrincipalByUserLogin(userLogin);
        if (IsChangreMen){
            List<Integer> deptIdList =new ArrayList<>();
            deptIdList.add(selectEmployeeService.selectByUserLogin(userLogin).getDeptId());
            params.put("deptIdList", deptIdList);
        }else {
            params.put("userLogin",userLogin);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrHonorMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrHonor item=(HrHonor)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    /**
     * 获取个人所有获奖证书附件 （已完成流程）
     * @param userLogin
     * @return
     */
    public List<EdFileDto> getAllEdFile(String userLogin){
        List<EdFileDto> list=new ArrayList<>();
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        map.put("processEnd",true);
        List<HrHonor> hrHonorList =hrHonorMapper.selectAll(map);
        for (HrHonor dto:hrHonorList){
            String businessId="hrHonor_"+dto.getId();
            List<EdFileDto> edFileDtoList = edFileService.listData(businessId);
            if (edFileDtoList.size()>0){
                list.addAll(edFileDtoList);
            }
        }
        return list;
    }
}

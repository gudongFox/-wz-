package com.cmcu.mcc.hr.service;

import com.cmcu.common.data.IDataService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.dto.ProcessInstanceDto;
import com.cmcu.mcc.hr.dao.HrIncomeProofMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrIncomeProofDto;
import com.cmcu.mcc.hr.entity.HrIncomeProof;
import com.cmcu.mcc.service.ActService;
import com.cmcu.common.dao.CommonConfigMapper;
import com.cmcu.common.entity.CommonConfig;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrIncomeProofService implements IDataService<HrIncomeProofDto> {

    @Resource
    HrIncomeProofMapper hrIncomeProofMapper;

    @Autowired
    ActService actService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Resource
    CommonConfigMapper commonConfigMapper;



    @Override
    public void remove(int id, String userLogin) {
        HrIncomeProof item = hrIncomeProofMapper.selectByPrimaryKey(id);
        item.setGmtModified(new Date());
        item.setDeleted(true);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            Assert.state(actService.checkProcessOnUser(item.getProcessInstanceId(), userLogin), "任务在其他环节处理中,无法删除!");
        }
        if (StringUtils.isNotEmpty(item.getProcessInstanceId())) {
            actService.delete(item.getProcessInstanceId(),"",userLogin);
        }
        hrIncomeProofMapper.updateByPrimaryKey(item);
    }

    @Override
    public void update(HrIncomeProofDto hrIncomeProofDto) {

        HrIncomeProof item = hrIncomeProofMapper.selectByPrimaryKey(hrIncomeProofDto.getId());
        item.setUserLogin(hrIncomeProofDto.getUserLogin());
        item.setUserName(hrIncomeProofDto.getUserName());
        item.setIdCardType(hrIncomeProofDto.getIdCardType());
        item.setIdCardNo(hrIncomeProofDto.getIdCardNo());
        item.setWorkYearNumber(hrIncomeProofDto.getWorkYearNumber());
        item.setRequestIncomeYear(hrIncomeProofDto.getRequestIncomeYear());
        item.setWorkPosition(hrIncomeProofDto.getWorkPosition());
        item.setYearIncome(hrIncomeProofDto.getYearIncome());
        item.setMonthIncome(hrIncomeProofDto.getMonthIncome());
        item.setCompanyName(hrIncomeProofDto.getCompanyName());
        item.setCompanyAddress(hrIncomeProofDto.getCompanyAddress());
        item.setCompanyTel(hrIncomeProofDto.getCompanyTel());
        item.setCompanyLinkMan(hrIncomeProofDto.getCompanyLinkMan());
        BeanValidator.check(item);
        ModelUtil.setNotNullFields(item);
      //  actService.setVariables(item.getProcessInstanceId(),"description","员工收入证明："+item.getCreatorName());
        hrIncomeProofMapper.updateByPrimaryKey(item);

    }

    @Override
    public HrIncomeProofDto getModelById(int id) {
        return getDto(hrIncomeProofMapper.selectByPrimaryKey(id));
    }

    @Override
    public int getNewModel(int foreignKey, String userLogin) {

        HrIncomeProof item=new HrIncomeProof();

        HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);
        CommonConfig sysConfig = commonConfigMapper.selectByPrimaryKey(1);


        item.setUserLogin(userLogin);
        item.setUserName(hrEmployeeDto.getUserName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setIdCardType(hrEmployeeDto.getIdCardType());
        item.setIdCardNo(hrEmployeeDto.getIdCardNo());
        item.setWorkYearNumber(getWorkYear(hrEmployeeDto.getJoinCompanyTime()));
        item.setRequestIncomeYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        item.setWorkPosition(hrEmployeeDto.getUserType());
        //公司信息
        item.setCompanyName(sysConfig.getCompanyName());
        item.setCompanyAddress(sysConfig.getCompanyAddress());
        item.setCompanyLinkMan(sysConfig.getCompanyLinkMan());
        item.setCompanyTel(sysConfig.getCompanyTel());

        item.setDeleted(false);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setProcessEnd(false);

        ModelUtil.setNotNullFields(item);
        hrIncomeProofMapper.insert(item);
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("description", "员工收入证明:"+item.getCreatorName());
        variables.put("departmentMan", "tangzhiming");
        variables.put("financeMan", "chenling");
        String processInstanceId = actService.startProcess(EdConst.HR_INCOME_PROOF,item.getId(),variables);
        item.setProcessInstanceId(processInstanceId);
        hrIncomeProofMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    @Override
    public HrIncomeProofDto getDto(Object item) {
        HrIncomeProofDto dto=HrIncomeProofDto.adapt((HrIncomeProof) item);
        HrIncomeProof model= (HrIncomeProof) item;

        ProcessInstanceDto processInstanceDto=actService.getProcessInstanceDto(model.getProcessInstanceId());

        dto.setProcessName(processInstanceDto.getProcessName());
        dto.setBusinessKey(processInstanceDto.getBusinessKey());
        if(!model.getProcessEnd()&& StringUtils.isEmpty(processInstanceDto.getProcessName())){
            model.setProcessEnd(true);
            hrIncomeProofMapper.updateByPrimaryKey(model);
        }
        if(StringUtils.isEmpty(processInstanceDto.getProcessName())){
            dto.setProcessName("流程已结束");
        }
        return dto;
    }

    @Override
    public List<HrIncomeProofDto> listDataByForeignKey(int foreignKey) {
        return null;
    }
   //分页查找
    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrIncomeProofMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            HrIncomeProof item=(HrIncomeProof)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }


    public String getWorkYear(String time) {
        if (!time.isEmpty()) {
            String[] oldTimes = time.split("-");
            int oldYear = Integer.parseInt(oldTimes[0]);
            Calendar calendar = Calendar.getInstance();
            int nowYear = calendar.get(Calendar.YEAR);
            int worktime = nowYear - oldYear;
            if (worktime > 0) {
                return String.valueOf(worktime);
            } else {
                return "1";
            }
        }else {
            return "0";
        }

    }
}

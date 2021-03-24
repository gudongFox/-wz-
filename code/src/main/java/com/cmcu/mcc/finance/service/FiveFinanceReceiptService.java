package com.cmcu.mcc.finance.service;

import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.act.model.MyProcessInstance;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceReceiptMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceReceiptDto;
import com.cmcu.mcc.finance.entity.FiveFinanceReceipt;
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
public class FiveFinanceReceiptService {
    @Resource
    FiveFinanceReceiptMapper fiveFinanceReceiptMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
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
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    @Autowired
    ProcessQueryService processQueryService;

    public void remove(int id,String userLogin){
        FiveFinanceReceipt item = fiveFinanceReceiptMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");
        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveFinanceReceiptDto fiveFinanceReceiptDto){
        FiveFinanceReceipt model = fiveFinanceReceiptMapper.selectByPrimaryKey(fiveFinanceReceiptDto.getId());

        model.setLegalDept(fiveFinanceReceiptDto.getLegalDept());
        model.setApplyMan(fiveFinanceReceiptDto.getApplyMan());
        model.setApplyManName(fiveFinanceReceiptDto.getApplyManName());
        model.setApplyManTel(fiveFinanceReceiptDto.getApplyManTel());
        model.setApplyTime(fiveFinanceReceiptDto.getApplyTime());
        model.setContractName(fiveFinanceReceiptDto.getContractName());
        model.setContractNo(fiveFinanceReceiptDto.getContractNo());
        model.setCustomerName(fiveFinanceReceiptDto.getCustomerName());
        model.setMoneyType(fiveFinanceReceiptDto.getMoneyType());
        model.setReceiptMoney(fiveFinanceReceiptDto.getReceiptMoney());
        model.setReceiptTime(fiveFinanceReceiptDto.getReceiptTime());
        model.setReceiptNo(fiveFinanceReceiptDto.getReceiptNo());
        model.setReceiptRemark(fiveFinanceReceiptDto.getReceiptRemark());

        model.setReceiveTime(fiveFinanceReceiptDto.getReceiveTime());

        model.setDeptId(fiveFinanceReceiptDto.getDeptId());
        model.setDeptName(fiveFinanceReceiptDto.getDeptName());
        model.setRemark(fiveFinanceReceiptDto.getRemark());
        model.setGmtModified(new Date());
        fiveFinanceReceiptMapper.updateByPrimaryKey(model);

        Map variables = Maps.newHashMap();
/*        variables.put("train", fiveFinanceReceiptDto.getReceipt().equalsIgnoreCase("是"));
        variables.put("scientific",false);
        variables.put("other",fiveFinanceReceiptDto.getReceipt().equalsIgnoreCase("否"));
        variables.put("money", fiveFinanceReceiptDto.getCount());*/

        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveFinanceReceiptDto getModelById(int id){

        return getDto(fiveFinanceReceiptMapper.selectByPrimaryKey(id));
    }

    public FiveFinanceReceiptDto getDto(FiveFinanceReceipt item) {
        FiveFinanceReceiptDto dto=FiveFinanceReceiptDto.adapt(item);
        dto.setProcessName("已完成");
       // MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(dto.getProcessInstanceId
                (), "", "");
        dto.setProcessName(customProcessInstance.getCurrentTaskName());
            if(customProcessInstance.isFinished()){
                dto.setProcessName("已完成");
            }

        if(!item.getProcessEnd()&&customProcessInstance.isFinished()){
            dto.setProcessEnd(true);
            fiveFinanceReceiptMapper.updateByPrimaryKey(dto);
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        FiveFinanceReceipt item=new FiveFinanceReceipt();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setApplyMan(hrEmployeeDto.getUserLogin());
        item.setApplyManName(hrEmployeeDto.getUserName());
        item.setApplyManTel(hrEmployeeDto.getMobile());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        item.setApplyTime(sdf.format(new Date()));
        item.setReceiptTime(sdf.format(new Date()));
        item.setReceiveTime(sdf.format(new Date()));

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());

        ModelUtil.setNotNullFields(item);
        fiveFinanceReceiptMapper.insert(item);

        String businessKey= EdConst.FIVE_FINANCE_RECEIPT+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(hrEmployeeDto.getDeptId()));
        variables.put("processDescription", "收据管理："+item.getCreatorName());

        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_FINANCE_RECEIPT,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        fiveFinanceReceiptMapper.updateByPrimaryKey(item);
        return item.getId();
    }
    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
         List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceReceiptMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceReceipt item=(FiveFinanceReceipt)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }



    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveFinanceReceipt item = fiveFinanceReceiptMapper.selectByPrimaryKey(id);
        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("materialPurchaseId",item.getId());
        map.put("deleted",false);

        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }
}

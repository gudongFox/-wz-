package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.FiveBusinessCooperationContractDto;
import com.cmcu.mcc.business.entity.FiveBusinessContractLibrary;
import com.cmcu.mcc.business.entity.FiveBusinessCooperationContract;
import com.cmcu.mcc.business.entity.FiveBusinessCooperationDelegation;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.finance.dao.FiveFinanceIncomeConfirmMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveBusinessCooperationContractService  extends BaseService {

    @Resource
    FiveBusinessCooperationContractMapper fiveBusinessCooperationContractMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Resource
    HandleFormService handleFormService;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    BusinessIncomeService businessIncomeService;
    @Autowired
    BusinessIncomeMapper businessIncomeMapper;
    @Autowired
    FiveFinanceIncomeConfirmMapper fiveFinanceIncomeConfirmMapper;
    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    FiveBusinessCooperationDelegationMapper fiveBusinessCooperationDelegationMapper;
    @Resource
    CommonFileService commonFileService;
    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id, String userLogin) {
        FiveBusinessCooperationContract item = fiveBusinessCooperationContractMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(FiveBusinessCooperationContractDto dto) {
        FiveBusinessCooperationContract item = fiveBusinessCooperationContractMapper.selectByPrimaryKey(dto.getId());
        item.setCooperationDelegationId(dto.getCooperationDelegationId());

        item.setCooperationProjectName(dto.getCooperationProjectName());
        item.setCooperationProjectNo(dto.getCooperationProjectNo());
        item.setCooperationProjectType(dto.getCooperationProjectType());
        item.setDelegationDeptId(dto.getDelegationDeptId());
        item.setDelegationDeptName(dto.getDelegationDeptName());
        item.setCooperationDeptName(dto.getCooperationDeptName());
        item.setCooperationDeptId(dto.getCooperationDeptId());
        item.setContractName(dto.getContractName());
        item.setContractType(dto.getContractType());
        item.setContractNo(dto.getContractNo());
        item.setChargeMoney(MyStringUtil.moneyToString(dto.getChargeMoney(),8));
        item.setChargeMoneyTime(dto.getChargeMoneyTime());
        item.setCooperationMoney(MyStringUtil.moneyToString(dto.getCooperationMoney(),8));
        item.setCooperationContent(dto.getCooperationContent());
        item.setCooperationMoneyClose(dto.getCooperationMoneyClose());
        item.setSignTime(dto.getSignTime());
        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());
        item.setTitle(dto.getTitle());
        BeanValidator.check(item);
        fiveBusinessCooperationContractMapper.updateByPrimaryKey(item);

        Map variables=Maps.newHashMap();
        variables.put("processDescription",item.getTitle());
        if(item.getDelegationDeptId()!=0){
            variables.put("delegationDeptChargeMen",selectEmployeeService.getDeptChargeMen(item.getDelegationDeptId()));//????????????????????????
            variables.put("delegationDeptLeader",selectEmployeeService.getOtherDeptChargeMan(item.getDelegationDeptId()));//?????????????????????
        }
        if(item.getCooperationDeptId()!=0){
            variables.put("cooperationDeptChargeMen",selectEmployeeService.getDeptChargeMen(item.getCooperationDeptId()));//????????????????????????
            variables.put("cooperationDeptLeader",selectEmployeeService.getOtherDeptChargeMan(item.getCooperationDeptId()));//?????????????????????
        }
        variables.put("businessChargeMen",selectEmployeeService.getDeptChargeMen(48));//???????????????
        taskHandleService.setVariables(item.getProcessInstanceId(),variables);
    }

    public FiveBusinessCooperationContractDto getModelById(int id) {
        return getDto(fiveBusinessCooperationContractMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin,String uiSref) {
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        FiveBusinessCooperationContract item = new FiveBusinessCooperationContract();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        item.setContractType(commonCodeService.listDataByCatalog(MccConst.APP_CODE,"????????????").get(0).getName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setTitle("???????????????????????????????????????:"+item.getCreatorName());
        ModelUtil.setNotNullFields(item);
        fiveBusinessCooperationContractMapper.insert(item);

        String processKey=EdConst.FIVE_BUSINESS_COOPERATION_CONTRACT;
        String businessKey=processKey+"_"+item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", item.getTitle());

        String processInstanceId = taskHandleService.startProcess(processKey, businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveBusinessCooperationContractMapper.updateByPrimaryKey(item);

        return item.getId();
    }


    public FiveBusinessCooperationContractDto getDto(Object item) {
        FiveBusinessCooperationContract model= (FiveBusinessCooperationContract) item;
        FiveBusinessCooperationContractDto dto=FiveBusinessCooperationContractDto.adapt(model);
        dto.setProcessName("?????????");
        if(!dto.getProcessEnd() && StringUtils.isNotEmpty(dto.getProcessInstanceId())){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveBusinessCooperationContractMapper.updateByPrimaryKey(dto);
                //???????????????
                insertContractLibrary(dto.getId());
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        dto.setChargeMoney(MyStringUtil.moneyToString(dto.getChargeMoney(),6));
        dto.setCooperationMoney(MyStringUtil.moneyToString(dto.getCooperationMoney(),6));
        return dto;
    }


    public List<FiveBusinessCooperationContract> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
        if (deptIdList.size()==0){
            params.put("creator",userLogin);
        }else {
            params.put("deptIdList",deptIdList);
        }
        return  fiveBusinessCooperationContractMapper.selectAll(params);
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessCooperationContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessCooperationContract item=(FiveBusinessCooperationContract)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<Map> downData(String userLogin,int outAssistId) {
        List<Map> list = Lists.newArrayList();

        String  sql ="";


        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return list;
    }
    //?????????????????????
    public void getInteriorContractNo(int id) {
        try {

            FiveBusinessCooperationContractDto cooperationContractDto = getModelById(id);
            String newContractNo = "H";
            //???????????? 2019???  19
            String signYear = cooperationContractDto.getSignTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode = hrDeptService.getModelById(cooperationContractDto.getCooperationDeptId()).getDeptCode();
            //??????????????????
            String typeCode = "N";

            //?????? 001
            Map<String, Object> params = Maps.newHashMap();
            params.put("deleted", false);
            params.put("contractNoHead", "H" + year + deptCode + typeCode);
            List<FiveBusinessCooperationContract> cooperationContracts = fiveBusinessCooperationContractMapper.selectAll(params);
            int size = 0;
            //???????????????????????????
            if (!cooperationContracts.isEmpty()) {
                for (FiveBusinessCooperationContract cooperationContract : cooperationContracts) {
                    if (!cooperationContract.getId().equals(id) && StringUtils.isNotEmpty(cooperationContract.getContractNo())) {
                        String contractNo = cooperationContract.getContractNo();
                        //???????????? H  ?????? H
                        if (contractNo.startsWith("H")) {
                            contractNo = contractNo.substring(1);
                        }
                        String maxSize = contractNo.substring(5);
                        if (size < Integer.parseInt(maxSize)) {
                            size = Integer.parseInt(maxSize);
                        }
                    }
                }
            }
            size = size + 1;
            String format = String.format("%03d", size);
            newContractNo = newContractNo + year + deptCode + typeCode + format;

            cooperationContractDto.setContractNo(newContractNo);
            update(cooperationContractDto);
        }catch (Exception e){
            Assert.state(false,"???????????????????????????!");
        }

    }

    //????????? ?????????
    public String insertContractLibrary(int cooperationContractId) {
        FiveBusinessCooperationContract cooperationContract = fiveBusinessCooperationContractMapper.selectByPrimaryKey(cooperationContractId);
        FiveBusinessCooperationDelegation cooperationDelegation = fiveBusinessCooperationDelegationMapper.selectByPrimaryKey(cooperationContract.getCooperationDelegationId());
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("cooperationContractId",cooperationContractId);
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()>0){
            return "????????? "+cooperationContract.getContractName()+" ??????????????????";
        }else{
                FiveBusinessContractLibrary library = new FiveBusinessContractLibrary();

                library.setCooperationContractId(cooperationContract.getId());
                //????????????
                library.setProjectName(cooperationContract.getCooperationProjectName());
                //??????????????????
                library.setRecordProjectName(cooperationDelegation.getProjectName());
                //?????????
                library.setProjectNo(cooperationDelegation.getInteriorProjectNo());
                //????????????
                library.setContractName(cooperationContract.getContractName());
                //?????????
                library.setContractNo(cooperationContract.getContractNo());
                //???????????? ??????
                library.setDeptId(cooperationContract.getDeptId());
                library.setDeptName(cooperationContract.getDeptName());
                //????????????
                library.setContractMoney(MyStringUtil.moneyToString(cooperationContract.getChargeMoney(),8));
                //????????????
                library.setSignTime(cooperationContract.getSignTime());
                //????????????
                library.setContractType(cooperationContract.getContractType());

                library.setCreator(cooperationContract.getCreator());
                library.setCreatorName(cooperationContract.getCreatorName());

                library.setGmtCreate(new Date());
                library.setGmtModified(new Date());

                ModelUtil.setNotNullFields(library);
                fiveBusinessContractLibraryMapper.insert(library);
                library.setBusinessKey(EdConst.FIVE_BUSINESSCONTRACTLIBRARY+"_"+library.getId());
                fiveBusinessContractLibraryMapper.updateByPrimaryKey(library);

                //?????? ????????????
                cooperationContract.setContractLibraryId(library.getId());
                fiveBusinessCooperationContractMapper.updateByPrimaryKey(cooperationContract);
                //?????????
                commonFileService.copyFileByBusinessKey(cooperationContract.getBusinessKey(),library.getBusinessKey(),0);
                return "????????? "+cooperationContract.getContractName()+"  ?????????????????????";
        }
    }


}

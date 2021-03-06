package com.cmcu.mcc.business.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.*;
import com.cmcu.mcc.business.dto.BusinessContractDto;
import com.cmcu.mcc.business.dto.BusinessInputContractDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.ed.service.EdProjectTreeService;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
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

import java.util.*;

@Service
public class BusinessInputContractService extends BaseService {
    @Resource
    BusinessInputContractMapper businessInputContractMapper;

    @Resource
    CommonCodeService commonCodeService;


    @Autowired
    MyActService myActService;
    @Autowired
    MyHistoryService myHistoryService;
    @Autowired
    HrEmployeeSysService hrEmployeeSysService;
    @Autowired
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    SelectEmployeeService selectEmployeeService;
    @Autowired
    EdProjectTreeService edProjectTreeService;
    @Autowired
    BusinessContractService businessContractService;
    @Autowired
    BusinessPreContractMapper businessPreContractMapper;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Resource
    HandleFormService handleFormService;

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
//???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> businessInputContractMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            BusinessInputContract item = (BusinessInputContract) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void remove(int id, String userLogin) {
        BusinessInputContract item = businessInputContractMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "????????????????????????!");

        Map preParams =new HashMap();
        preParams.put("inputContractId",id);
        List<BusinessPreContract> businessPreContracts = businessPreContractMapper.selectAll(preParams);
        //??????????????? ?????????????????????
        if(businessPreContracts.size()!=0) {
            BusinessPreContract preContract = businessPreContracts.get(0);
            preContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(preContract);
        }
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }

    public void update(BusinessInputContractDto model) {
        BusinessInputContract item =  businessInputContractMapper.selectByPrimaryKey(model.getId());


        //????????????????????? ???????????????????????????????????????
        if(item.getPreId()!= 0){
            BusinessPreContract oldPreContract = businessPreContractMapper.selectByPrimaryKey(item.getPreId());
            oldPreContract.setReviewContractId(0);
            businessPreContractMapper.updateByPrimaryKey(oldPreContract);
        }
        //????????????????????? ??????
        BusinessPreContract preContract = businessPreContractMapper.selectByPrimaryKey(model.getPreId());
        preContract.setReviewContractId(item.getId());
        businessPreContractMapper.updateByPrimaryKey(preContract);

        item.setRecordId(model.getRecordId());
        item.setPreId(model.getPreId());
        item.setProjectName(model.getProjectName());
        item.setContractNo(model.getContractNo());
        item.setProjectNo(model.getProjectNo());
        item.setDeptId(model.getDeptId());
        item.setDeptName(model.getDeptName());
        item.setContractMark(model.getContractMark());
        item.setContractSchedule(model.getContractSchedule());
        item.setContractDes(model.getContractDes());
        item.setContractMoney(MyStringUtil.moneyToString(model.getContractMoney()));
        item.setInOrOut(model.getInOrOut());
        item.setSignTime(model.getSignTime());
        item.setChargeMen(model.getChargeMen());
        item.setChargeMenName(model.getChargeMenName());
        item.setEnterTime(model.getEnterTime());
        item.setQuarter(model.getQuarter());
        item.setQuarter(model.getQuarter());
        item.setBankAccount(model.getBankAccount());
        item.setPostalAddress(model.getPostalAddress());
        item.setPostalCode(model.getPostalCode());
        item.setLinkPhone(model.getLinkPhone());
        item.setContractType(model.getContractType());
        item.setCustomerId(model.getCustomerId());
        item.setCustomerName(model.getCustomerName());
        item.setAgency(model.getAgency());
        item.setProjectNature(model.getProjectNature());
        item.setIndustryType(model.getIndustryType());
        item.setMilitaryMark(model.getMilitaryMark());
        item.setForeignTradeMark(model.getForeignTradeMark());
        item.setCivilMark(model.getCivilMark());
        item.setProjectMajorTypeFirst(model.getProjectMajorTypeFirst());
        item.setProjectMajorTypeSecond(model.getProjectMajorTypeSecond());
        item.setProjectMajorTypeThird(model.getProjectMajorTypeThird());
        item.setRemark(model.getRemark());
        item.setItemNum(model.getItemNum());
        item.setDepositBank(model.getDepositBank());
        item.setGmtModified(new Date());
        item.setEd(model.getEd());
        businessInputContractMapper.updateByPrimaryKey(item);
        //  BeanValidator.check(item);
        Map variables = Maps.newHashMap();
        myActService.setVariables(model.getProcessInstanceId(),variables);

    }

    public BusinessInputContractDto getModelById(int id) {
        return getDto(businessInputContractMapper.selectByPrimaryKey(id));
    }

    private BusinessInputContractDto getDto(BusinessInputContract item) {
        BusinessInputContractDto dto = BusinessInputContractDto.adapt(item);
       // MyProcessInstance processInstanceDto = myHistoryService.getMyProcessInstance(item.getProcessInstanceId(), "");
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if (customProcessInstance==null || customProcessInstance.isFinished()) {
                dto.setProcessEnd(true);
                businessInputContractMapper.updateByPrimaryKey(dto);
            }else {
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        return dto;
    }

    public int getNewModel(String userLogin,String uiSref) {
        List<Integer> deptIdList = hrEmployeeSysService.getMyDeptList(userLogin, uiSref);

        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        BusinessInputContract item = new BusinessInputContract();

        item.setContractMoney(MyStringUtil.moneyToString(""));
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setContractSchedule(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setInOrOut(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"?????????").toString());
        item.setQuarter(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????").toString());
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectNature(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setIndustryType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"??????????????????").toString());
        item.setMilitaryMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setCivilMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setForeignTradeMark(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        item.setProjectMajorTypeFirst(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeSecond(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setProjectMajorTypeThird(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????????????????").toString());
        item.setEd(true);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        Map<String, Object> defaultDept = hrDeptService.getDefaultDept(hrEmployeeDto.getDeptId());
        item.setDeptId(Integer.parseInt(defaultDept.get("deptId").toString()));
        item.setDeptName(defaultDept.get("deptName").toString());

        ModelUtil.setNotNullFields(item);
        businessInputContractMapper.insert(item);

        Map variables = Maps.newHashMap();
        List<String> businessMen = selectEmployeeService.getBusinessMenByDeptId(item.getDeptId());
        if(businessMen.size()!=0){
            variables.put("businessMen",selectEmployeeService.getBusinessMenByDeptId(item.getDeptId()));//??????????????????
            variables.put("businessMenFlag",true);
        }else{
            variables.put("businessMenFlag",false);
        }
        variables.put("userLogin", userLogin);
        variables.put("financeMan", selectEmployeeService.getFinanceChargeMan());
        variables.put("processDescription", "????????????");

        String processKey= EdConst.FIVE_BUSINESS_INPUT_CONTRACT;
        String businessKey = processKey+"_"+item.getId();
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(processKey, item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        businessInputContractMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    /**
     * ?????????????????? ??????????????????
     * @param contractId
     * @return
     */
    public BusinessInputContractDto getModelByContractId(int contractId){
        Map<String, Object> params=Maps.newHashMap();
        params.put("deleted", false);
        params.put("contractId",contractId);
        List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
        if (businessInputContracts.size()>0){
            return getDto(businessInputContracts.get(0));
        }else {
            return null;
        }



    }

    /**
     * XX ??????????????????2017?????????17???2018?????????18??????????????????
     * XX ????????????????????????
     * X ?????????????????? ???????????? 1 ;????????????2;	???????????????????????????????????????????????????????????????	3;	??????????????????????????????????????????4 ; ??????????????????????????????5;  ?????????	6;
     * XXX  ?????????????????????????????????????????????001??????
     * @param id
     */

    public String getContractNo(int id){
        try {
            BusinessInputContractDto businessInputContractDto = getModelById(id);
            String newContractNo="H";
            //???????????? 2019???  19
            String signYear = businessInputContractDto.getSignTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode= hrDeptService.getModelById(businessInputContractDto.getDeptId()).getDeptCode();
            //??????????????????
            List<CommonCodeDto> sysCodeDtoList = commonCodeService.listDataByCatalog("","????????????");
            String typeCode="";
            for ( CommonCode dto: sysCodeDtoList) {
                if (dto.getName().equals(businessInputContractDto.getContractType())){
                    typeCode=dto.getCode();
                }
            }
            //??????  ?????? ?????? ???????????? ???????????? ??????????????????
            if (businessInputContractDto.getContractNo().contains("H"+year+deptCode+typeCode)){
                return businessInputContractDto.getContractNo();
            }

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("contractNoHead","H"+year+deptCode+typeCode);
            List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
            int size=1;
            //???????????????????????????
            if (!businessInputContracts.isEmpty()){
                for (BusinessInputContract inputContract:businessInputContracts){
                    String maxSize=  inputContract.getContractNo().substring(6);
                    if (size<Integer.parseInt(maxSize)){
                        size = Integer.parseInt(maxSize);
                    }
                }
                size=size+1;
            }
            String format = String.format("%03d", size);
            newContractNo=newContractNo+year+deptCode+typeCode+format;

            businessInputContractDto.setContractNo(newContractNo);
            update(businessInputContractDto);

            return newContractNo;

        }catch (Exception e){
            Assert.state(false, "???????????????????????????????????????????????????????????????");
            return "";
        }


    }

    /**
     * XX ??????????????????2017?????????17???2018?????????18??????????????????
     * XX ????????????????????????
     * X ?????????????????? ???????????? S ???????????? Z ???????????? C ?????? K ?????? J ????????? W
     * XXX  ?????????????????????????????????????????????001??????
     * @param id
     */

    public String getProjectNo(int id){
        try {
            BusinessInputContractDto businessInputContractDto = getModelById(id);
            BusinessContractDto businessContractDto = businessContractService.getModelById(Integer.valueOf(businessInputContractDto.getContractId()));
            String newProjectNo = "";
            //???????????? 2019???  19
            String signYear = businessInputContractDto.getSignTime().split("-")[0];
            String year = signYear.substring(2);
            //?????????????????? 2??? 01
            String deptCode= hrDeptService.getModelById(businessInputContractDto.getDeptId()).getDeptCode();
            //??????????????????
            String typeCode="";
            /*List<SysCodeDto> sysCodeDtoList = sysCodeMapper.listDataByCatalog("??????????????????");
            for ( SysCodeDto dto: sysCodeDtoList) {
                if (dto.getName().equals(businessContractDto.getProjectType())){
                    typeCode=dto.getCode();
                }
            }*/
            if(businessContractDto.getProjectType().equals("????????????")){
                typeCode="S";
            }else if(businessContractDto.getProjectType().equals("????????????")){
                typeCode="Z";
            }else if(businessContractDto.getProjectType().equals("????????????")){
                typeCode="C";
            } else if(businessContractDto.getProjectType().equals("??????")){
                typeCode="K";
            } else if(businessContractDto.getProjectType().equals("??????")){
                typeCode="J";
            } else if(businessContractDto.getProjectType().equals("?????????")){
                typeCode="W";
            }
            //??????  ?????? ?????? ???????????? ???????????? ??????????????????
            if (businessInputContractDto.getProjectNo().contains(year+deptCode+typeCode)){
                return businessInputContractDto.getProjectNo();
            }

            //?????? 001
            Map<String, Object> params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("projectNoHead",year+deptCode+typeCode);
            List<BusinessInputContract> businessInputContracts = businessInputContractMapper.selectAll(params);
            int size=1;
            //???????????????????????????
            if (!businessInputContracts.isEmpty()){
                for (BusinessInputContract inputContract:businessInputContracts){
                    String maxSize=  inputContract.getProjectNo().substring(5);
                    if (size<Integer.parseInt(maxSize)){
                        size = Integer.parseInt(maxSize);
                    }
                }
                size=size+1;
            }
            String format = String.format("%03d", size);
            newProjectNo=newProjectNo+year+deptCode+typeCode+format;

            businessInputContractDto.setProjectNo(newProjectNo);
            update(businessInputContractDto);

            return newProjectNo;

        }catch (Exception e){
            Assert.state(false, "???????????????????????????????????????????????????????????????");
            return "";
        }


    }
    //??????????????? ????????? ??????????????????
    public String insertContractLibrary(int inputContractId) {
        BusinessInputContract input =businessInputContractMapper.selectByPrimaryKey(inputContractId);
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("contractPreId", input.getPreId());
        List<FiveBusinessContractLibrary> librarys = fiveBusinessContractLibraryMapper.selectAll(map);
        if(librarys.size()==0){
            return "?????????????????????"+input.getProjectName()+"???????????????";
        }
        FiveBusinessContractLibrary library =librarys.get(0);

        library.setContractMoney(MyStringUtil.moneyToString(input.getContractMoney()));
        library.setContractNo(input.getContractNo());

        return "????????? "+input.getProjectName()+"  ???????????????????????????";

    }


}

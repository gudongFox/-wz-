package com.cmcu.mcc.business.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.BusinessSubpackageMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.business.dto.FiveBusinessContractLibrarySubpackageDto;
import com.cmcu.mcc.business.entity.*;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.dto.HrEmployeeSysDto;
import com.cmcu.mcc.hr.entity.HrDept;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveBusinessContractLibrarySubpackageService extends BaseService {
    @Resource
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
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
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    BusinessSubpackageMapper  businessSubpackageMapper;
    @Autowired
    HrDeptService hrDeptService ;
    @Autowired
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveBusinessContractLibrarySubpackage model = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(id);
        Assert.state(model.getCreator().equals(userLogin),"??????????????????"+model.getCreatorName()+"??????");
        handleFormService.removeProcessInstance(model.getBusinessKey(),userLogin);

    }

    public void update(FiveBusinessContractLibrarySubpackageDto dto){
        FiveBusinessContractLibrarySubpackage item = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(dto.getId());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());

        item.setContractLibraryId(dto.getContractLibraryId());
        item.setContractNo(dto.getContractNo());
        item.setContractName(dto.getContractName());
        item.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney()));
        item.setContractType(dto.getContractType());
        item.setProjectNature(dto.getProjectNature());
        item.setSubContractType(dto.getSubContractType());
        item.setSubContractName(dto.getSubContractName());
        item.setSubContractMoney(MyStringUtil.moneyToString(dto.getSubContractMoney()));
        item.setSubContractNo(dto.getSubContractNo());
        item.setDeptId(dto.getDeptId());
        item.setDeptName(dto.getDeptName());
        item.setSubContractDesc(dto.getSubContractDesc());
        item.setCashDeposit(dto.getCashDeposit());
        item.setCashDepositMoney(MyStringUtil.moneyToString(dto.getCashDepositMoney()));
        item.setBackletter(dto.getBackletter());
        item.setBackletterMoney(MyStringUtil.moneyToString(dto.getBackletterMoney()));
        item.setSign(dto.getSign());
        item.setContractStatus(dto.getContractStatus());
        item.setSupplierId(dto.getSupplierId());
        item.setSupplierName(dto.getSupplierName());
        item.setSupplierCreditCode(dto.getSupplierCreditCode());
        item.setSupplierLinkMan(dto.getSupplierLinkMan());
        item.setSupplierLinkTel(dto.getSupplierLinkTel());
        item.setDepositBank(dto.getDepositBank());
        item.setBankAccount(dto.getBankAccount());
        item.setInCompany(dto.getInCompany());
        item.setInDeptId(dto.getInDeptId());
        item.setInDeptName(dto.getInDeptName());
        item.setRemark(dto.getRemark());

        item.setPurchase(dto.getPurchase());
        item.setReviewLevel(dto.getReviewLevel());

        item.setDeptChargeMen(dto.getDeptChargeMen());
        item.setDeptChargeMenName(dto.getDeptChargeMenName());
        BeanValidator.check(item);
        fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(item);
    }

    public FiveBusinessContractLibrarySubpackageDto getModelById(int id){
        return getDto(fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(id));
    }

    public FiveBusinessContractLibrarySubpackage getModelBySubpackageId(int subpackageId){
        Map map = new HashMap();
        map.put("deleted",false);
        map.put("subpackageId",subpackageId);
        List<FiveBusinessContractLibrarySubpackage> librarySubpackages = fiveBusinessContractLibrarySubpackageMapper.selectAll(map);
        if(librarySubpackages.size()>0){
            return librarySubpackages.get(0);
        }else {
            return null;
        }

    }

    public FiveBusinessContractLibrarySubpackageDto getDto(FiveBusinessContractLibrarySubpackage item) {
        FiveBusinessContractLibrarySubpackageDto dto=FiveBusinessContractLibrarySubpackageDto.adapt(item);
        //????????????
        if(dto.getSubpackageId()!=0){
            BusinessSubpackage subpackage = businessSubpackageMapper.selectByPrimaryKey(dto.getSubpackageId());
            dto.setBusinessSubpackage(subpackage);
        }
        //????????????
        if(dto.getReviewIds()!=""){
            List<BusinessSubpackage> reviews = new ArrayList<>();
            List<Integer> reviewIds = MyStringUtil.getIntList(dto.getReviewIds());
            for(Integer reviewId:reviewIds){
                reviews.add(businessSubpackageMapper.selectByPrimaryKey(reviewId));
            }
            dto.setReviews(reviews);
        }
        //????????????6???
        dto.setSubContractMoney(MyStringUtil.moneyToString(dto.getSubContractMoney(),6));
        dto.setContractMoney(MyStringUtil.moneyToString(dto.getContractMoney(),6));
        return dto;
    }

    public int getNewModel(String userLogin,String uiSref){

        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        FiveBusinessContractLibrarySubpackage item=new FiveBusinessContractLibrarySubpackage();
        item.setSupplierId(0);
        HrEmployeeSysDto hrEmployeeDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getHrEmployee().getUserName());
        item.setCashDeposit(false);
        item.setBackletter(false);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setInCompany(false);
        item.setSign(false);
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());
        //item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());
        //item.setSubContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"????????????").toString());

        item.setPurchase(true);
        item.setInsertType(1);

        item.setDeleted(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);
        fiveBusinessContractLibrarySubpackageMapper.insert(item);
        String businessKey= EdConst.FIVE_BUSINESSCONTRACTLIBRARYSUBPACKAGE+ "_" + item.getId();
        item.setBusinessKey(businessKey);
        fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(String uiSref,Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
//???????????????????????????????????????  myDeptData true?????????????????? false???????????????
/*        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));*/
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //??????????????????deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            params.put("deptIdList",deptIdList);
        }

        params.put("isLikeSelect",true);
       /* if(params.getOrDefault("isPurchase","").equals("true")){
            params.put("purchase",true);
        }else{
            params.put("purchase",false);
        }*/
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractLibrarySubpackageMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractLibrarySubpackage item=(FiveBusinessContractLibrarySubpackage) p;
            FiveBusinessContractLibrarySubpackageDto dto = getDto(item);
            list.add(dto);
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    //?????????????????? ??????????????? ???????????????
    public PageInfo<Object> listPagedDataSelect(String uiSref,Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        String userLogin = params.get("userLogin").toString();
        HrEmployeeSysDto hrEmployeeSysDto = hrEmployeeSysService.getModelByUserLogin(userLogin);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        if (deptIdList.size()==0){
            //??????????????????deptIds
            int headDeptId = selectEmployeeService.getHeadDeptId(hrEmployeeSysDto.getDeptId());
            List<Integer> deptIds = hrDeptService.listChild(headDeptId).stream().map(HrDept::getId).collect(Collectors.toList());
            deptIds.add(headDeptId);
            params.put("deptIdList",deptIds);
        }else {
            params.put("deptIdList",deptIdList);
        }

        params.put("isLikeSelect",true);
        if(params.getOrDefault("isPurchase","").equals("true")){
            params.put("purchase",true);
        }else{
            params.put("purchase",false);
        }
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveBusinessContractLibrarySubpackageMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveBusinessContractLibrarySubpackage item=(FiveBusinessContractLibrarySubpackage) p;
            FiveBusinessContractLibrarySubpackageDto dto = getDto(item);
            list.add(dto);
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<FiveBusinessContractLibrarySubpackage> listSubpackageByUserLogin(String userLogin, int subpackageId){
        List<FiveBusinessContractLibrarySubpackageDto> dtos = new ArrayList<>();
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,"five.businessContractLibrarySubpackage");
        params.put("deptIdList",deptIdList);

        List<FiveBusinessContractLibrarySubpackage> subpackages = fiveBusinessContractLibrarySubpackageMapper.selectAll(params)
                .stream().filter(p ->p.getInCompany()).filter(p ->p.getRecordId()==0).collect(Collectors.toList());
        //?????????????????? ??????
        if(subpackageId!=0){
            FiveBusinessContractLibrarySubpackage subpackage = fiveBusinessContractLibrarySubpackageMapper.selectByPrimaryKey(subpackageId);
            subpackages.add(subpackage);
        }
        return subpackages;
    }

    public List<Map> listMapData() {
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map map = new LinkedHashMap();
            map.put("???????????????????????? ??????/?????????","");
            map.put(" ??????????????? ", "");
            map.put(" ???????????? ", "");
            map.put(" ??????????????????????????? ", "");
            map.put(" ??????????????? ", "");

            map.put("       ??????/??????????????????       ","");
            map.put("  ??????/???????????????  ", "");
            map.put(" ??????/???????????????(??????) ", "");
            map.put(" ???????????? ", "");
            map.put(" ??????/?????????????????? ", "");

            map.put(" ???????????? ", "");
            map.put(" ???????????????  ", "");
            map.put(" ?????????????????????  ", "");
            map.put(" ?????? ", "");
            list.add(map);
        }
        return list;
    }

    public String insertBatch(InputStream inputStream, String userLogin) throws IOException {
        HrEmployeeDto dto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        int updateNum = 0;
        int insertNum = 0;
        String contractNoA = "";

        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            FiveBusinessContractLibrarySubpackage item = new FiveBusinessContractLibrarySubpackage();
            //????????????
            String purchase = map.getOrDefault(1,"??????").toString();
            if(purchase.equalsIgnoreCase("??????")){
                item.setPurchase(true);
            }else{
                item.setPurchase(false);
            }
            //???????????????
            String subContractNo = map.getOrDefault(7,"").toString();
            if(Strings.isNullOrEmpty(subContractNo)){
                Assert.state(false,"???"+(i+1)+"????????? "+purchase+"???????????????");
            }
            item.setSubContractNo(subContractNo);

            //??????????????????
            Map map1 = new HashMap();
            map1.put("deleted", false);
            map1.put("subContractNo", item.getSubContractNo());
            Boolean flag = true;
            if (fiveBusinessContractLibrarySubpackageMapper.selectAll(map1).size() > 0) {
                item = fiveBusinessContractLibrarySubpackageMapper.selectAll(map1).get(0);
                flag = false;
            }


            //???????????????
            String contractName = map.getOrDefault(2,"").toString();
            item.setContractName(contractName);
            //????????????
            String contractNo = map.getOrDefault(3,"").toString();
            item.setContractNo(contractNo);
            //???????????????
            String contractMoney = map.getOrDefault(4,"").toString();
            item.setContractMoney(MyStringUtil.moneyToString(contractMoney,8));
            //???????????????
            String contractType = map.getOrDefault(5,"").toString();
            item.setContractType(contractType);
            //????????????
            String subContractName = map.getOrDefault(6,"").toString();
            item.setSubContractName(subContractName);

            //?????????
            String subContractMoney = map.getOrDefault(8,"").toString();
            item.setSubContractMoney(MyStringUtil.moneyToString(subContractMoney,8));

            //????????????
            String deptName = map.getOrDefault(9,"").toString();
            HrDept hrDept = hrDeptService.selectByName(deptName);
            if (hrDept != null) {
                item.setDeptName(hrDept.getName());
                item.setDeptId(hrDept.getId());
            }else if(deptName.equals("????????????")) {
                item.setDeptName("????????????????????????????????????");
                item.setDeptId(1);
            }else if(deptName.contains("217")){
                item.setDeptName("??????217");
                item.setDeptId(0);
            } else {
                Assert.state(false, "???????????????"+deptName + " ???????????????????????????????????????" + item.getContractNo());
            }

            //??????????????????
            String subContractType = map.getOrDefault(10,"").toString();
            item.setSubContractType(subContractType);
            //????????????
            String supplierName = map.getOrDefault(11,"").toString();
            item.setSupplierName(supplierName);
            //???????????????
            String linkMan = map.getOrDefault(12,"").toString();
            item.setSupplierLinkMan(linkMan);
            //??????????????????
            String linkTel = map.getOrDefault(13,"").toString();
            item.setSupplierLinkTel(linkTel);
            //??????
            String remark = map.getOrDefault(14,"").toString();
            item.setRemark(remark);



            HrEmployeeDto hrEmployeeDto = hrEmployeeService.getModelByUserLogin(userLogin);

            item.setCreator(hrEmployeeDto.getUserLogin());
            item.setCreatorName(hrEmployeeDto.getUserName());
            //????????????  1?????? 2??????  0 ??????
            item.setInsertType(2);

            item.setReviewLevel("??????");
            item.setDeleted(false);
            item.setBackletter(false);//????????????
            item.setGmtModified(new Date());
            item.setGmtCreate(new Date());
            item.setRemark("????????????");
            ModelUtil.setNotNullFields(item);
            if (flag) {
                fiveBusinessContractLibrarySubpackageMapper.insert(item);
                String businessKey = EdConst.FIVE_BUSINESSCONTRACTLIBRARYSUBPACKAGE + "_" + item.getId();
                item.setBusinessKey(businessKey);
                fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(item);
                insertNum++;
            } else {
                fiveBusinessContractLibrarySubpackageMapper.updateByPrimaryKey(item);
                updateNum++;
            }

        }
        return  insertNum + "," + updateNum;
    }
}

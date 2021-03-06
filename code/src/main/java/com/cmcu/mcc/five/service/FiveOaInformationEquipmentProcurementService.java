package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.dto.ActHistoryDto;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentProcurementDetailMapper;
import com.cmcu.mcc.five.dao.FiveOaInformationEquipmentProcurementMapper;
import com.cmcu.mcc.five.dto.FiveOaInformationEquipmentProcurementDto;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurement;
import com.cmcu.mcc.five.entity.FiveOaInformationEquipmentProcurementDetail;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.ActService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaInformationEquipmentProcurementService  extends BaseService {
    @Resource
    FiveOaInformationEquipmentProcurementMapper fiveOaInformationEquipmentProcurementMapper;
    @Resource
    FiveOaInformationEquipmentProcurementDetailMapper fiveOaInformationEquipmentProcurementDetailMapper;
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
    ActService actService;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id,String userLogin){
        FiveOaInformationEquipmentProcurement item = fiveOaInformationEquipmentProcurementMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveOaInformationEquipmentProcurementDto item){
        FiveOaInformationEquipmentProcurement model = fiveOaInformationEquipmentProcurementMapper.selectByPrimaryKey(item.getId());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setProcurementTime(item.getProcurementTime());
        model.setChargeLeaderMen(item.getChargeLeaderMen());
        model.setChargeLeaderMenName(item.getChargeLeaderMenName());
        model.setFirstMangerMan(item.getFirstMangerMan());
        model.setFirstMangerManName(item.getFirstMangerManName());
        model.setListMen(item.getListMen());
        model.setListMenName(item.getListMenName());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        Map variables = Maps.newHashMap();

        variables.put("processDescription","????????????????????????????????????:" +model.getDeptName());
        myActService.setVariables(model.getProcessInstanceId(),variables);
        BeanValidator.check(model);
        fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(model);

    }

    public FiveOaInformationEquipmentProcurementDto getModelById(int id){

        return getDto(fiveOaInformationEquipmentProcurementMapper.selectByPrimaryKey(id));
    }

    public FiveOaInformationEquipmentProcurementDto getDto(FiveOaInformationEquipmentProcurement item) {
        FiveOaInformationEquipmentProcurementDto dto=FiveOaInformationEquipmentProcurementDto.adapt(item);

        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");
            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }

        //?????? ?????????
        String totalFinalPrice= MyStringUtil.moneyToString("0");
        for (FiveOaInformationEquipmentProcurementDetail detail:listDetail(item.getId())){
            totalFinalPrice = MyStringUtil.getNewAddMoney(totalFinalPrice,detail.getTotalPrice(),2);
        }
        if (!item.getFirstMangerMan().equals(totalFinalPrice)){
            item.setFirstMangerMan(totalFinalPrice);
            fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(item);
        }

        return dto;

    }

    public int getNewModel(String userLogin){

        FiveOaInformationEquipmentProcurement item=new FiveOaInformationEquipmentProcurement();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);

        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());

        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setProcurementTime((MyDateUtil.getYear()+1)+"");

        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","????????????????????????????????????:" +item.getDeptName());
        variables.put("deptChargeMen",selectEmployeeService.getParentDeptChargeMen(item.getDeptId(),1,true));//????????????????????? ??????????????????
        variables.put("otherDeptChargeMan", selectEmployeeService.getOtherDeptChargeMan(item.getDeptId()));

        fiveOaInformationEquipmentProcurementMapper.insert(item);
        String businessKey=EdConst.FIVE_OA_INFORMATIONEQUIPMENTPROCUREMENT + "_"+item.getId();

        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_INFORMATIONEQUIPMENTPROCUREMENT,businessKey, variables, MccConst.APP_CODE);
        item.setBusinessKey(businessKey);
        item.setProcessInstanceId(processInstanceId);
        fiveOaInformationEquipmentProcurementMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaInformationEquipmentProcurementMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaInformationEquipmentProcurement item=(FiveOaInformationEquipmentProcurement)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void removeDetail(int id){
        FiveOaInformationEquipmentProcurementDetail model = fiveOaInformationEquipmentProcurementDetailMapper.selectByPrimaryKey(id);

        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveOaInformationEquipmentProcurementDetailMapper.updateByPrimaryKey(model);
    }

    public void updateDetail(FiveOaInformationEquipmentProcurementDetail item){
        FiveOaInformationEquipmentProcurementDetail model = fiveOaInformationEquipmentProcurementDetailMapper.selectByPrimaryKey(item.getId());

        model.setEquipmentName(item.getEquipmentName());
        model.setEquipmentType(item.getEquipmentType());
        model.setUseType(item.getUseType());
        model.setNumber(item.getNumber());
        model.setPrice(MyStringUtil.moneyToString(item.getPrice(),2));
        model.setTotalPrice(MyStringUtil.moneyToString(item.getTotalPrice(),2));
        model.setEquipmentModel(item.getEquipmentModel());
        model.setRemark(item.getRemark());
        fiveOaInformationEquipmentProcurementDetailMapper.updateByPrimaryKey(model);
    }

    public FiveOaInformationEquipmentProcurementDetail getNewModelDetail(int  id){
        FiveOaInformationEquipmentProcurementDetail item=new FiveOaInformationEquipmentProcurementDetail();
        item.setInformationEquipmentProcurementId(id+"");
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveOaInformationEquipmentProcurementDetailMapper.insert(item);
        return item;

    }

    public FiveOaInformationEquipmentProcurementDetail getModelDetailById(int id){
        return fiveOaInformationEquipmentProcurementDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveOaInformationEquipmentProcurementDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("informationEquipmentProcurementId",id);
        List<FiveOaInformationEquipmentProcurementDetail> list = fiveOaInformationEquipmentProcurementDetailMapper.selectAll(params);
        return list;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaInformationEquipmentProcurement item = fiveOaInformationEquipmentProcurementMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("deptName",item.getDeptName());
        data.put("procurementTime",item.getProcurementTime());

        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());

        Map map =new HashMap();
        map.put("informationEquipmentProcurementId",item.getId());
        map.put("deleted",false);
        List<FiveOaInformationEquipmentProcurementDetail> informationEquipmentProcurementDetails = fiveOaInformationEquipmentProcurementDetailMapper.selectAll (map);
        FiveOaInformationEquipmentProcurementDetail total=new FiveOaInformationEquipmentProcurementDetail();
        total.setTotalPrice(item.getFirstMangerMan());
        informationEquipmentProcurementDetails.add(total);
        data.put("informationEquipmentProcurementDetails",informationEquipmentProcurementDetails);

       // data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        List<ActHistoryDto> actHistoryDtos = actService.listPassedHistoryDto(item.getProcessInstanceId());

        for (ActHistoryDto dto:actHistoryDtos){
            if (dto.getActivityName()==null){
                break;
            }
            if ("?????????".equals(dto.getActivityName())){
                data.put("startMan",dto);
            }
            if ("????????????".equals(dto.getActivityName())){
                data.put("deptChargeMan",dto);
            }
            if ("????????????".equals(dto.getActivityName())){
                data.put("branchedDeptChargeMan",dto);
            }
        }

        data.put("nodes",actHistoryDtos);

        return data;
    }

    /**
     * ??????excl
     * @param startTime1 ????????????
     * @param endTime1 ????????????
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,String uiSref,String startTime1,String endTime1,String userLogin,String userName){
        List<Map> list = new ArrayList<>();

        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);

        //??????????????????
        params.put("isLikeSelect",true);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);
        params.put("creator",userName);


        List<FiveOaInformationEquipmentProcurement> fiveOaInformationEquipmentProcurements=fiveOaInformationEquipmentProcurementMapper.selectAll(params);
        for (FiveOaInformationEquipmentProcurement dto:fiveOaInformationEquipmentProcurements){
            List<FiveOaInformationEquipmentProcurementDetail> fiveOaInformationEquipmentProcurementDetails=listDetail(dto.getId());

            for (FiveOaInformationEquipmentProcurementDetail detail:fiveOaInformationEquipmentProcurementDetails){
                Map map1=new LinkedHashMap();
                map1.put("????????????",dto.getDeptName());
                map1.put("????????????",dto.getProcurementTime());
                map1.put("?????????",dto.getCreatorName());
                map1.put("????????????",MyDateUtil.dateToStr(dto.getGmtCreate()));
                map1.put("????????????",detail.getEquipmentName());
                map1.put("????????????",detail.getEquipmentModel());
                map1.put("??????",detail.getEquipmentType());
                map1.put("???????????????????????????",detail.getUseType());
                map1.put("????????????",detail.getNumber());
                map1.put("???????????????",detail.getPrice());
                map1.put("???????????????",detail.getTotalPrice());
                map1.put("??????",detail.getRemark());
                list.add(map1);
            }
        }


        return list;
    }
}

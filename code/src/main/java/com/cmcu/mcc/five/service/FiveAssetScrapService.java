package com.cmcu.mcc.five.service;

import com.cmcu.act.dto.CustomSimpleProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.service.CommonFileService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.comm.EdConst;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveAssetScrapDetailMapper;
import com.cmcu.mcc.five.dao.FiveAssetScrapMapper;
import com.cmcu.mcc.five.dto.FiveAssetScrapDto;
import com.cmcu.mcc.five.entity.FiveAssetScrap;
import com.cmcu.mcc.five.entity.FiveAssetScrapDetail;
import com.cmcu.mcc.five.entity.FiveOaFurniturePurchaseDetail;
import com.cmcu.mcc.five.entity.FiveOaSignQuote;
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
import java.util.*;

@Service
public class FiveAssetScrapService extends BaseService {
    
    @Resource
    FiveAssetScrapMapper fiveAssetScrapMapper;
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
    @Resource
    FiveAssetScrapDetailMapper fiveAssetScrapDetailMapper;
    @Resource
    CommonFileService commonFileService;

  public List<FiveAssetScrap> listDate(String userLogin){
      Map map= Maps.newHashMap();
      map.put("deleted",false);
      map.put("",userLogin);
      List<FiveAssetScrap> fiveAssetScraps = fiveAssetScrapMapper.selectAll(map);
      return  fiveAssetScraps;
  }

    public void remove(int id,String userLogin){
        FiveAssetScrap item = fiveAssetScrapMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");
        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);

    }

    public void update(FiveAssetScrapDto item){
        FiveAssetScrap model = fiveAssetScrapMapper.selectByPrimaryKey(item.getId());
        model.setFormNo(item.getFormNo());
        model.setApplicant(item.getApplicant());
        model.setApplicantName(item.getApplicantName());
        model.setApplicantTime(item.getApplicantTime());
        model.setDeptId(item.getDeptId());
        model.setDeptName(item.getDeptName());
        model.setApplicantReason(item.getApplicantReason());
        model.setApplicantDeptOpinion(item.getApplicantDeptOpinion());
        model.setHandleOpinion(item.getHandleOpinion());
        model.setDisposeAsset(item.getDisposeAsset());
        model.setRemark(item.getRemark());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        fiveAssetScrapMapper.updateByPrimaryKey(model);
        Map variables = Maps.newHashMap();
        if(item.getDeptId()!=0) {
            variables.put("deptChargeMan", selectEmployeeService.getDeptChargeMen(item.getDeptId()));//?????????????????????
        }
        //?????????????????????
        if (model.getDisposeAsset()){
            //copyFinanceMan ?????????????????????
            variables.put("sign", true);
            variables.put("copyFinanceMan", MyStringUtil.listToString(selectEmployeeService.getDeptFinanceMan(item.getDeptId())));
        }else {
            variables.put("sign", false);
            variables.put("deptFinanceMan",selectEmployeeService.getDeptFinanceMan(item.getDeptId()));
        }
        variables.put("processDescription","??????????????????(????????????)???"+item.getApplicantName());
        BeanValidator.check(model);

        myActService.setVariables(model.getProcessInstanceId(),variables);

    }


    public FiveAssetScrapDto getModelById(int id){

        return getDto(fiveAssetScrapMapper.selectByPrimaryKey(id));
    }

    public FiveAssetScrapDto getDto(FiveAssetScrap item) {
        FiveAssetScrapDto dto=FiveAssetScrapDto.adapt(item);
        dto.setProcessName("?????????");

        if (!dto.getProcessEnd()){
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), dto.getBusinessKey(), "");
            if(customProcessInstance == null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveAssetScrapMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance !=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());

            }
        }

        return dto;
    }

    public int getNewModel(String userLogin){
        FiveAssetScrap item=new FiveAssetScrap();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        item.setApplicant(hrEmployeeDto.getUserLogin());
        item.setApplicantName(hrEmployeeDto.getUserName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        item.setApplicantTime(formatter.format(new Date()));
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
        fiveAssetScrapMapper.insert(item);

        String businessKey= EdConst.FIVE_ASSET_SCRAP+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription","??????????????????(????????????)???"+item.getCreatorName());
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("deptChargeMan",selectEmployeeService.getDeptChargeMen(item.getDeptId()));
        variables.put("financeLeader",selectEmployeeService.getDeptChargeMen(18));//???????????????
        variables.put("administrative", selectEmployeeService.getDeptChargeMen(67));//?????????????????????
        variables.put("administrativeCharge", selectEmployeeService.getOtherDeptChargeMan(67));//?????????????????????
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_ASSET_SCRAP,businessKey, variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);
        item.setBusinessKey(businessKey);
        fiveAssetScrapMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);

        Map map=Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveAssetScrapMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveAssetScrap item=(FiveAssetScrap)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public void updateDetail(FiveAssetScrapDetail item){
        FiveAssetScrapDetail model = fiveAssetScrapDetailMapper.selectByPrimaryKey(item.getId());

        model.setDeviceType(item.getDeviceType());
        model.setDeviceNo(item.getDeviceNo());
        model.setDeviceModel(item.getDeviceModel());
        model.setDutyPerson(item.getDutyPerson());
        model.setDutyPersonName(item.getDutyPersonName());
        model.setStartTime(item.getStartTime());
        model.setOriginalValue(item.getOriginalValue());
        model.setDepreciableLife(item.getDepreciableLife());
        model.setSubmitted(item.getSubmitted());
        model.setNetWorth(item.getNetWorth());
        model.setRemark(item.getRemark());
        if( commonFileService.listData(item.getBusinessKey(),0,"").size()>0){
            model.setUploadfile(true);
        }else {
            model.setUploadfile(false);
        }
        fiveAssetScrapDetailMapper.updateByPrimaryKey(model);
    }

    public FiveAssetScrapDetail getNewModelDetail(int  assetScrapId,String userLogin){
        FiveAssetScrapDetail item=new FiveAssetScrapDetail();
        item.setAssetScrapId(assetScrapId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        item.setStartTime(formatter.format(new Date()));
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveAssetScrapDetailMapper.insert(item);
        item.setBusinessKey("assetScrapDetail_"+item.getId());
        fiveAssetScrapDetailMapper.updateByPrimaryKey(item);
        return item;

    }

    public FiveAssetScrapDetail getModelDetailById(int id){
        return fiveAssetScrapDetailMapper.selectByPrimaryKey(id);
    }

    public List<FiveAssetScrapDetail> listDetail(int id){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("assetScrapId",id);//??????
        List<FiveAssetScrapDetail> list = fiveAssetScrapDetailMapper.selectAll(params);
        return list;
    }

    public void removeDetail(int id){
        FiveAssetScrapDetail model = fiveAssetScrapDetailMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        BeanValidator.validateObject(model);
        fiveAssetScrapDetailMapper.updateByPrimaryKey(model);
    }

    /**
     * ??????excel
     * @param params
     * @param uiSref
     * @param startTime1
     * @param endTime1
     * @param userLogin
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
        //??????????????????
        params.put("isLikeSelect",true);
        //??????????????????
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//???????????????userLogin ??????????????????
        //????????? ???????????????
        params.put("deleted",false);
        params.put("processEnd",true);
        //???????????????
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);


        List<FiveAssetScrap> fiveAssetScraps=fiveAssetScrapMapper.selectAll(params);
        for (FiveAssetScrap dto:fiveAssetScraps){
            List<FiveAssetScrapDetail> fiveAssetScrapDetails=listDetail(dto.getId());
            for (FiveAssetScrapDetail detail:fiveAssetScrapDetails){
                Map map1=new LinkedHashMap();
                map1.put("?????????",dto.getApplicantName());
                map1.put("????????????",dto.getDeptName());
                map1.put("????????????",dto.getDeptName());
                map1.put("????????????",dto.getRemark());
                map1.put("????????????",dto.getApplicantReason());
                map1.put("?????????",dto.getCreatorName());
                map1.put("????????????", MyDateUtil.dateToStr(dto.getGmtCreate()));

                map1.put("????????????",detail.getDeviceType());
                map1.put("????????????",detail.getDeviceNo());
                map1.put("????????????",detail.getDeviceModel());
                map1.put("?????????",detail.getDutyPersonName());
                map1.put("????????????",detail.getStartTime());
                map1.put("??????",detail.getOriginalValue());
                map1.put("????????????",detail.getDepreciableLife());
                map1.put("????????????",detail.getSubmitted());
                map1.put("??????",detail.getNetWorth());
                list.add(map1);
            }
        }
        return list;
    }

}

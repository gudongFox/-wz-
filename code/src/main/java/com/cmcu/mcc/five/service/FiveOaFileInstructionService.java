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
import com.cmcu.mcc.five.dao.FiveOaFileInstructionMapper;
import com.cmcu.mcc.five.dto.FiveOaFileInstructionDto;
import com.cmcu.mcc.five.entity.FiveOaFileInstruction;
import com.cmcu.mcc.five.entity.FiveOaInlandProjectApply;
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
public class FiveOaFileInstructionService extends BaseService {
    @Autowired
    FiveOaFileInstructionMapper fiveOaFileInstructionMapper;
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
    @Autowired
    FiveOaWordSizeService fiveOaWordSizeService;

    @Resource
    HandleFormService handleFormService;


    public void remove(int id,String userLogin){
        FiveOaFileInstruction item = fiveOaFileInstructionMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equals(userLogin),"??????????????????"+item.getCreatorName()+"??????");

        //????????????
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        //??????????????????
        fiveOaWordSizeService.removeWordByBusinessKey(item.getBusinessKey());

    }

    public void update(FiveOaFileInstructionDto fiveOaFileInstructionDto){
        FiveOaFileInstruction model = fiveOaFileInstructionMapper.selectByPrimaryKey(fiveOaFileInstructionDto.getId());
        model.setDeptId(fiveOaFileInstructionDto.getDeptId());
        model.setDeptName(fiveOaFileInstructionDto.getDeptName());
        model.setFileId(fiveOaFileInstructionDto.getFileId());
        model.setFileTitle(fiveOaFileInstructionDto.getFileTitle());
        model.setSigner(fiveOaFileInstructionDto.getSigner());
        model.setSignerName(fiveOaFileInstructionDto.getSignerName());
        model.setSecurity(fiveOaFileInstructionDto.getSecurity());
        model.setSendDeptName(fiveOaFileInstructionDto.getSendDeptName());
        model.setSendWordSize(fiveOaFileInstructionDto.getSendWordSize());
        model.setReceiveWordSize(fiveOaFileInstructionDto.getReceiveWordSize());
        model.setTextNumber(fiveOaFileInstructionDto.getTextNumber());
        model.setGmtModified(new Date());
        model.setUndertakeDeptId(fiveOaFileInstructionDto.getUndertakeDeptId());
        model.setUndertakeDeptName(fiveOaFileInstructionDto.getUndertakeDeptName());
        model.setCompanyLeader(fiveOaFileInstructionDto.getCompanyLeader());
        model.setCompanyLeaderName(fiveOaFileInstructionDto.getCompanyLeaderName());
        model.setReceiveTime(fiveOaFileInstructionDto.getReceiveTime());
        model.setReadLeader(fiveOaFileInstructionDto.getReadLeader());
        model.setReadLeaderName(fiveOaFileInstructionDto.getReadLeaderName());


        Map variables = Maps.newHashMap();
        if ("".equals(fiveOaFileInstructionDto.getUndertakeDeptName())){
            variables.put("IsDept",false);
        }else {
            variables.put("IsDept",true);
            List<String> deptChargeMen =Lists.newArrayList();
            String[] split = model.getUndertakeDeptId().split(",");
            for (String deptId:split){
                if (!"".equals(deptId)){
                    if (deptId=="38"){
                        //???????????????????????????
                        deptChargeMen.addAll( selectEmployeeService.getDeptAllChargeMen(Integer.parseInt(deptId)));
                    }else {
                        deptChargeMen.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
                    }
                }
            }
            variables.put("deptChargeMenList",deptChargeMen);
        }
        variables.put("companyLeadMan", MyStringUtil.getStringList(model.getCompanyLeader()));//????????????
        if ("".equals(fiveOaFileInstructionDto.getReadLeader())){
            variables.put("IsCompany",false);
        }else {
            variables.put("IsCompany",true);
            variables.put("companyLeaderList", MyStringUtil.getStringList(model.getReadLeader()));//????????????
        }
        variables.put("processDescription", "????????????:"+model.getFileTitle());
        BeanValidator.check(model);
        fiveOaFileInstructionMapper.updateByPrimaryKey(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }
    public FiveOaFileInstructionDto getModelById(int id){

        return getDto(fiveOaFileInstructionMapper.selectByPrimaryKey(id));
    }

    public FiveOaFileInstructionDto getDto(FiveOaFileInstruction item) {
        FiveOaFileInstructionDto dto=FiveOaFileInstructionDto.adapt(item);
        dto.setProcessName("?????????");
        if (!dto.getProcessEnd()) {
            CustomSimpleProcessInstance customProcessInstance = processQueryService.getCustomSimpleProcessInstance(dto.getProcessInstanceId(), "", "");

            if(customProcessInstance ==null || customProcessInstance.isFinished()){
                dto.setProcessEnd(true);
                fiveOaFileInstructionMapper.updateByPrimaryKey(dto);
            }
            if(customProcessInstance!=null && StringUtils.isNotEmpty(customProcessInstance.getCurrentTaskName())){
                dto.setProcessName(customProcessInstance.getCurrentTaskName());
            }
        }
        return dto;
    }

    public int getNewModel(String userLogin){
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("????????????(??????)");
        List<String> nuclearDraft =  hrEmployeeService.selectUserByRoleNames("????????????(??????)");//2021-01-05HNZ ???????????? ??????????????????
        Assert.state(secretary.size()>0,"?????????????????? ???????????? ?????????");
        FiveOaFileInstruction item=new FiveOaFileInstruction();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        Assert.state(hrEmployeeDto.getDeptId()==59,"????????? ?????????????????????????????????");
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setSigner(hrEmployeeDto.getUserLogin());
        item.setSignerName(hrEmployeeDto.getUserName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setSecurity("???");
        item.setYear(MyDateUtil.getYear());
        item.setReceiveTime(MyDateUtil.getStringToday());
        //????????? ??????+??????????????? 2020001
        /*DecimalFormat mFormat = new DecimalFormat("000");
        String format = mFormat.format(getSize());
        item.setSendWordSize( MyDateUtil.getYear()+format);
*/
        item.setDeleted(false);
        item.setProcessEnd(false);
        item.setCreator(userLogin);
        item.setGmtModified(new Date());
        item.setGmtCreate(new Date());
        ModelUtil.setNotNullFields(item);

        fiveOaFileInstructionMapper.insert(item);
        String businessKey= EdConst.FIVE_OA_FILE_INSTRUCTION+ "_" + item.getId();
        Map variables = Maps.newHashMap();
        variables.put("userLogin", userLogin);
        variables.put("processDescription", "????????????");
        variables.put("secretary",secretary);
        variables.put("nuclearDraft",nuclearDraft);
        variables.put("officeChargeMan",selectEmployeeService.getDeptChargeMen(59));
        item.setBusinessKey(businessKey);
        String processInstanceId = taskHandleService.startProcess(EdConst.FIVE_OA_FILE_INSTRUCTION,item.getBusinessKey(), variables, MccConst.APP_CODE);
        item.setProcessInstanceId(processInstanceId);

        fiveOaFileInstructionMapper.updateByPrimaryKey(item);
        return item.getId();
    }



    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("isLikeSelect",true);
      /*List<Integer> deptIdList=hrEmployeeSysService.getMyDeptListOa(userLogin,uiSref);
         if (deptIdList.size()==0){
            params.put("creator",userLogin);Maps.newHashMap()
        }else {
            params.put("deptIdList",deptIdList);
        }*/

        //???????????????????????????????????????  myDeptData true?????????????????? false???????????????
        Map map =Maps.newHashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));

        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveOaFileInstructionMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveOaFileInstruction item=(FiveOaFileInstruction)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public  Map getPrintData(int id) {
        Map data = Maps.newHashMap();
        FiveOaFileInstruction item = fiveOaFileInstructionMapper.selectByPrimaryKey(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String number = sdf.format(new Date())+item.getCreator().substring((item.getCreator()).length() - 4);
        data.put("tableNo",number);
        data.put("fileTitle",item.getFileTitle());//????????????
        data.put("signerName",item.getSignerName());//?????????
        data.put("security",item.getSecurity());//????????????
        data.put("sendDeptName",item.getSendDeptName());//????????????
        data.put("sendWordSize",item.getSendWordSize());//?????????
        data.put("receiveTime",item.getReceiveTime());//????????????
        data.put("receiveWordSize",item.getReceiveWordSize());//?????????
        data.put("textNumber",item.getTextNumber());//????????????
        data.put("companyLeaderName",item.getCompanyLeaderName());//????????????
        data.put("undertakeDeptName",item.getUndertakeDeptName());//????????????


        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());


        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    /**
     * ??????????????? ??????????????? ?????????
     * @return
     */
    public int getSize(){
        Map<String,Object> map=Maps.newHashMap();
        map.put("deleted",false);
        map.put("year",MyDateUtil.getYear());
        int size = fiveOaFileInstructionMapper.selectAll(map).size();
        return size+1;
    }

    /**
     * ??????excel
     * @param startTime1 ????????????
     * @param endTime1 ????????????
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


        List<FiveOaFileInstruction> fiveOaFileInstructions=fiveOaFileInstructionMapper.selectAll(params);
        for (FiveOaFileInstruction dto:fiveOaFileInstructions){

            Map map1=new LinkedHashMap();
            map1.put("????????????",dto.getFileTitle());
            map1.put("?????????",dto.getSignerName());
            map1.put("????????????",dto.getReceiveTime());
            map1.put("????????????",dto.getCompanyLeaderName());
            map1.put("????????????",dto.getUndertakeDeptName());
            map1.put("????????????", dto.getReadLeaderName());
            map1.put("????????????",dto.getSendDeptName());
            map1.put("?????????",dto.getReceiveWordSize());
            map1.put("?????????", dto.getSendWordSize());
            map1.put("????????????",dto.getSecurity());
            map1.put("????????????",dto.getTextNumber());
            map1.put("?????????",dto.getCreatorName());
            map1.put("????????????",dto.getGmtCreate());

            list.add(map1);
        }


        return list;
    }


}

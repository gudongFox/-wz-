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
        Assert.state(item.getCreator().equals(userLogin),"该数据是用户"+item.getCreatorName()+"创建");

        //流程作废
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
        //废弃文号使用
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
                        //人力资源部取正副职
                        deptChargeMen.addAll( selectEmployeeService.getDeptAllChargeMen(Integer.parseInt(deptId)));
                    }else {
                        deptChargeMen.addAll( selectEmployeeService.getDeptChargeMen(Integer.parseInt(deptId)));
                    }
                }
            }
            variables.put("deptChargeMenList",deptChargeMen);
        }
        variables.put("companyLeadMan", MyStringUtil.getStringList(model.getCompanyLeader()));//批示领导
        if ("".equals(fiveOaFileInstructionDto.getReadLeader())){
            variables.put("IsCompany",false);
        }else {
            variables.put("IsCompany",true);
            variables.put("companyLeaderList", MyStringUtil.getStringList(model.getReadLeader()));//阅示领导
        }
        variables.put("processDescription", "公司收文:"+model.getFileTitle());
        BeanValidator.check(model);
        fiveOaFileInstructionMapper.updateByPrimaryKey(model);
        myActService.setVariables(model.getProcessInstanceId(),variables);
    }
    public FiveOaFileInstructionDto getModelById(int id){

        return getDto(fiveOaFileInstructionMapper.selectByPrimaryKey(id));
    }

    public FiveOaFileInstructionDto getDto(FiveOaFileInstruction item) {
        FiveOaFileInstructionDto dto=FiveOaFileInstructionDto.adapt(item);
        dto.setProcessName("已完成");
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
        List<String> secretary =  hrEmployeeService.selectUserByRoleNames("机要秘书(收文)");
        List<String> nuclearDraft =  hrEmployeeService.selectUserByRoleNames("机要秘书(核稿)");//2021-01-05HNZ 新增角色 机要秘书核稿
        Assert.state(secretary.size()>0,"未找到职务为 机要秘书 人员！");
        FiveOaFileInstruction item=new FiveOaFileInstruction();
        HrEmployeeDto hrEmployeeDto = hrEmployeeMapper.selectByUserLoginOrNo(userLogin);
        Assert.state(hrEmployeeDto.getDeptId()==59,"该流程 只能由公司办人员发起！");
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setDeptName(hrEmployeeDto.getDeptName());

        item.setSigner(hrEmployeeDto.getUserLogin());
        item.setSignerName(hrEmployeeDto.getUserName());
        item.setCreator(hrEmployeeDto.getUserLogin());
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setSecurity("否");
        item.setYear(MyDateUtil.getYear());
        item.setReceiveTime(MyDateUtil.getStringToday());
        //来文号 年份+三位流水号 2020001
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
        variables.put("processDescription", "公司收文");
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

        //如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
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
        data.put("fileTitle",item.getFileTitle());//文件标题
        data.put("signerName",item.getSignerName());//签收人
        data.put("security",item.getSecurity());//密级等级
        data.put("sendDeptName",item.getSendDeptName());//来文单位
        data.put("sendWordSize",item.getSendWordSize());//来文号
        data.put("receiveTime",item.getReceiveTime());//签收日期
        data.put("receiveWordSize",item.getReceiveWordSize());//收文号
        data.put("textNumber",item.getTextNumber());//正文份数
        data.put("companyLeaderName",item.getCompanyLeaderName());//批示领导
        data.put("undertakeDeptName",item.getUndertakeDeptName());//承办单位


        data.put("creatorName",item.getCreatorName());
        data.put("gmtCreate",item.getGmtCreate());


        data.put("nodes",actService.listPassedHistoryDto(item.getProcessInstanceId()));

        return data;
    }

    /**
     * 获取流水号 所有未删除 当年的
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
     * 导出excel
     * @param startTime1 开始时间
     * @param endTime1 结束时间
     * @return
     */
    public List<Map> listMapData(Map<String,Object> params,  String uiSref,String startTime1 ,String endTime1,String userLogin){
        List<Map> list = new ArrayList<>();
        //模糊匹配查询
        params.put("isLikeSelect",true);
        //数据权限验证
        Map head=Maps.newHashMap();
        head.put("myDeptData",false);
        head.put("uiSref",uiSref);
        head.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(head));
        params.remove("userLogin");//字段中含有userLogin 排除干扰因素
        //为删除 流程已完成
        params.put("deleted",false);
        params.put("processEnd",true);
        //时间端参数
        params.put("startTime1",startTime1);
        params.put("endTime1",endTime1);


        List<FiveOaFileInstruction> fiveOaFileInstructions=fiveOaFileInstructionMapper.selectAll(params);
        for (FiveOaFileInstruction dto:fiveOaFileInstructions){

            Map map1=new LinkedHashMap();
            map1.put("文件标题",dto.getFileTitle());
            map1.put("签收人",dto.getSignerName());
            map1.put("签收日期",dto.getReceiveTime());
            map1.put("批示领导",dto.getCompanyLeaderName());
            map1.put("承办单位",dto.getUndertakeDeptName());
            map1.put("阅办领导", dto.getReadLeaderName());
            map1.put("来文单位",dto.getSendDeptName());
            map1.put("收文号",dto.getReceiveWordSize());
            map1.put("来文号", dto.getSendWordSize());
            map1.put("是否涉密",dto.getSecurity());
            map1.put("正文份数",dto.getTextNumber());
            map1.put("创建人",dto.getCreatorName());
            map1.put("创建时间",dto.getGmtCreate());

            list.add(map1);
        }


        return list;
    }


}

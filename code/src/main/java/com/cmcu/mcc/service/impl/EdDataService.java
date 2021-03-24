package com.cmcu.mcc.service.impl;

import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.CommonEdArrangeUserDto;
import com.cmcu.common.entity.CommonEdArrangeUser;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.service.CommonEdArrangeUserService;
import com.cmcu.common.service.IEdDataService;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.business.dao.BusinessContractMapper;
import com.cmcu.mcc.business.entity.BusinessContract;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EdDataService implements IEdDataService {

    @Resource
    EdProjectStepMapper edProjectStepMapper;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    @Resource
    BusinessContractMapper businessContractMapper;


    @Override
    public Map getNewEdData(String referType,int referId,String enLogin) {
        Map data = Maps.newHashMap();
        EdProjectStep edProjectStep = edProjectStepMapper.selectByPrimaryKey(referId);
        if (edProjectStep != null) {
            data = JsonMapper.string2Map(JsonMapper.obj2String(edProjectStep));
            data.remove("creator");
            data.remove("creatorName");
            data.remove("gmtCreate");
            data.remove("gmtModified");
            data.remove("remark");
            data.put("stepId", edProjectStep.getId());
            try{
                data.put("totalDesigner", MyStringUtil.getStringList(edProjectStep.getChargeMan()).get(0));
                data.put("totalDesignerName", MyStringUtil.getStringList(edProjectStep.getChargeManName()).get(0));
                data.put("exeChargeMan", MyStringUtil.getStringList(edProjectStep.getExeChargeMan()).get(0));
                data.put("exeChargeManName", MyStringUtil.getStringList(edProjectStep.getExeChargeManName()).get(0));
            }catch (Exception e){
                Assert.state(false, "请在期次信息中完善人员信息！");
            }

            List<String> projectChargeList = MyStringUtil.getStringList(edProjectStep.getProjectChargeMan());
            List<String> projectChargeNameList = MyStringUtil.getStringList(edProjectStep.getProjectChargeManName());
            if (projectChargeList.size() > 0) {
                data.put("projectChargeMan", projectChargeList.get(0));
                if (projectChargeNameList.size() > 0) {
                    data.put("projectChargeManName", projectChargeNameList.get(0));
                }
            }
        }
        return data;
    }

    @Override
    public List<CommonEdArrangeUserDto> listUser(int referId, String majorName, Integer buildId) {
        List<CommonEdArrangeUserDto> list = Lists.newArrayList();
        String businessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(referId);
        if (StringUtils.isNotEmpty(businessKey)) {
            List<CommonEdArrangeUserDto> users = commonEdArrangeUserService.listUser(businessKey);
            if (StringUtils.isNotEmpty(majorName))
                users = users.stream().filter(p -> p.getMajorName().equalsIgnoreCase(majorName)).collect(Collectors.toList());
            if (buildId != null)
                users = users.stream().filter(p -> buildId.equals(p.getBuildId())).collect(Collectors.toList());
            return users;
        }
        return list;
    }

    @Override
    public Map getEdInformation(int formDataId) {
        CommonFormData commonFormData = commonFormDataMapper.selectByPrimaryKey(formDataId);
        Map data = JsonMapper.string2Map(JsonMapper.obj2String(commonFormData));
        List<String> majorNames = MyStringUtil.getStringList(data.getOrDefault("majorName", "").toString());
        List<Integer> buildIds = MyStringUtil.getIntList(data.getOrDefault("buildId", "").toString());
        return data;
    }

    public void checkAttendUser(String businessKey) {
        CommonFormData commonFormData=commonFormDataMapper.selectByBusinessKey(businessKey);
        if(commonFormData!=null&&commonFormData.getBusinessKey().contains("fiveEdArrange")) {
            EdProjectStep step=edProjectStepMapper.selectByPrimaryKey(commonFormData.getReferId());
            if(step!=null) {
                BusinessContract businessContract = businessContractMapper.selectByPrimaryKey(step.getProjectId());
                String arrangeBusinessKey = commonEdArrangeUserService.getDefaultArrangeBusinessKey(step.getId());
                List<CommonEdArrangeUserDto> users = commonEdArrangeUserService.listUser(arrangeBusinessKey);
                String attendUser = StringUtils.join(users.stream().map(CommonEdArrangeUser::getAllUser).collect(Collectors.toList()), ",");
                step.setAttendUser(MyStringUtil.getMultiDotString(
                        attendUser
                                + "," + step.getChargeMan() + "," + step.getExeChargeMan() + "," + step.getOtherChargeMan() + "," + step.getProjectChargeMan() + "," + step.getOtherChargeMan()
                                + "," + businessContract.getChargeMen() + "," + businessContract.getExeChargeMen() + "," + businessContract.getTotalDesigner() + "," + businessContract.getBusinessManager())
                );
                edProjectStepMapper.updateByPrimaryKey(step);
            }
        }
    }
}

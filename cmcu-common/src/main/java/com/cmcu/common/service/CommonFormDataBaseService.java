package com.cmcu.common.service;


import com.cmcu.act.dto.CustomProcessInstance;
import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.TplConfigDto;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonFormDataBaseService {


    @Resource
    CommonFormService commonFormService;

    @Resource
    ProcessQueryService processQueryService;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    /**
     * 得到表单配置
     * @param businessKey
     * @param enLogin
     * @return
     */
    public TplConfigDto getTplConfigDto(String businessKey, String enLogin) {

        TplConfigDto tplConfigDto = new TplConfigDto();


        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        if (commonFormData != null) {
            CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getReferType(), commonFormData.getFormVersion());
            if (commonForm != null) {
                tplConfigDto.setBusinessKey(businessKey);
                TplConfigDto findTplConfigDto = TplConfigDto.getInstance(commonForm.getOtherTplConfig());
                tplConfigDto.setShowBusinessFile(findTplConfigDto.isShowBusinessFile());
                tplConfigDto.setShowFileType(findTplConfigDto.isShowFileType());
                tplConfigDto.setFileTypeCode(findTplConfigDto.getFileTypeCode());
                tplConfigDto.setProcessKey(findTplConfigDto.getProcessKey());
                tplConfigDto.setShowFileDir(findTplConfigDto.isShowFileDir());
                tplConfigDto.setShowProcessIntegration(findTplConfigDto.isShowProcessIntegration());
                tplConfigDto.setBusinessFileTip(findTplConfigDto.getBusinessFileTip());
                tplConfigDto.setMarkRoleNames(findTplConfigDto.getMarkRoleNames());
                tplConfigDto.setSelectCoFileType(findTplConfigDto.getSelectCoFileType());
                tplConfigDto.setFormDesc(commonForm.getFormDesc());
                tplConfigDto.setFormIcon(commonForm.getFormIcon());
                if (StringUtils.isEmpty(commonFormData.getProcessInstanceId())) {
                    tplConfigDto.setEditable(!commonFormData.getProcessEnd() && commonFormData.getCreator().equalsIgnoreCase(enLogin));
                } else {

                    CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance("", businessKey, enLogin);
                    if (tplConfigDto.getMarkRoleNames().size() > 0) {
                        for (String roleName : tplConfigDto.getMarkRoleNames()) {
                            if (customProcessInstance.getMyRunningTaskNameList().stream().anyMatch(p -> p.startsWith(roleName))) {
                                tplConfigDto.setMarkAddRoleName(roleName);
                                break;
                            }
                        }
                    }
                    tplConfigDto.setTaskList(customProcessInstance.getMyRunningTaskNameList());
                    tplConfigDto.setSaveAble(customProcessInstance.isFirstTask());
                    tplConfigDto.setEditable(customProcessInstance.getMyRunningTaskNameList().size() > 0);
                }

                if (tplConfigDto.isEditable()) {
                    List<CommonFormDetail> details = listEditableDetail(commonFormData, enLogin);
                    tplConfigDto.setSaveAble(details.size() > 0);
                }

                Map formData = JsonMapper.string2Map(commonFormData.getFormData());
                tplConfigDto.setMajorNames(formData.getOrDefault("majorName", "").toString());
                if (StringUtils.isEmpty(tplConfigDto.getMajorNames())) {
                    tplConfigDto.setMajorNames(formData.getOrDefault("sourceMajor", "").toString());
                }
                tplConfigDto.setBuildIds(formData.getOrDefault("buildIds", "").toString());
                tplConfigDto.setStepId(Integer.parseInt(formData.getOrDefault("stepId", "0").toString()));
            }
        }
        return tplConfigDto;
    }


    /**
     * 得到可以保存的数据列表
     * @param commonFormData
     * @param enLogin
     * @return
     */
    public List<CommonFormDetail> listEditableDetail(CommonFormData commonFormData, String enLogin) {
        CommonForm commonForm = commonFormService.getModel(commonFormData.getTenetId(), commonFormData.getFormKey(), commonFormData.getFormVersion());
        if (commonForm != null && !commonFormData.getProcessEnd()) {
            //非隐藏,且可编辑
            List<CommonFormDetail> details = commonFormService.listDetail(commonForm.getId()).stream().filter(CommonFormDetail::getEditable).collect(Collectors.toList());
            if (StringUtils.isEmpty(commonFormData.getProcessInstanceId())) {
                return details;
            } else if (StringUtils.isNotEmpty(commonFormData.getProcessInstanceId())) {
                CustomProcessInstance customProcessInstance = processQueryService.getCustomProcessInstance(commonFormData.getProcessInstanceId(), "", enLogin);
                if (customProcessInstance != null && customProcessInstance.getMyRunningTaskNameList().size() > 0) {
                    List<CommonFormDetail> list = Lists.newArrayList();
                    for (CommonFormDetail detail : details) {
                        if (detail.getEditableTag().equalsIgnoreCase("creator")) {
                            if (customProcessInstance.isFirstTask()) list.add(detail);
                        } else {
                            List<String> editableTags = MyStringUtil.getStringList(detail.getEditableTag());
                            if (customProcessInstance.getMyRunningTaskNameList().stream().anyMatch(p -> editableTags.stream().anyMatch(p::contains))) {
                                list.add(detail);
                            }
                        }
                    }
                    return list;
                }
            }
        }
        return Lists.newArrayList();
    }




}

package com.cmcu.common.service;

import com.cmcu.act.dto.CustomHistoricTaskInstance;
import com.cmcu.act.service.TaskQueryService;
import com.cmcu.common.dto.InputGroupDto;
import com.cmcu.common.dto.InputItemDto;
import com.cmcu.common.entity.CommonForm;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.util.JsonMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CommonPrintTableService {
    @Resource
    CommonFormDataService commonFormDataService;

    @Resource
    TaskQueryService taskQueryService;

    @Resource
    IHandleFormService handleFormService;


    public Map getPrintDate(String businessKey, String userLogin) {
        Map map = new HashMap();

        CommonFormData commonFormData = commonFormDataService.getModelByBusinessKey(businessKey);

        Map data3 = commonFormDataService.getModelById(commonFormData.getId(), userLogin);

        CommonForm template = (CommonForm) data3.get("template");
        map.put("tableName", template.getFormDesc());//表头
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String creator = commonFormData.getCreator();
        String id = sdf.format(new Date()) + creator.substring(creator.length() - 4);
        map.put("tableNo", id);//打印编号

        List<InputGroupDto> groupList = (List<InputGroupDto>) data3.get("groupList");
        List<InputGroupDto> groupListDetail = Lists.newArrayList();
        //删除不需要显示的内容
        for (InputGroupDto dto : groupList) {
            InputGroupDto newGroup = new InputGroupDto();
            newGroup.setGroupName(dto.getGroupName());
            List<InputItemDto> newItem = Lists.newArrayList();
            for (InputItemDto detail : dto.getItems()) {
                if (!detail.getCommonFormDetail().getDetailHidden()) {
                    newItem.add(detail);
                }
            }
            newGroup.setItems(newItem);
            groupListDetail.add(newGroup);
        }

        map.put("groupList", groupListDetail);

        List<List<Object>> list = Lists.newArrayList();
        List<Map> details = handleFormService.listFormChildList(businessKey);
        if (details.size() > 0) {
            list.add(Arrays.asList(details.get(0).keySet().toArray()));
            details.forEach(p -> list.add(Arrays.asList(p.values())));
        }
        //子表

        List<Map> listChild = handleFormService.listFormChildList(businessKey);

        map.put("groupDetailList", listChild);
        Object childTableType = JsonMapper.string2Map(template.getOtherTplConfig()).getOrDefault("childTableType", 2);
        map.put("childTableType", childTableType);//子表打印样式  1 横向表格打印  2 通用详情打印逻辑

        if (StringUtils.isNotEmpty(commonFormData.getProcessInstanceId())) {
            List<CustomHistoricTaskInstance> customHistoricTaskInstances = taskQueryService.listHistoricTaskInstance(commonFormData.getProcessInstanceId());
            Collections.reverse(customHistoricTaskInstances);//倒序
            map.put("nodes", customHistoricTaskInstances);
        }
        return map;
    }

}

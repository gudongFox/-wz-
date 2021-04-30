package com.cmcu.ed.service;


import com.cmcu.ed.dao.EdConfigPlanDetailMapper;
import com.cmcu.ed.dao.EdConfigPlanTemplateMapper;
import com.cmcu.ed.entity.EdConfigPlanDetail;
import com.cmcu.ed.entity.EdConfigPlanTemplate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EdConfigPlanService {

   @Resource
    EdConfigPlanTemplateMapper edConfigPlanTemplateMapper;

    @Resource
    EdConfigPlanDetailMapper edConfigPlanDetailMapper;


    public List<EdConfigPlanDetail> listData(String projectType,String stageName) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        int templateId = 0;
        List<EdConfigPlanTemplate> templates = edConfigPlanTemplateMapper.selectAll(params);
        if (templates.stream().filter(p -> p.getProjectType().equalsIgnoreCase(projectType)).anyMatch(p -> p.getStageName().equalsIgnoreCase(stageName))) {
            templateId = templates.stream()
                    .filter(p -> p.getProjectType().equalsIgnoreCase(projectType))
                    .filter(p -> p.getStageName().equalsIgnoreCase(stageName))
                    .sorted(Comparator.comparing(EdConfigPlanTemplate::getTemplateVersion).reversed()).findFirst().get().getId();
        } else if (templates.stream().filter(p -> p.getProjectType().contains(projectType)).anyMatch(p -> p.getStageName().contains(stageName))) {
            templateId = templates.stream()
                    .filter(p -> p.getProjectType().contains(projectType))
                    .filter(p -> p.getStageName().contains(stageName))
                    .sorted(Comparator.comparing(EdConfigPlanTemplate::getTemplateVersion).reversed()).findFirst().get().getId();
        }

        params.put("templateId", templateId);
        return edConfigPlanDetailMapper.selectAll(params).stream().sorted(Comparator.comparing(EdConfigPlanDetail::getSeq)).collect(Collectors.toList());
    }

}

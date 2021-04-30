package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdArrangePlanMapper;
import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.entity.CommonEdArrangePlan;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonEdArrangePlanService {

    @Resource
    CommonEdArrangePlanMapper commonEdArrangePlanMapper;

    @Resource
    CommonFormDataMapper commonFormDataMapper;

    @Resource
    CommonEdBuildService commonEdBuildService;

    @Resource
    CommonCodeService  commonCodeService;


    /**
     * 得到设计计划
     * @param businessKey
     * @param buildBusinessKey 便于没有的情况下进行自动生成 更新
     * @return
     */
    public List<CommonEdArrangePlan> listData(String businessKey,String buildBusinessKey) {
        CommonFormData commonFormData = commonFormDataMapper.selectByBusinessKey(businessKey);
        String tenetId = commonFormData.getTenetId();
        Date now = new Date();
        List<CommonCodeDto> codes = commonCodeService.listDataByCatalog(tenetId, "设计进度计划");
        List<CommonEdBuild> buildList = commonEdBuildService.listData(buildBusinessKey);


        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", businessKey);
        List<CommonEdArrangePlan> plans = commonEdArrangePlanMapper.selectAll(params);
        plans.stream().filter(p -> buildList.stream().noneMatch(o -> o.getId().equals(p.getBuildId()))).forEach(p -> {
            p.setDeleted(true);
            commonEdArrangePlanMapper.updateByPrimaryKey(p);
        });


        for (CommonEdBuild build : buildList) {
            params.put("buildId", build.getId());
            if (plans.stream().noneMatch(p -> p.getBuildId().equals(build.getId()))) {
                for (CommonCodeDto codeDto : codes) {
                    CommonEdArrangePlan commonEdArrangePlan = new CommonEdArrangePlan();
                    commonEdArrangePlan.setBuildId(build.getId());
                    commonEdArrangePlan.setBuildName(build.getBuildName());
                    commonEdArrangePlan.setPlanArea(build.getBuildArea());
                    commonEdArrangePlan.setBusinessKey(businessKey);
                    commonEdArrangePlan.setDeleted(false);
                    commonEdArrangePlan.setGmtCreate(now);
                    commonEdArrangePlan.setGmtModified(now);
                    commonEdArrangePlan.setSeq(codeDto.getSeq());
                    commonEdArrangePlan.setPlanName(codeDto.getName());
                    commonEdArrangePlan.setPlanTip(codeDto.getRemark());
                    ModelUtil.setNotNullFields(commonEdArrangePlan);
                    commonEdArrangePlanMapper.insert(commonEdArrangePlan);
                }
            }
        }
        params.remove("buildId");
        return commonEdArrangePlanMapper.selectAll(params);
    }

    public void save(int id,String planName,String planArea,String planDesc){
        CommonEdArrangePlan item=commonEdArrangePlanMapper.selectByPrimaryKey(id);
        if(!item.getPlanName().equalsIgnoreCase(planName)||!item.getPlanDesc().equalsIgnoreCase(planDesc)||!item.getPlanArea().equalsIgnoreCase(planArea)) {
            item.setPlanName(planName);
            item.setPlanDesc(planDesc);
            item.setPlanArea(planArea);
            item.setGmtModified(new Date());
            ModelUtil.setNotNullFields(item);
            commonEdArrangePlanMapper.updateByPrimaryKey(item);
        }
    }

    public void setSeq(int id,boolean up) {
        CommonEdArrangePlan item = commonEdArrangePlanMapper.selectByPrimaryKey(id);
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey", item.getBusinessKey());
        params.put("buildName", item.getBuildName());
        List<CommonEdArrangePlan> list= commonEdArrangePlanMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonEdArrangePlan::getSeq)).collect(Collectors.toList());
        Assert.state(list.size()>1,"当前为唯一数据,无需排序!");
        if(up){
            Assert.state(list.stream().anyMatch(p->p.getSeq().compareTo(item.getSeq())<0),"该数据处于最上层!");
            CommonEdArrangePlan changeItem=list.stream().filter(p->p.getSeq().compareTo(item.getSeq())<0).sorted(Comparator.comparing(CommonEdArrangePlan::getSeq).reversed()).findFirst().get();
            int newSeq=changeItem.getSeq();
            changeItem.setSeq(item.getSeq());
            commonEdArrangePlanMapper.updateByPrimaryKey(changeItem);
            item.setSeq(newSeq);
        } else{
            Assert.state(list.stream().anyMatch(p->p.getSeq().compareTo(item.getSeq())>0),"该数据处于最底层!");
            CommonEdArrangePlan changeItem=list.stream().filter(p->p.getSeq().compareTo(item.getSeq())>0).findFirst().get();
            int newSeq=changeItem.getSeq();
            changeItem.setSeq(item.getSeq());
            commonEdArrangePlanMapper.updateByPrimaryKey(changeItem);
            item.setSeq(newSeq);
        }
        commonEdArrangePlanMapper.updateByPrimaryKey(item);
    }

    public int getNewModel(String businessKey,String buildBusinessKey,String  buildName){
        Date now=new Date();

        CommonEdArrangePlan item=new CommonEdArrangePlan();
        item.setBuildName(buildName);
        item.setBuildId(0);

        List<CommonEdBuild> buildList = commonEdBuildService.listData(buildBusinessKey);
        if(buildList.stream().anyMatch(p->p.getBuildName().equalsIgnoreCase(buildName))){
            CommonEdBuild build=buildList.stream().filter(p->p.getBuildName().equalsIgnoreCase(buildName)).findFirst().get();
            item.setBuildId(build.getId());
            item.setPlanArea(build.getBuildArea());
        }
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("businessKey",businessKey);
        params.put("buildName", buildName);

        List<CommonEdArrangePlan> preList=commonEdArrangePlanMapper.selectAll(params);
        int seq=1;
        if(preList.size()>0){
            seq= preList.stream().max(Comparator.comparing(CommonEdArrangePlan::getSeq)).get().getSeq()+1;
        }
        item.setBusinessKey(businessKey);
        item.setSeq(seq);
        item.setGmtModified(now);
        item.setGmtCreate(now);
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        commonEdArrangePlanMapper.insert(item);
        return item.getId();
    }



    public void remove(int id,String enLogin){
        CommonEdArrangePlan item=commonEdArrangePlanMapper.selectByPrimaryKey(id);
        item.setGmtModified(new Date());
        item.setDeleted(true);
        commonEdArrangePlanMapper.updateByPrimaryKey(item);
    }




}

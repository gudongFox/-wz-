package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdBuildMapper;
import com.cmcu.common.entity.CommonEdBuild;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.google.common.collect.Maps;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonEdBuildService {


    @Resource
    CommonEdBuildMapper commonEdBuildMapper;
    @Resource
    CommonEdArrangeUserService commonEdArrangeUserService;

    public List<CommonEdBuild> listData(String businessKey){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("businessKey",businessKey);
        return commonEdBuildMapper.selectAll(params);
    }

    public CommonEdBuild getModelById(int id){
        return commonEdBuildMapper.selectByPrimaryKey(id);
    }

    public int getCount(String businessKey){
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("businessKey",businessKey);
        return commonEdBuildMapper.selectAll(params).size();
    }

    public void save(CommonEdBuild item) {
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        Date now = new Date();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("buildName", item.getBuildName());
        params.put("businessKey", item.getBusinessKey());
        List<CommonEdBuild> builds = commonEdBuildMapper.selectAll(params);
        if (item.getId() == null || item.getId() == 0) {
            Assert.state(builds.size() == 0, item.getBuildName() + "名称已存在!");
            item.setGmtCreate(now);
            item.setGmtModified(now);
            commonEdBuildMapper.insert(item);
        } else {
            Assert.state(builds.size() == 0 || builds.get(0).getId().equals(item.getId()), item.getBuildName() + "名称已存在!");
            item.setGmtModified(now);
            commonEdBuildMapper.updateByPrimaryKey(item);
        }
        commonEdArrangeUserService.buildCoDirData(Integer.valueOf(item.getBusinessKey()),item.getBusinessKey());
    }

    public void remove(int id,String enLogin){
        CommonEdBuild commonEdBuild = commonEdBuildMapper.selectByPrimaryKey(id);
        commonEdBuild.setDeleted(true);
        commonEdBuild.setGmtModified(new Date());
        commonEdBuildMapper.updateByPrimaryKey(commonEdBuild);


        commonEdArrangeUserService.buildCoDirData(Integer.valueOf(commonEdBuild.getBusinessKey()),commonEdBuild.getBusinessKey());
    }

    public CommonEdBuild getNewModel(String businessKey){
        Date now=new Date();
        if(getCount(businessKey)==0){
            CommonEdBuild item=new CommonEdBuild();
            item.setDeleted(false);
            item.setGmtModified(now);
            item.setGmtCreate(now);
            item.setBusinessKey(businessKey);
            item.setSeq(1);
            ModelUtil.setNotNullFields(item);
            return item;
        }else {
            List<CommonEdBuild> preList = listData(businessKey);
            CommonEdBuild item = preList.get(preList.size() - 1);
            item.setId(0);
            item.setSeq(item.getSeq() + 1);
            item.setGmtModified(now);
            item.setGmtCreate(now);
            item.setBuildName("");
            item.setBuildNo("");
            item.setBuildArea("");
            return item;
        }
    }

}

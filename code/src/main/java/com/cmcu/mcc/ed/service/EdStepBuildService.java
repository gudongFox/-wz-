package com.cmcu.mcc.ed.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.FileUtil;
import com.cmcu.mcc.ed.dao.EdProjectStepMapper;
import com.cmcu.mcc.ed.dao.EdStepBuildMapper;
import com.cmcu.mcc.ed.dto.EdStepBuildDto;
import com.cmcu.mcc.ed.entity.EdProjectStep;
import com.cmcu.mcc.ed.entity.EdStepBuild;
import com.cmcu.mcc.sys.service.SysConfigService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EdStepBuildService {

    @Resource
    EdStepBuildMapper edStepBuildMapper;

    @Resource
    EdProjectStepMapper edProjectStepMapper;



    @Autowired
    SysConfigService sysConfigService;

    public List<EdStepBuildDto> listDataByStepId(int stepId) {
        List<EdStepBuildDto> list= Lists.newArrayList();
        Map params= Maps.newHashMap();
        params.put("stepId",stepId);
        params.put("deleted",false);
        List<EdStepBuild> oList= edStepBuildMapper.selectAll(params);
        oList.forEach(p->list.add(getDto(p)));
        return list;
    }

    public void insert(EdStepBuild item){
        BeanValidator.check(item);
        edStepBuildMapper.insert(item);
    }


    public void checkChangeBuild(int stepId){
        //检测这个stepId下面有没有设计变更子项,么有就加一个,seq 99
        if(sysConfigService.getConfig().getAppCode().contains("mcc")) {
            Map params = Maps.newHashMap();
            params.put("stepId", stepId);
            params.put("deleted", false);
            List<EdStepBuild> edStepBuilds = edStepBuildMapper.selectAll(params);
            List<String> builds = new ArrayList<>();
            for (EdStepBuild edStepBuild : edStepBuilds) {
                builds.add(edStepBuild.getBuildName());
            }
            if (!builds.contains("设计变更")) {
                EdStepBuild item = new EdStepBuild();
                item.setStepId(stepId);
                item.setBuildName("设计变更");
                item.setBuildNo("设计变更");
                item.setGmtCreate(new Date());
                item.setGmtModified(new Date());
                item.setDeleted(false);
                item.setRemark("设计变更文件夹");
                item.setSeq(99);

                update(item);
            }
        }

    }


    public void update(EdStepBuild item){
        BeanValidator.check(item);
        EdStepBuild model=new EdStepBuild();
        boolean updateDir=true;
        if(item.getId()!=null&&item.getId()>0) {
            model= edStepBuildMapper.selectByPrimaryKey(item.getId());
            if(model.getSeq().equals(item.getSeq())&&model.getBuildName().equals(item.getBuildName())){
                updateDir=false;
            }
        }
        model.setStepId(item.getStepId());
        model.setBuildName(FileUtil.getGoodName(item.getBuildName()));
        model.setBuildNo(item.getBuildNo());
        model.setSeq(item.getSeq());
        model.setRemark(item.getRemark());
        model.setGmtCreate(item.getGmtCreate());
        model.setGmtModified(new Date());
        model.setDeleted(false);
        if(model.getId()!=null&&model.getId()>0) {
            edStepBuildMapper.updateByPrimaryKey(model);
        }else{
            edStepBuildMapper.insert(model);
        }
        if(updateDir){
            EdProjectStep edProjectStep=edProjectStepMapper.selectByPrimaryKey(item.getStepId());
        }
    }

    public void remove(int id) {
        EdStepBuild item=edStepBuildMapper.selectByPrimaryKey(id);
        EdStepBuildDto dto=getDto(item);
        Assert.state(dto.getSize()==0,"该子项存在文件"+dto.getSizeName()+",请先删除文件!");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        edStepBuildMapper.updateByPrimaryKey(item);
        //重新生成协同文件夹
        //EdProjectStep edProjectStep=edProjectStepMapper.selectByPrimaryKey(item.getStepId());
        //cpDirService.buildDir(edProjectStep.getId());
    }

    public EdStepBuild getModelById(int id) {
        return edStepBuildMapper.selectByPrimaryKey(id);
    }


    public EdStepBuild getNewModel(int stepId){
        Map params= Maps.newHashMap();
        params.put("stepId",stepId);
        params.put("deleted",false);
        int seq=(int) PageHelper.count(()->edStepBuildMapper.selectAll(params))+1;
        EdStepBuild edStepBuild=new EdStepBuild();
        edStepBuild.setId(0);
        edStepBuild.setSeq(seq);
        edStepBuild.setStepId(stepId);
        edStepBuild.setBuildName("");
        edStepBuild.setBuildNo("");
        edStepBuild.setSeq(seq);
        edStepBuild.setDeleted(false);
        edStepBuild.setGmtCreate(new Date());
        edStepBuild.setGmtModified(new Date());
        edStepBuild.setRemark("");
        //检查是否存在 设计变更子项
        checkChangeBuild(edStepBuild.getStepId());
        return edStepBuild;
    }


    private EdStepBuildDto getDto(EdStepBuild item){
        EdStepBuildDto dto=EdStepBuildDto.adapt(item);
        //计算子项文件夹大小
        long size=0;
      /*  try {
            Map params = Maps.newHashMap();
            params.put("stepId", dto.getStepId());
            params.put("locked", true);
            params.put("deleted", false);
            params.put("name", "作图区");
            if (PageHelper.count(() -> cpDirService.cpDirMapper.selectAll(params)) == 1) {
                CpDir writeDir = cpDirService.cpDirMapper.selectAll(params).get(0);
                params.remove("name");
                params.put("parentId", writeDir.getId());
                for (CpDir majorDir : cpDirService.cpDirMapper.selectAll(params)) {
                    if (majorDir.getName().equalsIgnoreCase("00参照")) {
                        params.put("parentId", majorDir.getId());
                        for (CpDir majorReferDir : cpDirService.cpDirMapper.selectAll(params)) {
                            params.put("parentId", majorReferDir.getId());
                            params.put("name", dto.getBuildName());
                            if (PageHelper.count(() -> cpDirService.cpDirMapper.selectAll(params)) == 1) {
                                CpDir buildDir = cpDirService.cpDirMapper.selectAll(params).get(0);
                                size = buildDir.getSize() + size;
                            }
                        }
                    } else {
                        params.put("parentId", majorDir.getId());
                        params.put("name", dto.getBuildName());
                        if (PageHelper.count(() -> cpDirService.cpDirMapper.selectAll(params)) == 1) {
                            CpDir buildDir = cpDirService.cpDirMapper.selectAll(params).get(0);
                            size = buildDir.getSize() + size;
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }*/
        dto.setSize(size);
        dto.setSizeName(FileUtil.getFileSizeName(size));
        return dto;
    }


}

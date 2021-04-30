package com.cmcu.common.service;

import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.dao.CommonCadPluginMapper;
import com.cmcu.common.dto.FastUserDto;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonCadPlugin;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class CommonCadPluginService {
    @Resource
    CommonCadPluginMapper commonCadPluginMapper;
    @Resource
    CommonUserService commonUserService;
    @Resource
    CommonAttachMapper commonAttachMapper;

    public CommonCadPlugin getLatest(String tenetId,String versionType) {
        CommonCadPlugin item = commonCadPluginMapper.getLatest(tenetId,versionType);
        return item;
    }

    public PageInfo<CommonCadPlugin> listPagedData(Map<String,Object> params,String enLogin ,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("tenetId",commonUserService.getTenetId(enLogin));
        PageInfo<CommonCadPlugin> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonCadPluginMapper.selectAll(params));
        return pageInfo;
    }

    public CommonCadPlugin getNewModel(String enLogin) {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        FastUserDto userDto = commonUserService.getFastByEnLogin(enLogin);
        CommonCadPlugin item=new CommonCadPlugin();
        item.setAttachId(0);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setVersionCode("0.0.0");
        item.setVersionType("patch");
        item.setSizeName("0B");
        item.setId(0);

        item.setCreatorName(userDto.getCnName());
        item.setTenetId(userDto.getTenetId());
        return item;
    }

    public void update(CommonCadPlugin commonCadPlugin){
        Assert.state(commonCadPlugin.getAttachId()>0,"请上传更新包!");
        commonCadPlugin.setSizeName(FileUtil.getFileSizeName(commonAttachMapper.selectByPrimaryKey(commonCadPlugin.getAttachId()).getSize()));
        commonCadPlugin.setGmtModified(new Date());
        BeanValidator.check(commonCadPlugin);
        if (commonCadPlugin.getId()==null||commonCadPlugin.getId()==0){
            commonCadPluginMapper.insert(commonCadPlugin);
        }else {
            commonCadPluginMapper.updateByPrimaryKey(commonCadPlugin);
        }

    }

    public void remove(int id) {
        CommonCadPlugin commonCadPlugin = commonCadPluginMapper.selectByPrimaryKey(id);
        commonCadPlugin.setDeleted(true);
        commonCadPlugin.setGmtModified(new Date());
        commonCadPluginMapper.updateByPrimaryKey(commonCadPlugin);
    }

    public CommonCadPlugin getModelById(int id){
        return commonCadPluginMapper.selectByPrimaryKey(id);
    }

}

package com.cmcu.mcc.sys.service;

import com.cmcu.common.dao.CommonAttachMapper;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.sys.dao.SysPluginMapper;
import com.cmcu.mcc.sys.entity.SysPlugin;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class SysPluginService {


    @Resource
    SysPluginMapper sysPluginMapper;

    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    CommonAttachMapper commonAttachMapper;



    public SysPlugin getLatest(){
        SysPlugin sysPlugin=sysPluginMapper.getLatest();
        if(sysPlugin==null){
            sysPlugin=new SysPlugin();
            sysPlugin.setAttachId(0);
            sysPlugin.setZipName("无");
        }
        return sysPlugin;
    }

    public PageInfo<SysPlugin> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        params.put("deleted",false);
        PageInfo<SysPlugin> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> sysPluginMapper.selectAll(params));
        return pageInfo;
    }

    public int getNewModel(String userLogin) {
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        SysPlugin sysPlugin=new SysPlugin();
        sysPlugin.setAttachId(0);
        sysPlugin.setGmtCreate(new Date());
        sysPlugin.setGmtModified(new Date());
        sysPlugin.setDeleted(false);
        sysPlugin.setVersionCode("0.0.0");
        sysPlugin.setSizeName("0B");
        sysPlugin.setCreatorName(hrEmployeeMapper.getNameByUserLogin(userLogin));
        ModelUtil.setNotNullFields(sysPlugin);
        sysPluginMapper.insert(sysPlugin);
        return sysPlugin.getId();
    }


    public void update(SysPlugin sysPlugin) {
        Assert.state(sysPlugin.getAttachId() > 0, "请上传更新包!");
        sysPlugin.setSizeName(FileUtil.getFileSizeName(commonAttachMapper.selectByPrimaryKey(sysPlugin.getAttachId()).getSize()));
        sysPlugin.setGmtModified(new Date());
        BeanValidator.check(sysPlugin);
        sysPluginMapper.updateByPrimaryKey(sysPlugin);
    }


    public void remove(int id) {
        SysPlugin sysPlugin = sysPluginMapper.selectByPrimaryKey(id);
        sysPlugin.setDeleted(true);
        sysPlugin.setGmtModified(new Date());
        sysPluginMapper.updateByPrimaryKey(sysPlugin);
    }
    public SysPlugin getModelById(int id){
        return sysPluginMapper.selectByPrimaryKey(id);
    }

}

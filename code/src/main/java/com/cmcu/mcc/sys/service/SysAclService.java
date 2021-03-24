package com.cmcu.mcc.sys.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyStringUtil;
import com.cmcu.mcc.sys.dao.SysAclMapper;
import com.cmcu.mcc.sys.dao.SysAclModuleMapper;
import com.cmcu.mcc.sys.dto.SysAclDto;
import com.cmcu.mcc.sys.entity.SysAcl;
import com.cmcu.mcc.sys.entity.SysAclModule;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Roy
 * Time: 2019/2/26 16:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysAclService {

    @Resource
    SysAclMapper sysAclMapper;

    @Resource
    SysAclModuleMapper sysAclModuleMapper;




    public void insert(SysAcl item){
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        sysAclMapper.insert(item);
    }

    public void update(SysAcl item){
        BeanValidator.check(item);
        sysAclMapper.updateByPrimaryKey(item);
    }

    public SysAclDto getModelById(int id) {
        SysAcl item = sysAclMapper.selectByPrimaryKey(id);
        return getDto(item);
    }

    public SysAclDto getNewModel(int aclModuleId) {
        Map<String, Object> deptParam = Maps.newHashMap();
        deptParam.put("isDeleted", false);
        deptParam.put("aclModuleId", aclModuleId);
        long total = PageHelper.count(() -> sysAclMapper.selectAll(deptParam));
        SysAclDto dto = new SysAclDto();
        dto.setAclModuleId(aclModuleId);
        dto.setDeleted(false);
        dto.setSeq((int) (total + 1));
        dto.setCode("");
        dto.setName("");
        dto.setUiSref("");
        dto.setUrl("");
        dto.setType(1);
        SysAclModule parent = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        dto.setAclModuleName(parent.getName());
        return dto;
    }



    public PageInfo<SysAclDto> listPagedData(Map<String,Object> params, int pageNum, int pageSize){
        PageInfo<SysAclDto> info=PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->sysAclMapper.selectAll(params));
        return info;
    }


    public List<SysAclDto> listAclByModules(List<Integer> aclModuleIds,String q){
        Map params=Maps.newHashMap();
        params.put("aclModuleIds",aclModuleIds);
        params.put("q",q);
        params.put("deleted",false);
        List<SysAclDto> list=sysAclMapper.selectAll(params);
        return list;
    }



    private SysAclDto getDto(SysAcl item){
        SysAclDto dto = SysAclDto.adapt(item);
        SysAclModule parent = sysAclModuleMapper.selectByPrimaryKey(item.getAclModuleId());
        dto.setAclModuleName(parent.getName());
        dto.setOptList(MyStringUtil.getStringList(dto.getOpts()));
        return dto;
    }




}

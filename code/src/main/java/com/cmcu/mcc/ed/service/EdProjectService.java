package com.cmcu.mcc.ed.service;

import com.cmcu.common.util.BeanValidator;
import com.cmcu.mcc.ed.dao.EdProjectMapper;
import com.cmcu.mcc.ed.dto.EdProjectDto;
import com.cmcu.mcc.ed.entity.EdProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EdProjectService {


    @Resource
    EdProjectMapper edProjectMapper;




    public PageInfo<Object> listPagedData(Map<String,Object> params, int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> edProjectMapper.selectAll(Maps.newHashMap()));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            EdProject item=(EdProject)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }



    public PageInfo<Object> listAttendProject(String userLogin,int pageNum, int pageSize) {
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> edProjectMapper.listAttendProject(userLogin));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            EdProject item=(EdProject)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }




    public void insert(EdProject item){
        BeanValidator.check(item);
        edProjectMapper.insert(item);
    }

    public void update(EdProject item){
        item.setGmtModified(new Date());
        BeanValidator.check(item);
        edProjectMapper.updateByPrimaryKey(item);
    }

    public void remove(int id) {
        EdProject item=edProjectMapper.selectByPrimaryKey(id);

        item.setDeleted(true);
        item.setGmtModified(new Date());
        edProjectMapper.deleteByPrimaryKey(id);


    }

    public EdProjectDto getModelById(int id) {
        return getDto(edProjectMapper.selectByPrimaryKey(id));
    }

    private EdProjectDto getDto(EdProject item) {
        EdProjectDto dto = EdProjectDto.adapt(item);
        //dto.setDeptName(sysDeptMapper.selectByPrimaryKey(item.getDeptId()).getName());
        return dto;
    }



}

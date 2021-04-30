package com.cmcu.common.service;

import com.cmcu.common.dao.CommonExportMapper;
import com.cmcu.common.dto.DeptDto;
import com.cmcu.common.entity.CommonExport;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonExportService {


    @Resource
    CommonExportMapper commonExportMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    JdbcTemplate jdbcTemplate;



    public List<CommonExport> listData(String enLogin) {
        String tenetId = commonUserService.getTenetId(enLogin);
        Map params = Maps.newHashMap();
        params.put("tenetId", tenetId);
        params.put("deleted", false);
        return commonExportMapper.selectAll(params);
    }


    public void save(CommonExport item) {
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        if (item.getId() > 0) {
            commonExportMapper.updateByPrimaryKey(item);
        }else {
            commonExportMapper.insert(item);
        }
    }

    public void remove(int id,String enLogin){
        String tenetId = commonUserService.getTenetId(enLogin);
        CommonExport commonExport=commonExportMapper.selectByPrimaryKey(id);
        if(commonExport.getTenetId().equalsIgnoreCase(tenetId)) {
            commonExport.setDeleted(true);
            commonExport.setGmtModified(new Date());
            commonExportMapper.updateByPrimaryKey(commonExport);
        }
    }

    public CommonExport getModelById(int id){
        return commonExportMapper.selectByPrimaryKey(id);
    }

    public CommonExport getNewModel(String enLogin) {
        String tenetId = commonUserService.getTenetId(enLogin);
        Map params=Maps.newHashMap();
        params.put("tenetId",tenetId);

        CommonExport model = new CommonExport();
        model.setId(0);
        model.setTenetId(tenetId);
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());
        model.setDeleted(false);
        model.setSeq((int)PageHelper.count(()->commonExportMapper.selectAll(params)) + 1);

        ModelUtil.setNotNullFields(model);
        return model;
    }




    public List<Map>  listMapData(int id) {
        List<Map> list = Lists.newArrayList();
        CommonExport export = commonExportMapper.selectByPrimaryKey(id);
        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(export.getSqlText());
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return list;
    }
    public List<Map>  listSelectData(int id,Map params) {
        List<Map> list = Lists.newArrayList();
        CommonExport export = commonExportMapper.selectByPrimaryKey(id);
        Map<String,Object> map=Maps.newHashMap();
        try {
            String sql=export.getSqlText();
            if (params.containsKey("deptId")){
                DeptDto deptId = commonUserService.getDeptByName(params.get("deptId").toString());
                sql=sql+" and dept_id="+deptId.getId();
            }
            if (params.containsKey("startTime")){
                sql=sql+" and gmt_create>="+params.get("startTime").toString();
            }
            if (params.containsKey("endTime")){
                sql=sql+" and gmt_create<= "+params.get("endTime").toString();
            }
            if (params.containsKey("creator")){
                sql += " and creator_name=" + "'" + params.get("creator").toString()+ "'";
            }
            sql+=";";
            System.out.println(sql);
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

}

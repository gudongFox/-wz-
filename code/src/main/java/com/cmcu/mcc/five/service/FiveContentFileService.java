package com.cmcu.mcc.five.service;

import com.cmcu.act.service.ProcessQueryService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.util.FileUtil;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.five.dao.FiveContentFileMapper;
import com.cmcu.mcc.five.dto.FiveContentFileDto;
import com.cmcu.mcc.five.entity.FiveContentFile;
import com.cmcu.mcc.five.entity.FiveOaNonSecretEquipmentScrap;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FiveContentFileService extends BaseService {
    @Resource
    FiveContentFileMapper fiveContentFileMapper;
    @Autowired
    HrEmployeeService hrEmployeeService;

    @Autowired
    ProcessQueryService processQueryService;


    public void remove(int id) {
        FiveContentFile item = fiveContentFileMapper.selectByPrimaryKey(id);
        item.setDelete(true);
        fiveContentFileMapper.updateByPrimaryKey(item);
    }

    public void removeOther(int id){
        FiveContentFile item = fiveContentFileMapper.selectByPrimaryKey(id);
        Map params= Maps.newHashMap();
        params.put("businessKey", item.getBusinessKey());
        params.put("fileType",item.getFileType());
        params.put("delete",false);
      if(fiveContentFileMapper.selectAll(params).size()>1){
          FiveContentFile fiveContentFile = fiveContentFileMapper.selectAll(params).get(1);
          fiveContentFile.setDelete(true);
          fiveContentFileMapper.updateByPrimaryKey(fiveContentFile);
      }

    }

    public int Add(FiveContentFileDto fiveContentFileDto) {

        int next=GetSeqByKey(fiveContentFileDto.getTableName(),fiveContentFileDto.getTableKey());
        FiveContentFile model=new FiveContentFile();
        model.setFileName(fiveContentFileDto.getFileName());
        model.setFileType(fiveContentFileDto.getFileType());
        model.setTableName(fiveContentFileDto.getTableName());
        model.setTableKey(fiveContentFileDto.getTableKey());
        model.setBusinessKey(fiveContentFileDto.getBusinessKey());
        model.setDelete(false);
        model.setCreator(fiveContentFileDto.getCreator());
        model.setCreatorName(hrEmployeeService.getModelByUserLogin(fiveContentFileDto.getCreator()).getUserName());
        model.setSize(fiveContentFileDto.getSize());
        model.setRemark("");
        model.setSeq(next);
        model.setGmtCreate(new Date());
        model.setLocalPath(fiveContentFileDto.getLocalPath());
        ModelUtil.setNotNullFields(model);
        fiveContentFileMapper.insert(model);
        return model.getId();
    }

    public PageInfo<Object> listPagedData(Map<String, Object> params, String userLogin, String uiSref, int pageNum, int pageSize) {
        params.put("deleted", false);
        params.put("isLikeSelect",true);

        Map map =new HashMap();
        map.put("myDeptData",false);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveContentFileMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveContentFile item = (FiveContentFile) p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public FiveContentFile getModelById(int id) {
        return fiveContentFileMapper.selectByPrimaryKey(id);
    }

    public List<FiveContentFile> GetFilesByKey(String tableName,int tableKey){
        Map params= Maps.newHashMap();
        params.put("tableName",tableName);
        params.put("tableKey",tableKey);
        List<FiveContentFile> orderedList=fiveContentFileMapper.selectAll(params).stream().sorted(Comparator.comparing(FiveContentFile::getSeq).reversed()).collect(Collectors.toList());;
        return  orderedList;
    }

    //通过业务表名 表Id 获取顺序号
    public int GetSeqByKey(String tableName,int tableKey){
        Map params= Maps.newHashMap();
        params.put("tableName",tableName);
        params.put("tableKey",tableKey);
        List<FiveContentFile> orderedList=fiveContentFileMapper.selectAll(params);
        if(orderedList.size()>0){
            int currentSeq=orderedList.get(0).getSeq()+1;
            return currentSeq;
        }
        else
           return  1;
    };


    public  FiveContentFileDto getModelByBusinessKey(String businessKey,int fileType){
        Map params= Maps.newHashMap();
        params.put("businessKey",businessKey);
        params.put("delete",false);
        params.put("fileType",fileType);
        if (fiveContentFileMapper.selectAll(params).size()>0){
            return getDto(fiveContentFileMapper.selectAll(params).get(0));
        }else {
            return new FiveContentFileDto();
        }
    }


    public Map<String,Object> getModelByBusinessKey(String businessKey){
        Map<String,Object> model=Maps.newHashMap();
        Map params= Maps.newHashMap();
        params.put("businessKey",businessKey);
        params.put("delete",false);

        params.put("fileType",1);
        if (fiveContentFileMapper.selectAll(params).size()>0){
            model.put("content",fiveContentFileMapper.selectAll(params).get(0));
        }
        params.put("fileType",0);
        if (fiveContentFileMapper.selectAll(params).size()>0){
            model.put("redhead",fiveContentFileMapper.selectAll(params).get(0));
        }
        return model;
    }

    public FiveContentFileDto getDto(FiveContentFile fiveContentFile){

        FiveContentFileDto dto=FiveContentFileDto.adapt(fiveContentFile);
        dto.setExtensionName(FileUtil.getExtensionName(dto.getFileName()));
        //MyProcessInstance processInstanceDto=myHistoryService.getMyProcessInstance(dto.getProcessInstanceId(),"");
        return dto;
    }

}

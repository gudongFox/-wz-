package com.cmcu.common.service;


import com.cmcu.common.dao.CommonCodeMapper;
import com.cmcu.common.dto.CommonCodeDto;
import com.cmcu.common.entity.CommonCode;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonCodeService {
    @Resource
    CommonCodeMapper commonCodeMapper;

    @Resource
    CommonUserService commonUserService;

    public PageInfo<CommonCode> listPagedData(int pageNum, int pageSize,Map<String,Object> params) {
        PageInfo<CommonCode> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonCodeMapper.selectAll(params));
        return pageInfo;
    }

    public List<CommonCodeDto> listDataByCatalog(String tenetId,String codeCatalog) {
        if(StringUtils.isEmpty(codeCatalog)) return Lists.newArrayList();

        List<CommonCodeDto> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("equalCodeCatalog", codeCatalog);
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonCodeMapper.selectAll(params)) == 0) {
            params.put("tenetId", "common");
        }
        commonCodeMapper.selectAll(params).forEach(p -> list.add(getDto(p)));
        return list;
    }

    /**
     * 查询数据字典 名字
     * @param tenetId
     * @param codeCatalog
     * @param name
     * @return
     */
    public CommonCodeDto getDataByCatalogAndName(String tenetId,String codeCatalog,String name) {
        List<CommonCodeDto> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("name", name);
        params.put("equalCodeCatalog", codeCatalog);
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonCodeMapper.selectAll(params)) == 0) {
            params.put("tenetId", "common");
        }
       // commonCodeMapper.selectAll(params).forEach(p -> list.add(getDto(p)));
        return  getDto(commonCodeMapper.selectAll(params).get(0));
    }

    /**
     * 列表栏查询使用测试 option:[{title:"全部",value:""},{title:"name",value:"name"}]
     * @return
     */
    public List<Map> listSelectTypeByCatalog(String tenetId,String codeCatalog){

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("equalCodeCatalog", codeCatalog);
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonCodeMapper.selectAll(params)) == 0) {
            params.put("tenetId", "common");
        }
        List<Map> selectList=Lists.newArrayList();

        Map mapAll=Maps.newHashMap();
        mapAll.put("title","全部");
        mapAll.put("value","");
        selectList.add(mapAll);
        List<CommonCode> commonCodes = commonCodeMapper.selectAll(params);
        for (CommonCode co:commonCodes){
            Map map=Maps.newHashMap();
            map.put("title",co.getName());
            map.put("value",co.getName());
            selectList.add(map);
        }

        return selectList;
    }

    public List<CommonCodeDto> listDataWithChild(String tenetId,String codeCatalog,String parentCode) {
        List<CommonCodeDto> list=Lists.newArrayList();
        List<CommonCodeDto> all =listDataByCatalog(tenetId,codeCatalog);
        if(all.stream().anyMatch(p->p.getCode().equalsIgnoreCase(parentCode))){
            CommonCodeDto parent=all.stream().filter(p->p.getCode().equalsIgnoreCase(parentCode)).findFirst().get();
            list.add(parent);
            recursiveAddChild(parent,all,list);
        }
        return list;
    }


    private void recursiveAddChild(CommonCodeDto parent,List<CommonCodeDto> all,List<CommonCodeDto> list){
        for(CommonCodeDto child:all.stream().filter(p->p.getParentId().equals(parent.getId())).collect(Collectors.toList())){
            list.add(child);
            recursiveAddChild(child,all,list);
        }
    }







    public CommonCode getModelById(int id){
        return commonCodeMapper.selectByPrimaryKey(id);
    }

    public void save(CommonCode commonCode){
        commonCode.setGmtModified(new Date());
        ModelUtil.setNotNullFields(commonCode);
        BeanValidator.check(commonCode);
        if(commonCode.getId()!=null&&commonCode.getId()>0){
            commonCodeMapper.updateByPrimaryKey(commonCode);
        }else{
            commonCodeMapper.insert(commonCode);
        }

    }

    public void remove(int id,String enLogin){
        CommonCode item=commonCodeMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        commonCodeMapper.updateByPrimaryKey(item);
    }

    public CommonCode getNewModel(String enLogin) {
        CommonCode model =new CommonCode();
        model.setId(0);
        model.setGmtModified(new Date());
        model.setGmtCreate(new Date());
        model.setDeleted(false);
        model.setDefaulted(false);
        model.setCodeType("String");
        String tenetId = commonUserService.getTenetId(enLogin);
        CommonCode latest = commonCodeMapper.getLatest(tenetId);
        if (latest != null) {
            model.setSeq(latest.getSeq() + 1);
            model.setCodeCatalog(latest.getCodeCatalog());
            model.setCodeType(latest.getCodeType());
        }
        model.setTenetId(tenetId);
        ModelUtil.setNotNullFields(model);
        return model;
    }

    public List<CommonCode> listCodeCategory(String tenetId){
        List<CommonCode> list= Lists.newArrayList();
        list.add(new CommonCode("","全部"));
        List<String> codeCatalogList=commonCodeMapper.listCodeCategory(tenetId);
        for(String codeCatalog:codeCatalogList){
            list.add(new CommonCode(codeCatalog,codeCatalog));
        }
        return list;
    }


    /**
     * 如果没有设置默认，则返回第一个
     * @param tenetId
     * @param codeCatalog
     * @return
     */
    public Object selectDefaultCodeValue(String tenetId,String codeCatalog) {
        List<CommonCodeDto> codes = listDataByCatalog(tenetId, codeCatalog);
        if(codes.size()==0) return "";
        if (codes.stream().anyMatch(CommonCode::getDefaulted))
            return codes.stream().filter(CommonCode::getDefaulted).findFirst().get().getCodeValue();
        return codes.stream().sorted(Comparator.comparing(CommonCode::getSeq)).findFirst().get().getCodeValue();
    }



    /**
     * 如果没有设置默认值，则返回空
     * @param tenetId
     * @param codeCatalog
     * @return
     */
    public Object selectCodeValue(String tenetId,String codeCatalog) {
        List<CommonCodeDto> codes = listDataByCatalog(tenetId, codeCatalog);
        if(codes.size()==0) return "";
        if (codes.stream().anyMatch(CommonCode::getDefaulted))
            return codes.stream().filter(CommonCode::getDefaulted).findFirst().get().getCodeValue();
        return "";
    }




    public List<CommonCodeDto> selectAll(String tenetId) {
        List<CommonCodeDto> list = Lists.newArrayList();
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        params.put("tenetId", tenetId);
        if (PageHelper.count(() -> commonCodeMapper.selectAll(params)) == 0) {
            params.put("tenetId", "common");
        }
        commonCodeMapper.selectAll(params).forEach(p -> list.add(getDto(p)));
        return list;
    }


    public void uploadData(List<Map> list,String tenetId){
        for(int i=0;i<list.size();i++) {
            Map map = list.get(i);
            Assert.state(StringUtils.isNotEmpty(map.get(0).toString()) && StringUtils.isNotEmpty(map.get(1).toString()) && StringUtils.isNotEmpty(map.get(2).toString()), "第" + (i + 1) + "行数据,名称、字典、类别存在空值!");
        }

        Date now=new Date();
        Map params=Maps.newHashMap();
        params.put("tenetId",tenetId);
        for(Map map:list){
            String code=map.get(0).toString();
            String name=map.get(1).toString();
            String codeCatalog=map.get(2).toString();
            params.put("codeCatalog",codeCatalog);
            params.put("code",code);
            if(PageHelper.count(()->commonCodeMapper.selectAll(params))==0){
                CommonCode commonCode=new CommonCode();
                commonCode.setTenetId(tenetId);
                commonCode.setName(name);
                commonCode.setCode(code);
                commonCode.setCodeCatalog(codeCatalog);
                commonCode.setCodeType("String");
                commonCode.setSeq(1);
                commonCode.setDefaulted(false);
                commonCode.setDeleted(false);
                commonCode.setGmtModified(now);
                commonCode.setGmtCreate(now);
                ModelUtil.setNotNullFields(commonCode);
                commonCodeMapper.insert(commonCode);
            }else{
                CommonCode commonCode=commonCodeMapper.selectAll(params).get(0);
                if(!name.equalsIgnoreCase(commonCode.getName())||commonCode.getDeleted()) {
                    commonCode.setName(name);
                    commonCode.setDeleted(false);
                    commonCode.setGmtModified(now);
                    commonCodeMapper.updateByPrimaryKey(commonCode);
                }
            }
        }
    }


    private CommonCodeDto getDto(CommonCode item){
        CommonCodeDto dto= CommonCodeDto.adapt(item);
        dto.setCodeValue(dto.getCode());
        if(StringUtils.isNotEmpty(dto.getCodeType())) {
            if (dto.getCodeType().equalsIgnoreCase("String")) {
                dto.setCodeValue(item.getCode());
            } else if (dto.getCodeType().equalsIgnoreCase("Integer")) {
                dto.setCodeValue(Integer.parseInt(item.getCode()));
            } else if (dto.getCodeType().equalsIgnoreCase("Long")) {
                dto.setCodeValue(Long.parseLong(item.getCode()));
            } else if (dto.getCodeType().equalsIgnoreCase("Boolean")) {
                dto.setCodeValue(Boolean.parseBoolean(dto.getCode()));
            }
        }
        return dto;
    }



}

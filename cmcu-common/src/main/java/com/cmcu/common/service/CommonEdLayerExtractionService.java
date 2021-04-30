package com.cmcu.common.service;

import com.cmcu.common.dao.CommonEdLayerExtractionMapper;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonEdLayerExtraction;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommonEdLayerExtractionService {
    @Resource
    CommonEdLayerExtractionMapper commonEdLayerExtractionMapper;

    @Resource
    CommonCodeService commonCodeService;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;

    public PageInfo<CommonEdLayerExtraction> listPagedData(Map<String,Object> params,String enLogin, int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("tenetId",commonUserService.getTenetId(enLogin));
        PageInfo<CommonEdLayerExtraction> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonEdLayerExtractionMapper.selectAll(params));
        return pageInfo;
    }



    public List<CommonEdLayerExtraction> selectAll(String tenetId){
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("tenetId",tenetId);
        return commonEdLayerExtractionMapper.selectAll(params);
    }


    /**
     * 创建私有
     * @param tenetId
     * @param enLogin
     * @param extractName
     * @param extractLayer
     * @param extractDesc
     * @param sourceMajor
     * @param destMajor
     */
    public void insert(String tenetId,String enLogin,String extractName,String extractLayer,String extractDesc,String sourceMajor,String destMajor) {
        Assert.state(StringUtils.isNotEmpty(extractDesc)
                &&StringUtils.isNotEmpty(extractLayer)
                &&StringUtils.isNotEmpty(extractName)
                &&StringUtils.isNotEmpty(sourceMajor),
                "图纸专业、模板名称、提取图层、提取说明不能为空!"
        );

        Map params = Maps.newHashMap();
        params.put("deleted", false);

        params.put("standard",false);
        params.put("creator",enLogin);
        params.put("extractName",extractName);
        if(PageHelper.count(()->commonEdLayerExtractionMapper.selectAll(params))==0) {
            UserDto userDto = commonUserService.selectByEnLogin(enLogin);
            params.put("tenetId", userDto.getTenetId());
            CommonEdLayerExtraction item = new CommonEdLayerExtraction();
            item.setSeq(99);
            item.setTenetId(tenetId);
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            item.setDeleted(false);
            item.setStandard(false);
            item.setExtractName(extractName);
            item.setExtractLayer(extractLayer);
            item.setExtractDesc(extractDesc);
            item.setSourceMajor(sourceMajor);
            item.setDestMajor(destMajor);
            item.setCreator(enLogin);
            item.setCreatorName(userDto.getCnName());
            item.setDeptId(userDto.getDeptId());
            item.setDeptName(userDto.getDeptName());
            ModelUtil.setNotNullFields(item);
            BeanValidator.check(item);
            commonEdLayerExtractionMapper.insert(item);
        }else{
            CommonEdLayerExtraction item =commonEdLayerExtractionMapper.selectAll(params).get(0);
            item.setExtractLayer(extractLayer);
            item.setExtractDesc(extractDesc);
            item.setSourceMajor(sourceMajor);
            item.setDestMajor(destMajor);
            item.setGmtModified(new Date());
            BeanValidator.check(item);
            commonEdLayerExtractionMapper.updateByPrimaryKey(item);
        }
    }



    public CommonEdLayerExtraction getNewModel(String enLogin){
        UserDto userDto = commonUserService.selectByEnLogin(enLogin);
        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("tenetId",userDto.getTenetId());
        CommonEdLayerExtraction item=new CommonEdLayerExtraction();
        item.setSeq((int)PageHelper.count(()->commonEdLayerExtractionMapper.selectAll(params))+1);
        item.setSourceMajor(commonCodeService.selectDefaultCodeValue(userDto.getTenetId(),"设计专业").toString());
        item.setDestMajor(commonCodeService.selectDefaultCodeValue(userDto.getTenetId(),"设计专业").toString());
        item.setTenetId(userDto.getTenetId());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        item.setStandard(true);
        item.setCreator(enLogin);
        item.setCreatorName(userDto.getCnName());
        item.setDeptId(userDto.getDeptId());
        item.setId(0);
        item.setDeptName(userDto.getDeptName());
        ModelUtil.setNotNullFields(item);
        return item;
    }

    public void update(CommonEdLayerExtraction commonEdLayerExtraction){
        commonEdLayerExtraction.setGmtModified (new Date());
        BeanValidator.check(commonEdLayerExtraction);
        if (commonEdLayerExtraction.getId()==0){
            commonEdLayerExtractionMapper.insert(commonEdLayerExtraction);
        }else {
            commonEdLayerExtractionMapper.updateByPrimaryKey(commonEdLayerExtraction);
        }

    }

    public void remove(int id) {
        CommonEdLayerExtraction cpDwgExtraction = commonEdLayerExtractionMapper.selectByPrimaryKey(id);
        cpDwgExtraction.setDeleted(true);
        commonEdLayerExtractionMapper.updateByPrimaryKey(cpDwgExtraction);
    }

    public CommonEdLayerExtraction getModelById(int id) {
        return commonEdLayerExtractionMapper .selectByPrimaryKey(id);
    }

}

package com.cmcu.common.service;


import com.cmcu.common.dao.CommonBlockAttrMapper;
import com.cmcu.common.dao.CommonBlockMapper;
import com.cmcu.common.dao.CommonCategoryMapper;
import com.cmcu.common.dto.UserDto;
import com.cmcu.common.entity.CommonBlock;
import com.cmcu.common.entity.CommonBlockAttr;
import com.cmcu.common.entity.CommonCategory;
import com.cmcu.common.util.ModelUtil;
import com.common.util.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonBlockService {

    @Resource
    CommonBlockMapper commonBlockMapper;

    @Resource
    CommonBlockAttrMapper commonBlockAttrMapper;

    @Resource
    CommonCategoryMapper commonCategoryMapper;

    @Resource
    IUserDataService userDataService;


    public List<CommonBlock> selectAll(int categoryId) {
        Map params = Maps.newHashMap();
        params.put("categoryId", categoryId);
        params.put("deleted", false);
        return commonBlockMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonBlock::getSeq)).collect(Collectors.toList());
    }

    public int insert(int categoryId, String fileName, int attachId, int thumbId, int minVersion, int maxVersion, String insertLayer, String enLogin) {
        UserDto userDto = userDataService.selectByEnLogin(enLogin);
        CommonCategory commonCategory = commonCategoryMapper.selectByPrimaryKey(categoryId);
        Date now = new Date();
        CommonBlock item = new CommonBlock();
        item.setCategoryId(categoryId);
        item.setGmtCreate(now);
        item.setGmtModified(now);
        item.setCreator(userDto.getEnLogin());
        item.setCreatorName(userDto.getCnName());
        item.setOwner(userDto.getEnLogin());
        item.setOwnerName(userDto.getCnName());
        item.setOwnerLevel("个人");
        item.setFileName(fileName);
        item.setAttachId(attachId);
        item.setThumbId(thumbId);
        item.setMinVersion(minVersion);
        item.setMaxVersion(maxVersion);
        item.setInsertLayer(insertLayer);
        item.setSeq(99);
        item.setOtherData("{}");
        item.setTenetId(commonCategory.getTenetId());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        commonBlockMapper.insert(item);
        return item.getId();
    }



    public void insertAttrList(int blockId,String attrData) {
        List<CommonBlockAttr> commonBlockAttrs = JsonMapper.string2Obj(attrData, new TypeReference<ArrayList<CommonBlockAttr>>() {/**/
        });

        for(CommonBlockAttr commonBlockAttr:commonBlockAttrs) {
            commonBlockAttr.setDeleted(false);
            commonBlockAttr.setBlockId(blockId);
            ModelUtil.setNotNullFields(commonBlockAttr);
            commonBlockAttrMapper.insert(commonBlockAttr);
        }
    }



}

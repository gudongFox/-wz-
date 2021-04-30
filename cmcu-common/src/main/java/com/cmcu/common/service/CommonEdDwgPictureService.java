package com.cmcu.common.service;


import com.cmcu.common.dao.CommonEdDwgPictureMapper;
import com.cmcu.common.dto.CommonFileDto;
import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.entity.CommonEdDwgPicture;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonEdDwgPictureService {

    @Resource
    GuavaCacheService guavaCacheService;

    @Resource
    CommonEdDwgPictureMapper commonEdDwgPictureMapper;

    @Autowired
    CommonCodeService commonCodeService;
    @Autowired
    CommonAttachService commonAttachService;
    @Autowired
    CommonUserService commonUserService;


    private static String CACHE_KEY="CommonEdDwgPicture_";
    /*插件使用*/
    public List<CommonEdDwgPicture> listData(String tenetId) {
        List<CommonEdDwgPicture> all = guavaCacheService.get(CACHE_KEY + tenetId, () -> {
            Map params = Maps.newHashMap();
            params.put("tenetId", tenetId);
            params.put("deleted", false);
            return Optional.of(commonEdDwgPictureMapper.selectAll(params).stream().sorted(Comparator.comparing(CommonEdDwgPicture::getSeq)).collect(Collectors.toList()));
        });
        return all;
    }

    /**
     * 删除
     * @param id
     */
    public void remove(int id) {
        CommonEdDwgPicture model = commonEdDwgPictureMapper.selectByPrimaryKey(id);
        model.setDeleted(true);
        model.setGmtModified(new Date());
        commonEdDwgPictureMapper.updateByPrimaryKey(model);
        invalidateCache(model.getTenetId());
    }


    public void update(CommonEdDwgPicture item) {
        if (item.getId()!=null&&item.getId()>0){
            CommonEdDwgPicture model = commonEdDwgPictureMapper.selectByPrimaryKey(item.getId());
            model.setAttachId(item.getAttachId());
            model.setPictureName(item.getPictureName());
            model.setPictureSize(item.getPictureSize());
            model.setExpandSize(item.getExpandSize());
            model.setPictureDirection(item.getPictureDirection());
            model.setRemark(item.getRemark());
            model.setSeq(item.getSeq());
            model.setGmtModified(new Date());
            BeanValidator.check(model);
            commonEdDwgPictureMapper.updateByPrimaryKey(model);

        }else {
            item.setGmtModified(new Date());
            commonEdDwgPictureMapper.insert(item);
        }

        invalidateCache(item.getTenetId());
    }

    public CommonEdDwgPicture getModelById(int id){
        return commonEdDwgPictureMapper.selectByPrimaryKey(id);
    }

    public int getNewModel(String tenetId) {
        Map params = Maps.newHashMap();
        params.put("deleted", false);
        int exist = (int) PageHelper.count(() -> commonEdDwgPictureMapper.selectAll(params));
        CommonEdDwgPicture model = new CommonEdDwgPicture();
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setSeq(exist + 1);
        model.setTenetId(tenetId);
        if(exist>0){
            CommonEdDwgPicture latestOne=commonEdDwgPictureMapper.selectLatestOne();
            model.setPictureSize(latestOne.getPictureSize());
            model.setExpandSize(latestOne.getExpandSize());
            model.setPictureName(latestOne.getPictureName());
            model.setPictureDirection(latestOne.getPictureDirection());
        }else{
            model.setPictureName(commonCodeService.selectDefaultCodeValue(tenetId,"图框类别").toString());
            model.setPictureSize(commonCodeService.selectDefaultCodeValue(tenetId,"图框尺寸").toString());
            model.setExpandSize(commonCodeService.selectDefaultCodeValue(tenetId,"图框加长").toString());
            model.setPictureDirection("横式");
        }
        ModelUtil.setNotNullFields(model);
        commonEdDwgPictureMapper.insert(model);
        return model.getId();
    }

    public CommonEdDwgPicture getNewModelLast(String enLogin) {
        String tenetId=commonUserService.getTenetId(enLogin);

        Map params = Maps.newHashMap();
        params.put("deleted", false);
        int exist = (int) PageHelper.count(() -> commonEdDwgPictureMapper.selectAll(params));
        CommonEdDwgPicture model = new CommonEdDwgPicture();
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setSeq(exist + 1);
        model.setTenetId(tenetId);
        model.setId(0);
        model.setPictureName(commonCodeService.selectDefaultCodeValue(tenetId,"图框类别").toString());
        model.setPictureSize(commonCodeService.selectDefaultCodeValue(tenetId,"图框尺寸").toString());
        model.setExpandSize(commonCodeService.selectDefaultCodeValue(tenetId,"图框加长").toString());
        model.setPictureDirection("横式");

        ModelUtil.setNotNullFields(model);
        return model;
    }


    public CommonEdDwgPicture updateAttach(String tenetId,int id,int attachId) {
        CommonEdDwgPicture model=null;
       if (id==0){
          model=new CommonEdDwgPicture();
       }else {
           model = commonEdDwgPictureMapper.selectByPrimaryKey(id);
       }


        CommonAttach commonAttach = commonAttachService.getModelById(attachId);
        model.setAttachId(commonAttach.getId());
        model.setGmtModified(new Date());
        String fileName = commonAttach.getName();

        //五洲图框
        if("wuzhou".equalsIgnoreCase(tenetId)) {
            //图框类别
            if (fileName.contains("JY_")) {
                model.setPictureName("建研院图框");
            } else if (fileName.contains("Standard_")) {
                model.setPictureName("标准图框");
            } else if (fileName.contains("SZ_")) {
                model.setPictureName("市政图框");
            }else if(fileName.contains("SZGC_")){
                model.setPictureName("市政工程图框");
            } else {
                model.setPictureName("其他类");
            }
            //图框方向
            if(fileName.contains("_H")){
                model.setPictureDirection("横式");
            }else if(fileName.contains("_S")){
                model.setPictureDirection("立式");
            }
        }else {
            //图框类别
            if (fileName.contains("变更")) {
                model.setPictureName("变更图框");
            } else if (fileName.contains("sz") || fileName.contains("市政")) {
                model.setPictureName("市政图框");
            } else if (fileName.contains("kc") || fileName.contains("勘察")) {
                if (fileName.contains("平面")) {
                    model.setPictureName("勘察平面图框");
                } else {
                    model.setPictureName("勘察剖面图框");
                }
            } else if (fileName.contains("目录")) {
                model.setPictureName("目录图框");
            } else {
                model.setPictureName("标准图框");
            }
            //图框方向
            model.setPictureDirection(fileName.contains("sA") ? "立式" : "横式");
        }

        //图框尺寸
        if (fileName.contains("A0")) {
            model.setPictureSize("A0");
        } else if (fileName.contains("A1")) {
            model.setPictureSize("A1");
        } else if (fileName.contains("A2")) {
            model.setPictureSize("A2");
        } else if (fileName.contains("A3")) {
            model.setPictureSize("A3");
        }else if(fileName.contains("A4")){
            model.setPictureSize("A4");
        }
        //图框加长
        if(fileName.contains("+4l")){
            model.setExpandSize("4l");
        }else if(fileName.contains("+3.5l")){
            model.setExpandSize("3.5l");
        }else if(fileName.contains("+3l")){
            model.setExpandSize("3l");
        }else if(fileName.contains("+2.75l")){
            model.setExpandSize("2.75l");
        }else if(fileName.contains("+2.5l")){
            model.setExpandSize("2.5l");
        }else if(fileName.contains("+2.25l")){
            model.setExpandSize("2.25l");
        }else if(fileName.contains("+2l")){
            model.setExpandSize("2l");
        }else if(fileName.contains("+1.875l")){
            model.setExpandSize("1.875l");
        } else if(fileName.contains("+1.75l")){
            model.setExpandSize("1.75l");
        }else if(fileName.contains("+1.625l")){
            model.setExpandSize("1.625l");
        }else if(fileName.contains("+1.5l")){
            model.setExpandSize("1.5l");
        }else if(fileName.contains("+1.375l")){
            model.setExpandSize("1.375l");
        }else if(fileName.contains("+1.25l")){
            model.setExpandSize("1.25l");
        }else if(fileName.contains("+1.125l")){
            model.setExpandSize("1.125l");
        } else if(fileName.contains("+l")){
            model.setExpandSize("1l");
        } else if(fileName.contains("+0.75l")){
            model.setExpandSize("0.75l");
        }else if(fileName.contains("+0.5l")){
            model.setExpandSize("0.5l");
        }else if(fileName.contains("+0.25l")){
            model.setExpandSize("0.25l");
        } else {
            model.setExpandSize("0l");
        }
        //五洲其他类图框
        if(model.getPictureName().equalsIgnoreCase("其他类")){
            model.setPictureSize(StringUtils.split(fileName,".")[0].replace("_H","").replace("_S",""));
        }
        commonEdDwgPictureMapper.updateByPrimaryKey(model);

        invalidateCache(tenetId);
        return model;
    }

    public CommonEdDwgPicture getNewModel(String tenetId,int attachId){
        int id=getNewModel(tenetId);
        return updateAttach(tenetId,id,attachId);
    }

    public PageInfo<CommonEdDwgPicture> listPagedData(Map<String,Object> params, String tenetId,int pageNum, int pageSize) {
        params.put("deleted",false);
        params.put("tenetId",tenetId);
        PageInfo<CommonEdDwgPicture> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> commonEdDwgPictureMapper.selectAll(params));
        return pageInfo;
    }

    /**
     * 修改删除后调用
     * @param tenetId
     */
    private void invalidateCache(String tenetId) {
        guavaCacheService.invalidate(CACHE_KEY + tenetId);
    }

}

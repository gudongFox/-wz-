package com.cmcu.mcc.ed.service;

import com.cmcu.mcc.ed.dao.FiveEdDesignDrawingDetailMapper;
import com.cmcu.mcc.ed.dao.FiveEdDesignDrawingMapper;
import com.cmcu.mcc.ed.dto.FiveEdMajorDrawingCheckDto;
import com.cmcu.mcc.ed.entity.FiveEdDesignDrawing;
import com.cmcu.mcc.ed.entity.FiveEdDesignDrawingDetail;
import com.cmcu.mcc.ed.entity.FiveEdMajorDrawingCheckDetail;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FiveEdDesignDrawingService {
    @Resource
    FiveEdDesignDrawingMapper fiveEdDesignDrawingMapper;
    @Resource
    FiveEdDesignDrawingDetailMapper fiveEdDesignDrawingDetailMapper;
    @Resource
    FiveEdMajorDrawingCheckService fiveEdMajorDrawingCheckService;

    public List<FiveEdDesignDrawing> listDataByStepId(int stepId,String userLogin) {

        Map params= Maps.newHashMap();
        params.put("deleted",false);
        params.put("stepId",stepId);
        List<FiveEdDesignDrawing> oList = fiveEdDesignDrawingMapper.selectAll(params);

        return oList;
    }


    public FiveEdDesignDrawing getModelById(int id){
        return fiveEdDesignDrawingMapper.selectByPrimaryKey(id);
    }


    public List<FiveEdDesignDrawingDetail> listDetail(int drawingId){
        Map <String,Object> params=Maps.newHashMap();
        params.put("deleted",false);
        params.put("drawingId",drawingId);

        List<FiveEdDesignDrawingDetail> list = fiveEdDesignDrawingDetailMapper.selectAll(params);
        return list;
    }

    public FiveEdDesignDrawingDetail getModelDetailById(int id){
        return fiveEdDesignDrawingDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 图纸清单跟新 新增
     * @param id
     */
    public void updateModel(int id){
        FiveEdMajorDrawingCheckDto checkDto = fiveEdMajorDrawingCheckService.getModelById(id);
        String[] split = checkDto.getMajorName().split(",");
        //循环专业验收清单 是否包含多个专业
        for (String major:split){
            FiveEdDesignDrawing item=new FiveEdDesignDrawing();
            int realId=0;
            Map params=Maps.newHashMap();
            params.put("deleted",false);
            params.put("stepId",checkDto.getStepId());
            params.put("majorName",major);
            //判断该专业是否已有数据 没有新增 已有更新
            if (fiveEdDesignDrawingMapper.selectAll(params).size()>0){
                item=fiveEdDesignDrawingMapper.selectAll(params).get(0);
                item.setGmtModified(new Date());
                fiveEdDesignDrawingMapper.updateByPrimaryKey(item);
            }else {
                BeanUtils.copyProperties(checkDto,item);

                item.setHandMan("彭东明");
                item.setMajorName(major);
                item.setId(realId);
                item.setDeleted(false);
                item.setGmtCreate(new Date());
                fiveEdDesignDrawingMapper.insert(item);
                item.setBusinessKey("fiveEdDesignDrawing_"+item.getId());
                fiveEdDesignDrawingMapper.updateByPrimaryKey(item);
            }

            List<FiveEdMajorDrawingCheckDetail> list = fiveEdMajorDrawingCheckService.listDetail(checkDto.getId());
            for (FiveEdMajorDrawingCheckDetail detail:list) {
              //判读子项是否和主表同一个专业
                if (detail.getMajorName().equals(major)){
                    Map map=Maps.newHashMap();
                    map.put("deleted",false);
                    map.put("stepId",checkDto.getStepId());
                    map.put("majorName",major);
                    map.put("buildName",detail.getBuildName());
                    map.put("drawNo",detail.getDrawNo());
                    FiveEdDesignDrawingDetail drawingDetail=new FiveEdDesignDrawingDetail();
                    //存在跟新
                  if (fiveEdDesignDrawingDetailMapper.selectAll(map).size()>0) {
                      drawingDetail=fiveEdDesignDrawingDetailMapper.selectAll(map).get(0);
                      BeanUtils.copyProperties(detail,drawingDetail);
                      drawingDetail.setDrawingId(item.getId());
                      drawingDetail.setGmtModified(new Date());
                      drawingDetail.setFormNo(item.getFormNo());
                      fiveEdDesignDrawingDetailMapper.updateByPrimaryKey(drawingDetail);
                  }else {
                      //新增
                      BeanUtils.copyProperties(detail,drawingDetail);
                      drawingDetail.setDrawingId(item.getId());
                      drawingDetail.setGmtCreate(new Date());
                      drawingDetail.setDeleted(false);
                      drawingDetail.setGmtModified(new Date());
                      drawingDetail.setFormNo(item.getFormNo());
                      drawingDetail.setId(0);
                      fiveEdDesignDrawingDetailMapper.insert(drawingDetail);
                  }
                }
            }
        }
    }

}

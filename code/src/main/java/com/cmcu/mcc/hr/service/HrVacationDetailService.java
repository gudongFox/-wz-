package com.cmcu.mcc.hr.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrVacationDetailMapper;
import com.cmcu.mcc.hr.dao.HrVacationMapper;
import com.cmcu.mcc.hr.dto.HrVacationDetailDto;
import com.cmcu.mcc.hr.entity.HrVacation;
import com.cmcu.mcc.hr.entity.HrVacationDetail;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class HrVacationDetailService  {

    @Resource
    HrVacationDetailMapper hrVacationDetailMapper;
    @Resource
    HrVacationMapper hrVacationMapper;
    @Autowired
    CommonCodeService commonCodeService;



    public void remove(int id, String userLogin) {
        HrVacationDetail hrVacationDetail = hrVacationDetailMapper.selectByPrimaryKey(id);
        HrVacation hrVacation = hrVacationMapper.selectByPrimaryKey(hrVacationDetail.getVacationId());
        Assert.state(hrVacation.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        if (hrVacation.getCreator().equals(userLogin)){
            hrVacationDetailMapper.deleteByPrimaryKey(id);
        }
    }


    public HrVacationDetail update(HrVacationDetailDto hrVacationDetailDto) {
        HrVacationDetail item = hrVacationDetailMapper.selectByPrimaryKey(hrVacationDetailDto.getId());
        item.setVacationType(hrVacationDetailDto.getVacationType());
        item.setPlanBegin(hrVacationDetailDto.getPlanBegin());
        item.setPlanEnd(hrVacationDetailDto.getPlanEnd());
        item.setPlanDay(hrVacationDetailDto.getPlanDay());
        item.setActualBegin(hrVacationDetailDto.getActualBegin());
        item.setActualEnd(hrVacationDetailDto.getActualEnd());
        item.setLastLeft(hrVacationDetailDto.getLastLeft());
        item.setGmtModified(new Date());
        hrVacationDetailMapper.updateByPrimaryKey(item);

        return item;
    }


    public HrVacationDetailDto getModelById(int id) {
        return getDto(hrVacationDetailMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(int vacationId) {

        HrVacationDetail item=new HrVacationDetail();
        item.setVacationType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"休假类型").toString());
        item.setVacationId(vacationId);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        hrVacationDetailMapper.insert(item);

        return item.getId();
    }


    public HrVacationDetailDto getDto(Object item) {
        return HrVacationDetailDto.adapt((HrVacationDetail) item);
    }


    public List<HrVacationDetail> listDataByForeignKey(int vacationId) {
        Map<String,Object> params= Maps.newHashMap();
        params.put("vacationId",vacationId);
        List<HrVacationDetail> hrVacationDetails = hrVacationDetailMapper.selectAll(params);
        return hrVacationDetails;
    }



}

package com.cmcu.mcc.oa.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaCarApplyMapper;
import com.cmcu.mcc.oa.dao.OaCarMapper;
import com.cmcu.mcc.oa.dto.OaCarDto;
import com.cmcu.mcc.oa.entity.OaCar;
import com.cmcu.mcc.oa.entity.OaCarApply;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaCarService {

    @Resource
    OaCarMapper oaCarMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    OaCarApplyMapper oaCarApplyMapper;

    public void remove(int id,String userLogin)
    {
        OaCar item = oaCarMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaCarMapper.updateByPrimaryKey(item);
    }

    public void update(OaCarDto oaCarDto)
    {
        OaCar item = oaCarMapper.selectByPrimaryKey(oaCarDto.getId());
        item.setDeptName(oaCarDto.getDeptName());
        item.setCarNo(oaCarDto.getCarNo());
        item.setCarType(oaCarDto.getCarType());
        item.setCarCc(oaCarDto.getCarCc());
        item.setCarPrice(oaCarDto.getCarPrice());
        item.setCarBrand(oaCarDto.getCarBrand());
        item.setCarColor(oaCarDto.getCarColor());
        item.setCarStatus(oaCarDto.getCarStatus());
        item.setGmtModified(new Date());
        item.setBuyDate(oaCarDto.getBuyDate());
        item.setRemark(oaCarDto.getRemark());
        BeanValidator.check(item);
        oaCarMapper.updateByPrimaryKey(item);
    }

    public OaCarDto getDto(OaCar item)
    {
        OaCarDto oaCarDto = OaCarDto.adapt(item);
        return oaCarDto;
    }

    public OaCarDto getModelById(int id)
    {
        return getDto(oaCarMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin)      //新增一条数据
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaCar item = new OaCar();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setCarStatus(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"车辆状态").toString());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
       /* item.setBuyDate(new Date());*/
        item.setCarPrice(BigDecimal.ZERO);

        ModelUtil.setNotNullFields(item);
        oaCarMapper.insert(item);
        item.setBusinessKey("oaCar_"+item.getId());
        oaCarMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {
        //跟新车辆状态
        updateCarState();

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaCarMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaCar item = (OaCar)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<OaCar> listAllCar() throws ParseException {
        //跟新车辆状态
        updateCarState();

        Map params = new HashMap();
        params.put("carStatus","正常");
        params.put("deleted",false);
        List<OaCar> list = oaCarMapper.selectAll(params);
        return list;
    }

    //跟新会议室状态
    public void updateCarState() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map carParams = new HashMap();
        carParams.put("deleted",false);
        List<OaCar> oaCars = oaCarMapper.selectAll(carParams);
        for(OaCar car:oaCars){
            //查询该车辆的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("carId",car.getId());
            params.put("isProcessEnd",true);
            List<OaCarApply> oaCarApplies = oaCarApplyMapper.selectAll(params);
            for(OaCarApply oaCarApply :oaCarApplies){
                //判断当前时间 是否落在申请区域
                if(now.after(simpleDateFormat.parse(oaCarApply.getBeginTime()))&&now.before(simpleDateFormat.parse(oaCarApply.getEndTime()))){
                    //跟新状态为 使用中
                    OaCar oaCar = oaCarMapper.selectByPrimaryKey(car.getId());
                    oaCar.setCarStatus("使用中");
                    oaCarMapper.updateByPrimaryKey(oaCar);
                } else{
                    //跟新状态为 正常 --不用每次修改 待优化
                    OaCar oaCar = oaCarMapper.selectByPrimaryKey(car.getId());
                    oaCar.setCarStatus("正常");
                    oaCarMapper.updateByPrimaryKey(oaCar);
                }
            }
        }


    }


}

package com.cmcu.mcc.hr.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.hr.dao.HrContractMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dto.HrContractDto;
import com.cmcu.mcc.hr.entity.HrContract;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class HrContractService {
    @Resource
    HrContractMapper hrContractMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;
    @Autowired
    CommonCodeService commonCodeService;






    private HrContractDto getDto(HrContract item) {
        HrContractDto dto = HrContractDto.adapt(item);
        HrEmployee hrEmployee=hrEmployeeMapper.selectByUserLoginOrNo(item.getUserLogin());
        dto.setUserName(hrEmployee.getUserName());
        return dto;
    }

    public HrContract getNewModel(String userLogin) {
        Date now = new Date();

        HrContract item = new HrContract();


        item.setUserLogin(userLogin);
        item.setContractType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"合同性质").toString());
        item.setWorkType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"用工形式").toString());

        item.setGmtModified(now);
        item.setGmtCreate(now);

        ModelUtil.setNotNullFields(item);
        hrContractMapper.insert(item);

        return item;
    }

    public void update(HrContractDto item){

        HrContract model=hrContractMapper.selectByPrimaryKey(item.getId());

        model.setUserLogin(item.getUserLogin());
        model.setContractType(item.getContractType());
        model.setWorkType(item.getWorkType());
        model.setContractLocation(item.getContractLocation());
        model.setInsureLocation(item.getInsureLocation());
        model.setContractYear(item.getContractYear());
        model.setBeginTime(item.getBeginTime());
        model.setEndTime(item.getEndTime());
        model.setNoticeTime(item.getNoticeTime());
        model.setReceiveTime(item.getReceiveTime());


        model.setGmtModified(new Date());
        BeanValidator.check(model);
        ModelUtil.setNotNullFields(model);
        hrContractMapper.updateByPrimaryKey(model);
    }

    public void remove(int id) {
        HrContract item = hrContractMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        hrContractMapper.updateByPrimaryKey(item);
    }

    public HrContractDto getModelById(int id) {
        return getDto(hrContractMapper.selectByPrimaryKey(id));
    }


    public List<HrContractDto> listData(String userLogin) {
        Map map=new HashMap();
        map.put("deleted",false);
        map.put("userLogin",userLogin);
        List<HrContract> contracts =hrContractMapper.selectAll(map);
        List<HrContractDto> list = Lists.newArrayList();
        contracts.forEach(p -> list.add(getDto(p)));
        return list;
    }
}

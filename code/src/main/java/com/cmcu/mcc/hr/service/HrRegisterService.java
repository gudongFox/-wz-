package com.cmcu.mcc.hr.service;

import com.cmcu.common.exception.ParamException;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.hr.dao.*;
import com.cmcu.mcc.hr.dto.HrRegisterDto;
import com.cmcu.mcc.hr.entity.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HrRegisterService {
    @Resource
    HrRegisterMapper hrRegisterMapper;
    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    private HrRegisterDto getDto(HrRegister item) {
        HrRegisterDto dto = HrRegisterDto.adapt(item);
        dto.setUserName(hrEmployeeMapper.getNameByUserLogin(item.getUserLogin()));
        return dto;
    }

    public int getNewModel(String userLogin) {
        Date now = new Date();
        HrRegister item = new HrRegister();
        item.setProvince("重庆市");
        item.setCity("九龙坡区");
        item.setUserLogin(userLogin);
        item.setGmtModified(now);
        item.setGmtCreate(now);
        ModelUtil.setNotNullFields(item);
        hrRegisterMapper.insert(item);
        item.setBusinessKey("hrRegister_"+item.getId());
        hrRegisterMapper.updateByPrimaryKey(item);
        return item.getId();
    }

    public void update(HrRegisterDto item) {

        HrRegister model = hrRegisterMapper.selectByPrimaryKey(item.getId());
        model.setProvince(item.getProvince());
        model.setCity(item.getCity());
        model.setSpecialty(item.getSpecialty());
        model.setUserLogin(item.getUserLogin());
        model.setUserName(item.getUserName());
        model.setStartTime(item.getStartTime());
        model.setEndTime(item.getEndTime());
        model.setRemark(item.getRemark());
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        ModelUtil.setNotNullFields(model);
        BeanValidator.check(model);
        hrRegisterMapper.updateByPrimaryKey(model);

    }

    public void remove(int id) {
        HrRegister item = hrRegisterMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        hrRegisterMapper.updateByPrimaryKey(item);
    }

    public HrRegisterDto getModelById(int id) {
        return getDto(hrRegisterMapper.selectByPrimaryKey(id));
    }


    public List<HrRegisterDto> listData(String userLogin) {
        Map map = new HashMap();
        map.put("deleted", false);
        map.put("userLogin", userLogin);
        List<HrRegister> Registers = hrRegisterMapper.selectAll(map);
        List<HrRegisterDto> list = Lists.newArrayList();
        Registers.forEach(p -> list.add(getDto(p)));
        return list;
    }

    public PageInfo<HrRegisterDto> loadPagedData(Map<String, Object> params, int pageNum, int pageSize) {
        params.put("deleted", 0);
        PageInfo<HrRegisterDto> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> hrRegisterMapper.selectAll(params));
        //pageInfo.getList().forEach(this::replenishDto);
        return pageInfo;
    }

    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 1, 0, false);
        List<HrRegister> insertList=Lists.newArrayList();
        for(int i=1;i<=list.size();i++) {
            Map map = list.get(i - 1);
            HrRegister item=new HrRegister();
            Map params = new HashMap();
            params.put("userName", map.get(0).toString());
            params.put("province", map.get(2).toString());
            params.put("city", map.get(3).toString());
            if (PageHelper.count(()->hrRegisterMapper.selectAll(params))>0){
                item=hrRegisterMapper.selectAll(params).get(0);
            }
            item.setUserLogin(hrEmployeeSysService.selectByUserName(map.get(0).toString()));
            item.setUserName(map.get(0).toString());
            item.setSpecialty(map.get(1).toString().replaceAll("，",","));
            item.setProvince(map.get(2).toString());
            item.setCity(map.get(3).toString());
            item.setStartTime(map.get(4).toString());
            item.setEndTime(map.get(5).toString());
            item.setRemark(map.get(6).toString());

            item.setGmtCreate(new Date());
            item.setDeleted(false);
            if (item.getId()==null) {
                item.setGmtModified(new Date());
                item.setId(0);
                item.setBusinessKey("");
            }
            //检查每一条数据是否完整
            try {
                BeanValidator.check(item);
            } catch (ParamException e) {
                throw new ParamException("第" + (i + 1) + "行数据异常：" + e.getMessage());
            }
            insertList.add(item);
        }
        //数据无误  新增、更新数据
        int updateNum=0;
        for (HrRegister hrRegister :insertList){
            if (hrRegister.getId()!=0){
                updateNum++;
                hrRegisterMapper.updateByPrimaryKey(hrRegister);
            }else {
                int id = hrRegisterMapper.insert(hrRegister);
                hrRegister.setBusinessKey("hrRegister_"+hrRegister.getId());
            }
        }
        return updateNum+","+(insertList.size()-updateNum);
    }
}











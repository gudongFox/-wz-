package com.cmcu.mcc.oa.service;

import com.cmcu.common.exception.ParamException;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaHardwareMapper;
import com.cmcu.mcc.oa.dto.OaHardwareDto;
import com.cmcu.mcc.oa.entity.OaHardware;

import com.cmcu.mcc.oa.entity.OaSoftware;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaHardwareService {

    @Resource
    OaHardwareMapper oaHardwareMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;


    

    public void remove(int id,String userLogin)
    {
        OaHardware item = oaHardwareMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaHardwareMapper.updateByPrimaryKey(item);
    }

    public void update(OaHardwareDto oaHardwareDto)
    {
        OaHardware item = oaHardwareMapper.selectByPrimaryKey(oaHardwareDto.getId());
        item.setEquipmentName(oaHardwareDto.getEquipmentName());
        item.setEquipmentType(oaHardwareDto.getEquipmentType());
        item.setBank(oaHardwareDto.getBank());
        item.setModel(oaHardwareDto.getModel());
        item.setNumber(oaHardwareDto.getNumber());
        item.setParameter(oaHardwareDto.getParameter());
        item.setAddress(oaHardwareDto.getAddress());
        item.setManager(oaHardwareDto.getManager());
        item.setGmtModified(new Date());
        item.setRemark(oaHardwareDto.getRemark());
        BeanValidator.check(item);
        oaHardwareMapper.updateByPrimaryKey(item);
    }

    public OaHardwareDto getDto(OaHardware item)
    {
        OaHardwareDto oaHardwareDto = OaHardwareDto.adapt(item);
        oaHardwareDto.setBusinessKey("oa_hardware"+item.getId());
        return oaHardwareDto;
    }

    public OaHardwareDto getModelById(int id)
    {
        return getDto(oaHardwareMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin)      //新增一条数据
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaHardware item = new OaHardware();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        oaHardwareMapper.insert(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaHardwareMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaHardware item = (OaHardware)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 2, 0, false);
        int seq= (int) PageHelper.count(()->oaHardwareMapper.selectAll(new HashMap()));
        List<OaHardware> insertList=Lists.newArrayList();
        for(int i=1;i<=list.size();i++) {
            Map map = list.get(i - 1);

            Map params = Maps.newHashMap();
            params.put("equipmentName", map.get(1).toString());

            OaHardware item = new OaHardware();
            if (PageHelper.count(() -> oaHardwareMapper.selectAll(params)) > 0) {
                item = oaHardwareMapper.selectAll(params).get(0);
            }


            item.setEquipmentName(map.get(1).toString());
            item.setEquipmentType(map.get(2).toString());
            item.setBank(map.get(3).toString());
            item.setModel(map.get(4).toString());
            item.setParameter(map.get(5).toString());
            item.setNumber(map.get(6).toString());
            item.setAddress(map.get(7).toString());
            item.setManager(map.get(8).toString());

            item.setRemark(map.get(10).toString());
            item.setGmtCreate(new Date());
            item.setGmtModified(new Date());
            item.setDeleted(false);

            ModelUtil.setNotNullFields(item);
            if (item.getId() == null) {
                item.setCreator(userLogin);
                item.setCreatorName(selectEmployeeService.getNameByUserLogin(userLogin));
                item.setGmtCreate(new Date());
                item.setProcessEnd(true);
                item.setProcessInstanceId("");
                item.setId(0);
            }
            try {
                BeanValidator.check(item);
            } catch (ParamException e) {
                throw new ParamException("第" + (i + 1) + "行导入失败：" + e.getMessage());
            }
            insertList.add(item);
        }
        int updateNum=0;

        for (OaHardware oaHardware:insertList){
            if (oaHardware.getId()!=0){
                oaHardwareMapper.updateByPrimaryKey(oaHardware);
                updateNum++;
            }else {
                oaHardware.setProcessInstanceId(0+"");
                oaHardwareMapper.insert(oaHardware);
            }
        }
        return updateNum+","+(insertList.size()-updateNum);
    }


}

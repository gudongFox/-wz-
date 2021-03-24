package com.cmcu.mcc.oa.service;

import com.cmcu.common.exception.ParamException;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyPoiUtil;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.oa.dao.OaSoftwareMapper;
import com.cmcu.mcc.oa.dto.OaSoftwareDto;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OaSoftwareService {

    @Resource
    OaSoftwareMapper oaSoftwareMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;


    

    public void remove(int id,String userLogin)
    {
        OaSoftware item = oaSoftwareMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        oaSoftwareMapper.updateByPrimaryKey(item);
    }

    public void update(OaSoftware oaSoftwareDto)
    {
        OaSoftware item = oaSoftwareMapper.selectByPrimaryKey(oaSoftwareDto.getId());
        item.setSoftwareName(oaSoftwareDto.getSoftwareName());
        item.setSoftwareType(oaSoftwareDto.getSoftwareType());
        item.setDevelopDept(oaSoftwareDto.getDevelopDept());
        item.setFuncationContent(oaSoftwareDto.getFuncationContent());
        item.setNumber(oaSoftwareDto.getNumber());
        item.setManager(oaSoftwareDto.getManager());
        item.setMaintainDept(oaSoftwareDto.getMaintainDept());
        item.setPrice(oaSoftwareDto.getPrice());
        item.setStartTime(oaSoftwareDto.getStartTime());
        item.setRemark(oaSoftwareDto.getRemark());

        item.setGmtModified(new Date());
        BeanValidator.check(item);
        oaSoftwareMapper.updateByPrimaryKey(item);
    }

    public OaSoftwareDto getDto(OaSoftware item)
    {
        OaSoftwareDto oaSoftwareDto = OaSoftwareDto.adapt(item);
        oaSoftwareDto.setBusinessKey("oa_software"+item.getId());
        return oaSoftwareDto;
    }

    public OaSoftwareDto getModelById(int id)
    {
        return getDto(oaSoftwareMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin)      //新增一条数据
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        OaSoftware item = new OaSoftware();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        ModelUtil.setNotNullFields(item);
        oaSoftwareMapper.insert(item);
        return item.getId();
    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->oaSoftwareMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            OaSoftware item = (OaSoftware)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }
    //导入数据
    public String insertByFile(InputStream inputStream, String userLogin) throws IOException {
        List<Map> list = MyPoiUtil.listTableData(inputStream, 2, 0, false);
        int seq= (int) PageHelper.count(()->oaSoftwareMapper.selectAll(new HashMap()));
        List<OaSoftware> insertList=Lists.newArrayList();
        for(int i=1;i<=list.size();i++) {
            Map map = list.get(i - 1);

            Map params = Maps.newHashMap();
            params.put("softwareName", map.get(1).toString());

            OaSoftware item = new OaSoftware();
            if (PageHelper.count(() -> oaSoftwareMapper.selectAll(params)) > 0) {
                item = oaSoftwareMapper.selectAll(params).get(0);
            }


            item.setSoftwareName(map.get(1).toString());
            item.setSoftwareType(map.get(2).toString());
            item.setDevelopDept(map.get(3).toString());
            item.setFuncationContent(map.get(4).toString());
            item.setManager(map.get(5).toString());
            item.setMaintainDept(map.get(6).toString());
            item.setNumber(map.get(7).toString());
            item.setPrice(map.get(8).toString());
            item.setStartTime(map.get(9).toString());
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

        for (OaSoftware oaSoftware:insertList){
            if (oaSoftware.getId()!=0){
                oaSoftwareMapper.updateByPrimaryKey(oaSoftware);
                updateNum++;
            }else {
                oaSoftware.setProcessInstanceId(0+"");
                oaSoftwareMapper.insert(oaSoftware);
            }
        }
        return updateNum+","+(insertList.size()-updateNum);
    }


}

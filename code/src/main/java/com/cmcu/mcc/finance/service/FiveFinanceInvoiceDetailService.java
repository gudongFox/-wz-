package com.cmcu.mcc.finance.service;

import com.cmcu.act.service.TaskHandleService;
import com.cmcu.common.service.BaseService;
import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.NumberChangeUtils;
import com.cmcu.mcc.act.service.MyActService;
import com.cmcu.mcc.act.service.MyHistoryService;
import com.cmcu.mcc.business.dao.BusinessCustomerMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibraryMapper;
import com.cmcu.mcc.business.dao.FiveBusinessContractLibrarySubpackageMapper;
import com.cmcu.mcc.finance.dao.FiveFinanceInvoiceDetailMapper;
import com.cmcu.mcc.finance.dto.FiveFinanceInvoiceDetailDto;
import com.cmcu.mcc.finance.entity.FiveFinanceInvoiceDetail;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;
import com.cmcu.mcc.service.impl.HandleFormService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiveFinanceInvoiceDetailService extends BaseService {

    @Resource
    FiveFinanceInvoiceDetailMapper fiveFinanceInvoiceDetailMapper;

    @Resource
    CommonCodeService commonCodeService;


    @Autowired
    HrEmployeeSysService hrEmployeeSysService;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Autowired
    HrDeptService hrDeptService;

    @Autowired
    MyActService myActService;

    @Autowired
    MyHistoryService myHistoryService;

    @Autowired
    BusinessCustomerMapper businessCustomerMapper;
    @Autowired
    TaskHandleService taskHandleService;
    @Autowired
    HrEmployeeService hrEmployeeService;
    @Autowired
    FiveBusinessContractLibraryMapper fiveBusinessContractLibraryMapper;
    @Autowired
    FiveBusinessContractLibrarySubpackageMapper fiveBusinessContractLibrarySubpackageMapper;
    @Resource
    HandleFormService handleFormService;

    public void remove(int id, String userLogin) {
        FiveFinanceInvoiceDetail item = fiveFinanceInvoiceDetailMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin), "只允许创建人删除!");
        handleFormService.removeProcessInstance(item.getBusinessKey(),userLogin);
    }


    public void update(FiveFinanceInvoiceDetailDto dto) {
        FiveFinanceInvoiceDetail item = fiveFinanceInvoiceDetailMapper.selectByPrimaryKey(dto.getId());

        item.setRemark(dto.getRemark());
        item.setGmtModified(new Date());

        BeanValidator.check(item);
        fiveFinanceInvoiceDetailMapper.updateByPrimaryKey(item);
    }


    public FiveFinanceInvoiceDetailDto getModelById(int id) {
        return getDto(fiveFinanceInvoiceDetailMapper.selectByPrimaryKey(id));
    }


    public int getNewModel(String userLogin,String uiSref) {

        FiveFinanceInvoiceDetail item = new FiveFinanceInvoiceDetail();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeService.getModelByUserLogin(userLogin).getUserName());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setDeleted(false);
        ModelUtil.setNotNullFields(item);
        fiveFinanceInvoiceDetailMapper.insert(item);

        return item.getId();
    }


    public FiveFinanceInvoiceDetailDto getDto(Object item) {
        FiveFinanceInvoiceDetail model= (FiveFinanceInvoiceDetail) item;
        FiveFinanceInvoiceDetailDto dto=FiveFinanceInvoiceDetailDto.adapt(model);
        return dto;
    }


    public List<FiveFinanceInvoiceDetail> selectAll(String userLogin, String uiSref) {
        Map params=Maps.newHashMap();
        params.put("deleted",false);
        List<Integer> deptIdList=hrEmployeeSysService.getMyDeptList(userLogin,uiSref);
        params.put("deptIdList",deptIdList);
        return  fiveFinanceInvoiceDetailMapper.selectAll(params);
    }


    public PageInfo<Object> listPagedData(Map<String,Object> params, String userLogin, String uiSref,int pageNum, int pageSize) {
        params.put("deleted",false);
//如果有数据权限判断数据权限  myDeptData true查看当前部门 false查看创建人
        Map map =new HashMap();
        map.put("myDeptData",true);
        map.put("uiSref",uiSref);
        map.put("enLogin",userLogin);
        params.putAll(getDefaultRightParams(map));
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> fiveFinanceInvoiceDetailMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p -> {
            FiveFinanceInvoiceDetail item=(FiveFinanceInvoiceDetail)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    /**
     * 收入金额转中文大写
     * @param money
     * @return
     */
    public String moneyTurnCapital(Double money){
        return NumberChangeUtils.moneyToChinese(money*10000);
    }

}

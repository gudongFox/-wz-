package com.cmcu.mcc.five.service;

import com.cmcu.common.service.CommonCodeService;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.ModelUtil;
import com.cmcu.common.util.MyDateUtil;
import com.cmcu.mcc.comm.MccConst;
import com.cmcu.mcc.five.dao.FiveOaCarApplyMapper;
import com.cmcu.mcc.five.dao.FiveOaCarMapper;
import com.cmcu.mcc.five.dto.FiveOaCarDto;
import com.cmcu.mcc.five.entity.FiveOaCar;
import com.cmcu.mcc.five.entity.FiveOaCarApply;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import com.cmcu.mcc.hr.service.HrEmployeeSysService;
import com.cmcu.mcc.hr.service.SelectEmployeeService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FiveOaCarService {

    @Resource
    FiveOaCarMapper fiveOaCarMapper;

    @Autowired
    SelectEmployeeService selectEmployeeService;

    @Resource
    CommonCodeService commonCodeService;

    @Autowired
    FiveOaCarApplyMapper fiveOaCarApplyMapper;



    @Resource
    JdbcTemplate jdbcTemplate;

    public void remove(int id,String userLogin)
    {
        FiveOaCar item = fiveOaCarMapper.selectByPrimaryKey(id);
        Assert.state(item.getCreator().equalsIgnoreCase(userLogin),"只允许创建人删除！");
        item.setGmtModified(new Date());
        item.setDeleted(true);
        fiveOaCarMapper.updateByPrimaryKey(item);
    }

    public void update(FiveOaCarDto fiveOaCarDto)
    {
        FiveOaCar item = fiveOaCarMapper.selectByPrimaryKey(fiveOaCarDto.getId());
        item.setDeptName(fiveOaCarDto.getDeptName());
        item.setDeptId(fiveOaCarDto.getDeptId());
        item.setCarNo(fiveOaCarDto.getCarNo());
        item.setCarType(fiveOaCarDto.getCarType());
        item.setCarCc(fiveOaCarDto.getCarCc());
        item.setCarPrice(fiveOaCarDto.getCarPrice());
        item.setCarBrand(fiveOaCarDto.getCarBrand());
        item.setCarColor(fiveOaCarDto.getCarColor());
        item.setCarStatus(fiveOaCarDto.getCarStatus());
        item.setSeating(fiveOaCarDto.getSeating());
        item.setUseNature(fiveOaCarDto.getUseNature());
        item.setOilCardNo(fiveOaCarDto.getOilCardNo());
        item.setRegisterTime(fiveOaCarDto.getRegisterTime());
        item.setCertificateType(fiveOaCarDto.getCertificateType());
        item.setVehicleIdentificationCode(fiveOaCarDto.getVehicleIdentificationCode());
        item.setEngineNumber(fiveOaCarDto.getEngineNumber());

        item.setGmtModified(new Date());
        item.setBuyDate(fiveOaCarDto.getBuyDate());
        item.setRemark(fiveOaCarDto.getRemark());
        item.setChargeMan(fiveOaCarDto.getChargeMan());
        item.setChargeManName(fiveOaCarDto.getChargeManName());
        BeanValidator.check(item);
        fiveOaCarMapper.updateByPrimaryKey(item);
    }

    public FiveOaCarDto getDto(FiveOaCar item)
    {
        FiveOaCarDto fiveOaCarDto = FiveOaCarDto.adapt(item);
        return fiveOaCarDto;
    }

    public FiveOaCarDto getModelById(int id)
    {
        return getDto(fiveOaCarMapper.selectByPrimaryKey(id));
    }

    public int getNewModel(String userLogin)      //新增一条数据
    {
        HrEmployeeDto hrEmployeeDto = selectEmployeeService.selectByUserLogin(userLogin);
        FiveOaCar item = new FiveOaCar();
        item.setCreator(userLogin);
        item.setCreatorName(hrEmployeeDto.getUserName());
        item.setDeptName(hrEmployeeDto.getDeptName());
        item.setDeptId(hrEmployeeDto.getDeptId());
        item.setCarStatus(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"车辆状态").toString());
        item.setDeptName(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"车辆所在单位").toString());
        item.setCarType(commonCodeService.selectDefaultCodeValue(MccConst.APP_CODE,"车辆类型").toString());
        item.setGmtCreate(new Date());
        item.setGmtModified(new Date());
        item.setBuyDate(MyDateUtil.getStringToday());
        item.setCarPrice(BigDecimal.ZERO);

        ModelUtil.setNotNullFields(item);
        fiveOaCarMapper.insert(item);
        item.setBusinessKey("fiveOaCar_"+item.getId());
        fiveOaCarMapper.updateByPrimaryKey(item);
        return item.getId();

    }

    public PageInfo<Object> listPagedData(Map<String,Object> params,String userLogin,int pageNum,int pageSize) throws ParseException {
        //跟新车辆状态
        updateCarState();

        params.put("deleted",false);
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->fiveOaCarMapper.selectAll(params));
        List<Object> list = Lists.newArrayList();
        pageInfo.getList().forEach(p ->{
            FiveOaCar item = (FiveOaCar)p;
            list.add(getDto(item));
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    public List<FiveOaCar> listAllCar(int applyId) throws ParseException {
        //跟新车辆状态
        updateCarState();
        FiveOaCarApply fiveOaCarApply = fiveOaCarApplyMapper.selectByPrimaryKey(applyId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<FiveOaCar> list = getCarsState(simpleDateFormat.parse(fiveOaCarApply.getBeginTime()),simpleDateFormat.parse(fiveOaCarApply.getEndTime()),applyId);
        return list;
    }

    public List<FiveOaCar> listAllCarByMaintain(int id) throws ParseException {
        Map map =new HashMap();
        map.put("deleted",false);
        List<FiveOaCar> list = fiveOaCarMapper.selectAll(map);
        return list;
    }

    //跟新车辆状态
    public void updateCarState() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map carParams = new HashMap();
        carParams.put("deleted",false);
        List<FiveOaCar> fiveOaCars = fiveOaCarMapper.selectAll(carParams);
        for(FiveOaCar car:fiveOaCars){
            //查询该车辆的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("carId",car.getId());
            params.put("isProcessEnd",true);
            List<FiveOaCarApply> FiveOaCarApplies = fiveOaCarApplyMapper.selectAll(params);
            for(FiveOaCarApply FiveOaCarApply :FiveOaCarApplies){
                //判断当前时间 是否落在申请区域
                if(now.after(simpleDateFormat.parse(FiveOaCarApply.getBeginTime()))&&now.before(simpleDateFormat.parse(FiveOaCarApply.getEndTime()))){
                    //跟新状态为 使用中
                    FiveOaCar FiveOaCar = fiveOaCarMapper.selectByPrimaryKey(car.getId());
                    if(!FiveOaCar.getCarStatus().equalsIgnoreCase("维修中")) {
                        FiveOaCar.setCarStatus("使用中");
                        fiveOaCarMapper.updateByPrimaryKey(FiveOaCar);
                    }
                } else{
                    //跟新状态为 正常 --不用每次修改 待优化
                    FiveOaCar FiveOaCar = fiveOaCarMapper.selectByPrimaryKey(car.getId());
                    if(!FiveOaCar.getCarStatus().equalsIgnoreCase("维修中")) {
                        FiveOaCar.setCarStatus("正常");
                        fiveOaCarMapper.updateByPrimaryKey(FiveOaCar);
                    }
                }
            }
        }


    }
    //查询时间段车辆状态
    public List<FiveOaCar> getCarsState(Date startTime, Date endTime, int applyId) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Map carParams = new HashMap();
        carParams.put("deleted",false);
        List<FiveOaCar> fiveOaCars = fiveOaCarMapper.selectAll(carParams);
        //冲突的carid
        List<Integer> conflictCarIds =new ArrayList<>();
        for(FiveOaCar car:fiveOaCars){
            //查询该车辆的所有申请 且流程已完结
            Map params = new HashMap();
            params.put("deleted",false);
            params.put("carId",car.getId());
            params.put("isProcessEnd",true);
            List<FiveOaCarApply> FiveOaCarApplies = fiveOaCarApplyMapper.selectAll(params);
            for(FiveOaCarApply FiveOaCarApply :FiveOaCarApplies){
                //排除当前申请
                if(FiveOaCarApply.getId()!=applyId) {
                    //判断当前时间 是否落在申请区域
                    if (endTime.before(simpleDateFormat.parse(FiveOaCarApply.getBeginTime())) || startTime.after(simpleDateFormat.parse(FiveOaCarApply.getEndTime()))) {
                        //修改状态为 正常
                        if (!car.getCarStatus().equalsIgnoreCase("维修中")) {
                            //该车辆已在冲突carId中 就不跟新状态
                            if(!conflictCarIds.contains(car.getId())){
                                car.setCarStatus("正常");
                            }
                        }
                    } else {
                        //修改状态为 使用中
                        if (!car.getCarStatus().equalsIgnoreCase("维修中")) {
                            conflictCarIds.add(car.getId());
                            car.setCarStatus("使用中");
                            car.setRemark(car.getRemark()+"<br>"+FiveOaCarApply.getBeginTime()+" - "+FiveOaCarApply.getEndTime());
                        }
                    }
                }
            }
        }
        return fiveOaCars;


    }
    public List<Map> listMapData(Map params) {
        List<Map> list = Lists.newArrayList();
        params.put("deleted", false);
        String  sql="";
        //判断搜索类型
        if(params.get("selectType").equals("车辆维护")){
            //年月统计
            String findCondition ="";
            if(params.get("selectYear") != null && params.get("selectYear") != ""){
                findCondition =findCondition+ " AND YEAR(gmt_create)="+params.get("selectYear");
            }
            if(params.get("selectMonth") != null && params.get("selectMonth") != ""){
                findCondition =findCondition+ " AND MONTH(gmt_create)="+params.get("selectMonth");
            }
            //按车辆
            if(params.get("selectCarId") != null && params.get("selectCarId") != ""){
                findCondition =findCondition+  " and car_id="+params.get("selectCarId");
            }
            //按单位统计
            if(params.get("selectDeptName") != null && params.get("selectDeptName") != ""){
                findCondition =findCondition+  " and deptName="+params.get("selectDeptName");
            }
            if(params.get("qType") != null && params.get("qType") != ""){
                findCondition =findCondition+  " and type='"+params.get("qType")+"'";
            }
            if ("加油卡".equals(params.get("qType"))){
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +
                        "    soil_time as '充卡/加油时间', soil_money as '充卡/加油金额'," +
                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0  "+findCondition+" order by id desc";
            }else if ("保养/维护".equals(params.get("qType"))){
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +
                        "    upkeep_course as '保养里程', upkeep_time as '保养时间', upkeep_factory as '修理厂', upkeep_money as '保养费用', upkeep_invoice_no as '发票号', upkeep_invoice_money as '发票金额'," +
                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0  "+findCondition+" order by id desc";
            }else if ("ETC".equals(params.get("qType"))){
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +
                        "    etc_money as 'etc充值金额', etc_time as 'etc充值时间'," +
                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0  "+findCondition+" order by id desc";
            }else if ("年检".equals(params.get("qType"))){
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +

                        "    check_time as '年检时间', check_money as '年检金额', check_address as '年检地址', " +

                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0  "+findCondition+" order by id desc";
            }else if ("其他".equals(params.get("qType"))) {
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +
                        "    other_type as '其他事项', other_money as '其他花费'," +
                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0  "+findCondition+" order by id desc";
            }else {
                sql ="select  dept_name as '使用单位',  type as '维护类型', car_no as '车牌号', " +
                        "    soil_time as '充卡/加油时间', soil_money as '充卡/加油金额'," +
                        "    upkeep_course as '保养里程', upkeep_time as '保养时间', upkeep_factory as '修理厂', upkeep_money as '保养费用', upkeep_invoice_no as '发票号', upkeep_invoice_money as '发票金额'," +
                        "    check_time as '年检时间', check_money as '年检金额', check_address as '年检地址', " +
                        "    etc_money as 'etc充值金额', etc_time as 'etc充值时间'," +
                        "    other_type as '其他事项', other_money as '其他花费'," +
                        "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                        "     from db_wuzhou.five_oa_car_maintain   where is_deleted = 0   "+findCondition+" order by id desc";
            }

        }else if(params.get("selectType").equals("车辆使用")) {
            //年月统计
            String findCondition ="";
            if(params.get("selectYear") != null && params.get("selectYear") != ""){
                findCondition =findCondition+ "AND YEAR(gmt_create)="+params.get("selectYear");
            }
            if(params.get("selectMonth") != null && params.get("selectMonth") != ""){
                findCondition =findCondition+ " AND MONTH(gmt_create)="+params.get("selectMonth");
            }
            //按车辆
            if(params.get("selectCarId") != null && params.get("selectCarId") != ""){
                findCondition =findCondition+  " and car_id="+params.get("selectCarId");
            }
            //按单位统计
            if(params.get("selectDeptName") != null && params.get("selectDeptName") != ""){
                findCondition =findCondition+  " and deptName="+params.get("selectDeptName");
            }
            if(params.get("qType") != null && params.get("qType") != ""){
                findCondition =findCondition+  " and type='"+params.get("qType")+"'";
            }
             sql ="select  car_name as '申请车辆',  type as '用车人', dept_name as '申请部门', " +
                    "    begin_time as '开始时间', end_time as '结束时间',apply_type as '用车类型',apply_reason as '用车事项'," +
                    "    is_self_drive as '自驾', driver_name as '司机', destination as '目的地', passenger as '乘客', user_num as '乘客人数', car_info as '用车事由'," +
                    "    mileage as '里程数', soil_pay as '油费', pass_pay as '过桥/过路费', " +
                    "    part_pay as '停车费'," +
                    "      gmt_create as '创建时间',  creator_name as '创建人', remark as '备注'" +
                    "     from db_wuzhou.five_oa_car_apply   where is_deleted = 0   "+findCondition+" order by id desc";
        }

        try {
            List<Map<String, Object>> oList = jdbcTemplate.queryForList(sql);
            list.addAll(oList);
        } catch (Exception ex) {

        }
        return  list;
    }

}

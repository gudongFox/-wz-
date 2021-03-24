package com.cmcu.mcc.finance.dto;

import com.cmcu.mcc.finance.entity.FiveFinanceDeptFund;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Getter
@Setter
public class FiveFinanceDeptFundDetailDto  {
    //使用类型
    private String type;
    //跳转路由
    private String url;
    //跳转路由参数
    private String urlIdName;
    //跳转路由参数
    private int urlId;
    //创建人
    private String creator;
    //创建时间
    private Date gmtModified;
    //使用金额
    private String money;
    //当前状态
    private String processName;


}

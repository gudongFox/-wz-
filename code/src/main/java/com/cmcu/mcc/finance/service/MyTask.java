package com.cmcu.mcc.finance.service;

import com.cmcu.mcc.hr.dto.HrDeptDto;
import com.cmcu.mcc.hr.service.HrDeptService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

public class MyTask {
    @Autowired
    HrDeptService hrDeptService;
    @Autowired
    FiveFinanceDeptFundService fiveFinanceDeptFundService;
    //跟新部门资金
    public void refreshAllDeptMoney(){
        System.out.println( new Date().toString()+"计算开始----");
        List<HrDeptDto> hrDeptDtos = hrDeptService.selectAll();
        for(HrDeptDto dto:hrDeptDtos){
            fiveFinanceDeptFundService.refreshMoney(dto.getId());
        }
        System.out.println(new Date().toString()+"计算结束----");
    }
}

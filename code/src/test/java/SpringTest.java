

import com.cmcu.common.dao.CommonFormDataMapper;
import com.cmcu.common.dao.CommonFormDetailMapper;
import com.cmcu.common.dto.InputConfigDto;
import com.cmcu.common.entity.CommonFormData;
import com.cmcu.common.entity.CommonFormDetail;
import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyPoiUtil;


import com.cmcu.mcc.hr.dao.HrEmployeeMapper;
import com.cmcu.mcc.hr.dao.HrEmployeeSysMapper;
import com.cmcu.mcc.hr.dto.HrEmployeeDto;
import com.cmcu.mcc.hr.entity.HrEmployee;
import com.cmcu.mcc.hr.entity.HrEmployeeSys;

import com.cmcu.mcc.sys.service.SysRoleAclService;

import com.common.wx.model.UserInfo;
import com.common.wx.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.method.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SpringTest {


    @Resource
    HrEmployeeSysMapper hrEmployeeSysMapper;


    @Resource
    HrEmployeeMapper hrEmployeeMapper;

    @Resource
    SysRoleAclService sysRoleAclService;


    @Resource
    HistoryService historyService;

    @Resource
    RuntimeService runtimeService;


    @Resource
    CommonFormDetailMapper commonFormDetailMapper;


    @Resource
    TaskService taskService;

    @Resource
    public
    CommonFormDataMapper commonFormDataMapper;


    @Test
    public void updateDetailKeyInfo() throws Exception {
        String enLogin = "luodong";






    }


    @Test
    public void SetUserWxId() throws Exception {

        String path="C:/Users/Administrator/Desktop/wuzhou.xls";

       List<Map> menus=  sysRoleAclService.listSystemMenu("2863");

        List<Map> list=MyPoiUtil.listTableDataNew(new FileInputStream(path), 1, 0, false);

        Map params=Maps.newHashMap();
        params.put("deleted",false);

        List<String> noneName=Lists.newArrayList();
        List<String> multipleName=Lists.newArrayList();
        for(Map data:list){
            String cnName=data.get("姓名").toString();
            if(StringUtils.isNotEmpty(cnName)) {
                cnName=StringUtils.replace(cnName,"——","-");
                cnName=StringUtils.replace(cnName,"~","-");
                cnName=StringUtils.replace(cnName,"~","-");
                cnName=StringUtils.replace(cnName,"(","（");
                cnName=StringUtils.replace(cnName,"2","（");
                if(cnName.contains("（")){
                    cnName=StringUtils.split(cnName,"（")[0];
                }
                if(cnName.contains("～")){
                    cnName=StringUtils.split(cnName,"～")[0];
                }
                if(cnName.contains("-")){
                    cnName=StringUtils.split(cnName,"-")[0];
                }
                if(cnName.contains(" ")){
                    cnName=StringUtils.split(cnName," ")[0];
                }
                cnName=StringUtils.replace(cnName,"張","张");
                params.put("userName",cnName);

                List<HrEmployeeDto> hrEmployeeList=hrEmployeeMapper.selectAll(params);

                String wxId = data.get("帐号").toString();
                String mobile = data.get("手机").toString();
                if(hrEmployeeList.size()==1) {
                    HrEmployee hrEmployee=hrEmployeeMapper.selectAll(params).get(0);
                    HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(hrEmployee.getUserLogin());
                    if(hrEmployeeSys!=null) {
                        if(StringUtils.isEmpty(hrEmployeeSys.getWxId())){
                            hrEmployeeSys.setWxId(wxId);
                            hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                        }
                        if(StringUtils.isEmpty(hrEmployee.getMobile())){
                            hrEmployee.setMobile(mobile);
                            hrEmployeeMapper.updateByPrimaryKey(hrEmployee);
                        }
                    }
                }else if(hrEmployeeList.size()==0){
                    noneName.add(cnName);
                }else{
                    if(hrEmployeeList.stream().anyMatch(p->p.getMobile().equals(mobile))){
                        //通过手机号匹配上了
                        HrEmployee hrEmployee=hrEmployeeList.stream().filter(p->p.getMobile().equals(mobile)).findFirst().get();
                        HrEmployeeSys hrEmployeeSys=hrEmployeeSysMapper.selectByUserLogin(hrEmployee.getUserLogin());
                        if(hrEmployeeSys!=null) {
                            if(StringUtils.isEmpty(hrEmployeeSys.getWxId())){
                                hrEmployeeSys.setWxId(wxId);
                                hrEmployeeSysMapper.updateByPrimaryKey(hrEmployeeSys);
                            }
                            if(StringUtils.isEmpty(hrEmployee.getMobile())){
                                hrEmployee.setMobile(mobile);
                                hrEmployeeMapper.updateByPrimaryKey(hrEmployee);
                            }
                        }
                    }else {
                        multipleName.add(cnName);
                    }
                }
            }
        }

        System.out.println("未匹配上："+noneName.size()+"."+StringUtils.join(noneName,","));
        System.out.println("重复名字："+multipleName.size()+"."+ StringUtils.join(multipleName,","));
    }

    @Test
    public void SetWxHead(){
       for(HrEmployeeSys hrEmployeeSys:hrEmployeeSysMapper.selectAll(Maps.newHashMap())){
           if(StringUtils.isNotEmpty(hrEmployeeSys.getWxId())){
               HrEmployee hrEmployee=hrEmployeeMapper.selectByUserLoginOrNo(hrEmployeeSys.getUserLogin());
               if(StringUtils.isEmpty(hrEmployee.getAvatar())||!hrEmployee.getAvatar().startsWith("http")) {
                   UserInfo userInfo = UserService.getUserInfo(hrEmployeeSys.getWxId());
                   if (userInfo!=null){
                       hrEmployee.setAvatar(userInfo.getThumb_avatar());
                       hrEmployeeMapper.updateByPrimaryKey(hrEmployee);
                   }else {
                       System.out.println(hrEmployee.getUserName());
                   }
                   System.out.println("设置用户"+hrEmployee.getUserLogin()+"微信头像");
               }
           }
       }
    }

    @Test
    public void Test() {


    }

}

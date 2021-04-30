package com.cmcu.common.service;


import com.cmcu.common.dao.CommonBlackMapper;
import com.cmcu.common.dao.CommonRequestMapper;
import com.cmcu.common.entity.CommonBlack;
import com.cmcu.common.util.BeanValidator;
import com.cmcu.common.util.GuavaCacheService;
import com.cmcu.common.util.IpUtil;
import com.cmcu.common.util.ModelUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommonBlackService {


    @Resource
    CommonBlackMapper commonBlackMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;
    @Resource
    TaskExecutor taskExecutor;
    @Resource
    CommonRequestMapper commonRequestMapper;


    public List<CommonBlack> selectAll(String enLogin) {
        String tenetId = commonUserService.getTenetId(enLogin);
        List<CommonBlack> all = guavaCacheService.get("COMMON_BLACK_ALL", () -> {
            Map params = Maps.newHashMap();
            params.put("deleted", false);
            return Optional.of(commonBlackMapper.selectAll(params).stream().filter(p -> p.getGmtExpired().getTime() > new Date().getTime()).collect(Collectors.toList()));
        });
        all = all.stream().filter(p -> p.getGmtExpired().getTime() > new Date().getTime()).collect(Collectors.toList());
        if (all.stream().anyMatch(p -> p.getTenetId().equalsIgnoreCase(tenetId))) {
            return all.stream().filter(p -> p.getTenetId().equalsIgnoreCase(tenetId)).collect(Collectors.toList());
        }
        return all.stream().filter(p -> StringUtils.isEmpty(tenetId)).collect(Collectors.toList());
    }

    public void remove(int id,String enLogin) {
        Assert.state(StringUtils.isNotEmpty(enLogin), "操作人不能为空!");
        CommonBlack item = commonBlackMapper.selectByPrimaryKey(id);
        item.setDeleted(true);
        item.setGmtModified(new Date());
        item.setGmtExpired(DateUtils.addMinutes(new Date(),-1));
        commonBlackMapper.updateByPrimaryKey(item);
        clearCache();
    }

    public CommonBlack getModelById(int id){
        return commonBlackMapper.selectByPrimaryKey(id);
    }

    public CommonBlack getNewModel(String enLogin) {
        CommonBlack model = new CommonBlack();
        model.setTargetIp("*");
        model.setTenetId(commonUserService.getTenetId(enLogin));
        model.setGmtCreate(new Date());
        model.setGmtModified(new Date());
        model.setGmtExpired(DateUtils.addYears(new Date(), 10));
        model.setId(0);
        model.setForbidden(true);
        ModelUtil.setNotNullFields(model);
        return model;
    }


    public void save(CommonBlack item){
        ModelUtil.setNotNullFields(item);
        BeanValidator.check(item);
        if(item.getId()>0){
            item.setGmtModified(new Date());
            commonBlackMapper.updateByPrimaryKey(item);
        }else{
            commonBlackMapper.insert(item);
        }
        clearCache();
    }

    /**
     * 判断用户是否在黑名单内
     * @param remoteAddr
     * @param ip
     * @param enLogin
     * @return
     */
    public boolean  blockByBlackList(String remoteAddr,String ip,String enLogin){

        List<CommonBlack> all=selectAll(enLogin);
        Date  now=new Date();
        if(all.stream().anyMatch(p->p.getGmtExpired().getTime()>now.getTime())){
            //正在生效的封禁IP
            List<CommonBlack> blockIpList= all.stream().filter(p->p.getGmtExpired().getTime()>now.getTime()).filter(p->p.getTargetUser().equalsIgnoreCase("*")||StringUtils.isEmpty(enLogin)).collect(Collectors.toList());
            //正在生效的规则
            all=all.stream().filter(p->p.getGmtExpired().getTime()>now.getTime()).filter(p->p.getTargetUser().equalsIgnoreCase(enLogin)||StringUtils.isEmpty(enLogin)).collect(Collectors.toList());
            List<CommonBlack> acceptList=all.stream().filter(p->!p.getForbidden()).collect(Collectors.toList());
            List<CommonBlack> blockList=all.stream().filter(CommonBlack::getForbidden).collect(Collectors.toList());

            //直接命中黑名单,则拒绝
            if(blockList.stream().anyMatch(p->p.getTargetUser().equalsIgnoreCase(enLogin))) {
                if (blockList.stream().filter(p -> p.getTargetUser().equalsIgnoreCase(enLogin)).anyMatch(p -> p.getTargetIp().equalsIgnoreCase(remoteAddr) || p.getTargetIp().equalsIgnoreCase(ip))) {
                    return true;
                }
            }

            //直接命中白名单,则允许
            if(acceptList.stream().anyMatch(p->p.getTargetUser().equalsIgnoreCase(enLogin))) {
                if (acceptList.stream().filter(p -> p.getTargetUser().equalsIgnoreCase(enLogin)).anyMatch(p -> p.getTargetIp().equalsIgnoreCase(remoteAddr) || p.getTargetIp().equalsIgnoreCase(ip))) {
                    return false;
                }
            }

            //在拒绝名单里面
            if(blockList.stream().anyMatch(p->p.getTargetIp().equalsIgnoreCase("*"))) return true;

            //在拒绝IP里面
           if (blockIpList.stream().anyMatch(p->p.getTargetIp().equals(ip))) return true;
        }
        return false;
    }



    private void clearCache(){
        guavaCacheService.invalidate("COMMON_BLACK_ALL");
    }


    /**
     * 判断是否未机器登录攻击
     */
    public void checkNormalLogin(String enLogin, String Ip){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                    Date end = new Date();
                    Date start = new Date(end.getTime()  - 1*60*1000);//一分钟以前.
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Map params=Maps.newHashMap();
                    params.put("requestIp",Ip);
                    params.put("startTime",formatter.format(start));
                    params.put("endTime",formatter.format(end));

                    boolean blockedIp=false;
                    long count= PageHelper.count(()->commonRequestMapper.selectAll(params));
                    if(count>1000){
                        blockedIp=true;
                    }else {
                        params.put("requestLoginUrl", "/login");
                        count= PageHelper.count(()->commonRequestMapper.selectAll(params));
                        //判断规定时间内错误登录次数超过30次 限制当前ip
                        if (count >= 30) {
                            Map map = Maps.newHashMap();
                            map.put("targetIp", Ip);
                            map.put("targetUser", enLogin);
                            if (commonBlackMapper.selectAll(map).size() < 1) {
                                blockedIp=true;
                            }
                        }
                    }

                    if(blockedIp){
                        Map map=Maps.newHashMap();
                        map.put("targetIp",Ip);
                        map.put("targetUser","*");
                        if (PageHelper.count(()->commonBlackMapper.selectAll(map))<1){
                            CommonBlack model = new CommonBlack();
                            model.setId(0);
                            model.setTenetId(commonUserService.getTenetId(enLogin));
                            model.setGmtCreate(new Date());
                            model.setGmtModified(new Date());
                            model.setGmtExpired(DateUtils.addYears(new Date(), 99));
                            model.setForbidden(true);
                            model.setDeleted(false);
                            model.setTargetIp(Ip);
                            model.setTargetUser("*");
                            model.setRemark("短时间内多次登录,系统限制IP使用!");
                            ModelUtil.setNotNullFields(model);
                            commonBlackMapper.insert(model);
                            //清除缓存
                            guavaCacheService.invalidate("COMMON_BLACK_ALL");
                        }

                    }
                    //限制账号
                    params.put("requestLoginUrl", "/login");
                    params.put("requestLogin",enLogin);
                    count =  PageHelper.count(()->commonRequestMapper.selectAll(params));
                    if(count>=15){
                        Map map=Maps.newHashMap();
                        map.put("targetIp",Ip);
                        map.put("targetUser",enLogin);
                        if (commonBlackMapper.selectAll(map).size()<1){
                            CommonBlack model = new CommonBlack();
                            model.setId(0);
                            model.setTargetIp(Ip);
                            model.setTargetUser(enLogin);
                            model.setTenetId(commonUserService.getTenetId(enLogin));
                            model.setGmtCreate(new Date());
                            model.setGmtModified(new Date());
                            model.setGmtExpired(DateUtils.addYears(new Date(), 99));
                            model.setForbidden(true);
                            model.setDeleted(false);
                            model.setRemark("短时间内多次登录,系统限制账号使用!");
                            ModelUtil.setNotNullFields(model);
                            commonBlackMapper.insert(model);
                            //清除缓存
                            guavaCacheService.invalidate("COMMON_BLACK_ALL");
                        }
                    }

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

    }


}

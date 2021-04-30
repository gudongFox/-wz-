package com.cmcu.common.web;

import com.common.model.JsonData;
import com.cmcu.common.dao.CommonRequestMapper;
import com.cmcu.common.entity.CommonRequest;
import com.cmcu.common.service.CommonBlackService;
import com.cmcu.common.util.*;
import com.cmcu.common.service.CommonUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {


    private static final String START_TIME = "startTime";
    private  static final String EN_LOGIN="enLogin";

    @Resource
    CommonBlackService commonBlackService;

    @Resource
    CommonRequestMapper commonRequestMapper;

    @Resource
    CommonUserService commonUserService;

    @Resource
    GuavaCacheService guavaCacheService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!IsIgnorePath(request.getRequestURI())) {

            long start = System.currentTimeMillis();
            request.setAttribute(START_TIME, start);
            String enLogin = request.getHeader(EN_LOGIN);
            if (StringUtils.isEmpty(enLogin)) {
                Map params = WebUtil.getRequestParameters();
                enLogin = params.getOrDefault(EN_LOGIN, "anon").toString();
            }
            request.setAttribute(EN_LOGIN, enLogin);

            //黑名单检测
            if (blockByBlackList(request)) {
                if (request.getRequestURI().endsWith(".json")) {
                    sendJsonMessage(response, JsonData.fail("您的账户已关停，请至企业微信申请解封!"));//2021-1-20HNZ 蔡工要求修改：禁止访问系统(black list)!
                } else {
                    request.getRequestDispatcher("/web/v1/403.html").forward(request, response);
                }
                return false;
            }

            //跳转到登录页面
            //if(request.getRequestURI().contains("/index")) {
            // System.out.println("跳转到首页");
            //request.getRequestDispatcher("/login").forward(request, response);
            // return false;
            // }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            if(!IsIgnorePath(request.getRequestURI())) {
                Date now = new Date();
                String enLogin = request.getAttribute(EN_LOGIN).toString();
                Map params = WebUtil.getRequestParameters();
                if (request.getAttribute("error") != null&&StringUtils.isEmpty(request.getAttribute("error").toString())) {
                    params.put("error", request.getAttribute("error"));
                }
                BigDecimal seconds = new BigDecimal((System.currentTimeMillis() - (Long) request.getAttribute(START_TIME)) / 1000.0).setScale(4, RoundingMode.UP);
                CommonRequest item = new CommonRequest();
                item.setTenetId(commonUserService.getTenetId(enLogin));
                item.setRequestMethod(request.getMethod());
                item.setRequestUrl(request.getRequestURI());
                item.setFinishTime(now);
                item.setRequestSecond(seconds);
                item.setRequestParameter(JsonMapper.obj2String(params));
                item.setReferer(request.getHeader("referer"));
                item.setUserAgent(request.getHeader("user-agent"));
                item.setRequestHost(request.getRemoteHost());
                item.setRequestIp(IpUtil.getUserIP(request));
                item.setRequestLogin(enLogin);
                item.setGmtCreate(now);
                item.setGmtModified(now);
                setRequestName(item);
                ModelUtil.setNotNullFields(item);
                if (item.getRequestParameter().length() > 1500) {
                    item.setRequestParameter("{\"error\":\"too big data\"}");
                    log.warn(item.getRequestUrl() + ",parameters too big:" + item.getRequestParameter());
                }
                commonRequestMapper.insert(item);
            }
        }catch (Exception logEx){
            log.warn("logEx",logEx);
        }
        super.afterCompletion(request, response, handler, ex);
    }

    private void setRequestName(CommonRequest commonRequest) {
        List<CommonRequest> list = guavaCacheService.get("IDENTIFIED_REQUEST_" + commonRequest.getTenetId(), () -> Optional.ofNullable(commonRequestMapper.listIdentifiedRequestName(commonRequest.getTenetId())));
        if (list.stream().filter(p -> p.getRequestMethod().equalsIgnoreCase(commonRequest.getRequestMethod()))
                .anyMatch(p -> p.getRequestUrl().equalsIgnoreCase(commonRequest.getRequestUrl()))) {
            commonRequest.setRequestName(list.stream().filter(p -> p.getRequestMethod().equalsIgnoreCase(commonRequest.getRequestMethod()))
                    .filter(p -> p.getRequestUrl().equalsIgnoreCase(commonRequest.getRequestUrl())).findFirst().get().getRequestName());
        }
    }

    private boolean blockByBlackList(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String ip = IpUtil.getUserIP(request);
        String enLogin = request.getAttribute(EN_LOGIN).toString();
        return commonBlackService.blockByBlackList(remoteAddr,ip,enLogin);
    }

    /**
     * 发送消息 text/html;charset=utf-8
     * @param response
     * @param str
     * @throws Exception
     */
    private  void sendTextMessage(HttpServletResponse response, String str) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(str);
        writer.close();
        response.flushBuffer();
    }

    /**
     * 将某个对象转换成json格式并发送到客户端
     * @param response
     * @param obj
     * @throws Exception
     */
    private void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JsonMapper.obj2String(obj));
        writer.close();
        response.flushBuffer();
    }


    private boolean IsIgnorePath(String uri) {
        if (uri.toLowerCase().startsWith("/m/") || uri.toLowerCase().startsWith("/assets/") || uri.endsWith(".html")) {
            return true;
        }

        List<String> ignoreList= Lists.newArrayList("/common/message/listLastFiveNoReceived.json");
        if(ignoreList.stream().anyMatch(p->p.equalsIgnoreCase(uri))) return true;

        return false;
    }

}

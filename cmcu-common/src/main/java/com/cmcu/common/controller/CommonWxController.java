package com.cmcu.common.controller;

import com.cmcu.common.dto.UserDto;
import com.cmcu.common.service.CommonUserService;
import com.cmcu.common.util.CookieSessionUtils;
import com.common.model.JsonData;
import com.common.wx.model.UserInfo;
import com.common.wx.service.BaseService;
import com.common.wx.service.MessageService;
import com.common.wx.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/wx")
public class CommonWxController {


    @Resource
    CommonUserService commonUserService;


    @RequestMapping("/auth")
    public ModelAndView auth(String code, String url, String state) {
        try {
            if (StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(state)) {
                Map userIdResult = UserService.getUserIdByCode(code);
                if (userIdResult != null) {
                    String wxUserId = userIdResult.get("UserId").toString();
                    log.info(wxUserId);
                    CookieSessionUtils.addSession("UserId",wxUserId);
                    UserDto user = commonUserService.getUserByWxId(state, wxUserId);
                    if (user != null) {
                        if(StringUtils.isEmpty(user.getAvatar())) {
                            UserInfo userInfo = UserService.getUserInfo(wxUserId);
                            if (userInfo!=null&&StringUtils.isNotEmpty(userInfo.getThumb_avatar())) {
                                Map property = Maps.newHashMap();
                                property.put("avatar", userInfo.getThumb_avatar());
                                commonUserService.updateProperty(user.getEnLogin(), property);
                            }
                        }
                        CookieSessionUtils.addSession("enLogin", user.getEnLogin());
                        if (StringUtils.isNotEmpty(url)) return new ModelAndView("redirect:" + url);
                        return new ModelAndView("redirect:/h5/index.html");
                    }else{
                        CookieSessionUtils.addSession("wxUserId",wxUserId);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return new ModelAndView("redirect:/h5/login.html");
    }


    @ResponseBody
    @RequestMapping("/jsApiConfig.json")
    public  JsonData jsApiConfig() {
        Map result = Maps.newHashMap();
        result.put("beta", true);
        result.put("debug", true);
        result.put("appId", BaseService.CORPID);
        result.put("jsApiList", new String[]{"chooseImage", "previewImage", "uploadImage", "downloadImage", "getLocalImgData", "previewFile", "closeWindow"});
        String nonceStr = BaseService.getNonceStr();
        long timestamp = System.currentTimeMillis();
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonceStr);
        JsonData encryptResult = BaseService.getSignature(nonceStr, timestamp, BaseService.DEFAULTURL);
        if (!encryptResult.getRet()) return encryptResult;
        result.put("signature", encryptResult.getData().toString());
        return JsonData.success(result);
    }




    @ResponseBody
    @PostMapping("/getH5FormDataUrl.json")
    public JsonData getH5FormDataUrl(String businessKey) {
        Map result = Maps.newHashMap();
        Map params = Maps.newHashMap();
        result.put("params", params);
        params.put("businessKey", businessKey);
        if(businessKey.startsWith("fiveOaDecisionMaking")){ //决策会议
            result.put("url", "task.oaDecisionMaking");
        }else if(businessKey.startsWith("fiveOaSignQuote")){ //签报
            result.put("url", "task.oaSignQuote");
        }else if(businessKey.startsWith("fiveOaDispatch")){ //公司发文
            result.put("url", "task.oaDispatch");
        }else if(businessKey.startsWith("fiveOaDepartmentPost")){ //部门发文
            result.put("url", "task.oaDepartmentPost");
        }else if(businessKey.startsWith("fiveOaFileInstruction")){ //公司收文
            result.put("url", "task.oaFileInstruction");
        }else if(businessKey.startsWith("fiveOaReport")){ //报告
            result.put("url", "task.oaReport");
        }else if(businessKey.startsWith("fiveBusinessCustomer")){ //客户信息管理
            result.put("url", "task.businessCustomer");
        }else if(businessKey.startsWith("fiveOaBidApply")){ //投标申请表
            result.put("url", "task.oaBidApply");
        }else if(businessKey.startsWith("fiveBusinessContractReview")){ //合同评审
            result.put("url", "task.businessContractReview");
        }else if(businessKey.startsWith("fiveOaSoftware")){ //软件购置申请
            result.put("url", "task.oaSoftware");
        }else if(businessKey.startsWith("oaNoticeApply")){ //信息发布
            result.put("url", "task.oaNoticeApply");
        }else if(businessKey.startsWith("fiveOaInformationPlan")){ //年度软件预算
            result.put("url", "task.oaInformationPlan");
        }else if(businessKey.startsWith("fiveOaInformationEquipmentProcurement")){ //年度信息化采购预算
            result.put("url", "task.oaInformationEquipmentProcurement");
        }else if(businessKey.startsWith("fiveBusinessPurchase")){ //采购合同评审
            result.put("url", "task.businessPurchase");
        }else if(businessKey.startsWith("fiveBusinessSubpackage")){ //分包合同评审
            result.put("url", "task.businessSubpackage");
        } else if(businessKey.startsWith("fiveOaInformationEquipmentApply")){ //信息化设备采购事项审批
            result.put("url", "task.oaInformationEquipmentApply");
        } else if(businessKey.startsWith("fiveBusinessOutAssist")){ //月度外协费费用支出明细
            result.put("url", "task.businessOutAssist");
        }else if(businessKey.startsWith("fiveOaProjectFundPlan")){ //项目资金使用计划
            result.put("url", "task.oaProjectFundPlan");
        }else if(businessKey.startsWith("fiveOaEquipmentAcceptance")){ //行政事务 设备验收
            result.put("url", "task.oaEquipmentAcceptance");
        }else if(businessKey.startsWith("fiveOaFileSynergy")){ //协同文件
            result.put("url", "task.oaFileSynergy");
        }else if(businessKey.startsWith("fiveFinanceBalance")){ //财务资金余额上报
            result.put("url", "task.financeBalance");
        }else if(businessKey.startsWith("fiveOaFurniturePurchase")){ //办公家具采购
            result.put("url", "task.furniturePurchase");
        }
        else if(businessKey.startsWith("fiveAssetScrap")){ //行政事务部-固定资产报废审批
            result.put("url", "task.assetScrap");
        } else if(businessKey.startsWith("fiveOaInlandProjectApply")){ //科研审批流程 -内部科研项目申请
            result.put("url", "task.inlandProjectApply");
        }else if(businessKey.startsWith("fiveOaExternalStandardApply")){ //科研审批流程 -外部标准规范、图集项目申请 发
            result.put("url", "task.externalStandardApply");
        }else if(businessKey.startsWith("oaStampApplyOffice_")){ //用印
            result.put("url", "task.stampOfficeDetail");
        }else if(businessKey.startsWith("fiveBusinessAdvance_")){ //月度预支绩效工资上报
            result.put("url", "task.businessAdvance");
        }
        else {
            result.put("url", "task.commonFormDetail");
        }
        return JsonData.success(result);

    }


    @ResponseBody
    @RequestMapping("/testMessage.json")
    public JsonData testMessage() {

        Map maps=Maps.newHashMap();
        maps.put("title","abcdefg");
        return MessageService.sendCardMsg("test_122",Lists.newArrayList("sunflowermore","YiZhenFeng","WangZaiGeGe"),"http://www.baidu.com","百度一下",maps);
    }


}

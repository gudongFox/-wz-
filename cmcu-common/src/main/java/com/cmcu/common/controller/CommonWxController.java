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
        if(businessKey.startsWith("fiveOaDecisionMaking")){ //????????????
            result.put("url", "task.oaDecisionMaking");
        }else if(businessKey.startsWith("fiveOaSignQuote")){ //??????
            result.put("url", "task.oaSignQuote");
        }else if(businessKey.startsWith("fiveOaDispatch")){ //????????????
            result.put("url", "task.oaDispatch");
        }else if(businessKey.startsWith("fiveOaDepartmentPost")){ //????????????
            result.put("url", "task.oaDepartmentPost");
        }else if(businessKey.startsWith("fiveOaFileInstruction")){ //????????????
            result.put("url", "task.oaFileInstruction");
        }else if(businessKey.startsWith("fiveOaReport")){ //??????
            result.put("url", "task.oaReport");
        }else if(businessKey.startsWith("fiveBusinessCustomer")){ //??????????????????
            result.put("url", "task.businessCustomer");
        }else if(businessKey.startsWith("fiveOaBidApply")){ //???????????????
            result.put("url", "task.oaBidApply");
        }else if(businessKey.startsWith("fiveBusinessContractReview")){ //????????????
            result.put("url", "task.businessContractReview");
        }else if(businessKey.startsWith("fiveOaSoftware")){ //??????????????????
            result.put("url", "task.oaSoftware");
        }else if(businessKey.startsWith("oaNoticeApply")){ //????????????
            result.put("url", "task.oaNoticeApply");
        }else if(businessKey.startsWith("fiveOaInformationPlan")){ //??????????????????
            result.put("url", "task.oaInformationPlan");
        }else if(businessKey.startsWith("fiveOaInformationEquipmentProcurement")){ //???????????????????????????
            result.put("url", "task.oaInformationEquipmentProcurement");
        }else if(businessKey.startsWith("fiveBusinessPurchase")){ //??????????????????
            result.put("url", "task.businessPurchase");
        }else if(businessKey.startsWith("fiveBusinessSubpackage")){ //??????????????????
            result.put("url", "task.businessSubpackage");
        } else if(businessKey.startsWith("fiveOaInformationEquipmentApply")){ //?????????????????????????????????
            result.put("url", "task.oaInformationEquipmentApply");
        } else if(businessKey.startsWith("fiveBusinessOutAssist")){ //?????????????????????????????????
            result.put("url", "task.businessOutAssist");
        }else if(businessKey.startsWith("fiveOaProjectFundPlan")){ //????????????????????????
            result.put("url", "task.oaProjectFundPlan");
        }else if(businessKey.startsWith("fiveOaEquipmentAcceptance")){ //???????????? ????????????
            result.put("url", "task.oaEquipmentAcceptance");
        }else if(businessKey.startsWith("fiveOaFileSynergy")){ //????????????
            result.put("url", "task.oaFileSynergy");
        }else if(businessKey.startsWith("fiveFinanceBalance")){ //????????????????????????
            result.put("url", "task.financeBalance");
        }else if(businessKey.startsWith("fiveOaFurniturePurchase")){ //??????????????????
            result.put("url", "task.furniturePurchase");
        }
        else if(businessKey.startsWith("fiveAssetScrap")){ //???????????????-????????????????????????
            result.put("url", "task.assetScrap");
        } else if(businessKey.startsWith("fiveOaInlandProjectApply")){ //?????????????????? -????????????????????????
            result.put("url", "task.inlandProjectApply");
        }else if(businessKey.startsWith("fiveOaExternalStandardApply")){ //?????????????????? -??????????????????????????????????????? ???
            result.put("url", "task.externalStandardApply");
        }else if(businessKey.startsWith("oaStampApplyOffice_")){ //??????
            result.put("url", "task.stampOfficeDetail");
        }else if(businessKey.startsWith("fiveBusinessAdvance_")){ //??????????????????????????????
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
        return MessageService.sendCardMsg("test_122",Lists.newArrayList("sunflowermore","YiZhenFeng","WangZaiGeGe"),"http://www.baidu.com","????????????",maps);
    }


}

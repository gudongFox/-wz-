package com.cmcu.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmcu.mcc.service.TempFormService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
// 导入对应产品模块的client
import com.tencentcloudapi.cvm.v20170312.CvmClient;
// 导入要请求接口对应的request response类
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;
import com.tencentcloudapi.cvm.v20170312.models.Filter;
//导入可选配置类
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;


/**
 * 
 * @ClassName: SmsTencentUtils
 * @Description: 腾讯云短信服务工具类
 * @author: cxg
 * @date: 2018年6月20日 上午10:02:09
 * @Copyright: All rights reserved.
 * 
 */
public class SmsTencentUtils {

	// 调用腾讯短信接口的唯一标识
	public static final String SDK_APPID = "1400475098";//1302806556

	// 密钥ID；
	public static final String SECRET_ID = "AKIDqcBWcRZQEUgfZKnO6fORO8eEhwFAMk82";
    //密钥KEY
	public static final String SECRET_KEY = "3qp54oa9HCpHaKo9RWLuhFYcavR5fz4u";
	//发送模板
	public static final String TEMPLETE_ID="847207";//密码重置 847217  新设备登录 847207  审批通过847193

	//签名 中国五洲集团
	public static final String SIGN="中国五洲集团";

   	//新设备登录验证码 短信请求
	public static Boolean sendSms(String phone,String checkCode) {
		// 指定腾讯云ID模板发送
		try {
			// 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
			Credential cred = new Credential(SECRET_ID, SECRET_KEY);

			HttpProfile httpProfile = new HttpProfile();
			httpProfile.setEndpoint("sms.tencentcloudapi.com");

			ClientProfile clientProfile = new ClientProfile();
			clientProfile.setHttpProfile(httpProfile);

			SmsClient client = new SmsClient(cred, "", clientProfile);

			SendSmsRequest req = new SendSmsRequest();
			//接受短信电话
			String[] phoneNumberSet1 = {"+86"+phone};
			req.setPhoneNumberSet(phoneNumberSet1);
            //模板ID
			req.setTemplateID(TEMPLETE_ID);
			//签名
			req.setSign(SIGN);

            //模板参数
			String[] templateParamSet1 = {checkCode};
			req.setTemplateParamSet(templateParamSet1);

			req.setSmsSdkAppid(SDK_APPID);
			SendSmsResponse resp = client.SendSms(req);
			SendStatus sendStatusSet = resp.getSendStatusSet()[0];
			System.out.println("发送状态:"+sendStatusSet.getCode());
			return true;
		} catch (Exception   e) {
			//e.printStackTrace();
			System.out.println("发送短信异常!");
			return false;
		}
	}

}

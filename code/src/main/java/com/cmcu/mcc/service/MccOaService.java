package com.cmcu.mcc.service;

import com.common.model.JsonData;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang3.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

public class MccOaService {

    private static final String namespaceURI = "http://tempuri.org/";
    private static final String url="http://oa.cmccltd.com:8888/WebService/LK6SyncService/MrBaseService/MrBaseService.asmx";

    public static  JsonData wmCheckUserByToken(String userCode,String token) throws RemoteException, ServiceException {

            Service service = new Service();
            String operationName = "wmCheckUserByToken";// 调用方法名
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(url);
            call.setOperationName(new QName(namespaceURI, operationName));

            call.addParameter(new QName(namespaceURI, "userCode"),
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(namespaceURI, "token"),
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);
            call.addParameter(new QName(namespaceURI, "ref"),
                org.apache.axis.encoding.XMLType.XSD_STRING,
                javax.xml.rpc.ParameterMode.IN);

            call.addParameter(new QName(namespaceURI, "errMessage"),
                    org.apache.axis.encoding.XMLType.XSD_STRING,
                    ParameterMode.OUT);

            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
            call.setUseSOAPAction(true);
            call.setSOAPActionURI(namespaceURI + operationName);

            String errMessage="";
            String result = (String) call.invoke(new Object[]{userCode,token,errMessage});
            System.out.println(result);
            if(result.equals("0")){
                    return JsonData.success();
            }
            return JsonData.fail(errMessage);

    }

}

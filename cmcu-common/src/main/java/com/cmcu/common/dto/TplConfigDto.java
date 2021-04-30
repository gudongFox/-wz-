package com.cmcu.common.dto;

import com.cmcu.common.util.JsonMapper;
import com.cmcu.common.util.MyStringUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
public class TplConfigDto {

    private String businessKey;

    //是否展示业务附件
    private boolean showBusinessFile;

    //业务附件提示信息
    private String businessFileTip;

    //是否显示文件类型
    private boolean showFileType;

    //文件类型数据字典
    private String fileTypeCode;

    //是否启用文件夹
    private boolean showFileDir;


    private String selectCoFileType;

    //冗余表单字段分期Id
    public int stepId;

    //冗余表单字段专业
    private String majorNames;

    //冗余表单字段子项
    private String buildIds;

    //流程id
    private String processKey;

    //是否集成显示流程信息
    private boolean showProcessIntegration;

    //意见标记点
    private List<String> markRoleNames;
    private String markAddRoleName;

    //有需要保存的
    private boolean saveAble;
    private boolean editable;

    //是否检查数据
    private boolean autoSubmit;

    private List<String> taskList;

    private String formDesc;

    private String formIcon;

    public TplConfigDto(){
        this.businessKey="";
        this.showBusinessFile=true;
        this.showFileType=false;
        this.showProcessIntegration=true;
        this.showFileDir=false;
        this.taskList= Lists.newArrayList();
        this.editable=false;
        this.saveAble=false;
        this.formIcon="icon-clock";
        this.formDesc="流程审批";
        this.markRoleNames=Lists.newArrayList();
        this.markAddRoleName ="";
        this.selectCoFileType="";
        this.autoSubmit=true;
    }

    public static  TplConfigDto getInstance(String configData){
        TplConfigDto config=new TplConfigDto();
        try{
            if(StringUtils.isNotEmpty(configData)) {
                Map data = JsonMapper.string2Map(configData);
                config.showBusinessFile=(boolean)data.getOrDefault("showBusinessFile",false);
                config.showFileType=(boolean)data.getOrDefault("showFileType",false);
                config.businessFileTip=data.getOrDefault("businessFileTip","").toString();
                config.fileTypeCode=data.getOrDefault("fileTypeCode","").toString();
                config.showFileDir=(boolean)data.getOrDefault("showFileDir",false);
                config.processKey=data.getOrDefault("processKey","").toString();
                config.showProcessIntegration=(boolean)data.getOrDefault("showProcessIntegration",false);
                config.markRoleNames= MyStringUtil.getStringList(data.getOrDefault("markRoleNames","").toString());
                config.selectCoFileType=data.getOrDefault("selectCoFileType","").toString();
                config.autoSubmit=(boolean)data.getOrDefault("autoSubmit",true);
            }
        }catch (Exception ex){

        }
        return config;
    }

}

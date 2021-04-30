package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonAttach;
import com.cmcu.common.entity.CommonFile;
import com.cmcu.common.util.JsonMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class CommonFileDto extends CommonFile {

    private CommonAttach commonAttach;

    private String dirPath;

    private long markCount;

    //审定附件Id
    private int signId;

    private Date signTime;

    private String signer;

    private String signMd5;

    private String majorName;

    private String buildName;

    //图层提取模板名称
    public  String extractName;

    //图纸来源文件(电子传递后文件才具有）
    private String sourceFileName;


    private  boolean checked;

    public static CommonFileDto adapt(CommonFile item){
        CommonFileDto dto=new CommonFileDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}

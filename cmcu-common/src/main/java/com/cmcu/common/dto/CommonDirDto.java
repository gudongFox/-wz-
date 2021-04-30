package com.cmcu.common.dto;

import com.cmcu.common.entity.CommonDir;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CommonDirDto extends CommonDir {

    private String dirPath;

    //是否可以上传、新建子文件夹
    private boolean uploadAble;

    //是否可以被删除
    private boolean removeAble;

    private boolean checked;

    private boolean haveChild;

    public static CommonDirDto adapt(CommonDir item){
        CommonDirDto dto=new CommonDirDto();
        BeanUtils.copyProperties(item,dto);
        return dto;
    }

}

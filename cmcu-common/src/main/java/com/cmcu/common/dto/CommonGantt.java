package com.cmcu.common.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class CommonGantt {

    private String name;

    private String desc;

    private Date from;

    private Date to;

    private String label;

    //ganttBlue,ganttRed,ganttOrange,ganttGreen
    private String customClass;





    public Map toMap(){
        Map map= Maps.newHashMap();
        List<Map> values= Lists.newArrayList();
        map.put("name",name);
        map.put("desc",desc);
        map.put("values",values);
        Map detail=Maps.newHashMap();
        values.add(detail);
        detail.put("from",from);
        detail.put("to",to);
        detail.put("label",label);
        detail.put("customClass",customClass);
        return map;
    }
}

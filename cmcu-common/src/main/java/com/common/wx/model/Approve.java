package com.common.wx.model;

import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Data
public class Approve {

    private String sp_no;

    private String sp_name;

    private Integer sp_status;

    private String template_id;

    private Integer apply_time;

    private LinkedHashMap applyer;

    private List<LinkedHashMap> sp_record;

    private List<LinkedHashMap>  notifyer;

    private LinkedHashMap apply_data;

    private List<LinkedHashMap>  comments;

}

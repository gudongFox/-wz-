
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发现场服务记录</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body{
            background-color:#efeff4;
        }
        textarea{
            font-family: Arial;
        }
        .weui-cell__bd{
            padding-right: 6px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="weui-panel">
        <div class="weui-panel__hd">基础信息</div>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_small-appmsg">
                <div class="weui-cells">
                    <a class="weui-cell weui-cell_access" href="/mobile/flow">
                        <div class="weui-cell__hd">
                            <label class="weui-label">流程</label>
                        </div>
                        <div class="weui-cell__bd">项目负责人(罗冬)</div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">编号</label>
                        </div>
                        <div class="weui-cell__bd">${data.formNo}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程名称</label>
                        </div>
                        <div class="weui-cell__bd">${data.projectName}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">创建人</label>
                        </div>
                        <div class="weui-cell__bd">${data.creatorName}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">创建时间</label>
                        </div>
                        <div class="weui-cell__bd"><fmt:formatDate value="${data.gmtCreate }" type="date" pattern="yyyy-MM-dd HH:mm"/></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="weui-panel__hd">填写信息</div>
        <div class="weui-panel__bd">
            <div class="weui-media-box weui-media-box_small-appmsg">
                <div class="weui-cells">
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">项目阶段</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="stageName" value="${data.stageName}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">施工单位</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionOrg" value="${data.constructionOrg}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">服务时间</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="serviceTime" id="serviceTime" placeholder="请选择" value="${data.serviceTime}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">服务人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="serviceMen" value="${data.serviceMen}">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="part-actFileFill.jsp"/>
</div>

<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="/assets/js/weui.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        $('#picker1').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#serviceTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'serviceTime'
            });
        });
    });
</script>
</body>
</html>
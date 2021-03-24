<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发任务书</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #efeff4;
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
                            <label class="weui-label">项目名称</label>
                        </div>
                        <div class="weui-cell__bd">${data.projectName}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程号</label>
                        </div>
                        <div class="weui-cell__bd">${data.projectNo}</div>
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
                        <div class="weui-cell__bd"><fmt:formatDate value="${data.gmtCreate }" type="date"
                                                                   pattern="yyyy-MM-dd HH:mm"/></div>
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
                            <label class="weui-label">建设单位</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionOrg" value=${data.constructionOrg}>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程地址</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionAddress" value=${data.constructionAddress}>
                        </div>
                    </div>

                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">工程规模</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionScale" value=${data.constructionScale}>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">联系人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionLink" value=${data.constructionLink}>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">联系电话</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="constructionTel" value=${data.constructionTel}>
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">交图时间</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="planDesignFinish" id="pickerContent1" placeholder="请选择" value="${data.planDesignFinish}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker2">
                        <div class="weui-cell__hd">
                            <label class="weui-label">质量要求</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="constructionQuality" id="pickerContent2" placeholder="请选择" value="${data.constructionQuality}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>备注</p>
                        </div>
                        <!--<div class="weui-cell__ft">
                            <textarea class="weui-textarea" rows="3" name="remark" placeholder="请输入内容" ></textarea>
                        </div>-->
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="remark" placeholder="请输入备注内容" >${data.remark}</textarea>
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
    var str = "${data.planDesignFinish}";
    var dateArr = str.split("-");
    $(function(){
        $('#picker1').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [dateArr[0], dateArr[1], dateArr[2]],
                onConfirm: function(result){
                    $('#pickerContent1').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'picker1'
            });
        });
    });

    $(function(){
        $('#picker2').on('click', function () {
            weui.picker([
                {
                    label: '合格',
                    value: '合格'
                },
                {
                    label: '省市级优秀设计',
                    value: '省市级优秀设计'
                },
                {
                    label: '国家级优秀设计',
                    value: '国家级优秀设计'
                }
            ], {
                className: 'constructionQuality',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#pickerContent2').val(result);
                },
                id: 'pickerContent2'
            });
        });
    });
</script>
</body>
</html>
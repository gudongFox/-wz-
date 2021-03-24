
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计过程函件</title>
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
                            <label class="weui-label">项目名称</label>
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
                            <label class="weui-label">主题</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="subject" value="${data.subject}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker2">
                        <div class="weui-cell__hd">
                            <label class="weui-label">紧急程度</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="letterLevel" id="letterLevel" placeholder="请选择" value="${data.letterLevel}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">收件方</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="receiveCompany" value="${data.receiveCompany}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">收件人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="receiver" value="${data.receiver}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">发件方</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="sendCompany" value="${data.sendCompany}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">发件人</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="sender" value="${data.sender}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">传真号</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="faxNo" value="${data.faxNo}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">电话号</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="telNo" value="${data.telNo}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">日期</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="letterDate" id="letterDate" placeholder="请选择" value="${data.letterDate}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">编号</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="letterNo" value="${data.letterNo}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>内容</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="letterContent" placeholder="请输入内容" >${data.remark}</textarea>
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
                    $('#letterDate').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'letterDate'
            });
        });
    });

    $(function(){
        $('#picker2').on('click', function () {
            weui.picker([
                {
                    label: '一般',
                    value: '一般'
                },
                {
                    label: '特别紧急',
                    value: '特别紧急'
                }
            ], {
                className: 'letterLevel',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#letterLevel').val(result);
                },
                id: 'letterLevel'
            });
        });
    });
</script>
</body>
</html>
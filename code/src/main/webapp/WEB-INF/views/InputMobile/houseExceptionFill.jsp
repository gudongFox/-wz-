
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>重大例外情况处理记录</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body{
            background-color:#efeff4;
        }
        textarea{
            font-family: Arial;
        }
        .weui-cell__hd{
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
                            <label class="weui-label">合同号</label>
                        </div>
                        <div class="weui-cell__bd">${data.contractNo}</div>
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
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">重大例外类别</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="exceptionType" id="exceptionType" placeholder="请选择" value="${data.exceptionType}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>一、重大例外情况简述</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionDesc" placeholder="请输入重大例外情况简述" >${data.exceptionDesc}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>二、本项目允许按以下简化、变通程序进行设计</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionMethod" placeholder="请输入重大例外情况简述" >${data.exceptionMethod}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>三、按顾客意见或经过顾客同意，本设计依据下列设想的前提条件和基础数据</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionBase" placeholder="请输入重大例外情况简述" >${data.exceptionBase}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>四、在下一阶段设计前必须解决的问题</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionSolve" placeholder="请输入重大例外情况简述" >${data.exceptionSolve}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>五、对有关专业保证质量的要求</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionQuality" placeholder="请输入重大例外情况简述" >${data.exceptionQuality}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>六、对质量保证要求的验证</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionQualityValidation" placeholder="请输入重大例外情况简述" >${data.exceptionQualityValidation}</textarea>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <p>七、无法验证的风险分析及其应对措施</p>
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__bd">
                            <textarea class="weui-textarea" rows="3" name="exceptionNoValidation" placeholder="请输入重大例外情况简述" >${data.exceptionNoValidation}</textarea>
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
            weui.picker([
                {
                    label: '轻微',
                    value: '轻微'
                },
                {
                    label: '一般',
                    value: '一般'
                },
                {
                    label: '严重',
                    value: '严重'
                }
            ], {
                className: 'exceptionType',
                container: 'body',
                defaultValue: [0],
                onConfirm: function (result) {
                    $('#exceptionType').val(result);
                },
                id: 'exceptionType'
            });
        });
    });
</script>
</body>
</html>
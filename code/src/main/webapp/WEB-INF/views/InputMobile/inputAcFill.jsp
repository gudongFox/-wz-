
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>设计与开发输入清单及评审记录(建筑)</title>
    <link href="/assets/js/weui.min.css" rel="stylesheet"/>
    <style>
        body{
            background-color:#efeff4;
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
                            <label class="weui-label">合同号</label>
                        </div>
                        <div class="weui-cell__bd">${data.contractNo}</div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">评审阶段</label>
                        </div>
                        <div class="weui-cell__bd">${data.stageName}</div>
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
                            <label class="weui-label">总面积</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="totalArea" value="${data.totalArea}">
                        </div>
                    </div>

                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">总高度</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="totalHeight" value="${data.totalHeight}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">层数</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="floorNum" value="${data.floorNum}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">容积率</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="plotRatio" value="${data.plotRatio}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">建筑类别</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="buildCategory" value="${data.buildCategory}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">覆盖率</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="coverPercent" value="${data.coverPercent}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">耐火等级</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="fireLevel" value="${data.fireLevel}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">停车数</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="parkNum" value="${data.parkNum}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">人防等级</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="peopleLevel" value="${data.peopleLevel}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">绿地率</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="greenPercent" value="${data.greenPercent}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">退红线</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="redLine" value="${data.redLine}">
                        </div>
                    </div>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">装修标准</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="decorateGrade" value="${data.decorateGrade}">
                        </div>
                    </div>
                    <a class="weui-cell weui-cell_access" id="picker1">
                        <div class="weui-cell__hd">
                            <label class="weui-label">合同/标书/委托</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="contractReceiveTime" id="contractReceiveTime" placeholder="请选择" value="${data.contractReceiveTime}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker2">
                        <div class="weui-cell__hd">
                            <label class="weui-label">产品要求评审记录</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="reviewReceiveTime" id="reviewReceiveTime" placeholder="请选择" value="${data.reviewReceiveTime}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker3">
                        <div class="weui-cell__hd">
                            <label class="weui-label">项目立项批文</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="projectApproveReceiveTime" id="projectApproveReceiveTime" placeholder="请选择" value="${data.projectApproveReceiveTime}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker4">
                        <div class="weui-cell__hd">
                            <label class="weui-label">用地红线图</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="redLineReceiveTime" id="redLineReceiveTime" placeholder="请选择" value="${data.redLineReceiveTime}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker5">
                        <div class="weui-cell__hd">
                            <label class="weui-label">规划许可证</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="planLicenseReceiveTime" id="planLicenseReceiveTime" placeholder="请选择" value="${data.planLicenseReceiveTime}">
                        </div>
                    </a>
                    <a class="weui-cell weui-cell_access" id="picker6">
                        <div class="weui-cell__hd">
                            <label class="weui-label">顾客要求</label>
                        </div>
                        <div class="weui-cell__ft" >
                            <input type="text" class="weui-input" name="customerQualityReceiveTime" id="customerQualityReceiveTime" placeholder="请选择" value="${data.customerQualityReceiveTime}">
                        </div>
                    </a>
                    <div class="weui-cell">
                        <div class="weui-cell__hd">
                            <label class="weui-label">备注</label>
                        </div>
                        <div class="weui-cell__bd">
                            <input type="text" class="weui-input" name="remark" value="${data.remark}">
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
                    $('#contractReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'contractReceiveTime'
            });
        });
    });
    $(function(){
        $('#picker2').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#reviewReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'reviewReceiveTime'
            });
        });
    });
    $(function(){
        $('#picker3').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#projectApproveReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'projectApproveReceiveTime'
            });
        });
    });
    $(function(){
        $('#picker4').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#redLineReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'redLineReceiveTime'
            });
        });
    });
    $(function(){
        $('#picker5').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#planLicenseReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'planLicenseReceiveTime'
            });
        });
    });
    $(function(){
        $('#picker6').on('click', function () {
            weui.datePicker({
                start: 2010,
                end: 2050,
                defaultValue: [2019, 6, 9],
                onConfirm: function(result){
                    $('#customerQualityReceiveTime').val(result[0]+"-"+result[1]+"-"+result[2]);
                },
                id: 'customerQualityReceiveTime'
            });
        });
    });
</script>
</body>
</html>
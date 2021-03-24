<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">

    .print_table{
        border-collapse: collapse;
        width: 100%;
        font-size: 16px;
        color: red;
    }
    .print_table td {
        border: solid red 1px;
        color: red;
        height: 60px;
        text-align: center;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
        word-wrap:break-word;
        word-break:break-all;
    }
    #div1_line {
        width: 270px;
        border-top: 2px solid red;
        text-align: center;
    }

</style>
<%--不会签 打印模板--%>
<div id="print_area" hidden>
    <h3 style="text-align: center; color: red">中 国 五 洲 工 程 设 计 集 团</h3>
    <h1 style="text-align: center; color: red">签&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报</h1>
    <div id="div1_line" style="margin-left:220px;"></div>
    <table class="print_table">
        <tr>
            <td style="width:15%;">送签部门:</td>
            <td colspan="2" style=" ;color: black;" ng-bind="vm.printData.deptName"></td>
            <td style="width:15%;">负责人:</td>
            <td style="width:18%; color: black;" ng-bind="vm.printData.deptChargeManName"></td>
            <td style="width:15%;">公司办编号:</td>
            <td style="width:20%; color: black;" ng-bind="vm.printData.companyNo"></td>
        </tr>
        <tr>
            <td style="width:15%;">事项:</td>
            <td colspan="6" style="color: black;text-align:left;padding-left:15px;" ng-bind="vm.printData.item"></td>
        </tr>
        <tr>
            <td colspan="3" style="border-top: solid 2px;border-right: solid 1px;">是否属于“三重一大”事项:</td>
            <td colspan="4" style="text-align:left;padding-left:37px;border-top: solid 2px;">公司领导批示:</td>
        </tr>
        <tr>
            <td colspan="3" style="height:120px;border-right: solid red 1px;color: black;" ng-bind="vm.printData.belongThreeOne"></td>
            <td colspan="4" rowspan="5" style="text-align:left;vertical-align:top;color: black;padding-left:37px;padding-right:5px;border-bottom: solid red 2px;">
                <div ng-repeat="node in vm.printData.leaderList" ng-if="node.userName!=null">
                    <span ng-bind="node.activityName"></span>
                    <span ng-bind="node.comment"></span>
                    <span ng-bind="node.userName"></span>
                    <img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+node.userLogin}}">
                    <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="3" style="border-top: solid 2px;border-right: solid 1px;">是否进行会签:</td>
        </tr>
        <tr>
            <td colspan="3" style="color: black;height:120px;border-right: solid red 1px;" >否</td>
        </tr>
        <tr>
            <td colspan="3" style="border-top: solid 2px;border-right: solid 1px;">公司办:</td>
        </tr>
        <tr>
            <td colspan="3" style="color: black;height:240px;border-right: solid red 1px;border-bottom: solid red 2px;vertical-align: top;">
                <P ng-bind="vm.printData.companyOffice.comment"></P>
                <P ng-bind="vm.printData.companyOffice.userName"></P>
                <p ng-if="vm.printData.companyOffice.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyOffice.userLogin}}"></p>
                <P ng-bind="vm.printData.companyOffice.end|date:'yyyy-MM-dd'"></P>
            </td>
        </tr>
        <tr>
            <td>经办人:</td>
            <td style="color: black" colspan="2" ng-bind="vm.printData.agentName"></td>
            <td >公司办核收:</td>
            <td style="color: black" ng-bind="vm.printData.companyCheckManName"></td>
            <td >报送日期:</td>
            <td style="color: black" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>


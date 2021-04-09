<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style_common">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 16px;
        color: red;
    }
    .print_table td {
        border: solid red 1px;
        color: red;
        height: 50px;
        text-align: center;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
        word-wrap:break-word;
        word-break:break-all;
    }

</style>
<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<%--通用会签单 打印模板--%>
<div id="print_area_common" hidden>
    <h3 style="text-align: center; color: red">中 国 五 洲 工 程 设 计 集 团</h3>
    <h1 style="text-align: center; color: red">签&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;报</h1>
    <div id="div1_line" style="margin-left:220px;"></div>
    <table class="print_table">
        <tr>
            <td style="width:15%;">送签部门:</td>
            <td colspan="2" style=";color: black;" ng-bind="vm.printData.deptName"></td>
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
            <td colspan="3" style="color: black;height:120px;border-right: solid red 1px;" >是</td>
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

    <div  style="page-break-before:always">
        <h1 style="text-align: center; color: red">通用会签单</h1>
        <table class="print_table">
            <tr>
                <td style="width:15%;">事项:</td>
                <td colspan="3" style="text-align:left;padding-left:15px;color: black;" ng-bind="vm.printData.item"></td>
            </tr>
            <tr>
                <td colspan="4" style="vertical-align:top;border-top: solid 2px;text-align:left;padding-left:37px;">法律审查:</td>
            </tr>
            <tr>
                <td colspan="4" style="height:80px;border-bottom:solid 2px;text-align:left;vertical-align:top;padding-left:37px;padding-right:15px;">
                    <span ng-bind="vm.printData.legalReview.comment"></span>
                    <span ng-bind="vm.printData.legalReview.userName"></span>
                    <span ng-if="vm.printData.legalReview.userName.length>0"><img style="width: 90px;height: 35px;"  ng-src="{{'/sys/attach/downloadPic/'+function.actHistory.userLogin}}"></span>
                    <span ng-bind="vm.printData.legalReview.end|date:'yyyy-MM-dd'"></span>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="width:50%;text-align:left;padding-left:37px;border-right: solid 1px;border-bottom: solid 1px;">职能部门会签:</td>
                <td colspan="2" style="width:50%;text-align:left;padding-left:37px;border-bottom: solid 1px;">生产单位会签:</td>
            </tr>
            <tr>
                <td colspan="2" style="height:550px;text-align:left;vertical-align:top;padding-top:15px;padding-left:37px;padding-right:15px;border-right: solid 1px;border-bottom: solid red 2px;">
                    <div ng-repeat="design in vm.printData.functionList">
                        <p ng-bind="design.deptName"></p>
                        <span ng-bind="design.actHistory.comment"></span>
                        <span ng-bind="design.actHistory.userName"></span>
                        <span ng-if="design.actHistory.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+design.actHistory.userLogin}}"></span>
                        <span ng-bind="design.actHistory.end|date:'yyyy-MM-dd'"></span>
                    </div>
                </td>
                <td colspan="2" style="text-align:left;vertical-align:top;padding-top:15px;padding-left:37px;padding-right:15px;border-bottom: solid red 2px;">
                    <div ng-repeat="function in vm.printData.designList">
                        <p ng-bind="function.deptName"></p>
                        <span ng-bind="function.actHistory.comment"></span>
                        <span ng-bind="function.actHistory.userName"></span>
                        <span ng-if="function.actHistory.userName.length>0"><img style="width: 90px;height: 35px;"  ng-src="{{'/sys/attach/downloadPic/'+function.actHistory.userLogin}}"></span>
                        <span ng-bind="function.actHistory.end|date:'yyyy-MM-dd'"></span>
                    </div>
                </td>
            </tr>
        </table>
        <table class="print_table">
            <tr>
                <td>部门:</td>
                <td style="color: black" colspan="2" ng-bind="vm.printData.deptName"></td>
                <td>负责人:</td>
                <td style="color: black" ng-bind="vm.printData.deptChargeManName"></td>
                <td>经办人:</td>
                <td style="color: black" ng-bind="vm.printData.creatorName"></td>
                <td>报送日期:</td>
                <td style="color: black" ng-bind="vm.printData.submitTime"></td>
            </tr>
        </table>
    </div>
</div>


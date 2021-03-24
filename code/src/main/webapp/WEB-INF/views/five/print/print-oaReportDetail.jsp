<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
        color: #0bb20c;
    }
    .print_table td {
        border: solid #0bb20c 1px;
        color: #0bb20c;
        height: 60px;
        text-align: center;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
        word-wrap:break-word;
        word-break:break-all;
    }
    #div1 {
        width: 270px;
        border-top: 2px solid #0bb20c;
        text-align: center;
    }

</style>


<div id="print_area" hidden>
    <h3 style="text-align: center; color: #0bb20c">中 国 五 洲 工 程 设 计 集 团</h3>
    <h2 style="text-align: center; color: #0bb20c">报&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;告</h2>
    <div id="div1" style="margin-left:220px;"></div>
    <table class="print_table">
        <tr>
            <td style="width:15%;">办公室主任:</td>
            <td style="width:20%; color: black;" ng-bind="vm.printData.companyOfficeManName"></td>
            <td style="width:15%;">批示领导:</td>
            <td style="width:17%; color: black;" ng-bind="vm.printData.companyLeaderName"></td>
            <td style="width:15%;">分发方式:</td>
            <td style="width:18%; color: black;" ng-bind="vm.printData.flag"></td>
        </tr>
        <tr>
            <td style="width:15%;">副职领导:</td>
            <td style="width:20%; color: black;" ng-bind="vm.printData.viceLeaderName"></td>
            <td style="width:15%;">送签部门:</td>
            <td style="width:17%; color: black;" ng-bind="vm.printData.deptName"></td>
            <td style="width:15%;">公司编号:</td>
            <td style="width:18%; color: black;" ng-bind="vm.printData.officeNo"></td>
        </tr>
        <tr>
        </tr>
        <tr>
            <td colspan="6" style="text-align:left;padding-left:37px;border-top: solid 2px;">事项:</td>
        </tr>
        <tr>
            <td colspan="6" style="height:600px;border-bottom: solid #0bb20c 2px;text-align:left;vertical-align:top;padding-left:15px;padding-right:15px;padding-top:25px;color: black" ng-bind="vm.printData.reportContent"></td>
        </tr>
        <tr>
            <td style="width:15%;">经办人:</td>
            <td style="width:20%;border-bottom: solid #0bb20c 1px;color: black" colspan="2" ng-bind="vm.printData.chargeManName"></td>
            <td style="width:15%;">报送日期:</td>
            <td style="width:18%;border-bottom: solid #0bb20c 1px;color: black" colspan="2" ng-bind="vm.printData.reportTime"></td>
        </tr>
    </table>
</div>
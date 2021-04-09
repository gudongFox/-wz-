<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style">
    table {
        border-collapse: collapse;
        width: 100%;
        font-size: 17px;
    }
    .print_table td {
        border: solid red 1px;
        height: 45px;
        text-align: left;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
    }
    #div1 {
        width: 800px;
        border-top: 2px solid red;
        text-align: center;
    }

</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>

    <h1 style="text-align: center; color: red;font-size: 36px">中国五洲工程设计集团有限公司<span ng-bind="vm.printData.meetType"></span></h1>

    <h2 style="text-align: center; " ng-bind="vm.printData.item">（2020年第一届董事会第10次）</h2>

    <table class="print_table">
        <tr >
            <td style="width:20%;border-top: 2px solid red;font-size: 24px;padding-left:40px;"><b>时&nbsp;&nbsp;&nbsp;&nbsp;间:</b></td>
            <td style="width:80%;border-top: 2px solid red;font-size: 24px" ng-bind="vm.printData.meetingTimePlan"></td>
        </tr>
        <tr>
            <td style="padding-left:40px;font-size: 24px;"><b>地&nbsp;&nbsp;&nbsp;&nbsp;点:</b></td>
            <td style="font-size: 24px;" ng-bind="vm.printData.meetingTime"></td>
        </tr>
        <tr>
            <td style="width:20%;padding-left:40px;font-size: 24px;"><b>主持人:</b></td>
            <td style="font-size: 24px;" ng-bind="vm.printData.compereName"></td>
        </tr>
        <tr>
            <td style="width:20%;padding-left:40px;vertical-align:top;padding-top:40px;font-size: 24px;"><b>议&nbsp;&nbsp;&nbsp;&nbsp;题:</b></td>
            <td style="font-size: 24px;"  ></td>
        </tr>
        <tr  ng-repeat="detail in vm.printData.detailList">
            <td style="width:10%;padding-left:20px;font-size: 24px;text-align: right" ng-bind="detail.array" valign="top"></td>
            <td style="font-size: 24px;width: 80%" valign="top"><p  ng-bind="detail.title"></p></td>
        </tr>
        <tr>
            <td style="padding-left:40px;font-size: 24px;"><b>出&nbsp;&nbsp;&nbsp;&nbsp;席:</b></td>
            <td style="font-size: 24px; " ng-bind="vm.printData.companyLeaderName"></td>
        </tr>
        <tr>
            <td style="padding-left:40px;font-size: 24px;"><b>列&nbsp;&nbsp;&nbsp;&nbsp;席:</b></td>
            <td style="font-size: 24px;" ng-bind="vm.printData.attenderName"></td>
        </tr>
        <tr ng-repeat="att in vm.printData.attenderName2">
            <td style="padding-left:40px;font-size: 24px;"></td>
            <td style="font-size: 24px;" ng-bind="att"></td>
        </tr>
        <tr>
            <td style="padding-left:40px;font-size: 24px;"><b>通&nbsp;&nbsp;&nbsp;&nbsp;知:</b></td>
            <td style="font-size: 24px;"  ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td style="padding-left:40px;font-size: 24px;"><b>记&nbsp;&nbsp;&nbsp;&nbsp;录:</b></td>
            <td style="font-size: 24px;"  ng-bind="vm.printData.recordManName"></td>
        </tr>
    </table>
</div>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area">
    <h2 style="text-align: center;">专家评审意见表</h2>
    <table class="print_table">
        <tr>
            <td style="width:20%;height: 50px;">会议名称</td>
            <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.printData.conferenceName"></td>
        </tr>
        <tr>
            <td style="width:20%;height: 50px;" colspan="1">专家签名</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.expertSign"></td>
            <td style="width: 20%;" colspan="1">评审专业</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.specialty"></td>
        </tr>
        <tr>
            <td style="width:20%;height: 50px;" colspan="1">时间</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.time"></td>
            <td style="width: 20%;" colspan="1">地点</td>
            <td style="width: 30%;" colspan="1" ng-bind="vm.printData.place"></td>
        </tr>

        <tr>
            <td colspan="4" style="height: 600px;text-align: left;padding-left: 30px;padding-right: 30px;padding-top: 20px;padding-bottom: 550px">评审意见:</br><span ng-bind="vm.printData.reviewContent" ></span></td>
            </td>
        </tr>
    </table>
</div>
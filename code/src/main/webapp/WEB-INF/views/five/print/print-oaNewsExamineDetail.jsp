<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 55px;
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">五洲集团新闻宣传及信息报送审查单</h3>
    <table class="print_table">
        <tr>
            <td style="width:25%;">标题</td>
            <td colspan="3" ng-bind="vm.item.title"></td>
        </tr>
        <tr>
            <td style="width:25%;">报送单位</td>
            <td style="width:25%;" ng-bind="vm.item.deptName"></td>
            <td style="width:25%;">报送日期</td>
            <td style="width:25%;" ng-bind="vm.item.sendTime"></td>
        </tr>
        <tr>
            <td>作者</td>
            <td ng-bind="vm.item.authorName"></td>
            <td>联系电话</td>
            <td ng-bind="vm.item.authorLink"></td>
        </tr>
        <tr>
            <td style="height:90px;">建议发布平台</td>
            <td colspan="3" style="padding-left:15px;padding-left:15px" ng-bind="vm.item.publishingPlatform"></td>

        </tr>
        <tr>
            <td rowspan="2">部门/单位审查意见</td>
            <td colspan="3" style="height:90px;vertical-align:top;padding-left:15px;padding-left:15px;padding-top:5px;" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">部门/单位负责人:</td>
            <td>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td  ng-bind="'日期：'+vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">党群工作部审查意见</td>
            <td colspan="3" style="height:90px;vertical-align:top;padding-left:15px;padding-left:15px;padding-top:5px;" ng-bind="vm.printData.partyChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="text-align:right;">党群工作部负责人:</td>
            <td>
                <span ng-bind="vm.printData.partyChargeMen.userName"></span>
                <span ng-if="vm.printData.partyChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.partyChargeMen.userLogin}}"></span>
            </td>
            <td  ng-bind="'日期：'+vm.printData.partyChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>

    </table>
</div>
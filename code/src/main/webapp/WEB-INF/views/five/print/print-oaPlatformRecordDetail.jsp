<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 45px;
        text-align: left;
        padding-left:5px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">各地公共资源平台备案情况</h3>
    <table class="print_table">
        <tr>
            <td style="width:12%;border-right:none">单位名称:</td>
            <td style="border-right:none;border-left:none;" colspan="3" ng-bind="vm.printData.deptName"></td>
            <td style="text-align:right;width:12%;border-right:none;border-left:none;">备案流水号:</td>
            <td style="width:13%;border-right:none;border-left:none;" ng-bind="vm.printData.recordNo"></td>
            <td style="text-align:right;width:12%;border-right:none;border-left:none;">发起申请时间:</td>
            <td style="width:13%;border-left:none;" ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
        </tr>
        <tr>
            <td colspan="8" style="width:12%;border-bottom:none">备案信息:</td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:8%;">备案人</td>
            <td style="width:8%;">备案时间</td>
            <td style="width:8%;">备案省份</td>
            <td style="width:9%;">备案城市或地区</td>
            <td style="width:9%;">备案平台名称</td>
            <td style="width:9%;">备案平台网址</td>
            <td style="width:9%;">备案内容</td>
            <td style="width:9%;">备案有效期</td>
            <td style="width:9%;">是否密码锁</td>
            <td style="width:9%;">密码管理人</td>
            <td style="width:10%;">备注</td>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="printDetail.recordMan"></td>
            <td ng-bind="printDetail.recordTime"></td>
            <td ng-bind="printDetail.province"></td>
            <td ng-bind="printDetail.city"></td>
            <td ng-bind="printDetail.platformName"></td>
            <td ng-bind="printDetail.platformUrl"></td>
            <td ng-bind="printDetail.recordContent"></td>
            <td ng-bind="printDetail.recordValidity"></td>
            <td >
                <span ng-if="printDetail.password">是</span>
                <span ng-if="!printDetail.password">否</span>
            </td>
            <td ng-bind="printDetail.passwordCustodian"></td>
            <td ng-bind="printDetail.remark"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width:16%;border-top:none">申请人</td>
            <td style="width:17%;border-top:none">
                <span ng-if="vm.printData.startMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.startMen.userLogin}}"></span>
                <span ng-bind="vm.printData.startMen.userName"></span>
                <span ng-bind="vm.printData.startMen.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width:16%;border-top:none">单位领导</td>
            <td style="width:17%;border-top:none">
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width:17%;border-top:none">经营发展部</td>
            <td style="width:17%;border-top:none">
                <span ng-if="vm.printData.manageDevelopmentDept.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.manageDevelopmentDept.userLogin}}"></span>
                <span ng-bind="vm.printData.manageDevelopmentDept.userName"></span>
                <span ng-bind="vm.printData.manageDevelopmentDept.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

    </table>
</div>
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
        text-align: center;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">协会交费申请表</h3>
    <p style="text-align:right;padding-right:25px;" ng-bind="'日期:'+vm.printData.gmtCreate"></p>
    <table class="print_table">
        <tr>
            <td style="width:15%;">协会编号</td>
            <td ng-bind="vm.item.associationNo"></td>
            <td style="width:15%;">申请人</td>
            <td ng-bind="vm.item.handleManName"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:30px;background: #dcdcdc;">协会基本信息:</td>
        </tr>
        <tr>
            <td>协会（学会）名称</td>
            <td colspan="3" ng-bind="vm.item.associationName"></td>
        </tr>
        <tr>
            <td>主管单位</td>
            <td ng-bind="vm.item.deptChargeName"></td>
            <td>公司角色</td>
            <td ng-bind="vm.item.associationRole"></td>
        </tr>
        <tr>
            <td>公司代表</td>
            <td ng-bind="vm.item.recommendMan"></td>
            <td>联系人</td>
            <td ng-bind="vm.item.linkMan"></td>
        </tr>
        <tr>
            <td>会费</td>
            <td ng-bind="vm.item.associationFee"></td>
            <td colspan="2"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:30px;background: #dcdcdc;">[1]交费信息:</td>
        </tr>
        <tr>
            <td>交费时间</td>
            <td ng-bind="vm.item.paymentTime"></td>
            <td>交费金额（元）</td>
            <td ng-bind="vm.item.paymentMoney"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:30px;background: #dcdcdc;">[2]信息化建设与管理部审批:</td>
        </tr>
        <tr>
            <td colspan="4"  style="height:120px;text-align:left;vertical-align:top;padding-left:15px;padding-right:15px;padding-top: 15px;">
                <P ng-bind="vm.printData.informationViceOffice.comment"></P>
                <span ng-bind="vm.printData.informationViceOffice.userName"></span>
                <span ng-if="vm.printData.informationViceOffice.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.informationViceOffice.userLogin}}"></span>
                <span ng-bind="vm.printData.informationViceOffice.end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>

    </table>
</div>
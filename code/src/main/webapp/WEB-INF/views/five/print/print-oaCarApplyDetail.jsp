<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table1 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table1 td {
        border: solid #000 1px;
        height: 45px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
    .print_table2 {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table2 td {
        border: solid #000 1px;
        height: 45px;
        text-align: left;
        padding-left:10px;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">用车申请 </h3>
    <table class="print_table1">
        <tr>
            <td style="width:15%;">开始时间</td>
            <td style="width:35%;" ng-bind="vm.printData.beginTime"></td>
            <td style="width:15%;">结束时间</td>
            <td style="width:35%;" ng-bind="vm.printData.endTime"></td>
        </tr>
        <tr>
            <td>申请车辆</td>
            <td ng-bind="vm.printData.carName"></td>
            <td>申请部门</td>
            <td ng-bind="vm.printData.deptName"></td>
        </tr>
        <tr>
            <td>用车类别</td>
            <td ng-bind="vm.printData.applyType"></td>
            <td>用车事项</td>
            <td ng-bind="vm.printData.applyReason"></td>
        </tr>
        <tr>
            <td><b>司机</b></td>
            <td ng-bind="vm.printData.driverName"></td>
            <td>用车目的地</td>
            <td ng-bind="vm.printData.destination"></td>
        </tr>
        <tr>
            <td>乘客</td>
            <td ng-bind="vm.printData.passenger"></td>
            <td>乘坐人数</td>
            <td ng-bind="vm.printData.userNum"></td>
        </tr>
        <tr>
            <td style="height:145px;">车辆详情</td>
            <td colspan="3" style="text-align:left;padding-left:15px;padding-left:15px;padding-top:15px;vertical-align:top" ng-bind="vm.printData.carInfo"></td>
        </tr>
        <tr>
            <td>备注</td>
            <td colspan="3" ng-bind="vm.printData.remark"></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:left;padding-left:10px;height:30px;"><b>司机回执单</b></td>
        </tr>
        <tr>
            <td>里程数</td>
            <td ng-bind="vm.printData.mileage"></td>
            <td>油费</td>
            <td ng-bind="vm.printData.soilPay"></td>
        </tr>
        <tr>
            <td>过路/桥费</td>
            <td ng-bind="vm.printData.passPay"></td>
            <td>停车费</td>
            <td ng-bind="vm.printData.partPay"></td>
        </tr>


    </table>
    <table class="print_table2">
        <tr>
            <td colspan="8" style="height:30px;border-top:none;"><b>审核意见</b></td>
        </tr>
        <tr>
            <td colspan="4" style="width:50%;">申请人：</td>
            <td colspan="4" >部门领导：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:80px;vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.startMen.comment"></td>
            <td colspan="4" style="vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.deptMen.comment"></td>
        </tr>
        <tr>
            <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
            <td style="width:19%;border-right:none;border-left:none;border-top:none;">
                <span ng-bind="vm.printData.startMen.userName"></span>
                <span ng-if="vm.printData.startMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.startMen.userLogin}}"></span>
            </td>
            <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
            <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.startMen.end|date:'yyyy-MM-dd'"></td>
            <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
            <td style="width:19%;border-right:none;border-left:none;border-top:none;">
                <span ng-bind="vm.printData.deptMen.userName"></span>
                <span ng-if="vm.printData.deptMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptMen.userLogin}}"></span>
            </td>
            <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
            <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.deptMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="width:50%;">单位负责人：</td>
            <td colspan="4" >公司办负责人：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:80px;vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.deptChargeMen.comment"></td>
            <td colspan="4" style="vertical-align:top;padding-top:10px;border-bottom:none;" ng-bind="vm.printData.companyChargeMen.comment"></td>
        </tr>
        <tr>
            <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
            <td style="width:19%;border-right:none;border-left:none;border-top:none;">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
            <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
            <td style="width:8%;border-right:none;border-top:none;text-align: right;">签字：</td>
            <td style="width:19%;border-right:none;border-left:none;border-top:none;">
                <span ng-bind="vm.printData.companyChargeMen.userName"></span>
                <span ng-if="vm.printData.companyChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyChargeMen.userLogin}}"></span>
            </td>
            <td style="width:8%;border-right:none;border-left:none;border-top:none;">日期：</td>
            <td style="width:15%;border-left:none;border-top:none;" ng-bind="vm.printData.companyChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 50px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">参加外部技术交流会议审批</h3>
    <table class="print_table" width="100px">
        <tr>
            <td style="width:18%;">会议名称</td>
            <td  colspan="3"  style="text-align: left;padding-left: 25px;" ng-bind="vm.item.meetName"></td>
        </tr>
        <tr>
            <td>会议通知</td>
            <td  colspan="3" style="text-align: left;padding-left: 25px;" ng-bind="vm.item.meetNotice"></td>
        </tr>
        <tr>
            <td rowspan="5" >专家委审批意见</td>
            <td style="width:18%;border-right:none;text-align:left;padding-left:15px;">1是否派人参见：</td>
            <td colspan="2" style="border-left:none;text-align:left;padding-right:15px;" ng-bind="vm.item.attend"></td>
        </tr>
        <tr>
            <td style="border-right:none;text-align:left;padding-left:15px;">2建议拟派人员：</td>
            <td colspan="2" style="border-left:none;text-align:left;padding-right:15px;" ng-bind="vm.item.attendManName"></td>
        </tr>
        <tr>
            <td style="border-right:none;text-align:left;padding-left:15px;">3费用支付方：</td>
            <td colspan="2" style="border-left:none;text-align:left;padding-right:15px;" ng-bind="vm.item.pricePayment"></td>
        </tr>
        <tr>
            <td style=height:120px;text-align:left;padding-left:15px;vertical-align:top;padding-top:15px;">4审批意见：</td>
            <td colspan="2" style="text-align:left;vertical-align:top;padding-left:15px;padding-top:15px;" ng-bind="vm.item.specialistComment"></td>
        </tr>
        <tr>
            <td style="height:80px;text-align:right;padding-right:5px">签字：</td>
            <td>
                <span ng-bind="vm.printData.expertCommittee.userName"></span>
                <span ng-if="vm.printData.expertCommittee.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.expertCommittee.userLogin}}"></span>
            </td>
            <td ng-bind="vm.printData.expertCommittee.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">主管领导审批意见</td>
            <td colspan="3"  style="height:120px;"><span style="float: left;text-align:left;padding-left:15px;padding-right:15px;" ng-bind="vm.item.deptChargeComment"></span></td>
        </tr>
        <tr>
            <td style="height:80px;text-align:right;padding-right:5px">签字：</td>
            <td style="height:80px;text-align:right;padding-right:5px">
                <span ng-bind="vm.printData.deptChargeMan.userName"></span>
                <span ng-if="vm.printData.deptChargeMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMan.userLogin}}"></span>
            </td>
            <td style="height:80px;text-align:right;padding-right:5px" ng-bind="vm.printData.companyOffice.end|date:'yyyy-MM-dd'"></td>
        </tr>
<%--        <tr>
            <td style="height:80px;">备注</td>
            <td  colspan="3" style="text-align:left;padding-left:15px;padding-right:15px;"></td>
        </tr>--%>

    </table>
</div>
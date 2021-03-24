<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<style id="print_style">
    #print_area{
      display: none;
    }
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }
    .print_table td{
        border: solid #000 1px;
        height: 35px;
        text-align: center;
    }
    .print_td_label {
        border: none ;height: 30px;font-size: 16px;color: red;text-align: center;
    }
    .print_td_value {
        border: none;
        border-bottom: solid #E6110D 1px;height: 30px;color: #000;text-align: center;
    }
</style>
<div id="print_area">
    <h3 style="height: 40px"></h3>
    <h3  style=" text-align: center;letter-spacing:8px;color: red;">中国五洲工程设计集团有限公司</h3>
    <h2 style=" text-align: center;letter-spacing:40px;color: red;">签报</h2>
    <hr size="1.5px" color="red">
    <table class="print_table">
        <tr style="height: 30px"></tr>
        <tr>
            <td class="print_td_label" style="width:13%;">送签部门：</td>
            <td class="print_td_value" style="width:17%;" ng-bind="vm.printData.deptName"></td>
            <td class="print_td_label" style="width:10%;">负责人：</td>
            <td class="print_td_value" style="width:20%;"  ng-bind="vm.printData.deptChargeManName"></td>
            <td class="print_td_label" style="width:15%;">公司办编号:</td>
            <td class="print_td_value" style="width:25%;"  ng-bind="vm.printData.companyNo"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td class="print_td_label" style="width:13%;border: none  ;">事项：</td>
            <td class="print_td_value" rowspan="5" style="width:87%;border: none" ng-bind="vm.printData.item"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td class="print_td_label" style="width:15%;border-top: solid red 1px;border-right: solid red 1px;">是否属于“三重一大”事项：</td>
            <td class="print_td_label" style="width:17%;border-top: solid red 1px;text-align: left">公司领导批示：</td>
            <td style="width:70%;border-top: solid red 1px;"></td>
        </tr>
        <tr>
            <td class="print_td_value"  style="width:15%;border: none;border-right: solid red 1px" ng-bind="vm.printData.belongThreeOne"></td>
            <td class="print_td_label" style="width:17%;"></td>
            <td class="print_td_value" colspan="4" style="width:85%;height:300px; border: none;text-align: left;" >
               <div ng-repeat="node in vm.printData.nodes" >
                   <span ng-bind="node.activityName"></span>
                   <span ng-bind="node.userName"></span>
                   <span ng-bind="node.userLogin"></span>
                   <img style="width: 90px;height: 35px;"  ng-src="{{'/sys/attach/downloadPic/'+node.userLogin}}">
                   <span ng-bind="node.comment"></span>
                   <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
               </div>
            </td>
        </tr>
        <tr>
            <td class="print_td_label" style="width:15%;border: none;border-right: solid red 1px">是否进行会签：</td>
            <td class="print_td_label" style="width:15%;"></td>
        </tr>
        <tr>
            <td class="print_td_value"  style="width:15%;height:150px;border: none;border-right: solid red 1px" ng-bind="vm.printData.flag"></td>
            <td class="print_td_label" style="width:15%;"></td>
        </tr>
        <tr>
            <td class="print_td_label" style="width:15%;border: none ;border-right: solid red 1px;border-top: solid red 1px">公司办：</td>
            <td class="print_td_label" style="width:15%;"></td>
        </tr>
        <tr>
            <td class="print_td_value"  style="width:15%;height:100px;border: none;border-right: solid red 1px;" >
                <span ng-bind="vm.printData.companyOffice.comment"></span>
                <span ng-bind="vm.printData.companyOffice.userLogin"></span>
                <img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.companyOffice.userLogin}}">
                <span ng-bind="vm.printData.companyOffice.userName"></span>
                <span ng-bind="vm.printData.companyOffice.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td class="print_td_label" style="width:15%;"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="">
            <td class="print_td_label" style="width:13%;border-top:  solid red 2px;">经办人：</td>
            <td class="print_td_value" style="width:17%;border-top:  solid red 2px;" ng-bind="vm.printData.agentName"></td>
            <td class="print_td_label" style="width:15%;border-top:  solid red 2px;" >公司办核收：</td>
            <td class="print_td_value" style="width: 25%;border-top:  solid red 2px;"  ng-bind="vm.printData.deptChargeManName"></td>
            <td class="print_td_label" style="width: 13%;border-top:  solid red 2px;" >报送日期:</td>
            <td class="print_td_value" style="width: 17%;border-top:  solid red 2px;"  ng-bind="vm.printData.gmtCreate|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>


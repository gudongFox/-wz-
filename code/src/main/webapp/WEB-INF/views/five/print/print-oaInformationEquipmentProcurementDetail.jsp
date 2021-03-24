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
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>

<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;"><span ng-bind="vm.printData.year"></span>年信息化设备采购预算</h3>
    <span style="padding-left: 15px" ng-bind="vm.printData.deptName+'印章处:'"></span><span style="float:right;padding-right: 15px;">单位:元</span>
 <%--   <table class="print_table">
        <tr>
            <td style="width:15px;">部门名称</td>
            <td style="width:40px;" ng-bind="vm.printData.deptName"></td>
            <td style="width:15px;">采购年份</td>
            <td style="width:30px;" ng-bind="vm.printData.procurementTime"></td>
        </tr>
     &lt;%&ndash;   <tr>
            <td colspan="4" style="text-align:left;vertical-align:top;color:red;padding-left:5px;padding-right:5px;padding-top:5px;padding-bottom:5px;border-bottom:none;"><b>
                注意: 1、备注列填写是否为白名单设备，如不是需另行说明原因；</br>
                &emsp; &emsp; &emsp; 2、类型：服务器、计算机包括：服务器、台式计算机、硬携式让算机、工作站；网络设备包括：交换机、路由器、网关；
                外部设施设备包括：打印机、扫描仪；办公自动化设备包括：复印机、传真机、多功能一体机、碎纸机、晒图仪、绘图仪；声像设备包括:投影仪、视频会议设备、数字化会议设备。</b>
            </td>
        </tr>&ndash;%&gt;
    </table>--%>
    <table class="print_table">
        <tr>
            <td>序号</td>
            <td>设备名称</td>
            <td>设备类型</td>
            <td>型号</td>
            <td>功能（用途、网络）</td>
            <td>数量</td>
            <td>单价(元)</td>
            <td>合计(元)</td>
            <td>备注</td>
        </tr>
        <tr ng-repeat="printDetail in vm.printDetails">
            <td ng-bind="">
                <span ng-bind="$index+1" ng-if="vm.printDetails.length==($index+1)  "></span>
                <span ng-if="vm.printDetails.length!=($index+1)">总计</span>
            </td>
            <td ng-bind="printDetail.equipmentModel"></td>
            <td ng-bind="printDetail.equipmentName"></td>
            <td ng-bind="printDetail.equipmentType"></td>
            <td ng-bind="printDetail.useType"></td>
            <td ng-bind="printDetail.number"></td>
            <td ng-bind="printDetail.price"></td>
            <td ng-bind="printDetail.totalPrice"></td>
            <td ng-bind="printDetail.remark"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;border-top:none;">制表人：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.startMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.startMan.userName"></span>
                <span ng-if="vm.printData.startMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.startMan.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.startMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">第一管理者:</td>
        </tr>
        <tr>
            <td colspan="4" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.deptChargeMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td><span ng-bind="vm.printData.deptChargeMan.userName"></span>
                <span ng-if="vm.printData.deptChargeMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMan.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.deptChargeMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">公司主管领导：</td>
        </tr>
        <tr>
            <td colspan="4" style="height:60px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.branchedDeptChargeMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td><span ng-bind="vm.printData.branchedDeptChargeMan.userName"></span>
                <span ng-if="vm.printData.branchedDeptChargeMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.branchedDeptChargeMan.userLogin}}"></span>
            <td style="text-align: right;width:20%;">日期：</td>
            <td  ng-bind="vm.printData.branchedDeptChargeMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>
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


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">公用计算机及U盘申请表</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">申请部门</td>
            <td style="width:19%;" ng-bind="vm.printData.deptName"></td>
            <td style="width:15%;">责任人</td>
            <td style="width:18%;" ng-bind="vm.printData.applicationManName"></td>
            <td style="width:15%;">申请日期</td>
            <td style="width:18%;" ng-bind="vm.printData.applicationTime"></td>
        </tr>
        <tr>
            <td>设备编号</td>
            <td ng-bind="vm.printData.equipmentNo"></td>
            <td>房间号</td>
            <td ng-bind="vm.printData.roomNo"></td>
            <td>IP地址或U盘序列号</td>
            <td ng-bind="vm.printData.ipAddress"></td>
        </tr>
        <tr>
            <td colspan="6" style="height:100px;text-align:left;padding-left:15px;padding-right:15px;font-size: 20px;">&emsp;&emsp;责任人保证公用计算机及公用U意只进行非密信息交换，在以后的使用中能按照保密要求不在公用计算机及公用U盘中处理、接收、存储含有敏感信息的文件及图纸，愿意承担公司管理规定责任。</td>
        </tr>
        <tr>
            <td rowspan="2">部门审批</td>
            <td colspan="5" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.printData.departmentComment"></td>
        </tr>
        <tr>
            <td></td>
            <td>部门负责人</td>
            <td>
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            <td>日期</td>
            <td ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">信息化建设与管理部审批</td>
            <td colspan="5" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.printData.scienceComment"></td>
        </tr>
        <tr>
            <td></td>
            <td>信息化建设与管理部审批</td>
            <td>
                <span ng-bind="vm.printData.qualityDepartmentChargeMen.userName"></span>
                <span ng-if="vm.printData.qualityDepartmentChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.qualityDepartmentChargeMen.userLogin}}"></span>
            <td>日期</td>
            <td ng-bind="vm.printData.qualityDepartmentChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2">计算机所执行情况</td>
            <td colspan="5" style="height:100px;border-bottom:none;text-align:left;padding-left:15px;padding-left:15px" ng-bind="vm.printData.computerComment"></td>
        </tr>
        <tr>
            <td></td>
            <td>计算机所执行情况</td>
            <td>
                <span ng-bind="vm.printData.computerChargeMen.userName"></span>
                <span ng-if="vm.printData.computerChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.computerChargeMen.userLogin}}"></span>
            <td>日期</td>
            <td ng-bind="vm.printData.computerChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>
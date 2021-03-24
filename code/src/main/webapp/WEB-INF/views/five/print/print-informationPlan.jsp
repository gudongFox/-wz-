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
        height: 30px;
        text-align: left;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;"><span ng-bind="vm.printData.year"></span>年度软件采购预算</h3>
    <span style="padding-left: 15px" ng-bind="vm.printData.deptName+'印章处:'"></span><span style="float:right;padding-right: 15px;">单位:元</span>
    <table class="print_table1">
        <tr>
            <td>序号</td>
            <td>软件名称</td>
            <td>功能模块</td>
            <td>类型</td>
            <td>用途</td>
            <td>专业</td>
            <td>数量（套）</td>
            <td>单价（元）</td>
            <td>总计（元）</td>
            <td>备注</td>
        </tr>
        <tr ng-repeat="detail in vm.printData.planDetails">
            <td>
                <span ng-bind="$index+1" ng-if="vm.detalList.length==($index+1)  "></span>
                <span ng-if="vm.detalList.length!=($index+1)">总计</span>
            </td>
            <td ng-bind="detail.softwareName"  ></td>
            <td ng-bind="detail.softwareModel"></td>
            <td ng-bind="detail.softwareType"></td>
            <td ng-bind="detail.softwareUseWay"></td>
            <td ng-bind="detail.useMajor"></td>
            <td ng-bind="detail.softwareNumber"></td>
            <td ng-bind="detail.softwarePrice"></td>
            <td ng-bind="detail.softwareTotal"></td>
            <td ng-bind="detail.remark"></td>
        </tr>

    </table>
    <table class="print_table" ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
        <tr>
            <td  rowspan="2"  style="width: 10%;border-top: none;height: 35px;" ng-bind="node.activityName"></td>
            <td  colspan="4" style="width: 40%;border-top: none;height: 35px;" style="vertical-align:top;padding-right: 5px;padding-left:5px;padding-top: 5px;border-top:none;">
                <span ng-bind="node.comment" style="margin-right: 10px;"></span>
            </td>

            <td rowspan="2" style="width: 10%;border-top: none" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td colspan="4"  style="width: 40%;border-top: none;height: 35px;" style="vertical-align:top;padding-right: 5px;padding-left:5px;padding-top: 5px;border-top:none;" >
                <span ng-bind="vm.printData.nodes[$index+1].comment" style="margin-right: 10px;"></span>
            </td>
        </tr>
        <tr>
            <td style="width:6%">签字:</td>
            <td style="width:15%">
                <span ng-if="node.userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+node.userLogin}}"></span>
                <span ng-bind="node.userName"></span>
            </td>
            <td style="width:6%">日期:</td>
            <td style="width:15%" ng-bind="node.end|date:'yyyy-MM-dd'"></td>

            <td style="width:6%">签字:</td>
            <td style="width:15%">
                <span ng-if="vm.printData.nodes[$index+1].userName.length>0"><img style="width: 90px;height: 35px;"   ng-src="{{'/sys/attach/downloadPic/'+vm.printData.nodes[$index+1].userLogin}}"></span>
                <span ng-bind="vm.printData.nodes[$index+1].userName"></span>
            </td>
            <td style="width:6%">日期:</td>
            <td style="width:15%" ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>
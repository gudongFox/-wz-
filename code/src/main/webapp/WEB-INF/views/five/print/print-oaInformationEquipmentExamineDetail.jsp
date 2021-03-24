<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 40px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">信息化设备验收审批表</h3>
    <p><span style="text-align: left;" ng-bind="'验收日期:'+vm.printData.acceptTime"></span></p>
    <table class="print_table">
        <tr>
            <td colspan="9" style="height:35px;text-align:left;padding-left:15px;background: #dcdcdc;">信息化设备基础信息</td>
        </tr>
        <tr>
            <td style="width:11%">设备序列号</td>
            <td style="width:11%">所属单位</td>
            <td style="width:11%">责任人</td>
            <td style="width:11%">责任人职工号</td>
            <td style="width:11%">使用人</td>
            <td style="width:12%">用途</td>
        </tr>
        <tr>
            <td ng-bind="vm.printData.equipmentNo"></td>
            <td ng-bind="vm.printData.deptName"></td>
            <td ng-bind="vm.printData.chargeManName"></td>
            <td ng-bind="vm.printData.chargeMan"></td>
            <td ng-bind="vm.printData.userName"></td>
            <td ng-bind="vm.printData.useType"></td>
        </tr>
        <tr>
            <td style="width:11%">设备名称</td>
            <td style="width:11%">使用类别</td>
            <td>启用时间</td>
            <td style="width:11%">设备型号</td>
            <td style="width:11%">设备品牌</td>
            <td>放置地点</td>
        </tr>
        <tr>
            <td ng-bind="vm.printData.equipmentName"></td>
            <td ng-bind="vm.printData.secretRank"></td>
            <td ng-bind="vm.printData.startTime"></td>
            <td ng-bind="vm.printData.type"></td>
            <td ng-bind="vm.printData.brand"></td>
            <td ng-bind="vm.printData.address"></td>
        </tr>
        <tr>
            <td  colspan="2">显示器型号</td>
            <td  colspan="4">设备类型</td>
        </tr>
        <tr>
            <td colspan="2" ng-bind="vm.printData.displayNo"></td>
            <td colspan="4" ng-bind="vm.printData.osInstallTime"></td>
        </tr>

        <tr>
            <td colspan="2" >验收价格</td>
            <td colspan="4">资产编号</td>
        </tr>
        <tr>
            <td colspan="2" ng-bind="vm.printData.checkPrice"></td>
            <td colspan="4" ng-bind="vm.printData.fixedAssetNo"></td>
        </tr>
        <tr>
            <td colspan="2">购置单编号</td>
            <td colspan="4">备注</td>
        </tr>
        <tr>
            <td colspan="2" ng-bind="vm.printData.diskNo"></td>
            <td colspan="4" ng-bind="vm.printData.remark"></td>
        </tr>
        <tr>
            <td colspan="9" style="height:35px;text-align:left;padding-left:15px;bolder-bottom:none;background: #dcdcdc;">验收审批信息</td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">验收人意见</td>
        </tr>
        <tr>
            <td colspan="4" style="height:40px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px" ng-bind="vm.printData.startMan.comment"></td>
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
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">行政事务部意见</td>
        </tr>
        <tr>
            <td colspan="4" style="height:40px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.affairMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td><span ng-bind="vm.printData.affairMan.userName"></span>
                <span ng-if="vm.printData.affairMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.affairMan.userLogin}}"></span>
            </td>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.affairMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">信息化建设与管理部意见</td>
        </tr>
        <tr>
            <td colspan="4" style="height:40px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.technologyMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.technologyMan.userName"></span>
                <span ng-if="vm.printData.technologyMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.technologyMan.userLogin}}"></span>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.technologyMan.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td colspan="4" style="height:35px;text-align:left;padding-left:15px;bolder-top:none;">财务验收意见</td>
        </tr>
        <tr>
            <td colspan="4" style="height:40px;text-align:left;vertical-align:top;padding-right: 15px;padding-left:15px;padding-top: 15px;bolder-top:none;" ng-bind="vm.printData.financeMan.comment"></td>
        </tr>
        <tr>
            <td style="text-align: right;width:20%;">签字：</td>
            <td>
                <span ng-bind="vm.printData.financeMan.userName"></span>
                <span ng-if="vm.printData.financeMan.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.financeMan.userLogin}}"></span>
            <td style="text-align: right;width:20%;">日期：</td>
            <td ng-bind="vm.printData.financeMan.end|date:'yyyy-MM-dd'"></td>
        </tr>

    </table>
</div>
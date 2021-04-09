<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 16px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 35px;
        text-align: left;
        padding-left: 5px;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area" hidden>
    <h3 style="text-align: center;">专利申请登记表</h3>
    <p style="float:left;"></p>
    <table class="print_table">
        <tr>
            <td style="width:25%;">公司内申请单位：</td>
            <td style="width:25%;" ng-bind="vm.printData.deptName"></td>
            <td style="width:25%;">公司外合作单位(附协议)</td>
            <td style="width:25%;" ng-bind="vm.printData.cooperator"></td>
        </tr>
        <tr>
            <td>申请人顺序(法人单位名称)</td>
            <td colspan="3" ng-bind="vm.item.applyNameSort"></td>
        </tr>
        <tr>
            <td style="width:25%;">发明名称</td>
            <td style="width:25%;" ng-bind="vm.printData.inventName"></td>
            <td style="width:25%;">所属专业</td>
            <td style="width:25%;" ng-bind="vm.printData.majorName"></td>
        </tr>
        <tr>
            <td style="width:25%;">第一发明人</td>
            <td style="width:25%;" ng-bind="vm.printData.firstInventManName"></td>
            <td style="width:25%;">身份证号</td>
            <td style="width:25%;" ng-bind="vm.printData.idNumber"></td>
        </tr>
        <tr>
            <td>其它发明人顺序</td>
            <td colspan="3" ng-bind="vm.printData.otherInventMen"></td>
        </tr>
        <tr>
            <td>与关键技术或创新点有关的保密情况</td>
            <td colspan="3" style="height:70px" ng-bind="vm.printData.securityDec"></td>
        </tr>
        <tr>
            <td>关键技术或创新点:</td>
            <td colspan="3" style="height:70px" ng-bind="vm.printData.technologyAndInnovate"></td>
        </tr>
        <tr>
            <td>检索查新情况:</td>
            <td colspan="3" style="height:70px" ng-bind="vm.printData.retruevalDec"></td>
        </tr>
        <tr>
            <td>技术与市场预测:</td>
            <td colspan="3" style="height:70px" ng-bind="vm.printData.technologyMarket"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td rowspan="4" style="width:25%;border-top:none;">申请单位意见:</td>
            <td colspan="4" style="border-top:none;">(与公司外的单位共同申请的，应写明费用分担形式和成果共享形式，并附合同获协议)</td>
        </tr>
        <tr>
            <td colspan="5" style="height:70px" ng-bind="vm.printData.deptChargeMen.comment"></td>
        </tr>
        <tr>
            <td colspan="5" style="height:30px">附件：
                <div  ng-repeat="file in vm.printData.fileList">
                    <span ng-bind="file.fileName+'_'+file.creatorName"></span>
                </div>
            </td>
        </tr>
        <tr>
            <td style="width:18%;text-align:right;">签字</td>
            <td style="width:20%;">
                <span ng-bind="vm.printData.deptChargeMen.userName"></span>
                <span ng-if="vm.printData.deptChargeMen.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deptChargeMen.userLogin}}"></span>
            </td>
            <td style="width:18%;text-align:right;">日期</td>
            <td style="width:19%;" ng-bind="vm.printData.deptChargeMen.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="2" style="width:25%;border-top:none;">专业技术委员会审查意见</td>
            <td colspan="5" style="height:70px" ng-bind="vm.printData.expertCommittee.comment"></td>
        </tr>
        <tr>
            <td style="width:18%;text-align:right;">签字</td>
            <td style="width:20%;">
                <span ng-bind="vm.printData.expertCommittee.userName"></span>
                <span ng-if="vm.printData.expertCommittee.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.expertCommittee.userLogin}}"></span>
            </td>
            <td style="width:18%;text-align:right;">日期</td>
            <td style="width:19%;" ng-bind="vm.printData.expertCommittee.end|date:'yyyy-MM-dd'"></td>
        </tr>
        <tr>
            <td rowspan="3" style="width:25%;">公司总工程师意见</td>
            <td colspan="5" style="border-bottom:none;">审查结论及其他意见:</td>
        </tr>
        <tr>
            <td colspan="5" style="height:70px;border-top:none;vertical-align:top;" ng-bind="vm.printData.deputyManager.comment"></td>
        </tr>
        <tr>
            <td style="width:18%;text-align:right;">签字</td>
            <td style="width:20%;">
                <span ng-bind="vm.printData.deputyManager.userName"></span>
                <span ng-if="vm.printData.deputyManager.userName.length>0"><img style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deputyManager.userLogin}}"></span>
            </td>
            <td style="width:18%;text-align:right;">日期</td>
            <td style="width:19%;" ng-bind="vm.printData.deputyManager.end|date:'yyyy-MM-dd'"></td>
        </tr>
    </table>
</div>
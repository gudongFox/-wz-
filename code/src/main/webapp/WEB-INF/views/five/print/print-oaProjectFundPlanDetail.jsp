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
    <h3 style="text-align: center;"><span ng-bind="vm.printData.projectName"></span>项目<span ng-bind="vm.printData.planTime|date:'yyyy年-MM月'"></span>资金使用计划</h3>
    <span style="padding-left: 15px" ng-bind="'编制单位:'+vm.printData.deptName"></span><span style="float:right;padding-right: 15px;">单位:万元</span>
    <table class="print_table1">
        <tr>
            <td colspan="9" style="text-align:left;padding-left:5px;">一、总包合同（收）</td>
        </tr>
        <tr>
            <td>序号</td>
            <td>合同号</td>
            <td>对方单位名称</td>
            <td>项目名称</td>
            <td>合同额</td>
            <td>本月前累计收款</td>
            <td>本月应收款</td>
            <td>合同尾款</td>
            <td>备注</td>
        </tr>
        <tr ng-repeat="detail in vm.details|filter:{type:'总包'}:true">
            <td ng-bind="$index+1"></td>
            <td ng-bind="detail.contractNo"></td>
            <td ng-bind="detail.deptName"></td>
            <td ng-bind="detail.projectName"></td>
            <td ng-bind="detail.contractPrice"></td>
            <td ng-bind="detail.accumulativePrice"></td>
            <td ng-bind="detail.receivablePrice"></td>
            <td ng-bind="detail.finalPrice"></td>
            <td ng-bind="detail.remark"></td>
        </tr>
        <tr>
            <td colspan="4" style="background: #dcdcdc;">收款合计</td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalContractPrice"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalAccumulativePrice"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalReceivablePrice"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalFinalPrice"></td>
            <td style="background: #dcdcdc;"></td>
        </tr>
        <tr>
            <td colspan="9" style="text-align:left;padding-left:5px;">一、分包合同（付）</td>
        </tr>
        <tr>
            <td>序号</td>
            <td>合同号</td>
            <td>对方单位名称</td>
            <td>项目名称</td>
            <td>合同额</td>
            <td>本月前累计付款</td>
            <td>本月应付款</td>
            <td>合同尾款</td>
            <td>备注</td>
        </tr>
        <tr ng-repeat="detail in vm.details|filter:{type:'分包'}:true">
        <td ng-bind="$index+1"></td>
        <td ng-bind="detail.contractNo"></td>
        <td ng-bind="detail.deptName"></td>
        <td ng-bind="detail.projectName"></td>
        <td ng-bind="detail.contractPrice"></td>
        <td ng-bind="detail.accumulativePrice"></td>
        <td ng-bind="detail.receivablePrice"></td>
        <td ng-bind="detail.finalPrice"></td>
        <td ng-bind="detail.remark"></td>
        </tr>
        <tr>
            <td colspan="4" style="background: #dcdcdc;">付款合计</td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalContractPrice2"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalAccumulativePrice2"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalReceivablePrice2"></td>
            <td style="background: #dcdcdc;" ng-bind="vm.printData.totalFinalPrice2"></td>
            <td style="background: #dcdcdc;"></td>
        </tr>
        <tr>
            <td colspan="5" style="background: #dcdcdc;">项目结余</td>
            <td colspan="4" style="background: #dcdcdc;" ng-bind="vm.printData.residue"></td>

        </tr>
    </table>
    <table class="print_table2">
        <tr>
            <td style="width:8%;">总经理:</td>
            <td style="width:6%;"><span ng-bind="vm.printData.chiefManager.userName"></span></td>
            <td style="width:11%;" ><img ng-if="vm.printData.chiefManager.userName.length>0"style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefManager.userLogin}}"></td>
            <td style="width:8%;" ng-bind="vm.printData.chiefManager.end|date:'yyyy-MM-dd'"></td>

            <td style="width:8%;">总会计师:</td>
            <td style="width:6%;"><span ng-bind="vm.printData.chiefAccountant.userName"></span></td>
            <td style="width:11%;" ><img ng-if="vm.printData.chiefAccountant.userName.length>0"style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.chiefAccountant.userLogin}}"></td>
            <td style="width:8%;" ng-bind="vm.printData.chiefAccountant.end|date:'yyyy-MM-dd'"></td>

            <td style="width:8%;">分管副总:</td>
            <td style="width:6%;"><span ng-bind="vm.printData.deputyManager.userName"></span></td>
            <td style="width:11%;" ><img ng-if="vm.printData.deputyManager.userName.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.deputyManager.userLogin}}"></td>
            <td style="width:8%;" ng-bind="vm.printData.deputyManager.end|date:'yyyy-MM-dd'"></td>

        </tr>


        <tr>
            <td style="width:8%;">财务金融部:</td>
            <td style="width:6%;" ><span ng-bind="vm.printData.financialDepartment.userName"></span></td>
            <td style="width:11%;" ><img ng-if="vm.printData.financialDepartment.userName.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.financialDepartment.userLogin}}"></td>
            <td style="width:8%;" ng-bind="vm.printData.financialDepartment.end|date:'yyyy-MM-dd'"></td>

            <td>工程管理部:</td>
            <td ng-bind="vm.printData.engineeringManagementDepartment.userName"></td>
            <td ><img ng-if="vm.printData.engineeringManagementDepartment.userName.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.engineeringManagementDepartment.userLogin}}"></td>
            <td ng-bind="vm.printData.engineeringManagementDepartment.end|date:'yyyy-MM-dd'"></td>


            <td>项目经理:</td>
            <td><span ng-bind="vm.printData.projectManager.userName"></span></td>
            <td ><img ng-if="vm.printData.projectManager.userName.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+vm.printData.projectManager.userLogin}}"></td>
            <td ng-bind="vm.printData.projectManager.end|date:'yyyy-MM-dd'"></td>

        </tr>
        <tr ng-repeat=" deptmans in vm.printData.deptChargeMen">
            <td ng-if="deptmans.userName1.length>0">部门负责人:</td>
            <td><span ng-bind="deptmans.userName1"></span></td>
            <td ><img ng-if="deptmans.userName1.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+deptmans.userLogin1}}"></td>
            <td ng-bind="deptmans.end1|date:'yyyy-MM-dd'"></td>

            <td ng-if="deptmans.userName2.length>0">部门负责人:</td>
            <td><span ng-bind="deptmans.userName2"></span></td>
            <td ><img ng-if="deptmans.userName2.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+deptmans.userLogin2}}"></td>
            <td ng-bind="deptmans.end2|date:'yyyy-MM-dd'"></td>

            <td ng-if="deptmans.userName3.length>0">部门负责人:</td>
            <td ><span ng-bind="deptmans.userName3"></span></td>
            <td ><img ng-if="deptmans.userName3.length>0" style="width: 90px;height: 35px;" ng-src="{{'/sys/attach/downloadPic/'+deptmans.userLogin3}}"></td>
            <td ng-bind="deptmans.end3|date:'yyyy-MM-dd'"></td>

        </tr>
    </table>
</div>
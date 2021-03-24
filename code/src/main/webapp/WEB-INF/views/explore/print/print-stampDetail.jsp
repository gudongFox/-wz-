
<div id="print_area">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;" ng-bind="vm.printData.stampType"></h3>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">用印所室、部门</td>
            <td style="width: 35%" ng-bind="vm.printData.deptName"></td>
            <td style="width: 15%;">用印时间</td>
            <td style="width: 35%" ng-bind="vm.printData.useTime"></td>
        </tr>
        <tr>
            <td >用印单位经办人</td>
            <td ng-bind="vm.printData.deptUserName" ></td>
            <td >办公室经办人</td>
            <td ng-bind="vm.printData.officeUserName"></td>
        </tr>
        <tr>
            <td style="width: 15%;">用印明细</td>
            <td  colspan="2" style="width: 200px;text-align: left;padding-left: 50px">
                 <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'用印明细'}:true">
                     <input type="checkbox" ng-checked="vm.printData.useStamp.indexOf(quality.codeValue)>=0" />
                     <span ng-bind="quality.codeValue" style="font-size: 12px;"></span>
                     <br>
                 </span>
            </td>
            <td style="height: 200px;text-align: left;padding-left: 50px">
                 <span ng-repeat="quality in sysCodes | filter:{codeCatalog:'用印文件类型'}:true">
                     <input type="radio" ng-checked="quality.codeValue==vm.printData.docType" />
                     <span ng-bind="quality.codeValue" style="font-size: 12px;"></span>
                      <br>
                </span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td style="width: 15%;border-top: none" rowspan="3">用印事由</td>
            <td style="border-top: none">项目名称</td>
            <td style="border-top: none" colspan="3" ng-bind="vm.printData.projectName"></td>
        </tr>
        <tr>
            <td >用印事由</td>
            <td  colspan="3" style="height: 100px;text-align: left;padding-left:  10px;" >
                <span style="margin-top: 10px" ng-bind="vm.printData.useDescription"></span>
            </td>
        </tr>
        <tr>
            <td style="width: 10%">合同金额</td>
            <td style="width: 32.5%" ng-bind="vm.printData.contractMoney"></td>
            <td style="width: 10%">执行阶段</td>
            <td style="width: 32.5%" ng-bind="vm.printData.stageName"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none" ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="width: 20%;border-top: none" ng-bind="node.activityName"></td>
            <td style="width: 30%;border-top: none">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style="width: 20%;border-top: none" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="width: 30%;border-top: none">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
    <p style="font-size: 12px;">注:本表由项目负责人和技术质量部负责人填写。</p>
</div>
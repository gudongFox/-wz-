<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div id="print_area" style="display: none">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">重大例外情况处理记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 15%;">合同号</td>
            <td ng-bind="vm.printData.contractNo"></td>
            <td style="width: 15%;">工程名称</td>
            <td ng-bind="vm.printData.projectName"></td>
        </tr>

        <tr height="60px">
            <td  colspan="4" style="height:50px;color: #0d0d0d ;font-size: 16px;" >为满足顾客需要，本项目是在有重大例外情况下进行的设计工作，为保证设计质量，<br/>明确责任以
                及妥善处理遗留问题，兹将领导批准的有关事项及措施记录如下：</td>
        </tr>
        <tr height="90px">
            <td  colspan="4" style="height: 150px;text-align: left">
                <span style="font-size:16px;padding-left: 10px;margin-top: 50px">本项目发生的重大例外是：</span><br><br>
                <span style="padding-left: 10px;" ng-repeat="quality in sysCodes | filter:{codeCatalog:'重大例外'}:true">
                    <span ng-bind="quality.codeValue"></span>
                    <input type="checkbox" ng-checked="quality.codeValue==vm.printData.exceptionType"/><span>&nbsp;&nbsp;</span>
                  <br ng-if="$index==2"/>
                </span>
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr style="border-top: none">
            <td style="font-size: 16px;border-top: none;text-align: left">重大例外情况简述：</td>
        </tr>
        <tr>
            <td style="height: 500px;text-align: left;padding-left: 30px;padding-bottom: 450px;padding-top: 20px"
                ng-bind="vm.printData.exceptionDesc">
            </td>
        </tr>
    </table>
    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
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
    <p style="font-size: 12px;">注：将顾客签字、盖章按重大例外情况进行设计的函、委托书、设计要求等附后。</p>
</div>
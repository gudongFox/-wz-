<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察输入清单及评审记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
    <table class="print_table">
        <tr>
            <td style="width: 20%;">工程名称</td>
            <td style="width: 40%;" ng-bind="vm.printData.projectName"></td>
            <td style="width: 20%;">工程号</td>
            <td style="width: 20%;" ng-bind="vm.printData.projectNo"></td>
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width: 20%;border-top: none;border-bottom:none;">勘察阶段</td>
            <td style="width: 80%;border-top: none;border-bottom:none;">
                <span ng-repeat="stageNames in sysCodes | filter:{codeCatalog:'勘察阶段'}:true">
                    &nbsp;&nbsp;&nbsp<span ng-bind="stageNames.codeValue"></span>
                    <input type="checkbox" ng-checked="stageNames.codeValue==vm.printData.stageName"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
            </td>

        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 20%;">机号</td>
            <td style="width: 30%;" ng-bind="vm.printData.machineCode"></td>
            <td style="width: 20%;">所用软件</td>
            <td style="width: 30%;" ng-bind="vm.printData.softwareName"></td>

        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="text-align: center;border-top: none;"><strong>勘 察 输 入 清 单</strong></td>
        </tr>
    </table>


    <table class="print_table">

        <tr ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys">
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;width: 60%;">
                <span ng-bind="($index+1)+'、'+exploreDetailSatisfy.contentDesc"></span>
            </td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;">
                <input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/>
            </td>
        </tr>
        <tr>
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;width: 60%;">

            </td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;">

            </td>
        </tr>
    </table>


    <table class="print_table">
        <tr>
            <td style="text-align: center;border-bottom:none;"><strong>勘察输入评审记录</strong></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 15%;">主持人</td>
            <td style="width: 15%;" ng-bind="vm.printData.compereUser"></td>
            <td style="width: 15%;">参会人</td>
            <td style="width: 55%;" ng-bind="vm.printData.attendUser"></td>
        </tr>

    </table>

    <table class="print_table">

        <tr>
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;width: 50%;">1、勘察资料及条件是否齐全：</td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;"> 齐全 <input type="checkbox" ng-checked="vm.printData.docResult=='齐全'"/>     不够齐全 <input type="checkbox" ng-checked="vm.printData.docResult=='不够齐全'"/></td>
        </tr>
        <tr>
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;">2、勘察要求是否正确：</td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;"> 正确 <input type="checkbox" ng-checked="vm.printData.requestResult=='正确'"/>   不够正确 <input type="checkbox"
                                                                                                         ng-checked="vm.printData.requestResult=='不够正确'"/></td>
        </tr>
        <tr>
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;">3、勘察输入的完整型：</td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;"> 完整 <input type="checkbox" ng-checked="vm.printData.inputResult=='完整'"/>      不够完整 <input type="checkbox" ng-checked="vm.printData.inputResult=='不够完整'"/></td>
        </tr>
        <tr>
            <td style="border-top: none;border-bottom: none;border-right:none;padding-left:20px;text-align: left;" ng-bind="'4、其它:'+vm.printData.otherResult"></td>
            <td style="border-top: none;border-bottom: none;border-left: none;text-align: left;"></td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td>
			<span style="float:left;text-align:left;height: 100px;">
				<p style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;">针对勘察输入存在的问题的应对措施：</p>
	            <p style="float:left;padding-top:5px;padding-left:20px;padding-right: 20px;text-align: left;"
                   ng-bind="vm.printData.solveSuggestion"></p>
			</span>
            </td>
        </tr>

    </table>

    <table class="print_table">
        <tr ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
            <td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
            </td>
            <td style=" border-top: none;width: 20%;height:45px"
                ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
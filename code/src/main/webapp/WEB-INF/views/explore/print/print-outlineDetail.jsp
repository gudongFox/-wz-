<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察纲要评审记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 15%;height: 40px;">工程名称</td>
            <td style="width: 35%;" ng-bind="vm.printData.projectName">  </td>
			<td style="width: 15%;">工程号</td>
            <td style="width: 35%;" ng-bind="vm.printData.projectNo">  </td>
        </tr>

	</table>	
		<table class="print_table">
        <tr>
            <td style="width: 15%;height: 40px;border-top: none;border-bottom:none;">勘察文件</td>
            <td style="width: 85%;border-top: none;border-bottom:none;">
				<span ng-repeat="exploreStage in sysCodes | filter:{codeCatalog:'勘察文件'}:true">
                    <span ng-bind="exploreStage.codeValue"></span>
                    <input type="checkbox" ng-checked="exploreStage.codeValue==vm.printData.exploreStage"/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
			</td>

</tr>
	</table>
	
		<table class="print_table">
        <tr>
            <td style="width: 15%;height: 40px;">评审</br>主持人</td>
            <td style="width: 22%;" ng-bind="vm.printData.compereUser">  </td>
			<td style="width: 10%;">项目负责人</td>
            <td style="width: 22%;" ng-bind="vm.printData.exploreCharge">  </td>
			<td style="width: 10%;">评审日期</td>
            <td style="width: 22%;" ng-bind="vm.printData.checkTime">  </td>

</tr>
	</table>
	
	<table class="print_table">
        <tr>           
            <td style="width: 15%;height: 40px;border-top: none;border-bottom:none;">参加人员</br>名单</td>
            <td style="width: 85%;border-top: none;border-bottom:none;text-align:left;padding-left:20px;padding-right: 20px" ng-bind="vm.printData.attendUser">  </td>
         </tr>
	</table>
	
		
	<table class="print_table">
        <tr>
            <td style="text-align: left;height:180px;padding-top:10px">
			<span style="float:left;padding-bottom:30px;padding-left:10px;text-align:left;">
			评审资料及文件：</br></br>
		    <span ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-bind="exploreDetailSatisfy.contentDesc"></span>
			<input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/>
			<br>
		</span>
	 </span>
			</td>            
</tr>
	</table>
	
	<table class="print_table">
        <tr>
            <td style="text-align: left;height:160px;padding-top:10px;border-top: none;">
			<span style="float:left;padding-bottom:30px;padding-left:10px;text-align:left;">
			评审内容及要求：</br></br>
				<span ng-repeat="exploreDetailSatisfy in vm.printData.exploreDetailSatisfys1">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-bind="exploreDetailSatisfy.contentDesc"></span>
			<input type="checkbox" ng-checked="exploreDetailSatisfy.satisfy"/>
			<br>
		</span>
	 </span>
	        </span>
			</td>            
            </tr>
    </table>
	
		<table class="print_table">
        <tr>
            <td style="text-align: left;height:160px;padding-top:10px;border-top: none;">
			<span style="float:left;padding-bottom:10px;padding-left:10px;text-align:left;">
			评审意见及结论：</br></br>
			1、甲方提供的勘察基础资料及其条件是否齐全；</br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;齐全<input type="checkbox" ng-checked="vm.printData.resultDocSatisfy=='齐全'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                不够齐全<input type="checkbox" ng-checked="vm.printData.resultDocSatisfy=='不够齐全'"/></br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </br>
			2、勘察工作量布置是否合理；</br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合理<input type="checkbox" ng-checked="vm.printData.resultWorkloadWell=='合理'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                不够合理<input type="checkbox" ng-checked="vm.printData.resultWorkloadWell=='不够合理'"/></br></br>
			3、纲要是否符合勘察主规范要求；</br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;符合<input type="checkbox" ng-checked="vm.printData.resultOutlineSatisfy=='符合'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                不符合<input type="checkbox" ng-checked="vm.printData.resultOutlineSatisfy=='不符合'"/></br>
                </br>
	        </span>
			</td>            
            </tr>
    </table>

	
		<table class="print_table">
        <tr>
            <td style="text-align:left;height: 80px;border-top:none;">
			<span style="float:left;padding-bottom:10px;padding-left:10px;text-align:left;">野外勘察是否满足本阶段要求：</br></br>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span ng-repeat="outSatisfy in sysCodes | filter:{codeCatalog:'设计与开发评审结论'}:true">
                    <span ng-bind="outSatisfy.codeValue"></span>
                    <input type="checkbox" ng-checked="outSatisfy.codeValue==vm.printData.resultOutSatisfy"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </span>
            </span>
            </td>
        </tr>

	</table>

        <table class="print_table">
            <tr  ng-repeat="node in vm.printData.nodes" ng-if="($index%2)===0">
                <td style="border-top: none;width: 20%;height: 45px" ng-bind="node.activityName"></td>
                <td style="border-top: none;width: 30%">
                    <span ng-bind="node.userName" style="margin-right: 10px;"></span>
                    <span ng-bind="node.end|date:'yyyy-MM-dd'"></span>
                </td>
                <td style=" border-top: none;width: 20%;height:45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
                <td style="border-top: none;width: 30%">
                    <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                    <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
                </td>
            </tr>
	</table>
	
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
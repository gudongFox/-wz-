<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;" >
	<h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
	<h3 style="text-align: center;">岩土工程勘察野外检查记录表</h3>
	<p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 10%;height: 45px" colspan="1">项次</td>
            <td style="width: 12%;" colspan="1"> 项目名称 </td>
			<td style="width: 60%;"colspan="4">检查情况及处理意见</td>
            <td style="width: 18%;" colspan="1"> 检查时间 </td>
        </tr>
        <tr>
            <td rowspan="3";style="width:10%;height: 50px;">1 </td>
            <td rowspan="3";style="width:12%;height: 50px;">测绘放孔</td>

			<td style="width:30%;height: 10px;"colspan="2">钻孔位置</td>
            <td style="width:30%;height: 10px;"colspan="2"> 孔口高程 </td>
			<td  rowspan="3";style="width:18%;" ng-bind="vm.printData.holeTime"> </td>
		</tr>
		<tr>
			<td>准确 <input type="checkbox" ng-checked="vm.printData.holeLocation=='准确'"/></td>
			<td>不准确 <input type="checkbox" ng-checked="vm.printData.holeLocation=='不准确'"/></td>
			<td>准确 <input type="checkbox" ng-checked="vm.printData.holeHeight=='准确'"/></td>
			<td>不准确 <input type="checkbox" ng-checked="vm.printData.holeHeight=='不准确'"/></td>
		</tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">
				重新测绘<input type="checkbox" ng-checked="vm.printData.holeResult=='重新测绘'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				校核<input type="checkbox" ng-checked="vm.printData.holeResult=='校核'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				合格<input type="checkbox" ng-checked="vm.printData.holeResult=='合格'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
		
		<tr>
            <td rowspan="2";style="width:10%;">2 </td>
            <td rowspan="2";style="width:12%;">机具设备</td>
			<td style="height: 30px">运转正常<input type="checkbox" ng-checked="vm.printData.equipmentState=='运转正常'"/></td>
			<td colspan="2">基本正常<input type="checkbox" ng-checked="vm.printData.equipmentState=='基本正常'"/></td>
			<td>不正常<input type="checkbox" ng-checked="vm.printData.equipmentState=='不正常'"/></td>
			<td rowspan="2"  ng-bind="vm.printData.equipmentTime"></td>
	    </tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">更换<input type="checkbox" ng-checked="vm.printData.equipmentResult=='更换'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				现场准确<input type="checkbox" ng-checked="vm.printData.equipmentResult=='现场准确'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				设备正常<input type="checkbox" ng-checked="vm.printData.equipmentResult=='设备正常'"/></td>
	    </tr>
		
		<tr>
			<td rowspan="2">3</td>
			<td rowspan="2">孔位偏移</td>
			<td >无偏移<input type="checkbox" ng-checked="vm.printData.holeOffset=='无偏移'"/></td>
			<td colspan="2">偏移较小<input type="checkbox" ng-checked="vm.printData.holeOffset=='偏移较小'"/></td>
			<td>偏移较大<input type="checkbox" ng-checked="vm.printData.holeOffset=='偏移较大'"/></td>
			<td rowspan="2" ng-bind="vm.printData.holeOffsetTime"></td>
		</tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">满足<input type="checkbox" ng-checked="vm.printData.holeOffsetResult=='满足'"/>&nbsp;&nbsp;&nbsp;不满足勘察要求，重新钻孔<input type="checkbox" ng-checked="vm.printData.holeOffsetResult=='不满足勘察要求，重新钻孔'"/></td>
	    </tr>
		
		<tr>
			<td rowspan="2">4</td>
			<td rowspan="2" >钻孔，取样</td>
			<td >取样位置正常<input type="checkbox" ng-checked="vm.printData.sampleState=='取样位置正常'"/></td>
			<td colspan="2">取样位置偏浅<input type="checkbox" ng-checked="vm.printData.sampleState=='取样位置偏浅'"/></td>
			<td>取样位置偏深<input type="checkbox" ng-checked="vm.printData.sampleState=='取样位置偏深'"/></td>
			<td rowspan="2" ng-bind="vm.printData.sampleTime"></td>
		</tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">满足<input type="checkbox" ng-checked="vm.printData.sampleResult=='满足'"/>&nbsp;&nbsp;&nbsp;不满足勘察要求，重新钻孔<input type="checkbox" ng-checked="vm.printData.sampleResult=='不满足勘察要求，重新钻孔'"/></td>
	    </tr>
		
		<tr>
			<td rowspan="2">5</td>
			<td rowspan="2">原位测试</td>
			<td >满足勘察要求<input type="checkbox" ng-checked="vm.printData.orginalState=='满足勘察要求'"/></td>
			<td colspan="2">基本满足勘察要求<input type="checkbox" ng-checked="vm.printData.orginalState=='基本满足勘察要求'"/></td>
			<td>不满足勘察要求<input type="checkbox" ng-checked="vm.printData.orginalState=='不满足勘察要求'"/></td>
			<td rowspan="2" ng-bind="vm.printData.orginalTime"></td>
		</tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">合格<input type="checkbox" ng-checked="vm.printData.orginalResult=='合格'"/>&nbsp;&nbsp;
				基本合格<input type="checkbox"ng-checked="vm.printData.orginalResult=='基本合格'"/>&nbsp;&nbsp;
				不合格，重新补做<input type="checkbox" ng-checked="vm.printData.orginalResult=='不合格，重新补做'"/></td>
	    </tr>
		
		<tr>
			<td rowspan="2">6</td>
			<td rowspan="2">岩土样封存、储存</td>
			<td >样品制作良好<input type="checkbox" ng-checked="vm.printData.rockState=='样品制作良好'"/></td>
			<td colspan="2">样品制作合格<input type="checkbox" ng-checked="vm.printData.rockState=='样品制作合格'"/></td>
			<td>样品制作不合格<input type="checkbox" ng-checked="vm.printData.rockState=='样品制作不合格'"/></td>
			<td rowspan="2" ng-bind="vm.printData.rockTime"></td>
		</tr>
		<tr>
			<td>处理意见</td>
			<td colspan="3">不处理<input type="checkbox" ng-checked="vm.printData.rockResult=='不处理'"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;重新制作封装<input type="checkbox" ng-checked="vm.printData.rockResult=='重新制作封装'"/></td>
	    </tr>
		
		<tr>
			<td rowspan="2">7</td>
			<td rowspan="2">野外编录</td>
			<td >质量良好<input type="checkbox" ng-checked="vm.printData.outState=='质量良好'"/></td>
			<td colspan="2">质量一般<input type="checkbox" ng-checked="vm.printData.outState=='质量一般'"/></td>
			<td>质量差<input type="checkbox" ng-checked="vm.printData.outState=='质量差'"/></td>
			<td rowspan="2" ng-bind="vm.printData.outTime"></td>
		</tr>

		<tr>
			<td>处理意见</td>
			<td colspan="3">编录内容合格，不处理<input type="checkbox" ng-checked="vm.printData.outResult=='编录内容合格，不处理'"/></br>
				不合格，重新整理<input type="checkbox" ng-checked="vm.printData.outResult=='不合格，重新整理'"/></td>
	    </tr>
		
		<tr>
			<td style="height:140px">检验要求</td>
			<td style="height:110px;text-align:left;padding-top:5px;padding-left:10px;"colspan="6"
				ng-bind-html="vm.html">
			</td>
		</tr>	
		<tr>
			<td style="height:140px">对纠正、改进的跟踪验证结果</td>
			<td colspan="6" style="height:110px;text-align:left;padding-bottom:10px;padding-top:10px;padding-left:10px;" ng-bind="vm.printData.correctDesc">

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
			<td style=" border-top: none;width: 20%;height: 45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
			<td style="border-top: none;width: 30%">
				<span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
				<span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
			</td>
		</tr>
	</table>
	 
</div>
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		
		
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">勘察野外作业钻探任务书</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width: 15%;height: 40px;">工程名称</td>
            <td style="width: 45%;" ng-bind="vm.printData.projectName">  </td>
			<td style="width: 15%;">工程号</td>
            <td style="width: 25%;" ng-bind="vm.printData.projectNo">  </td>
        </tr>
        <tr>
            <td style="width: 15%;height: 40px;border-bottom: none;">工程地点</td>
            <td style="width: 35%;border-bottom: none;" ng-bind="vm.printData.constructionAddress">  </td>
			<td style="width: 15%;border-bottom: none;">日期</td>
            <td style="width: 35%;border-bottom: none;" ng-bind="vm.printData.outDate">  </td>
        </tr>
	</table>
<table class="print_table">
<tr>
            <td style="width: 15%;" rowspan="2">建筑物性质</td>
            <td style="width: 22% ;" colspan="1">结构类型</td>
			<td style="width: 23% ;" colspan="1">层数（高度）</td>
			<td style="width: 15% ;" colspan="1">拟采用基础</br>形式</td>
			<td style="width: 25% ;" colspan="1">基底荷载</td>
        </tr>
         <tr>
            <td style="width: 14% ;height:40px;" colspan="1" ng-bind="vm.printData.buildStructure"></td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.buildHeight"></td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.buildBase"></td>
			<td style="width: 14% ;" colspan="1" ng-bind="vm.printData.buildUnderLoad"></td>
        </tr>
        </table>	
		<table class="print_table">
        <tr>
            <td style="width: 15%;height: 40px;border-top: none;">单项名称</td>
            <td style="width: 45%;border-top: none;">工作量</td>
			<td style="width: 15%;border-top: none;">完成日期</td>
            <td style="width: 25%;border-top: none;">责任人</td>
        </tr>
		<tr>
            <td style="width: 15%;">放孔测量</td>
            <td style="width: 45%;">
			<span style="float:left;padding-bottom:5px;padding-top:5px;padding-left:10px;text-align: left;">
			放孔：<span style="text-decoration:underline" ng-bind="vm.printData.holePoint"></span>点</br>
            地形测量：<span style="text-decoration:underline" ng-bind="vm.printData.holeLand"></span> Km^2</br>
            断面测量：<span style="text-decoration:underline" ng-bind="vm.printData.holeFracture"></span>		Km</br>
            </span>			
			</td>
			<td style="width: 15%;" ng-bind="vm.printData.holeMeasureTime">  </td>
            <td style="width: 25%;" ng-bind="vm.printData.holeMeasureUser">  </td>
        </tr>
		<tr>
            <td style="width: 15%;height: 25px;border-top: none;">控制</td>
            <td style="width: 45%;border-top: none;">  
			<span style="float:left;padding-bottom:5px;padding-top:5px;padding-left:10px;text-align: left;">
			取样孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlSample"></span>个</br>
            鉴别孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlIdentify"></span> 个</br>
            取岩样孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlRock"></span>个</br>
			取土样孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlSoil"></span>个</br>
            取水样孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlWater"></span>个</br>
            水文观测孔：<span style="text-decoration:underline" ng-bind="vm.printData.controlWaterLook"></span>个</br>
            探槽、探井、探坑：<span style="text-decoration:underline" ng-bind="vm.printData.controlDig"></span>个</br>
            </span>	
			</td>
			<td style="width: 15%;border-top: none;" ng-bind="vm.printData.controlTime">  </td>
            <td style="width: 25%;border-top: none;" ng-bind="vm.printData.controlUser">  </td>
        </tr>
		<tr>
            <td style="width: 15%;height: 25px;">野外测试</td>
            <td style="width: 45%;"> 
			<span style="float:left;padding-bottom:5px;padding-top:5px;padding-left:10px;text-align: left;">
			静探：<span style="text-decoration:underline" ng-bind="vm.printData.outSilence"></span>个</br>
            动探孔：<span style="text-decoration:underline" ng-bind="vm.printData.outAction"></span> 个</br>
            标贯孔：<span style="text-decoration:underline" ng-bind="vm.printData.outPass"></span>个</br>
			波速测试孔：<span style="text-decoration:underline" ng-bind="vm.printData.outWaveSpeed"></span>个</br>
            水文测试孔：<span style="text-decoration:underline" ng-bind="vm.printData.outWater"></span>个</br>
            其他：<span style="text-decoration:underline" ng-bind="vm.printData.outOther"></span></br>
            </span>
			</td>
			<td style="width: 15%;" ng-bind="vm.printData.outTime">  </td>
            <td style="width: 25%;" ng-bind="vm.printData.outUser">  </td>
        </tr>
	    <tr>
            <td style="width: 15%;height: 25px;">取样</td>
            <td style="width: 45%;">
			<span style="float:left;padding-bottom:5px;padding-top:5px;padding-left:10px;text-align: left;">
			原状土样： <span style="text-decoration:underline" ng-bind="vm.printData.sampleOriginalSoil"></span>件</br>
            扰动土样：<span style="text-decoration:underline" ng-bind="vm.printData.sampleActionSoil"></span> 件</br>
            岩芯样：<span style="text-decoration:underline" ng-bind="vm.printData.sampleRock"></span>	件</br>
			水   样：<span style="text-decoration:underline" ng-bind="vm.printData.sampleWater"></span>件</br>
            </span>			
			</td>
			<td style="width: 15%;" ng-bind="vm.printData.sampleTime">  </td>
            <td style="width: 25%;" ng-bind="vm.printData.sampleUser">  </td>
        </tr>

	</table>
    <table class="print_table">
        <tr>
            <td style="width: 100%;height: 30px;border-top: none;">技术安全要求
			</td>            
            </tr>
    </table>	
    <table class="print_table">
        <tr>
            <td style="text-align:left;border-top:none;">
			<span style="float:left;padding-top:5px;padding-bottom:5px;padding-left:10px;text-align:left;"
                  ng-bind-html="vm.html">
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
            <td style=" border-top: none;width: 20%;height: 45px" ng-bind="vm.printData.nodes[$index+1].activityName"></td>
            <td style="border-top: none;width: 30%">
                <span ng-bind="vm.printData.nodes[$index+1].userName" style="margin-right: 10px;"></span>
                <span ng-bind="vm.printData.nodes[$index+1].end|date:'yyyy-MM-dd'"></span>
            </td>
        </tr>
    </table>
	<p style="font-size: 12px;" >注：本表供野外作业使用，由工程负责人填写。</p>
	</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="print_area" style="display: none;">
    <h2 style="text-align: center;" ng-bind="vm.printData.companyName"></h2>
    <h3 style="text-align: center;">岩土工程勘察质量综合评审表</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号:'+vm.printData.formNo"></p>
	<table class="print_table">
        <tr>
            <td style="width:10%;height: 50px;">工序</td>
            <td style="width:50%;"> 评  审  内  容</td>
			<td style="width: 20%;">评分标准</td>
            <td style="width: 20%;">评定分数</td>
        </tr>
        <tr>
            <td  rowspan="2">勘察纲要</td>
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">委托书和技术要求明确，已有资料和经验充分利用，初步分析到位。</span>
			</td>
            <td style="width: 20%;">5</td>
            <td style="width: 20%;" ng-bind="vm.printData.score0">  </td>
        </tr>
				
        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">勘探点布置、工作量、勘察手段符合规范要求和场地实际，
			满足业主和设计要求，方案经济合理。</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score1">  </td>
        </tr>
		
		
		 <tr>
            <td  rowspan="4">勘探与</br>原位测试</td>
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">勘探及原位测试工作量满足纲要及规范要求。</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score2">  </td>
        </tr>
				
        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">勘探及原位测试与操作符合规范要求。</span>
			</td>
            <td style="width: 20%;">5</td>
            <td style="width: 20%;"ng-bind="vm.printData.score3">  </td>
        </tr>
		        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">取土、水样、操作符合规范要求，地下水位量测准确。</span>
			</td>
            <td style="width: 20%;">5</td>
            <td style="width: 20%;"ng-bind="vm.printData.score4">  </td>
        </tr>
		
		        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">记录齐全、清晰，岩性鉴别准确。</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score5">  </td>
        </tr>
		
		
		 <tr>
            <td rowspan="1" style="height: 50px">室内试验</td>
            <td colspan="1">
			<span style="float:left;padding-left:10px;text-align: left;">室内岩土成果报告准确。</span>
			</td>
            <td style="width: 20%;">5</td>
            <td style="width: 20%;"ng-bind="vm.printData.score6">  </td>
        </tr>
		
		
		
		
		 <tr>
            <td  rowspan="5">勘</br>勘</br>察</br>报</br>告</td>
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">报告文字通顺，用词规范，图表美
			观适用，无错漏。</span>
			</td>
            <td style="width: 20%;">5</td>
            <td style="width: 20%;"ng-bind="vm.printData.score7">  </td>
        </tr>
				
        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;padding-right: 10px;text-align: left;">各种图件比例尺准确、内容合理、
			完整美观、符合规范要求。</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score8">  </td>
        </tr>
		        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">指标统计计算书清晰，依据规范，用多种方法综合分析提出岩土的承载力等值</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;" ng-bind="vm.printData.score9">  </td>
        </tr>
		
		        <tr>
 
            <td colspan="1" style="height: 50px">
			<span style="float:left;padding-left:10px;text-align: left;">对不良地质现象分析、抗震评价、场地稳定性评价、特殊岩土类描述计算和评价均符合规范规定，资料完整可靠</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score10">  </td>
        </tr>		
	    <tr>
            <td colspan="1" style="height: 50px">
                <span style="float:left;padding-left:10px;text-align: left;">提供多种地基方案，计算正确，符合规范，对边坡和深基坑支护及其它基础工程所提供的参数及方案均符合规范，经济合理，安全可靠。</span>
			</td>
            <td style="width: 20%;">10</td>
            <td style="width: 20%;"ng-bind="vm.printData.score11">  </td>
        </tr>		
		<tr>
           <td style="width:10%;border-bottom: none;height: 50px"colspan="1">审校</td>
		   <td style="width:50%;height: 25px;border-bottom: none;">
		   <span style="float:left;padding-left:10px;text-align: left;">一切原始资料、图纸、最终成果的审校签署完整</span>
		   </td>		
            <td style="width: 20%;border-bottom: none;">5</td>
            <td style="width: 20%;border-bottom: none;"ng-bind="vm.printData.score12"> </td>
        </tr>
		</table>
		
		
	<table class="print_table">
            <tr>
            <td style="width: 10%;height: 50px">评审</br>意见</td>
			<td style="width: 30%;">
			    <span style="float:left;padding-left:10px;padding-right: 10px;text-align: left;" ng-bind="vm.printData.reviewResult"></span></td>
            <td style="width: 10%;">质量</br>等级</td>
            <td style="width: 20%;" ng-bind="vm.printData.reviewLevel"></td>
			<td style="width: 15%;">实际</br>得分</td>
            <td style="width: 15%;" ng-bind="vm.printData.reviewScore"></td>
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
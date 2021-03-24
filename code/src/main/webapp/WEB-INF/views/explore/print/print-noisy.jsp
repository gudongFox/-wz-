<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="print_area" style="display: none;">
    <h2 style="text-align: center;">中冶建工集团有限公司</h2>
    <h3 style="text-align: center;">噪音污染监测记录</h3>
    <p style="float: right;font-size: 12px;" ng-bind="'编号：'+vm.printData.formNo"></p>

    <table class="print_table">
        <tr>
            <td style="width: 60%;height: 25px;" colspan="2">施工噪音监测记录表</td>
            <td style="width: 15%;"> 编号</td>
            <td style="width: 25%;" ></td>
        </tr>

        <tr>
            <td style="width: 20%;height: 45px;" colspan="1">项目名称</td>
            <td style="width: 40%;" ng-bind="vm.printData.projectName"></td>
            <td style="width: 15%;">监测日期</td>
            <td style="width: 25%;" ng-bind="vm.printData.recordDate"></td>

        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width: 60%;height: 25px;border-top: none;" colspan="1">监测仪器型号</td>
            <td style="width: 40%;border-top: none;" ng-bind="vm.printData.machineName"></td>
        </tr>

    </table>

    <table class="print_table">
        <tr>
            <td style="height: 45px;border-top: none;border-bottom: none;" ng-bind="vm.printData.nosiyRule"></td>

        </tr>

    </table>


    <table class="print_table">
        <tr>
            <td style="text-align: center;height: 80px;" colspan="1">施工现场示意图</br></br>
                施工场地边界及测点位置
            </td>

        </tr>

        <tr>
            <td style="height: 320px;border-bottom: none;" colspan="1"></td>

        </tr>

    </table>

    <table class="print_table">
        <tr>
        <tr>
            <td style="width: 10%;">序号</td>
            <td style="width: 20%;"> 监测区域</td>
            <td style="width: 20%;">监测时间</td>
            <td style="width: 20%;"> 监测值（db)</td>
            <td style="width: 30%;">监测人</td>

        </tr>

        <tr>
            <td style="width: 10%;" > 1</td>
            <td style="width: 20%;"> 监测点1</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>

        </tr>

        <tr>
            <td style="width: 10%;" > 2</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;"> 3</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;" >4</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;"> 5</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>

        <tr>
            <td style="width: 10%;"> 6</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;"> 7</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;"> 8</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>


        <tr>
            <td style="width: 10%;"> 9</td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 20%;"></td>
            <td style="width: 30%;"></td>

        </tr>
    </table>
    <td style="width: 10%;height: 45px;">项目负责人：{{vm.printData.userCreateTaskMan}} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 监测人：{{vm.printData.usertask2Man}}
    </td>


</div>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
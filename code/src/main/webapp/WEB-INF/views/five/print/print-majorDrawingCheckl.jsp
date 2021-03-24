<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>



<style id="print_style">
    .print_table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }

    .print_table td {
        border: solid #000 1px;
        height: 45px;
        text-align: center;
        word-wrap:break-word;
        word-break:break-all;
    }
    .print_table2 {
        border: solid #000 0px;
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
    <h3 style="text-align: center;"><span ng-bind="vm.printData.year"></span><span>专业图纸验收清单</span></h3>
    <table class="print_table2">
        <tr>
            <td style="width: 10%">设计单位:</td>
            <td>  <span style="" ng-bind="vm.printData.deptName"></span></td>
            <td style="width: 10%">项目代码:</td>
            <td> <span style="" ng-bind="vm.printData.projectNo"></span></td>
            <td style="width: 10%">日期:</td>
            <td> <span style="" ng-bind="vm.printData.checkTime"></span></td>
            <td style="width: 10%">验收单号:</td>
            <td><span style="" ng-bind="vm.printData.formNo"></span></td>
        </tr>
        <tr>
            <td>阶段:</td>
            <td> <span style="" ng-bind="vm.printData.stageName"></span></td>
            <td>专业:</td>
            <td><span style="" ng-bind="vm.printData.major"></span></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td style="width: 10%">项目名称:</td>
            <td cowspan="7">
                <span style="" ng-bind="vm.printData.projectName"></span>
            </td>
        </tr>
    </table>

    <table class="print_table">
        <tr>
            <td style="width:10% ">图号</td>
            <td  style="width:25% " ng-bind="vm.printData.drawNo"></td>
            <td style="width:10% ">密级</td>
            <td  style="width:25% "  ng-bind="vm.printData.secretLevel"></td>
            <td  style="width:15% ">完成时间</td>
            <td  style="width:15% " ng-bind="vm.printData.gmtModified"></td>
        </tr>
        <tr>
            <td></td>
            <td>文稿/底图张数</td>
            <td>复制份数</td>
            <td>装订规格</td>
            <td colspan="2" >折合A1张数</td>
        </tr>
        <tr>
            <td>中文</td>
            <td ng-bind="vm.printData.inlandPage"></td>
            <td ng-if="vm.printData.inlandPage==null||vm.printData.inlandPage==0">---</td>
            <td ng-if="vm.printData.inlandPage!=null&&vm.printData.inlandPage!=0" ng-bind="vm.printData.copyNumber"></td>
            <td rowspan="3" >
                <span  ng-bind="'活      页'+vm.printData.leaflet+'份'"></span><br>
                <span  ng-bind="'简      装'+vm.printData.paperback+'份'"></span><br>
                <span  ng-bind="'国内精装'+vm.printData.inlandHardbound+'份'"></span><br>
                <span  ng-bind="'国外精装'+vm.printData.foreignHardbound+'份'"></span>
            </td>
            <td>底图</td>
            <td>蓝图</td>
        </tr>
        <tr>
            <td>外文</td>
            <td ng-bind="vm.printData.foreignPage"></td>
            <td ng-if="vm.printData.foreignPage==null||vm.printData.foreignPage==0">---</td>
            <td ng-if="vm.printData.foreignPage!=null&&vm.printData.foreignPage!=0" ng-bind="vm.printData.copyNumber"></td>

            <td rowspan="2" ng-bind="vm.printData.drawA1Number">13.5</td>
            <td rowspan="2" ng-bind="vm.printData.lanDrawing">13.5</td>
        </tr>
        <tr>
            <td>图纸</td>
            <td ng-bind="vm.printData.drawNumber"></td>
            <td ng-bind="vm.printData.copyNumber"></td>

        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width: 70%;border-top: 0px;" rowspan="3">
                <p style="text-align: left;margin-top: 1px;">
                    <span>备注：</span><br>
                    <span ng-bind="vm.printData.remark"></span>
                </p>
            </td>
            <td style="width: 30%;border-top: 0px;">发图<span ng-bind="vm.printData.sendNumber">0</span>份</td>
        </tr>
        <tr>
            <td>入库<span>1</span>份</td>
        </tr>
        <tr>
            <td>总师<span ng-bind="vm.printData.totalNumber">0</span>份</td>
        </tr>

    </table>
    <table class="print_table2">
        <tr>
            <td>验收人</td>
            <td ng-bind="vm.printData.pigeonhole"></td>
            <td>交验人</td>
            <td ng-bind="vm.printData.applyManName"></td>
            <td>电话</td>
            <td ng-bind="vm.printData.applyPhone"></td>
            <td>审批人</td>
            <td ng-bind="vm.printData.totalChargeMan"></td>
        </tr>
    </table>
</div>
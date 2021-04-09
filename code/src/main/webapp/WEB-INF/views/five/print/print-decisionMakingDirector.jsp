<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<style id="print_style2">
    table {
        border-collapse: collapse;
        width: 100%;
        font-size: 14px;
    }
    .print_table td {
        border: solid red 1px;
        height: 45px;
        text-align: left;
        border-left: none;
        border-right: none;
        border-top: none;
        border-bottom: none;
    }
   /* .print_table p {
        line-height:100%;//行间距
    }*/
    .pStyle{
        font-family:'仿宋_GB2312','仿宋','宋体',sans-serif;
        letter-spacing: 1px;//字间距
        line-height:100%;//行间距
    }
    .top{
        font-size: 16px;
        font-family:'仿宋_GB2312','仿宋','宋体',sans-serif;
    }
</style>

<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div id="print_area2" hidden>
    <h2 style="text-align: center; color: red;font-size: 26px;font-family:'宋体',sans-serif;">中国五洲工程设计集团有限公司</h2>
    <h1 style="text-align: center; color: red;font-size: 36px;font-family:'宋体',sans-serif;letter-spacing: 2px;" ng-bind="vm.printData.meetingType"></h1>
    <h1 style="font-size: 36px"></h1>
    <table class="print_table">
        <tr style="border-bottom: 2px solid red; ">
            <td class="top" style="width:50%;border-bottom: 2px solid red;  text-align: left;   padding-left: 40px;" ng-bind="vm.printData.deptName2">五洲集团董事会办公室</td>
            <td class="top" style="width:50%;border-bottom: 2px solid red; text-align: right; padding-right: 40px;"  ng-bind="vm.printData.meetingTimePlan2">2020年7月27日</td>
        </tr>
        <tr style="height: 16px"></tr>
        <tr>
            <td colspan="2" style="font-size: 18px;text-align: center" ><strong ng-bind="vm.printData.item">董事会2020年度第十五次会议纪要</strong></td>
        </tr>
        <tr style="height: 16px">
        </tr>
    </table>
    <table class="print_table">
        <tr>
            <td style="width:100%;padding-left:40px;font-size: 24px;">
                <p class="pStyle" style="text-indent: 2em;"><span ng-bind="vm.printData.meetingTimePlan">7月27日</span>，<span ng-bind="vm.printData.compereName">刘志刚</span>董事长在<span ng-bind="vm.printData.meetingTime">201会议室</span>主持召开<span ng-bind="vm.printData.meetType">董事会</span>
                    <span ng-bind="vm.printData.year">2020</span>年度第<span ng-bind="vm.printData.stages">十五</span>次会议。公司董事和其他领导班子成员，及议题相关负责人、董事会秘书列席会议。</p>
            </td>
        </tr>
        <tr>
            <td  style="width:100%;padding-left:40px;font-size: 24px;">
                <p class="pStyle" style="text-indent: 2em;"><span ng-bind="vm.printData.meetingResult"></span><span>现纪要如下：</span></p>
            </td>
        </tr>
    </table>
    <%--审议事项--%>
    <table class="print_table" ng-repeat="detail in vm.printData.detailList">
        <tr >
            <%--如果只要一行数据不显示序号--%>

            <td style="font-size: 24px;width: 100%;padding-left:40px;" valign="top" ng-if="vm.printData.detailList.length==1" ><p class="pStyle" style="text-indent: 2em;" ng-bind="detail.conclusion"></p></td>

            <td style="font-size: 24px;width: 100%;padding-left:40px;" valign="top" ng-if="vm.printData.detailList.length>1"><p class="pStyle" style="text-indent: 2em;"  ng-bind="detail.array+''+detail.conclusion"></p></td>
        </tr>
    </table>



    <table  class="print_table" style="margin-bottom: 1px;border-bottom: 1px;padding-bottom: 1px;">
        <tr >
            <td colspan="2" style=" border-top: 50px; border-bottom: 2px solid red;"></td>
        </tr>
        <tr >
            <td colspan="2" class="top" style="border-top: 2px solid red; ">签字栏：（出席领导 签名）</td>
        </tr>
        <tr style="height: 40px">
            <%--刘志刚--%>
            <td  colspan="2">
                <img style="width: 90px;height: 35px;margin-left: 10px" ng-repeat="leader in vm.printData.companyLeader" ng-src="{{'/sys/attach/downloadPic/'+leader.userLogin}}">
            </td>
        </tr>

        <tr >
            <td class="top"  style="width:15%; border-top: 2px solid red; ">出席</td>
            <td class="top" style="border-top: 2px solid red; " ng-bind="vm.printData.companyLeaderName">刘志刚  湛  峰  李宝林</td>
        </tr>
        <tr>
            <td class="top" style="width:15%;">列席</td>
            <td class="top"  ng-bind="vm.printData.attenderName">连文平  于万河  王建明  谢力平  孟  云  刘 颖</td>

        </tr>
        <tr>
            <td class="top"  style="width:15%; border-bottom: 2px solid red;"></td>
            <td class="top" style="border-bottom: 2px solid red; " ng-bind="vm.printData.attenderName5">林  芳  吴俊杰  孙婧毅  张  维</td>
        </tr>
        <tr>
            <td class="top" style=" width:15%; ">主送:</td>
            <td class="top" style="">公司领导。</td>
        </tr>
        <tr>
            <td class="top" style=" width:15%; ">抄送：</td>
            <td class="top" >各有关部门，存档。</td>
        </tr>
        <tr>
            <td class="top" style="width:15%;border-bottom: 2px solid red;">会议记录：</td>
            <td class="top" style="border-bottom: 2px solid red;" ng-bind="vm.printData.recordManName">林  芳</td>
        </tr>
    </table>
</div>
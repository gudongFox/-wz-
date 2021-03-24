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
    }
</style>


<div id="print_area" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
    <h3 style="text-align: center;">院刊审查</h3>
    <table class="print_table">
        <tr>
            <td style="width:15%;">稿件题目</td>
            <td colspan="3"  ng-bind="vm.item.manuscriptTitle"></td>
        </tr>
        <tr>
            <td style="width:15%;">第一作者</td>
            <td style="width:35%;" ng-bind="vm.item.firstAuthor"></td>
            <td style="width:15%;">投稿时间</td>
            <td style="width:35%;" ng-bind="vm.item.submitTime"></td>
        </tr>
        <tr>
            <td>单位保密人</td>
            <td ng-bind="vm.item.deptSecrecyUserName"></td>
            <td>审稿人</td>
            <td ng-bind="vm.item.readerName"></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:left;"><p style="text-align:center;"><b>审稿要求</b></p><div style="padding-left:15px"><p><b>政治法律方面：</b>有无政治性错误；</br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;有无设计著作版权问题（抄袭、剽窃、虚假著名等）。</p><b>学术技术方面：</b>
                论点是否正确（有无学术性、概念性错误，或含混、失实之处）；</br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;技术是否先进（有无创新，创新的成分和程度如何）；</br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;是否适合当前工作需要（可行性、实用性、普遍性如何）。</p>
                <b>参考价值:</b>&emsp;&emsp;&emsp;&emsp;&emsp;理论意义如何；实用意义如何；有无广泛参考价值。</br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;请根据以上理由，在下面审查意见表中写出您对文稿质量总体评价，以及具体应做补充、</br>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;修改、完善，并建议：</br>
                    &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;1．刊用；2.修改后刊用；3.修改后重审；4.转他人审；5.不予刊用。<p><b>注：稿件要求详见当年征文通知</b></div></td>
        </tr>
        <tr>
            <td rowspan="2">部门审查意见意见：</td>
            <td>推荐栏目</td>
            <td colspan="2" ng-bind="vm.item.recommendColumns"></td>
        </tr>
        <tr>
            <td colspan="3" style="height:120px;text-align: left;padding-left: 15px;padding-right: 15px" ng-bind="vm.item.deptOpinion"></td>
        </tr>
    </table>
</div>
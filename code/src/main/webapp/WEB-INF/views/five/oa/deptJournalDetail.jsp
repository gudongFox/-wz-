<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>

    .ul_size {
        list-style: none;
    }

    .li_size {
        margin-bottom: 5px;
    }

    .span_size {
        height: 30px;
        font-weight: bold;
    }

    .content {
        width: 50%;
        margin-left: 25%;
        margin-top: 50px;
    }

    .content .m-title {
        display: flex;
        align-content: center;
        justify-content: center;
        line-height: 30px;
        font-weight: bold;
    }

    .content .m-top {
        display: flex;
        align-content: center;
    }

    .content .m-center {
        display: flex;
        align-content: center;
    }

    .content .m-bottom {
        display: flex;
        flex-direction: column;
    }

    .m-bottom .u-item {
        display: flex;
        align-content: center;
        margin-bottom: 15px;
    }
    .m-style .u-item {
        display: flex;
        align-content: center;
        padding-right: 150px;
    }

</style>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:43})">协会学会审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">院刊审查</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">院刊审查</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%> style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">稿件题目</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.manuscriptTitle" name="manuscriptTitle"  required   ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">第一作者</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.firstAuthor" name="firstAuthor"   ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">投稿时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="submitTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.submitTime" name="submitTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">定密责任人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" required="true" ng-model="vm.item.deptSecrecyUserName" name="deptSecrecyUserName"  ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptSecrecyUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">审稿人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.readerName" name="readerName" required  ng-disabled="processInstance.myRunningTaskName.indexOf('院刊主编')==-1" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reader');" ng-disabled="processInstance.myRunningTaskName.indexOf('院刊主编')==-1"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="content">
                                    <div class="m-title">审稿要求</div>
                                    <div class="m-top">
                                        <span class="span_size">政治法律方面：</span>
                                        <ul class="ul_size">
                                            <li class="li_size">有无政治性错误；</li>
                                            <li class="li_size"> 有无设计著作版权问题（抄袭、剽窃、虚假著名等）。</li>
                                        </ul>
                                    </div>
                                    <div class="m-center">
                                        <span class="span_size">学术技术方面：</span>
                                        <ul class="ul_size">
                                            <li class="li_size">论点是否正确（有无学术性、概念性错误，或含混、失实之处）；</li>
                                            <li class="li_size"> 技术是否先进（有无创新，创新的成分和程度如何）；</li>
                                            <li class="li_size"> 是否适合当前工作需要（可行性、实用性、普遍性如何）。</li>
                                        </ul>
                                    </div>
                                    <div class="m-bottom">
                                        <div class="u-item">
                                            <span class="span_size">参 考 价 值：</span>
                                            <ul class="ul_size">
                                                <li class="li_size"> 理论意义如何；实用意义如何；有无广泛参考价值。</li>
                                                <li class="li_size"> 请根据以上理由，在下面审查意见表中写出您对文稿质量总体评价，以及具体应做补充、修改、完善，并建议：</li>
                                                <li class="li_size"> 1．刊用；2.修改后刊用；3.修改后重审；4.转他人审；5.不予刊用。</li>
                                            </ul>
                                        </div>
                                        <span class="span_size">注：稿件要求详见当年征文通知</span>
                                       <div style="color: red;font-size: 16px " class="row"><input type="checkbox" checked style="width: 16px;height: 16px;" ng-disabled="!processInstance.firstTask">   本人承诺：内容不涉及国家秘密和公司商业秘密。</div>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label ">拟报送栏目</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.recommendColumns"  name="recommendColumns" placeholder="" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>

<%--                            <div class="form-group">
                                <div style="text-align: left;padding-left: 25%"><span style="color: red;">本人承诺：内容不涉及国家秘密和公司商业秘密。</span></div>

                            </div>--%>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print-oaDeptJournalDetail.jsp"/>

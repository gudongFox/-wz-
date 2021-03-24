<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance">经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">投标申请表</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">投标申请表</span>
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
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投标申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.bidManName" name="bidManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('bidMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">投标日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="bidTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.bidTime" name="bidTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true" disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask" >请点击后方按钮选择项目备案</span>
                                </div>
                                <label class="col-md-2 control-label required">项目编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                               name="projectNo" required="true" disabled/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" readonly />
                                </div>
                                <label class="col-md-2 control-label required">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" readonly
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投标级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'投标级别'}:true"
                                            ng-model="vm.item.bidRank" ng-change="vm.changeBidRank();" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投标分管副总</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">评审人员</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUserName" required="true" name="reviewUserName"   disabled />
                                        <span class="input-group-btn" ng-if="vm.item.bidRank=='公司级'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                        <span class="input-group-btn" ng-if="vm.item.bidRank=='非公司级'">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="processInstance.myRunningTaskName.indexOf('发起人')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）')>=0">请点击后方按钮选择</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目所属行业</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectIndustry" name="projectIndustry"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">信息来源</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.informationSource" name="informationSource" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">预计合同额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bidderLinkMan" name="bidderLinkMan" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bidderLinkTel" name="bidderLinkTel" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
<%--                            <div class="form-group">
                                <label class="col-md-2 control-label required">备案号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.recordNo" name="recordNo" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
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

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 工程项目信息
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form2">
            <div class="form-body">
                <div class="form-group"  style="margin-top: 10px">
                    <label class="col-md-2 control-label required">建设地点</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.item.projectAddress" required="true" name="projectAddress"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">建设规模</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.projectScale" required="true" name="projectScale"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">资金来源</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.moneySource" required="true" name="qualification"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.moneySourceOther"  name="moneySourceOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">合格条件与<br>资格要求</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.qualification" required="true" name="qualification"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.qualificationOther"  name="qualificationOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">招标方式</label>
                    <div class="col-md-4">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投标方式'}:true"
                                ng-model="vm.item.bidType" class="form-control"
                                ng-disabled="!processInstance.firstTask"></select>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.bidTypeOther"  name="bidTypeOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label required">合同方式</label>
                    <div class="col-md-4">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                ng-model="vm.item.contractType" class="form-control"
                                ng-disabled="!processInstance.firstTask"></select>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.contractTypeOther"  name="contractTypeOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">工期</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.projectTime" required="true" name="projectTime"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">进度要求</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.scheduleTarget" required="true" name="scheduleTarget"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label required">投标保证金额（万元）</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.depositMoney" required="true" name="depositMoney"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">保证金期限(天)</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.depositTime" required="true" name="depositTime"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">招标范围</label>
                    <div class="col-md-10">
                        <textarea type="text" class="form-control" ng-model="vm.item.bidScope" required="true" name="bidScope"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label ">招标文件及<br>资料费</label>
                    <div class="col-md-10">
                        <textarea type="text" class="form-control" ng-model="vm.item.fileDataCost"  name="fileDataCost"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">工程款支付方式<br>节点与支付条件</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.paymentRule" required="true" name="paymentRule" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">与招标方合作经历</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.cooperationExperience" required="true" name="cooperationExperience" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">潜在竞争对手</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.opponent" required="true" name="opponent" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 投标可行性评价
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form3">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">技术可行性</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.technologyFeasibility" required="true" name="technologyFeasibility"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.technologyFeasibilityOther"  name="technologyFeasibilityOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">生产能力可行性</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.productFeasibility" required="true" name="productFeasibility"ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label ">其他</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.productFeasibilityOther"  name="productFeasibilityOther"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">其他可行性因素</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.otherFeasibility" required="true" name="otherFeasibility" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">合同条款风险</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.contractRisk" required="true" name="contractRisk" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">是否中标</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                ng-model="vm.item.win" class="form-control"
                                ng-disabled="processInstance.myRunningTaskName!=''"></select>
                    </div>
                    <label class="col-md-2 control-label " ng-if="!vm.item.win">未中标原因</label>
                    <div class="col-md-4" ng-if="!vm.item.win">
                        <input type="text" class="form-control" ng-model="vm.item.failReason"  name="failReason"
                               ng-disabled="processInstance.myRunningTaskName!=''"/>
                    </div>
            </div>
        </form>
    </div>
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>



<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                            <th style="width: 40px;">是否已录入合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord|filter:{projectName:vm.qProject}" >
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success" style="cursor: pointer;" ng-if="record.contractReviewId!=0">是</span>
                                <span class="label label-sm label-default" ng-if="record.contractReviewId==0">否</span>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecordModel()">确认</button>
            </div>
        </div>
    </div>

</div>

<jsp:include page="../print/print-oaBidApplyDetail.jsp"/>
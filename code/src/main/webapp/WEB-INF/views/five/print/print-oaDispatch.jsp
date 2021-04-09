<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<div id="page_index" hidden>
    <p>表单编号：{{vm.printData.tableNo}}</p>
</div>
<div class="portlet  box blue">
    <div class="portlet-title plot-hidden" >
        <div class="caption" >
           <span  ng-bind="vm.item.dispatchType"></span><input  ng-click="vm.plot()" class="button" type="button" value="打印">
        </div>
    </div>
    <div class="portlet-body">
        <table class="print_table plot-show">
            <tr>
                <td style="width:10%;height: 30px;">发文标题</td>
                <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.item.dispatchTitle"></td>
            </tr>
            <tr>
                <td style="width:20%;height: 30px;" colspan="1">签发人</td>
                <td style="width: 30%;" colspan="1" ng-bind="vm.item.signerName"></td>
                <td style="width: 20%;" colspan="1">会签人</td>
                <td style="width: 30%;" colspan="1" ng-bind="vm.item.countersignName"></td>
            </tr>
            <tr>
                <td style="width:15%;height: 30px;">秘密等级</td>
                <td style="width: 35%;" ng-bind="vm.item.secretGrade"></td>
                <td style="width: 15%;">期限</td>
                <td style="width: 35%;" ng-bind="vm.item.allottedTime"></td>
            </tr>
            <tr>
                <td style="width:15%;height: 30px;">公司办人员</td>
                <td style="width: 35%;" ng-bind="vm.item.companyOfficeName"></td>
                <td style="width: 15%;">公司保密人员</td>
                <td style="width: 35%;" ng-bind="vm.item.companySecurityName"></td>
            </tr>
            <tr>
                <td style="width:15%;height: 30px;">主题词</td>
                <td style="width: 35%;" ng-bind="vm.item.subjectTerm"></td>
                <td style="width: 15%;">主办单位</td>
                <td style="width: 35%;" ng-bind="vm.item.deptName"></td>
            </tr>
            <tr>
                <td style="width:15%;height: 30px;">主送</td>
                <td style="width: 35%;" ng-bind="vm.item.realSendManName"></td>
                <td style="width: 15%;">抄送</td>
                <td style="width: 35%;" ng-bind="vm.item.copySendManName"></td>
            </tr>
            <tr>
                <td style="width:15%;height: 30px;">创建人</td>
                <td style="width: 35%;" ng-bind="vm.item.creatorName"></td>
                <td style="width: 15%;">创建时间</td>
                <td style="width: 35%;" ng-bind="vm.item.gmtCreate|date:'yyyy-MM-dd'"></td>
            </tr>
        </table>
        <div class="tabbable-line plot-hidden">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded  >
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
                        <table class="print_table">
                            <tr>
                                <td style="width:10%;height: 30px;">发文标题</td>
                                <td colspan="3" style="padding-right: 30px;padding-left: 30px" ng-bind="vm.item.dispatchTitle"></td>
                            </tr>
                            <tr>
                                <td style="width:20%;height: 30px;" colspan="1">签发人</td>
                                <td style="width: 30%;" colspan="1" ng-bind="vm.item.signerName"></td>
                                <td style="width: 20%;" colspan="1">会签人</td>
                                <td style="width: 30%;" colspan="1" ng-bind="vm.item.countersignName"></td>
                            </tr>
                            <tr>
                                <td style="width:15%;height: 30px;">秘密等级</td>
                                <td style="width: 35%;" ng-bind="vm.item.secretGrade"></td>
                                <td style="width: 15%;">期限</td>
                                <td style="width: 35%;" ng-bind="vm.item.allottedTime"></td>
                            </tr>
                            <tr>
                                <td style="width:15%;height: 30px;">公司办人员</td>
                                <td style="width: 35%;" ng-bind="vm.item.companyOfficeName"></td>
                                <td style="width: 15%;">公司保密人员</td>
                                <td style="width: 35%;" ng-bind="vm.item.companySecurityName"></td>
                            </tr>
                            <tr>
                                <td style="width:15%;height: 30px;">主题词</td>
                                <td style="width: 35%;" ng-bind="vm.item.subjectTerm"></td>
                                <td style="width: 15%;">主办单位</td>
                                <td style="width: 35%;" ng-bind="vm.item.deptName"></td>
                            </tr>
                            <tr>
                                <td style="width:15%;height: 30px;">主送</td>
                                <td style="width: 35%;" ng-bind="vm.item.realSendManName"></td>
                                <td style="width: 15%;">抄送</td>
                                <td style="width: 35%;" ng-bind="vm.item.copySendManName"></td>
                            </tr>
                            <tr>
                                <td style="width:15%;height: 30px;">创建人</td>
                                <td style="width: 35%;" ng-bind="vm.item.creatorName"></td>
                                <td style="width: 15%;">创建时间</td>
                                <td style="width: 35%;" ng-bind="vm.item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            </tr>
                        </table>

                       <%-- <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label  ">发文标题</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.dispatchTitle" name="dispatchTitle"       disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label  " style="font-weight: bold;">签发人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.signerName" name="signerName"      disabled/>
                                </div>
                                <label class="col-md-2 control-label  ">会签人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.countersignName" name="countersignName"     disabled />
                                </div>

                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label  " >秘密等级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.secretGrade"   disabled />
                                </div>
                                <label class="col-md-2 control-label ">期限</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.allottedTime"   disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label  ">公司办人员</label>
                                <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.companyOfficeName" name="companyOfficeName"   disabled/>
                                </div>
                                <label class="col-md-2 control-label ">公司保密人员</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companySecurityName" name="companySecurityName"    disabled/>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label  ">主题词</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.subjectTerm" name="subjectTerm"   disabled />
                                </div>

                                <label class="col-md-2 control-label  ">主办单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"     disabled     />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label  ">发文</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dispatch"   disabled />
                                </div>
                                <label class="col-md-2 control-label  ">发文类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dispatchType"   disabled />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label  ">主送</label>

                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.realSendManName"     name="realSendManName" disabled   ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label  ">抄送</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.copySendManName"     name="copySendManName"   disabled ></textarea>
                                </div>
                            </div>


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
                        </div>--%>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../../common/common-redDoc.jsp"/>
<jsp:include page="../../common/common-opinion.jsp"/>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../../common/common-actFlow.jsp"/>



<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:57})">会议管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">决策会议管理</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">决策会议管理</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">事项|会议类型：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="公文标题"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">会议名称</th>
                                <th style="width: 90px">次数</th>
                                <th style="width: 90px">会议类型</th>
                                <th style="width: 140px" >会议时间</th>
                                <th style="width: 120px">会议地点</th>
                                <%--<th style="width: 20px;">议题数目</th>--%>
                                <th style="width: 220px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.item"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="'第'+item.stages+'次'" ></td>
                                <td ng-bind="item.meetingType" ></td>
                                <td ng-bind="item.meetingTimePlan"></td>
                               <%-- <td ng-bind="item.makingNum"></td>--%>
                                <td ng-bind="item.meetingTime"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span >待处理议题</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.listNotCompletedDetail();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDecisionDetailModal();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">议题名称<input type="search"
                                                                              class="form-control input-inline input-sm"
                                                                              placeholder="议题名称"
                                                                              ng-model="vm.signQuteParams.keyWord"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.listNotCompletedDetail();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">议题名称</th>
                                <th>所属部门</th>
                                <th style="width: 120px">会议类型</th>
                                <th style="width: 100px" >议题状态</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.signQutePageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.title"  class="data_title"  ng-click="vm.showDetail(item);"><strong ></strong>
                                </td>
                                <td ng-bind="item.deptName" ></td>
                                <td ng-bind="item.detailType"></td>
                                <td ng-bind="item.issueStatus"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetail(item);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.removeDetail(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.signQutePageInfo" on-load="vm.listNotCompletedDetail()"></my-pager>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="decisionDetailModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog" style="min-width: 650px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设置议题</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题名称</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.title" />
                    </div>
                    <span>&nbsp;</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">议题描述</label>
                    <div class="col-md-10">
                        <textarea  class="form-control" ng-model="vm.currenDetail.detail"  ng-disabled="user.enLogin!=vm.currenDetail.creator"/>
                    </div>
                    <span>&nbsp;</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">附件</label>
                    <div class="col-md-10">
                        <div class="col-md-10">
                            <span  id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1">
                            <i class="fa fa-cloud-upload" style="height:15px " ></i>
                            <span>上传</span>
                            <input type="file" name="file" id="uploadFile"
                                   accept="*"/>
                            </span>

                            <div style="margin-top: 4px"  ng-repeat="file in vm.currenDetailFileList">
                                <span style="color: blue;font-size: 16px;" ng-bind="file.fileName" ng-click="downloadFile(file,true);"></span>
                                <i class="fa  fa-trash-o" ng-click="vm.removeDetailFile(file.id);"></i><br>
                            </div>
                        </div>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div  class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">所属部门</label>
                    <div class="col-md-10">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.currenDetail.deptName" />
                            <span class="input-group-btn" >
                            <button class="btn default" type="button" ng-click="vm.showDeptModal();"><i class="fa fa-cog"></i></button>
                        </span>
                        </div>
                    </div>
                    <span>&nbsp;</span>
                </div>

                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">入库时间</label>
                    <div class="col-md-4">
                        <input value="{{vm.currenDetail.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" class="form-control" disabled/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题状态</label>
                    <div class="col-md-4">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'议题状态'}:true"
                                ng-model="vm.currenDetail.issueStatus" class="form-control"
                                ng-disabled="!processInstance.firstTask"></select>
                    </div>
                    <span>&nbsp;</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">签批领导</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.leader"    disabled/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题管理</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.creatorName"    disabled/>
                    </div>
                    <span>&nbsp;</span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


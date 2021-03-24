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
            <span ng-bind="vm.item.name"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 会议室详情
            <small style="font-size: 50%;" ng-bind="processInstance.creatorName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="true">
                        申请记录 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 200px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">会议室名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="会议室名称"
                                           ng-model="vm.item.name" />
                                </div>
                                <label class="col-md-2 control-label required">会议室容量</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="会议室容量"  ng-model="vm.item.capacity" />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">会议室所属城市</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="会议室所在城市"  ng-model="vm.item.city" />
                                </div>
                                <label class="col-md-2 control-label">会议室所在楼</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="会议室所在楼"  ng-model="vm.item.building" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">会议室所在层</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="会议室所在层"  ng-model="vm.item.floor" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">会议室设备名称</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.equipmentNames" name="equipmentNames"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel();"><i class="fa fa-cog" ></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_detail_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div class="row">
                        <div>
                            <label style="margin-left: 20px;">查询日期：<input id ="now" type="search" class="form-control input-inline input-sm date-picker-start-now"  placeholder="查询日期" ng-model="vm.now"></label>
                            <a class="btn green btn-sm" ng-click="vm.loadDetail();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 40px;">#</th>
                                <th style="width: 120px;">预定人</th>
                                <th style="width: 200px;">会议室</th>
                                <th style="width: 150px;">开始时间</th>
                                <th style="width: 150px;">结束时间</th>
                                <th>备注</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in  vm.listDetail">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td>
                                    <img alt="" class="img-circle" ng-src="{{item.bookUserInfo.thumb_avatar}}" onerror="this.src='/assets/img/avatar.jpg'" style="height: 22px;width: 22px;margin-top: -5px;margin-right: 10px;" />
                                    <span ng-bind="item.bookUserInfo.name" title="">罗冬</span>
                                </td>
                                <td ng-bind="item.meetingRoom.name"></td>
                                <td ng-bind="item.startTime | date:'yyyy-MM-dd HH:mm' "></td>
                                <td ng-bind="item.endTime | date:'yyyy-MM-dd HH:mm' "></td>
                                <td></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="typeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择收入类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.equipmentNames.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" />
                    <span ng-bind="type.name" class="margin-right-10" style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%5==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../print/print.jsp"/>

<%--<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 服务项目</div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();" ng-show="vm.item.creator==user.userLogin" >
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height:{{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>服务内容</th>
                    <th>服务数目</th>
                    <th>服务费用</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.listDetail">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.project" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.projectNum"></td>
                    <td ng-bind="detail.projectCost"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);"  ng-show="vm.item.creator==user.userLogin" title="删除" ng-show=""></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>--%>

<%--<div class="modal fade" id="detailModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">服务详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">服务名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.project" name="project" required="true" ng-disabled="">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">服务数目</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.projectNum" name="projectNum" required="true" ng-disabled="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">服务收费</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.projectCost" name="projectCost" required="true" ng-disabled="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">创建时间</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.gmtCreate |  date:'yyyy-MM-dd'" name="gmtCreate" required="true" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">修改时间</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.gmtModified | date:'yyyy-MM-dd'" name="gmtModified" required="true" disabled>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetail();"  ng-show="vm.item.creator==user.userLogin" >保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>--%>







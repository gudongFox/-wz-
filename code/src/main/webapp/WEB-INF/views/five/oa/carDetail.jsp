<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:58})">用车管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ng-bind="tableName">车辆信息管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.carNo"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 车辆详情
            <small style="font-size: 50%;" ng-bind="processInstance.creatorName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.creator==user.userLogin"
               ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6">
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
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        车辆维护
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        车辆使用
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">车牌号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="车牌号" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.carNo" />
                                </div>

                                <label class="col-md-2 control-label required">车辆类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('车辆类型')}:true" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                            ng-model="vm.item.carType" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">座位数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="座位数" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.seating" />
                                </div>

                                <label class="col-md-2 control-label required">使用性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('车辆使用性质')}:true" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                            ng-model="vm.item.useNature" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">汽车排量</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车排量" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.carCc" />
                                </div>

                                <label class="col-md-2 control-label required">汽车价格</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" placeholder="汽车价格" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.carPrice" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">汽车品牌</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车品牌" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.carBrand" />
                                </div>

                                <label class="col-md-2 control-label required">汽车颜色</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车颜色" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.carColor" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">购买时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="buyDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.buyDate" name="buyDate" required="true"  ng-disabled="rightData.selectOpts.indexOf('删除')<0">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">汽车状态</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('车辆状态')}:true"
                                            ng-model="vm.item.carStatus" class="form-control" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                            ></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">车辆所在单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="rightData.selectOpts.indexOf('删除')<0"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="rightData.selectOpts.indexOf('删除')<0"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">车辆负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName"   />
                                        <span class="input-group-btn" >
                                         <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');" ><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">油卡卡号（一车一卡）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="油卡卡号" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                           ng-model="vm.item.oilCardNo" />
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
                                </div>

                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>


                            <div class="portlet light" >
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-file"></i>  注册登记证
                                    </div>
                                </div>
                                <div class="portlet-body">

                                    <div class="form-group" style="margin-top: 10px">
                                        <label class="col-md-2 control-label required">注册日期</label>
                                        <div class="col-md-4">
                                            <div class="input-group date date-picker" id="registerTime">
                                                <input type="text" class="form-control"
                                                       ng-model="vm.item.registerTime" name="registerTime" required="true"  ng-disabled="rightData.selectOpts.indexOf('删除')<0">
                                                <span class="input-group-btn">
                                            <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                           </span>
                                            </div>
                                        </div>
                                        <label class="col-md-2 control-label">发证类型</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.certificateType" name="certificateType" ng-disabled="rightData.selectOpts.indexOf('删除')<0"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-md-2 control-label required">车辆识别码</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" placeholder="车辆识别码" ng-disabled="rightData.selectOpts.indexOf('删除')<0"
                                                   ng-model="vm.item.vehicleIdentificationCode" />
                                        </div>
                                        <label class="col-md-2 control-label">发动机号</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.engineNumber" name="engineNumber" ng-disabled="rightData.selectOpts.indexOf('删除')<0"/>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </form>

                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div class="portlet-body">
                        <div class="row">
                            <label style="margin-left: 20px;margin-right: 20px;">维护类型：
                                <select ng-options="s.name as s.name for s in commonCodes | filter:{codeCatalog:'车辆维护类型'}:true"
                                        ng-model="vm.params.qName" class="form-control input-inline input-sm">
                                    <option label="" value="">全部</option>
                                </select>
                            </label>
                            <label style="margin-left: 20px;margin-right: 20px;">
                              <input type="text" class="form-control form-control-sm input-sm"   placeholder="年份：2020" ng-model="vm.params.selectYear">
                            </label>
                            <label style="margin-left: 20px;margin-right: 20px;">
                                <input type="text" class="form-control form-control-sm input-sm"   placeholder="月份：1" ng-model="vm.params.selectMonth">
                            </label>
                            <a class="btn green btn-sm" ng-click="vm.loadCarMaintain();"><i class="fa fa-search"></i> 查询 </a>
                            <a class="btn green btn-sm" ng-click="vm.downExcelMaintain();"><i class="fa fa-search"></i> 导出 </a>
                        </div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th style="width: 120px">车辆牌号</th>
                                    <th>维护类型</th>
                                    <th>备注</th>
                                    <th style="width: 50px;">创建人</th>
                                    <th style="width: 100px;">创建时间</th>
                                    <th style="width: 50px;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in  vm.pageInfo.list">
                                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                    <td ng-bind="item.carNo" style="color: blue" ng-click="vm.show(item.id);"></td>
                                    <td ng-bind="item.type"></td>
                                    <td ng-bind="item.remark"></td>
                                    <td ng-bind="item.creatorName"></td>
                                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                                    <td>
                                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadCarMaintain()"></my-pager>
                    </div>
                </div>

                <div class="tab-pane" id="tab_detail_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div class="portlet-body">
                        <div class="row">
                            <label style="margin-left: 20px;margin-right: 20px;">用车方式：
                                <select ng-model="vm.params.qNameType" class="form-control input-inline input-sm">
                                    <option label="" value="">全部</option>
                                    <option label="" value="self">个人用车</option>
                                    <option label="" value="leader">领导用车</option>
                                </select>
                            </label>
                            <label style="margin-left: 20px;margin-right: 20px;">
                                <input type="text" class="form-control form-control-sm input-sm"   placeholder="年份：2020" ng-model="vm.params.selectYear">
                            </label>
                            <label style="margin-left: 20px;margin-right: 20px;">
                                <input type="text" class="form-control form-control-sm input-sm"   placeholder="月份：1" ng-model="vm.params.selectMonth">
                            </label>
                            <a class="btn green btn-sm" ng-click="vm.loadCarApply();"><i class="fa fa-search"></i> 查询 </a>
                            <a class="btn green btn-sm" ng-click="vm.downExcelApply();"><i class="fa fa-search"></i> 导出 </a>
                        </div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th>车辆信息</th>
                                    <th>车辆用途</th>
                                    <th>开始时间</th>
                                    <th>结束时间</th>
                                    <th style="width: 50px;">创建人</th>
                                    <th style="width: 100px;">创建时间</th>
                                    <th style="width: 130px;">任务状态</th>
                                    <th style="width: 50px;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in  vm.pageInfoApply.list">
                                    <td class="dt-right" ng-bind="$index+vm.pageInfoApply.startRow"></td>
                                    <td ng-bind="item.carName" style="color: blue" ng-click="vm.show(item.id);"></td>
                                    <td ng-bind="item.applyReason"></td>
                                    <td ng-bind="item.beginTime | date:'yyyy-MM-dd HH:mm'"></td>
                                    <td ng-bind="item.endTime | date:'yyyy-MM-dd HH:mm'"></td>
                                    <td ng-bind="item.creatorName"></td>
                                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                                    <td ng-bind="item.processName"></td>
                                    <td>
                                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin"></i>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <my-pager data-page-info="vm.pageInfoApply" on-load="vm.loadCarApply()"></my-pager>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>







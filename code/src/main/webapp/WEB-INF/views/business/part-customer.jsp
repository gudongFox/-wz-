
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="table-scrollable">
    <div class="modal fade" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
        <div class="modal-dialog modal-md">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title margin-right-10">客户名称</h4>
                </div>
                <div class="modal-body">

                    <div class="row">
                        <div class="col-md-4">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qCustomer"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th>客户名称</th>
                                <th>录入部门</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}">
                                <td>
                                    <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c.name}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="c.name"></td>
                                <td ng-bind="c.deptName"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn blue" ng-click="vm.saveSelectCustomer();">确认</button>
                </div>
            </div>
        </div>
    </div>
</div>




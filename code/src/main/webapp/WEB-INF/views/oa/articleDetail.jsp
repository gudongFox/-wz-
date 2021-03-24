<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="" ng-bind="vm.item.noticeType"></a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue full-height-content">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> <span ng-bind="vm.item.noticeType"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="full-height-content-body">
                <div ng-if="!vm.showHtml">
                    <div style="width: 80%;height:100%;margin: 10px auto;" ng-bind-html="vm.htmlConvertText(vm.item.noticeContent)"  ></div>
                </div>


                 <div id="wmwrapper" style="width: 100%;height:85%;margin: 10px auto;text-align: center" ng-if="vm.showHtml">
                     <iframe id="owaframe" style="max-width:800px;width: 100%;height: 100%;"  ng-src="{{vm.hrefOWA}}" frameborder="0" referrerpolicy="no-referrer-when-downgrade">

                     </iframe>
                 </div>
                <div style="text-align: right">
                    <div ng-bind="'阅读次数：'+vm.item.pageLoad" style="color: grey;"></div>
                </div>
                <div  ng-repeat="file in vm.files" >
                    <span  style="font-size: 18px;margin-right: 5px" ng-bind="'附件 '+($index+1)"></span>:
                    <img ng-src="{{'/assets/img/'+file.commonAttach.extensionName+'.png'}}" onerror="this.src='/assets/img/doc.png'" style="width: 25px;height: 25px;" alt="">
                    <span style="font-size: 14px;margin-right: 5px;"></span>
                    <span style="font-size: 18px;color: #0b94ea" ng-bind="file.fileName" ng-click="vm.downloadFile(file.id);"></span>
                    <span style="margin-left: 20px" ng-bind="file.commonAttach.sizeName"></span>
                </div>

        </div>
    </div>
</div>











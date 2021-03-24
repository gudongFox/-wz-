angular.module('controllers.wuzhou.file', [])


    .controller("WuZhouFileContentController", function ($state, $stateParams,$rootScope,$scope,fiveHomeService,actService) {
        var fileContent = this;
        $(".cb_edFile:checked").each(function () {
            var index = $(this).attr("data-index");
            if (index < $rootScope.files.length) {
                $rootScope.downloadEdFileWithXml($rootScope.files[index]);
            }
        });
        return fileContent;
    })




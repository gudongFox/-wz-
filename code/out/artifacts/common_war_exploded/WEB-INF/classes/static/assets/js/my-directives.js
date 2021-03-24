angular.module('ui.my.directives', [])
    .directive('myPager', function () {
        return {
            restrict: 'ACE',
            scope: {
                pageInfo: '=pageInfo',
                loadPagedData: '&onLoad'
            },
            templateUrl: '/assets/js/tpl/pager.html'
        };
    })
    .directive('customSwitch', function () {
        return {
            restrict: 'ACE',
            scope: {
                enable: '=enable',
                disabled: '=disabled'
            },
            templateUrl: '/assets/js/tpl/customSwitch.html'
        };
    })
    .directive('customCheck', function () {
        return {
            restrict: 'ACE',
            scope: {
                dataSource: '=dataSource',
                currentValue:'=currentValue',
                single:'=single'
            },
            templateUrl: '/assets/js/tpl/customCheck.html',
            controller:function ($scope) {

                $scope.init = function () {
                    if($scope.dataSource) {
                        if ($scope.dataSource.constructor === String) {
                            var dataSource = [];
                            var _dataSource = $scope.dataSource.split(',');
                            for (var i = 0; i < _dataSource.length; i++) {
                                var code = _dataSource[i];
                                if (code) {
                                    dataSource.push({code: code, name: code});
                                }
                            }
                            $scope.dataSource = dataSource;
                        }

                        var valueList = [];
                        if($scope.currentValue) {
                            valueList =$scope.currentValue.split(',');
                        }

                        for (var i = 0; i < $scope.dataSource.length; i++) {
                            var item = $scope.dataSource[i];
                            item.checked = $.inArray(item.code, valueList) >= 0;
                        }
                    }
                }

                $scope.checkItem=function(item){
                    if($scope.single){
                        if(item.checked){
                            $scope.currentValue=item.code;
                            $scope.init();
                        }else{
                            $scope.currentValue="";
                        }
                    }else {
                        var valueList = $scope.currentValue.split(',');
                        if (item.checked) {
                            valueList.push(item.code);
                        } else {
                            var index = $.inArray(item.code, valueList)
                            valueList.splice(index, 1);
                        }
                        $scope.currentValue = valueList.join(',');
                    }
                }

                $scope.$watch('dataSource', function (newTotal, oldTotal) {
                    $scope.init();
                });
                $scope.$watch('currentValue', function (newTotal, oldTotal) {
                    $scope.init();
                });
            }
        };
    });


function singleCheckBox(selectName, id) {
    setTimeout(function () {
        $(selectName).bind('change', function () {
            if ($(this).is(':checked')) {
                var name = $(this).attr(id);
                $(selectName).each(function () {
                    if ($(this).attr(id) !== name) {
                        $(this).attr("checked", false);
                    }
                });
            }
        });
    }, 100);
}

function initCheckBox(selectName) {
    $(selectName).unbind();
}


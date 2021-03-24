angular.module('ui.my.directives', []).directive('myPager', function () {
    return {
        restrict: 'ACE',
        scope: {
            pageInfo: '=pageInfo',
            loadPagedData: '&onLoad'
        },
        templateUrl: '/assets/js/tpl/pager.html'
    };
});


function  singleCheckBox(selectName,id) {
    setTimeout(function () {
        $(selectName).bind('change',function () {
            if($(this).is(':checked')){
                var name=$(this).attr(id);
                $(selectName).each(function () {
                    if($(this).attr(id)!==name){
                        $(this).attr("checked",false);
                    }
                });
            }
        });
    },100);
}

function  initCheckBox(selectName) {
    $(selectName).unbind();
}


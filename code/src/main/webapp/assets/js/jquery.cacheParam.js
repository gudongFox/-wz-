
$.extend({
    setCacheParam: function(key,params) {
        window.localStorage.setItem(key,JSON.stringify(params));
    },
    getCacheParams:function (key,params) {
        if(window.localStorage.getItem(key)!=null) {
            var cacheParams = jQuery.parseJSON(window.localStorage.getItem(key));
            for (var cacheName in cacheParams) {
                for (var name in params) {
                    if(cacheName==name) {
                        params[name] = cacheParams[name];
                        break;
                    }
                    params[cacheName]=cacheParams[cacheName];
                }
            }
        }
        return params;
    }
});

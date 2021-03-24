angular.module('services.five.home', [])


    .factory('fiveHomeService', function ($q, $http) {
        var listTask=function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:'/myAct/listTask.json',
                params: {
                    userLogin:userLogin,
                    processDefinitionName:'',
                    description:''
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listDoneTask=function (userLogin,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/myAct/listDoneTask.json',
                params: {
                    userLogin:userLogin,
                    pageNum:pageNum,
                    pageSize:pageSize,
                    processDefinitionName:'',
                    description:''
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listDownLoad = function (userLogin,pageNum,pageSize) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: '/sys/software/listPagedData.json',
                params: {
                    userLogin:userLogin,
                    pageNum:pageNum,
                    pageSize:pageSize,
                    deptNames:'',
                    softwareNames:''
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listFirmDateByType=function (type,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:'/oa/notice/listFirmDateByType.json',
                params: {
                    type:type,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var listDateByDeptName=function (deptName,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:'/oa/notice/listDateByDeptName.json',
                params: {
                    deptName:deptName,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var getOtherType=function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:'/oa/notice/getOtherType.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        var getNewsPhoto=function () {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url:'/oa/notice/getNewsPhoto.json',
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

        return {
            listDateByDeptName:function (deptName,userLogin) {
                return listDateByDeptName(deptName,userLogin);
            },
            listFirmDateByType:function (type,userLogin) {
                return listFirmDateByType(type,userLogin);
            },
            listTask:function (userLogin) {
                return listTask(userLogin);
            },
            listDoneTask:function (userLogin,pageNum,pageSize) {
                return listDoneTask(userLogin,pageNum,pageSize);
            },
            listDownLoad: function (userLogin,pageNum,pageSize) {
                return listDownLoad(userLogin,pageNum,pageSize);
            },
            getOtherType:function () {
                return getOtherType();
            },
            getNewsPhoto:function () {
                return getNewsPhoto();
            },
        }
    })

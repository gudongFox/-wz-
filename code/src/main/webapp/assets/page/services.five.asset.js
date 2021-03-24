angular.module('services.five.asset', [])

    .factory('fiveAssetComputerService', function ($q, $http) {

        var head = "/five/asset/assetComputer";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelByComputerNo = function (computerNo) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelByComputerNo.json',
                params: {
                    computerNo: computerNo
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDate = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDate.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downTempleXls = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downExcel = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downExcel.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getModelByComputerNo: function (computerNo) {
                return getModelByComputerNo(computerNo);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listDate: function (userLogin) {
                return listDate(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

            downTempleXls:function(params){
                return downTempleXls(params);
            },

            downExcel:function(params){
                return downExcel(params);
            },

        }
    })

    .factory('fiveAssetAllotService', function ($q, $http) {

        var head = "/five/asset/assetAllot";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDate = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDate.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downTempleXls = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listDate: function (userLogin) {
                return listDate(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (id) {
                return listDetail(id);
            },
            getNewModelDetail: function (id,userLogin) {
                return getNewModelDetail(id,userLogin);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },
        }
    })

    .factory('fiveAssetScrapService', function ($q, $http) {

        var head = "/five/asset/assetScrap";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDate = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDate.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (assetScrapId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    assetScrapId:assetScrapId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downTempleXls = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listDate: function (userLogin) {
                return listDate(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (id) {
                return listDetail(id);
            },
            getNewModelDetail: function (assetScrapId,userLogin) {
                return getNewModelDetail(assetScrapId,userLogin);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },
        }
    })

    .factory('fiveComputerScrapService', function ($q, $http) {

        var head = "/five/asset/computerScrap";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDate = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDate.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (computerScrapId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    computerScrapId:computerScrapId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var downTempleXls = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };



        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            listDate: function (userLogin) {
                return listDate(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (id) {
                return listDetail(id);
            },
            getNewModelDetail: function (computerScrapId,userLogin) {
                return getNewModelDetail(computerScrapId,userLogin);
            },
            downTempleXls:function(params){
                return downTempleXls(params);
            },

        }
    })

    .factory('fiveOaCardChangeService', function ($q, $http) {

        var head = "/five/oa/cardChange";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (id) {
                return listDetail(id);
            },
            getNewModelDetail: function (id,type) {
                return getNewModelDetail(id,type);
            },

            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

        }
    })

    .factory('fiveOaEquipmentAcceptanceService', function ($q, $http) {

        var head = "/five/oa/equipmentAcceptance";

        var getModelById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var update = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/update.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var remove = function (id, userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/remove.json',
                params: {
                    id: id,
                    userLogin: userLogin
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listPagedData = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPagedData.json',
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getModelDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getModelDetailById.json',
                params: {
                    id: id
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var updateDetail = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/updateDetail.json',
                data: item
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var removeDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/removeDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var listDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDetail.json',
                params: {
                    id: id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var downTempleXls = function (params) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/downTempleXls.json',
                responseType: "blob",
                transformResponse: function(data, headers){
                    var response = {};
                    response.data = data;
                    response.headers = headers();
                    return response;
                },
                params: params
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModelDetail = function (id,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    userLogin: userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        return {
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getModelDetailById:function (id) {
                return getModelDetailById(id);
            },
            updateDetail: function (item) {
                return updateDetail(item);
            },
            removeDetail: function (id) {
                return removeDetail(id);
            },
            listDetail: function (id) {
                return listDetail(id);
            },
            getNewModelDetail: function (id,type) {
                return getNewModelDetail(id,type);
            },

            getNewModel: function (userLogin) {
                return getNewModel(userLogin);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            downTempleXls: function (params) {
                return downTempleXls(params);
            },

        }
    })

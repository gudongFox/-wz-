angular.module('services.five.supervise', [])



    .factory('fiveSuperviseService', function ($q, $http) {

        var head = "/five/supervise";

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

        var getNewModelByType = function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByType.json',
                params: {
                    userLogin: userLogin,
                    superviseType:superviseType,
                    companyLeader:companyLeader,
                    businessId:businessId,
                    fileTitle:fileTitle,
                    view:view,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getNewModel = function (userLogin,superviseType) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    superviseType: superviseType,
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

        var listPageTask = function (userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listPageTask.json',
                params: {
                    userLogin: userLogin
                }
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

        var getNewModelDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getPrintData.json',
                params: {
                    id:id
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
            listPageTask: function (userLogin) {
                return listPageTask(userLogin);
            },

            getNewModelDetail: function (id) {
                return getNewModelDetail(id);
            },

            getNewModel: function (userLogin,superviseType) {
                return getNewModel(userLogin,superviseType);
            },

            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },

            getPrintData: function (id) {
                return getPrintData(id);
            },
            getNewModelByType:function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
                 return getNewModelByType(userLogin,superviseType,companyLeader,businessId,fileTitle,view);
            },

        }
    })

    .factory('fiveSuperviseDetailService', function ($q, $http) {

        var head = "/five/superviseDetail";

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

        var getNewModelById = function (userLogin,superviseId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelById.json',
                params: {
                    userLogin: userLogin,
                    superviseId:superviseId
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
        var listDate = function (superviseId) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listDate.json',
                params: {
                    superviseId:superviseId
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

            getNewModelById: function (userLogin,superviseId) {
                return getNewModelById(userLogin,superviseId);
            },
            listDate: function (superviseId) {
                return listDate(superviseId);
            },

            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },

        }
    })

    .factory('fiveSuperviseFileService', function ($q, $http) {

        var head = "/five/superviseFile";

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

        var getNewModelByType = function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelByType.json',
                params: {
                    userLogin: userLogin,
                    superviseType:superviseType,
                    companyLeader:companyLeader,
                    businessId:businessId,
                    fileTitle:fileTitle,
                    view:view,
                }
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

        var getPrintData = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head+'/getPrintData.json',
                params: {
                    id:id
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
            listPageTask: function (userLogin) {
                return listPageTask(userLogin);
            },

            getNewModelDetail: function (id) {
                return getNewModelDetail(id);
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

            getPrintData: function (id) {
                return getPrintData(id);
            },
            getNewModelByType:function (userLogin,superviseType,companyLeader,businessId,fileTitle,view) {
                return getNewModelByType(userLogin,superviseType,companyLeader,businessId,fileTitle,view);
            },

        }
    })

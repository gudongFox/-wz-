angular.module('services.five.budget', [])

    .factory('fiveBudgetIndependentService', function ($q, $http) {

        var head = "/five/budget/independent";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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
        var getDetailByDetailId = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailByDetailId.json',
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


        var getLastYearDetailById = function (id,budgetYear) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
                params: {
                    id: id,
                    budgetYear: budgetYear
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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
        var refreshBudgetDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/refreshBudgetDetail.json',
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
        var getBudgetIdByDeptId = function (budgetType,deptId,budgetYear) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getBudgetIdByDeptId.json',
                params: {
                    budgetType:budgetType,
                    deptId: deptId,
                    budgetYear:budgetYear
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getPublicBudgetById = function (deptId,budgetYear) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getPublicBudgetById.json',
                params: {
                    deptId: deptId,
                    budgetYear:budgetYear
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getDetailByDetailId: function (id) {
                return getDetailByDetailId(id);
            },
            getLastYearDetailById: function (id,budgetYear) {
                return getLastYearDetailById(id,budgetYear);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },
            refreshBudgetDetail: function (id) {
                return refreshBudgetDetail(id);
            },
            getBudgetIdByDeptId: function (budgetType,deptId,budgetYear) {
                return getBudgetIdByDeptId(budgetType,deptId,budgetYear);
            },
            getPublicBudgetById: function (deptId,budgetYear) {
                return getPublicBudgetById(deptId,budgetYear);
            },


        }
    })

    .factory('fiveBudgetProductionService', function ($q, $http) {

        var head = "/five/budget/production";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetFunctionService', function ($q, $http) {

        var head = "/five/budget/function";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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
        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetScientificFundsOutService', function ($q, $http) {

        var head = "/five/budget/scientificFundsOut";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetScientificFundsInService', function ($q, $http) {

        var head = "/five/budget/scientificFundsIn";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetPublicFundsService', function ($q, $http) {

        var head = "/five/budget/publicFunds";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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

        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })
    .factory('fiveBudgetLaborCostService', function ($q, $http) {

        var head = "/five/budget/laborCost";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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

        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })
    .factory('fiveBudgetStaffNumberService', function ($q, $http) {

        var head = "/five/budget/staffNumber";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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

        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetCapitalOutService', function ($q, $http) {

        var head = "/five/budget/capitalOut";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetTurnInRentService', function ($q, $http) {

        var head = "/five/budget/turnInRent";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetPostExpenseService', function ($q, $http) {

        var head = "/five/budget/postExpense";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };
        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },
            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })


    .factory('fiveBudgetFeeService', function ($q, $http) {

        var head = "/five/budget/fee";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })

    .factory('fiveBudgetFeeChangeService', function ($q, $http) {

        var head = "/five/budget/feeChange";

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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
        }
    })

    .factory('fiveBudgetTurnInService', function ($q, $http) {

        var head = "/five/budget/turnIn";

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
        var getDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getDetailById.json',
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

        var insert = function (item) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/insert.json',
                data: item
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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
                url: head + '/getPrintData.json',
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

        var selectAll = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/selectAll.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        }

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

        var getNewModelDetail = function (id,detailId,userLogin) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModelDetail.json',
                params: {
                    id:id,
                    detailId:detailId,
                    userLogin:userLogin,
                }
            }).success(function (data) {
                deferred.resolve(data);
            }).error(function (error) {
                deferred.reject(error);
            });
            return deferred.promise;
        };

        var getLastYearDetailById = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getLastYearDetailById.json',
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


        return {
            insert: function (item) {
                return insert(item);
            },
            update: function (item) {
                return update(item);
            },
            getModelById: function (id) {
                return getModelById(id);
            },
            getDetailById: function (id) {
                return getDetailById(id);
            },
            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
            },
            remove: function (id, userLogin) {
                return remove(id, userLogin);
            },
            getLastYearDetailById: function (id) {
                return getLastYearDetailById(id);
            },

            listPagedData: function (params) {
                return listPagedData(params);
            },
            getPrintData: function (id) {
                return getPrintData(id);
            },
            downloadModel: function (userLogin) {
                return downloadModel(userLogin);
            },
            selectAll:function (userLogin,uiSref) {
                return selectAll(userLogin,uiSref);
            },
            getProjectNo: function (id) {
                return getProjectNo(id);
            },
            moneyTurnCapital: function (money) {
                return moneyTurnCapital(money);
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
            getNewModelDetail: function (id,detailId,userLogin) {
                return getNewModelDetail(id,detailId,userLogin);
            },



        }
    })
    .factory('fiveBudgetMaintainService', function ($q, $http) {

    var head = "/five/budget/maintain";

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

    var getNewModel = function (userLogin,uiSref) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head + '/getNewModel.json',
            params: {
                userLogin: userLogin,
                uiSref:uiSref
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
    var listLastYearDetail = function (id) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head + '/listLastYearDetail.json',
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

    var getTotal = function (temp1,temp2,temp3) {
        var deferred = $q.defer();
        return $http({
            method: 'POST',
            url: head + '/getTotal.json',
            params: {
                temp1: temp1,
                temp2: temp2,
                temp3: temp3,
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
        listLastYearDetail: function (id) {
            return listLastYearDetail(id);
        },
        getTotal: function (temp1,temp2,temp3) {
            return getTotal(temp1,temp2,temp3);
        },

        getNewModelDetail: function (id,userLogin) {
            return getNewModelDetail(id,userLogin);
        },

        getNewModel: function (userLogin,uiSref) {
            return getNewModel(userLogin,uiSref);
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
    }
})
    .factory('fiveBudgetStockService', function ($q, $http) {

        var head = "/five/budget/stock";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
        var listLastYearDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listLastYearDetail.json',
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

        var getTotal = function (temp1,temp2,temp3) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getTotal.json',
                params: {
                    temp1: temp1,
                    temp2: temp2,
                    temp3: temp3,
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
            listLastYearDetail: function (id) {
                return listLastYearDetail(id);
            },
            getTotal: function (temp1,temp2,temp3) {
                return getTotal(temp1,temp2,temp3);
            },

            getNewModelDetail: function (id,userLogin) {
                return getNewModelDetail(id,userLogin);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
        }
    })
    .factory('fiveBudgetBusinessService', function ($q, $http) {

        var head = "/five/budget/business";

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

        var getNewModel = function (userLogin,uiSref) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getNewModel.json',
                params: {
                    userLogin: userLogin,
                    uiSref:uiSref
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
        var listLastYearDetail = function (id) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/listLastYearDetail.json',
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
        var getTotal = function (temp1,temp2,temp3) {
            var deferred = $q.defer();
            return $http({
                method: 'POST',
                url: head + '/getTotal.json',
                params: {
                    temp1: temp1,
                    temp2: temp2,
                    temp3: temp3,
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
            listLastYearDetail: function (id) {
                return listLastYearDetail(id);
            },

            getTotal: function (temp1,temp2,temp3) {
                return getTotal(temp1,temp2,temp3);
            },

            getNewModelDetail: function (id,userLogin) {
                return getNewModelDetail(id,userLogin);
            },

            getNewModel: function (userLogin,uiSref) {
                return getNewModel(userLogin,uiSref);
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
        }
    })
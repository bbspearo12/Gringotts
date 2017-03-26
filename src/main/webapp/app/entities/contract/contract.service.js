(function() {
    'use strict';
    angular
        .module('gringottsApp')
        .factory('Contract', Contract);

    Contract.$inject = ['$resource', 'DateUtils'];

    function Contract ($resource, DateUtils) {
        var resourceUrl =  'api/contracts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startOfContract = DateUtils.convertLocalDateFromServer(data.startOfContract);
                        data.endOfContract = DateUtils.convertLocalDateFromServer(data.endOfContract);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startOfContract = DateUtils.convertLocalDateToServer(copy.startOfContract);
                    copy.endOfContract = DateUtils.convertLocalDateToServer(copy.endOfContract);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startOfContract = DateUtils.convertLocalDateToServer(copy.startOfContract);
                    copy.endOfContract = DateUtils.convertLocalDateToServer(copy.endOfContract);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();

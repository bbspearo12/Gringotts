(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('CompanyDetailController', CompanyDetailController);

    CompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Company', '$http'];

    function CompanyDetailController($scope, $rootScope, $stateParams, previousState, entity, Company, $http) {
        var vm = this;

        vm.company = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gringottsApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    	$scope.exp = function(id) {
        	console.log("getting assets for company "+id);
        	var url = '/api/companies/'+id+'/assets';
            $http.get(url).success(function(data, status, headers, config) {
            	//console.log(data);
            	var contentDispositionHeader = headers('Content-Disposition');
                var result = contentDispositionHeader.split(';')[1].trim().split('=')[1];
                var filename = result.replace(/"/g, '');
                console.log(filename);
                var csvfile = document.createElement('a');
                csvfile.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(data);
                csvfile.target = '_blank';
                csvfile.download = filename;
                csvfile.click();
                $scope.returned_data=JSON.stringify(data);
                return JSON.stringify(data);
            }).error(function(err, status) {
            	console.log(err);
            	console.log(status);
            	$scope.returned_data='Failed to export to csv, Error from server: '+JSON.stringify(err);
            	return JSON.stringify(err);
            });
        };
    }
    
})();

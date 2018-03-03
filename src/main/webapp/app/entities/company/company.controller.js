(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['Company', 'CompanySearch', '$scope', '$http', '$q' ];

    function CompanyController(Company, CompanySearch, $scope, $http, $q) {

        var vm = this;

        vm.companies = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
		$scope.selected = {};
		var totalData = "";
		$scope.data = "";
		$scope.response = null;
        loadAll();
        	var urlCalls = [];
        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanySearch.query({query: vm.searchQuery}, function(result) {
                vm.companies = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }
        	var httpCallback = function(url, callbackfunc) {
        		$http.get(url).success(function (response) {
        			callbackfunc(response)
        		});
        	}
    		$scope.exportData = function() { 
    			  angular.forEach($scope.selected, function(cbvalue, id) {
    				  console.log(id + ': ' + cbvalue);
    				  if (cbvalue == true) {
						console.log("generating csv for: "+id);
						var url = '/api/companies/'+id+'/assets';
						urlCalls.push($http.get(url));
    				  }
    				});
    			  $q.all(urlCalls).then(function success(response) {
    				  var csvfile = document.createElement('a');
    				  var jsondata = "";
    				  for (var i=0; i<response.length; i++) {
    					  jsondata = jsondata.concat(response[i].data);	  
    				  }
        			  console.log(jsondata);
                  csvfile.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(jsondata);
                  csvfile.target = '_blank';
                  csvfile.download = "companies.csv";
                  csvfile.click();    
    			  }, function error(response) {
    				  csvfile.href = 'data:attachment/csv;charset=utf-8,' + encodeURI(JSON.stringify("Error downloading data:",JSON.stringify(response)));
                  csvfile.target = '_blank';
                  csvfile.download = "companies.csv";
                  csvfile.click(); 
    			  });
    		};
    }
    

})();

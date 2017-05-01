(function() {
	'use strict';

	angular
	.module('gringottsApp')
	.controller('HomeController', HomeController);

	HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'AssetSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'ContractSearch', 'ProviderSearch', 'CompanySearch'];

	function HomeController ($scope, Principal, LoginService, $state, AssetSearch, ParseLinks, AlertService, paginationConstants, ContractSearch, ProviderSearch, CompanySearch) {
		var vm = this;

		vm.account = null;
		vm.isAuthenticated = null;
		vm.login = LoginService.open;
		vm.register = register;
		vm.search = search;
		vm.clear = clear;
		$scope.$on('authenticationSuccess', function() {
			getAccount();
		});

		getAccount();

		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}
		function register () {
			$state.go('register');
		}
		
		function clear () {
            vm.assets = [];
            vm.companies = [];
            vm.providers = [];
            vm.contracts = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
        }
		function search (query) {
			console.log("Searchquery is");
			console.log(query);
			getSearchEntity(query)
			if (vm.searchEntity == 'Unknown') {
				return
			}
			// check if the searchQuery is for assets
			esearch(vm.searchQuery);
		}
		
		function getSearchEntity (query) {
			query =  query.toString();
			if (query.includes("asset=")) {
				vm.searchEntity ="asset";
				vm.searchQuery=query.split("=")[1];
				console.log("set search query to ", vm.searchQuery);
			} else if (query.includes("company=")) {
				vm.searchEntity ="company";
				vm.searchQuery=query.split("=")[1];
				console.log("set search query to ", vm.searchQuery);
			} else if (query.includes("provider=")) {
				vm.searchEntity ="provider";
				vm.searchQuery=query.split("=")[1];
				console.log("set search query to ", vm.searchQuery);
			} else if (query.includes("contract=")) {
				vm.searchEntity ="contract";
				vm.searchQuery=query.split("=")[1];
				console.log("set search query to ", vm.searchQuery);
			} else {
				vm.searchEntity ="Unknown";
				vm.searchQuery="";
				console.log("set search query to ", vm.searchQuery);
			}
		}

		// Asset search
		function esearch(searchQuery) {
			if (!searchQuery){
				return vm.clear();
			}
			vm.assets = [];
			vm.contracts = [];
			vm.providers = [];
			vm.companies = [];
			vm.links = {
					last: 0
			};
			vm.page = 0;
			vm.predicate = '_score';
			vm.reverse = false;
			vm.currentSearch = searchQuery;
			
			console.log("Set current search to:", vm.currentSearch);
			load(vm.searchEntity);
		}

		// loadEntities
		function load(entityName) {
			if (entityName === "asset") {
				if (vm.currentSearch) {
					console.log(vm.currentSearch);
					AssetSearch.query({
						query: vm.currentSearch,
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onAssetSuccess, onError);
				} else {
					console.log("No query");
					Asset.query({
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onAssetSuccess, onError);
				}
			} else if (entityName === "contract") {
				if (vm.currentSearch) {
					console.log(vm.currentSearch);
					ContractSearch.query({
						query: vm.currentSearch,
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onContractSuccess, onError);
				} else {
					Contract.query({
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onContractSuccess, onError);
				}
			} else if (entityName === "provider") {
				if (vm.currentSearch) {
					console.log(vm.currentSearch);
					ProviderSearch.query({
						query: vm.currentSearch,
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onProviderSuccess, onError);
				} else {
					Provider.query({
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onProviderSuccess, onError);
				}
			} else if (entityName === "company") {
				if (vm.currentSearch) {
					console.log(vm.currentSearch);
					CompanySearch.query({
						query: vm.currentSearch,
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onCompanySuccess, onError);
				} else {
					Company.query({
						page: vm.page,
						size: vm.itemsPerPage,
						sort: sort()
					}, onCompanySuccess, onError);
				}
			}
			function sort() {
				var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
				if (vm.predicate !== 'id') {
					result.push('id');
				}
				return result;
			}

			function onAssetSuccess(data, headers) {
				vm.links = ParseLinks.parse(headers('link'));
				vm.totalItems = headers('X-Total-Count');
				for (var i = 0; i < data.length; i++) {
					vm.assets.push(data[i]);
				}
			}
			
			function onContractSuccess(data, headers) {
				for (var i = 0; i < data.length; i++) {
					vm.contracts.push(data[i]);
				}
			}
			
			function onProviderSuccess(data, headers) {
				for (var i = 0; i < data.length; i++) {
					vm.providers.push(data[i]);
				}
			}
			
			function onCompanySuccess(data, headers) {
				for (var i = 0; i < data.length; i++) {
					vm.companies.push(data[i]);
				}
			}
			function onError(error) {
				AlertService.error(error.data.message);
			}
		}
	}
})();

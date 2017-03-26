(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .controller('ContractController', ContractController);

    ContractController.$inject = ['Contract', 'ContractSearch'];

    function ContractController(Contract, ContractSearch) {

        var vm = this;

        vm.contracts = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Contract.query(function(result) {
                vm.contracts = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ContractSearch.query({query: vm.searchQuery}, function(result) {
                vm.contracts = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();

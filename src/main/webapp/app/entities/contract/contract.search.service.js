(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .factory('ContractSearch', ContractSearch);

    ContractSearch.$inject = ['$resource'];

    function ContractSearch($resource) {
        var resourceUrl =  'api/_search/contracts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

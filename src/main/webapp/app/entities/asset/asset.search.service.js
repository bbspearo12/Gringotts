(function() {
    'use strict';

    angular
        .module('gringottsApp')
        .factory('AssetSearch', AssetSearch);

    AssetSearch.$inject = ['$resource'];

    function AssetSearch($resource) {
        var resourceUrl =  'api/_search/assets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

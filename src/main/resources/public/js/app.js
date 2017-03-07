(function(angular) {
    'use strict';
    var myApp = angular.module('Movies', []);

    myApp.controller('MoviesController', ['$scope', '$http', function($scope, $http) {

        $http.get("movies.json").then(function(response) {
            $scope.movies = response.data;
        });

        $scope.userId = 1;
        $scope.setUserId = function (id) {
            $scope.userId = id;
        };

        $scope.rate = function(movie, rating) {
            movie.rating = rating;
            var event = {
                userId: $scope.userId,
                itemId: movie.id,
                rating: rating,
                timestamp: new Date().getTime()
            };
            $http.post('/api/ratings', event);
        };

    }]);
})(window.angular);
(function(angular) {
    'use strict';
    var myApp = angular.module('Movies', []);

    myApp.controller('MoviesController', ['$scope', '$http', function($scope, $http) {

        $http.get("movies.json").then(function(response) {
            $scope.movies = response.data;
        });

        // $scope.movies = [
        //     {
        //         id: 1,
        //         genre: "fantasy",
        //         title: "Inception"
        //     },
        //     {
        //         id: 2,
        //         genre: "fantasy",
        //         title: "The Matrix"
        //     },
        //     {
        //         id: 3,
        //         genre: "fantasy",
        //         title: "Back to the Future"
        //     }
        // ];

        $scope.rate = function(movie, rating) {
            movie.rating = rating;
            $http.post('/api/movies/' + movie.id + "/ratings/" + rating);
        };


    }]);
})(window.angular);
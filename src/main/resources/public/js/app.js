(function(angular) {
    'use strict';
    var myApp = angular.module('Movies', []);

    myApp.controller('MoviesController', ['$scope', '$http', function($scope, $http) {

        $http.get("movies.json").then(function(response) {
            $scope.movies = response.data;
            $scope.movieMap = {};
            for (var i in response.data) {
                var movie = response.data[i];
                $scope.movieMap[movie.id] = movie;
            }
        });

        $scope.userId = 1;
        $scope.setUserId = function (id) {
            $scope.userId = id;
            resetRecommendations();
            updateRecommendations();
        };

        $scope.ratings = {};

        $scope.rate = function(movie, rating) {
            var userRatings = $scope.ratings[$scope.userId];
            if (!userRatings) {
                userRatings = {};
                $scope.ratings[$scope.userId] = userRatings;
            }
            userRatings[movie.id] = rating;

            var event = {
                userId: $scope.userId,
                itemId: movie.id,
                rating: rating,
                timestamp: new Date().getTime()
            };
            $http.post('/api/ratings', event);
        };

        function resetRecommendations() {
            $scope.recommendations = new Array(3);
        }

        function updateRecommendations() {
            $http.get('/api/recommendations/' + $scope.userId).then(function (res) {
                var arr = res.data.items;
                arr = arr.splice(0, 3);
                while (arr.length < 3) {
                    arr.push(0);
                }
                $scope.recommendations = arr;
            }, function(err) {
                // not yet available
                $scope.recommendations = new Array(3);
            });
        }
        resetRecommendations();
        updateRecommendations();

        setInterval(updateRecommendations, 3000);

    }]);
})(window.angular);
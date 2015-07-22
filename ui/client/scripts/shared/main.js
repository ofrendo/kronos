(function() {
    'use strict';
    angular.module('app.controllers', []).controller('AppCtrl', [
        '$scope', '$location',
        function($scope, $location) {
            $scope.isSpecificPage = function() {
                var path;
                path = $location.path();
                return _.contains(['/404', '/pages/500', '/pages/login', '/pages/signin', '/pages/signin1', '/pages/signin2', '/pages/signup', '/pages/signup1', '/pages/signup2', '/pages/forgot', '/pages/lock-screen'], path);
            };
            return $scope.main = {
                brand: 'Cronos',
                name: 'Lisa Doe'
            };
        }
    ]).controller('DashboardCtrl', ['$scope', '$websocket', function($scope, $websocket) {
        var ws = $websocket.$new("ws://localhost:9003"); //define new ws-address and connect
        $scope.erpData = []; //the array, where our erp data is pushed to
        $scope.tracker = []; //the array, where our tracking data is stored
        $scope.test = [];
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            console.log("Connection established");
            $scope.products = {};
        });
        ws.$on('$message', function(data) {
            $scope.$apply(function() { //we need to manually apply a scope change, so dynamic array changes will be reflected in view
                if (data.type == "erpData") {
                    $scope.products[data.orderNumber] = {};
                    $scope.products[data.orderNumber].erpData = data.simData;
                    $scope.products[data.orderNumber].state = data.state;

                    console.info($scope.products);
                    // $scope.erpData.push(data); //TODO: apply pop() to remove finished products?
                    // $scope.erpAmount = $scope.erpData.length;
                    // //TODO: calculate progress by looking at the current station
                };
                if (data.type == "machineData") {
                    console.log(data);
                    $scope.products[data.orderNumber].state = data.state;
                    if ($scope.products[data.orderNumber].state == "END_OF_PRODUCTION") {

                    };
                    console.info($scope.products);
                }

            });
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

    }]);


}).call(this);

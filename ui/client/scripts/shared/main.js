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
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            console.log("Connection established");
        });
        ws.$on('$message', function(data) {
            $scope.$apply(function() { //we need to manually apply a scope change, so dynamic array changes will be reflected in view
                if (data.type == "erpData") {
                console.info("Receiving ERP data...");
                  $scope.erpData.push(data); //TODO: apply pop() to remove finished products?
                  $scope.erpAmount = $scope.erpData.length;
                  //TODO: calculate progress by looking at the current station
                };
                
            });
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

    }]);


}).call(this);

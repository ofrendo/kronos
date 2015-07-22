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

        $scope.hallo = "60";
        $scope.counter = 1;
        $scope.erpData = []; //the array, where our erp data is pushed to
        $scope.machineData = []; //the array, where our machine data ist pushed to
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            console.log("Connection established");
        });
        ws.$on('$message', function(data) {
            console.info("Receiving message...");
            $scope.$apply(function() { //we need to manually apply a scope change, so dynamic array changes will be reflected in view
                //console.log($scope.erpData);
                console.log($scope.erpData['orderNumber']);
                //console.log(data);
                if (data.type == "erpData") {
                  $scope.erpData.push(data); //TODO: apply pop() to remove finished products?
                  //TODO: calculate progress by looking at the current station
                };
                
                
            });
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

    }]).controller('HistoryCtrl', ['$scope', '$http', function($scope, $http) {
        console.log("Sind drin");
        $http.get('/data/getDataByAnalysisResult')
        .success(function(data, status, headers, config){
            console.log("YIPPIE");
            console.log(data);
        })
        .error(function(data,status,headers,config){
            console.log("Scheiße gelaufen");
        });
    }]);


}).call(this);

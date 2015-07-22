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
    ]).controller('DashboardCtrl', ['$scope', '$websocket', '$timeout', function($scope, $websocket) {
        var ws = $websocket.$new("ws://localhost:9003"); //define new ws-address and connect

        $scope.hallo = "60";
        $scope.counter = 1;
        $scope.erpData = []; //the array, where our erp data is pushed to
        $scope.machineData = []; //the array, where our machine data ist pushed to
        $scope.products = {};
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            console.log("Connection established");
        });
        ws.$on('$message', function(data) {
            console.log(data);
            $scope.$apply(function() { //we need to manually apply a scope change, so dynamic array changes will be reflected in view
                if (data.type == "erpData") {
                    $scope.products[data.orderNumber] = {};
                    $scope.products[data.orderNumber].erpData = data.simData;
                    $scope.products[data.orderNumber].state = data.state;
                    $scope.products[data.orderNumber].progress = 0;
                };
                if (data.type == "machineData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress += $scope.calculateProgress(data.state);
                    }
                }
                if (data.type == "saData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress += $scope.calculateProgress(data.state);
                        if ($scope.products[data.orderNumber].state == "FINISH") {
                            $scope.products[data.orderNumber].analysisStatus = data.simData.overallStatus;
                            var scoperef = $scope;
                            setTimeout(function() {
                                delete scoperef.products[data.orderNumber];
                            }, 3000);
                        };
                    }
                }

            });
            $scope.calculateProgress = function(state) {
                console.log(state);
                switch (state) {
                    case "INIT":
                        return 2;
                    case "LIGHTBARRIER_1":
                        return 2;
                    case "BETWEEN_L1_L2":
                        return 2;
                    case "LIGHTBARRIER_2":
                        return 2;
                    case "BETWEEN_L2_L3":
                        return 2;
                    case "MILLING_STATION":
                        return 4;
                    case "BETWEEN_L3_L4":
                        return 2;
                    case "DRILLING_STATION":
                        return 3;
                    case "BETWEEN_L4_L5":
                        return 2;
                    case "LIGHTBARRIER_5":
                        return 2;
                    case "END_OF_PRODUCTION":
                        return 9;
                    case "FINISH":
                        return 1;
                }
            }
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

    }]).controller('HistoryCtrl', ['$scope', '$http', function($scope, $http) {
        console.log("Sind drin");
        $http.get('/data/getDataByAnalysisResult')
            .success(function(data, status, headers, config) {
                console.log("YIPPIE");
                console.log(data);
            })
            .error(function(data, status, headers, config) {
                console.log("Scheiße gelaufen");
            });
    }]);


}).call(this);

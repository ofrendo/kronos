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
                if (data.type == "machineData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress = $scope.calculateProgress(data.state);
                        console.log($scope.products[data.orderNumber].progress);
                        if ($scope.products[data.orderNumber].state == "END_OF_PRODUCTION") {
                            delete $scope.products[data.orderNumber];
                        };
                    }


                }
            });
            $scope.calculateProgress = function(state) {
                var progress = 0;
                switch (state) {
                    case "LIGHTBARRIER_1_INTERRUPT":
                        progress = 1;
                        break;
                    case "LIGHTBARRIER_1_CONNECT":
                        progress = 2;
                        break;
                    case "LIGHTBARRIER_2_INTERRUPT":
                        progress = 3;
                        break;
                    case "LIGHTBARRIER_2_CONNECT":
                        progress = 4;
                        break;
                    case "LIGHTBARRIER_3_INTERRUPT":
                        progress = 5;
                        break;
                    case "MILLING_STATION":
                        progress = 6;
                        break;
                    case "LIGHTBARRIER_3_CONNECT":
                        progress = 17;
                        break;
                    case "LIGHTBARRIER_4_INTERRUPT":
                        progress = 18;
                        break;
                    case "DRILLING_STATION":
                        progress = 19;
                        break;
                    case "LIGHTBARRIER_4_CONNECT":
                        progress = 29;
                        break;
                    case "LIGHTBARRIER_5_INTERRUPT":
                        progress = 30;
                        break;
                    case "LIGHTBARRIER_5_CONNECT":
                        progress = 31;
                        break;
                }
                return progress;
            }
        });
        ws.$on('$close', function() {
            console.error("Connection lost");
        });

    }]).controller('HistoryCtrl', ['$scope', '$http', function($scope, $http) {
        var compareData;
        console.log("Sind drin");
        $http.get('/data/getDataByAnalysisResult')
        .success(function(data, status, headers, config){
            console.log("YIPPIE");
            console.log(data);
            $scope.compareData = data;
        })
        .error(function(data,status,headers,config){
            console.log("Scheiße gelaufen");
        });

        // Morris.Bar({
        //     element: 'compareDrillingHeat',
        //     data: "compareData",
        //     xkey: 'compareData.AnalysisResult',
        //     ykeys: ['compareData.AvgDrillingHeat'],
        //     labels: ['OK', 'Not OK'],
        // });
    }]);


}).call(this);

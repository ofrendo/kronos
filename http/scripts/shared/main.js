(function() {
    'use strict';
    angular.module('app.controllers', []).controller('AppCtrl', [
        '$scope', '$location',
        function($scope, $location) {
            $scope.isSpecificPage = function() {
                var path;
                path = $location.path();
                return _.contains(['/404', '/pages/500'], path);
            };
            return $scope.main = {
                brand: 'Cronos',
                name: 'Cronos'
            };
        }
    ]).controller('DashboardCtrl', ['$scope', '$websocket', 'logger', function($scope, $websocket, logger) {
        var ws = $websocket.$new("ws://localhost:9003"); //define new ws-address and connect

        $scope.hallo = "60";
        $scope.counter = 1;
        $scope.erpData = []; //the array, where our erp data is pushed to
        $scope.machineData = []; //the array, where our machine data ist pushed to
        $scope.products = {};
        $scope.kpiData = {};
        $scope.successStreak = 0;
        $scope.drillingSpeed = 0;
        ws.$on('$open', function() { //some status info in console, TODO: status info as toast
            logger.log("Websocket connection established");
            $.get("/data/getKPIs").success(function(data) {
                $scope.kpiData = data.data[0];
                logger.logSuccess("Initial KPIs gathered");
            }).error(function() {});
        });
        ws.$on('$message', function(data) {
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
                        if ($scope.products[data.orderNumber].state == "DRILLING_STATION" && data.simData.itemName == "Drilling Speed") {
                            $scope.drillingSpeed = data.simData.value;
                            drillspeed.load({
                                columns: [
                                    ['data', $scope.drillingSpeed]
                                ]
                            });
                        }
                        if ($scope.products[data.orderNumber].state == "DRILLING_STATION" && data.simData.itemName == "Drilling Heat") {
                            $scope.drillingTemp = data.simData.value.toFixed(2);
                            drilltemp.load({
                                columns: [
                                    ['data', $scope.drillingTemp]
                                ]
                            });
                        }
                        if ($scope.products[data.orderNumber].state == "MILLING_STATION" && data.simData.itemName == "Milling Speed") {
                            $scope.millingSpeed = data.simData.value;
                            millspeed.load({
                                columns: [
                                    ['data', $scope.millingSpeed]
                                ]
                            });
                        }
                        if ($scope.products[data.orderNumber].state == "MILLING_STATION" && data.simData.itemName == "Milling Heat") {
                            $scope.millingTemp = data.simData.value.toFixed(2);
                            milltemp.load({
                                columns: [
                                    ['data', $scope.millingTemp]
                                ]
                            });
                        }

                    }
                }
                if (data.type == "saData") {
                    if ($scope.products[data.orderNumber]) {
                        $scope.products[data.orderNumber].state = data.state;
                        $scope.products[data.orderNumber].progress += $scope.calculateProgress(data.state);
                        if ($scope.products[data.orderNumber].state == "FINISH") {
                            $scope.products[data.orderNumber].analysisStatus = data.simData.overallStatus;
                            if (data.simData.overallStatus == "OK") {
                                $scope.successStreak += 1;
                            } else {
                                $scope.successStreak = 0;
                            }
                            $.get("/data/getKPIs").success(function(data) {
                                $scope.kpiData = data.data[0];
                            }).error(function() {});
                            var scoperef = $scope;
                            setTimeout(function() {
                                delete scoperef.products[data.orderNumber];
                            }, 3000);
                        };
                    }
                }

            });

            $scope.calculateProgress = function(state) {
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

        $scope.genGauge = function(bindTo, max, unit, color) {
            var gauge = c3.generate({
                bindto: bindTo,
                transition: {
                    duration: 500
                },
                data: {
                    columns: [
                        ['data', 0]
                    ],
                    type: 'gauge'
                },
                gauge: {
                    label: {
                        format: function(value, ratio) {
                            return value;
                        },
                        show: true // to turn off the min/max labels.
                    },
                    min: 0, // 0 is default, //can handle negative min e.g. vacuum / voltage / current flow / rate of change
                    max: max, // 100 is default
                    units: unit,
                    width: 39 // for adjusting arc thickness
                },
                color: {
                    pattern: [color]
                },
                size: {
                    height: 100
                }
            });
            return gauge;
        }
        var drillspeed = $scope.genGauge("#drillgauge", 20000, "rpm", "#66B5D7");
        var millspeed = $scope.genGauge("#millgauge", 15000, "rpm", "#66B5D7");
        var drilltemp = $scope.genGauge("#drilltemp", 350, "°C", "#EEC95A");
        var milltemp = $scope.genGauge("#milltemp", 250, "°C", "#EEC95A");



        ws.$on('$close', function() {
            logger.logWarning("Websocket Connection lost!");
        });

    }]).controller('HistoryCtrl', ['$scope', '$http','logger', function($scope, $http, logger) {
        var compareData;
        $http.get('/data/getDataByAnalysisResult')
            .success(function(data, status, headers, config) {
                logger.log("Analysis Results updated!");
                $scope.compareData = data.data;
                var colorFunc = function(row, series, type) {
                    if (row.label == "OK") return "#61A656";
                    else return "#d32030";
                };

                for (var i = 0; i < $scope.compareData.length; i++) {
                    $scope.compareData[i].AvgDrillingHeat = Math.round($scope.compareData[i].AvgDrillingHeat * 10) / 10;
                    $scope.compareData[i].AvgMillingHeat = Math.round($scope.compareData[i].AvgMillingHeat * 10) / 10;
                    $scope.compareData[i].AvgDrillingSpeed = Math.round($scope.compareData[i].AvgDrillingSpeed);
                    $scope.compareData[i].AvgMillingSpeed = Math.round($scope.compareData[i].AvgMillingSpeed);
                    $scope.compareData[i].AvgDrillingTime = Math.round($scope.compareData[i].AvgDrillingTime / 1000 * 100) / 100;
                    $scope.compareData[i].AvgMillingTime = Math.round($scope.compareData[i].AvgMillingTime / 1000 * 100) / 100;
                };

                Morris.Bar({
                    element: 'compareDrillingHeat',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingHeat'],
                    labels: ['Drilling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareDrillingSpeed',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingSpeed'],
                    labels: ['Drilling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareDrillingTime',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgDrillingTime'],
                    labels: ['Drilling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'compareMillingHeat',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingHeat'],
                    labels: ['Milling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareMillingSpeed',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingSpeed'],
                    labels: ['Milling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareMillingTime',
                    data: $scope.compareData,
                    xkey: 'AnalysisResult',
                    ykeys: ['AvgMillingTime'],
                    labels: ['Milling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });

            })
            .error(function(data, status, headers, config) {
                logger.logError("Failed to get analysis results!");
            });

    }]).controller('MaterialCtrl', ['$scope', '$http','logger', function($scope, $http, logger) {
        var compareMaterial;
        $http.get('/data/getDataByMat')
            .success(function(data, status, headers, config) {
                logger.logSuccess("Material Results successfully updated!");
                $scope.compareMaterial = data.data;
                var colorFunc = function(row, series, type) {
                    if (row.label < "7500") return "#5B90BF";
                    else return "#d08770";
                };

                for (var i = 0; i < $scope.compareMaterial.length; i++) {
                    $scope.compareMaterial[i].AvgDrillingHeat = Math.round($scope.compareMaterial[i].AvgDrillingHeat * 10) / 10;
                    $scope.compareMaterial[i].AvgMillingHeat = Math.round($scope.compareMaterial[i].AvgMillingHeat * 10) / 10;
                    $scope.compareMaterial[i].AvgDrillingSpeed = Math.round($scope.compareMaterial[i].AvgDrillingSpeed);
                    $scope.compareMaterial[i].AvgMillingSpeed = Math.round($scope.compareMaterial[i].AvgMillingSpeed);
                    $scope.compareMaterial[i].AvgDrillingTime = Math.round($scope.compareMaterial[i].AvgDrillingTime / 1000 * 100) / 100;
                    $scope.compareMaterial[i].AvgMillingTime = Math.round($scope.compareMaterial[i].AvgMillingTime / 1000 * 100) / 100;
                    $scope.compareMaterial[i].OKPercentage = Math.round((1 - $scope.compareMaterial[i].OKPercentage) * 10000) / 100;
                };

                Morris.Bar({
                    element: 'compareDrillingHeat',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingHeat'],
                    labels: ['Drilling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareDrillingSpeed',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingSpeed'],
                    labels: ['Drilling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareDrillingTime',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgDrillingTime'],
                    labels: ['Drilling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'compareMillingHeat',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingHeat'],
                    labels: ['Milling Heat'],
                    barColors: colorFunc,
                    postUnits: '°C'
                });
                Morris.Bar({
                    element: 'compareMillingSpeed',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingSpeed'],
                    labels: ['Milling Speed'],
                    barColors: colorFunc,
                    postUnits: ' rpm'
                });
                Morris.Bar({
                    element: 'compareMillingTime',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['AvgMillingTime'],
                    labels: ['Milling Time'],
                    barColors: colorFunc,
                    postUnits: ' s'
                });
                Morris.Bar({
                    element: 'analysisResultPercentage',
                    data: $scope.compareMaterial,
                    xkey: 'MaterialNo',
                    ykeys: ['OKPercentage'],
                    labels: ['Defective Goods'],
                    barColors: colorFunc,
                    postUnits: '%'
                });

            })
            .error(function(data, status, headers, config) {
                logger.logError("Failed to get material results!");
            });

    }]);


}).call(this);

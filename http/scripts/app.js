(function(){"use strict";angular.module("app.chart.ctrls",[]).controller("chartCtrl",["$scope",function($scope){return $scope.easypiechart={percent:65,options:{animate:{duration:1e3,enabled:!0},barColor:"#31C0BE",lineCap:"round",size:180,lineWidth:5}},$scope.easypiechart2={percent:35,options:{animate:{duration:1e3,enabled:!0},barColor:"#66B5D7",lineCap:"round",size:180,lineWidth:10}},$scope.easypiechart3={percent:68,options:{animate:{duration:1e3,enabled:!0},barColor:"#60CD9B",lineCap:"square",size:180,lineWidth:20,scaleLength:0}},$scope.gaugeChart1={data:{maxValue:3e3,animationSpeed:40,val:1375},options:{lines:12,angle:0,lineWidth:.47,pointer:{length:.6,strokeWidth:.03,color:"#000000"},limitMax:"false",colorStart:"#A3C86D",colorStop:"#A3C86D",strokeColor:"#E0E0E0",generateGradient:!0,percentColors:[[0,"#60CD9B"],[1,"#60CD9B"]]}},$scope.gaugeChart2={data:{maxValue:3e3,animationSpeed:45,val:1200},options:{lines:12,angle:0,lineWidth:.47,pointer:{length:.6,strokeWidth:.03,color:"#464646"},limitMax:"true",colorStart:"#7ACBEE",colorStop:"#7ACBEE",strokeColor:"#F1F1F1",generateGradient:!0,percentColors:[[0,"#66B5D7"],[1,"#66B5D7"]]}},$scope.gaugeChart3={data:{maxValue:3e3,animationSpeed:50,val:1100},options:{lines:12,angle:0,lineWidth:.47,pointer:{length:.6,strokeWidth:.03,color:"#464646"},limitMax:"true",colorStart:"#FF7857",colorStop:"#FF7857",strokeColor:"#F1F1F1",generateGradient:!0,percentColors:[[0,"#E87352"],[1,"#E87352"]]}}}]).controller("morrisChartCtrl",["$scope",function($scope){return $scope.mainData=[{month:"2013-01",xbox:294e3,will:136e3,playstation:244e3},{month:"2013-02",xbox:228e3,will:335e3,playstation:127e3},{month:"2013-03",xbox:199e3,will:159e3,playstation:13e4},{month:"2013-04",xbox:174e3,will:16e4,playstation:82e3},{month:"2013-05",xbox:255e3,will:318e3,playstation:82e3},{month:"2013-06",xbox:298400,will:401800,playstation:98600},{month:"2013-07",xbox:37e4,will:225e3,playstation:159e3},{month:"2013-08",xbox:376700,will:303600,playstation:13e4},{month:"2013-09",xbox:527800,will:301e3,playstation:119400}],$scope.simpleData=[{year:"2008",value:20},{year:"2009",value:10},{year:"2010",value:5},{year:"2011",value:5},{year:"2012",value:20},{year:"2013",value:19}],$scope.comboData=[{year:"2008",a:20,b:16,c:12},{year:"2009",a:10,b:22,c:30},{year:"2010",a:5,b:14,c:20},{year:"2011",a:5,b:12,c:19},{year:"2012",a:20,b:19,c:13},{year:"2013",a:28,b:22,c:20}],$scope.donutData=[{label:"Download Sales",value:12},{label:"In-Store Sales",value:30},{label:"Mail-Order Sales",value:20},{label:"Online Sales",value:19}]}]).controller("flotChartCtrl",["$scope",function($scope){var areaChart,barChart,lineChart1;return lineChart1={},lineChart1.data1=[[1,15],[2,20],[3,14],[4,10],[5,10],[6,20],[7,28],[8,26],[9,22],[10,23],[11,24]],lineChart1.data2=[[1,9],[2,15],[3,17],[4,21],[5,16],[6,15],[7,13],[8,15],[9,29],[10,21],[11,29]],$scope.line1={},$scope.line1.data=[{data:lineChart1.data1,label:"Product A"},{data:lineChart1.data2,label:"Product B",lines:{fill:!1}}],$scope.line1.options={series:{lines:{show:!0,fill:!0,fillColor:{colors:[{opacity:0},{opacity:.3}]}},points:{show:!0,lineWidth:2,fill:!0,fillColor:"#ffffff",symbol:"circle",radius:5}},colors:["#31C0BE","#8170CA","#E87352"],tooltip:!0,tooltipOpts:{defaultTheme:!1},grid:{hoverable:!0,clickable:!0,tickColor:"#f9f9f9",borderWidth:1,borderColor:"#eeeeee"},xaxis:{ticks:[[1,"Jan."],[2,"Feb."],[3,"Mar."],[4,"Apr."],[5,"May"],[6,"June"],[7,"July"],[8,"Aug."],[9,"Sept."],[10,"Oct."],[11,"Nov."],[12,"Dec."]]}},areaChart={},areaChart.data1=[[2007,15],[2008,20],[2009,10],[2010,5],[2011,5],[2012,20],[2013,28]],areaChart.data2=[[2007,15],[2008,16],[2009,22],[2010,14],[2011,12],[2012,19],[2013,22]],$scope.area={},$scope.area.data=[{data:areaChart.data1,label:"Value A",lines:{fill:!0}},{data:areaChart.data2,label:"Value B",points:{show:!0},yaxis:2}],$scope.area.options={series:{lines:{show:!0,fill:!1},points:{show:!0,lineWidth:2,fill:!0,fillColor:"#ffffff",symbol:"circle",radius:5},shadowSize:0},grid:{hoverable:!0,clickable:!0,tickColor:"#f9f9f9",borderWidth:1,borderColor:"#eeeeee"},colors:["#60CD9B","#8170CA"],tooltip:!0,tooltipOpts:{defaultTheme:!1},xaxis:{mode:"time"},yaxes:[{},{position:"right"}]},barChart={},barChart.data1=[[2008,20],[2009,10],[2010,5],[2011,5],[2012,20],[2013,28]],barChart.data2=[[2008,16],[2009,22],[2010,14],[2011,12],[2012,19],[2013,22]],barChart.data3=[[2008,12],[2009,30],[2010,20],[2011,19],[2012,13],[2013,20]],$scope.barChart={},$scope.barChart.data=[{label:"Value A",data:barChart.data1},{label:"Value B",data:barChart.data2},{label:"Value C",data:barChart.data3}],$scope.barChart.options={series:{stack:!0,bars:{show:!0,fill:1,barWidth:.3,align:"center",horizontal:!1,order:1}},grid:{hoverable:!0,borderWidth:1,borderColor:"#eeeeee"},tooltip:!0,tooltipOpts:{defaultTheme:!1},colors:["#60CD9B","#66B5D7","#EEC95A","#E87352"]},$scope.pieChart={},$scope.pieChart.data=[{label:"Download Sales",data:12},{label:"In-Store Sales",data:30},{label:"Mail-Order Sales",data:20},{label:"Online Sales",data:19}],$scope.pieChart.options={series:{pie:{show:!0}},legend:{show:!0},grid:{hoverable:!0,clickable:!0},colors:["#60CD9B","#66B5D7","#EEC95A","#E87352"],tooltip:!0,tooltipOpts:{content:"%p.0%, %s",defaultTheme:!1}},$scope.donutChart={},$scope.donutChart.data=[{label:"Download Sales",data:12},{label:"In-Store Sales",data:30},{label:"Mail-Order Sales",data:20},{label:"Online Sales",data:19}],$scope.donutChart.options={series:{pie:{show:!0,innerRadius:.5}},legend:{show:!0},grid:{hoverable:!0,clickable:!0},colors:["#60CD9B","#66B5D7","#EEC95A","#E87352"],tooltip:!0,tooltipOpts:{content:"%p.0%, %s",defaultTheme:!1}},$scope.donutChart2={},$scope.donutChart2.data=[{label:"Download Sales",data:12},{label:"In-Store Sales",data:30},{label:"Mail-Order Sales",data:20},{label:"Online Sales",data:19},{label:"Direct Sales",data:15}],$scope.donutChart2.options={series:{pie:{show:!0,innerRadius:.5}},legend:{show:!1},grid:{hoverable:!0,clickable:!0},colors:["#1BB7A0","#39B5B9","#52A3BB","#619CC4","#6D90C5"],tooltip:!0,tooltipOpts:{content:"%p.0%, %s",defaultTheme:!1}}}]).controller("flotChartCtrl.realtime",["$scope",function($scope){}]).controller("sparklineCtrl",["$scope",function($scope){return $scope.demoData1={data:[3,1,2,2,4,6,4,5,2,4,5,3,4,6,4,7],options:{type:"line",lineColor:"#fff",highlightLineColor:"#fff",fillColor:"#60CD9B",spotColor:!1,minSpotColor:!1,maxSpotColor:!1,width:"100%",height:"150px"}},$scope.simpleChart1={data:[3,1,2,3,5,3,4,2],options:{type:"line",lineColor:"#31C0BE",fillColor:"#bce0df",spotColor:!1,minSpotColor:!1,maxSpotColor:!1}},$scope.simpleChart2={data:[3,1,2,3,5,3,4,2],options:{type:"bar",barColor:"#31C0BE"}},$scope.simpleChart3={data:[3,1,2,3,5,3,4,2],options:{type:"pie",sliceColors:["#31C0BE","#60CD9B","#E87352","#8170CA","#EEC95A","#60CD9B"]}},$scope.tristateChart1={data:[1,2,-3,-5,3,1,-4,2],options:{type:"tristate",posBarColor:"#95b75d",negBarColor:"#fa8564"}},$scope.largeChart1={data:[3,1,2,3,5,3,4,2],options:{type:"line",lineColor:"#674E9E",highlightLineColor:"#7ACBEE",fillColor:"#927ED1",spotColor:!1,minSpotColor:!1,maxSpotColor:!1,width:"100%",height:"150px"}},$scope.largeChart2={data:[3,1,2,3,5,3,4,2],options:{type:"bar",barColor:"#31C0BE",barWidth:10,width:"100%",height:"150px"}},$scope.largeChart3={data:[3,1,2,3,5],options:{type:"pie",sliceColors:["#31C0BE","#60CD9B","#E87352","#8170CA","#EEC95A","#60CD9B"],width:"150px",height:"150px"}}}])}).call(this),function(){"use strict";angular.module("app.chart.directives",[]).directive("gaugeChart",[function(){return{restrict:"A",scope:{data:"=",options:"="},link:function(scope,ele,attrs){var data,gauge,options;return data=scope.data,options=scope.options,gauge=new Gauge(ele[0]).setOptions(options),gauge.maxValue=data.maxValue,gauge.animationSpeed=data.animationSpeed,gauge.set(data.val)}}}]).directive("flotChart",[function(){return{restrict:"A",scope:{data:"=",options:"="},link:function(scope,ele,attrs){var data,options,plot;return data=scope.data,options=scope.options,plot=$.plot(ele[0],data,options)}}}]).directive("flotChartRealtime",[function(){return{restrict:"A",link:function(scope,ele,attrs){var data,getRandomData,plot,totalPoints,update,updateInterval;return data=[],totalPoints=300,getRandomData=function(){var i,prev,res,y;for(data.length>0&&(data=data.slice(1));data.length<totalPoints;)prev=data.length>0?data[data.length-1]:50,y=prev+10*Math.random()-5,0>y?y=0:y>100&&(y=100),data.push(y);for(res=[],i=0;i<data.length;)res.push([i,data[i]]),++i;return res},update=function(){plot.setData([getRandomData()]),plot.draw(),setTimeout(update,updateInterval)},data=[],totalPoints=300,updateInterval=200,plot=$.plot(ele[0],[getRandomData()],{series:{lines:{show:!0,fill:!0},shadowSize:0},yaxis:{min:0,max:100},xaxis:{show:!1},grid:{hoverable:!0,borderWidth:1,borderColor:"#eeeeee"},colors:["#5BDDDC"]}),update()}}}]).directive("sparkline",[function(){return{restrict:"A",scope:{data:"=",options:"="},link:function(scope,ele,attrs){var data,options,sparkResize,sparklineDraw;return data=scope.data,options=scope.options,sparkResize=void 0,sparklineDraw=function(){return ele.sparkline(data,options)},$(window).resize(function(e){return clearTimeout(sparkResize),sparkResize=setTimeout(sparklineDraw,200)}),sparklineDraw()}}}]).directive("morrisChart",[function(){return{restrict:"A",scope:{data:"="},link:function(scope,ele,attrs){var colors,data,func,options;switch(data=scope.data,attrs.type){case"line":return colors=void 0===attrs.lineColors||""===attrs.lineColors?null:JSON.parse(attrs.lineColors),options={element:ele[0],data:data,xkey:attrs.xkey,ykeys:JSON.parse(attrs.ykeys),labels:JSON.parse(attrs.labels),lineWidth:attrs.lineWidth||2,lineColors:colors||["#0b62a4","#7a92a3","#4da74d","#afd8f8","#edc240","#cb4b4b","#9440ed"]},new Morris.Line(options);case"area":return colors=void 0===attrs.lineColors||""===attrs.lineColors?null:JSON.parse(attrs.lineColors),options={element:ele[0],data:data,xkey:attrs.xkey,ykeys:JSON.parse(attrs.ykeys),labels:JSON.parse(attrs.labels),lineWidth:attrs.lineWidth||2,lineColors:colors||["#0b62a4","#7a92a3","#4da74d","#afd8f8","#edc240","#cb4b4b","#9440ed"],behaveLikeLine:attrs.behaveLikeLine||!1,fillOpacity:attrs.fillOpacity||"auto",pointSize:attrs.pointSize||4},new Morris.Area(options);case"bar":return colors=void 0===attrs.barColors||""===attrs.barColors?null:JSON.parse(attrs.barColors),options={element:ele[0],data:data,xkey:attrs.xkey,ykeys:JSON.parse(attrs.ykeys),labels:JSON.parse(attrs.labels),barColors:colors||["#0b62a4","#7a92a3","#4da74d","#afd8f8","#edc240","#cb4b4b","#9440ed"],stacked:attrs.stacked||null},new Morris.Bar(options);case"donut":return colors=void 0===attrs.colors||""===attrs.colors?null:JSON.parse(attrs.colors),options={element:ele[0],data:data,colors:colors||["#0B62A4","#3980B5","#679DC6","#95BBD7","#B0CCE1","#095791","#095085","#083E67","#052C48","#042135"]},attrs.formatter&&(func=new Function("y","data",attrs.formatter),options.formatter=func),new Morris.Donut(options)}}}}])}.call(this),function(){"use strict";angular.module("app.ui.form.ctrls",[]).controller("TagsDemoCtrl",["$scope",function($scope){return $scope.tags=["foo","bar"]}]).controller("DatepickerDemoCtrl",["$scope",function($scope){return $scope.today=function(){return $scope.dt=new Date},$scope.today(),$scope.showWeeks=!0,$scope.toggleWeeks=function(){return $scope.showWeeks=!$scope.showWeeks},$scope.clear=function(){return $scope.dt=null},$scope.disabled=function(date,mode){return"day"===mode&&(0===date.getDay()||6===date.getDay())},$scope.toggleMin=function(){var _ref;return $scope.minDate=null!=(_ref=$scope.minDate)?_ref:{"null":new Date}},$scope.toggleMin(),$scope.open=function($event){return $event.preventDefault(),$event.stopPropagation(),$scope.opened=!0},$scope.dateOptions={"year-format":"'yy'","starting-day":1},$scope.formats=["dd-MMMM-yyyy","yyyy/MM/dd","shortDate"],$scope.format=$scope.formats[0]}]).controller("TimepickerDemoCtrl",["$scope",function($scope){return $scope.mytime=new Date,$scope.hstep=1,$scope.mstep=15,$scope.options={hstep:[1,2,3],mstep:[1,5,10,15,25,30]},$scope.ismeridian=!0,$scope.toggleMode=function(){return $scope.ismeridian=!$scope.ismeridian},$scope.update=function(){var d;return d=new Date,d.setHours(14),d.setMinutes(0),$scope.mytime=d},$scope.changed=function(){return void 0},$scope.clear=function(){return $scope.mytime=null}}]).controller("TypeaheadCtrl",["$scope",function($scope){return $scope.selected=void 0,$scope.states=["Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Dakota","North Carolina","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"]}]).controller("RatingDemoCtrl",["$scope",function($scope){return $scope.rate=7,$scope.max=10,$scope.isReadonly=!1,$scope.hoveringOver=function(value){return $scope.overStar=value,$scope.percent=100*(value/$scope.max)},$scope.ratingStates=[{stateOn:"glyphicon-ok-sign",stateOff:"glyphicon-ok-circle"},{stateOn:"glyphicon-star",stateOff:"glyphicon-star-empty"},{stateOn:"glyphicon-heart",stateOff:"glyphicon-ban-circle"},{stateOn:"glyphicon-heart"},{stateOff:"glyphicon-off"}]}])}.call(this),function(){angular.module("app.ui.form.directives",[]).directive("uiRangeSlider",[function(){return{restrict:"A",link:function(scope,ele){return ele.slider()}}}]).directive("uiFileUpload",[function(){return{restrict:"A",link:function(scope,ele){return ele.bootstrapFileInput()}}}]).directive("uiSpinner",[function(){return{restrict:"A",compile:function(ele,attrs){return ele.addClass("ui-spinner"),{post:function(){return ele.spinner()}}}}}])}.call(this),function(){"use strict";angular.module("app.form.validation",[]).controller("wizardFormCtrl",["$scope",function($scope){return $scope.wizard={firstName:"some name",lastName:"",email:"",password:"",age:"",address:""},$scope.isValidateStep1=function(){return void 0},$scope.finishedWizard=function(){return void 0}}]).controller("formConstraintsCtrl",["$scope",function($scope){var original;return $scope.form={required:"",minlength:"",maxlength:"",length_rage:"",type_something:"",confirm_type:"",foo:"",email:"",url:"",num:"",minVal:"",maxVal:"",valRange:"",pattern:""},original=angular.copy($scope.form),$scope.revert=function(){return $scope.form=angular.copy(original),$scope.form_constraints.$setPristine()},$scope.canRevert=function(){return!angular.equals($scope.form,original)||!$scope.form_constraints.$pristine},$scope.canSubmit=function(){return $scope.form_constraints.$valid&&!angular.equals($scope.form,original)}}]).controller("signinCtrl",["$scope",function($scope){var original;return $scope.user={email:"",password:""},$scope.showInfoOnSubmit=!1,original=angular.copy($scope.user),$scope.revert=function(){return $scope.user=angular.copy(original),$scope.form_signin.$setPristine()},$scope.canRevert=function(){return!angular.equals($scope.user,original)||!$scope.form_signin.$pristine},$scope.canSubmit=function(){return $scope.form_signin.$valid&&!angular.equals($scope.user,original)},$scope.submitForm=function(){return $scope.showInfoOnSubmit=!0,$scope.revert()}}]).controller("signupCtrl",["$scope",function($scope){var original;return $scope.user={name:"",email:"",password:"",confirmPassword:"",age:""},$scope.showInfoOnSubmit=!1,original=angular.copy($scope.user),$scope.revert=function(){return $scope.user=angular.copy(original),$scope.form_signup.$setPristine(),$scope.form_signup.confirmPassword.$setPristine()},$scope.canRevert=function(){return!angular.equals($scope.user,original)||!$scope.form_signup.$pristine},$scope.canSubmit=function(){return $scope.form_signup.$valid&&!angular.equals($scope.user,original)},$scope.submitForm=function(){return $scope.showInfoOnSubmit=!0,$scope.revert()}}]).directive("validateEquals",[function(){return{require:"ngModel",link:function(scope,ele,attrs,ngModelCtrl){var validateEqual;return validateEqual=function(value){var valid;return valid=value===scope.$eval(attrs.validateEquals),ngModelCtrl.$setValidity("equal",valid),"function"==typeof valid?valid({value:void 0}):void 0},ngModelCtrl.$parsers.push(validateEqual),ngModelCtrl.$formatters.push(validateEqual),scope.$watch(attrs.validateEquals,function(newValue,oldValue){return newValue!==oldValue?ngModelCtrl.$setViewValue(ngModelCtrl.$ViewValue):void 0})}}}])}.call(this),function(){"use strict";angular.module("app.page.ctrls",[]).controller("invoiceCtrl",["$scope","$window",function($scope,$window){return $scope.printInvoice=function(){var originalContents,popupWin,printContents;return printContents=document.getElementById("invoice").innerHTML,originalContents=document.body.innerHTML,popupWin=window.open(),popupWin.document.open(),popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="styles/main.css" /></head><body onload="window.print()">'+printContents+"</html>"),popupWin.document.close()}}])}.call(this),function(){"use strict";angular.module("app.tables",[]).controller("tableCtrl",["$scope","$filter",function($scope,$filter){var init;return $scope.stores=[{name:"Nijiya Market",price:"$$",sales:292,rating:4},{name:"Eat On Monday Truck",price:"$",sales:119,rating:4.3},{name:"Tea Era",price:"$",sales:874,rating:4},{name:"Rogers Deli",price:"$",sales:347,rating:4.2},{name:"MoBowl",price:"$$$",sales:24,rating:4.6},{name:"The Milk Pail Market",price:"$",sales:543,rating:4.5},{name:"Nob Hill Foods",price:"$$",sales:874,rating:4},{name:"Scratch",price:"$$$",sales:643,rating:3.6},{name:"Gochi Japanese Fusion Tapas",price:"$$$",sales:56,rating:4.1},{name:"Cost Plus World Market",price:"$$",sales:79,rating:4},{name:"Bumble Bee Health Foods",price:"$$",sales:43,rating:4.3},{name:"Costco",price:"$$",sales:219,rating:3.6},{name:"Red Rock Coffee Co",price:"$",sales:765,rating:4.1},{name:"99 Ranch Market",price:"$",sales:181,rating:3.4},{name:"Mi Pueblo Food Center",price:"$",sales:78,rating:4},{name:"Cucina Venti",price:"$$",sales:163,rating:3.3},{name:"Sufi Coffee Shop",price:"$",sales:113,rating:3.3},{name:"Dana Street Roasting",price:"$",sales:316,rating:4.1},{name:"Pearl Cafe",price:"$",sales:173,rating:3.4},{name:"Posh Bagel",price:"$",sales:140,rating:4},{name:"Artisan Wine Depot",price:"$$",sales:26,rating:4.1},{name:"Hong Kong Chinese Bakery",price:"$",sales:182,rating:3.4},{name:"Starbucks",price:"$$",sales:97,rating:3.7},{name:"Tapioca Express",price:"$",sales:301,rating:3},{name:"House of Bagels",price:"$",sales:82,rating:4.4}],$scope.searchKeywords="",$scope.filteredStores=[],$scope.row="",$scope.select=function(page){var end,start;return start=(page-1)*$scope.numPerPage,end=start+$scope.numPerPage,$scope.currentPageStores=$scope.filteredStores.slice(start,end)},$scope.onFilterChange=function(){return $scope.select(1),$scope.currentPage=1,$scope.row=""},$scope.onNumPerPageChange=function(){return $scope.select(1),$scope.currentPage=1},$scope.onOrderChange=function(){return $scope.select(1),$scope.currentPage=1},$scope.search=function(){return $scope.filteredStores=$filter("filter")($scope.stores,$scope.searchKeywords),$scope.onFilterChange()},$scope.order=function(rowName){return $scope.row!==rowName?($scope.row=rowName,$scope.filteredStores=$filter("orderBy")($scope.stores,rowName),$scope.onOrderChange()):void 0},$scope.numPerPageOpt=[3,5,10,20],$scope.numPerPage=$scope.numPerPageOpt[2],$scope.currentPage=1,$scope.currentPageStores=[],(init=function(){return $scope.search(),$scope.select($scope.currentPage)})()}])}.call(this),function(){"use strict";angular.module("app.ui.ctrls",[]).controller("NotifyCtrl",["$scope","logger",function($scope,logger){return $scope.notify=function(type){switch(type){case"info":return logger.log("Heads up! This alert needs your attention, but it's not super important.");case"success":return logger.logSuccess("Well done! You successfully read this important alert message.");case"warning":return logger.logWarning("Warning! Best check yo self, you're not looking too good.");case"error":return logger.logError("Oh snap! Change a few things up and try submitting again.")}}}]).controller("AlertDemoCtrl",["$scope",function($scope){return $scope.alerts=[{type:"success",msg:"Well done! You successfully read this important alert message."},{type:"info",msg:"Heads up! This alert needs your attention, but it is not super important."},{type:"warning",msg:"Warning! Best check yo self, you're not looking too good."},{type:"danger",msg:"Oh snap! Change a few things up and try submitting again."}],$scope.addAlert=function(){var num,type;switch(num=Math.ceil(4*Math.random()),type=void 0,num){case 0:type="info";break;case 1:type="success";break;case 2:type="info";break;case 3:type="warning";break;case 4:type="danger"}return $scope.alerts.push({type:type,msg:"Another alert!"})},$scope.closeAlert=function(index){return $scope.alerts.splice(index,1)}}]).controller("ProgressDemoCtrl",["$scope",function($scope){return $scope.max=200,$scope.random=function(){var type,value;value=Math.floor(100*Math.random()+10),type=void 0,type=25>value?"success":50>value?"info":75>value?"warning":"danger",$scope.showWarning="danger"===type||"warning"===type,$scope.dynamic=value,$scope.type=type},$scope.random()}]).controller("AccordionDemoCtrl",["$scope",function($scope){$scope.oneAtATime=!0,$scope.groups=[{title:"Dynamic Group Header - 1",content:"Dynamic Group Body - 1"},{title:"Dynamic Group Header - 2",content:"Dynamic Group Body - 2"},{title:"Dynamic Group Header - 3",content:"Dynamic Group Body - 3"}],$scope.items=["Item 1","Item 2","Item 3"],$scope.status={isFirstOpen:!0,isFirstOpen1:!0,isFirstOpen2:!0,isFirstOpen3:!0,isFirstOpen4:!0,isFirstOpen5:!0,isFirstOpen6:!0},$scope.addItem=function(){var newItemNo;newItemNo=$scope.items.length+1,$scope.items.push("Item "+newItemNo)}}]).controller("CollapseDemoCtrl",["$scope",function($scope){return $scope.isCollapsed=!1}]).controller("ModalDemoCtrl",["$scope","$modal","$log",function($scope,$modal,$log){$scope.items=["item1","item2","item3"],$scope.open=function(){var modalInstance;modalInstance=$modal.open({templateUrl:"myModalContent.html",controller:"ModalInstanceCtrl",resolve:{items:function(){return $scope.items}}}),modalInstance.result.then(function(selectedItem){$scope.selected=selectedItem},function(){$log.info("Modal dismissed at: "+new Date)})}}]).controller("ModalInstanceCtrl",["$scope","$modalInstance","items",function($scope,$modalInstance,items){$scope.items=items,$scope.selected={item:$scope.items[0]},$scope.ok=function(){$modalInstance.close($scope.selected.item)},$scope.cancel=function(){$modalInstance.dismiss("cancel")}}]).controller("PaginationDemoCtrl",["$scope",function($scope){return $scope.totalItems=64,$scope.currentPage=4,$scope.maxSize=5,$scope.setPage=function(pageNo){return $scope.currentPage=pageNo},$scope.bigTotalItems=175,$scope.bigCurrentPage=1}]).controller("TabsDemoCtrl",["$scope",function($scope){return $scope.tabs=[{title:"Dynamic Title 1",content:"Dynamic content 1.  Consectetur adipisicing elit. Nihil, quidem, officiis, et ex laudantium sed cupiditate voluptatum libero nobis sit illum voluptates beatae ab. Ad, repellendus non sequi et at."},{title:"Disabled",content:"Dynamic content 2.  Lorem ipsum dolor sit amet, consectetur adipisicing elit. Nihil, quidem, officiis, et ex laudantium sed cupiditate voluptatum libero nobis sit illum voluptates beatae ab. Ad, repellendus non sequi et at.",disabled:!0}],$scope.navType="pills"}]).controller("TreeDemoCtrl",["$scope",function($scope){return $scope.list=[{id:1,title:"Item 1",items:[]},{id:2,title:"Item 2",items:[{id:21,title:"Item 2.1",items:[{id:211,title:"Item 2.1.1",items:[]},{id:212,title:"Item 2.1.2",items:[]}]},{id:22,title:"Item 2.2",items:[{id:221,title:"Item 2.2.1",items:[]},{id:222,title:"Item 2.2.2",items:[]}]}]},{id:3,title:"Item 3",items:[]},{id:4,title:"Item 4",items:[{id:41,title:"Item 4.1",items:[]}]},{id:5,title:"Item 5",items:[]},{id:6,title:"Item 6",items:[]},{id:7,title:"Item 7",items:[]}],$scope.selectedItem={},$scope.options={},$scope.remove=function(scope){scope.remove()},$scope.toggle=function(scope){scope.toggle()},$scope.newSubItem=function(scope){var nodeData;nodeData=scope.$modelValue,nodeData.items.push({id:10*nodeData.id+nodeData.items.length,title:nodeData.title+"."+(nodeData.items.length+1),items:[]})}}]).controller("MapDemoCtrl",["$scope","$http","$interval",function($scope,$http,$interval){var i,markers;for(markers=[],i=0;8>i;)markers[i]=new google.maps.Marker({title:"Marker: "+i}),i++;$scope.GenerateMapMarkers=function(){var d,lat,lng,loc,numMarkers;for(d=new Date,$scope.date=d.toLocaleString(),numMarkers=Math.floor(4*Math.random())+4,i=0;numMarkers>i;)lat=43.66+Math.random()/100,lng=-79.4103+Math.random()/100,loc=new google.maps.LatLng(lat,lng),markers[i].setPosition(loc),markers[i].setMap($scope.map),i++},$interval($scope.GenerateMapMarkers,2e3)}])}.call(this),function(){"use strict";angular.module("app.ui.directives",[]).directive("uiTime",[function(){return{restrict:"A",link:function(scope,ele){var checkTime,startTime;return startTime=function(){var h,m,s,t,time,today;return today=new Date,h=today.getHours(),m=today.getMinutes(),s=today.getSeconds(),m=checkTime(m),s=checkTime(s),time=h+":"+m+":"+s,ele.html(time),t=setTimeout(startTime,500)},checkTime=function(i){return 10>i&&(i="0"+i),i},startTime()}}}]).directive("uiWeather",[function(){return{restrict:"A",link:function(scope,ele,attrs){var color,icon,skycons;return color=attrs.color,icon=Skycons[attrs.icon],skycons=new Skycons({color:color,resizeClear:!0}),skycons.add(ele[0],icon),skycons.play()}}}])}.call(this),function(){"use strict";angular.module("app.ui.services",[]).factory("logger",[function(){var logIt;return toastr.options={closeButton:!0,positionClass:"toast-bottom-right",timeOut:"3000"},logIt=function(message,type){return toastr[type](message)},{log:function(message){logIt(message,"info")},logWarning:function(message){logIt(message,"warning")},logSuccess:function(message){logIt(message,"success")},logError:function(message){logIt(message,"error")}}}])}.call(this),function(){"use strict";angular.module("app",["ngRoute","ngAnimate","ui.bootstrap","easypiechart","textAngular","ngTagsInput","ngWebsocket","app.ui.ctrls","app.ui.directives","app.ui.services","app.controllers","app.directives","app.form.validation","app.ui.form.ctrls","app.ui.form.directives","app.tables","app.chart.ctrls","app.chart.directives","app.page.ctrls"]).config(["$routeProvider",function($routeProvider){return $routeProvider.when("/",{redirectTo:"/dashboard"}).when("/dashboard",{templateUrl:"views/dashboard.html"}).when("/ui/typography",{templateUrl:"views/ui/typography.html"}).when("/ui/buttons",{templateUrl:"views/ui/buttons.html"}).when("/ui/icons",{templateUrl:"views/ui/icons.html"}).when("/ui/grids",{templateUrl:"views/ui/grids.html"}).when("/ui/widgets",{templateUrl:"views/ui/widgets.html"}).when("/ui/components",{templateUrl:"views/ui/components.html"}).when("/ui/timeline",{templateUrl:"views/ui/timeline.html"}).when("/ui/nested-lists",{templateUrl:"views/ui/nested-lists.html"}).when("/ui/pricing-tables",{templateUrl:"views/ui/pricing-tables.html"}).when("/forms/elements",{templateUrl:"views/forms/elements.html"}).when("/forms/layouts",{templateUrl:"views/forms/layouts.html"}).when("/forms/validation",{templateUrl:"views/forms/validation.html"}).when("/tables/static",{templateUrl:"views/tables/static.html"}).when("/tables/responsive",{templateUrl:"views/tables/responsive.html"}).when("/tables/dynamic",{templateUrl:"views/tables/dynamic.html"}).when("/charts/others",{templateUrl:"views/charts/charts.html"}).when("/charts/morris",{templateUrl:"views/charts/morris.html"}).when("/charts/flot",{templateUrl:"views/charts/flot.html"}).when("/pages/features",{templateUrl:"views/pages/features.html"}).when("/404",{templateUrl:"views/pages/404.html"}).when("/pages/500",{templateUrl:"views/pages/500.html"}).when("/pages/blank",{templateUrl:"views/pages/blank.html"}).otherwise({redirectTo:"/404"})}])}.call(this),function(){angular.module("app.directives",[]).directive("imgHolder",[function(){return{restrict:"A",link:function(scope,ele,attrs){return Holder.run({images:ele[0]})}}}]).directive("customBackground",function(){return{restrict:"A",controller:["$scope","$element","$location",function($scope,$element,$location){var addBg,path;return path=function(){return $location.path()},addBg=function(path){switch($element.removeClass("body-home body-special body-tasks body-lock"),path){case"/":return $element.addClass("body-home");case"/404":case"/pages/500":case"/pages/signin":case"/pages/signup":case"/pages/forgot":return $element.addClass("body-special");case"/pages/lock-screen":return $element.addClass("body-special body-lock");case"/tasks":return $element.addClass("body-tasks")}},addBg($location.path()),$scope.$watch(path,function(newVal,oldVal){return newVal!==oldVal?addBg($location.path()):void 0})}]}}).directive("uiColorSwitch",[function(){return{restrict:"A",link:function(scope,ele,attrs){return ele.find(".color-option").on("click",function(event){var $this,hrefUrl,style;if($this=$(this),hrefUrl=void 0,style=$this.data("style"),"loulou"===style)hrefUrl="styles/main.css",$('link[href^="styles/main"]').attr("href",hrefUrl);else{if(!style)return!1;style="-"+style,hrefUrl="styles/main"+style+".css",$('link[href^="styles/main"]').attr("href",hrefUrl)}return event.preventDefault()})}}}]).directive("toggleMinNav",["$rootScope",function($rootScope){return{restrict:"A",link:function(scope,ele,attrs){var $content,$nav,$window,Timer,app,updateClass;return app=$("#app"),$window=$(window),$nav=$("#nav-container"),$content=$("#content"),ele.on("click",function(e){return app.hasClass("nav-min")?app.removeClass("nav-min"):(app.addClass("nav-min"),$rootScope.$broadcast("minNav:enabled")),e.preventDefault()}),Timer=void 0,updateClass=function(){var width;return width=$window.width(),768>width?app.removeClass("nav-min"):void 0},$window.resize(function(){var t;return clearTimeout(t),t=setTimeout(updateClass,300)})}}}]).directive("collapseNav",[function(){return{restrict:"A",link:function(scope,ele,attrs){var $a,$aRest,$lists,$listsRest,app;return $lists=ele.find("ul").parent("li"),$lists.append('<i class="fa fa-caret-right icon-has-ul"></i>'),$a=$lists.children("a"),$listsRest=ele.children("li").not($lists),$aRest=$listsRest.children("a"),app=$("#app"),$a.on("click",function(event){var $parent,$this;return app.hasClass("nav-min")?!1:($this=$(this),$parent=$this.parent("li"),$lists.not($parent).removeClass("open").find("ul").slideUp(),$parent.toggleClass("open").find("ul").stop().slideToggle(),event.preventDefault())}),$aRest.on("click",function(event){return $lists.removeClass("open").find("ul").slideUp()}),scope.$on("minNav:enabled",function(event){return $lists.removeClass("open").find("ul").slideUp()})}}}]).directive("highlightActive",[function(){return{restrict:"A",controller:["$scope","$element","$attrs","$location",function($scope,$element,$attrs,$location){var highlightActive,links,path;return links=$element.find("a"),path=function(){return $location.path()},highlightActive=function(links,path){return path="#"+path,angular.forEach(links,function(link){var $li,$link,href;return $link=angular.element(link),$li=$link.parent("li"),href=$link.attr("href"),$li.hasClass("active")&&$li.removeClass("active"),0===path.indexOf(href)?$li.addClass("active"):void 0})},highlightActive(links,$location.path()),$scope.$watch(path,function(newVal,oldVal){return newVal!==oldVal?highlightActive(links,$location.path()):void 0})}]}}]).directive("toggleOffCanvas",[function(){return{
restrict:"A",link:function(scope,ele,attrs){return ele.on("click",function(){return $("#app").toggleClass("on-canvas")})}}}]).directive("slimScroll",[function(){return{restrict:"A",link:function(scope,ele,attrs){return ele.slimScroll({height:attrs.scrollHeight||"100%"})}}}]).directive("goBack",[function(){return{restrict:"A",controller:["$scope","$element","$window",function($scope,$element,$window){return $element.on("click",function(){return $window.history.back()})}]}}])}.call(this),function(){"use strict";angular.module("app.controllers",[]).controller("AppCtrl",["$scope","$location",function($scope,$location){return $scope.isSpecificPage=function(){var path;return path=$location.path(),_.contains(["/404","/pages/500","/pages/login","/pages/signin","/pages/signin1","/pages/signin2","/pages/signup","/pages/signup1","/pages/signup2","/pages/forgot","/pages/lock-screen"],path)},$scope.main={brand:"Cronos",name:"Lisa Doe"}}]).controller("DashboardCtrl",["$scope","$websocket",function($scope,$websocket){var ws=$websocket.$new("ws://localhost:9003");$scope.erpData=[],ws.$on("$open",function(){}),ws.$on("$message",function(data){$scope.$apply(function(){"erpData"==data.type&&($scope.erpData.push(data),$scope.erpAmount=$scope.erpData.length)})}),ws.$on("$close",function(){})}])}.call(this);
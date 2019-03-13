<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title></title>

<link href="resources/css/plugins/chosen/chosen.css" rel="stylesheet">
<link href="resources/css/plugins/select2/select2.min.css"
	rel="stylesheet">
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="resources/css/animate.css" rel="stylesheet">
<link href="resources/css/style.css" rel="stylesheet">

<link href="resources/css/dataTables/truck_history_bootstrap.min.css" rel="stylesheet">
<link href="resources/css/dataTables/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="resources/css/dataTables/fixedHeader.bootstrap.min.css" rel="stylesheet">
<link href="resources/css/dataTables/responsive.bootstrap.min.css" rel="stylesheet">
 
 
 
 
 <!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/fixedheader/3.1.5/css/fixedHeader.bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.bootstrap.min.css" rel="stylesheet">
  -->





 
 <link
	href="resources/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css"
	rel="stylesheet">
	<style>
	.text-large {
  font-size: 100%;
}
	</style>
<body class="md-skin">
	<div id="wrapper">
		<!--  <div id="navbar"></div> -->
		<%@include file="navbar.jsp"%>
		<div id="page-wrapper" class="gray-bg">
			<div id="header"></div>
			<div class="row wrapper border-bottom white-bg page-heading">
				<div class="col-lg-10">
					<h2>History</h2>
					<ol class="breadcrumb">
						<li><a href="index.html">Home</a></li>
						<li><a>History</a></li>

					</ol>
				</div>
				<div class="col-lg-2"></div>
			</div>
			<div class="wrapper wrapper-content animated fadeInRight">

				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h5>DistrictWise Truck History Details</h5>
								<!-- <div class="ibox-tools">
									<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
									</a> <a class="dropdown-toggle" data-toggle="dropdown" href="#">
										<i class="fa fa-wrench"></i>
									</a>
									<ul class="dropdown-menu dropdown-user">
										<li><a href="#">Config option 1</a></li>
										<li><a href="#">Config option 2</a></li>
									</ul>
									<a class="close-link"> <i class="fa fa-times"></i>
									</a>
								</div> -->
							</div>
							<div class="ibox-content">
								<form:form action="searchByVehicleNoDistrictName">
									<div calss="row">
										<!-- <div class="col-lg-4">
											<div class="form-group">

												<label class="font-normal ">Select for search</label>
												<div class="input-group text-large">
													<select id="selected" name="selected" class="chosen-select" style="width:200px;">
														<option value="Select">Select</option>
														<option value="Vehicle No">Vehicle No</option>
														<option value="District Name">District Name</option>
													</select>
												</div>
											</div>
										</div>
 -->
										<!-- <div class="col-lg-3">
											<br />
											<div class="form-group">

												<input type="text" class="form-control" id="textId" name="value" value=""
													placeholder="Search By" />
												

											</div>
 

										</div>
										<div class="col-lg-1"><br/>
										<button class="btn btn-sm btn-primary"  type="submit">Search</button>
										</div>

 -->
										<c:choose>
											<c:when test="${showMessage == 'format'}">
												<h4 style="color: red;">select proper format file to
													upload</h4>
											</c:when>
										</c:choose>

										<table id= "dataTable" class="table table-striped table-bordered nowrap">
											<thead>
												<tr>
													<th>Truck No</th>
													<th>District Code</th>
													<th>District Name</th>
													<th>Rated Load</th>
													<th>Normal Load</th>
													<th>Axle/Wheeler Type</th>
													<!--                                      <th>Last_Transaction_Of_Normal_Load</th>
 -->
												</tr>
											</thead>
											<tbody id="searchData">
												<c:choose>
												
													<c:when test="${not empty truckHistoryList}">
														<c:forEach items="${truckHistoryList}" var="historyData"
															varStatus="status">
															<tr>
																<td>${historyData.truckNo}</td>
																<td>${historyData.districtCode}</td>
																<td>${historyData.districtName}</td>
																<td>${historyData.ratedLoad}</td>
																<td>${historyData.normalLoad}</td>
																<td>${historyData.wheelerType}</td>
																
																<%-- 												<td>${historyData.lastTransactionOfNormalLoad}</td>
 --%>
															</tr>
														</c:forEach>
													</c:when>
													<c:when test="${empty truckHistoryList}">
														<tr>
															<td colspan='5'><center>
																	<p style="color: red; font-size: 15px;">Data is
																		not present</p>
																</center></td>
														</tr>
													
													</c:when>
												</c:choose>
											</tbody>

										</table>

									</div>
								</form:form>
							</div>
						</div>

					</div>

				</div>
				<!--  <div id="footer"></div> -->
				<%@include file="footer.html"%>
			</div>
		</div>




		<div class="modal inmodal" id="configurealgo" tabindex="-1"
			role="dialog" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content animated bounceInRight">
					<div class="modal-header">


						<h4 class="modal-title">Configure IntellShip Alogrithm</h4>

					</div>
					<div class="modal-body">
						<table class="table">
							<thead>
								<tr>
									<th></th>
									<th>Yes</th>
									<th>No</th>


								</tr>
							</thead>
							<tbody>
								<tr>
									<td>Enable Plant Re-organization</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio2" id="radio3" value="option1">
											<label for="radio3"> </label>
										</div>
									</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio2" id="radio4" value="option2">
											<label for="radio4"> </label>
										</div>
									</td>

								</tr>
								<tr>
									<td>Include Warehouse Location</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio3" id="radio1" value="option1">
											<label for="radio1"> </label>
										</div>
									</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio3" id="radio2" value="option2">
											<label for="radio2"> </label>
										</div>
									</td>

								</tr>
								<tr>
									<td>Include Shipment to Warehouse</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio3" id="radio01"
												value="option1"> <label for="radio01"> </label>
										</div>
									</td>
									<td>
										<div class="radio">
											<input type="radio" name="radio3" id="radio02"
												value="option2"> <label for="radio02"> </label>
										</div>
									</td>

								</tr>
							</tbody>
						</table>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-white btn-rounded"
							data-dismiss="modal">Close</button>
						<button type="button" class="btn btn-primary btn-rounded">Save
							changes</button>
					</div>
				</div>
			</div>
		</div>
		
	<!-- 	<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap.min.js"></script>
<script src="https://cdn.datatables.net/fixedheader/3.1.5/js/dataTables.fixedHeader.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.3/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.3/js/responsive.bootstrap.min.js"></script> -->

<!-- Data Tables   -->
<script src="resources/js/dataTables/truck_history_jquery-3.3.1.js"></script>
<script src="resources/js/dataTables/jquery.dataTables.min.js"></script>
   <script src="resources/js/dataTables/dataTables.bootstrap.min.js"></script>
   <script src="resources/js/dataTables/dataTables.fixedHeader.min.js"></script>
   <script src="resources/js/dataTables/dataTables.responsive.min.js"></script>
   <script src="resources/js/dataTables/responsive.bootstrap.min.js"></script> 


<script>
		$(document).ready(function() {
		    var table = $('#dataTable').DataTable( {
		        responsive: true
		    } );
		 
		    new $.fn.dataTable.FixedHeader( table );
		} );
</script>


		<!-- Mainly scripts -->
		<!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"
			integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
			crossorigin="anonymous"></script>
		 -->
		
		
		
		
		<script>
		
$(document).ready(function(){
    $("#navbar").load("navbar.html");
	 $("#header").load("header.html");
	 $("#footer").load("footer.html");
});

   	</script>

		
		<!-- <script src="resources/js/jquery-2.1.1_old.js"></script> -->
		<script src="resources/js/bootstrap.min.js"></script>
		<script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
		<script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

   

   
		<!-- Peity -->
		<script src="resources/js/plugins/peity/jquery.peity.min.js"></script>

		<!-- Custom and plugin javascript -->
		<script src="resources/js/inspinia.js"></script>
		<script src="resources/js/plugins/pace/pace.min.js"></script>

		<!-- iCheck -->
		<script src="resources/js/plugins/iCheck/icheck.min.js"></script>

		<!-- Peity -->
		<script src="resources/js/demo/peity-demo.js"></script>

		<!-- Custom and plugin javascript -->
		<script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

		<!-- Chosen -->
		<script src="resources/js/plugins/chosen/chosen.jquery.js"></script>
		<!-- <!-- Chosen -->
		<script src="js/plugins/chosen/chosen.jquery.js"></script>
		-->
		<!-- JSKnob -->
		<script src="resources/js/plugins/jsKnob/jquery.knob.js"></script>

		<!-- Input Mask-->
		<script src="resources/js/plugins/jasny/jasny-bootstrap.min.js"></script>

		<!-- Data picker -->
		<script src="resources/js/plugins/datapicker/bootstrap-datepicker.js"></script>

		<!-- NouSlider -->
		<script src="resources/js/plugins/nouslider/jquery.nouislider.min.js"></script>

		<!-- Switchery -->
		<script src="resources/js/plugins/switchery/switchery.js"></script>

		<!-- IonRangeSlider -->
		<script
			src="resources/js/plugins/ionRangeSlider/ion.rangeSlider.min.js"></script>

		<!-- iCheck -->
		<script src="resources/js/plugins/iCheck/icheck.min.js"></script>

		<!-- MENU -->
		<script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>

		<!-- Color picker -->
		<script
			src="resources/js/plugins/colorpicker/bootstrap-colorpicker.min.js"></script>

		<!-- Clock picker -->
		<script src="resources/js/plugins/clockpicker/clockpicker.js"></script>

		<!-- Image cropper -->
		<script src="resources/js/plugins/cropper/cropper.min.js"></script>

		<!-- Date range use moment.js same as full calendar plugin -->
		<script src="resources/js/plugins/fullcalendar/moment.min.js"></script>

		<!-- Date range picker -->
		<script src="resources/js/plugins/daterangepicker/daterangepicker.js"></script>

		<!-- Select2 -->
		<script src="resources/js/plugins/select2/select2.full.min.js"></script>

		<!-- TouchSpin -->
		<script
			src="resources/js/plugins/touchspin/jquery.bootstrap-touchspin.min.js"></script>

		<script>
        $(document).ready(function(){

            var $image = $(".image-crop > img")
            $($image).cropper({
                aspectRatio: 1.618,
                preview: ".img-preview",
                done: function(data) {
                    // Output the result data for cropping image.
                }
            });

            var $inputImage = $("#inputImage");
            if (window.FileReader) {
                $inputImage.change(function() {
                    var fileReader = new FileReader(),
                            files = this.files,
                            file;

                    if (!files.length) {
                        return;
                    }

                    file = files[0];

                    if (/^image\/\w+$/.test(file.type)) {
                        fileReader.readAsDataURL(file);
                        fileReader.onload = function () {
                            $inputImage.val("");
                            $image.cropper("reset", true).cropper("replace", this.result);
                        };
                    } else {
                        showMessage("Please choose an image file.");
                    }
                });
            } else {
                $inputImage.addClass("hide");
            }

            $("#download").click(function() {
                window.open($image.cropper("getDataURL"));
            });

            $("#zoomIn").click(function() {
                $image.cropper("zoom", 0.1);
            });

            $("#zoomOut").click(function() {
                $image.cropper("zoom", -0.1);
            });

            $("#rotateLeft").click(function() {
                $image.cropper("rotate", 45);
            });

            $("#rotateRight").click(function() {
                $image.cropper("rotate", -45);
            });

            $("#setDrag").click(function() {
                $image.cropper("setDragMode", "crop");
            });

            $('#data_1 .input-group.date').datepicker({
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                calendarWeeks: true,
                autoclose: true
            });

            $('#data_2 .input-group.date').datepicker({
                startView: 1,
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                format: "dd/mm/yyyy"
            });

            $('#data_3 .input-group.date').datepicker({
                startView: 2,
                todayBtn: "linked",
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true
            });

            $('#data_4 .input-group.date').datepicker({
                minViewMode: 1,
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
                todayHighlight: true
            });

            $('#data_5 .input-daterange').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true
            });

            var elem = document.querySelector('.js-switch');
            var switchery = new Switchery(elem, { color: '#1AB394' });

            var elem_2 = document.querySelector('.js-switch_2');
            var switchery_2 = new Switchery(elem_2, { color: '#ED5565' });

            var elem_3 = document.querySelector('.js-switch_3');
            var switchery_3 = new Switchery(elem_3, { color: '#1AB394' });

            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green'
            });

            $('.demo1').colorpicker();

            var divStyle = $('.back-change')[0].style;
            $('#demo_apidemo').colorpicker({
                color: divStyle.backgroundColor
            }).on('changeColor', function(ev) {
                        divStyle.backgroundColor = ev.color.toHex();
                    });

            $('.clockpicker').clockpicker();

            $('input[name="daterange"]').daterangepicker();

            $('#reportrange span').html(moment().subtract(29, 'days').format('MMMM D, YYYY') + ' - ' + moment().format('MMMM D, YYYY'));

            $('#reportrange').daterangepicker({
                format: 'MM/DD/YYYY',
                startDate: moment().subtract(29, 'days'),
                endDate: moment(),
                minDate: '01/01/2012',
                maxDate: '12/31/2015',
                dateLimit: { days: 60 },
                showDropdowns: true,
                showWeekNumbers: true,
                timePicker: false,
                timePickerIncrement: 1,
                timePicker12Hour: true,
                ranges: {
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                    'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                    'This Month': [moment().startOf('month'), moment().endOf('month')],
                    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                },
                opens: 'right',
                drops: 'down',
                buttonClasses: ['btn', 'btn-sm'],
                applyClass: 'btn-primary',
                cancelClass: 'btn-default',
                separator: ' to ',
                locale: {
                    applyLabel: 'Submit',
                    cancelLabel: 'Cancel',
                    fromLabel: 'From',
                    toLabel: 'To',
                    customRangeLabel: 'Custom',
                    daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
                    monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                    firstDay: 1
                }
            }, function(start, end, label) {
                console.log(start.toISOString(), end.toISOString(), label);
                $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
            });

            $(".select2_demo_1").select2();
            $(".select2_demo_2").select2();
            $(".select2_demo_3").select2({
                placeholder: "Select a state",
                allowClear: true
            });


            $(".touchspin1").TouchSpin({
                buttondown_class: 'btn btn-white',
                buttonup_class: 'btn btn-white'
            });

            $(".touchspin2").TouchSpin({
                min: 0,
                max: 100,
                step: 0.1,
                decimals: 2,
                boostat: 5,
                maxboostedstep: 10,
                postfix: '%',
                buttondown_class: 'btn btn-white',
                buttonup_class: 'btn btn-white'
            });

            $(".touchspin3").TouchSpin({
                verticalbuttons: true,
                buttondown_class: 'btn btn-white',
                buttonup_class: 'btn btn-white'
            });


        });
        var config = {
                '.chosen-select'           : {},
                '.chosen-select-deselect'  : {allow_single_deselect:true},
                '.chosen-select-no-single' : {disable_search_threshold:10},
                '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
                '.chosen-select-width'     : {width:"95%"}
                }
            for (var selector in config) {
                $(selector).chosen(config[selector]);
            }

        $("#ionrange_1").ionRangeSlider({
            min: 0,
            max: 5000,
            type: 'double',
            prefix: "$",
            maxPostfix: "+",
            prettify: false,
            hasGrid: true
        });

        $("#ionrange_2").ionRangeSlider({
            min: 0,
            max: 10,
            type: 'single',
            step: 0.1,
            postfix: " carats",
            prettify: false,
            hasGrid: true
        });

        $("#ionrange_3").ionRangeSlider({
            min: -50,
            max: 50,
            from: 0,
            postfix: "°",
            prettify: false,
            hasGrid: true
        });

        $("#ionrange_4").ionRangeSlider({
            values: [
                "January", "February", "March",
                "April", "May", "June",
                "July", "August", "September",
                "October", "November", "December"
            ],
            type: 'single',
            hasGrid: true
        });

        $("#ionrange_5").ionRangeSlider({
            min: 10000,
            max: 100000,
            step: 100,
            postfix: " km",
            from: 55000,
            hideMinMax: true,
            hideFromTo: false
        });

        $(".dial").knob();

        $("#basic_slider").noUiSlider({
            start: 40,
            behaviour: 'tap',
            connect: 'upper',
            range: {
                'min':  20,
                'max':  80
            }
        });

        $("#range_slider").noUiSlider({
            start: [ 40, 60 ],
            behaviour: 'drag',
            connect: true,
            range: {
                'min':  20,
                'max':  80
            }
        });

        $("#drag-fixed").noUiSlider({
            start: [ 40, 60 ],
            behaviour: 'drag-fixed',
            connect: true,
            range: {
                'min':  20,
                'max':  80
            }
        });


    </script>

		<script>
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
        });
    </script>
</body>

</html>

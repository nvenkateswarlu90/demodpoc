<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page session="false" %>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title></title>

<link href="resources/css/plugins/chosen/chosen.css" rel="stylesheet">
   <link href="resources/css/plugins/select2/select2.min.css" rel="stylesheet">
   <link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="resources/css/animate.css" rel="stylesheet">
    <link href="resources/css/style.css" rel="stylesheet">
	 <link href="resources/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
</head>

<body class="md-skin">
<div id="wrapper">
  <!--  <div id="navbar"></div> -->
 <%@include file="navbar.jsp" %>
        <div id="page-wrapper" class="gray-bg">
       <div id="header"></div>
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-lg-10">
                    <h2>History</h2>
                    <ol class="breadcrumb">
                        <li>
                            <a href="index.html">Home</a>
                        </li>
                        <li>
                            <a>History</a>
                        </li>
                      
                    </ol>
                </div>
                <div class="col-lg-2">

                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight">
      
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>History</h5>
                       
                        </div>
                        <div class="ibox-content">
							<!-- <div calss="row">
								  <div class="col-lg-6">
								       <div class="form-group">
                <label class="font-noraml">Select</label>
                <div class="input-group">
                <select data-placeholder="" class="chosen-select" style="width:300px;">
                <option value="">Select</option>
                <option value="1">Date</option>
                <option value="2">Plant</option>
                <option value="3">Material</option>
              
                </select>
                </div>
                </div>	
								</div>
								  <div class="col-lg-4">
								     
                             <div class="form-group" id="data_1">
                                <label class="font-noraml">Select Date</label>
                                <div class="input-group date">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control" value="03/04/2014">
                                </div>
                            </div>

								</div>
								  <div class="col-lg-2">
								     
                             <a class="btn btn-success btn-rounded" href="#">Search</a>
								</div>
							</div> -->
                           <table class="table table-bordered">
                                <thead>
                                <tr>
 											<th>ShippingId</th>
											<th>Truck No.</th>
											<th>Axle/Wheeler Type</th>
											<th>District Name</th>
											<th>Load Type</th>
											<th>Material Type</th>
											<th>Total Order</th>
											<th>Total Order Quantity</th>
											<th>Truck Capacity</th>
											<th>Pending Quantity</th>
											<th>Plant</th>
											<th>Total Kilometers</th>
										</tr>
                                </thead>
                                <tbody>
										<c:forEach items="${ordersHistory}"
											var="shippingGroupDetails" varStatus="status">
											<tr>
											 <td>${shippingGroupDetails.shippingOrderId}</td>
												<td>${shippingGroupDetails.truckNo}</td>
												<td>${shippingGroupDetails.wheelerType}</td>
												<td>${shippingGroupDetails.districtName}</td>
												<td>${shippingGroupDetails.loadType}</td>
												<td>${shippingGroupDetails.materialType}</td>
												<td><button class="btn btn-success btn-circle" type="button" 
														onclick="getOrderDetailsByMaterial('${shippingGroupDetails.truckNo}')">${shippingGroupDetails.totalOrders}
													</button></td> 
												<td>${shippingGroupDetails.totalOrderQuantity}</td>
												<td>${shippingGroupDetails.truckCapacity}</td>
												<td>${shippingGroupDetails.pendingQuantity}</td>
												<td>${shippingGroupDetails.plant}</td>
												<td>${shippingGroupDetails.totalKilometers}</td>
												
											</tr>
										</c:forEach>
									</tbody>

                            </table>


                        </div>
                    </div>
                </div>
        
            </div>
       
        </div>
      <!--  <div id="footer"></div> -->
 <%@include file="footer.html" %>
        </div>
        </div>

<div class="modal" id="orderConfigurealgo" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Orders Information</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>Delivery</th>
              <th>Reference Document</th>
              <th>Sold to Party</th>
              <th>Name of sold to Party</th>
              <th>Name of the ship tp party</th>
              <th>Material</th>
              <th>Actual delivery Qty</th>
              <th>Route Description</th>
              <th>District Name</th>
              <th>Plant</th>
              <th>Route</th>
              <th>Forwarding Agent Name</th>
              <th>Distribution Channel</th>
              <th>Deliv.Date(From/To)</th>
              <th>Delivery Type</th>
              <th>Shipping Point/Receiving Pt</th>
              <th>District Code</th>
              <th>Ship to Party</th>
              <th>Shio to Long</th>
              <th>Ship to Latt</th>
            </tr>
          </thead>
          <tbody id="orderData1">
																				
				</tbody>
        </table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
        <!-- <button type="button" class="btn btn-primary btn-rounded">Save changes</button> -->
      </div>
    </div>
  </div>
</div>
  <!-- Mainly scripts -->
	<!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"
			  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
			  crossorigin="anonymous"></script>  -->
		<script>
		   $("#orderConfigurealgo").modal()
$(document).ready(function(){
    $("#navbar").load("navbar.html");
	 $("#header").load("header.html");
	 $("#footer").load("footer.html");
});
	</script>
 
    <!-- <script src="resources/js/jquery-2.1.1_old.js"></script> -->
    <script src="resources/js/jquery-2.1.1_old.js"></script> 
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
    <script src="js/plugins/chosen/chosen.jquery.js"></script> -->
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
     <script src="resources/js/plugins/ionRangeSlider/ion.rangeSlider.min.js"></script>

    <!-- iCheck -->
    <script src="resources/js/plugins/iCheck/icheck.min.js"></script>

    <!-- MENU -->
    <script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>

    <!-- Color picker -->
     <script src="resources/js/plugins/colorpicker/bootstrap-colorpicker.min.js"></script> 

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
    <script src="resources/js/plugins/touchspin/jquery.bootstrap-touchspin.min.js"></script>


    <script>
    function getOrderDetailsByMaterial(truckNo){
  	  $.ajax({
			type : "GET",
			url : "getGroupOrderByTruck",
			data : "truckNo=" + truckNo,
			success : function(response) {
				$("#orderData1").empty();
				$.each(response, function(i, value) {
					 $("#orderData1").append("<tr><td>" + value.delivery + "</td><td>" + value.deference_document + "</td><td>" + value.sold_to_party + 
							"</td><td>" + value.name_of_sold_to_party + "</td><td>"  + value.name_of_the_ship_to_party + "</td><td>" + 
							 value.material + "</td><td>" + value.actual_delivery_qty + "</td><td>" + 
							  value.route_description + "</td><td>" + value.district_name + "</td><td>"  
							 + value.plant + "</td><td>"+ value.route + "</td><td>" + 
							 value.forwarding_agent_name + "</td><td>" + value.distribution_channel + "</td><td>" + value.deliv_date + "</td><td>" +
							 value.delivery_type + "</td><td>" +value.shipping_Point + "</td><td>" +value.district_code + "</td><td>" +
value.ship_to_party + "</td><td>"+ value.ship_to_long + "</td><td>" + value.ship_to_latt +"</td></tr>"); 
				});
				$("#orderConfigurealgo").modal();
			},
			error : function(e) {
				 alert('Error: ' + e); 
			}
		});

    }
    
    $(document).ready(function(){
        $('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });
    });
    
    </script>

</body>

</html>

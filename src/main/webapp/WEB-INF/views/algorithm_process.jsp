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

<link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="resources/css/animate.css" rel="stylesheet">
    <link href="resources/css/style.css" rel="stylesheet">
	 <link href="resources/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

</head>

<body class="md-skin">
<div id="wrapper">
    <!-- <div id="navbar"></div> -->
      <%@include file="navbar.jsp" %>
  <div id="page-wrapper" class="gray-bg">
   <div id="header"></div>
    <div class="row wrapper border-bottom white-bg page-heading">
      <div class="col-lg-10">
        <h2>Algorithm Process</h2>
        <ol class="breadcrumb">
          <li> <a href="index.html">Home</a> </li>
          <li> <a>Algorithm Process</a> </li>
        </ol>
      </div>
      <div class="col-lg-2"> </div>
    </div>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="row">
        <div class="col-lg-12">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Algorithm Process</h5>
            <!--   <div class="ibox-tools"> <a class="collapse-link"> <i class="fa fa-chevron-up"></i> </a> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i class="fa fa-wrench"></i> </a>
                <ul class="dropdown-menu dropdown-user">
                  <li><a href="#">Config option 1</a> </li>
                  <li><a href="#">Config option 2</a> </li>
                </ul>
                <a class="close-link"> <i class="fa fa-times"></i> </a> </div> -->
            </div>
            <div class="ibox-content">
              <div calss="row">
                <p class="pull-right">
                
              <!-- <a class="btn btn-primary btn-rounded" data-toggle="modal" data-target="#configurealgo">Configure Algo</a> </p> -->
                
<%--                   <button class="btn btn-warning btn-circle" type="button"><i class="fa fa-refresh"></i> </button>
                  <a class="btn btn-success btn-rounded" href="#">Process New Order Batch</a> <a class="btn btn-primary btn-rounded" href="<c:url value='getShippingOrderHistory' />">History</a> </p>
 --%>              </div>
              <table class="table table-bordered" id="dtHorizontalVerticalExample">
                                <thead>
                                <tr>
                                    <th>Delivery OrderNo</th>
                                    <th>Sold-to party</th>
                                    <th>Name of sold-to party</th>
                                    <th>Name of the ship-to party</th>
									<th>Material</th>
                                    <th>Actual delivery qty</th>
                                    <th>Route Description</th>
                                    <th>District Name</th>
									<th>Plant</th>
                                    <th>Route</th>
                                    <th>Forwarding agent name</th>
                                    <th>Distribution Channel</th>
                                    <th>Delivery. date(From/to)</th>
                                    <th>Delivery Type</th>
                                    <th>Shipping Point/Receiving Pt</th>
                                    <th>District Code</th>
                                    <th>Ship-to party</th>
                                    <th>Ship to Long.</th>
                                    <th>Ship to Latt</th>
                                </tr>
                                </thead>
									<tbody>
										<c:forEach items="${shippingaOrderList}"
											var="shippingDetails" varStatus="status">
											<tr>
												<td>${shippingDetails.delivery}</td>
												<td>${shippingDetails.sold_to_party}</td>
												<td>${shippingDetails.name_of_sold_to_party}</td>
												<td>${shippingDetails.name_of_the_ship_to_party}</td>
												<td>${shippingDetails.material}</td>
												<td>${shippingDetails.actual_delivery_qty}</td>
												<td>${purchaseOrder.route_description}</td>
												<td>${shippingDetails.district_name}</td>
												<td>${shippingDetails.plant}</td>
												<td>${shippingDetails.route}</td>
												<td>${shippingDetails.forwarding_agent_name}</td>
												<td>${shippingDetails.distribution_channel}</td>
												<td>${shippingDetails.deliv_date}</td>
												<td>${shippingDetails.delivery_type}</td>
												<td>${shippingDetails.shipping_Point}</td>
												<td>${shippingDetails.district_code}</td>
												<td>${shippingDetails.ship_to_party}</td>
												<td>${shippingDetails.ship_to_long}</td>
												<td>${shippingDetails.ship_to_latt}</td>
												<%-- <td style="width: 50%;">${purchaseOrder.termsAndConditions}</td> --%>
											</tr>
										</c:forEach>
									</tbody>
								</table>

            </div>
          </div>
        </div>
      </div>
    </div>
    <%@include file="footer.html" %>
     <!-- <div id="footer"></div> -->
  </div>
</div>
<div class="modal inmodal" id="configurealgo" tabindex="-1" role="dialog" aria-hidden="true">
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
                                    <th>Maximum Load</th>
                                    <th>Normal</th>
                                    
                                   
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Enable Truck Load Type(Maximum/Normal)</td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio2" id="radio3" value="option1">
                                                    <label for="radio3">
                                                       
                                                    </label>
                                                </div></td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio2" id="radio4" value="option2">
                                                    <label for="radio4">
                                                      
                                                    </label>
                                                </div></td>
                                  
                                </tr>
                                <!-- <tr>
                                   <td>Include Warehouse Location</td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio3" id="radio1" value="option1">
                                                    <label for="radio1">
                                                       
                                                    </label>
                                                </div></td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio3" id="radio2" value="option2">
                                                    <label for="radio2">
                                                      
                                                    </label>
                                                </div></td>
                                  
                                </tr>
                                <tr>
                                    <td>Include Shipment to Warehouse</td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio3" id="radio01" value="option1">
                                                    <label for="radio01">
                                                       
                                                    </label>
                                                </div></td>
                                    <td> <div class="radio">
                                                    <input type="radio" name="radio3" id="radio02" value="option2">
                                                    <label for="radio02">
                                                      
                                                    </label>
                                                </div></td>
                                   
                                </tr> -->
                                </tbody>
                            </table>
                                                 
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
                                            <button type="button" class="btn btn-primary btn-rounded">Save changes</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

<!-- Mainly scripts --> 
 <!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"
			  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
			  crossorigin="anonymous"></script>  -->
		<script>
		
$(document).ready(function(){
    $("#navbar").load("src/main/webapp/WEB-INF/views/navbar.jsp");
	 $("#header").load("src/main/webapp/WEB-INF/views/header.html");
	 $("#footer").load("src/main/webapp/WEB-INF/views/footer.html");
});
	</script>
<!-- Mainly scripts -->
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
<script>
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
        });
        
        $(document).ready(function () {
        	$('#dtHorizontalVerticalExample').DataTable({
        	"scrollX": true,
        	"scrollY": 200,
        	});
        	$('.dataTables_length').addClass('bs-select');
        	});
    </script>
</body>
</html>

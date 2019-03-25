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

  <%@include file="navbar.jsp" %>
 <link href="resources/css/bootstrap.min.css" rel="stylesheet">
  <link href="resources/css/plugins/chosen/chosen.css" rel="stylesheet">
    <link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="resources/css/animate.css" rel="stylesheet">
    <link href="resources/css/style.css" rel="stylesheet">
    <link href="resources/css/icon.css" rel="stylesheet">
    <link href="resources/css/select2.min.css" rel="stylesheet">
	 <link href="resources/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
<style>
#map {
	width: 100%;
	height: 430px;
	margin-left: auto;
	margin-right: auto;
}
}
</style>
</head>

<body class="md-skin">
<div id="wrapper">
 
  <div id="page-wrapper" class="gray-bg">
  


    <div class="row wrapper border-bottom white-bg page-heading">
      <div class="col-lg-10">
        <h2>Channel Configuration</h2>
        <ol class="breadcrumb">
          <li> <a href="index.html">Home</a> </li>
          <li> <a>Channel Configuration</a> </li>
        </ol>
      </div>
      <div class="col-lg-2"> </div>
    </div>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="row">
        <div class="col-lg-12">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>Channel Configuration</h5>
              <!-- <div class="ibox-tools"> <a class="collapse-link"> <i class="fa fa-chevron-up"></i> </a> <a class="dropdown-toggle" data-toggle="dropdown" href="#"> <i class="fa fa-wrench"></i> </a>
                <ul class="dropdown-menu dropdown-user">
                  <li><a href="#">Config option 1</a> </li>
                  <li><a href="#">Config option 2</a> </li>
                </ul>
                <a class="close-link"> <i class="fa fa-times"></i> </a> </div> -->
            </div>
            <div class="ibox-content">
				<button class="btn btn-success btn-rounded pull-right" data-toggle="modal" data-target="#confadd" type="button">Add</button> 
              <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th>Sequence</th>
                    <th>Channel</th>
                    <th>SKU Type</th>
                    <th>Action</th>
                   
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${channelConfigList}" var="channelConfig" varStatus="status">
                                    <tr>
                                    	<td style="vertical-align:middle">${channelConfig.sequence}</td>
                                    	<td style="vertical-align:middle">${channelConfig.channel}</td>
                                    	<td style="vertical-align:middle">${channelConfig.skuType}</td>
                                    	 <td>
                                    	 <button class="btn btn-success "
														onclick="editChannelConfiguration('${channelConfig.id}','${channelConfig.sequence}','${channelConfig.channel}','${channelConfig.skuType}')">Edit</button>
                                    	<a href="<c:url value='/deleteChannelConfiguration/${channelConfig.id}' />" class="btn btn-danger custom-width">Delete</a>
												</td>
                                   	 
                                    </tr>                                  
                                    </c:forEach>
                </tbody>
              </table>

				 <div class="col-lg-12">
				<div class="row">

										<div class="form-group">
											<label class="font-noraml">Channel Sequence</label>
											<div class="input-group">
												<select class="chosen-select" multiple="multiple" id="sequenceId" name="sequenceId[]" placeholder="select" style="width: 450px; height: 450px; display: none;" tabindex="4">
													
													<c:forEach items="${sequenceList}" var="sequence"
														varStatus="status">
														<option value="${sequence}">${sequence}</option>
													</c:forEach>
												</select>
												<input type="hidden" name="multiple_value" id="multiple_value"  />
												<button class="btn btn-success" style="margin:5px"
														onclick="saveSequence()">Submit</button>
											</div>
										</div>


										<a href="<c:url value='/showPendingOrders'/>" class="btn btn-success pull-right btn-rounded" type="button">Back </a> </div>
					
			  </div>
			  </div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer">
      <div class="pull-right"> 10GB of <strong>250GB</strong> Free. </div>
      <div> <strong>Copyright</strong> A4Technology Solution Pvt. Ltd &copy; 2017-2018 </div>
    </div>
  </div>
</div>
<div class="modal" id="confedit" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Edit Channel Configuration</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
        <form id="formId" class="form-horizontal m-l-md" action="editChannelConfiguration" method="post">
        <table class="table table-bordered">
          <thead>
                 <tr>
                    <th>Sequence</th>
                    <th>Channel</th>
                    <th>SKU Type</th>
                   
                   
                  </tr>
          </thead>
          <tbody>
            <tr>
                 <input type="hidden" id="id" name="id">
				<td><input type="text" id="sequence" name="sequence" class="form-control" readonly="readonly"></td>
				<td><input type="text" id="channel" name="channel" class="form-control"></td>
				<td><input type="text" id="skuType" name="skuType" class="form-control"></td>
            </tr>
         
          </tbody>
        </table>
     
      <div class="modal-footer">
        <button type="button" class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-primary btn-rounded">Save changes</button>
      </div>
      </form>
       </div>
    </div>
  </div>
</div>
<div class="modal" id="confadd" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Add Channel Configuration</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
      <form id="formId" class="form-horizontal m-l-md" action="channelConfiguration" method="post">
         <table class="table table-bordered">
          <thead>
                 <tr>
                    <th>Sequence</th>
                    <th>Channel</th>
                    <th>SKU Type</th>
                   
                   
                  </tr>
          </thead>
          <tbody>
            <tr>
				    <td> <input type="text" name="sequence" class="form-control" ></td>
					  <td> <input type="text" name="channel"class="form-control" ></td>
					   <td> <input type="text" name="skuType" class="form-control" ></td>
            </tr>
         
          </tbody>
        </table>
     
      <div class="modal-footer">
        <button type="button" class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-primary btn-rounded">Save changes</button>
        
      </div>
      </form>
       </div>
    </div>
  </div>
</div>

<!-- Mainly scripts --> 
    <script src="resources/js/jquery-2.1.1_old.js"></script> 
   
    
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="resources/js/bootstable.js"></script>
    <script src="resources/js/select2.min.js"></script>
   <!--  <script>
 $('table').SetEditable();
</script> -->
    <!-- Peity -->
    <script src="resources/js/plugins/peity/jquery.peity.min.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="resources/js/inspinia.js"></script>
    <script src="resources/js/plugins/pace/pace.min.js"></script>
<script src="resources/js/plugins/chosen/chosen.jquery.js"></script>
    <!-- iCheck -->
    <script src="resources/js/plugins/iCheck/icheck.min.js"></script>

    <!-- Peity -->
    <script src="resources/js/demo/peity-demo.js"></script>
<!-- <script src="js/jquery-2.1.1.js"></script> 
<script src="js/bootstrap.min.js"></script> 
<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script> 
<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script> 

	Chosen
    <script src="js/plugins/chosen/chosen.jquery.js"></script>

Peity 
<script src="js/plugins/peity/jquery.peity.min.js"></script> 

Custom and plugin javascript 
<script src="js/inspinia.js"></script> 
<script src="js/plugins/pace/pace.min.js"></script> 

iCheck 
<script src="js/plugins/iCheck/icheck.min.js"></script> 

Peity 
<script src="js/demo/peity-demo.js"></script> 
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&amp;libraries=geometry"></script> 
 --><script>
		
        $(document).ready(function(){
            $('.i-checks').iCheck({
                checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-green',
            });
        });
	$(".chosen-select").chosen();
	
	 function editChannelConfiguration(id,sequence,channel,skuType){
        	 $("#id").val(id);   
        	$("#sequence").val(sequence);
        	    $("#channel").val(channel);
        	    $("#skuType").val(skuType);
        	  
				var myEditModal = $("#confedit");
        	   myEditModal.modal({ show: true });
        }
	 
	 function saveSequence(){
		 var sequnce = $('#sequenceId').val();
		 alert(sequnce)
		 
		 $.ajax({
				type : "GET",
				url : "orderSequence",
				data : "sequence=" + sequnce,
				success : function(response) {
					alert(response)
				  if(response == "success"){
					  alert('order sequence saved successfully')
				  } else {
					  
				  }
					
				},
				error : function(e) {
					 alert('Error: ' + e); 
				}
			});
		
	 }
	 
		  $("select").select2({
			  tags: true,
			  placeholder: "Please select Sequence"
			}); 
 
			 $("select").on("select2:select", function (evt) {
			  var element = evt.params.data.element;
			  var $element = $(element);
			  
			  $element.detach();
			  $(this).append($element);
			  $(this).trigger("change");
			});  
	 
    </script>
</body>
</html>

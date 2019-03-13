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
    <link href="resources/css/icon.css" rel="stylesheet">
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
                    <h2>District Clubbing Order ByPass Configuration</h2>
                </div>
                <div class="col-lg-2">
                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight">
      
            <div class="row">
        <div class="col-lg-12">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>District Clubbing Order ByPass</h5>
            </div>
            <div class="ibox-content">
				  <button type="button" class="btn btn-info add-new pull-right btn-rounded"  data-toggle="modal" data-target="#create"><i class="fa fa-plus"></i> Add New District</button>
              <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th>District Name</th>
                    <th>District Code</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                   <!--  <th>Action</th> -->
                    
                   
                  </tr>
                </thead>
                <tbody>
                 <c:forEach items="${distByPass}" var="districtByPass" varStatus="status">
                                    <tr>
                                    	<td style="vertical-align:middle">${districtByPass.districtName}</td>
                                    	<td style="vertical-align:middle">${districtByPass.districtCode}</td>
                                    	<td style="vertical-align:middle">${districtByPass.startDate}</td>
                                    	<td style="vertical-align:middle">${districtByPass.endDate}</td>
                                    	<%-- <td style="width:17%">
                                    	<div class="btn-group" role="group" aria-label="Basic example">
             <button type="button" class="btn btn-warning btn-rounded" onclick="editDistrictDetails()"> <i class="fa fa-pencil"></i> Edit</button>
             <button type="button" class="btn btn-danger btn-rounded" onclick="removeDistrictName()"> <i class="fa fa-trash"></i> Remove</button>
             <a href="<c:url value='/delete-user-${districtByPass.districtName}' />" class="btn btn-danger custom-width">delete</a>
</div> 
                                    	
                                    	</td> --%>
                                    	<%-- <td>
                                    	<a href="<c:url value='/deleteDistrict/${districtByPass.id}' />" class="btn btn-danger custom-width">Delete</a>
                                    	<a href="<c:url value='/deleteDistrict1/${districtByPass.id}' />" class="btn btn-success  custom-width"  data-toggle="modal" data-districtData="${districtByPass}" data-target="#edit">Edit</a>
                                    	</td>
 --%>                                    	 
                                    </tr>                                  
                                    </c:forEach>
                </tbody>
              </table>
				
           </div>
          </div>
        </div>
      </div>
                  
        </div>
      <!-- <div id="footer"></div> -->
      <%@include file="footer.html" %>

        </div>
        </div>
<%-- <form:form id="formId" class="form-horizontal m-l-md"  action="distClubOrdByPassConfig" method="post"> --%>
<div class="modal" id="create" tabindex="-1" data-backdrop="static" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Add District Clubbing Order By Pass</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
      
      <form id="formId" class="form-horizontal m-l-md" action="distClubOrdByPassConfig" method="post">
       <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th>District Name</th>
                    <th>District Code</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                   
                  </tr>
                </thead>
                <tbody>
                  <tr>
                 <%--  <td> <form:input path="districtName" class="form-control"/> </td>
                 <td> <form:input path="districtCode" class="form-control"/></td>
                  <td><form:input path="startDate" class="datepicker"/></td>
                 <td> <form:input path="endDate" class="datepicker"/></td> --%>
                  
                    <td> <input type="text" name="districtName" class="form-control" value="${districtByPass.districtName}"></td>
					  <td> <input type="text" name="districtCode"class="form-control"></td>
					   <td> <input type="date" name="startDate" class="form-control"></td>
                    <td> <input type="date" name="endDate" class="form-control"></td>
                   
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

<div class="modal" id="edit" tabindex="-1" data-backdrop="static" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Edit District Clubbing Order By Pass</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
      
      <form id="formId" class="form-horizontal m-l-md" action="distClubOrdByPassConfig" method="post">
       <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th>District Name</th>
                    <th>District Code</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                   
                  </tr>
                </thead>
                <tbody>
                  <tr>
                 <%--  <td> <form:input path="districtName" class="form-control"/> </td>
                 <td> <form:input path="districtCode" class="form-control"/></td>
                  <td><form:input path="startDate" class="datepicker"/></td>
                 <td> <form:input path="endDate" class="datepicker"/></td> --%>
                  
                    <td> <input type="text" name="districtName" class="form-control"></td>
					  <td> <input type="text" name="districtCode"class="form-control"></td>
					   <td> <input type="date" name="startDate" class="form-control"></td>
                    <td> <input type="date" name="endDate" class="form-control"></td>
                   
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
<%-- </form:form> --%>

    <!-- Mainly scripts -->
   <!-- <script src="https://code.jquery.com/jquery-3.3.1.min.js"
			  integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
			  crossorigin="anonymous"></script> -->
		<script>
		
$(document).ready(function(){
    $("#navbar").load("navbar.jsp");
	 $("#header").load("header.html");
	 $("#footer").load("footer.html");
});
	</script>
    <script src="resources/js/jquery-2.1.1_old.js"></script> 
   
    
    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="resources/js/bootstable.js"></script>
   <!--  <script>
 $('table').SetEditable();
</script> -->
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
        
        function removeDistrictName(){
        	alert('remove');
        }
        
        function editDistrictDetails(){
        	alert('edit');
        }
    </script>

</body>

</html>

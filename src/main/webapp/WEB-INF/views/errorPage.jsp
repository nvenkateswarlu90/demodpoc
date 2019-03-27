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
                    
                </div>
                <div class="col-lg-2">

                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight">
      
            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>Error message</h5>
                       
                        </div>
                        <div class="ibox-content">
   <h4 style="color:red">Please choose channel sequence in Channel Configuration  </h4>
                        </div>
                    </div>
                </div>
        
            </div>
       
        </div>
      <!-- <div id="footer"></div> -->
      <%@include file="footer.html" %>

        </div>
        </div>
        
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
    </script>

</body>

</html>

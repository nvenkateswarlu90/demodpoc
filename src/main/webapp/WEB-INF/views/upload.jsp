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
<title>Upload Trucks Info</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="resources/css/animate.css" rel="stylesheet">
    <link href="resources/css/style.css" rel="stylesheet">
    <link href="resources/css/plugins/dropzone/dropzone.css" rel="stylesheet">
	 <link href="resources/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

</head>
<style>
h4 {
 font-size:20px;
 font-style: italic;
}
</style>
<body class="md-skin">
<div id="wrapper">
	   <%@include file="navbar.jsp" %>
  <div id="page-wrapper" class="gray-bg">
	    <div id="header"></div>
     <div class="wrapper wrapper-content animated fadeIn">
            <div class="row">
                <div class="col-lg-12">
                <div class="ibox float-e-margins">
                   <!--  <div class="ibox-title">             
                    </div> -->
              
                    <div class="ibox-content">
						<center>
					 <form:form method = "POST" modelAttribute = "fileUpload"
         enctype = "multipart/form-data" style="margin: 10% 0px;">
				 <h2>Please select a file to upload  </h2> 
         <input class="btn btn-primary btn-rounded" type = "file" name = "file" />
         <input class="btn btn-success btn-rounded" type = "submit" value = "upload" />
         
           <c:choose>
             	<c:when test="${showMessage == 'success'}">
             	<h4 style="color: green;">Truck details data has been saved successfully</h4> 
             	</c:when>
             	<c:when test="${showMessage == 'select'}">
             	<h4 style="color: red;">select proper file to upload</h4> 
             	</c:when>
             	<c:when test="${showMessage == 'format'}">
             	<h4 style="color: red;">select proper format file to upload</h4> 
             	</c:when>
             	<c:when test="${pendingOrderMessage == 'success'}">
             	<h4 style="color: green;">Pending Orders data has been saved successfully</h4> 
             	</c:when>
             	<c:when test="${truckHistoryMessage == 'success'}">
             	<h4 style="color: green;">Truck History data has been saved successfully</h4> 
             	</c:when>
             		<c:when test="${normalLoad == 'success'}">
             	<h4 style="color: green;">Normal load configuration data has been saved successfully</h4> 
             	</c:when>
      
             	</c:choose>
        
      </form:form>
						</center>
					</div>    
                    
				
                </div>
            </div>
				
            </div>

            </div>
	     <%@include file="footer.html" %>
  </div>
</div>

<!-- <script type="text/javascript">
    var popup;
    function SelectName() {
        popup = window.open("Popup.htm", "Popup", "width=300,height=100");
        popup.focus();
    }
</script> -->



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
<script>

function popup() {
    window.open("../popup/searchspring", 'window', 'width=200,height=100');
}
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
<!-- DROPZONE -->
    <script src="resources/js/plugins/dropzone/dropzone.js"></script>

    <script>
        /* $(document).ready(function(){

            Dropzone.options.myAwesomeDropzone = {

                autoProcessQueue: false,
                uploadMultiple: true,
                parallelUploads: 100,
                maxFiles: 100,

                // Dropzone settings
                init: function() {
                    var myDropzone = this;

                    this.element.querySelector("button[type=submit]").addEventListener("click", function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        myDropzone.processQueue();
                    });
                    this.on("sendingmultiple", function() {
                    });
                    this.on("successmultiple", function(files, response) {
                    });
                    this.on("errormultiple", function(files, response) {
                    });
                }

            }

       });
 */        $(document).on('click', '.browse', function(){
        	  var file = $(this).parent().parent().parent().find('.file');
        	  file.trigger('click');
        	});
        	$(document).on('change', '.file', function(){
        	  $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
        	});
	</script>
</body>
</html>

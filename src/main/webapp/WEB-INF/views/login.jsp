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
<title>Login</title>
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="resources/css/animate.css" rel="stylesheet">
<link href="resources/css/style.css" rel="stylesheet">
<style>
body {
  background-image: url("resources/img/bg.jpg");
  background-repeat: no-repeat, repeat; 
  background-color: #cccccc;
}
</style>
</head>

<body>
<br/>
<br/>
<div class="middle-box text-center animated fadeInDown">
  <div class="contact-box"> <img class="img-responsive center-block" width="60%" src="resources/img/logo.png"/>
	  <br>
  <!--   <h3>Welcome to Dalmia</h3> -->
   
    <%-- <form class="m-t" role="form" action="index.html"> --%>
    <form:form action="login" method="POST" modelAttribute="user">
      <div class="form-group">
     
        <form:input path="userName" name="userName" id="userName" class="form-control" placeholder="Username"/>
         <p id="userne" style="color: red"></p>
      </div>
      <div class="form-group">
     
        <form:password path="password" name="passWord" id="passWord" class="form-control" placeholder="Password"/>
        <p id="passwd" style="color: red"></p>
      </div>
      <div style="color: red" id="error">${msg}</div>
   
     <form:button type="submit" class="btn btn-primary block full-width m-b" onclick="return loginValidation()">Login</form:button>
   
    </form:form>
  <!--   <p class="m-t"> <small>Dalmia Cement &copy; 2019</small> </p> -->
  </div>
</div>

<!-- Mainly scripts --> 
 <script src="resources/js/jquery-2.1.1_old.js"></script> 
 <script src="resources/js/bootstrap.min.js"></script>
 <script type="text/javascript">
	function loginValidation() {
		//alert(1);
		var userName = $("#userName").val();
		var password = $("#passWord").val();
		$("#error").html("");
			if (userName != null && userName != "") {
				$("#userne").html("");
				if (password != null && password != "") {
					$("#passwd").html("");
					return true;
				} else {
					/* document.getElementById("asipassftp").innerHTML = ""; */
					$("#passwd").html(
							"<i><b>!</b></i> &nbsp;Enter your Password");
					$("#passWord").focus();
				}
			} else {
				$("#userne").html(
						"<i><b>!</b></i> &nbsp;Enter your Username");
				$("#userName").focus();
		} 
		return false;
	}
</script>
 
 
 
 
</body>
</html>

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
	    <script src="resources/js/jquery-2.1.1_old.js"></script> 

    <script src="resources/js/bootstrap.min.js"></script>
    <script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<style type="text/css">
    body {
        color: #404E67;
        background: #F5F7FA;
		font-family: 'Open Sans', sans-serif;
	}
	.table-wrapper {
		width: 700px;
		margin: 30px auto;
        background: #fff;
        padding: 20px;	
        box-shadow: 0 1px 1px rgba(0,0,0,.05);
    }
    .table-title {
        padding-bottom: 10px;
        margin: 0 0 10px;
    }
    .table-title h2 {
        margin: 6px 0 0;
        font-size: 22px;
    }
    .table-title .add-new {
        float: right;
		height: 30px;
		font-weight: bold;
		font-size: 12px;
		text-shadow: none;
		min-width: 100px;
		border-radius: 50px;
		line-height: 13px;
    }
	.table-title .add-new i {
		margin-right: 4px;
	}
    table.table {
        table-layout: fixed;
    }
    table.table tr th, table.table tr td {
        border-color: #e9e9e9;
    }
    table.table th i {
        font-size: 13px;
        margin: 0 5px;
        cursor: pointer;
    }
    /* table.table th:last-child {
        width: 100px;
    } */
    table.table td a {
		cursor: pointer;
        display: inline-block;
        margin: 0 5px;
		min-width: 24px;
    }    
	table.table td a.add {
        color: #27C46B;
    }
    table.table td a.edit {
        color: #FFC107;
    }
    table.table td a.delete {
        color: #E34724;
    }
    table.table td i {
        font-size: 19px;
    }
	table.table td a.add i {
        font-size: 24px;
    	margin-right: -1px;
        position: relative;
        top: 3px;
    }    
    table.table .form-control {
        height: 32px;
        line-height: 32px;
        box-shadow: none;
        border-radius: 2px;
    }
	table.table .form-control.error {
		border-color: #f50000;
	}
	table.table td .add {
		display: none;
	}
</style>

</head>

<body class="md-skin">
<div id="wrapper">
 <!-- <div id="navbar"></div> -->
  <%@include file="navbar.jsp" %>

        <div id="page-wrapper" class="gray-bg">
     <div id="header"></div>
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-lg-10">
                    <h2>Axle/Wheel Configuration</h2>
                   <!--  <ol class="breadcrumb">
                        <li>
                            <a href="index.html">Home</a>
                        </li>
                        <li>
                            <a>Axle/Wheel Config</a>
                        </li>
                      
                    </ol> -->
                </div>
                <div class="col-lg-2">

                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight">
      </div>
     
            <div class="row">
                <div class="col-lg-12">
                   <form:form method="POST" action="axelWheelConfiguration" >
                 
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                     
							 <h5>Axle/Wheeler type  </h5>
   								
                                  <div class="col-sm-3">
                                  <select id="mySelect" name="mySelect" class="form-control" style="color:#000 !important; margin-top:-7px;"> 
                                        <option value="Select" label="Select Axle Wheeler Type">
				    					<option selected="selected"  value="6" label="6 Wheeler"/>
										<option value="10" label="10 Wheeler"/>
										<option value="12" label="12 Wheeler"/>
										<option value="14" label="14 Wheeler"/>
         								<option value="18" label="18 Wheeler"/>
                               </select>				
                                       
                                     </div>

					                 </div>
                                      </div>
                                  </form:form>
                        
                        
                        <div class="ibox-content">
						
                      <button type="button" class="btn btn-info pull-right" data-toggle="modal" data-target="#addAxleWheeler" onclick="hideText()"><i class="fa fa-plus"></i> Add New</button>
 <!--                          <button type="button" class="btn btn-info" data-toggle="modal" data-target="#create" onClick="fnClear()">Create Axle/Wheel</button>
 -->    
                        <table class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                     <th>Sl.No</th> 
                                    <th>Lead from 1st order</th>
                                    <th>Clubbing forward</th>
                                    <th>Action</th>
                                  <!--   <th>Action</th> -->
                                   
                                </tr>
                                </thead>
                               <tbody id="axleWhel">
                             <%-- <c:forEach items="${axleWhllerInfoList}"
											var="axleWheelerIfList" varStatus="status">
											<tr>
											 <td>${axleWheelerIfList.id}</td>
											 <td>${axleWheelerIfList.order}</td>
											 <td>${axleWheelerIfList.club}</td>
											 <td>
							                    <a class="add" title="Add" data-toggle="tooltip"><i class="material-icons">&#xE03B;</i></a>
                            					<a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>
                            					<a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>
                        					</td>
											</tr>
										</c:forEach>
                                    </tbody>  --%>
                            </table>
                                          
                      
                        
            <div id="create" class="modal fade" role="dialog">
	
			<div class="modal-dialog">
				<div class="modal-content animated bounceInRight">
					<div class="modal-header">					


					</div>
					
					<div class="modal-body">
                     <form id="formId" class="form-horizontal m-l-md" action="axelWheelConfiguration" method="post">
                            
                       
                                <div class="form-group"><label class="col-lg-3 control-label">Wheeler Type:</label>

                                    <div class="col-lg-5">
                                    <input id="axlewheelertype" name="axlewheelertype" type="text" class="form-control" ><br/>
                                     <button class="btn btn-sm btn-primary center-block" name="addType" type="submit">Add</button>
<!--                                      <button class="btn btn-sm btn-primary pull-right" name="addType" type="button" >Reset</button>
 -->                                    </div>
                                   
                                </div>
                    
                    
    			 <c:choose>
             	<c:when test="${showMessage == 'success'}">
             	<script>
             	$(document).ready(function(){
             	$('#create').modal('show'); 
             	});
             	</script>
             	<span style="color: green;" class="text-center"  id="errExist">wheeler type is has been saved successfully</span> 
             	</c:when>
             	<c:when test="${showMessage == 'Error'}">
             	 	<script>
             	$(document).ready(function(){
             	$('#create').modal('show');
             	});
             	</script>
             	<span style="color: red;" class="text-center" id="errExist">The wheeler type is already present</span> 
             	</c:when>
             	</c:choose>
                  </form>
                            
                        </div>
                    </div>
                </div>
        
            </div>
      
      
                  
        </div>
      

        </div>
        </div>
 
 <div class="modal" id="edit" tabindex="-1" data-backdrop="static" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Edit Axle/Wheeler Information</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
      
      <form id="formId" class="form-horizontal m-l-md" action="editAxleWheeler" method="post">
       <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th>Id</th>
                    <th>Lead From</th>
                    <th>Club</th>
                    
                   
                  </tr>
                </thead>
                <tbody>
                  <tr>
                   <td class="wheelerTypeId"> <input type="text" id="id" name="id" class="form-control" readonly="readonly" ></td>
				   <td> <input type="text" id="leadFrom" name="leadFrom"class="form-control"></td>
				   <td> <input type="text" id="club" name="club"class="form-control"></td>
				   <td class="wheerlerType"> <input type="text"  id="wheerlerType" name="wheerlerType" class="form-control"> </td>
                  </tr>
               
                </tbody>
              </table>
                <div class="modal-footer">
        <button type="button"   class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-primary btn-rounded">Save changes</button>
      </div>
              </form>
      </div>
    
    </div>
  </div>
</div>

 <div class="modal" id="addAxleWheeler" tabindex="-1" data-backdrop="static" role="dialog" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content animated bounceInRight">
      <div class="modal-header">
        <h4 class="modal-title">Add Axle/Wheeler Information</h4>
      </div>
      <div class="modal-body" style="overflow-x: auto;">
      
      <form id="formId" class="form-horizontal m-l-md" action="addAxleWheeler" method="post">
       <table class="table table-bordered table-hover">
                <thead>
                  <tr>
                    <th  width="50%">Lead From</th>
                    <th>Club</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
				   <td> <input type="text" id="leadFrom" name="leadFrom"class="form-control"></td>
				   <td> <input type="text" id="club" name="club"class="form-control"></td>
				   <td class="newWheerlerType"> <input type="text"  id="newWheerlerType" name="newWheerlerType" class="form-control"> </td>
                  </tr>
               
                </tbody>
              </table>
                <div class="modal-footer">
        <button type="button"   class="btn btn-white btn-rounded" data-dismiss="modal">Close</button>
        <button type="submit" class="btn btn-primary btn-rounded">Save changes</button>
      </div>
              </form>
      </div>
    
    </div>
  </div>
</div>

<div class="modal" tabindex="-1" role="dialog" id="deleteSuccessId">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
       <!--  <h5 class="modal-title">Modal title</h5> -->
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" style="background-color:#67e569;">
        <p >Axle wheeler info has been  successfully deleted</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<script>
		
$(document).ready(function(){

    $("#navbar").load("navbar.jsp");
	 $("#header").load("header.html");
	 $("#footer").load("footer.html");
});

function fnClear()
{
$('#errExist').hide();
}
function getWheelerInfoData(wheelerTypes){
	
	 var value = value;
		var srNo = 1 ; 
		
		 $.ajax({
				type : "GET",
				url : "axelWheelValue",
				data : "optionValue=" + wheelerTypes,
				success : function(response) {
					var json = JSON.stringify(response);
					$("#axleWhel").empty();
					$.each(response, function(i, value) {
						 $("#axleWhel").append("<tr><td>" + value.id + "</td><td>" + value.order + "</td><td>" + value.club +
								 
						"</td><td>"+ '<a class="add" title="Add"  data-toggle="tooltip"><i class="material-icons">&#xE03B;</i></a>'
						+' <a style="display: inline-block !important;"  data-userid="'+value.id+'" data-userage="'+value.order+'"  data-club="'+value.club+'"  class="edit dhar"  title="Edit"  data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>'+ '<a class="delete" title="Delete" data-deleteuserid="'+value.id+'" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>'+
						"</td></tr>"); 
						 
						srNo = srNo+1;
					});
   				},
				error : function(e) {
					 alert('Error: ' + e); 
				}
			});
		
}

$(document).ready(function()
		{
	 var modelAttributeValue = '${mySelect}';
	getWheelerInfoData(modelAttributeValue);
	$("#mySelect").val(modelAttributeValue); 
	  $("#mySelect").change(function()
			  {
		 var wheelerType = $("#mySelect option:selected").val(); 
		 getWheelerInfoData(wheelerType);
		 var modelAttributeValue = '${mySelect}';
		 //var wheelerType = $("#mySelect option:selected").val(); 
			   });
		});
		

/*            
 * <a class="add" title="Add" data-toggle="tooltip"><i class="material-icons">&#xE03B;</i></a>
 */
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip();
	var actions = $("table td:last-child").html();
	// Append table with add row form on add new button click
    $(".add-new").click(function(){
    
		$(this).attr("disabled", "disabled");
		var index = $("table tbody tr:last-child").index();
        var row = '<tr>' +
            '<td><input type="text" class="form-control" name="name" id="name"></td>' +
            '<td><input type="text" class="form-control" name="department" id="department"></td>' +
            '<td><input type="text" class="form-control" name="phone" id="phone"></td>' +
			'<td>' +  '<a class="add" title="Add" data-toggle="tooltip"><i class="material-icons">&#xE03B;</i></a>'
				+' <a style="display: inline-block !important;" class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons">&#xE254;</i></a>'+ '<a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">&#xE872;</i></a>' + '</td>' + 
        '</tr>';
    	$("table").append(row);		
		$("table tbody tr").eq(index + 1).find(".add, .edit").toggle();
        $('[data-toggle="tooltip"]').tooltip();
    });
	// Add row on add button click
	$(document).on("click", ".add", function(){
	
		var empty = false;
		var input = $(this).parents("tr").find('input[type="text"]');
        input.each(function(){
			if(!$(this).val()){
				$(this).addClass("error");
				empty = true;
			} else{
                $(this).removeClass("error");
            }
		});
		$(this).parents("tr").find(".error").first().focus();
		if(!empty){
			input.each(function(){
				$(this).parent("td").html($(this).val());
			});			
		 	$(this).parents("tr").find(".add, .edit").toggle();
			$(".add-new").removeAttr("disabled"); 
		}	
		
    });
	// Edit row on edit button click
	$(document).on("click", ".edit", function(){
		var wheelerType = $("#mySelect option:selected").val(); 
		 $("#id").val($(this).data("userid"));   
			$("#leadFrom").val($(this).data("userage"));
			    $("#club").val($(this).data("club"));
			    $("#wheerlerType").val(wheelerType);
			    $('.wheerlerType').hide();
				var myEditModal = $("#edit");
			   myEditModal.modal({ show: true });
		 
        $(this).parents("tr").find("td:not(:last-child)").each(function(){
			$(this).html('<input type="text"  class="form-control" value="' + $(this).text() + '">');
		});	
        
        
	/* 	 $(this).parents("tr").find(".add, .edit").toggle();
		$(".add-new").attr("disabled", "disabled");   */
    });
	// Delete row on delete button click
	$(document).on("click", ".delete", function(){
		var wheelerId = $(this).data("deleteuserid");
		  $.ajax({
				type : "GET",
				url : "deleteAxleWheelerInfo",
				data : "axleWheelerId=" + wheelerId,
				success : function(response) {
					var deleteId = $("#deleteSuccessId");
					deleteId.modal({ show: true });
				},
				error : function(e) {
					 alert('Error: ' + e); 
				}
			});
		  $(this).parents("tr").remove();
		  $(".add-new").removeAttr("disabled");
    });

});

 function hideText(){
	 $('.newWheerlerType').hide();
	 var wheelerTypes = $("#mySelect option:selected").val(); 
	// alert(wheelerType);
	$("#newWheerlerType").val(wheelerTypes);
 }

</script>
	
 

    <!-- Peity -->
    <script src="resources/js/plugins/peity/jquery.peity.min.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="resources/js/inspinia.js"></script>
    <script src="resources/js/plugins/pace/pace.min.js"></script>

    <!-- iCheck -->
    <script src="resources/js/plugins/iCheck/icheck.min.js"></script>

    <!-- Peity -->
    <script src="resources/js/demo/peity-demo.js"></script>

</body>

</html>

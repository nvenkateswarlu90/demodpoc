 <nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
      <ul class="nav metismenu" id="side-menu">
        <li class="nav-header">
          <div class="dropdown profile-element"> <span> <img alt="image"  src="resources/img/logo.png" /> </span> <a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span class="clear"><!--  <span class="block m-t-xs"> <strong class="font-bold">Azam Rizvi</strong> </span> <span class="text-muted text-xs block">Admin <b class="caret"></b></span> </span> --> </a>
            <ul class="dropdown-menu animated fadeInRight m-t-xs">
              <li><a href="profile.html">Profile</a></li>
              <li><a href="contacts.html">Contacts</a></li>
              <li><a href="mailbox.html">Mailbox</a></li>
              <li class="divider"></li>
              <li><a href="login.html">Logout</a></li>
            </ul>
          </div>
          <div class="logo-element"> </div>
        </li>
        <!-- <li class="active"> <a href="index.html"><i class="fa fa-th-large"></i> <span class="nav-label">Dashboards</span></a> </li> -->
		      <li> <a href="<c:url value='/algorithmProcess' />"><i class="fa fa-th-large"></i> <span class="nav-label">Algorithm Process
		      <span class="fa arrow"></span></a> 
		     <ul class="nav nav-first-level collapse">
		      <li><a href="<c:url value='/processBatchFile'/>"> <span class="fa fa-chevron-right submenu"></span>  Process New Order Batch</a></li>
		    <li ><a href="<c:url value='/showPendingOrders'/>"> <span class="fa fa-chevron-right submenu"></span>  Pending Orders</a></li>
		    <li><a href="<c:url value='intellShip' />"> <span class="fa fa-chevron-right submenu"></span>  Run Itelliship Algo</a></li>
            <li><a href="<c:url value='/getShippingOrderHistory'/>"> <span class="fa fa-chevron-right submenu"></span>  History</a></li>
           
		  </ul>
		  </li>
		  
		    <li> <a href="#"><i class="fa fa-th-large"></i> <span class="nav-label">Transport Info</span><span class="fa arrow"></span></a> 
		  <ul class="nav nav-first-level collapse">
		  <li><a href="<c:url value='uploadTrucksInfo' />"> <span class="fa fa-chevron-right submenu"></span> Upload Trucks Information</a></li>
            <li><a href="<c:url value='getAllTrucksInformation' />"> <span class="fa fa-chevron-right submenu"></span> All Available Trucks</a></li>
            <li><a href="<c:url value='showTruckHistoryDetails' />"> <span class="fa fa-chevron-right submenu"></span> Show Trucks History Details</a></li>
             <li><a href="<c:url value='uploadTruckHistoryDetails' />"> <span class="fa fa-chevron-right submenu"></span> Upload And Update Truck History Details</a></li> 
            <li><a href="#"> <span class="fa fa-chevron-right submenu"></span> Clubbed Order Loaded Trucks</a></li>
             </ul>
             </li>
             
              <li> <a href="#"><i class="fa fa-th-large"></i> <span class="nav-label">Configuration</span><span class="fa arrow"></span></a>
               <ul class="nav nav-first-level collapse">
               <li><a href="<c:url value='normalLoadConfiguration' />"> <span class="fa fa-chevron-right submenu"></span> DistrictWise Normal Load Configuration</a></li>
               <li><a href="<c:url value='uploadLoadConfig' />"> <span class="fa fa-chevron-right submenu"></span>Upload DistrictWise Configuration</a></li>
               <li><a href="<c:url value='axelWheelConfiguration' />"> <span class="fa fa-chevron-right submenu"></span> Axel Wheel Configuration</a></li>
               <li><a href="<c:url value='distClubOrdByPassConfig' />"> <span class="fa fa-chevron-right submenu"></span>DistrictClubbingOrderByPass</a></li>
         
               </ul>
              </li>
              
             <%--  <li> <a href="#"><i class="fa fa-th-large"></i> <span class="nav-label">Configuration View</span><span class="fa arrow"></span></a>
              <ul class="nav nav-first-level collapse">
               <li><a href="<c:url value='dwnlcView' />"> <span class="fa fa-chevron-right submenu"></span> DistrictWise Normal Load Configuration View</a></li>
               </ul>
              </li> --%>
          </ul>
    </div>
  </nav>

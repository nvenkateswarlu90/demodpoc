package com.a4tech.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import saveShipping.StoreSpDetails;

import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.dao.entity.DistrictWiseNormalLoadCapacity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.map.model.Address;
import com.a4tech.map.service.MapService;
import com.a4tech.service.mapper.IOrderDataMapper;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.AxleWheelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.FileUploadBean;
import com.a4tech.shipping.model.IntellishipModel;
import com.a4tech.shipping.model.IntellishipModelByMaterial;
import com.a4tech.shipping.model.NormalLoadConfiguration;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.PlantDetails;
import com.a4tech.shipping.model.ShippingDeliveryOrder;
import com.a4tech.shipping.model.ShippingDetails;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.ShortestDistLantiAndLongti;
import com.a4tech.shipping.model.AvailableTrucksModel;
import com.a4tech.shipping.model.TruckHistoryDetail;
import com.a4tech.shipping.model.User;
import com.a4tech.shipping.services.ShippingService;
import com.a4tech.shipping.validator.NormalLoadValidator;
import com.a4tech.util.ApplicationConstants;
import com.a4tech.util.CommonUtility;
import com.a4tech.util.TruckTypeInfo;

@Controller
@RequestMapping({ "/", "/demoversion" })
public class ShippingDetailController {

	
	/*
	 * @Autowired private IOrderDataMapper dataMapper;
	 */
	@Autowired
	private IShippingOrder shippingOrderService;
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	private IOrderDataMapper dataMapper;
	
	@Autowired
	private NormalLoadValidator normalLoadValidatior;
	
	@InitBinder("normalLoadConfig")
	protected void initBinder(WebDataBinder binder){
		binder.setValidator(normalLoadValidatior);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String login(Model model) {
	model.addAttribute("user", new User());
		return "login";
	}
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String loginProcess(@ModelAttribute("user") User user,Model model) {
	     User userDetails = shippingOrderService.getUserDetails(user.getUserName());
	     if(userDetails != null) {
			if ((userDetails.getUserName().equalsIgnoreCase(user.getUserName()))
					&& (userDetails.getPassword().equals(user.getPassword()))) {
				return "redirect:/showPendingOrders";
			 } else {
				 model.addAttribute("msg", "Invalid Details");
				 return "login";
			 }
		 } else {
			 model.addAttribute("msg", "Invalid Details");
			 return "login";
		 }
	     
		
		//return "/redirect:"
	}
	
	@RequestMapping(value = "/getShortestDistence/{orderNo}")
	public String getShortDistence(@PathVariable("orderNo") String orderNo) {
		System.out.println(orderNo);
		return getDistence(orderNo);
	}

	@RequestMapping(value = "/getAllShortestDistence")
	public String getAllShortDistenceFromOrders() {
		return getAllDistence1();
	}

	/*
	 * @RequestMapping(value="/saveShippingMapping") public void
	 * saveShippingData(){ dataMapper.mapper(); System.out.println("Test"); }
	 */
	/*@RequestMapping(method = RequestMethod.GET)
	public ModelAndView algorithmProcess1() {
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllShippingOrders();
		System.out.println("Total Orders: " + shippingaOrderList.size());
		return new ModelAndView("algorithm_process", "shippingaOrderList", shippingaOrderList);
	}*/

	@RequestMapping(value = "/algorithmProcess")
	public ModelAndView algorithmProcess() {
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllShippingOrders();
		System.out.println("Total Orders: " + shippingaOrderList.size());
		return new ModelAndView("algorithm_process", "shippingaOrderList", shippingaOrderList);
	}
	
	@RequestMapping(value = "/showPendingOrders")
	public ModelAndView pendingOrders() {
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllShippingOrders();
		System.out.println("Total Orders: " + shippingaOrderList.size());
		return new ModelAndView("algorithm_process", "shippingaOrderList", shippingaOrderList);
	}


	@RequestMapping(value = "/intellShip")
	public ModelAndView intellShipPro() {
		// List<IntellishipModel> orderGroupList = getGroupOrders();
		shippingOrderService.deleteAllGroupOrders("orderGroup");
		shippingOrderService.deleteAllGroupOrders("orderReAssign");
		shippingOrderService.deleteAllGroupOrders("shippingFinalOrders");
		
		//here new map
		List<ShippingDetails1> shippingaOrderListOnChannel = getAllOrdersBasedOnDistributionChannel("1");
		Map<String, List<ShippingDetails1>> ordersOnDistrictMap = getAllOrdersBasedOnDistricts(
				shippingaOrderListOnChannel);
		Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap = getAllOrdersBasedOnMaterial(
				ordersOnDistrictMap);
		Map<String, Map<List<ShippingDetails1>, List<TruckHistoryDetail>>> finalTruckDetails = shippingService.getOrdersFitIntoTruck1(finalMaterialOrdMap);
		shippingService.getFinalOrdersClub1(finalTruckDetails);
		List<IntellishipModelByMaterial> finalIntelishipModel = shippingService.getFinalGroupOrders();
		
		/*Map<String, Map<String, Map<Integer, List<ShippingDetails1>>>> groupByDistance = shippingService
				.getOrdersBasedOnDistence(finalMaterialOrdMap);
	Map<String, Map<List<ShippingDetails1>, List<TruckDetails>>> finalTruckDetails1 = shippingService.getOrdersFitIntoTruck(groupByDistance);
	shippingService.getFinalOrdersClub(finalTruckDetails);
	List<IntellishipModelByMaterial> finalIntelishipModel12 = shippingService.getFinalGroupOrders();*/
		/*Map<String, Map<List<ShippingDetails1>, List<TruckDetails>>> finalTruckDetails = getOrdersFitIntoTruck(
				groupByDistance);*/
	//shippingService.getFinalOrdersClub
	
	//getFinalOrdersClub(finalTruckDetails);
	
	/*	List<ShippingDetails1> shippingaOrderListOnChannel = getAllOrdersBasedOnDistributionChannel("1");
		Map<String, List<ShippingDetails1>> ordersOnDistrictMap = getAllOrdersBasedOnDistricts(
				shippingaOrderListOnChannel);
		Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap = getAllOrdersBasedOnMaterial(
				ordersOnDistrictMap);
		Map<String, Map<List<ShippingDetails1>, List<TruckDetails>>> finalTruckDetails = getOrdersFitIntoTruck1(
				finalMaterialOrdMap);
		getFinalOrdersClub(finalTruckDetails);
		List<IntellishipModelByMaterial> finalIntelishipModel = getFinalGroupOrders();
		*/
		
		//List<IntellishipModelByMaterial> finalIntelishipModel = getFinalGroupOrders();
		/* studentlist.sort((Student s1, Student
		 s2)->s1.getName().compareTo(s2.getName()));*/
		 //soring based on district name
		/*finalIntelishipModel.sort((IntellishipModelByMaterial i1, IntellishipModelByMaterial i2) -> i1.getDistrictName()
				.compareTo(i2.getDistrictName()));*/
		//finalIntelishipModel.sort(arg0);
		Collections.sort(finalIntelishipModel, Comparator.comparing(IntellishipModelByMaterial::getTotalOrderQuantity));
		return new ModelAndView("intellShipProcess", "shippingGroupList", finalIntelishipModel);
		/*
		 * if(isemptyValues(finalTruckDetails)){ return new
		 * ModelAndView("intellShipProcess", "shippingGroupList", new
		 * ArrayList<>()); } else { return new ModelAndView("intellShipProcess",
		 * "shippingGroupList", finalIntelishipModel); }
		 */

		// return "intellShipProcess";
	}

	@RequestMapping(value = "/getShippingOrderByDate")
	@ResponseBody
	public List<ShippingDetails1> getOderDetailsByDate(HttpServletRequest req) {
		System.out.println("Get Shipping Order details based On Date");
		String orderDate = req.getParameter("orderDate");
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getShippingDetailsByDate(orderDate);
		return shippingaOrderList;
	}

	@RequestMapping(value = "/getGroupOrderByDate")
	@ResponseBody
	public List<OrderGroup> getGroupOrders(HttpServletRequest req) {
		System.out.println("Get order Group Order details based On Date");
		String orderDate = req.getParameter("orderDate");
		List<OrderGroup> orderGroupList = shippingOrderService.getOrderGroupByDate(orderDate);
		return orderGroupList;
	}

	@RequestMapping(value = "/getShippingOrderHistory")
	public ModelAndView getShippingHistory() {
		List<IntellishipModelByMaterial> ordersHistoryData = shippingOrderService.getAllShippingFinalOrders();
		Collections.sort(ordersHistoryData, Comparator.comparing(IntellishipModelByMaterial::getTotalOrderQuantity));
		return new ModelAndView("history", "ordersHistory", ordersHistoryData);
		
		// return "intellShipProcess";
	}

	@RequestMapping(value = "/getGroupOrderByTruck")
	@ResponseBody
	public List<ShippingDetails1> getGroupOrdersByTruck(HttpServletRequest req) {
		System.out.println("Get order Group Order details based On Truck");
		String truckNo = req.getParameter("truckNo");
		/*List<String> orderNoList = shippingOrderService.getOrderNoByTruck(truckNo);
		List<ShippingDetails1> shippingaOrderList = getShippingDetailsByTruckNo(orderNoList);*/
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllReAssignOrdersBasedOnTruckNo(truckNo);
		return shippingaOrderList;
	}

	@RequestMapping(value = "/getLatiAndLongValues", produces = "application/json")
	@ResponseBody
	public List<Address> getLatitudeAndLongitude(HttpServletRequest req) {
		System.out.println("Get Latitude AndLongitude values");
		String truckNo = req.getParameter("truckNo");
		List<Address> addressList = shippingOrderService.getLatitudeAndLongitude(truckNo);
		return addressList;
	}

	@RequestMapping(value = "/getAllTrucksInformation")
	public ModelAndView getAllTrucksInfo() {
		List<AvailableTrucksModel> trucksInfoList = shippingOrderService.getAllTruckInfo();
		return new ModelAndView("truck_info", "trucksList", trucksInfoList);
	}
	  @RequestMapping(value = "/uploadTrucksInfo", method = RequestMethod.GET)
	   public ModelAndView fileUploadPage() {
		  FileUploadBean file = new FileUploadBean();
	      ModelAndView modelAndView = new ModelAndView("upload", "command", file);
	      return modelAndView;
	   }
	  @RequestMapping(value="/uploadTrucksInfo", method = RequestMethod.POST)
	   public String fileUpload(FileUploadBean mfile, ModelMap modelmap,Model model) throws IOException {
		  
		  int countTruckDetailsFile=10;
		  int numberOfCells=0;
		  if(mfile.getFile().getSize() == 0)
		  {
			  model.addAttribute("showMessage", "select");
		  }
		  else{
		  File file = convertMultiPartFileIntoFile(mfile.getFile());
			//long fileSize = file.length(); 
			Workbook wb = getWorkBook(file);
			Sheet sheet = wb.getSheetAt(0);
            numberOfCells=sheet.getRow(0).getPhysicalNumberOfCells();
            if(numberOfCells==countTruckDetailsFile){
			dataMapper.readTruckExcel(wb);
			model.addAttribute("showMessage", "success");
            }else
            {
    		model.addAttribute("showMessage", "format");
            }
		  }
		  return "upload";
	   }
	  
	  @RequestMapping(value = "/processBatchFile", method = RequestMethod.GET)
   public ModelAndView batchFileUpload() {
	  FileUploadBean file = new FileUploadBean();
      ModelAndView modelAndView = new ModelAndView("upload", "command", file);
      return modelAndView;
   }
  @RequestMapping(value="/processBatchFile", method = RequestMethod.POST)
   public String processBatchFile(FileUploadBean mfile, ModelMap modelmap,Model model) throws IOException {
	  int countTruckDetailsFile=20;
	  int numberOfCells=0;
	  if(mfile.getFile().getSize() == 0)
	  {
		  model.addAttribute("showMessage", "select");
	  }
	  else{
	  File file = convertMultiPartFileIntoFile(mfile.getFile());
//		long fileSize = file.length();
		Workbook wb = getWorkBook(file);
		Sheet sheet = wb.getSheetAt(0);
        numberOfCells=sheet.getRow(0).getPhysicalNumberOfCells();
        dataMapper.pendingOrderMapper(wb);
		model.addAttribute("pendingOrderMessage", "success");
		/*	if (numberOfCells == countTruckDetailsFile) {
			dataMapper.pendingOrderMapper(wb);
			model.addAttribute("pendingOrderMessage", "success");
		} else {
			model.addAttribute("showMessage", "format");
		}*/
	  }
	  return "upload";
   }
  
  @RequestMapping(value = "/uploadTruckHistoryDetails", method = RequestMethod.GET)
  public ModelAndView truckHistoryFileUpload() {
	  FileUploadBean file = new FileUploadBean();
     ModelAndView modelAndView = new ModelAndView("uploadAndUpdate", "command", file);
     return modelAndView;
  }
  
@RequestMapping(value="/uploadTruckHistoryDetails", method = RequestMethod.POST)
  public String processHistory(FileUploadBean mfile,Model model,HttpServletRequest request) throws IOException {
	  int countTruckDetailsFile=6;
	  int numberOfCells=0;
	  if(mfile.getFile().getSize() == 0)
	  {
		  model.addAttribute("showMessage", "select");
	  }
	  else{
	  File file = convertMultiPartFileIntoFile(mfile.getFile());
		Workbook wb = getWorkBook(file);
		Sheet sheet = wb.getSheetAt(0);
		 numberOfCells=sheet.getRow(0).getLastCellNum();
			if (numberOfCells == countTruckDetailsFile) {
				 if(request.getParameter("button1") != null) {
			      dataMapper.readTruckHistoryExcel(wb);
			      model.addAttribute("truckHistoryMessage", "success");
				 }else {
				  dataMapper.updateTruckHistoryExcel(wb);
			      model.addAttribute("truckHistoryMessageUpdated", "success");
				  }
				} else {
			model.addAttribute("showMessage", "format");
		} }
	  return "uploadAndUpdate";
  }



@RequestMapping(value="/updateTruckHistoryDetails", method = RequestMethod.POST)
public String updateHistory(FileUploadBean mfile, ModelMap modelmap,Model model) throws IOException {
	  int countTruckDetailsFile=6;
	  int numberOfCells=0;
	  if(mfile.getFile().getSize() == 0)
	  {
		  model.addAttribute("showMessage", "select");
	  }
	  else{
	  File file = convertMultiPartFileIntoFile(mfile.getFile());
//		long fileSize = file.length();
		Workbook wb = getWorkBook(file);
		Sheet sheet = wb.getSheetAt(0);
     //numberOfCells=sheet.getRow(0).getPhysicalNumberOfCells();
		 numberOfCells=sheet.getRow(0).getLastCellNum();
			if (numberOfCells == countTruckDetailsFile) {
			dataMapper.readTruckHistoryExcel(wb);
			model.addAttribute("truckHistoryMessage", "success");
		} else {
			model.addAttribute("showMessage", "format");
		}
	  }
	  return "uploadAndUpdate";
}





	@RequestMapping(value = "/showTruckHistoryDetails", method = RequestMethod.GET)
	public ModelAndView showTruckHistory() {
		List<TruckHistoryDetailsEntity> truckHistoryList = shippingOrderService.getAllTrucksHistoryDetails();
		return new ModelAndView("truckHistoryDetails", "truckHistoryList", truckHistoryList);
	}
	
	
	@RequestMapping(value = "/searchByVehicleNo" ,produces = "application/json")
	@ResponseBody
	public  List<TruckHistoryDetailsEntity> getSearch(HttpServletRequest req,Model model) {

		String type = req.getParameter("type");
		String value = req.getParameter("type1");
		List<TruckHistoryDetailsEntity> searchHistoryList =null;
		
		if(type.equals("Vehicle No"))
		{
			 searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value,type);

		}else{//base on district name
			 searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value,type);
		
		}
		return searchHistoryList;
	
	}

	@RequestMapping(value = "/searchByVehicleNoDistrictName")
	public ModelAndView getSearch1(HttpServletRequest req, Model model) {

		String type = req.getParameter("selected");
		String value = req.getParameter("value");
		List<TruckHistoryDetailsEntity> searchHistoryList = null;

		if ("Vehicle No".equals(type)) {
			searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value, type);
		} else if("District Name".equals(type)){// base on district name
			searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value, type);
		} else {
			 searchHistoryList = shippingOrderService.getAllTrucksHistoryDetails();
		}
		return new ModelAndView("truckHistoryDetails", "truckHistoryList", searchHistoryList);

	}
	
	/*@RequestMapping(value = "/searchByVehicleNo")
	@ResponseBody
	public ModelAndView searchHistory(HttpServletRequest req) {

		String type = req.getParameter("type");
		String value = req.getParameter("type1");
		List<TruckHistoryDetailsEntity> searchHistoryList =null;
		if(type.equals("Vehicle No"))
		{
			 searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value,type);

		}else{//base on district name
			 searchHistoryList = shippingOrderService.getSearchTrucksHistoryDetails(value,type);
		
		}
		
		return new ModelAndView("truckHistoryDetails", "searchHistoryList", searchHistoryList);
	
	}*/

	@RequestMapping(value = "/normalLoadConfiguration", method = RequestMethod.GET)
	public ModelAndView showNormalLoadConfiguration() {
		return new ModelAndView("configure_districtWise_Normal_load", "normalLoadConfig",
				new NormalLoadConfiguration());
		/*return "configure_districtWise_Normal_load";*/
	}
	

	@RequestMapping(value = "/normalLoadConfiguration", method = RequestMethod.POST,params="add")
	public String addNormalLoadConfiguration(
			@ModelAttribute("normalLoadConfig") @Validated NormalLoadConfiguration normalLoadConfig,BindingResult result,Model model) {
		System.out.println("add configuration");
		if(result.hasErrors()){
			return "configure_districtWise_Normal_load";
		}
     shippingOrderService.saveDistrictWiseNormalLoad(normalLoadConfig);
       model.addAttribute("message", "success");
		return "configure_districtWise_Normal_load";
	}
	@RequestMapping(value = "/normalLoadConfiguration", method = RequestMethod.POST,params="update")
	public String updateNormalLoadConfiguration(
			@ModelAttribute("normalLoadConfig") @Validated NormalLoadConfiguration normalLoadConfig,BindingResult result,Model model) {
		System.out.println("Update configuration");
		if(result.hasErrors()){
			return "configure_districtWise_Normal_load";
		}
     shippingOrderService.updateDistrictWiseNormalLoad(normalLoadConfig);
       model.addAttribute("update", "updateSuccess");
		return "configure_districtWise_Normal_load";
	}
	
	
	@RequestMapping(value = "/normalLoadConfiguration1", method = RequestMethod.POST,params="add")
	public ModelAndView addNormalLoadConfiguration1(
			@ModelAttribute("normalLoadConfig") @Validated NormalLoadConfiguration normalLoadConfig,BindingResult result,Model model) {
		System.out.println("add configuration");
		if(result.hasErrors()){
			//return "configure_districtWise_Normal_load";
		}
     shippingOrderService.saveDistrictWiseNormalLoad(normalLoadConfig);
       model.addAttribute("message", "success");
       
       List<DistrictWiseNormalLoadCapacity> dwnList = shippingOrderService.getAllDistrictWiseLoads();
		return new ModelAndView("districtWiseNormalLoadConfigureView", "configureViewData", dwnList);
       
		//return "configure_districtWise_Normal_load";
	}
	@RequestMapping(value = "/normalLoadConfiguration1", method = RequestMethod.POST,params="update")
	public ModelAndView updateNormalLoadConfiguration1(
			@ModelAttribute("normalLoadConfig") @Validated NormalLoadConfiguration normalLoadConfig,BindingResult result,Model model) {
		System.out.println("Update configuration");
		if(result.hasErrors()){
			//return "configure_districtWise_Normal_load";
		}
     shippingOrderService.updateDistrictWiseNormalLoad(normalLoadConfig);
       model.addAttribute("update", "updateSuccess");
       List<DistrictWiseNormalLoadCapacity> dwnList = shippingOrderService.getAllDistrictWiseLoads();
		return new ModelAndView("districtWiseNormalLoadConfigureView", "configureViewData", dwnList);
		//return "configure_districtWise_Normal_load";
	}
	
	@RequestMapping(value="/checkDistrictName")
	@ResponseBody
	public DistrictWiseNormalLoadCapacity isDistrictNameAvailable(HttpServletRequest req){
		String districtName = req.getParameter("districtName");
		DistrictWiseNormalLoadCapacity districtData = shippingOrderService.getDistrictTruckLoad(districtName);
		if(districtData != null){
			return districtData;
		}
		return new DistrictWiseNormalLoadCapacity();
	}
	@RequestMapping(value="/dwnlcView")
	public ModelAndView showDistrictWiseLoadConfigure() {
		List<DistrictWiseNormalLoadCapacity> dwnList = shippingOrderService.getAllDistrictWiseLoads();
		return new ModelAndView("districtWiseNormalLoadConfigureView", "configureViewData", dwnList);
	}

	
	@RequestMapping(value = "/axelWheelConfiguration", method = RequestMethod.GET)
	public ModelAndView showAxelWheelConfiguration(Model model) {
		 List<AxleWheelTypeEntity> listOfAxleWheller = shippingOrderService.getAllAxleWheelTypeEntity();
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		return new ModelAndView("axleWheelConfig", "axelWheelConfig",
				new NormalLoadConfiguration());
		/*return "configure_districtWise_Normal_load";*/
	}
	
	
	@RequestMapping(value = "/axelWheelConfiguration",method = RequestMethod.POST,params="addType")
	public String createWheelerType(
		@ModelAttribute("axleWheelConfig")  @Validated AxleWheelConfiguration axleWheelConfig,BindingResult result,Model model) {
			
		System.out.println("add configuration");

		String wheelType = axleWheelConfig.getAxlewheelertype();
		AxleWheelTypeEntity wheelData = shippingOrderService.getAxlewheel(wheelType);
		if(wheelData != null){
		model.addAttribute("showMessage", "Error");
		}
		else{
		 model.addAttribute("showMessage", "success");
		 shippingOrderService.saveAxleWheelConfiguration(axleWheelConfig);
		}
		 List<AxleWheelTypeEntity> listOfAxleWheller = shippingOrderService.getAllAxleWheelTypeEntity();
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		return "axleWheelConfig";
			
	}
	
		
	
	@RequestMapping(value = "axelWheelValue")
	@ResponseBody
	public List<AxleWheelnfoEntity> getDropdownValue(HttpServletRequest req){
		String name=req.getParameter("optionValue");
		  List<AxleWheelnfoEntity> infoList=shippingOrderService.getWheelTypeInfo(name);
		return infoList;

	}
	
	
	 @RequestMapping(value = "/uploadLoadConfig", method = RequestMethod.GET)
	   public ModelAndView uploadconfg() {
		  FileUploadBean file = new FileUploadBean();
	      ModelAndView modelAndView = new ModelAndView("upload", "command", file);
	      return modelAndView;
	   }
	
	 @RequestMapping(value="/uploadLoadConfig", method = RequestMethod.POST)
	  public String processHistory1(FileUploadBean mfile, ModelMap modelmap,Model model) throws IOException {
		  int countTruckDetailsFile=5;
		  int numberOfCells=0;
		  if(mfile.getFile().getSize() == 0)
		  {
			  model.addAttribute("showMessage", "select");
		  }
		  else{
		  File file = convertMultiPartFileIntoFile(mfile.getFile());
//			long fileSize = file.length();
			Workbook wb = getWorkBook(file);
			Sheet sheet = wb.getSheetAt(0);
	       //numberOfCells=sheet.getRow(0).getPhysicalNumberOfCells();
			int numberOfCells123=sheet.getRow(0).getPhysicalNumberOfCells();
			 numberOfCells=sheet.getRow(0).getLastCellNum();
			 String name = sheet.getSheetName();
				if (numberOfCells == countTruckDetailsFile) {
				dataMapper.readNormalLoad(wb);
				model.addAttribute("normalLoad", "success");
			} else {
				model.addAttribute("showMessage", "format");
			}
		  }
		  return "upload";
	  }
		


	@RequestMapping(value="/distClubOrdByPassConfig",method = RequestMethod.GET)
	public ModelAndView districtClubOrderByPassConfigure() {
		 List<DistrictClubOrdByPass> districtByPassList = shippingOrderService.getAllDistrictClubOrdByPass();
			return new ModelAndView("district_clubbing_order_bypass", "distByPass", districtByPassList);
	}
	@RequestMapping(value = "/distClubOrdByPassConfig", method = RequestMethod.POST)
	public ModelAndView districtClubOrderByPassConfigure(
			@ModelAttribute("districtByPass") @Validated DistrictClubOrdByPass districtByPass,BindingResult result,Model model,HttpServletRequest req) {
		System.out.println("Update configuration");
		String startDateStr = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
      shippingService.orderDistrictByPass(districtByPass, startDateStr, endDate);
       model.addAttribute("update", "updateSuccess");
       List<DistrictClubOrdByPass> districtByPassList = shippingOrderService.getAllDistrictClubOrdByPass();
		return new ModelAndView("district_clubbing_order_bypass", "distByPass", districtByPassList);
		//return "configure_districtWise_Normal_load";
	}
	@RequestMapping(value="/deleteDistrict/{id}")
	public String deleteOrderByPassDistrict(@PathVariable("id") Integer id) {
		shippingOrderService.deleteOrderByPassDistrict(id);
		return "redirect:/distClubOrdByPassConfig";
	}
	@RequestMapping(value = "/editDistClubOrdByPass", method = RequestMethod.POST)
	public ModelAndView editDistrictClubOrderByPassConfigure(
			@ModelAttribute("districtByPass") @Validated DistrictClubOrdByPass districtByPass,BindingResult result,Model model,HttpServletRequest req) {
		System.out.println("Update configuration");
		districtByPass.setId(Integer.parseInt(req.getParameter("distId1")));
		districtByPass.setDistrictName(req.getParameter("districtName1"));
		districtByPass.setDistrictCode(req.getParameter("districtCode1"));
		districtByPass.setStartDate(LocalDate.parse(req.getParameter("startDate1")));
		districtByPass.setEndDate(LocalDate.parse(req.getParameter("endDate1")));
      shippingService.orderDistrictByPass(districtByPass, req.getParameter("startDate1"), req.getParameter("endDate1"));
       model.addAttribute("update", "updateSuccess");
       List<DistrictClubOrdByPass> districtByPassList = shippingOrderService.getAllDistrictClubOrdByPass();
		return new ModelAndView("district_clubbing_order_bypass", "distByPass", districtByPassList);
		//return "configure_districtWise_Normal_load";
	}
	@GetMapping(value="/channelConfiguration")
	public String showChannelConfiguration() {
		return "channel_config";
	}
	private boolean isemptyValues(Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> finalTruckDetails) {
		for (Map.Entry<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> data : finalTruckDetails.entrySet()) {
			Map<List<ShippingDetails1>, List<AvailableTrucksModel>> vals = data.getValue();
			if (CollectionUtils.isEmpty(vals)) {
				return true;
			}
		}
		return false;
	}
   
	public List<ShippingDetails1> getAllOrdersBasedOnDistributionChannel(String distributionChannel) {
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllShippingOrders();
		List<ShippingDetails1> shippingaOrderListOnChannel = shippingaOrderList.stream()
				.filter(order -> order.getDistribution_channel().contains(distributionChannel))
				.collect(Collectors.toList());
		return shippingaOrderListOnChannel;
	}

	public Map<String, List<ShippingDetails1>> getAllOrdersBasedOnDistricts(List<ShippingDetails1> shippingOrderList) {
		Map<String, List<ShippingDetails1>> ordersOnDistrictMap = new HashMap<>();
        List<DistrictClubOrdByPass> distByPassList = shippingOrderService.getAllDistrictClubOrdByPass();
		for (ShippingDetails1 shippingDetails1 : shippingOrderList) {
			String districtName = shippingDetails1.getDistrict_name();
			if(shippingService.isDistrictByPass(distByPassList, districtName)) {
				continue;
			}
			List<ShippingDetails1> ordersList = ordersOnDistrictMap.get(districtName);
			if (ordersList != null) {
				ordersList.add(shippingDetails1);
				ordersOnDistrictMap.put(districtName, ordersList);
			} else {
				List<ShippingDetails1> ordList = new ArrayList<>();
				ordList.add(shippingDetails1);
				ordersOnDistrictMap.put(districtName, ordList);
			}
		}
		return ordersOnDistrictMap;
	}

	public Map<String, Map<String, List<ShippingDetails1>>> getAllOrdersBasedOnMaterial(
			Map<String, List<ShippingDetails1>> ordersOnDistrictMap) {
		Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap = new HashMap<>();
		for (Map.Entry<String, List<ShippingDetails1>> orders : ordersOnDistrictMap.entrySet()) {
			String districtName = orders.getKey();
			List<ShippingDetails1> ordersList = orders.getValue();
			Map<String, List<ShippingDetails1>> ordersOnMaterialsMap = new HashMap<>();
			for (ShippingDetails1 shippingDetails1 : ordersList) {
				String materialType = shippingDetails1.getMaterial();
				List<ShippingDetails1> materialOrdersList = ordersOnMaterialsMap.get(materialType);
				if (materialOrdersList != null) {
					materialOrdersList.add(shippingDetails1);
					ordersOnMaterialsMap.put(materialType, materialOrdersList);
				} else {
					List<ShippingDetails1> matrlOrdList = new ArrayList<>();
					matrlOrdList.add(shippingDetails1);
					ordersOnMaterialsMap.put(materialType, matrlOrdList);
				}
			}
			finalMaterialOrdMap.put(districtName, ordersOnMaterialsMap);
		}
		return finalMaterialOrdMap;
	}

	public Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> getOrdersFitIntoTruck1(Map<String, Map<String, List<ShippingDetails1>>> materialOrdGroupMap){
		//List<TruckDetails> initialTruckInfoList = shippingOrder.getAllTruckInfo();
		List<ShippingDetails1> unGroupOrderList = new ArrayList<>();
		List<AvailableTrucksModel> allAssignedTrucksList = new ArrayList<>(); 
		Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> finalTruckDetails = new HashMap<>();
		for (Map.Entry<String, Map<String, List<ShippingDetails1>>> ordGrpList : materialOrdGroupMap.entrySet()) {
			String districtName = ordGrpList.getKey();
			Map<String, List<ShippingDetails1>> vals = ordGrpList.getValue();
			Map<List<ShippingDetails1>, List<AvailableTrucksModel>> truckAndOrderMap = new HashMap<>();
			for (Map.Entry<String, List<ShippingDetails1>> ordList : vals.entrySet()) {
				List<AvailableTrucksModel> trucksList = new ArrayList<>();
				String materialName = ordList.getKey();
				List<ShippingDetails1> ordsList = ordList.getValue();
				if(ordsList.size() == 1){//if single order contain for same material ,no need to group the that product
					unGroupOrderList.add(ordsList.get(0));
					continue;
				}
				String truckType = TruckTypeInfo.getTruckLoadType(districtName);
				int totOrdQty = ordsList.stream().map(ShippingDetails1::getActual_delivery_qty)
						.mapToInt(Integer::valueOf).sum();
				if("Minimum".equals(truckType)){
					trucksList = getNormalTruckList(trucksList, totOrdQty,allAssignedTrucksList);
					allAssignedTrucksList.addAll(trucksList);
				} else{//maximum Extra
					trucksList = getHeavyTruckList(trucksList, totOrdQty,allAssignedTrucksList);
					allAssignedTrucksList.addAll(trucksList);
				}
				truckAndOrderMap.put(ordsList, trucksList);
			}
			finalTruckDetails.put(districtName, truckAndOrderMap);
		}
		return finalTruckDetails;
	}
	
	public Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> getOrdersFitIntoTruck(
			Map<String, Map<String, Map<Integer, List<ShippingDetails1>>>> groupByDistance) {
		// List<TruckDetails> initialTruckInfoList =
		// shippingOrder.getAllTruckInfo();
		List<ShippingDetails1> unGroupOrderList = new ArrayList<>();
		List<AvailableTrucksModel> allAssignedTrucksList = new ArrayList<>();
		Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> finalTruckDetails = new HashMap<>();
		for (Map.Entry<String, Map<String, Map<Integer, List<ShippingDetails1>>>> ordGrpList : groupByDistance.entrySet()) {
			String districtName = ordGrpList.getKey();
			Map<String, Map<Integer, List<ShippingDetails1>>> vals = ordGrpList.getValue();
			Map<List<ShippingDetails1>, List<AvailableTrucksModel>> truckAndOrderMap = new HashMap<>();
			for (Map.Entry<String, Map<Integer, List<ShippingDetails1>>> distanceByGroup : vals.entrySet()) {
				String materialName = distanceByGroup.getKey();
				Map<Integer, List<ShippingDetails1>> vals1 = distanceByGroup.getValue();
				for (Map.Entry<Integer, List<ShippingDetails1>> ordList : vals1.entrySet()) {
					List<AvailableTrucksModel> trucksList = new ArrayList<>();
					Integer orderDistance = ordList.getKey();
					List<ShippingDetails1> ordsList = ordList.getValue();
					/*if (ordsList.size() == 1) {// if single order contain for same
												// material ,no need to group the
												// that product
						unGroupOrderList.add(ordsList.get(0));
						continue;
					}*/
					String truckType = TruckTypeInfo.getTruckLoadType(districtName);
					int totOrdQty = ordsList.stream().map(ShippingDetails1::getActual_delivery_qty)
							.mapToInt(Integer::valueOf).sum();
					if ("Minimum".equals(truckType)) {
						trucksList = getNormalTruckList(trucksList, totOrdQty, allAssignedTrucksList);
						allAssignedTrucksList.addAll(trucksList);
					} else {// maximum Extra
						trucksList = getHeavyTruckList(trucksList, totOrdQty, allAssignedTrucksList);
						allAssignedTrucksList.addAll(trucksList);
					}
					truckAndOrderMap.put(ordsList, trucksList);
				}	
			}
			//Map<List<ShippingDetails1>, List<TruckDetails>> truckAndOrderMap = new HashMap<>();
			
			finalTruckDetails.put(districtName, truckAndOrderMap);
		}
		return finalTruckDetails;
	}

	private List<AvailableTrucksModel> getNormalTruckList(List<AvailableTrucksModel> groupTruckList, int orderQty,
			List<AvailableTrucksModel> allAssignedTrucksList) {
		List<AvailableTrucksModel> initialTruckInfoList = shippingOrderService.getAllTruckInfo();
		AvailableTrucksModel maxTruckDetails = initialTruckInfoList.stream()
				.max(Comparator.comparing(AvailableTrucksModel::getVehicleType)).orElseThrow(NoSuchElementException::new);
		int truckMaxCapacity = maxTruckDetails.getVehicleType();
		if (orderQty > truckMaxCapacity) {// check order qty with max truck
											// capacity
			if (orderQty != truckMaxCapacity) {
				maxTruckDetails = getOrderTruck(initialTruckInfoList, maxTruckDetails, allAssignedTrucksList);
				allAssignedTrucksList.add(maxTruckDetails);
				groupTruckList.add(maxTruckDetails);
				truckMaxCapacity = maxTruckDetails.getVehicleType();
				int remainingOrderQty = orderQty - truckMaxCapacity;
				if (remainingOrderQty < 2) {
					return groupTruckList;
				}
				if (remainingOrderQty > truckMaxCapacity) {
					getNormalTruckList(groupTruckList, remainingOrderQty, allAssignedTrucksList);
				} else {// l
					AvailableTrucksModel truckDtls = getTruckDetails(initialTruckInfoList, remainingOrderQty, groupTruckList,
							allAssignedTrucksList);
					if (truckDtls != null) {
						allAssignedTrucksList.add(truckDtls);
						groupTruckList.add(truckDtls);
					} else {
						List<Integer> trucksCapacity = initialTruckInfoList.stream()
								.map(truck -> truck.getVehicleType()).collect(Collectors.toList());
						int capacityNo = trucksCapacity.stream()
								.min(Comparator.comparingInt(i -> Math.abs(i - remainingOrderQty)))
								.orElseThrow(() -> new NoSuchElementException("No value present"));
						truckDtls = getTruckDetails(initialTruckInfoList, capacityNo, groupTruckList,
								allAssignedTrucksList);
						if (truckDtls != null) {
							allAssignedTrucksList.add(truckDtls);
							groupTruckList.add(truckDtls);
						}
					}
					/*
					 * for (TruckDetails truckDetails : initialTruckInfoList) {
					 * if(truckDetails.getVehicleType() == remainingOrderQty){
					 * if(!isTruckGroup(groupTruckList,truckDetails.getSlNo())){
					 * truckDetails = getOrderTruck(initialTruckInfoList,
					 * truckDetails, allAssignedTrucksList);
					 * allAssignedTrucksList.add(truckDetails);
					 * groupTruckList.add(truckDetails); break; } } else {
					 * getfinalTruckList(groupTruckList, remainingOrderQty); } }
					 */

				}
			} else {// if allOrderQty and truck capacity is same

			}
		} else {// if orser qty is low compare to truck max capacity
			AvailableTrucksModel tDetails = Collections.min(initialTruckInfoList,
					Comparator.comparing(AvailableTrucksModel::getVehicleType));
			tDetails = getOrderTruck(initialTruckInfoList, tDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(tDetails);
			groupTruckList.add(tDetails);

		}
		return groupTruckList;
	}

	private List<AvailableTrucksModel> getHeavyTruckList(List<AvailableTrucksModel> groupTruckList, int totOrdQty,
			List<AvailableTrucksModel> allAssignedTrucksList) {
		List<AvailableTrucksModel> initialTruckInfoList = shippingOrderService.getAllTruckInfo();
		double initialTruckCap = totOrdQty - (totOrdQty * 0.5);
		AvailableTrucksModel initialTruckDetails = getTruckDetails(initialTruckInfoList, initialTruckCap);

		if (initialTruckDetails != null) {// if order qty is equal to truck
											// capacity
			initialTruckDetails = getOrderHeavyTruck(initialTruckInfoList, initialTruckDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(initialTruckDetails);
			groupTruckList.add(initialTruckDetails);
			return groupTruckList;
		}
		AvailableTrucksModel tDetails = Collections.max(initialTruckInfoList,
				Comparator.comparing(AvailableTrucksModel::getVehicleType));// maximum
																	// truck
																	// details
		int truckMaxCapacity = tDetails.getVehicleType();
		double truckCarryCapacity = truckMaxCapacity + (truckMaxCapacity * 0.5);
		if (totOrdQty > truckCarryCapacity) {// order qty is greater than truck
												// capacity then orders into
												// split to assign another truck
			tDetails = getOrderHeavyTruck(initialTruckInfoList, tDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(tDetails);
			groupTruckList.add(tDetails);
			double remainingOrdQty = Double.valueOf(totOrdQty) - truckCarryCapacity;
			groupTruckList = getHeavyGroupTruckDetails(initialTruckInfoList, remainingOrdQty, groupTruckList,
					allAssignedTrucksList);

		} else {

		}
		return groupTruckList;
	}

	private AvailableTrucksModel getTruckDetails(List<AvailableTrucksModel> truckInfoList, double qty) {
		for (AvailableTrucksModel truckDetails : truckInfoList) {
			int truckCapacity = truckDetails.getVehicleType();
			double truckmaxCapacity = truckCapacity + (truckCapacity * 0.5);
			if (truckmaxCapacity == qty) {
				return truckDetails;
			}
		}
		return null;
	}

	private AvailableTrucksModel getTruckDetails(List<AvailableTrucksModel> initialTruckInfoList, int orderQty,
			List<AvailableTrucksModel> groupTruckList, List<AvailableTrucksModel> allAssignedTrucksList) {

		for (AvailableTrucksModel truckDetails : initialTruckInfoList) {
			if (truckDetails.getVehicleType() == orderQty) {
				if (!isTruckGroup(groupTruckList, truckDetails.getSlNo())) {
					truckDetails = getOrderTruck(initialTruckInfoList, truckDetails, allAssignedTrucksList);
					return truckDetails;
				}
			}
		}
		return null;
	}

	private AvailableTrucksModel getHeavyTruckDetails(List<AvailableTrucksModel> truckInfoList, double qty) {

		for (AvailableTrucksModel truckDetails : truckInfoList) {
			if (Double.valueOf(truckDetails.getVehicleType()) == qty) {
				return truckDetails;
			}
		}

		return null;
	}

	private AvailableTrucksModel getOrderTruck(List<AvailableTrucksModel> allTrucksList, AvailableTrucksModel truckDetails,
			List<AvailableTrucksModel> assignedTruckList) {
		/*
		 * if(CollectionUtils.isEmpty(assignedTruckList)){ return truckDetails;
		 * }
		 */
		String truckNo = truckDetails.getSlNo();
		int truckCapacity = truckDetails.getVehicleType();
		String allTruckNos = assignedTruckList.stream().map(AvailableTrucksModel::getSlNo).collect(Collectors.joining(","));
		if (!allTruckNos.contains(truckNo)) {
			return truckDetails;
		} else {
			for (AvailableTrucksModel truckDtls : allTrucksList) {
				if (truckDtls.getVehicleType() == truckCapacity && !allTruckNos.contains(truckDtls.getSlNo())) {
					return truckDtls;
				}
			}
		}
		return truckDetails;
	}

	private AvailableTrucksModel getOrderHeavyTruck(List<AvailableTrucksModel> allTrucksList, AvailableTrucksModel truckDetails,
			List<AvailableTrucksModel> assignedTruckList) {
		/*
		 * if(CollectionUtils.isEmpty(assignedTruckList)){ return truckDetails;
		 * }
		 */
		String truckNo = truckDetails.getSlNo();
		int truckCapacity = truckDetails.getVehicleType();
		double maxCapacity = truckCapacity + (truckCapacity * 0.5);
		truckDetails.setVehicleType((int) maxCapacity);
		String allTruckNos = assignedTruckList.stream().map(AvailableTrucksModel::getSlNo).collect(Collectors.joining(","));
		if (!allTruckNos.contains(truckNo)) {
			return truckDetails;
		} else {
			for (AvailableTrucksModel truckDtls : allTrucksList) {
				if (truckDtls.getVehicleType() == truckCapacity && !allTruckNos.contains(truckDtls.getSlNo())) {
					truckDtls.setVehicleType((int) maxCapacity);
					return truckDtls;
				}
			}
		}
		return truckDetails;
	}

	private List<AvailableTrucksModel> getHeavyGroupTruckDetails(List<AvailableTrucksModel> truckInfoList, double qty,
			List<AvailableTrucksModel> truckGroupList, List<AvailableTrucksModel> allAssignedTrucksList) {
		if (qty < 8) {
			return truckGroupList;
		}
		AvailableTrucksModel maxTruckDetails = null;
		if (qty == 18.0) {
			maxTruckDetails = getMaxCapacityTruckDetails("", 12, truckInfoList);
		} else {
			maxTruckDetails = truckInfoList.stream().max(Comparator.comparing(AvailableTrucksModel::getVehicleType))
					.orElseThrow(NoSuchElementException::new);
		}

		int truckCapacity = maxTruckDetails.getVehicleType();
		if (truckCapacity == 27) {
			truckCapacity = 18;
		}
		String assignedTruckNo = allAssignedTrucksList.stream().map(AvailableTrucksModel::getSlNo)
				.collect(Collectors.joining(","));
		if (assignedTruckNo.contains(maxTruckDetails.getSlNo())) {
			maxTruckDetails = getMaxCapacityTruckDetails(assignedTruckNo, truckCapacity, truckInfoList);
		}
		double maxLoadcapacity = truckCapacity + (truckCapacity * 0.5);
		if (qty > maxLoadcapacity) {
			maxTruckDetails = getOrderHeavyTruck(truckInfoList, maxTruckDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(maxTruckDetails);
			truckGroupList.add(maxTruckDetails);
			double remainingQty = qty - maxLoadcapacity;
			getHeavyGroupTruckDetails(truckInfoList, remainingQty, truckGroupList, allAssignedTrucksList);
		} else if (qty == maxLoadcapacity) {
			maxTruckDetails.setVehicleType((int) maxLoadcapacity);
			allAssignedTrucksList.add(maxTruckDetails);
			truckGroupList.add(maxTruckDetails);
		} else {
			double truckCap = qty - (qty * 0.5);
			AvailableTrucksModel tt = truckInfoList.stream()
					.reduce((result, current) -> Math.abs(truckCap - current.getVehicleType()) < Math
							.abs(truckCap - result.getVehicleType()) ? current : result)
					.get();
			int cap = tt.getVehicleType();
			double maxTruckLoad = cap + (cap * 0.5);
			double remCap = qty - maxTruckLoad;
			if (remCap > 8) {
				truckGroupList.add(tt);
				allAssignedTrucksList.add(tt);
				getHeavyGroupTruckDetails(truckInfoList, remCap, truckGroupList, allAssignedTrucksList);
			} else {
				truckGroupList.add(tt);
			}

		}
		return truckGroupList;
	}

	private AvailableTrucksModel getMaxCapacityTruckDetails(String allAssignedTrucks, int truckCapacity,
			List<AvailableTrucksModel> truckDetails) {
		for (AvailableTrucksModel truckDetails2 : truckDetails) {
			if (allAssignedTrucks.contains(truckDetails2.getSlNo())) {
				continue;
			} else {
				if (truckDetails2.getVehicleType() == truckCapacity) {
					return truckDetails2;
				}
			}
		}
		return null;
	}

	private void getFinalOrdersClub(Map<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> finalTruckDetails) {
	List<String> trucksNoAssigned = new ArrayList<>();
		for (Map.Entry<String, Map<List<ShippingDetails1>, List<AvailableTrucksModel>>> data : finalTruckDetails.entrySet()) {
			String districtName = data.getKey();
			Map<List<ShippingDetails1>, List<AvailableTrucksModel>> ords = data.getValue();
			orderGroupByTruck1(ords,trucksNoAssigned);
		}
	}

	private void orderGroupByTruck1(Map<List<ShippingDetails1>, List<AvailableTrucksModel>> groupTruckMap,List<String> trucksNoAssigned) {
		for (Map.Entry<List<ShippingDetails1>, List<AvailableTrucksModel>> allDetails : groupTruckMap.entrySet()) {
			List<ShippingDetails1> ordersList = allDetails.getKey();
			// List<ShippingDetails1> ordersList =
			// intellship.getOrderDetailsList();
			// ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).
			int allOrdsQtys = getAllOrdersQty(ordersList);
			List<AvailableTrucksModel> groupTruckDetails = allDetails.getValue();
			OrderGroup orderGroup = null;
			List<OrderGroup> orderGroupList = new ArrayList<>();
			Map<String, List<OrderGroup>> ordersMap = new HashMap<>();
			int totTrucks = groupTruckDetails.size();
			int trucksCount = 1;
			Map<String, List<OrderGroup>> ordersTruckMap = new HashMap<>();
			Date todaysDate = new Date();
			ShippingDeliveryOrder shippingDeliveryOrder = null; 
			for (AvailableTrucksModel truckDetails : groupTruckDetails) {
				shippingDeliveryOrder = new ShippingDeliveryOrder();
				int initialOrder = 1;
				int truckCapacity = truckDetails.getVehicleType();
				int ordersQtyTruck = 0;
				shippingDeliveryOrder.setTruckNo(truckDetails.getSlNo());
				shippingDeliveryOrder.setShippingDeliveryDate(todaysDate);
				int shippingDelivaryId = shippingOrderService.generateShippingOrderId(shippingDeliveryOrder);
				for (ShippingDetails1 orderDetails : ordersList) {
					orderGroup = new OrderGroup();
					int orderQty = Integer.parseInt(orderDetails.getActual_delivery_qty());
					if (isFullTruck(ordersTruckMap, truckCapacity, truckDetails.getSlNo())) {
						break;
					}
					if (isOrderQtyTruck(ordersMap, orderQty, orderDetails.getDelivery())) {// check
																							// is
																							// order
																							// load
																							// in
																							// truck
																							// or
																							// not,if
																							// order
																							// is
																							// loaded
																							// then
																							// skip
																							// those
																							// order
						continue;
					}
					int remaingTruckCapacity = 0;
					if (initialOrder != 1) {
						int truckOrdQty = truckCapacity - ordersQtyTruck;
						if (truckOrdQty < orderQty) {
							remaingTruckCapacity = truckOrdQty;
						} else {
							remaingTruckCapacity = orderQty;
						}
					}
					if (ordersMap.containsKey(orderDetails.getDelivery())) {
						remaingTruckCapacity = getRemainingOrderqQty(ordersMap, orderDetails.getDelivery());
						if (trucksCount != totTrucks && isLessOrderQty(ordersList, ordersMap, remaingTruckCapacity)) {
							continue;
						}
						if (initialOrder == 1) {
							orderQty = remaingTruckCapacity;
						}
					}
					orderGroup.setDelivaryNo(orderDetails.getDelivery());
					orderGroup.setOriginalOrderQty(orderDetails.getActual_delivery_qty());
					orderGroup.setTruckNo(truckDetails.getSlNo());
					orderGroup.setTruckCapacity(String.valueOf(truckDetails.getVehicleType()));
					orderGroup.setMaterialType(orderDetails.getMaterial());
					orderGroup.setDistrictName(orderDetails.getDistrict_name());
					orderGroup.setLatitude(orderDetails.getShip_to_latt());
					orderGroup.setLongitude(orderDetails.getShip_to_long());
					orderGroup.setNameShipToParty(orderDetails.getName_of_sold_to_party());
					orderGroup.setShippingDelivaryId(shippingDelivaryId);
					// shipping order Date
					Date todayDate = new Date();
					// LocalDate date = new date
					orderGroup.setOrderShippingDate(todayDate);
					LocalDateTime currentTime = LocalDateTime.now();
					LocalDate date = currentTime.toLocalDate();
					// orderGroup.setOrderShippingDate(date);
					// orderGroup.setOrderShippingDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
					if (initialOrder == 1) {
						if (orderQty > truckCapacity) {
							orderQty = truckCapacity;
						}
						orderGroup.setTruckOrderQty(orderQty);
						ordersQtyTruck = orderQty;
					} else {
						orderGroup.setTruckOrderQty(remaingTruckCapacity);
						ordersQtyTruck = ordersQtyTruck + remaingTruckCapacity;
					}
					orderGroup.setDelivaryDate(orderDetails.getDeliv_date());
					// orderGroupList.add(orderGroup);
					if (ordersMap.containsKey(orderDetails.getDelivery())) {
						List<OrderGroup> orderGroupsList = ordersMap.get(orderDetails.getDelivery());
						orderGroupsList.add(orderGroup);
						ordersMap.put(orderDetails.getDelivery(), orderGroupsList);
					} else {
						List<OrderGroup> orders = new ArrayList<>();
						orders.add(orderGroup);
						ordersMap.put(orderDetails.getDelivery(), orders);
					}
					// for checking trucks data
					if (ordersTruckMap.containsKey(orderGroup.getTruckNo())) {
						List<OrderGroup> orderGroupsTruckList = ordersTruckMap.get(orderGroup.getTruckNo());
						orderGroupsTruckList.add(orderGroup);
						ordersTruckMap.put(orderGroup.getTruckNo(), orderGroupsTruckList);
					} else {
						List<OrderGroup> ordersTruck = new ArrayList<>();
						ordersTruck.add(orderGroup);
						ordersTruckMap.put(orderGroup.getTruckNo(), ordersTruck);
					}

					initialOrder++;
					if (ordersQtyTruck == truckCapacity
							|| isOrderQtyTruck(ordersMap, allOrdsQtys, orderDetails.getDelivery())) {// truck
																										// is
																										// full
																										// with
																										// orders
						break;
					}
				}
				ordersTruckMap = new HashMap<>();
				// orderGroupList = new ArrayList<>();
				trucksCount++;
			} // end truck for loop
				// check all orders fit into truck or not

			for (Map.Entry<String, List<OrderGroup>> ords : ordersMap.entrySet()) {
				List<OrderGroup> ordGroup = ords.getValue();
				for (OrderGroup orderGroup2 : ordGroup) {
					shippingOrderService.saveOrderGroup(orderGroup2);
					shippingOrderService.updateOrderGroupFlag(orderGroup2.getDelivaryNo());
				}
			}
		}
	}

	private boolean isLessOrderQty(List<ShippingDetails1> orderDetails, Map<String, List<OrderGroup>> ordsMap,
			int remainingOrder) {
		for (ShippingDetails1 shippingDetails1 : orderDetails) {
			if (!ordsMap.containsKey(shippingDetails1.getDelivery())) {
				int orderQty = Integer.parseInt(shippingDetails1.getActual_delivery_qty());
				if (orderQty > remainingOrder) {
					return true;
				}
			}
		}
		return false;
	}

	public List<IntellishipModelByMaterial> getFinalGroupOrders() {
		MapService gmapDist = new MapService();
		StoreSpDetails sd = new StoreSpDetails();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		PlantDetails plantDetails = plantDetailsList.get(2);
		List<OrderGroup> orderGroupList = shippingOrderService.getAllGroupOrderList();
		List<IntellishipModelByMaterial> finalIntelishipModel = new ArrayList<>();
		Map<String, List<OrderGroup>> groupBasedOnTruck = new HashMap<>();
		for (OrderGroup orderGroup : orderGroupList) {
			String truckNo = orderGroup.getTruckNo();
			List<OrderGroup> ordGroList = groupBasedOnTruck.get(truckNo);
			if (ordGroList != null) {
				ordGroList.add(orderGroup);
				groupBasedOnTruck.put(truckNo, ordGroList);
			} else {
				List<OrderGroup> ordGroupList = new ArrayList<>();
				ordGroupList.add(orderGroup);
				groupBasedOnTruck.put(truckNo, ordGroupList);
			}
		}
		IntellishipModelByMaterial intellishModel = null;
		Map<String, OrderGroup> pendingOrderMap = new HashMap<>();
		int shippingStatusCount = 1;
		for (Map.Entry<String, List<OrderGroup>> groupList : groupBasedOnTruck.entrySet()) {
			intellishModel = new IntellishipModelByMaterial();
			String truckNo = groupList.getKey();
			List<OrderGroup> orderGrpList = groupList.getValue();
			StringBuilder shippingLatitudeAndLonitude = new StringBuilder();
			double distence = 0.0;
			String duration = "";
			int totalOrdQty = 0;
			int ordQty = 0;
			int truckCapacity = 0;
			for (OrderGroup orderGrup : orderGrpList) {
				shippingLatitudeAndLonitude.append(orderGrup.getLatitude()).append(",")
						.append(orderGrup.getLongitude());
				shippingLatitudeAndLonitude.append("|");
				/*
				 * int pendingQty =
				 * getPendingOrderQuantity(orderGrup.getOriginalOrderQty(),
				 * orderGrup.getTruckCapacity(), orderGrup.getTruckOrderQty());
				 * if(pendingQty != 0){
				 * intellishModel.setPendingQuantity(pendingQty); }
				 */
				OrderGroup pendingOrder = pendingOrderMap.get(orderGrup.getDelivaryNo());
				if (pendingOrder != null) {
					ordQty = pendingOrder.getTruckOrderQty();
				}
				totalOrdQty = totalOrdQty + Integer.parseInt(orderGrup.getOriginalOrderQty());
				intellishModel.setLoadType(TruckTypeInfo.getLoadType(orderGrup.getDistrictName()));
				intellishModel.setMaterialType(orderGrup.getMaterialType());
				String truckType = TruckTypeInfo.getTruckLoadType(orderGrup.getDistrictName());
				if ("Minimum".equals(truckType)) {
					intellishModel.setTruckCapacity(orderGrup.getTruckCapacity());
				} else {
					int truckCap = Integer.parseInt(orderGrup.getTruckCapacity());
					int originalTruck = 0;
					if (truckCap == 27) {
						originalTruck = 18;
					} else if (truckCap == 18) {
						originalTruck = 12;
					} else {
						originalTruck = truckCap;
					}
					String finalTruckCap = originalTruck + "(" + truckCap + ")";
					intellishModel.setTruckCapacity(finalTruckCap);
				}

				intellishModel.setDistrictName(orderGrup.getDistrictName());
				truckCapacity = Integer.parseInt(orderGrup.getTruckCapacity());
				pendingOrderMap.put(orderGrup.getDelivaryNo(), orderGrup);
			}
			int pedningQty = totalOrdQty - truckCapacity;
			if (ordQty != 0) {
				pedningQty = pedningQty - ordQty;
				totalOrdQty = totalOrdQty - ordQty;
			}
			try {
				String distenceAndHrs = gmapDist.getMaxDistenceAndHrsFromMultipleDestination(
						plantDetails.getLatitude() + "," + plantDetails.getLongitude(),
						shippingLatitudeAndLonitude.toString());
				String[] data = distenceAndHrs.split("###");
				distence = Double.parseDouble(data[0]);
				duration = data[1];

			} catch (IOException e) {
				System.out.println("Unbale to calculate distence :" + e.getCause());
				e.printStackTrace();
			}

			/*
			 * if(districName.equals("GODDA")){ distence = 295.2; } else {
			 * distence = 342.6; }
			 */
			intellishModel.setTruckNo(truckNo);
			intellishModel.setTotalKilometers(String.valueOf(distence));
			intellishModel.setTotalOrders(orderGrpList.size());
			intellishModel.setTotalOrderQuantity(totalOrdQty);
			intellishModel.setPlant(plantDetails.getPlantName());
			intellishModel.setPendingQuantity(pedningQty);
			//intellishModel.setShippingStatus(getShippingStatus(shippingStatusCount));
			intellishModel.setEstimationTime(duration);
			finalIntelishipModel.add(intellishModel);
			shippingStatusCount++;
		}
		return finalIntelishipModel;
	}

	private String getShippingStatus(int shippingStaNo) {
		String status = "";
		if (shippingStaNo == 1) {
			status = "In Transit";
		} else if (shippingStaNo == 2) {
			status = "Shipment Delivered";
		} else if (shippingStaNo == 3) {
			status = "Shipment picked up";
		} else {
			status = "In Transit";
		}
		return status;
	}

	private int getPendingOrderQuantity(String originalOrderQty, String truckCapacity, int orderQtyInTruck) {
		int remainingQty = Integer.parseInt(originalOrderQty) - orderQtyInTruck;
		return remainingQty;
	}

	private int getPendingOrderQuantity(List<OrderGroup> ordGroupList) {
		for (OrderGroup orderGroup : ordGroupList) {
			int truckCapacity = Integer.parseInt(orderGroup.getTruckCapacity());
			int orderQtyInTruck = orderGroup.getTruckOrderQty();
			int originalOrdQty = Integer.parseInt(orderGroup.getOriginalOrderQty());
			int remainingQty = originalOrdQty - orderQtyInTruck;

		}

		return 0;
	}

	private boolean isTruckGroup1(List<AvailableTrucksModel> truckList, String truckNo) {
		String allTruckNos = truckList.stream().map(AvailableTrucksModel::getSlNo).collect(Collectors.joining(","));
		if (allTruckNos.contains(truckNo)) {
			return true;
		}
		return false;
	}

	public List<IntellishipModel> getGroupOrders() {
		StoreSpDetails sd = new StoreSpDetails();
		MapService gmapDist = new MapService();
		List<ShippingDetails1> shippingaOrderList = shippingOrderService.getAllShippingOrders();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		Map<String, ShippingDetails> shippingDetailsMap = sd.getAllShippingDetailsMap();
		Map<Double, String> distences = new LinkedHashMap<>();
		double initialDistence = 0.0;
		Map<String, StringBuilder> shortDist = new HashMap<>();
		String plantLangitude = "";
		String plantLatitude = "";
		int intitialDist = 1;
		Map<Long, List<ShippingDetails1>> groupBasedOnDays = new HashMap<>();
		PlantDetails plantDetails = plantDetailsList.get(2);
		for (ShippingDetails1 shippingDetail : shippingaOrderList) {
			String date = shippingDetail.getDeliv_date();

			LocalDate delivaryDate = LocalDate.parse(date);
			Long noOfdays = ChronoUnit.DAYS.between(LocalDate.now(), delivaryDate);
			List<ShippingDetails1> shippingList = groupBasedOnDays.get(noOfdays);
			if (shippingList == null) {
				List<ShippingDetails1> initialList = new ArrayList<>();
				initialList.add(shippingDetail);
				groupBasedOnDays.put(noOfdays, initialList);
			} else {
				shippingList.add(shippingDetail);
				groupBasedOnDays.put(noOfdays, shippingList);
			}
		}
		Map<Long, List<ShippingDetails1>> afterSoringGroup = new HashMap<>();
		for (Map.Entry<Long, List<ShippingDetails1>> data : groupBasedOnDays.entrySet()) {
			Collections.sort(data.getValue(), new Comparator<ShippingDetails1>() {

				@Override
				public int compare(ShippingDetails1 o1, ShippingDetails1 o2) {
					return Double.compare(Double.parseDouble(o1.getShip_to_latt()),
							Double.parseDouble(o2.getShip_to_latt()));
				}
			});
			afterSoringGroup.put(data.getKey(), data.getValue());
		}
		List<IntellishipModel> intellishipList = new ArrayList<>();
		IntellishipModel intellishipObj = null;
		int orderNo = 1;
		for (Map.Entry<Long, List<ShippingDetails1>> details : afterSoringGroup.entrySet()) {
			intellishipObj = new IntellishipModel();
			Long delivaryDueDays = details.getKey();
			List<ShippingDetails1> shippingOrderList = details.getValue();
			StringBuilder shippingLatitudeAndLonitude = new StringBuilder();
			double distence = 0.0;
			String delivaryDate = "";
			for (ShippingDetails1 shippingDetails1 : shippingOrderList) {
				delivaryDate = shippingDetails1.getDeliv_date();
				shippingLatitudeAndLonitude.append(shippingDetails1.getShip_to_latt()).append(",")
						.append(shippingDetails1.getShip_to_long());
				shippingLatitudeAndLonitude.append("|");
			}
			try {
				distence = gmapDist.getMaxDistenceFromMultipleDestination(
						plantDetails.getLatitude() + "," + plantDetails.getLongitude(),
						shippingLatitudeAndLonitude.toString());
			} catch (IOException e) {
				System.out.println("Unbale to calculate distence :" + e.getCause());
				e.printStackTrace();
			}
			intellishipObj.setDelivaryDate(delivaryDate);
			intellishipObj.setTotalOrders(shippingOrderList.size());
			if (orderNo == 1) {
				intellishipObj.setTruckNum("JH02AE4348");
				intellishipObj.setTruckCapacity("10");
			} else if (orderNo == 2) {
				intellishipObj.setTruckNum("JH09AL3681");
				intellishipObj.setTruckCapacity("12");
			} else if (orderNo == 3) {
				intellishipObj.setTruckNum("JH09AH6721");
				intellishipObj.setTruckCapacity("14");
			} else if (orderNo == 4) {
				intellishipObj.setTruckNum("JH09AD4244");
				intellishipObj.setTruckCapacity("12");
			} else if (orderNo == 5) {
				intellishipObj.setTruckNum("JH09T5702");
				intellishipObj.setTruckCapacity("12");
			} else {
				intellishipObj.setTruckNum("JH11E9261");
				intellishipObj.setTruckCapacity("10");
			}
			intellishipObj.setPlant(plantDetails.getPlantName());
			intellishipObj.setTotalKilometers(String.valueOf(distence));
			intellishipObj.setOrderDetailsList(shippingOrderList);
			intellishipList.add(intellishipObj);
			orderNo++;
		}

		/*
		 * System.out.println("After sorting"); for (Map.Entry<Long,
		 * List<ShippingDetails1>> details : afterSoringGroup.entrySet()) { for
		 * (ShippingDetails1 shippingDetils : details.getValue()) {
		 * System.out.println("No Delivary Due Days: " + details.getKey() +
		 * "Shipping details:" + shippingDetils.getShip_to_latt() +
		 * ":Longitude:  " + shippingDetils.getShip_to_long()); }
		 * //System.out.println("Delivary Time Day due:"+details.getKey()+
		 * "##"+"NoOfOrders: "+details.getValue().size()); }
		 * 
		 * StringBuilder allData = new StringBuilder();
		 * allData.append("All Plant Distence from Receptive Order: ").append(
		 * "\n"); for (Map.Entry<Double, String> dist: distences.entrySet()) {
		 * System.out.println(dist.getValue()+"::"+dist.getKey());
		 * allData.append(dist.getValue()).append("::").append(dist.getKey());
		 * allData.append("\n"); } allData.append("\n");
		 * System.out.println("#####SHORTEST DISTENCE FROM PLANT#####");
		 * allData.append("#####SHORTEST DISTENCE FROM PLANT#####");
		 * allData.append("\n"); for (Map.Entry<String, StringBuilder> dist :
		 * shortDist.entrySet()) {
		 * System.out.println("Order No: "+dist.getKey()+
		 * "::"+"Shortest Distence:"+dist.getValue());
		 * allData.append("Order No: "+dist.getKey()).append("::").
		 * append("Shortest Distence:"+dist.getValue()); allData.append("\n"); }
		 */
		getGroupByOrderQty(intellishipList);
		List<IntellishipModel> newIntelliList = getTrucksCount(intellishipList);
		return newIntelliList;

	}

	private void getGroupByOrderQty(List<IntellishipModel> data) {
		// List<TruckDetails> truckInfoList = shippingOrder.getAllTruckInfo();
		Map<IntellishipModel, List<AvailableTrucksModel>> groupTruckMap = new HashMap<>();
		for (IntellishipModel intellishipModel : data) {
			List<ShippingDetails1> ordersList = intellishipModel.getOrderDetailsList();
			List<AvailableTrucksModel> groupTruckList = new ArrayList<>();
			int allOrderQty = 0;
			for (ShippingDetails1 shippingDetails1 : ordersList) {
				allOrderQty = allOrderQty + Integer.parseInt(shippingDetails1.getActual_delivery_qty());
			}
			List<AvailableTrucksModel> groupTruckDetails = getfinalTruckList(groupTruckList, allOrderQty);
			groupTruckMap.put(intellishipModel, groupTruckDetails);
		}
		orderGroupByTruck(groupTruckMap);
		// split tru1cks based on order quantity
	}

	private List<AvailableTrucksModel> getfinalTruckList(List<AvailableTrucksModel> groupTruckList, int orderQty) {
		List<AvailableTrucksModel> initialTruckInfoList = shippingOrderService.getAllTruckInfo();
		AvailableTrucksModel maxTruckDetails = initialTruckInfoList.stream()
				.max(Comparator.comparing(AvailableTrucksModel::getVehicleType)).orElseThrow(NoSuchElementException::new);
		int truckMaxCapacity = maxTruckDetails.getVehicleType();
		if (orderQty > truckMaxCapacity) {// check order qty with max truck
											// capacity
			if (orderQty != truckMaxCapacity) {
				groupTruckList.add(maxTruckDetails);
				int remainingOrderQty = orderQty - truckMaxCapacity;
				if (remainingOrderQty > truckMaxCapacity) {

				} else {
					for (AvailableTrucksModel truckDetails : initialTruckInfoList) {
						if (truckDetails.getVehicleType() == remainingOrderQty) {
							if (!isTruckGroup(groupTruckList, truckDetails.getSlNo())) {
								groupTruckList.add(truckDetails);
								break;
							}
						} /*
							 * else { getfinalTruckList(groupTruckList,
							 * remainingOrderQty); }
							 */
					}
				}
			} else {// if allOrderQty and truck capacity is same

			}
		} else {// if orser qty is low compare to truck max capacity
			AvailableTrucksModel tDetails = Collections.min(initialTruckInfoList,
					Comparator.comparing(AvailableTrucksModel::getVehicleType));
			groupTruckList.add(tDetails);

		}
		return groupTruckList;
	}

	private boolean isTruckGroup(List<AvailableTrucksModel> truckList, String truckNo) {
		String allTruckNos = truckList.stream().map(AvailableTrucksModel::getSlNo).collect(Collectors.joining(","));
		if (allTruckNos.contains(truckNo)) {
			return true;
		}
		return false;
	}

	private void orderGroupByTruck(Map<IntellishipModel, List<AvailableTrucksModel>> groupTruckMap) {
		for (Map.Entry<IntellishipModel, List<AvailableTrucksModel>> allDetails : groupTruckMap.entrySet()) {
			IntellishipModel intellship = allDetails.getKey();
			List<ShippingDetails1> ordersList = intellship.getOrderDetailsList();
			// ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).
			int allOrdsQtys = getAllOrdersQty(ordersList);
			List<AvailableTrucksModel> groupTruckDetails = allDetails.getValue();
			OrderGroup orderGroup = null;
			List<OrderGroup> orderGroupList = new ArrayList<>();
			Map<String, List<OrderGroup>> ordersMap = new HashMap<>();
			for (AvailableTrucksModel truckDetails : groupTruckDetails) {
				int initialOrder = 1;
				int truckCapacity = truckDetails.getVehicleType();
				int ordersQtyTruck = 0;
				for (ShippingDetails1 orderDetails : ordersList) {
					orderGroup = new OrderGroup();
					int orderQty = Integer.parseInt(orderDetails.getActual_delivery_qty());
					if (isOrderQtyTruck(ordersMap, orderQty, orderDetails.getDelivery())) {// check
																							// is
																							// order
																							// load
																							// in
																							// truck
																							// or
																							// not,if
																							// order
																							// is
																							// loaded
																							// then
																							// skip
																							// those
																							// order
						continue;
					}
					int remaingTruckCapacity = 0;
					if (initialOrder != 1) {
						int truckOrdQty = truckCapacity - ordersQtyTruck;
						if (truckOrdQty < orderQty) {
							remaingTruckCapacity = truckOrdQty;
						} else {
							remaingTruckCapacity = orderQty;
						}
					}
					if (ordersMap.containsKey(orderDetails.getDelivery())) {
						remaingTruckCapacity = getRemainingOrderqQty(ordersMap, orderDetails.getDelivery());
						if (initialOrder == 1) {
							orderQty = remaingTruckCapacity;
						}
					}
					orderGroup.setDelivaryNo(orderDetails.getDelivery());
					orderGroup.setOriginalOrderQty(orderDetails.getActual_delivery_qty());
					orderGroup.setTruckNo(truckDetails.getSlNo());
					orderGroup.setTruckCapacity(String.valueOf(truckDetails.getVehicleType()));
					if (initialOrder == 1) {
						orderGroup.setTruckOrderQty(orderQty);
						ordersQtyTruck = orderQty;
					} else {
						orderGroup.setTruckOrderQty(remaingTruckCapacity);
						ordersQtyTruck = ordersQtyTruck + remaingTruckCapacity;
					}
					orderGroup.setDelivaryDate(orderDetails.getDeliv_date());
					// orderGroupList.add(orderGroup);
					if (ordersMap.containsKey(orderDetails.getDelivery())) {
						List<OrderGroup> orderGroupsList = ordersMap.get(orderDetails.getDelivery());
						orderGroupsList.add(orderGroup);
						ordersMap.put(orderDetails.getDelivery(), orderGroupsList);
					} else {
						List<OrderGroup> orders = new ArrayList<>();
						orders.add(orderGroup);
						ordersMap.put(orderDetails.getDelivery(), orders);
					}

					initialOrder++;
					if (ordersQtyTruck == truckCapacity
							|| isOrderQtyTruck(ordersMap, allOrdsQtys, orderDetails.getDelivery())) {// truck
																										// is
																										// full
																										// with
																										// orders
						break;
					}
				}
				// orderGroupList = new ArrayList<>();
			} // end truck for loop
			for (Map.Entry<String, List<OrderGroup>> ords : ordersMap.entrySet()) {
				List<OrderGroup> ordGroup = ords.getValue();
				for (OrderGroup orderGroup2 : ordGroup) {
					shippingOrderService.saveOrderGroup(orderGroup2);
				}
			}
		}
	}

	private int getRemainingOrderqQty(Map<String, List<OrderGroup>> ordersMap, String delivaryNo) {
		List<OrderGroup> orderGroupList = ordersMap.get(delivaryNo);
		int finalOrdQty = 0;
		for (OrderGroup orderGroup : orderGroupList) {
			int originalOrdQty = Integer.parseInt(orderGroup.getOriginalOrderQty());
			int truckOrdQty = orderGroup.getTruckOrderQty();
			finalOrdQty = originalOrdQty - truckOrdQty;
		}

		return finalOrdQty;
	}

	private int getAllOrdersQty(List<ShippingDetails1> shippingDetailsList) {
		int ordQty = 0;
		for (ShippingDetails1 shippingDetails1 : shippingDetailsList) {
			ordQty = ordQty + Integer.parseInt(shippingDetails1.getActual_delivery_qty());
		}
		return ordQty;
	}

	private boolean isOrderQtyTruck(Map<String, List<OrderGroup>> ordersMap, int allOrdersQty, String delivaryNo) {
		List<OrderGroup> orderGroupList = ordersMap.get(delivaryNo);
		if (CollectionUtils.isEmpty(orderGroupList)) {
			return false;
		}
		int ordQtyInTruck = 0;
		for (OrderGroup orderGroup : orderGroupList) {
			ordQtyInTruck = ordQtyInTruck + orderGroup.getTruckOrderQty();
		}
		if (allOrdersQty == ordQtyInTruck) {
			return true;
		}
		return false;
	}

	private boolean isFullTruck(Map<String, List<OrderGroup>> ordersMap, int truckCapacity, String truckNo) {
		List<OrderGroup> orderGroupList = ordersMap.get(truckNo);
		if (CollectionUtils.isEmpty(orderGroupList)) {
			return false;
		}
		int trucksQty = 0;
		for (OrderGroup orderGroup : orderGroupList) {
			int qty = orderGroup.getTruckOrderQty();
			trucksQty = trucksQty + qty;
		}
		if (trucksQty == truckCapacity) {
			return true;
		}
		return false;
	}

	public List<IntellishipModel> getTrucksCount(List<IntellishipModel> intellishModel) {
		List<OrderGroup> orderGroupList = shippingOrderService.getAllGroupOrderList();
		Map<String, Set<String>> truckGroup = new HashMap<>();
		for (OrderGroup orderGroup : orderGroupList) {
			String date = orderGroup.getDelivaryDate();
			if (truckGroup.containsKey(date)) {
				Set<String> truckNo = truckGroup.get(date);
				truckNo.add(orderGroup.getTruckNo());
				truckGroup.put(date, truckNo);
			} else {
				Set<String> truNo = new HashSet<>();
				truNo.add(orderGroup.getTruckNo());
				truckGroup.put(date, truNo);
			}
		}
		List<IntellishipModel> newIntellishModel = new ArrayList<>();
		for (IntellishipModel intellModel : intellishModel) {
			String delivaryDate = intellModel.getDelivaryDate();
			Set<String> totTrucks = truckGroup.get(delivaryDate);
			intellModel.setTruckNum(String.valueOf(totTrucks.size()));
			newIntellishModel.add(intellModel);
		}
		return newIntellishModel;
	}

	public String getDistence(String orderNo) {
		ShortestDistLantiAndLongti shortestDetails = new ShortestDistLantiAndLongti();
		StoreSpDetails sd = new StoreSpDetails();
		MapService gmapDist = new MapService();
		List<ShippingDetails> shippingDetailsList = sd.getAllShippingDetails();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		Map<String, ShippingDetails> shippingDetailsMap = sd.getAllShippingDetailsMap();
		// List<Double> distences = new ArrayList<>();
		ShippingDetails shippingDetail = shippingDetailsMap.get(orderNo);
		if (shippingDetail == null) {
			return "Invaild OrderNo,Please enter vaild OrderNo";
		}
		Map<Double, String> distences = new LinkedHashMap<>();
		double initialDistence = 0.0;
		Map<String, StringBuilder> shortDist = new HashMap<>();
		double plantLangitude = 0.0;
		double plantLatitude = 0.0;
		int intitialDist = 1;
		for (PlantDetails plantDetails : plantDetailsList) {
			/*
			 * double distence =
			 * distance(Double.parseDouble(shippingdetail.getLatitude()),
			 * Double.parseDouble(shippingdetail.getLongitude()),
			 * Double.parseDouble(plantDetails.getLatitude()),
			 * Double.parseDouble(plantDetails.getLongitude()), "K");
			 */
			double distence = 0.0;
			try {
				distence = gmapDist.getDistence(shippingDetail.getLatitude() + "," + shippingDetail.getLongitude(),
						plantDetails.getLatitude() + "," + plantDetails.getLongitude());
			} catch (IOException e) {
				System.out.println("Unbale to calculate distence :" + e.getCause());
				e.printStackTrace();
			}
			if (intitialDist == 1) {
				initialDistence = distence;
			}
			if (distence < initialDistence) {
				initialDistence = distence;
				plantLangitude = plantDetails.getLongitude();
				plantLatitude = plantDetails.getLatitude();
			}
			StringBuilder ord = new StringBuilder();
			ord.append("PlantName:" + plantDetails.getPlantName()).append("_").append(plantDetails.getPlantState());
			distences.put(distence, ord.toString());
			intitialDist++;
		}
		shortestDetails.setPlantLatitude(plantLatitude);
		shortestDetails.setPlantLongitude(plantLangitude);
		shortestDetails.setShortestDist(initialDistence);
		shortestDetails.setOrderLatitude(shippingDetail.getLatitude());
		shortestDetails.setOrderLongitude(shippingDetail.getLongitude());
		StringBuilder allDetails = new StringBuilder();
		/*
		 * allDetails.append(initialDistence).append("##")
		 * .append("Plant Latitude & Longitude:").append("Latitude: " +
		 * plantLatitude).append(":") .append("Longitude: " +
		 * plantLangitude).append("Order Latitude & Longitude:")
		 * .append("Latitude: " + shippingDetail.getLatitude()).append(":")
		 * .append("Longitude: " + shippingDetail.getLongitude());
		 */
		allDetails.append(initialDistence).append("##").append("Plant Details: ").append(plantLangitude).append(",")
				.append(plantLatitude);
		shortDist.put(shippingDetail.getDelivary(), allDetails);

		StringBuilder allData = new StringBuilder();
		allData.append("All Plant Distence from Receptive Order: ").append(orderNo).append("\n");
		for (Map.Entry<Double, String> dist : distences.entrySet()) {
			System.out.println(dist.getValue() + "::" + dist.getKey());
			allData.append(dist.getValue()).append("::").append(dist.getKey());
			allData.append("\n");
		}
		allData.append("\n");
		System.out.println("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("\n");
		for (Map.Entry<String, StringBuilder> dist : shortDist.entrySet()) {
			System.out.println("Order No: " + dist.getKey() + "::" + "Shortest Distence:" + dist.getValue());
			allData.append("Order No: " + dist.getKey()).append("::").append("Shortest Distence:" + dist.getValue());
		}
		shortestDetails.setOrderNo(orderNo);
		return allData.toString();

	}

	public String getAllDistence() {
		StoreSpDetails sd = new StoreSpDetails();
		MapService gmapDist = new MapService();
		List<ShippingDetails> shippingDetailsList = sd.getAllShippingDetails();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		Map<String, ShippingDetails> shippingDetailsMap = sd.getAllShippingDetailsMap();
		// List<Double> distences = new ArrayList<>();
		// ShippingDetails shippingDetail = shippingDetailsMap.get();
		/*
		 * if(shippingDetail == null){ return
		 * "Invaild OrderNo,Please enter vaild OrderNo"; }
		 */
		Map<Double, String> distences = new LinkedHashMap<>();
		double initialDistence = 0.0;
		Map<String, StringBuilder> shortDist = new HashMap<>();
		String plantLangitude = "";
		String plantLatitude = "";
		int intitialDist = 1;
		for (ShippingDetails shippingDetail : shippingDetailsList) {
			for (PlantDetails plantDetails : plantDetailsList) {
				/*
				 * double distence =
				 * distance(Double.parseDouble(shippingdetail.getLatitude()),
				 * Double.parseDouble(shippingdetail.getLongitude()),
				 * Double.parseDouble(plantDetails.getLatitude()),
				 * Double.parseDouble(plantDetails.getLongitude()), "K");
				 */
				double distence = 0.0;
				try {
					distence = gmapDist.getDistence(shippingDetail.getLatitude() + "," + shippingDetail.getLongitude(),
							plantDetails.getLatitude() + "," + plantDetails.getLongitude());
				} catch (IOException e) {
					System.out.println("Unbale to calculate distence :" + e.getCause());
					e.printStackTrace();
				}
				if (intitialDist == 1) {
					initialDistence = distence;
				}
				if (distence < initialDistence) {
					initialDistence = distence;
					plantLangitude = plantDetails.getPlantName();
					plantLatitude = plantDetails.getPlantState();
				}
				StringBuilder ord = new StringBuilder();
				ord.append("Order:" + shippingDetail.getDelivary()).append("##")
						.append("PlantName:" + plantDetails.getPlantName()).append("_")
						.append(plantDetails.getPlantState());
				distences.put(distence, ord.toString());
				intitialDist++;
			}
			StringBuilder allDetails = new StringBuilder();
			/*
			 * allDetails.append(initialDistence).append("##")
			 * .append("Plant Latitude & Longitude:").append("Latitude: " +
			 * plantLatitude).append(":") .append("Longitude: " +
			 * plantLangitude).append("Order Latitude & Longitude:")
			 * .append("Latitude: " + shippingDetail.getLatitude()).append(":")
			 * .append("Longitude: " + shippingDetail.getLongitude());
			 */
			allDetails.append(initialDistence).append("##").append("Plant Details: ").append(plantLangitude).append(",")
					.append(plantLatitude);
			shortDist.put(shippingDetail.getDelivary(), allDetails);
		}
		StringBuilder allData = new StringBuilder();
		allData.append("All Plant Distence from Receptive Order: ").append("\n");
		for (Map.Entry<Double, String> dist : distences.entrySet()) {
			System.out.println(dist.getValue() + "::" + dist.getKey());
			allData.append(dist.getValue()).append("::").append(dist.getKey());
			allData.append("\n");
		}
		allData.append("\n");
		System.out.println("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("\n");
		for (Map.Entry<String, StringBuilder> dist : shortDist.entrySet()) {
			System.out.println("Order No: " + dist.getKey() + "::" + "Shortest Distence:" + dist.getValue());
			allData.append("Order No: " + dist.getKey()).append("::").append("Shortest Distence:" + dist.getValue());
			allData.append("\n");
		}
		return allData.toString();

	}

	public String getAllDistence1() {
		StoreSpDetails sd = new StoreSpDetails();
		MapService gmapDist = new MapService();
		List<ShippingDetails> shippingDetailsList = sd.getAllShippingDetails();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		Map<String, ShippingDetails> shippingDetailsMap = sd.getAllShippingDetailsMap();
		// List<Double> distences = new ArrayList<>();
		// ShippingDetails shippingDetail = shippingDetailsMap.get();
		/*
		 * if(shippingDetail == null){ return
		 * "Invaild OrderNo,Please enter vaild OrderNo"; }
		 */
		Map<Double, String> distences = new LinkedHashMap<>();
		double initialDistence = 0.0;
		Map<String, StringBuilder> shortDist = new HashMap<>();
		String plantLangitude = "";
		String plantLatitude = "";
		int intitialDist = 1;
		// Date todayDate = Calendar.getInstance().getTime().getDate();
		// Calendar.getInstance().get
		// DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

		// to convert Date to String, use format method of SimpleDateFormat
		// class.
		// String todatdte = dateFormat.format(todayDate);
		Map<Long, List<ShippingDetails>> groupBasedOnDays = new HashMap<>();
		for (ShippingDetails shippingDetail : shippingDetailsList) {
			String date = shippingDetail.getDate();

			LocalDate delivaryDate = LocalDate.parse(date);
			// LocalDate currentDate = LocalDate.parse(todatdte);
			Long noOfdays = ChronoUnit.DAYS.between(LocalDate.now(), delivaryDate);
			// int noOfDays =
			List<ShippingDetails> shippingList = groupBasedOnDays.get(noOfdays);
			if (shippingList == null) {
				List<ShippingDetails> initialList = new ArrayList<>();
				initialList.add(shippingDetail);
				groupBasedOnDays.put(noOfdays, initialList);
			} else {
				shippingList.add(shippingDetail);
				groupBasedOnDays.put(noOfdays, shippingList);
			}
		}
		System.out.println("Before sorting latitude and longtude");
		for (Map.Entry<Long, List<ShippingDetails>> details : groupBasedOnDays.entrySet()) {
			for (ShippingDetails shippingDetils : details.getValue()) {
				System.out.println("No Delivary Due Days: " + details.getKey() + "Shipping details:"
						+ shippingDetils.getLatitude() + ":Longitude:  " + shippingDetils.getLongitude());
			}
			// System.out.println("Delivary Time Day
			// due:"+details.getKey()+"##"+"NoOfOrders:
			// "+details.getValue().size());
		}

		Map<Long, List<ShippingDetails>> afterSoringGroup = new HashMap<>();
		for (Map.Entry<Long, List<ShippingDetails>> data : groupBasedOnDays.entrySet()) {
			Collections.sort(data.getValue(), new Comparator<ShippingDetails>() {

				@Override
				public int compare(ShippingDetails o1, ShippingDetails o2) {
					return Double.compare(o1.getLatitude(), o2.getLatitude());
				}
			});
			afterSoringGroup.put(data.getKey(), data.getValue());
		}
		System.out.println("After sorting");
		for (Map.Entry<Long, List<ShippingDetails>> details : afterSoringGroup.entrySet()) {
			for (ShippingDetails shippingDetils : details.getValue()) {
				System.out.println("No Delivary Due Days: " + details.getKey() + "Shipping details:"
						+ shippingDetils.getLatitude() + ":Longitude:  " + shippingDetils.getLongitude());
			}
			// System.out.println("Delivary Time Day
			// due:"+details.getKey()+"##"+"NoOfOrders:
			// "+details.getValue().size());
		}

		StringBuilder allData = new StringBuilder();
		allData.append("All Plant Distence from Receptive Order: ").append("\n");
		for (Map.Entry<Double, String> dist : distences.entrySet()) {
			System.out.println(dist.getValue() + "::" + dist.getKey());
			allData.append(dist.getValue()).append("::").append(dist.getKey());
			allData.append("\n");
		}
		allData.append("\n");
		System.out.println("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("#####SHORTEST DISTENCE FROM PLANT#####");
		allData.append("\n");
		for (Map.Entry<String, StringBuilder> dist : shortDist.entrySet()) {
			System.out.println("Order No: " + dist.getKey() + "::" + "Shortest Distence:" + dist.getValue());
			allData.append("Order No: " + dist.getKey()).append("::").append("Shortest Distence:" + dist.getValue());
			allData.append("\n");
		}
		return allData.toString();

	}

	public void distence() {
		StoreSpDetails sd = new StoreSpDetails();
		MapService gmapDist = new MapService();
		List<ShippingDetails> shippingDetailsList = sd.getAllShippingDetails();
		List<PlantDetails> plantDetailsList = sd.getAllPlantDetails();
		Map<String, ShippingDetails> shippingDetailsMap = sd.getAllShippingDetailsMap();
		// List<Double> distences = new ArrayList<>();
		Map<Double, String> distences = new LinkedHashMap<>();
		double initialDistence = 0.0;
		Map<String, StringBuilder> shortDist = new HashMap<>();
		double plantLangitude = 0.0;
		double plantLatitude = 0.0;
		for (ShippingDetails shippingdetail : shippingDetailsList) {
			int intitialDist = 1;
			for (PlantDetails plantDetails : plantDetailsList) {
				/*
				 * double distence =
				 * distance(Double.parseDouble(shippingdetail.getLatitude()),
				 * Double.parseDouble(shippingdetail.getLongitude()),
				 * Double.parseDouble(plantDetails.getLatitude()),
				 * Double.parseDouble(plantDetails.getLongitude()), "K");
				 */
				double distence = 0.0;
				try {
					distence = gmapDist.getDistence(shippingdetail.getLatitude() + "," + shippingdetail.getLongitude(),
							plantDetails.getLatitude() + "," + plantDetails.getLongitude());
				} catch (IOException e) {
					System.out.println("Unbale to calculate distence :" + e.getCause());
					e.printStackTrace();
				}
				if (intitialDist == 1) {
					initialDistence = distence;
				}
				if (distence < initialDistence) {
					initialDistence = distence;
					plantLangitude = plantDetails.getLongitude();
					plantLatitude = plantDetails.getLatitude();
				}
				StringBuilder ord = new StringBuilder();
				ord.append("Order:" + shippingdetail.getDelivary()).append(":")
						.append("PlantName:" + plantDetails.getPlantName()).append("_")
						.append(plantDetails.getPlantState());
				distences.put(distence, ord.toString());
				intitialDist++;
			}
			StringBuilder allDetails = new StringBuilder();
			allDetails.append(initialDistence).append("##").append("Plant Latitude & Longitude:")
					.append("Latitude: " + plantLatitude).append(":").append("Longitude: " + plantLangitude)
					.append("Order Latitude & Longitude:").append("Latitude: " + shippingdetail.getLatitude())
					.append(":").append("Longitude: " + shippingdetail.getLongitude());
			shortDist.put(shippingdetail.getDelivary(), allDetails);
		}
		for (Map.Entry<Double, String> dist : distences.entrySet()) {
			System.out.println(dist.getValue() + "::" + dist.getKey());
		}
		System.out.println("#####SHORTEST DISTENCE FROM PLANT#####");
		for (Map.Entry<String, StringBuilder> dist : shortDist.entrySet()) {
			System.out.println("Order No: " + dist.getKey() + "::" + "Shortest Distence:" + dist.getValue());
		}

	}

	private List<ShippingDetails1> getShippingDetailsByTruckNo(List<String> orderNoList) {
		List<ShippingDetails1> shippingDetailList = new ArrayList<>();
		for (String orderNo : orderNoList) {
			ShippingDetails1 ShippingDetailsObj = shippingOrderService.getShippingDetails(orderNo);
			shippingDetailList.add(ShippingDetailsObj);
		}
		return shippingDetailList;
	}
	private File convertMultiPartFileIntoFile(MultipartFile mfile){
		File file = null;
		file = new File(mfile.getOriginalFilename());
		try {
			mfile.transferTo(file);
		} catch (IllegalStateException | IOException e) {
			
		}
		
		return file;
	}
	 private  Workbook getWorkBook(File file){
		    String fileExtension = CommonUtility.getFileExtension(file.getName());
		    ZipSecureFile.setMinInflateRatio(0.001d);
		   // File file = convertMultiPartFileIntoFile(mfile);
		    Workbook workBook = null;
		    if(ApplicationConstants.CONST_STRING_XLS.equalsIgnoreCase(fileExtension)){
		    	try(Workbook workbook1 = new HSSFWorkbook(new FileInputStream(file))) {
					return workbook1;
				} catch (IOException e) {
					//_LOGGER.error("unable to file convert into excelsheet"+e);
				}
		     }else if(ApplicationConstants.CONST_STRING_XLSX.equalsIgnoreCase(fileExtension)){
		    	try(Workbook workBook2 = new XSSFWorkbook(file)) {
		    		return workBook2;
				} catch (InvalidFormatException | IOException e) {
					//_LOGGER.error("unable to file convert into excelsheet"+e);
				}
		    }else if(ApplicationConstants.CONST_STRING_CSV.equalsIgnoreCase(fileExtension)){
		    	//workBook = getExcel(file);
		    	return workBook;
		    }else{
		    	
		    }
			return workBook;
		}
	 
	 

	 
	 
	 
	 
	 
}

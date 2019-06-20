package com.a4tech.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.dao.entity.DistrictWiseNormalLoadCapacity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.exceptions.ChannelSequenceException;
import com.a4tech.exceptions.MapOverLimitException;
import com.a4tech.exceptions.MapServiceRequestDeniedException;
import com.a4tech.map.model.Address;
import com.a4tech.service.mapper.IOrderDataMapper;
import com.a4tech.services.ShippingService;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.AvailableTrucks;
import com.a4tech.shipping.model.AxleWheelConfiguration;
import com.a4tech.shipping.model.ChannelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.FileUploadBean;
import com.a4tech.shipping.model.IntellishipModelByMaterial;
import com.a4tech.shipping.model.NormalLoadConfiguration;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.UsedTrucksModel;
import com.a4tech.shipping.model.User;
import com.a4tech.shipping.validator.NormalLoadValidator;
import com.a4tech.util.ApplicationConstants;
import com.a4tech.util.CommonUtility;

@Controller
@RequestMapping({ "/", "/demoversion" })
public class ShippingDetailController {

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
		Collections.sort(shippingaOrderList, Comparator.comparing(ShippingDetails1::getDistrict_name));
		return new ModelAndView("algorithm_process", "shippingaOrderList", shippingaOrderList);
	}


	@RequestMapping(value = "/intellShip")
	public ModelAndView intellShipPro() throws MapOverLimitException, MapServiceRequestDeniedException {
		
		 String channelSequence =  shippingOrderService.getChannelSequence();
		 if(StringUtils.isEmpty(channelSequence)) {
			
			 //return new ModelAndView("errorPage", "", "");
			throw new ChannelSequenceException(
					"Channel Sequence should not be empty, Please Configure Channel Sequence in Channel Configuration");
		 }
		 shippingOrderService.deleteAllGroupOrders("orderGroup");
		 shippingOrderService.deleteAllGroupOrders("orderReAssign");
		 shippingOrderService.deleteAllGroupOrders("shippingFinalOrders");
		 try {
			shippingService.getClubbedOrders(channelSequence);
		} catch (MapOverLimitException e) {
			throw new MapOverLimitException(e.getErrorMsg());
		} catch (MapServiceRequestDeniedException e) {
			throw new MapServiceRequestDeniedException(e.getErrorMsg());
		}
		List<IntellishipModelByMaterial> finalIntelishipModel = shippingService.getFinalGroupOrders();
		Collections.sort(finalIntelishipModel, Comparator.comparing(IntellishipModelByMaterial::getPendingQuantity));
		return new ModelAndView("intellShipProcess", "shippingGroupList",finalIntelishipModel);
		
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
		List<AvailableTrucks> trucksInfoList = shippingOrderService.getAllAvilableTrucks();
		trucksInfoList.sort(Comparator.comparing(AvailableTrucks::getDelayTimeInMins).reversed());
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
	  if(mfile.getFile().getSize() == 0)
	  {
		  model.addAttribute("showMessage", "select");
	  }
	  else{
	  File file = convertMultiPartFileIntoFile(mfile.getFile());
		Workbook wb = getWorkBook(file);
        dataMapper.pendingOrderMapper(wb);
		model.addAttribute("pendingOrderMessage", "success");
		
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
		Workbook wb = getWorkBook(file);
		Sheet sheet = wb.getSheetAt(0);
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
	
	@RequestMapping(value = "/normalLoadConfiguration", method = RequestMethod.GET)
	public ModelAndView showNormalLoadConfiguration() {
		return new ModelAndView("configure_districtWise_Normal_load", "normalLoadConfig",
				new NormalLoadConfiguration());
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
	}
	@RequestMapping(value = "/normalLoadConfiguration1", method = RequestMethod.POST,params="update")
	public ModelAndView updateNormalLoadConfiguration1(
			@ModelAttribute("normalLoadConfig") @Validated NormalLoadConfiguration normalLoadConfig,BindingResult result,Model model) {
		System.out.println("Update configuration");
		if(result.hasErrors()){
		}
     shippingOrderService.updateDistrictWiseNormalLoad(normalLoadConfig);
       model.addAttribute("update", "updateSuccess");
       List<DistrictWiseNormalLoadCapacity> dwnList = shippingOrderService.getAllDistrictWiseLoads();
		return new ModelAndView("districtWiseNormalLoadConfigureView", "configureViewData", dwnList);
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
		 List<AxleWheelnfoEntity> infoList=shippingOrderService.getWheelTypeInfo("6");
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		 model.addAttribute("axleWhllerInfoList", infoList);
		 model.addAttribute("mySelect", "6");
		return new ModelAndView("axleWheelConfig", "axelWheelConfig",
				new NormalLoadConfiguration());
	}
	@RequestMapping(value = "/axelWheelConfiguration")
	public ModelAndView showAxelWheelConfigurationByWheelerType(Model model,@RequestParam("mySelect") String wheelerType)  {
		 List<AxleWheelTypeEntity> listOfAxleWheller = shippingOrderService.getAllAxleWheelTypeEntity();
		 List<AxleWheelnfoEntity> infoList=shippingOrderService.getWheelTypeInfo(wheelerType);
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		 model.addAttribute("axleWhllerInfoList", infoList);
		 model.addAttribute("mySelect", wheelerType);
		return new ModelAndView("axleWheelConfig", "axelWheelConfig",
				new NormalLoadConfiguration());
	}
	
	@RequestMapping(value = "/axelWheelConfiguration", method = RequestMethod.POST, params = "addType")
	public String createWheelerType(
			@ModelAttribute("axleWheelConfig") @Validated AxleWheelConfiguration axleWheelConfig, BindingResult result,
			Model model) {
		String wheelType = axleWheelConfig.getAxlewheelertype();
		AxleWheelTypeEntity wheelData = shippingOrderService.getAxlewheel(wheelType);
		if (wheelData != null) {
			model.addAttribute("showMessage", "Error");
		} else {
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
			Workbook wb = getWorkBook(file);
			Sheet sheet = wb.getSheetAt(0);
			 numberOfCells=sheet.getRow(0).getLastCellNum();
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
	public ModelAndView showChannelConfiguration(Model model) {
		 List<ChannelConfiguration> channelConfigList = shippingOrderService.getAllChannelConfigurations();
		List<String> sequenceList = channelConfigList.stream().map(ChannelConfiguration::getSequence)
				.collect(Collectors.toList());
		String channelSeq = shippingOrderService.getChannelSequence();
		 model.addAttribute("sequenceList", sequenceList);
		 model.addAttribute("channelSeq", channelSeq);
			return new ModelAndView("channel_config", "channelConfigList", channelConfigList);
	}
	@PostMapping(value="/channelConfiguration")
	public String channelConfiguration(
			@ModelAttribute("districtByPass") @Validated ChannelConfiguration channelConfig,BindingResult result,Model model,HttpServletRequest req) {
		
       shippingOrderService.saveOrUpdatechannelConfiguration(channelConfig);
       return "redirect:/channelConfiguration";
      
	}
	@GetMapping(value="/deleteChannelConfiguration/{id}")
	public String deleteChannelConfiguration(@PathVariable("id") Integer id) {
		shippingOrderService.deleteChannelConfiguration(id);
		return "redirect:/channelConfiguration";
	}
	@PostMapping(value="/editChannelConfiguration")
	public String editChannelConfiguration(
			@ModelAttribute("districtByPass") @Validated ChannelConfiguration channelConfig,BindingResult result,Model model,HttpServletRequest req) {
		 shippingOrderService.saveOrUpdatechannelConfiguration(channelConfig);
	       model.addAttribute("update", "updateSuccess");
	       return "redirect:/channelConfiguration";
	}
	
	@RequestMapping(value = "/editAxleWheeler", method = RequestMethod.POST)
	public ModelAndView editAxleWheelerConfigure(Model model,HttpServletRequest req) {
		System.out.println("Update configuration");
	  AxleWheelnfoEntity axleConfiguration = new AxleWheelnfoEntity();
	  String wheelerType = req.getParameter("wheerlerType");
	  axleConfiguration.setClub(req.getParameter("club"));
	  axleConfiguration.setOrder(req.getParameter("leadFrom"));
	  axleConfiguration.setId(Integer.parseInt(req.getParameter("id")));
	  axleConfiguration.setNo(Integer.parseInt(wheelerType));
	  shippingOrderService.updateAxleWheelerInfo(axleConfiguration);
	  List<AxleWheelTypeEntity> listOfAxleWheller = shippingOrderService.getAllAxleWheelTypeEntity();
		 List<AxleWheelnfoEntity> infoList=shippingOrderService.getWheelTypeInfo(wheelerType);
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		 model.addAttribute("axleWhllerInfoList", infoList);
		 model.addAttribute("mySelect", wheelerType);
		return new ModelAndView("axleWheelConfig", "axelWheelConfig",
				new NormalLoadConfiguration());
		//return "configure_districtWise_Normal_load";
	}
	
	@RequestMapping(value = "/addAxleWheeler", method = RequestMethod.POST)
	public ModelAndView addAxleWheelerConfigure(Model model,HttpServletRequest req) {
		System.out.println("Update configuration");
	  AxleWheelnfoEntity axleConfiguration = new AxleWheelnfoEntity();
	  String wheelerType = req.getParameter("newWheerlerType");
	  axleConfiguration.setClub(req.getParameter("club"));
	  axleConfiguration.setOrder(req.getParameter("leadFrom"));
	  axleConfiguration.setNo(Integer.parseInt(wheelerType));
	  shippingOrderService.addAxleWheelerInfo(axleConfiguration);
	  List<AxleWheelTypeEntity> listOfAxleWheller = shippingOrderService.getAllAxleWheelTypeEntity();
		 List<AxleWheelnfoEntity> infoList=shippingOrderService.getWheelTypeInfo(wheelerType);
		 model.addAttribute("axleWhllerList", listOfAxleWheller);
		 model.addAttribute("axleWhllerInfoList", infoList);
		 model.addAttribute("mySelect", wheelerType);
		return new ModelAndView("axleWheelConfig", "axelWheelConfig",
				new NormalLoadConfiguration());
		//return "configure_districtWise_Normal_load";
	}
	
	@RequestMapping(value = "/deleteAxleWheelerInfo")
	@ResponseBody
	public void deleteAxleWheelerInfo(HttpServletRequest req) {
		String wheelerId = req.getParameter("axleWheelerId");
		shippingOrderService.deleteAxleWheelerInfo(Integer.parseInt(wheelerId));
		System.out.println("wheelerId: "+wheelerId);
		//List<ShippingDetails1> shippingaOrderList = shippingOrderService.getShippingDetailsByDate(orderDate);
	}
	public String saveOrderSequence() {
		
		return "";
	}
	@RequestMapping(value = "/orderSequence", produces = "text/plain")
	@ResponseBody
	public String saveOrderSequence(HttpServletRequest req,HttpServletResponse response) throws IOException {
		String sequence = req.getParameter("sequence");
		shippingOrderService.saveChannelSequence(sequence);
		System.out.println(sequence);
		 return "success";
		
	}
	@ExceptionHandler(ChannelSequenceException.class)
	public ModelAndView channelSequenceException(ChannelSequenceException exce) {
		ModelAndView mv = new ModelAndView("channelSequenceError");
		mv.addObject("channelSeqExcp",exce);
	   return mv;
	}
	@ExceptionHandler(MapOverLimitException.class)
	public ModelAndView mapSeriveException(MapOverLimitException exce) {
		ModelAndView mv = new ModelAndView("channelSequenceError");
		mv.addObject("channelSeqExcp",exce);
	   return mv;
	}
	@ExceptionHandler(MapServiceRequestDeniedException.class)
	public ModelAndView mapSeriveRequestDeniedException(MapServiceRequestDeniedException exce) {
		ModelAndView mv = new ModelAndView("channelSequenceError");
		mv.addObject("channelSeqExcp",exce);
	   return mv;
	}
	@RequestMapping(value="/usedTrucks")
	public ModelAndView showUsedTrucks() {
		List<UsedTrucksModel> usedTrucksList = shippingOrderService.getAllUsedTrucks();
		return new ModelAndView("usedTrucks", "usedTrucksList", usedTrucksList);
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

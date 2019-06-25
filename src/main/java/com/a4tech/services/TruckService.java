package com.a4tech.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.dao.entity.UsedTrucksEntity;
import com.a4tech.exceptions.MapOverLimitException;
import com.a4tech.exceptions.MapServiceRequestDeniedException;
import com.a4tech.map.model.DistanceMatrix;
import com.a4tech.map.service.MapService;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.AvailableTrucks;
import com.a4tech.shipping.model.PlantDetails;
import com.a4tech.shipping.model.ShippingDeliveryOrder;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.ShippingOrdersReAssignModel;
import com.a4tech.shipping.model.UsedTrucksModel;

import saveShipping.StoreSpDetails;

@Service
public class TruckService {
	@Autowired
	private IShippingOrder shippingOrderService;
	@Autowired
	private OrderService orderService;
	MapService mapSerice = new MapService();
	private static Logger _LOGGER = Logger.getLogger(TruckService.class);
	public AvailableTrucks getAvailableTruck(List<AvailableTrucks> availableTrucksList,
			List<TruckHistoryDetailsEntity> truckHistoryList,List<String> usedTruckNos, String districtName) {
		for (AvailableTrucks availableTrucks : availableTrucksList) {
			String truckNo = availableTrucks.getSlNo() + "_" + districtName;
			if (usedTruckNos.contains(truckNo)) {
				continue;
			}
			List<TruckHistoryDetailsEntity> tempTruckHistoryList = truckHistoryList.stream()
					.filter(truckHistory -> truckHistory.getTruckNo().equals(truckNo)
							&& truckHistory.getDistrictName().equals(districtName))
					.collect(Collectors.toList());
			if (tempTruckHistoryList == null) {
				continue;
			} else {
				availableTrucks = getAvailableTruck(tempTruckHistoryList.get(0), availableTrucks);
				return availableTrucks;
			}

		}
		
		return null;
	}
	
	
	public void getOrderAssignTrucksWithSameMaterial(Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap,List<AxleWheelnfoEntity> axleWheelerInfoList) throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		List<AvailableTrucks> availableTrucksList =  shippingOrderService.getAllAvilableTrucks();
		availableTrucksList.sort(Comparator.comparing(AvailableTrucks::getDelayTimeInMins).reversed());
		List<TruckHistoryDetailsEntity> truckHistoryList = shippingOrderService.getAllTrucksHistoryDetails();
		List<AvailableTrucks> assignTruckList = new ArrayList<>();
		for (Map.Entry<String, Map<String, List<ShippingDetails1>>> districtWiseGroup : finalMaterialOrdMap.entrySet()) {
			String districtName = districtWiseGroup.getKey();
			Map<String, List<ShippingDetails1>> materialWiseGroup = districtWiseGroup.getValue();
			for (Map.Entry<String, List<ShippingDetails1>> matrlGrp: materialWiseGroup.entrySet()) {
				List<ShippingDetails1> ordersList = matrlGrp.getValue();
				if(ordersList.size() == 1) 
					continue;
				assignTruckList = getAssignTrucks(ordersList, availableTrucksList, truckHistoryList,assignTruckList, districtName,axleWheelerInfoList);
				//orderService.saveOrdersBasedOnTrucks(assignedTrucks);
			}
		}
	}
	public void getOrderAssignTrucksWithDifferentMaterial(Map<String, List<ShippingDetails1>>  finalMaterialOrdMap,List<AxleWheelnfoEntity> axleWheelerInfoList) throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		List<AvailableTrucks> availableTrucksList =  shippingOrderService.getAllAvilableTrucks();
		availableTrucksList.sort(Comparator.comparing(AvailableTrucks::getDelayTimeInMins).reversed());
		List<TruckHistoryDetailsEntity> truckHistoryList = shippingOrderService.getAllTrucksHistoryDetails();
		List<AvailableTrucks> assignTruckList = new ArrayList<>();
		for (Map.Entry<String, List<ShippingDetails1>> districtWiseGroup : finalMaterialOrdMap.entrySet()) {
			String districtName = districtWiseGroup.getKey();
			List<ShippingDetails1> ordersList = districtWiseGroup.getValue();
				if(ordersList.size() == 1) 
					continue;
				assignTruckList = getAssignTrucks(ordersList, availableTrucksList, truckHistoryList,assignTruckList, districtName,axleWheelerInfoList);
				//orderService.saveOrdersBasedOnTrucks(assignedTrucks);
		}
	}

	private List<AvailableTrucks> getAssignTrucks(List<ShippingDetails1> ordersList, List<AvailableTrucks> availableTrucksList,
			List<TruckHistoryDetailsEntity> truckHistoryList,List<AvailableTrucks> assignTrucksList, String districtName,List<AxleWheelnfoEntity> axleWheelerInfoList) throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		Date shippingDate = new Date();
		ShippingDeliveryOrder shippingDeliveryOrder = null;
		Map<String, List<ShippingDetails1>> ordersAssignInTrucks = new HashMap<>();
		int tempTruckCapacity;
		List<ShippingDetails1> pendingOrdersList = new ArrayList<>();
		List<ShippingDetails1> ordersAssignList = new ArrayList<>();
		ShippingOrdersReAssignModel shippingReorder = null;
		int totalOriginalOrderQty = ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).mapToInt(Integer::valueOf).sum();;
		boolean isFirstTruck = true;
		List<ShippingDetails1> allOrdersInTruck = null;
		List<PlantDetails> plantDetailsList =  new StoreSpDetails().getAllPlantDetails();
		PlantDetails plantDetails = plantDetailsList.get(2);
		for (AvailableTrucks availableTrucks : availableTrucksList) {
			shippingDeliveryOrder = new ShippingDeliveryOrder();
			availableTrucks = getAvailableTruckFromHistory(districtName, availableTrucks, truckHistoryList);
			if(availableTrucks == null) { // Available truck is not available in history data then we need to skip those truck
				continue;
			}
			if(isAssignTruck(assignTrucksList, availableTrucks)) {//if truck is already assign then we need to those truck we need to pickup another from truck pool
				continue;
			}
			if(!isFirstTruck) {
				  if(totalOriginalOrderQty < 10) { // all orders is already assign in trucks , order qty is less than means remaing orders goes next cycle process
					 break; 
				  }
			}
			List<AxleWheelnfoEntity> axleInfoList = getAxleWheelerInfoByWheelerType(availableTrucks.getVehicleType(),
					axleWheelerInfoList);
			ordersList = getForwardOrders(axleInfoList, ordersList, plantDetails);
			String truckNo = availableTrucks.getSlNo();
			Integer truckOriginalCapacity = availableTrucks.getNormalLoad();
			shippingDeliveryOrder.setTruckNo(truckNo);
			shippingDeliveryOrder.setShippingDeliveryDate(shippingDate);
			int shippingDelivaryId = shippingOrderService.generateShippingOrderId(shippingDeliveryOrder);
			allOrdersInTruck = new ArrayList<>();
			//Integer truckCapacity = availableTrucks.getNormalLoad();
			if(!CollectionUtils.isEmpty(ordersAssignList)) {
				int ordQty;
				if(!CollectionUtils.isEmpty(pendingOrdersList)) {
					ordersList = pendingOrdersList;
					if(ordersList.size() == 1) {
						 ordQty = Integer.parseInt(ordersList.get(0).getActual_delivery_qty());
						 if(ordQty < 10 ) {
							 break;
						 }
					}
				}
				ordersAssignList = new ArrayList<>();
				pendingOrdersList = new ArrayList<>();
			}
			List<ShippingDetails1> ordListInTrucks = new ArrayList<>();
			Integer totOrdersQty = ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).mapToInt(Integer::valueOf).sum();
			int totOrders = ordersList.size();
			int processOrdCount = 0;
			List<ShippingOrdersReAssignModel> shippingReordersList = new ArrayList<>();
			if(availableTrucks.getNormalLoad() == totOrdersQty) {
				orderService.saveOrder(ordersList, availableTrucks, shippingDate, shippingDelivaryId);
				 totalOriginalOrderQty = totalOriginalOrderQty - totOrdersQty;
				 allOrdersInTruck.addAll(ordersList);
			} else {
				for (ShippingDetails1 order : ordersList) {
					     if(availableTrucks.getNormalLoad() == null) { // it means truck is full with orders ,then we need pickup another truck from truck pool
					    	  if(processOrdCount != totOrders) {
					    		  for(;processOrdCount<totOrders;processOrdCount++) {
					    			  pendingOrdersList.add(ordersList.get(processOrdCount));
					    		  }
					    		  break; 
					    	  } 
					     }
					    Integer ordQty = Integer.parseInt(order.getActual_delivery_qty());
					   
					    int qtyDiff = availableTrucks.getNormalLoad() - ordQty;
					if (qtyDiff > 0) {// if truck capacity is higher than order Qty
						if (ordersAssignInTrucks.containsKey(truckNo)) {
							List<ShippingDetails1> orderList = ordersAssignInTrucks.get(truckNo);
							orderList.add(order);
							ordersAssignInTrucks.put(truckNo, orderList);
							tempTruckCapacity = availableTrucks.getNormalLoad() - ordQty;
							if (tempTruckCapacity > 0) {
								availableTrucks.setNormalLoad(tempTruckCapacity);
							}
							shippingReorder = orderService.convertShippingDetailsIntoAnother(order, availableTrucks.getSlNo());
						    shippingOrderService.deleteOrderFromPendingList(order);
						    ordListInTrucks.add(order);
						    shippingReordersList.add(shippingReorder);
						    totalOriginalOrderQty = totalOriginalOrderQty - Integer.parseInt(order.getActual_delivery_qty());
						} else {
							List<ShippingDetails1> ordList = new ArrayList<>();
							ordList.add(order);
							ordersAssignInTrucks.put(truckNo, ordList);
							tempTruckCapacity = availableTrucks.getNormalLoad() - ordQty;
							if (tempTruckCapacity > 0) {
								availableTrucks.setNormalLoad(tempTruckCapacity);
							}
							shippingReorder = orderService.convertShippingDetailsIntoAnother(order, availableTrucks.getSlNo());
							shippingOrderService.deleteOrderFromPendingList(order);
							 ordListInTrucks.add(order);
							 shippingReordersList.add(shippingReorder);
							 totalOriginalOrderQty = totalOriginalOrderQty - Integer.parseInt(order.getActual_delivery_qty());//it is used for reducing qty from all orders
						}
						ordersAssignList.add(order);
					} else { // if truck capacity is lower than order Qty
						qtyDiff = ordQty - availableTrucks.getNormalLoad();//example: ordQty is 15 and truckCapacity is 25
						List<ShippingDetails1> orderList = ordersAssignInTrucks.get(truckNo);
						if(orderList == null) {  // if truck is assign first time then we need to check the order list
							                     // if order quantity is larger than the truck capacity
							orderList = new ArrayList<>();
						}
						 if(qtyDiff >= 0) { //if truck is full then we need to assign as null
							 order.setActual_delivery_qty(String.valueOf(availableTrucks.getNormalLoad()));
							 availableTrucks.setNormalLoad(null);
						 } else {
							 availableTrucks.setNormalLoad(availableTrucks.getNormalLoad() - ordQty);
							 order.setActual_delivery_qty(String.valueOf(ordQty - availableTrucks.getNormalLoad()));// if order qty is larger than truck capacity
						 }
						 totalOriginalOrderQty = totalOriginalOrderQty - Integer.parseInt(order.getActual_delivery_qty());
						 orderList.add(order);
							ordersAssignInTrucks.put(truckNo, orderList);
							shippingReorder = orderService.convertShippingDetailsIntoAnother(order, availableTrucks.getSlNo());
							 ordListInTrucks.add(order);
							 shippingReordersList.add(shippingReorder);
						 if(qtyDiff != 0) {
							    try {
								ShippingDetails1 pendingOrder =	(ShippingDetails1) order.clone();
								pendingOrder.setActual_delivery_qty(String.valueOf(qtyDiff));// we need to assign pending order qty 
								pendingOrdersList.add(pendingOrder);
								} catch (CloneNotSupportedException e) {
									_LOGGER.warn("unable to clone the object: "+e.getMessage());
								}
						 }
					}
					processOrdCount++;
					allOrdersInTruck.add(order);
				}
				shippingOrderService.saveShippingOrderReAssign(shippingReordersList);
			}
			UsedTrucksModel usedTruck = new UsedTrucksModel();
			usedTruck.setDistrictName(districtName);
			usedTruck.setShippedDate(shippingDate);
			usedTruck.setTransporterName(availableTrucks.getEntryType());
			usedTruck.setTruckNo(availableTrucks.getSlNo());
			usedTruck.setVehicalCapacity(truckOriginalCapacity);
			usedTruck.setVehicalType(String.valueOf(availableTrucks.getVehicleType())+"W");
			usedTruck.setTaggedTime(availableTrucks.getTaggedTime());
			usedTruck.setNormalLoad(availableTrucks.getNormalLoad());
			usedTruck.setRatedLoad(availableTrucks.getRatedLoad());
		   shippingOrderService.saveUsedTruck(usedTruck);
		   shippingOrderService.deleteTruckFromTruckPool(availableTrucks);
		   orderService.saveOrdersBasedOnTrucks(ordListInTrucks, usedTruck, shippingDelivaryId);
		   assignTrucksList.add(availableTrucks);
		   isFirstTruck = false;
		}
		return assignTrucksList;
	}
	
	private AvailableTrucks getAvailableTruckFromHistory(String districtName,
			AvailableTrucks availableTrucks, List<TruckHistoryDetailsEntity> truckHistoryList) {
		// here we will get truck past history data based on truck no and district name 
		Optional<TruckHistoryDetailsEntity> optionalTruckHistoryData = truckHistoryList.stream()
				.filter(truckHistory -> availableTrucks.getSlNo().equals(truckHistory.getTruckNo())
						&& districtName.equals(truckHistory.getDistrictName())).filter(Objects::nonNull).findFirst();
		if (optionalTruckHistoryData.isPresent()) {
			return getAvailableTruck(optionalTruckHistoryData.get(), availableTrucks);
		} else {
			//We will get truck past history data based on wheeler types
			TruckHistoryDetailsEntity truckHistory = getHighestNormalLoad(districtName, availableTrucks.getVehicleType(),truckHistoryList);
			if(truckHistory != null) {
				shippingOrderService.saveTruckhistory(truckHistory, districtName, availableTrucks.getSlNo());
				return getAvailableTruck(truckHistory, availableTrucks);
			}
		}
         return null;
	}
	/*
	 * @Author : Venkat
	 * Description : This method has assign normal and rated load types to available trucks
	 * @Return : Available Trucks
	 */
	private AvailableTrucks getAvailableTruck(TruckHistoryDetailsEntity truckHist, AvailableTrucks availableTrucks) {
		availableTrucks.setRatedLoad(truckHist.getRatedLoad());
		availableTrucks.setNormalLoad(truckHist.getNormalLoad());
		return availableTrucks;
	}
	/*
	 * @Author :    Venkat
	 * @Description : This method used to get highest normal load by using district name and wheeler types , 
	 *                      if list has contains multiple then system should pickup highest normal load from existing list
	 * @Return : Truck history object
	 */
	private TruckHistoryDetailsEntity getHighestNormalLoad(String districtName, int wheelerType,List<TruckHistoryDetailsEntity> truckHistoryList) {
		String wheeler = wheelerType+"W";
		List<TruckHistoryDetailsEntity> highestNormalTruck = truckHistoryList.stream()
				.filter(truckHistory -> districtName.equalsIgnoreCase(truckHistory.getDistrictName())
						&& wheeler.equalsIgnoreCase(truckHistory.getWheelerType()))
				.filter(Objects::nonNull).collect(Collectors.toList());
		if(!CollectionUtils.isEmpty(highestNormalTruck)) {
			highestNormalTruck.sort(Comparator.comparing(TruckHistoryDetailsEntity::getNormalLoad).reversed());
			return highestNormalTruck.get(0);
		}
		return null;
	}
	
	private boolean isAssignTruck(List<AvailableTrucks> assignTrucks,AvailableTrucks availableTruck) {
		String allTruckNos = assignTrucks.stream().map(AvailableTrucks::getSlNo).collect(Collectors.joining(","));
		if(allTruckNos.contains(availableTruck.getSlNo())) {
			return true;
		}
		
		return false;
	}

	public String getTruckLoadType(String truckNo) {
		UsedTrucksEntity usedTruck = shippingOrderService.getUsedTruckByTruckNo(truckNo);
		if (usedTruck != null) {
			if (usedTruck.getNormalLoad() == usedTruck.getRatedLoad()) {
				return "Rated Load";
			}
			return "Normal Load";
		} else {
			return "Normal Load";
		}
	}
	
	private List<AxleWheelnfoEntity> getAxleWheelerInfoByWheelerType(Integer wheelerType,List<AxleWheelnfoEntity> axleWheelerInfoList) {
		return axleWheelerInfoList.stream().filter(axleWheeler -> axleWheeler.getNo() == wheelerType)
				.collect(Collectors.toList());
	}
	
	private List<ShippingDetails1> getForwardOrders(List<AxleWheelnfoEntity> axleInfoList, List<ShippingDetails1> ordersList,
			PlantDetails plantDetails) throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		 List<ShippingDetails1> finalOrdList = new ArrayList<>();
		 int listSize = ordersList.size();
		 double plantToOrderDistance = 0.0;
		 double ordersNextClubbingDistance = 0.0;
		 double previousOrderDistance = 0.0;
		 Double nextOrderDistance = 0.0;
		 for (int ordNo = 0; ordNo < listSize; ordNo++) {
			 if(ordNo == 0) {// this is first order
				 finalOrdList.add(ordersList.get(ordNo));
				 plantToOrderDistance = getOrderDistanceFromPlant(plantDetails.getLatitude()+","+plantDetails.getLongitude(),
							ordersList.get(ordNo).getShip_to_latt() + ","
									+ ordersList.get(ordNo).getShip_to_long()); // this is first dropping point from plant
				 previousOrderDistance = plantToOrderDistance;
				  nextOrderDistance = getNextForwardOrderClubDistance(axleInfoList, plantToOrderDistance);
				 ordersNextClubbingDistance = plantToOrderDistance + nextOrderDistance; // this is used for next order dropping distance
				 continue;
			 }
			 try {
				 if(ordNo == listSize - 1) {// this is for last order from district
					 plantToOrderDistance = getOrderDistanceFromPlant(plantDetails.getLatitude()+","+plantDetails.getLongitude(),
								ordersList.get(ordNo).getShip_to_latt() + ","
										+ ordersList.get(ordNo).getShip_to_long());// from 2nd order distances
					  double ordersDistance = plantToOrderDistance - previousOrderDistance;// we are checking the distance difference between the 2 orders
					  if(nextOrderDistance >= ordersDistance) {//
							finalOrdList.add(ordersList.get(ordNo));
							previousOrderDistance = plantToOrderDistance;
							nextOrderDistance = getNextForwardOrderClubDistance(axleInfoList, plantToOrderDistance);// we will find for next order dropping point distance
					  } else { // this means system has dropped current order because current order does not meet forward criteria condition
						  
					  }
				 } else {
					  plantToOrderDistance = getOrderDistanceFromPlant(plantDetails.getLatitude()+","+plantDetails.getLongitude(),
								ordersList.get(ordNo).getShip_to_latt() + ","
										+ ordersList.get(ordNo).getShip_to_long());// from 2nd order distances
					  double ordersDistance = plantToOrderDistance - previousOrderDistance;// we are checking the distance difference between the 2 orders
					  if(nextOrderDistance >= ordersDistance) {//
							finalOrdList.add(ordersList.get(ordNo));
							previousOrderDistance = plantToOrderDistance;
							nextOrderDistance = getNextForwardOrderClubDistance(axleInfoList, plantToOrderDistance);// we will find for next order dropping point distance
					  } else { // this means system has dropped current order because current order does not meet forward criteria condition
						  
					  }
					  
					/* double distanceDiff = getOrderDistance(
							ordersList.get(ordNo).getShip_to_latt() + "," + ordersList.get(ordNo).getShip_to_long(),
							ordersList.get(ordNo + 1).getShip_to_latt() + ","
									+ ordersList.get(ordNo + 1).getShip_to_long());*/
						/*if(distanceDiff < 90) {
							finalOrdList.add(ordersList.get(ordNo));
						}*/
				 }
			} catch (IndexOutOfBoundsException e) {
				_LOGGER.error("no such data element in list");
			}
		}
		return finalOrdList;
	}
	
	private double getOrderDistance(String source, String destination)
			throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		DistanceMatrix distenceMatrixPojo =	mapSerice.getMaxDistenceFromMultipleDestinationsRestTemplate(source, destination);
		String dist = distenceMatrixPojo.getRowsList().get(0).getElements().get(0).getDistance().getText();
		return Double.parseDouble(dist);
	
	}
	private double getOrderDistanceFromPlant(String plant, String orderDestination)
			throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		DistanceMatrix distenceMatrixPojo =	mapSerice.getMaxDistenceFromMultipleDestinationsRestTemplate(plant, orderDestination);
		String dist = distenceMatrixPojo.getRowsList().get(0).getElements().get(0).getDistance().getText();
		return Double.parseDouble(dist);
	
	}
	private Double getNextForwardOrderClubDistance(List<AxleWheelnfoEntity> axleInfoList,double plantToOrderDistance) {
		for (AxleWheelnfoEntity axleWheelnfoEntity : axleInfoList) {
		     if(axleWheelnfoEntity.getOrder().contains("Upto")) {
		    	 continue;
		     } else {
		    	 String distancePeriod = axleWheelnfoEntity.getOrder();
		    	 String[] distancePeriods = distancePeriod.split("to");
		    	 int startingDistance = Integer.parseInt(distancePeriods[0].trim());
		    	 int endingDistance = Integer.parseInt(distancePeriods[1].replaceAll("[^0-9.]", "").trim());
		    	 if(startingDistance > plantToOrderDistance && endingDistance < plantToOrderDistance) {
		    		 String nextClubbingDistance = axleWheelnfoEntity.getClub();
		    		 nextClubbingDistance = nextClubbingDistance.replaceAll("[^0-9.]", "");
		    		 return Double.parseDouble(nextClubbingDistance);
		    	 }
		     }
		}
		return 50.0;// default next clubbing order distance if none of case doesn't meet 
		
	}
}

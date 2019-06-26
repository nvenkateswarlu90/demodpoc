package com.a4tech.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.exceptions.MapOverLimitException;
import com.a4tech.exceptions.MapServiceRequestDeniedException;
import com.a4tech.map.model.Distance;
import com.a4tech.map.model.DistanceMatrix;
import com.a4tech.map.model.Elements;
import com.a4tech.map.service.MapService;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.AvailableTrucks;
import com.a4tech.shipping.model.ChannelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.OrderMap;
import com.a4tech.shipping.model.PlantDetails;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.ShippingOrdersReAssignModel;
import com.a4tech.shipping.model.UsedTrucksModel;

import saveShipping.StoreSpDetails;

@Service
public class OrderService {
	@Autowired
	private IShippingOrder shippingOrder;
	private Logger _LOGGER = Logger.getLogger(OrderService.class);
  
	  public List<ChannelConfiguration> getChannelsList(String channelSequence){
		 List<ChannelConfiguration> allChannels = shippingOrder.getAllChannelConfigurations();
		  List<String> channelSeq = Arrays.asList(channelSequence.split(","));
		return allChannels.stream().filter(channel -> channelSeq.contains(channel.getSequence()))
				.collect(Collectors.toList());
	  }
	  public boolean isDistrictByPass(List<DistrictClubOrdByPass> distBypassList, String distrciName) {
			for (DistrictClubOrdByPass districtClubOrdByPass : distBypassList) {
				if (districtClubOrdByPass.getDistrictName().equalsIgnoreCase(distrciName)) {
					if (isDistrictOrderByPassDate(districtClubOrdByPass.getStartDate(),
							districtClubOrdByPass.getEndDate())) {
						return true;
					}
				}
			}
			return false;
		}
		private boolean isDistrictOrderByPassDate(LocalDate startDate, LocalDate endDate) {
			LocalDate now = LocalDate.now();
			if ((now.isAfter(startDate) && now.isBefore(endDate)) || (now.equals(startDate) || now.equals(endDate))) {
				return true;
			}
			return false;
		}
	
		public Map<String, List<ShippingDetails1>> getAllOrdersBasedOnDistricts(List<ShippingDetails1> shippingOrderList) {
			Map<String, List<ShippingDetails1>> ordersOnDistrictMap = new HashMap<>();
	        List<DistrictClubOrdByPass> distByPassList = shippingOrder.getAllDistrictClubOrdByPass();
			for (ShippingDetails1 shippingDetails1 : shippingOrderList) {
				String districtName = shippingDetails1.getDistrict_name();
				if(isDistrictByPass(distByPassList, districtName)) {
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

		public Map<String, Map<String, List<ShippingDetails1>>> getAllGroupOrdersBasedOnSameMaterial(
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
		
		public void saveOrder(List<ShippingDetails1> ordersList,AvailableTrucks avaiableTrucks,Date shippingDate,int shippingDelivaryId) {
			OrderGroup orderGrpObj = null;
			ShippingOrdersReAssignModel shippingReorder = null;
			for (ShippingDetails1 shippingDetails1 : ordersList) {
				orderGrpObj = new OrderGroup();
				shippingReorder = convertShippingDetailsIntoAnother(shippingDetails1, avaiableTrucks.getSlNo());
				orderGrpObj.setDelivaryDate(shippingDetails1.getDeliv_date());
				orderGrpObj.setDelivaryNo(shippingDetails1.getDelivery());
				orderGrpObj.setDistrictName(shippingDetails1.getDistrict_name());
				orderGrpObj.setLatitude(shippingDetails1.getShip_to_latt());
				orderGrpObj.setLongitude(shippingDetails1.getShip_to_long());
				orderGrpObj.setMaterialType(shippingDetails1.getMaterial());
				orderGrpObj.setNameShipToParty(shippingDetails1.getName_of_the_ship_to_party());
				orderGrpObj.setOrderShippingDate(shippingDate);
				orderGrpObj.setOriginalOrderQty(shippingDetails1.getActual_delivery_qty());
				orderGrpObj.setShippingDelivaryId(shippingDelivaryId);
				orderGrpObj.setTruckCapacity(String.valueOf(avaiableTrucks.getNormalLoad()));
				orderGrpObj.setTruckNo(avaiableTrucks.getSlNo());
				orderGrpObj.setWheelerType(avaiableTrucks.getVehicleType()+"W");
				orderGrpObj.setTruckOrderQty(Integer.parseInt(shippingDetails1.getActual_delivery_qty()));
				shippingOrder.saveOrderGroup(orderGrpObj);
				shippingOrder.saveShippingOrderReAssign(shippingReorder);
			}
		}
		
		public ShippingOrdersReAssignModel convertShippingDetailsIntoAnother(ShippingDetails1 ship, String truckNo) {
			ShippingOrdersReAssignModel so = new ShippingOrdersReAssignModel();
			so.setActual_delivery_qty(ship.getActual_delivery_qty());
			so.setMaterial(ship.getMaterial());
			so.setDistrict_name(ship.getDistrict_name());
			so.setDistrict_code(ship.getDistrict_code());
			so.setDistribution_channel(ship.getDistribution_channel());
			so.setDeference_document(ship.getDeference_document());
			so.setDeliv_date(ship.getDeliv_date());
			so.setDelivery(ship.getDelivery());
			so.setDelivery_type(ship.getDelivery_type());
			so.setForwarding_agent_name(ship.getForwarding_agent_name());
			so.setName_of_sold_to_party(ship.getName_of_sold_to_party());
			so.setName_of_the_ship_to_party(ship.getName_of_the_ship_to_party());
			so.setRoute(ship.getRoute());
			so.setPlant(ship.getPlant());
			so.setRoute_description(ship.getRoute_description());
			so.setShip_to_latt(ship.getShip_to_latt());
			so.setShip_to_long(ship.getShip_to_long());
			so.setShipping_Point(ship.getShipping_Point());
			so.setSold_to_party(ship.getSold_to_party());
			so.setTruckNo(truckNo);
			return so;
		}
     public void saveOrdersBasedOnTrucks(List<ShippingDetails1> ordsList,UsedTrucksModel usedTrucks,Integer delivaryId) {
    	 OrderGroup orderGroup = null;
    	 for (ShippingDetails1 shippingDetails1 : ordsList) {
    		 orderGroup = new OrderGroup();
    		 orderGroup.setDelivaryNo(shippingDetails1.getDelivery());
    		 orderGroup.setDelivaryDate(shippingDetails1.getDeliv_date());
    		 orderGroup.setDistrictName(shippingDetails1.getDistrict_name());
    		 orderGroup.setLatitude(shippingDetails1.getShip_to_latt());
    		 orderGroup.setLongitude(shippingDetails1.getShip_to_long());
    		 orderGroup.setMaterialType(shippingDetails1.getMaterial());
    		 orderGroup.setNameShipToParty(shippingDetails1.getName_of_the_ship_to_party());
    		 orderGroup.setOrderShippingDate(usedTrucks.getShippedDate());
    		 orderGroup.setOriginalOrderQty(shippingDetails1.getActual_delivery_qty());
    		 orderGroup.setShippingDelivaryId(delivaryId);
    		 orderGroup.setTruckCapacity(String.valueOf(usedTrucks.getVehicalCapacity()));
    		 orderGroup.setTruckNo(usedTrucks.getTruckNo());
    		 orderGroup.setTruckOrderQty(Integer.parseInt(shippingDetails1.getActual_delivery_qty()));
    		 orderGroup.setWheelerType(usedTrucks.getVehicalType());
    		 shippingOrder.saveOrderGroup(orderGroup);
    		 shippingOrder.updateOrderGroupFlag(orderGroup.getDelivaryNo());
		}
    	 
     }
 	public Map<String, Map<String, List<ShippingDetails1>>> getAllForwordOrders(
			Map<String, Map<String, List<ShippingDetails1>>> ordersByMaterial) throws MapOverLimitException,MapServiceRequestDeniedException  {
		MapService gmapDist = new MapService();
		List<PlantDetails> plantDetailsList =  new StoreSpDetails().getAllPlantDetails();
		PlantDetails plantDetails = plantDetailsList.get(2);
		String origin = plantDetails.getLatitude()+","+plantDetails.getLongitude();
		Map<String, Map<String, List<ShippingDetails1>>> orderDistrictList = new HashMap<>();
		for (Map.Entry<String,Map<String, List<ShippingDetails1>>> ordersList : ordersByMaterial.entrySet()) {
			String districtName = ordersList.getKey();//DistrictName
			Map<String, List<ShippingDetails1>> districtOrdersList = ordersList.getValue();
			Map<String, List<ShippingDetails1>> orderMaterialMap = new HashMap<>();
			for (Map.Entry<String,List<ShippingDetails1>> materialOrdersList: districtOrdersList.entrySet()) {
				String materialName = materialOrdersList.getKey();
				List<ShippingDetails1> finalOrders = materialOrdersList.getValue();
				if(finalOrders.size() == 1) {// if order has contains single order ,no need to club the order 
					continue;
				}
				String allDestinations = allDestinationsLattAndLong(finalOrders);
				DistanceMatrix distenceMatrixPojo = null;
				try {
					distenceMatrixPojo = MapService.getAllDestinationsStore(allDestinations);
					if(distenceMatrixPojo == null) {
						// distencePojo =	gmapDist.getMaxDistenceFromMultipleDestinations(origin, allDestinations);
						  distenceMatrixPojo =	gmapDist.getMaxDistenceFromMultipleDestinationsRestTemplate(origin, allDestinations);
						 MapService.saveAllDestinationsStore(allDestinations, distenceMatrixPojo);
					}
					finalOrders = getFinalOrdersList(finalOrders, distenceMatrixPojo);
					orderMaterialMap.put(materialName, finalOrders);
				//}catch (MapServiceex e) {
					// TODO: handle exception
				} 
				catch (IOException e) {
					_LOGGER.error("unable to calculate all destinations: "+e.getMessage());
					throw new MapOverLimitException("Google map daily request quota has been exceeded");
				}
			}
			orderDistrictList.put(districtName, orderMaterialMap);
		}
		
		return orderDistrictList;
	}
 	private String allDestinationsLattAndLong(List<ShippingDetails1> ordsList) {
 		StringBuilder allDestinations = new StringBuilder();
 		for (ShippingDetails1 shippingDetails1 : ordsList) {
 				allDestinations.append(shippingDetails1.getShip_to_latt()).append(",")
 						.append(shippingDetails1.getShip_to_long()).append("|");
 		}
 		 return allDestinations.toString();
 	}
 	private List<ShippingDetails1> getFinalOrdersList(List<ShippingDetails1> shippingList,DistanceMatrix distancePojo) {
 		 List<String> distAddress = distancePojo.getDestinationAddresses();
 		 List<Elements> elementList = distancePojo.getRowsList().get(0).getElements();
 		 
 		 List<OrderMap> orderMapList = new ArrayList<>();
 		 for(int destNo=0;destNo <distAddress.size();destNo++) {
 			double orderDistence = getOrderDistance(elementList.get(destNo));
 			 orderMapList.add(new OrderMap(shippingList.get(destNo), distAddress.get(destNo), orderDistence));
 		 }
 		 Collections.sort(orderMapList, Comparator.comparing(OrderMap::getDistance));
 		 List<ShippingDetails1> finalOrdList = new ArrayList<>();
 		 int listSize = orderMapList.size();
 		 for (int ordNo = 0; ordNo < listSize; ordNo++) {
 			 finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
 			/* if(ordNo == 0) {
 				 finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
 				 continue;
 			 }
 			 try {
 				 if(ordNo == listSize - 1) {
 					 finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
 				 } else {
 					 double distanceDiff = orderMapList.get(ordNo).getDistance() - orderMapList.get(ordNo+1).getDistance();
 						if(distanceDiff < 90) {
 							finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
 						}
 				 }
 				
 			} catch (IndexOutOfBoundsException e) {
 				_LOGGER.error("no such data element in list");
 			}*/
 			
 		}
 		/*for (int ordNo = 0; ordNo < listSize; ordNo++) {
 			 double distanceDiff = orderMapList.get(ordNo).getDistance();
 			 if(distanceDiff < 350) {  // if any order exceed 350km from plant then drop those orders from clubbing process
 				finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
 			 }else {
 				 _LOGGER.info("order exceed more than 350KM +: "+orderMapList.get(ordNo).getShippingDetails().getDelivery());
 			 }
 		}*/
 		 return finalOrdList;
// 		orderMapList.sort(()->
 	}
 	
	private List<ShippingDetails1> getFinalOrdersList1(List<ShippingDetails1> shippingList, DistanceMatrix distancePojo,
			List<AxleWheelnfoEntity> axleInfoList) {
		 List<String> distAddress = distancePojo.getDestinationAddresses();
		 List<Elements> elementList = distancePojo.getRowsList().get(0).getElements();
		 
		 List<OrderMap> orderMapList = new ArrayList<>();
		 for(int destNo=0;destNo <distAddress.size();destNo++) {
			double orderDistence = getOrderDistance(elementList.get(destNo));
			 orderMapList.add(new OrderMap(shippingList.get(destNo), distAddress.get(destNo), orderDistence));
		 }
		 Collections.sort(orderMapList, Comparator.comparing(OrderMap::getDistance));
		 List<ShippingDetails1> finalOrdList = new ArrayList<>();
		 int listSize = orderMapList.size();
		 for (int ordNo = 0; ordNo < listSize; ordNo++) {
			 if(ordNo == 0) {
				 finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
				 continue;
			 }
			 try {
				 if(ordNo == listSize - 1) {
					 finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
				 } else {
					 double distanceDiff = orderMapList.get(ordNo).getDistance() - orderMapList.get(ordNo+1).getDistance();
						if(distanceDiff < 90) {
							finalOrdList.add(orderMapList.get(ordNo).getShippingDetails());
						}
				 }
			} catch (IndexOutOfBoundsException e) {
				_LOGGER.error("no such data element in list");
			}
		}
		 return finalOrdList;
	}
 	
 	
 	public static double getOrderDistance(Elements element) {
 		 Distance dist = element.getDistance();
 		 String kiloMeters = dist.getText();
 		 kiloMeters = kiloMeters.replaceAll("[^0-9.]", "").trim();
 			return Double.parseDouble(kiloMeters);
 		}
 	/*public boolean isDistrictByPass(List<DistrictClubOrdByPass> distBypassList, String distrciName) {
		for (DistrictClubOrdByPass districtClubOrdByPass : distBypassList) {
			if (districtClubOrdByPass.getDistrictName().equalsIgnoreCase(distrciName)) {
				if (isDistrictOrderByPassDate(districtClubOrdByPass.getStartDate(),
						districtClubOrdByPass.getEndDate())) {
					return true;
				}

			}
		}
		return false;
	}

	private boolean isDistrictOrderByPassDate(LocalDate startDate, LocalDate endDate) {
		LocalDate now = LocalDate.now();
		if ((now.isAfter(startDate) && now.isBefore(endDate)) || (now.equals(startDate) || now.equals(endDate))) {
			return true;
		}
		return false;
	}*/
}

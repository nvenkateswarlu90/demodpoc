package com.a4tech.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a4tech.exceptions.MapOverLimitException;
import com.a4tech.exceptions.MapServiceRequestDeniedException;
import com.a4tech.map.service.MapService;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.ChannelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.IntellishipModelByMaterial;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.PlantDetails;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.util.ApplicationConstants;
import com.a4tech.util.CommonUtility;

import saveShipping.StoreSpDetails;

@Service("shippingService")
public class ShippingService {
	@Autowired
	private OrderService orderService;
	@Autowired
	private TruckService truckService;
	MapService gmapDist = new MapService();
	StoreSpDetails sd = new StoreSpDetails();
	@Autowired
	private IShippingOrder shippingOrderService;
	public static Map<String, String> latitudeAndLongitudeMap = new HashMap<>();

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
			int truckCapacity = 0;
			if(orderGrpList.size() == 1) {
				continue;
			}
			for (OrderGroup orderGrup : orderGrpList) {
				shippingLatitudeAndLonitude.append(orderGrup.getLatitude()).append(",")
						.append(orderGrup.getLongitude());
				shippingLatitudeAndLonitude.append("|");
				
				if(orderGrup.getOriginalOrderQty() != null) {
					totalOrdQty = totalOrdQty + Integer.parseInt(orderGrup.getOriginalOrderQty());
				}
				intellishModel.setMaterialType(orderGrup.getMaterialType());
				intellishModel.setTruckCapacity(orderGrup.getTruckCapacity());
				intellishModel.setShippingOrderId(orderGrup.getShippingDelivaryId());
				intellishModel.setWheelerType(orderGrup.getWheelerType());
				intellishModel.setDistrictName(orderGrup.getDistrictName());
				truckCapacity = Integer.parseInt(orderGrup.getTruckCapacity());
				pendingOrderMap.put(orderGrup.getDelivaryNo(), orderGrup);
			}
			int pedningQty = totalOrdQty - truckCapacity;
			if(pedningQty < 0) {// truck capacity is larger than order qty
				pedningQty = truckCapacity - totalOrdQty;
			}
			try {
				String distenceAndHrs = MapService.getDistanceAndHrsMapStore(shippingLatitudeAndLonitude.toString());
				if (distenceAndHrs == null) {
					distenceAndHrs = gmapDist.getMaxDistenceAndHrsFromMultipleDestination(
							plantDetails.getLatitude() + "," + plantDetails.getLongitude(),
							shippingLatitudeAndLonitude.toString());
					MapService.saveDistanceAndHrsMapStore(shippingLatitudeAndLonitude.toString(), distenceAndHrs);
				}
				String[] data = distenceAndHrs.split("###");
				distence = Double.parseDouble(data[0]);
				duration = data[1];

			} catch (IOException e) {
				System.out.println("Unbale to calculate distence :" + e.getCause());
				e.printStackTrace();
			}
			intellishModel.setLoadType(truckService.getTruckLoadType(truckNo));
			intellishModel.setTruckNo(truckNo);
			intellishModel.setTotalKilometers(String.valueOf(distence));
			intellishModel.setTotalOrders(orderGrpList.size());
			intellishModel.setTotalOrderQuantity(totalOrdQty);
			intellishModel.setPlant(plantDetails.getPlantName());
			intellishModel.setPendingQuantity(pedningQty);
			intellishModel.setShippingStatus(getShippingStatus(shippingStatusCount));
			intellishModel.setEstimationTime(duration);
			shippingOrderService.saveShippingFinalOrders(intellishModel);
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

	public void orderDistrictByPass(DistrictClubOrdByPass districtByPass, String startDate, String endDate) {
		districtByPass.setStartDate(LocalDate.parse(startDate));
		districtByPass.setEndDate(LocalDate.parse(endDate));
		shippingOrderService.saveOrUpdateDistrictClubOrdByPass(districtByPass);

	}
	
	public void getClubbedOrders(String channelSeq) throws MapOverLimitException,MapServiceRequestDeniedException {
		List<ChannelConfiguration> channelList            = orderService.getChannelsList(channelSeq);
		List<ShippingDetails1> shippingaOrderList         = shippingOrderService.getAllShippingOrders();
		for (ChannelConfiguration channelConfiguration : channelList) {
			String channelType = CommonUtility.getChannels(channelConfiguration.getChannel());
			String skuType = channelConfiguration.getSkuType();
			List<ShippingDetails1> ordersList = getAllOrdersBasedOnDistributionChannel(channelType, shippingaOrderList);
			// Map<String, >
			Collections.sort(ordersList, Comparator.comparing(ShippingDetails1::getDistrict_name));
			Map<String, List<ShippingDetails1>> ordersOnDistrictMap = orderService.getAllOrdersBasedOnDistricts(
					ordersList);
			if ("Same".equalsIgnoreCase(skuType)) {
				Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap = orderService.getAllGroupOrdersBasedOnSameMaterial(
						ordersOnDistrictMap);
				finalMaterialOrdMap = orderService.getAllForwordOrders(finalMaterialOrdMap);
				truckService.getOrderAssignTrucksWithSameMaterial(finalMaterialOrdMap);
			} else {// Multiple SKU's
				truckService.getOrderAssignTrucksWithDifferentMaterial(ordersOnDistrictMap);
			}
		}
	}
   /*@author Venkat
    * Description: This method is returns pending orders list based on channels i.e. 01,02..
    * parameters: int , list
    * Returns: List (Pending Orders list)
    */
	public List<ShippingDetails1> getAllOrdersBasedOnDistributionChannel(String distributionChannel,
			List<ShippingDetails1> shippingaOrderList) {
		List<ShippingDetails1> ordersList = null;
		if(distributionChannel.contains(ApplicationConstants.CONST_STRING_COMMA_SEP)) {
			String[] channels = distributionChannel.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
			if(channels.length == ApplicationConstants.CONST_INT_VALUE_TWO) {
				ordersList = shippingaOrderList.stream()
						.filter(order -> (order.getDistribution_channel().contains(channels[ApplicationConstants.CONST_NUMBER_ZERO]) 
								|| order.getDistribution_channel().contains(channels[ApplicationConstants.CONST_INT_VALUE_ONE] )))
						.collect(Collectors.toList());
			} else if(channels.length == ApplicationConstants.CONST_INT_VALUE_THREE) {
				ordersList = shippingaOrderList.stream()
						.filter(order -> (order.getDistribution_channel().contains(channels[ApplicationConstants.CONST_NUMBER_ZERO]) 
								|| order.getDistribution_channel().contains(channels[ApplicationConstants.CONST_INT_VALUE_ONE])
								|| order.getDistribution_channel().contains(channels[ApplicationConstants.CONST_INT_VALUE_TWO])))
						.collect(Collectors.toList());
			}
		} else {
			ordersList = shippingaOrderList.stream()
			.filter(order -> order.getDistribution_channel().contains(distributionChannel))
			.collect(Collectors.toList());
		}
		return ordersList;
	}

}

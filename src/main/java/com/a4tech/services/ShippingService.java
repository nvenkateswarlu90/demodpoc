package com.a4tech.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.map.service.MapService;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.AvailableTrucks;
import com.a4tech.shipping.model.ChannelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.IntellishipModelByMaterial;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.PlantDetails;
import com.a4tech.shipping.model.ShippingDeliveryOrder;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.ShippingOrdersReAssignModel;
import com.a4tech.shipping.model.TruckHistoryDetail;
import com.a4tech.util.ApplicationConstants;
import com.a4tech.util.CommonUtility;
import com.a4tech.util.TruckTypeInfo;

import saveShipping.StoreSpDetails;

@Service("shippingService")
public class ShippingService {
	@Autowired
	private OrderService orderService;
	@Autowired
	private TruckService truckService;
	MapService gmapDist = new MapService();
	StoreSpDetails sd = new StoreSpDetails();
	// static Map<String, String> allLongitudeAndLatitudes = new HashMap<>();
	@Autowired
	private IShippingOrder shippingOrderService;
	private Logger _LOGGER = Logger.getLogger(ShippingService.class);
	public static Map<String, String> latitudeAndLongitudeMap = new HashMap<>();

	public Map<String, Map<String, Map<Integer, List<ShippingDetails1>>>> getOrdersBasedOnDistence(
			Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrd) {
		PlantDetails plantDetails = sd.getAllPlantDetails().get(2);
		String plantLongAndLatiVal = plantDetails.getLatitude() + "," + plantDetails.getLongitude();
		Map<String, Map<String, Map<Integer, List<ShippingDetails1>>>> districtByMap = new HashMap<>();
		for (Map.Entry<String, Map<String, List<ShippingDetails1>>> materialGroups : finalMaterialOrd.entrySet()) {
			String districtName = materialGroups.getKey();
			Map<String, List<ShippingDetails1>> ordersOnDistricts = materialGroups.getValue();
			Map<String, Map<Integer, List<ShippingDetails1>>> matrialByMap = new HashMap<>();
			for (Map.Entry<String, List<ShippingDetails1>> ords : ordersOnDistricts.entrySet()) {
				String materialName = ords.getKey();
				List<ShippingDetails1> shippingDetails = ords.getValue();
				Map<Integer, List<ShippingDetails1>> distanceOrdersMap = new HashMap<>();
				List<ShippingDetails1> ordersList = null;
				for (ShippingDetails1 shippingDetails1 : shippingDetails) {
					try {
						double maxDistance = gmapDist.getMaxDistenceFromMultipleDestination(plantLongAndLatiVal,
								shippingDetails1.getShip_to_latt() + "," + shippingDetails1.getShip_to_long());
						Integer distanceBand = getDistanceBand(maxDistance);
						if (distanceOrdersMap.containsKey(distanceBand)) {
							ordersList = distanceOrdersMap.get(distanceBand);
							ordersList.add(shippingDetails1);
							distanceOrdersMap.put(distanceBand, ordersList);
						} else {
							ordersList = new ArrayList<>();
							ordersList.add(shippingDetails1);
							distanceOrdersMap.put(distanceBand, ordersList);
						}

					} catch (IOException e) {
						System.out.println("Unbale to calculate distence :" + e.getCause());
						e.printStackTrace();
					}

				}
				matrialByMap.put(materialName, distanceOrdersMap);
			}
			districtByMap.put(districtName, matrialByMap);
		}
		return districtByMap;
	}

	public Integer getDistanceBand(double distance) {
		Integer distanceBand;
		if (distance <= 50) {
			distanceBand = 50;
		} else if (distance >= 51 && distance <= 100) {
			distanceBand = 100;
		} else if (distance >= 101 && distance <= 150) {
			distanceBand = 150;
		} else if (distance >= 151 && distance <= 200) {
			distanceBand = 200;
		} else if (distance >= 201 && distance <= 250) {
			distanceBand = 250;
		} else if (distance >= 251 && distance <= 300) {
			distanceBand = 300;
		} else {// distance is greater than 300KM +
			distanceBand = 350;
		}
		return distanceBand;
	}

	public Map<String, Map<List<ShippingDetails1>, List<AvailableTrucks>>> getOrdersFitIntoTruck(
			Map<String, Map<String, Map<Integer, List<ShippingDetails1>>>> groupByDistance) {
		// List<TruckDetails> initialTruckInfoList =
		// shippingOrder.getAllTruckInfo();
		List<ShippingDetails1> unGroupOrderList = new ArrayList<>();
		List<AvailableTrucks> allAssignedTrucksList = new ArrayList<>();
		Map<String, Map<List<ShippingDetails1>, List<AvailableTrucks>>> finalTruckDetails = new HashMap<>();
		for (Map.Entry<String, Map<String, Map<Integer, List<ShippingDetails1>>>> ordGrpList : groupByDistance
				.entrySet()) {
			String districtName = ordGrpList.getKey();
			Map<String, Map<Integer, List<ShippingDetails1>>> vals = ordGrpList.getValue();
			Map<List<ShippingDetails1>, List<AvailableTrucks>> truckAndOrderMap = new HashMap<>();
			for (Map.Entry<String, Map<Integer, List<ShippingDetails1>>> distanceByGroup : vals.entrySet()) {
				String materialName = distanceByGroup.getKey();
				Map<Integer, List<ShippingDetails1>> vals1 = distanceByGroup.getValue();
				for (Map.Entry<Integer, List<ShippingDetails1>> ordList : vals1.entrySet()) {
					List<AvailableTrucks> trucksList = new ArrayList<>();
					Integer orderDistance = ordList.getKey();
					List<ShippingDetails1> ordsList = ordList.getValue();
					/*
					 * if (ordsList.size() == 1) {// if single order contain for same // material
					 * ,no need to group the // that product unGroupOrderList.add(ordsList.get(0));
					 * continue; }
					 */
					String truckType = TruckTypeInfo.getTruckLoadType(districtName);

					int totOrdQty = ordsList.stream().map(ShippingDetails1::getActual_delivery_qty)
							.mapToInt(Integer::valueOf).sum();
					if ("Normal Load".equals(truckType)) {// means extra load type
						trucksList = getHeavyTruckList(trucksList, totOrdQty, allAssignedTrucksList);
						allAssignedTrucksList.addAll(trucksList);
					} else {// Rated Load
						trucksList = getNormalTruckList(trucksList, totOrdQty, allAssignedTrucksList);
						allAssignedTrucksList.addAll(trucksList);
					}
					truckAndOrderMap.put(ordsList, trucksList);
				}
			}
			// Map<List<ShippingDetails1>, List<TruckDetails>> truckAndOrderMap = new
			// HashMap<>();

			finalTruckDetails.put(districtName, truckAndOrderMap);
		}
		return finalTruckDetails;
	}

	private List<AvailableTrucks> getHeavyTruckList(List<AvailableTrucks> groupTruckList, int totOrdQty,
			List<AvailableTrucks> allAssignedTrucksList) {
		List<AvailableTrucks> initialTruckInfoList = shippingOrderService.getAllAvilableTrucks();
		double initialTruckCap = totOrdQty - (totOrdQty * 0.5);
		AvailableTrucks initialTruckDetails = getTruckDetails(initialTruckInfoList, initialTruckCap);

		if (initialTruckDetails != null) {// if order qty is equal to truck
											// capacity
			initialTruckDetails = getOrderHeavyTruck(initialTruckInfoList, initialTruckDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(initialTruckDetails);
			groupTruckList.add(initialTruckDetails);
			return groupTruckList;
		}
		AvailableTrucks tDetails = Collections.max(initialTruckInfoList,
				Comparator.comparing(AvailableTrucks::getVehicleType));// maximum
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

	private AvailableTrucks getOrderHeavyTruck(List<AvailableTrucks> allTrucksList,
			AvailableTrucks truckDetails, List<AvailableTrucks> assignedTruckList) {
		/*
		 * if(CollectionUtils.isEmpty(assignedTruckList)){ return truckDetails; }
		 */
		String truckNo = truckDetails.getSlNo();
		int truckCapacity = truckDetails.getVehicleType();
		double maxCapacity = truckCapacity + (truckCapacity * 0.5);
		truckDetails.setVehicleType((int) maxCapacity);
		String allTruckNos = assignedTruckList.stream().map(AvailableTrucks::getSlNo)
				.collect(Collectors.joining(","));
		if (!allTruckNos.contains(truckNo)) {
			return truckDetails;
		} else {
			for (AvailableTrucks truckDtls : allTrucksList) {
				if (truckDtls.getVehicleType() == truckCapacity && !allTruckNos.contains(truckDtls.getSlNo())) {
					truckDtls.setVehicleType((int) maxCapacity);
					return truckDtls;
				}
			}
		}
		return truckDetails;
	}

	private AvailableTrucks getTruckDetails(List<AvailableTrucks> truckInfoList, double qty) {
		for (AvailableTrucks truckDetails : truckInfoList) {
			int truckCapacity = truckDetails.getVehicleType();
			double truckmaxCapacity = truckCapacity + (truckCapacity * 0.5);
			if (truckmaxCapacity == qty) {
				return truckDetails;
			}
		}
		return null;
	}

	private List<AvailableTrucks> getHeavyGroupTruckDetails(List<AvailableTrucks> truckInfoList, double qty,
			List<AvailableTrucks> truckGroupList, List<AvailableTrucks> allAssignedTrucksList) {
		if (qty < 8) {
			return truckGroupList;
		}
		AvailableTrucks maxTruckDetails = null;
		if (qty == 18.0) {
			maxTruckDetails = getMaxCapacityTruckDetails("", 12, truckInfoList);
		} else {
			maxTruckDetails = truckInfoList.stream().max(Comparator.comparing(AvailableTrucks::getVehicleType))
					.orElseThrow(NoSuchElementException::new);
		}

		int truckCapacity = maxTruckDetails.getVehicleType();
		if (truckCapacity == 27) {
			truckCapacity = 18;
		}
		String assignedTruckNo = allAssignedTrucksList.stream().map(AvailableTrucks::getSlNo)
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
			AvailableTrucks tt = truckInfoList.stream()
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

	private AvailableTrucks getMaxCapacityTruckDetails(String allAssignedTrucks, int truckCapacity,
			List<AvailableTrucks> truckDetails) {
		for (AvailableTrucks truckDetails2 : truckDetails) {
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

	private List<AvailableTrucks> getNormalTruckList(List<AvailableTrucks> groupTruckList, int orderQty,
			List<AvailableTrucks> allAssignedTrucksList) {
		List<AvailableTrucks> initialTruckInfoList = shippingOrderService.getAllAvilableTrucks();
		AvailableTrucks maxTruckDetails = initialTruckInfoList.stream()
				.max(Comparator.comparing(AvailableTrucks::getVehicleType))
				.orElseThrow(NoSuchElementException::new);
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
					AvailableTrucks truckDtls = getTruckDetails(initialTruckInfoList, remainingOrderQty,
							groupTruckList, allAssignedTrucksList);
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
					 * if(!isTruckGroup(groupTruckList,truckDetails.getSlNo())){ truckDetails =
					 * getOrderTruck(initialTruckInfoList, truckDetails, allAssignedTrucksList);
					 * allAssignedTrucksList.add(truckDetails); groupTruckList.add(truckDetails);
					 * break; } } else { getfinalTruckList(groupTruckList, remainingOrderQty); } }
					 */

				}
			} else {// if allOrderQty and truck capacity is same

			}
		} else {// if orser qty is low compare to truck max capacity
			AvailableTrucks tDetails = Collections.min(initialTruckInfoList,
					Comparator.comparing(AvailableTrucks::getVehicleType));
			tDetails = getOrderTruck(initialTruckInfoList, tDetails, allAssignedTrucksList);
			allAssignedTrucksList.add(tDetails);
			groupTruckList.add(tDetails);

		}
		return groupTruckList;
	}

	private AvailableTrucks getOrderTruck(List<AvailableTrucks> allTrucksList,
			AvailableTrucks truckDetails, List<AvailableTrucks> assignedTruckList) {
		/*
		 * if(CollectionUtils.isEmpty(assignedTruckList)){ return truckDetails; }
		 */
		String truckNo = truckDetails.getSlNo();
		int truckCapacity = truckDetails.getVehicleType();
		String allTruckNos = assignedTruckList.stream().map(AvailableTrucks::getSlNo)
				.collect(Collectors.joining(","));
		if (!allTruckNos.contains(truckNo)) {
			return truckDetails;
		} else {
			for (AvailableTrucks truckDtls : allTrucksList) {
				if (truckDtls.getVehicleType() == truckCapacity && !allTruckNos.contains(truckDtls.getSlNo())) {
					return truckDtls;
				}
			}
		}
		return truckDetails;
	}

	private AvailableTrucks getTruckDetails(List<AvailableTrucks> initialTruckInfoList, int orderQty,
			List<AvailableTrucks> groupTruckList, List<AvailableTrucks> allAssignedTrucksList) {

		for (AvailableTrucks truckDetails : initialTruckInfoList) {
			if (truckDetails.getVehicleType() == orderQty) {
				if (!isTruckGroup(groupTruckList, truckDetails.getSlNo())) {
					truckDetails = getOrderTruck(initialTruckInfoList, truckDetails, allAssignedTrucksList);
					return truckDetails;
				}
			}
		}
		return null;
	}

	private boolean isTruckGroup(List<AvailableTrucks> truckList, String truckNo) {
		String allTruckNos = truckList.stream().map(AvailableTrucks::getSlNo).collect(Collectors.joining(","));
		if (allTruckNos.contains(truckNo)) {
			return true;
		}
		return false;
	}

	public void getFinalOrdersClub(
			Map<String, Map<List<ShippingDetails1>, List<AvailableTrucks>>> finalTruckDetails) {
		List<String> trucksNoAssigned = new ArrayList<>();
		for (Map.Entry<String, Map<List<ShippingDetails1>, List<AvailableTrucks>>> data : finalTruckDetails
				.entrySet()) {
			String districtName = data.getKey();
			Map<List<ShippingDetails1>, List<AvailableTrucks>> ords = data.getValue();
			orderGroupByTruck1(ords, trucksNoAssigned);
		}
	}

	/// start
	private void orderGroupByTruck1(Map<List<ShippingDetails1>, List<AvailableTrucks>> groupTruckMap,
			List<String> trucksNoAssigned) {
		for (Map.Entry<List<ShippingDetails1>, List<AvailableTrucks>> allDetails : groupTruckMap.entrySet()) {
			List<ShippingDetails1> ordersList = allDetails.getKey();
			// List<ShippingDetails1> ordersList =
			// intellship.getOrderDetailsList();
			// ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).
			int allOrdsQtys = getAllOrdersQty(ordersList);
			List<AvailableTrucks> groupTruckDetails = allDetails.getValue();
			OrderGroup orderGroup = null;
			List<OrderGroup> orderGroupList = new ArrayList<>();
			Map<String, List<OrderGroup>> ordersMap = new HashMap<>();
			int totTrucks = groupTruckDetails.size();
			int trucksCount = 1;
			Map<String, List<OrderGroup>> ordersTruckMap = new HashMap<>();
			Date todaysDate = new Date();
			ShippingDeliveryOrder shippingDeliveryOrder = null;
			for (AvailableTrucks truckDetails : groupTruckDetails) {
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
					if (isOrderQtyTruck(ordersMap, orderQty, orderDetails.getDelivery())) {// check is order load in  truck or not,if order is loaded then skip those orders
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
						remaingTruckCapacity = getRemainingOrderqQty(ordersMap, orderDetails.getDelivery(),ordersList);
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

	//// end

	private int getAllOrdersQty(List<ShippingDetails1> shippingDetailsList) {
		int ordQty = 0;
		for (ShippingDetails1 shippingDetails1 : shippingDetailsList) {
			ordQty = ordQty + Integer.parseInt(shippingDetails1.getActual_delivery_qty());
		}
		return ordQty;
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

	private int getRemainingOrderqQty(Map<String, List<OrderGroup>> ordersMap, String delivaryNo,List<ShippingDetails1> shippingList) {
		List<OrderGroup> orderGroupList = ordersMap.get(delivaryNo);
		int finalOrdQty = 0;
		Map<String, String> shippingOrdQty = shippingList.stream()
				.collect(Collectors.toMap(ShippingDetails1::getDelivery, ShippingDetails1::getActual_delivery_qty));
		for (OrderGroup orderGroup : orderGroupList) {
			//int originalOrdQty = Integer.parseInt(orderGroup.getOriginalOrderQty());
			int originalOrdQty = Integer.parseInt(shippingOrdQty.get(orderGroup.getDelivaryNo()));
			int truckOrdQty = orderGroup.getTruckOrderQty();
			finalOrdQty = originalOrdQty - truckOrdQty;
		}

		return finalOrdQty;
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
			int truckCapacity = 0;
			if(orderGrpList.size() == 1) {
				continue;
			}
			for (OrderGroup orderGrup : orderGrpList) {
				shippingLatitudeAndLonitude.append(orderGrup.getLatitude()).append(",")
						.append(orderGrup.getLongitude());
				shippingLatitudeAndLonitude.append("|");
				
				if(orderGrup.getOriginalOrderQty() != null) {
					//truckOrdQty = truckOrdQty + Integer.parseInt(orderGrup.getOriginalOrderQty());
					totalOrdQty = totalOrdQty + Integer.parseInt(orderGrup.getOriginalOrderQty());
				}
				intellishModel.setMaterialType(orderGrup.getMaterialType());
				intellishModel.setTruckCapacity(orderGrup.getTruckCapacity());
				//intellishModel.setLoadType(loadType);
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
			/*if (ordQty != 0) {
				pedningQty = pedningQty - ordQty;
				totalOrdQty = totalOrdQty - ordQty;
			}*/
			try {

				String distenceAndHrs = MapService.getDistanceAndHrsMapStore(shippingLatitudeAndLonitude.toString());
				// String distenceAndHrs =
				// getDistanceAndHr(shippingLatitudeAndLonitude.toString());
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

			/*
			 * if(districName.equals("GODDA")){ distence = 295.2; } else { distence = 342.6;
			 * }
			 */
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
		/*
		 * DateTimeFormatter initialFormatter =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd"); DateTimeFormatter finalFormatter =
		 * DateTimeFormatter.ofPattern("dd-MM-yyyy"); String startDate1 =
		 * LocalDate.parse(startDate,initialFormatter).format(finalFormatter); String
		 * endDate1 = LocalDate.parse(endDate,initialFormatter).format(finalFormatter);
		 * districtByPass.setStartDate(LocalDate.parse(startDate1,finalFormatter));
		 * districtByPass.setEndDate(LocalDate.parse(endDate1,finalFormatter));
		 * 
		 * LocalDate ld = LocalDate.parse(startDate1);
		 * 
		 * LocalDate ld123 = LocalDate.parse(endDate1);
		 */
		shippingOrderService.saveOrUpdateDistrictClubOrdByPass(districtByPass);

	}

	public Map<String, Map<List<ShippingDetails1>, List<TruckHistoryDetail>>> getOrdersFitIntoTruck1(
			Map<String, Map<String, List<ShippingDetails1>>> materialOrdGroupMap) {
		// List<TruckDetails> initialTruckInfoList = shippingOrder.getAllTruckInfo();
		// allLongitudeAndLatitudes =
		// shippingOrderService.getAllLatitudeAndLongitudeVals();
		List<ShippingDetails1> unGroupOrderList = new ArrayList<>();
		List<TruckHistoryDetail> allAssignedTrucksList = new ArrayList<>();
		Map<String, Map<List<ShippingDetails1>, List<TruckHistoryDetail>>> finalTruckDetails = new HashMap<>();
		List<AvailableTrucks> availableTruckInfoList = shippingOrderService.getAllAvilableTrucks();
		/*
		 * Map<String, TruckDetails> availableTrucksMap =
		 * availableTruckInfoList.stream()
		 * .collect(Collectors.toMap(TruckDetails::getSlNo, truc -> truc));
		 */
		List<String> usedTrucks = new ArrayList<>();
		// List<AxleWheelnfoEntity> axleWheelerList =
		// shippingOrderService.getWheelTypeInfo(name);
		for (Map.Entry<String, Map<String, List<ShippingDetails1>>> ordGrpList : materialOrdGroupMap.entrySet()) {
			String districtName = ordGrpList.getKey();
			Map<String, List<ShippingDetails1>> vals = ordGrpList.getValue();
			Map<List<ShippingDetails1>, List<TruckHistoryDetail>> truckAndOrderMap = new HashMap<>();
			for (Map.Entry<String, List<ShippingDetails1>> ordList : vals.entrySet()) {
				List<TruckHistoryDetail> trucksList = new ArrayList<>();
				String materialName = ordList.getKey();
				List<ShippingDetails1> ordsList = ordList.getValue();
				// if(ordsList.size() == 1 &&
				// (Integer.parseInt(ordsList.get(0).getActual_delivery_qty()) <= 9)){//if
				// single order contain for same material ,no need to group the that product
				if (ordsList.size() == 1) {
					unGroupOrderList.add(ordsList.get(0));
					continue;
				}
				String truckType = TruckTypeInfo.getTruckLoadType(districtName);
				int totOrdQty = ordsList.stream().map(ShippingDetails1::getActual_delivery_qty)
						.mapToInt(Integer::valueOf).sum();
				List<TruckHistoryDetail> truckPreviousList = shippingOrderService
						.getAllPreviousTrucksByDistName(districtName);
				String axleType = getWheelerType(ordsList, totOrdQty);
				axleType = getAxleTypeWheeler(truckPreviousList, axleType, totOrdQty);
				truckPreviousList = getTruckHistoryDataByAxleType(truckPreviousList, axleType);
				if (totOrdQty <= 12) {
					TruckHistoryDetail trucksPre = getClosetTrunk(truckPreviousList, totOrdQty);
					int qtyDiff = trucksPre.getNormalLoad() - totOrdQty;
					if (qtyDiff >= 15) {
						break;
					}
				}
				List<TruckHistoryDetail> assignedTrucks = getAssignedTrucksFromOrder(totOrdQty, truckPreviousList,
						usedTrucks);

				/*
				 * if("Minimum".equals(truckType)){ trucksList = getNormalTruckList1(trucksList,
				 * totOrdQty,allAssignedTrucksList); allAssignedTrucksList.addAll(trucksList); }
				 * else{//maximum Extra trucksList = getHeavyTruckList(trucksList,
				 * totOrdQty,allAssignedTrucksList); allAssignedTrucksList.addAll(trucksList); }
				 */
				truckAndOrderMap.put(ordsList, assignedTrucks);
			}
			finalTruckDetails.put(districtName, truckAndOrderMap);
		}
		return finalTruckDetails;
	}

	private TruckHistoryDetail getAvailableTruckFromPreviousData(Map<String, AvailableTrucks> availableTrucksMap,
			List<TruckHistoryDetail> truckPreviousList, List<String> usedTrucks) {
		TruckHistoryDetail availableTruck = null;
		for (TruckHistoryDetail truckHistoryDetail : truckPreviousList) {
			if (availableTrucksMap.containsKey(truckHistoryDetail.getTruckNo())) {
				if (usedTrucks.contains(truckHistoryDetail.getTruckNo())) {// truck is already assigned then check with
																			// other truck
					continue;
				}
				availableTruck = truckHistoryDetail;
			}
		}
		return availableTruck;

	}

	private List<TruckHistoryDetail> getAssignedTrucksFromOrder(int originalOrderQty,
			List<TruckHistoryDetail> availableTrucks, List<String> assigndTruckNos) {

		List<TruckHistoryDetail> trucksAssignList = new ArrayList<>();// 25
		boolean isFirstTruck = true;
		// int minimumRatedLoad =
		// availableTrucks.stream().mapToInt(TruckHistoryDetail::getNormalLoad).min().getAsInt();
		// int maximumRatedLoad =
		// availableTrucks.stream().mapToInt(TruckHistoryDetail::getNormalLoad).max().getAsInt();

		TruckHistoryDetail initialTruck = null;
		TruckHistoryDetail closestTruck = getClosetTrunk(availableTrucks, originalOrderQty);
		int remainingQty = 0;
		// final int tempRemainingQty ;

		if (closestTruck != null) {
			remainingQty = originalOrderQty - closestTruck.getNormalLoad();
			trucksAssignList.add(closestTruck);
			// availableTrucks.remove(closestTruck);
			assigndTruckNos.add(closestTruck.getTruckNo());
			if (remainingQty > 10) {
				TruckHistoryDetail closestTruck1 = getClosetTrunk(availableTrucks, remainingQty);
				if (!assigndTruckNos.contains(closestTruck1.getTruckNo())) {
					trucksAssignList.add(closestTruck1);
					assigndTruckNos.add(closestTruck1.getTruckNo());
					remainingQty = remainingQty - closestTruck1.getNormalLoad();
				}
			}

		} else {
			remainingQty = originalOrderQty;
		}
		// int tempQty = originalOrderQty;
		// tempRemainingQty = remainingQty;
		if (remainingQty < 10) {
			// no need to assign any truck if qty is less than 10 tones ,this order goes
			// into another cycle
		} else {
			for (TruckHistoryDetail availableTruck : availableTrucks) {
				if (assigndTruckNos.contains(availableTruck.getTruckNo())) {// truck is already assign for orders then
																			// check with another available trucks
					continue;
				}
				// int tempQty = remainingQty - availableTruck.getNormalLoad();
				int availQty = availableTruck.getNormalLoad();
				if (remainingQty == availQty) {
					trucksAssignList.add(availableTruck);
					break;
				} else {
					if (remainingQty < 10) {
						break;
					} else {
						int tempMaxQty = 0;
						if (remainingQty > availQty) {
							tempMaxQty = remainingQty - availQty;
							if (tempMaxQty >= 10 && tempMaxQty < 20) {
								trucksAssignList.add(availableTruck);
								remainingQty = remainingQty - availQty;
							}
						} else {// if(availQty > remainingQty)
							tempMaxQty = availQty - remainingQty;
							if (tempMaxQty <= 2) {
								trucksAssignList.add(availableTruck);
								remainingQty = remainingQty - availQty;
							}
						}
					}

				}

			}
		}

		return trucksAssignList;
	}

	private TruckHistoryDetail getClosetTrunk(List<TruckHistoryDetail> availableTrucks, int qty) {
		TruckHistoryDetail closestTruck = null;
		try {
			closestTruck = availableTrucks.stream()

					.min((f1, f2) -> Math.abs(f1.getNormalLoad() - qty) - Math.abs(f2.getNormalLoad() - qty)).get();
			return closestTruck;
		} catch (NullPointerException nce) {
			_LOGGER.error("unable to find closet truck from given available trucks: " + nce.getMessage());
		} catch (Exception e) {
			_LOGGER.error("unable to find closet truck from given available trucks: " + e.getMessage());
		}

		return closestTruck;
	}

	public void getFinalOrdersClub1(
			Map<String, Map<List<ShippingDetails1>, List<TruckHistoryDetail>>> finalTruckDetails) {
		List<String> trucksNoAssigned = new ArrayList<>();
		for (Map.Entry<String, Map<List<ShippingDetails1>, List<TruckHistoryDetail>>> data : finalTruckDetails
				.entrySet()) {
			String districtName = data.getKey();
			Map<List<ShippingDetails1>, List<TruckHistoryDetail>> ords = data.getValue();
			orderGroupByTruck2(ords, trucksNoAssigned);
		}
	}

	private void orderGroupByTruck2(Map<List<ShippingDetails1>, List<TruckHistoryDetail>> groupTruckMap,
			List<String> trucksNoAssigned) {
		Map<String, List<OrderGroup>> ordersTruckMap = new HashMap<>();
		for (Map.Entry<List<ShippingDetails1>, List<TruckHistoryDetail>> allDetails : groupTruckMap.entrySet()) {
			List<ShippingDetails1> ordersList = allDetails.getKey();
			// List<ShippingDetails1> ordersList =
			// intellship.getOrderDetailsList();
			// ordersList.stream().map(ShippingDetails1::getActual_delivery_qty).
			int allOrdsQtys = getAllOrdersQty(ordersList);
			List<TruckHistoryDetail> groupTruckDetails = allDetails.getValue();
			OrderGroup orderGroup = null;
			List<OrderGroup> orderGroupList = new ArrayList<>();
			Map<String, List<OrderGroup>> ordersMap = new HashMap<>();
			int totTrucks = groupTruckDetails.size();
			int trucksCount = 1;
			
			Date todaysDate = new Date();
			ShippingDeliveryOrder shippingDeliveryOrder = null;
			for (TruckHistoryDetail truckDetails : groupTruckDetails) {
				shippingDeliveryOrder = new ShippingDeliveryOrder();
				int initialOrder = 1;
				int truckCapacity = truckDetails.getNormalLoad();
				int ordersQtyTruck = 0;
				shippingDeliveryOrder.setTruckNo(truckDetails.getTruckNo());
				shippingDeliveryOrder.setShippingDeliveryDate(todaysDate);
				int shippingDelivaryId = shippingOrderService.generateShippingOrderId(shippingDeliveryOrder);
				ShippingOrdersReAssignModel shippingReorder = null;
				for (ShippingDetails1 orderDetails : ordersList) {
					shippingReorder = convertShippingDetailsIntoAnother(orderDetails, truckDetails.getTruckNo());
					orderGroup = new OrderGroup();
					int orderQty = Integer.parseInt(orderDetails.getActual_delivery_qty());
					if (isFullTruck(ordersTruckMap, truckCapacity, truckDetails.getTruckNo())) {
						break;
					}
					//// check is order load in truck or not,if order is loaded then skip those
					//// order
					if (isOrderQtyTruck(ordersMap, orderQty, orderDetails.getDelivery())) {
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
						remaingTruckCapacity = getRemainingOrderqQty(ordersMap, orderDetails.getDelivery(),ordersList);
						if (trucksCount != totTrucks && isLessOrderQty(ordersList, ordersMap, remaingTruckCapacity)) {
							continue;
						}
						if (initialOrder == 1) {
							orderQty = remaingTruckCapacity;
						}
					}
					orderGroup.setDelivaryNo(orderDetails.getDelivery());
					//orderGroup.setOriginalOrderQty(orderDetails.getActual_delivery_qty());
					orderGroup.setTruckNo(truckDetails.getTruckNo());
					orderGroup.setTruckCapacity(String.valueOf(truckDetails.getNormalLoad()));
					orderGroup.setMaterialType(orderDetails.getMaterial());
					orderGroup.setDistrictName(orderDetails.getDistrict_name());
					orderGroup.setLatitude(orderDetails.getShip_to_latt());
					orderGroup.setLongitude(orderDetails.getShip_to_long());
					orderGroup.setNameShipToParty(orderDetails.getName_of_sold_to_party());
					orderGroup.setShippingDelivaryId(shippingDelivaryId);
					orderGroup.setWheelerType(truckDetails.getWheelerType());
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
						shippingReorder.setActual_delivery_qty(String.valueOf(orderQty));
						orderGroup.setOriginalOrderQty(String.valueOf(orderQty));
					} else {
						orderGroup.setTruckOrderQty(remaingTruckCapacity);
						ordersQtyTruck = ordersQtyTruck + remaingTruckCapacity;
						shippingReorder.setActual_delivery_qty(String.valueOf(remaingTruckCapacity));
						orderGroup.setOriginalOrderQty(String.valueOf(remaingTruckCapacity));
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
					shippingReorder.setTruckNo(truckDetails.getTruckNo());

					initialOrder++;
					shippingOrderService.saveShippingOrderReAssign(shippingReorder);
					if (ordersQtyTruck == truckCapacity
							|| isOrderQtyTruck(ordersMap, allOrdsQtys, orderDetails.getDelivery())) {// truck
																										// is
																										// full
																										// with
																										// orders
						break;
					}
				}
				//ordersTruckMap = new HashMap<>();
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

	private ShippingOrdersReAssignModel convertShippingDetailsIntoAnother(ShippingDetails1 ship, String truckNo) {
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

	private String getTruckLoadType(String districtName, String truckNo) {
		TruckHistoryDetailsEntity truckDetails = shippingOrderService.getDistrictDetails(districtName, truckNo);
		if (truckDetails.getNormalLoad() == truckDetails.getRatedLoad()) {
			return "Rated Load";
		} else {
			return "Normal Load";
		}

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

	private String getWheelerType(List<ShippingDetails1> ordersList, int totOrdQty) {
		double distance = getOrdersDistance(ordersList);
		String wheelerType = "";
		if (distance <= 180) {
			wheelerType = "6W";
		} else {
			if (totOrdQty > 15 && totOrdQty <= 30) {
				wheelerType = "10W";
			} else if (totOrdQty > 30 && totOrdQty <= 35) {
				wheelerType = "12W";
			} else if (totOrdQty > 35 && totOrdQty <= 50) {
				wheelerType = "14W";
			} else if (totOrdQty > 50) {
				wheelerType = "18W";
			} else {

			}
		}
		return wheelerType;
	}

	private double getOrdersDistance(List<ShippingDetails1> ordersList) {
		PlantDetails plantDetails = sd.getAllPlantDetails().get(2);
		StringBuilder ordersLongitudeAndLatitude = new StringBuilder();
		StringBuilder plantLongAndLati = new StringBuilder();
		plantLongAndLati.append(plantDetails.getLatitude()).append(",").append(plantDetails.getLongitude());
		for (ShippingDetails1 shippingDetails1 : ordersList) {
			ordersLongitudeAndLatitude.append(shippingDetails1.getShip_to_latt()).append(",")
					.append(shippingDetails1.getShip_to_long()).append("|");
		}
		Double maxDistance = 0.0;
		try {
			maxDistance = MapService.getDistanceMapStore(ordersLongitudeAndLatitude.toString());
			// maxDistance = getDistance(ordersLongitudeAndLatitude.toString());
			// shippingOrderService
			if (maxDistance == null) {
				maxDistance = gmapDist.getMaxDistenceFromMultipleDestination(plantLongAndLati.toString(),
						ordersLongitudeAndLatitude.toString());
				/*
				 * LangitudeAndLatitudeMap lat = new LangitudeAndLatitudeMap();
				 * lat.setLatitudeAndLongitude(ordersLongitudeAndLatitude.toString());
				 * lat.setDistance(String.valueOf(maxDistance));
				 * shippingOrderService.saveLatitudeAndLongitudeVals(lat);
				 */
				MapService.saveDistanceMapStore(ordersLongitudeAndLatitude.toString(), maxDistance);
			}
		} catch (IOException e) {
			_LOGGER.error("Unbale to calculate distance between 2 places: " + e.getMessage());
		}
		return maxDistance;
	}

	private List<TruckHistoryDetail> getTruckHistoryDataByAxleType(List<TruckHistoryDetail> truckHistoryList,
			String axleType) {
		return truckHistoryList.stream().filter(truck -> truck.getWheelerType().contains(axleType))
				.collect(Collectors.toList());
	}

	private String getAxleTypeWheeler(List<TruckHistoryDetail> truckPreviousList, String whellerType, int totOrdQty) {
		Set<String> whellerList = truckPreviousList.stream().map(truck -> truck.getWheelerType())
				.collect(Collectors.toSet());
		if (!whellerList.contains(whellerType)) {
			if (whellerType.equalsIgnoreCase("6W")) {
				if (totOrdQty > 50) {
					if (whellerList.contains("18W")) {
						return "18W";
					}
				}
			} else {
				whellerList.remove("6W");
				if (totOrdQty > 50) {
					if (whellerList.contains("18W")) {
						return "18W";
					}
				} else {
					return whellerList.iterator().next();
				}
			}
		}
		return whellerType;
	}
   
	public void getClubbedOrders(String channelSeq) {
		List<ChannelConfiguration> channelList            = orderService.getChannelsList(channelSeq);
		List<ShippingDetails1> shippingaOrderList         = shippingOrderService.getAllShippingOrders();
		for (ChannelConfiguration channelConfiguration : channelList) {
			String channelType = CommonUtility.getChannels(channelConfiguration.getChannel());
			String skuType = channelConfiguration.getSkuType();
			List<ShippingDetails1> ordersList = getAllOrdersBasedOnDistributionChannel(channelType, shippingaOrderList);
			// Map<String, >
			Collections.sort(ordersList, Comparator.comparing(ShippingDetails1::getDistrict_name));
			if ("Same".equalsIgnoreCase(skuType)) {
				Map<String, List<ShippingDetails1>> ordersOnDistrictMap = orderService.getAllOrdersBasedOnDistricts(
						ordersList);
				Map<String, Map<String, List<ShippingDetails1>>> finalMaterialOrdMap = orderService.getAllOrdersBasedOnMaterial(
						ordersOnDistrictMap);
				finalMaterialOrdMap = orderService.getAllForwordOrders(finalMaterialOrdMap);
				truckService.getOrderAssignTrucks(finalMaterialOrdMap);
				
				
			} else {// Multiple SKU's

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

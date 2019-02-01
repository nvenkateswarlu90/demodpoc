package com.a4tech.shipping.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a4tech.dao.entity.DistrictClubOrdByPassEntity;
import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.DistrictWiseNormalLoadCapacity;
import com.a4tech.dao.entity.OrderGroupEntity;
import com.a4tech.dao.entity.ShippingDeliveryOrderEntity;
import com.a4tech.dao.entity.ShippingEntity;
import com.a4tech.dao.entity.TruckDetailsEntity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.map.model.Address;
import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.ishippingDao.IshippingOrderDao;
import com.a4tech.shipping.model.AxleWheelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.NormalLoadConfiguration;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.ShippingDeliveryOrder;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.TruckDetails;

@Service
public class ShippingOrderImpl implements IShippingOrder {

	@Autowired
	private IshippingOrderDao shippingOrderDao;

	@Override
	public List<ShippingDetails1> getAllShippingOrders() {
		List<ShippingEntity> shippingOrdersList = shippingOrderDao.listAllData(ShippingEntity.class);
		List<ShippingDetails1> shippingOrderList = new ArrayList<>();
		ShippingDetails1 shippingDetails = null;
		for (ShippingEntity shippingEntity : shippingOrdersList) {
			shippingDetails = new ShippingDetails1();
			shippingDetails.setDelivery(shippingEntity.getDelivery());
			shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
			shippingDetails.setDeference_document(shippingEntity.getDeference_document());
			shippingDetails.setSold_to_party(shippingEntity.getSold_to_party());
			shippingDetails.setName_of_sold_to_party(shippingEntity.getName_of_sold_to_party());
			shippingDetails.setName_of_the_ship_to_party(shippingEntity.getName_of_the_ship_to_party());
			shippingDetails.setMaterial(shippingEntity.getMaterial());
			shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
			shippingDetails.setRoute_description(shippingEntity.getRoute_description());
			shippingDetails.setDistrict_name(shippingEntity.getDistrict_name());
			shippingDetails.setPlant(shippingEntity.getPlant());
			shippingDetails.setRoute(shippingEntity.getRoute());
			shippingDetails.setForwarding_agent_name(shippingEntity.getForwarding_agent_name());
			shippingDetails.setDistribution_channel(shippingEntity.getDistribution_channel());
			shippingDetails.setDeliv_date(shippingEntity.getDeliv_date());
			shippingDetails.setDelivery_type(shippingEntity.getDelivery_type());
			shippingDetails.setShipping_Point(shippingEntity.getShipping_Point());
			shippingDetails.setDistrict_code(shippingEntity.getDistrict_code());
			shippingDetails.setShip_to_party(shippingEntity.getShip_to_party());
			shippingDetails.setShip_to_long(shippingEntity.getShip_to_long());
			shippingDetails.setShip_to_latt(shippingEntity.getShip_to_latt());
			shippingOrderList.add(shippingDetails);
		}

		return shippingOrderList;
	}


	@Override
	public List<TruckHistoryDetailsEntity> getAllTrucksHistoryDetails() {

		return shippingOrderDao.listAllData(TruckHistoryDetailsEntity.class);
	/*List<TruckHistoryDetailsEntity> truckEntityDetailsList = shippingOrderDao.getAllTrucksHistoryDetails();
	List<TruckHistoryDetail> truckHistoryDetailList = new ArrayList<>();
	TruckHistoryDetail truckDetailsObj = null;
	for (TruckHistoryDetailsEntity truck : truckEntityDetailsList) {
		 truckDetailsObj = new TruckHistoryDetail();
		 truckDetailsObj.setTruckNo(truck.getTruckNo());
		 truckDetailsObj.setNormalLoad(truck.getNormalLoad());
		 truckDetailsObj.setRatedLoad(truck.getRatedLoad());
		 truckDetailsObj.setDistrictCode(truck.getDistrictCode());
		 truckDetailsObj.setDistrictName(truck.getDistrictName());
		 truckDetailsObj.setSr_No(truck.getSr_No());
		 if(truck.getLastTransactionOfNormalLoad() != null){
		 truckDetailsObj.setLastTransactionOfNormalLoad(truck.getLastTransactionOfNormalLoad());
		 }
		 truckHistoryDetailList.add(truckDetailsObj);
		}

			return truckHistoryDetailList;
*/		}


	@Override
	public List<ShippingDetails1> getShippingDetailsByDate(String date) {
		List<ShippingEntity> shippingOrdersList = shippingOrderDao.getShippingDetailsByDate(date);
		List<ShippingDetails1> shippingOrderList = new ArrayList<>();
		ShippingDetails1 shippingDetails = null;
		for (ShippingEntity shippingEntity : shippingOrdersList) {
			shippingDetails = new ShippingDetails1();
			shippingDetails.setDelivery(shippingEntity.getDelivery());
			shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
			shippingDetails.setDeference_document(shippingEntity.getDeference_document());
			shippingDetails.setSold_to_party(shippingEntity.getSold_to_party());
			shippingDetails.setName_of_sold_to_party(shippingEntity.getName_of_sold_to_party());
			shippingDetails.setName_of_the_ship_to_party(shippingEntity.getName_of_the_ship_to_party());
			shippingDetails.setMaterial(shippingEntity.getMaterial());
			shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
			shippingDetails.setRoute_description(shippingEntity.getRoute_description());
			shippingDetails.setDistrict_name(shippingEntity.getDistrict_name());
			shippingDetails.setPlant(shippingEntity.getPlant());
			shippingDetails.setRoute(shippingEntity.getRoute());
			shippingDetails.setForwarding_agent_name(shippingEntity.getForwarding_agent_name());
			shippingDetails.setDistribution_channel(shippingEntity.getDistribution_channel());
			shippingDetails.setDeliv_date(shippingEntity.getDeliv_date());
			shippingDetails.setDelivery_type(shippingEntity.getDelivery_type());
			shippingDetails.setShipping_Point(shippingEntity.getShipping_Point());
			shippingDetails.setDistrict_code(shippingEntity.getDistrict_code());
			shippingDetails.setShip_to_party(shippingEntity.getShip_to_party());
			shippingDetails.setShip_to_long(shippingEntity.getShip_to_long());
			shippingDetails.setShip_to_latt(shippingEntity.getShip_to_latt());
			shippingOrderList.add(shippingDetails);
		}
		return shippingOrderList;

	}

	@Override
	public List<TruckDetails> getAllTruckInfo() {
		List<TruckDetailsEntity> truckDetailsEntity = shippingOrderDao.listAllData(TruckDetailsEntity.class);
		List<TruckDetails> truckDetailsList = new ArrayList<>();
		TruckDetails truckDetailObj = null;
		for (TruckDetailsEntity truckDtlsEntity : truckDetailsEntity) {
			truckDetailObj = new TruckDetails();
			truckDetailObj.setDelay(truckDtlsEntity.getDelay());
			truckDetailObj.setDoNo(truckDtlsEntity.getDoNo());
			truckDetailObj.setEntryType(truckDtlsEntity.getEntryType());
			// truckDetailObj.setRefTruckVendorId(truckDtlsEntity.getRefTruckVendorId());
			truckDetailObj.setSlNo(truckDtlsEntity.getSlNo());
			truckDetailObj.setVehicleType(truckDtlsEntity.getVehicleType());
			truckDetailObj.setVehiclNo(truckDtlsEntity.getVehiclNo());
			truckDetailObj.setWheels(truckDtlsEntity.getWheels());
			truckDetailObj.setTaggedDate(truckDtlsEntity.getTaggedDate());
			truckDetailObj.setTaggedTime(truckDtlsEntity.getTaggedTime());
			truckDetailObj.setTaggedTranspoter(truckDtlsEntity.getTaggedTranspoter());
			truckDetailsList.add(truckDetailObj);
		}
		return truckDetailsList;
	}

	@Override
	public void saveOrderGroup(OrderGroup orderGroup) {
		OrderGroupEntity ordGrpEntity = new OrderGroupEntity();
		ordGrpEntity.setDelivaryDate(orderGroup.getDelivaryDate());
		ordGrpEntity.setDelivaryNo(orderGroup.getDelivaryNo());
		ordGrpEntity.setOriginalOrderQty(orderGroup.getOriginalOrderQty());
		ordGrpEntity.setTruckCapacity(orderGroup.getTruckCapacity());
		ordGrpEntity.setTruckNo(orderGroup.getTruckNo());
		ordGrpEntity.setTruckOrderQty(String.valueOf(orderGroup.getTruckOrderQty()));
		ordGrpEntity.setMaterialName(orderGroup.getMaterialType());
		ordGrpEntity.setDistrictName(orderGroup.getDistrictName());
		ordGrpEntity.setLatitude(orderGroup.getLatitude());
		ordGrpEntity.setLongitude(orderGroup.getLongitude());
		ordGrpEntity.setNameShipToParty(orderGroup.getNameShipToParty());
		ordGrpEntity.setOrder_shipping_date(orderGroup.getOrderShippingDate());
		ordGrpEntity.setShippingDelivaryId(orderGroup.getShippingDelivaryId());
		shippingOrderDao.saveOrderGroup(ordGrpEntity);
	}

	@Override
	public List<OrderGroup> getAllGroupOrderList() {
		List<OrderGroupEntity> orderGroupEntityList = shippingOrderDao.listAllData(OrderGroupEntity.class);
		List<OrderGroup> orderGroupList = new ArrayList<>();
		OrderGroup orderGroup = null;
		for (OrderGroupEntity ordGrpEntity : orderGroupEntityList) {
			orderGroup = new OrderGroup();
			orderGroup.setDelivaryNo(ordGrpEntity.getDelivaryNo());
			orderGroup.setDelivaryDate(ordGrpEntity.getDelivaryDate());
			orderGroup.setOriginalOrderQty(ordGrpEntity.getOriginalOrderQty());
			orderGroup.setTruckNo(ordGrpEntity.getTruckNo());
			orderGroup.setTruckCapacity(ordGrpEntity.getTruckCapacity());
			orderGroup.setTruckOrderQty(Integer.parseInt(ordGrpEntity.getTruckOrderQty()));
			orderGroup.setMaterialType(ordGrpEntity.getMaterialName());
			orderGroup.setDistrictName(ordGrpEntity.getDistrictName());
			orderGroup.setLatitude(ordGrpEntity.getLatitude());
			orderGroup.setLongitude(ordGrpEntity.getLongitude());
			orderGroupList.add(orderGroup);
		}
		return orderGroupList;
	}

	@Override
	public List<OrderGroup> getOrderGroupByDate(String date) {
		List<OrderGroupEntity> orderGroupEntityList = shippingOrderDao.getOrderGroupByDate(date);
		List<OrderGroup> orderGroupList = new ArrayList<>();
		OrderGroup orderGroup = null;
		for (OrderGroupEntity ordGrpEntity : orderGroupEntityList) {
			orderGroup = new OrderGroup();
			orderGroup.setDelivaryNo(ordGrpEntity.getDelivaryNo());
			orderGroup.setDelivaryDate(ordGrpEntity.getDelivaryDate());
			orderGroup.setOriginalOrderQty(ordGrpEntity.getOriginalOrderQty());
			orderGroup.setTruckNo(ordGrpEntity.getTruckNo());
			orderGroup.setTruckCapacity(ordGrpEntity.getTruckCapacity());
			orderGroup.setTruckOrderQty(Integer.parseInt(ordGrpEntity.getTruckOrderQty()));
			orderGroupList.add(orderGroup);
		}
		return orderGroupList;
	}

	@Override
	public ShippingDetails1 getShippingDetails(String orderNo) {
		ShippingEntity shippingEntity = shippingOrderDao.getShippingDetails(orderNo);
		ShippingDetails1 shippingDetails = new ShippingDetails1();
		shippingDetails.setDelivery(shippingEntity.getDelivery());
		shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
		shippingDetails.setDeference_document(shippingEntity.getDeference_document());
		shippingDetails.setSold_to_party(shippingEntity.getSold_to_party());
		shippingDetails.setName_of_sold_to_party(shippingEntity.getName_of_sold_to_party());
		shippingDetails.setName_of_the_ship_to_party(shippingEntity.getName_of_the_ship_to_party());
		shippingDetails.setMaterial(shippingEntity.getMaterial());
		shippingDetails.setActual_delivery_qty(shippingEntity.getActual_delivery_qty());
		shippingDetails.setRoute_description(shippingEntity.getRoute_description());
		shippingDetails.setDistrict_name(shippingEntity.getDistrict_name());
		shippingDetails.setPlant(shippingEntity.getPlant());
		shippingDetails.setRoute(shippingEntity.getRoute());
		shippingDetails.setForwarding_agent_name(shippingEntity.getForwarding_agent_name());
		shippingDetails.setDistribution_channel(shippingEntity.getDistribution_channel());
		shippingDetails.setDeliv_date(shippingEntity.getDeliv_date());
		shippingDetails.setDelivery_type(shippingEntity.getDelivery_type());
		shippingDetails.setShipping_Point(shippingEntity.getShipping_Point());
		shippingDetails.setDistrict_code(shippingEntity.getDistrict_code());
		shippingDetails.setShip_to_party(shippingEntity.getShip_to_party());
		shippingDetails.setShip_to_long(shippingEntity.getShip_to_long());
		shippingDetails.setShip_to_latt(shippingEntity.getShip_to_latt());
		return shippingDetails;
	}

	@Override
	public List<Address> getLatitudeAndLongitude(String truckNo) {
		List<OrderGroupEntity> orderGroupEntityList = shippingOrderDao.getLatitudeAndLongitude(truckNo);
		List<Address> addressGroList = new ArrayList<>();
		addressGroList.add(getPlantAddress());
		Address address = null;
		for (OrderGroupEntity ordGrpEntity : orderGroupEntityList) {
			address = new Address();
			address.setLatitude(ordGrpEntity.getLatitude());
			address.setLongitude(ordGrpEntity.getLongitude());
			address.setPlaceName(ordGrpEntity.getNameShipToParty());
			addressGroList.add(address);
		}
		return addressGroList;
	}

	@Override
	public void deleteAllGroupOrders() {
		shippingOrderDao.deleteAllGroupOrders();
	}

	@Override
	public void updateOrderGroupFlag(String delivaryNo) {
		shippingOrderDao.updateOrderGroupFlag(delivaryNo);
	}

	@Override
	public List<String> getOrderNoByTruck(String truckNo) {
		return shippingOrderDao.getOrderNoByTruck(truckNo);
	}

	@Override
	public int generateShippingOrderId(ShippingDeliveryOrder shippingDelivary) {
		ShippingDeliveryOrderEntity shippingDelOrd = new ShippingDeliveryOrderEntity();
		shippingDelOrd.setTruckNo(shippingDelivary.getTruckNo());
		shippingDelOrd.setShippingDeliveryDate(shippingDelivary.getShippingDeliveryDate());
		int deliveryId = shippingOrderDao.generateShippingOrderId(shippingDelOrd);
		return deliveryId;
	}

	@Override
	public List<DistrictWiseNormalLoadCapacity> getAllDistrictWiseLoads() {
		return shippingOrderDao.listAllData(DistrictWiseNormalLoadCapacity.class);
	}
		

	@Override
	public DistrictWiseNormalLoadCapacity getDistrictTruckLoad(String districtName) {
		return shippingOrderDao.getDistrictTruckLoad(districtName);
	}
	@Override
	public void saveDistrictWiseNormalLoad(NormalLoadConfiguration normal) {
     DistrictWiseNormalLoadCapacity normalLoadEntity = new DistrictWiseNormalLoadCapacity();
     normalLoadEntity.setDistrictName(normal.getDistrictName());
     normalLoadEntity.setRatedLoad(normal.getRatedLoad());
     normalLoadEntity.setTruckOverLoading(normal.getNormalLoad());
     normalLoadEntity.setTruckOverLoadingtonns(normal.getNormalLoadInTonns());
     shippingOrderDao.saveDistrictWiseNormalLoad(normalLoadEntity);
	}
	@Override
	public void updateDistrictWiseNormalLoad(NormalLoadConfiguration normal) {
		DistrictWiseNormalLoadCapacity normalLoadEntity = new DistrictWiseNormalLoadCapacity();
	     normalLoadEntity.setDistrictName(normal.getDistrictName());
	     normalLoadEntity.setRatedLoad(normal.getRatedLoad());
	     normalLoadEntity.setTruckOverLoading(normal.getNormalLoad());
	     normalLoadEntity.setTruckOverLoadingtonns(normal.getNormalLoadInTonns());
	     shippingOrderDao.updateDistrictWiseNormalLoad(normalLoadEntity);
	}
	private Address getPlantAddress() {
		Address address = new Address();
		address.setLatitude("23.7012517");
		address.setLongitude("86.0591489");
		address.setPlaceName("Dalmia Cement,JHARKHAND");
		return address;
	}



	@Override
	public List<TruckHistoryDetailsEntity> getSearchTrucksHistoryDetails(String value,String type) {

		return shippingOrderDao.getSearchTrucksHistoryDetails(value,type);
	}


	@Override
	public void saveAxleWheelConfiguration(
			AxleWheelConfiguration axleWheelConfig) {
		
		AxleWheelTypeEntity wheelEntity=new AxleWheelTypeEntity(); 
		wheelEntity.setAxlewheelerid(axleWheelConfig.getAxlewheelerid());
		wheelEntity.setAxlewheelertype(axleWheelConfig.getAxlewheelertype());
		shippingOrderDao.saveAxleWheelConfiguration(wheelEntity);

	}
	public void saveDistrictClubOrdByPass(DistrictClubOrdByPass districtByPass) {
		// TODO Auto-generated method stub
		DistrictClubOrdByPassEntity distByPassEnti = new DistrictClubOrdByPassEntity();
		distByPassEnti.setDistrictCode(districtByPass.getDistrictCode());
		distByPassEnti.setDistrictName(districtByPass.getDistrictName());
		distByPassEnti.setStartDate(districtByPass.getStartDate());
		distByPassEnti.setEndDate(districtByPass.getEndDate());
		shippingOrderDao.saveDistrictClubOrdByPass(distByPassEnti);
	}


	@Override
	public List<DistrictClubOrdByPass> getAllDistrictClubOrdByPass() {
		
		List<DistrictClubOrdByPassEntity> distList =  shippingOrderDao.listAllData(DistrictClubOrdByPassEntity.class);
		List<DistrictClubOrdByPass> districtBypassList = new ArrayList<>();
		DistrictClubOrdByPass distObj = null;
		for (DistrictClubOrdByPassEntity districtClubOrdByPassEntity : distList) {
			distObj = new DistrictClubOrdByPass();
			distObj.setDistrictName(districtClubOrdByPassEntity.getDistrictName());
			distObj.setDistrictCode(districtClubOrdByPassEntity.getDistrictCode());
			distObj.setStartDate(districtClubOrdByPassEntity.getStartDate());
			distObj.setEndDate(districtClubOrdByPassEntity.getEndDate());
			districtBypassList.add(distObj);
		}
		return districtBypassList;
	}


	@Override
	public AxleWheelTypeEntity getAxlewheel(String wheelType) {
		
		return shippingOrderDao.getAxlewheel(wheelType);
	}


	@Override
	public List<AxleWheelTypeEntity> getAllAxleWheelTypeEntity() {
		// TODO Auto-generated method stub
		return shippingOrderDao.listAllData(AxleWheelTypeEntity.class);
	}




}

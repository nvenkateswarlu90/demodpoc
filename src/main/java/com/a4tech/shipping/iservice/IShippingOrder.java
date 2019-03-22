package com.a4tech.shipping.iservice;

import java.util.List;
import java.util.Map;

import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.dao.entity.DistrictClubOrdByPassEntity;
import com.a4tech.dao.entity.DistrictWiseNormalLoadCapacity;
import com.a4tech.dao.entity.LangitudeAndLatitudeMap;
import com.a4tech.dao.entity.ShippingFinalOrders;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.dao.entity.UserEntity;
import com.a4tech.map.model.Address;
import com.a4tech.shipping.model.AxleWheelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;
import com.a4tech.shipping.model.IntellishipModelByMaterial;
import com.a4tech.shipping.model.NormalLoadConfiguration;
import com.a4tech.shipping.model.OrderGroup;
import com.a4tech.shipping.model.ShippingDeliveryOrder;
import com.a4tech.shipping.model.ShippingDetails1;
import com.a4tech.shipping.model.ShippingOrdersReAssignModel;
import com.a4tech.shipping.model.AvailableTrucksModel;
import com.a4tech.shipping.model.TruckHistoryDetail;
import com.a4tech.shipping.model.User;

public interface IShippingOrder {
  public List<ShippingDetails1> getAllShippingOrders();
  public List<ShippingDetails1> getShippingDetailsByDate(String date);
  public List<AvailableTrucksModel> getAllTruckInfo();
  public void saveOrderGroup(OrderGroup orderGroup);
  public List<OrderGroup> getAllGroupOrderList();
  public List<OrderGroup> getOrderGroupByDate(String date);
  public ShippingDetails1 getShippingDetails(String orderNo);
  public List<String> getOrderNoByTruck(String truckNo);
  public void updateOrderGroupFlag(String delivaryNo);
  public List<Address> getLatitudeAndLongitude(String truckNo);
  public void deleteAllGroupOrders(String tableName);
  public int generateShippingOrderId(ShippingDeliveryOrder shippingDelivary);
  public void saveDistrictWiseNormalLoad(NormalLoadConfiguration normal);
  public List<DistrictWiseNormalLoadCapacity> getAllDistrictWiseLoads();
  public DistrictWiseNormalLoadCapacity getDistrictTruckLoad(String districtName);
  public void updateDistrictWiseNormalLoad(NormalLoadConfiguration normal);
  public List<TruckHistoryDetailsEntity> getAllTrucksHistoryDetails();
  public List<TruckHistoryDetailsEntity> getSearchTrucksHistoryDetails(String value,String type);
  public void saveAxleWheelConfiguration(AxleWheelConfiguration axleWheelConfig);
  public void saveOrUpdateDistrictClubOrdByPass(DistrictClubOrdByPass districtByPass);
  public List<DistrictClubOrdByPass> getAllDistrictClubOrdByPass();
  public AxleWheelTypeEntity getAxlewheel(String wheelType);
  public List<AxleWheelTypeEntity> getAllAxleWheelTypeEntity();
  public List<AxleWheelnfoEntity> getWheelTypeInfo(String name);
  public List<TruckHistoryDetail> getAllPreviousTrucksByDistName(String distName);
  public void saveShippingOrderReAssign(ShippingOrdersReAssignModel shippingEntity); 
  public List<ShippingDetails1> getAllReAssignOrdersBasedOnTruckNo(String truck);
  public TruckHistoryDetailsEntity getDistrictDetails(String distName,String truckNo);
  public List<AvailableTrucksModel> getAllAvailableTrucksByAxleType(String axleType);
  public void deleteOrderByPassDistrict(Integer id);
 // public void updateDistrictClubOrdByPass(DistrictClubOrdByPass districtByPass);
  public void saveLatitudeAndLongitudeVals(LangitudeAndLatitudeMap longi);
  public Map<String, String> getAllLatitudeAndLongitudeVals();
  
  public User getUserDetails(String name);
  public void saveShippingFinalOrders(IntellishipModelByMaterial shippingFinalOrders);
  public List<IntellishipModelByMaterial> getAllShippingFinalOrders();
}

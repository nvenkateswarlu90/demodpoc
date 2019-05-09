package com.a4tech.shipping.ishippingDao;

import java.util.List;

import com.a4tech.dao.entity.AxleWheelTypeEntity;
import com.a4tech.dao.entity.AxleWheelnfoEntity;
import com.a4tech.dao.entity.ChannelConfigurationEntity;
import com.a4tech.dao.entity.ChannelSequenceEntity;
import com.a4tech.dao.entity.DistrictClubOrdByPassEntity;
import com.a4tech.dao.entity.DistrictWiseNormalLoadCapacity;
import com.a4tech.dao.entity.LangitudeAndLatitudeMap;
import com.a4tech.dao.entity.OrderGroupEntity;
import com.a4tech.dao.entity.ShippingDeliveryOrderEntity;
import com.a4tech.dao.entity.ShippingEntity;
import com.a4tech.dao.entity.ShippingFinalOrders;
import com.a4tech.dao.entity.ShippingOrdersReAssign;
import com.a4tech.dao.entity.AvailableTrucksEntity;
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.dao.entity.UsedTrucksEntity;
import com.a4tech.dao.entity.UserEntity;
import com.a4tech.shipping.model.DistrictClubOrdByPass;

public interface IshippingOrderDao {
  public void saveShippingEntity(ShippingEntity shippingEntity);
  public List<ShippingEntity> getAllShippingOrders(); 
  public List<ShippingEntity> getShippingDetailsByDate(String date);
  public List<AvailableTrucksEntity> getAllTruckInfo(); 
  public void saveOrderGroup(OrderGroupEntity orderGroupEntity);
  public List<OrderGroupEntity> getALLGroupOrders();
  public List<OrderGroupEntity> getOrderGroupByDate(String date);
  public ShippingEntity getShippingDetails(String orderNo);
  public List<String> getOrderNoByTruck(String truckNo);
  public void updateOrderGroupFlag(String delivaryNo);
  public List<OrderGroupEntity> getLatitudeAndLongitude(String truckNo);
  public void deleteAllGroupOrders(String tableName);
  public int generateShippingOrderId(ShippingDeliveryOrderEntity shippingDelivary);
  public void saveDistrictWiseNormalLoad(DistrictWiseNormalLoadCapacity normal);
  public List<DistrictWiseNormalLoadCapacity> getAllDistrictWiseLoads();
  public DistrictWiseNormalLoadCapacity getDistrictTruckLoad(String districtName);
  public void updateDistrictWiseNormalLoad(DistrictWiseNormalLoadCapacity normal);
  public List<TruckHistoryDetailsEntity> getAllTrucksHistoryDetails();
  public void saveTruckdetailsEntity(AvailableTrucksEntity truckEntity);
  public void saveTruckhistory(TruckHistoryDetailsEntity truckHistory);
  public void saveAxleWheelConfiguration(AxleWheelTypeEntity wheelEntity);
  public List<TruckHistoryDetailsEntity> getSearchTrucksHistoryDetails(String value,String type);
  public void saveOrUpdateDistrictClubOrdByPass(DistrictClubOrdByPassEntity byPassEnitity);
  public List<DistrictClubOrdByPassEntity> getAllDistrictClubOrdByPass();
  public AxleWheelTypeEntity getAxlewheel(String wheelType);
  public List<AxleWheelTypeEntity> getAllAxleWheelTypeEntity();
  public void updateTruckhistory(TruckHistoryDetailsEntity historyObj);
  public List<AxleWheelnfoEntity> getWheelTypeInfo(String name);
  List<TruckHistoryDetailsEntity> getTruckHistoryDataByDistrictName(String districtName);
  public void saveShippingEntityReOrder(ShippingOrdersReAssign shippingEntity); 
  public List<ShippingOrdersReAssign> getAllReAssignOrdersBasedOnTruckNo(String truck); 
  public TruckHistoryDetailsEntity getDistrictDetails(String districtName,String truckNo);
  public List<AvailableTrucksEntity> getAllAvailableTrucksByAxleType(String axleType);
  
  public void saveShippingFinalOrders(ShippingFinalOrders shippingFinalOrders);
  public void deleteOrderByPassDistrict(Integer id);
  public void saveLatitudeAndLongitudeVals(LangitudeAndLatitudeMap longi);
  public UserEntity getUserDetails(String userName);
  public void saveOrUpdateChannelConfiguration(ChannelConfigurationEntity channelConfig);
  public void deleteChannelConfiguration(Integer id);
  public void saveChannelSequence(ChannelSequenceEntity channelSeq);
  public ChannelSequenceEntity getChannelSequence();
  public void deleteTruckFromTruckPool(AvailableTrucksEntity availableTruckEntity);
  public void saveUsedTruck(UsedTrucksEntity usedTruckEntity);
  public void deleteOrderFromPendingList(ShippingEntity shipping);
 // public void updateDistrictClubOrdByPass(DistrictClubOrdByPassEntity districtByPass);
  public <T> List<T> listAllData(Class<T> clazz); 
  public <T> List<T> listAllDataById(Class<T> clazz,String variableName,String val);
  public <T> T getDataById(Class<T> clazz,String type,T val);


}

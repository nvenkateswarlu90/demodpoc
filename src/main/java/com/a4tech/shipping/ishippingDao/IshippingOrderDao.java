package com.a4tech.shipping.ishippingDao;

import java.util.List;

import com.a4tech.dao.entity.OrderGroupEntity;
import com.a4tech.dao.entity.ShippingEntity;
import com.a4tech.dao.entity.TruckDetailsEntity;

public interface IshippingOrderDao {
  public void saveShippingEntity(ShippingEntity shippingEntity);
  public List<ShippingEntity> getAllShippingOrders();
  public List<ShippingEntity> getShippingDetailsByDate(String date);
  public List<TruckDetailsEntity> getAllTruckInfo();
  public void saveOrderGroup(OrderGroupEntity orderGroupEntity);
  public List<OrderGroupEntity> getALLGroupOrders();
  public List<OrderGroupEntity> getOrderGroupByDate(String date);
}
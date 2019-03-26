package com.a4tech.shipping.shippingDaoImpl;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.a4tech.dao.entity.AvailableTrucks;
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
import com.a4tech.dao.entity.TruckHistoryDetailsEntity;
import com.a4tech.dao.entity.UserEntity;
import com.a4tech.shipping.ishippingDao.IshippingOrderDao;


@Transactional
public class ShippingDao implements IshippingOrderDao{ 

	SessionFactory sessionFactory;
     private Logger _LOGGER = Logger.getLogger(ShippingDao.class);
	public void saveShippingEntity(ShippingEntity shippingEntityBean) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(shippingEntityBean);
			transaction.commit();
			_LOGGER.info("Order Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<ShippingEntity> getAllShippingOrders() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			/*Criteria criteria = session.createCriteria(ShippingEntity.class);
			criteria.add(Restrictions.eq("isOrderGroup", "No"));
			List<ShippingEntity> shippingData = criteria.list();*/
			List<ShippingEntity> shippingData = session.createCriteria(ShippingEntity.class).list();
			//transaction.commit();
			return shippingData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping ordet data from DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TruckHistoryDetailsEntity> getAllTrucksHistoryDetails() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		    List<TruckHistoryDetailsEntity> truckData = session.createCriteria(TruckHistoryDetailsEntity.class).list();
			//transaction.commit();
			return truckData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping ordet data from DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ShippingEntity> getShippingDetailsByDate(String date) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(ShippingEntity.class);
			criteria.add(Restrictions.eq("deliv_date", date));
			List<ShippingEntity> shippingData = criteria.list();
			transaction.commit();
			return shippingData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping order data from DB based on date: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public List<AvailableTrucks> getAllTruckInfo() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<AvailableTrucks> truckDetailsList = session.createCriteria(AvailableTrucks.class).list();
			transaction.commit();
			return truckDetailsList;
		} catch (Exception ex) {
			_LOGGER.error("unable to get Truck Details data from DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
       return new ArrayList<>();
	}
	
	@Override
	public void saveTruckdetailsEntity(AvailableTrucks truckEntity) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(truckEntity);
			transaction.commit();
			_LOGGER.info(truckEntity.getSlNo()+":Truck Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save Truck Details into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	
	@Override
	public void saveOrderGroup(OrderGroupEntity orderGroupEntity) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(orderGroupEntity);
			transaction.commit();
			_LOGGER.info("Order Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save orderGroup details into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	@Override
	public List<OrderGroupEntity> getALLGroupOrders() {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			@SuppressWarnings("unchecked")
			List<OrderGroupEntity> orderGroupList = session.createCriteria(OrderGroupEntity.class).list();
			transaction.commit();
			return orderGroupList;
		} catch (Exception ex) {
			_LOGGER.error("unable to get orderGroupList Details data from DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
       return new ArrayList<>();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<OrderGroupEntity> getOrderGroupByDate(String date) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(OrderGroupEntity.class);
			criteria.add(Restrictions.eq("delivaryDate", date));
			List<OrderGroupEntity> ordGroupEntity = criteria.list();
			transaction.commit();
			return ordGroupEntity;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping order data from DB based on date: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public ShippingEntity getShippingDetails(String orderNo) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(ShippingEntity.class);
			criteria.add(Restrictions.eq("delivery", orderNo));
			ShippingEntity shippingDetails = (ShippingEntity) criteria.uniqueResult();
			transaction.commit();
			return shippingDetails;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping order data from DB based on date: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOrderNoByTruck(String truckNo) {
		
		Session session = null;
		//Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			//transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(OrderGroupEntity.class);
			criteria.add(Restrictions.eq("truckNo", truckNo));
			Projection prop = Projections.property("delivaryNo");
			List<String> delivaryOrderNoList = criteria.setProjection(prop).list();
			List<String> finalDelivaryOrdsList = new ArrayList<>();
			for (String delivaryOrdNo : delivaryOrderNoList) {
			      	if(!finalDelivaryOrdsList.contains(delivaryOrdNo)){
			      		finalDelivaryOrdsList.add(delivaryOrdNo);
			      	}
			}
			//transaction.commit();
			return finalDelivaryOrdsList;
		} catch (Exception ex) {
			_LOGGER.error("unable to get shipping order data from DB based on date: "+ex.getCause());
			/*if (transaction != null) {
				transaction.rollback();
			}*/
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
	@Override
	public void updateOrderGroupFlag(String delivaryNo) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			
			Query query = session.createQuery("UPDATE ShippingEntity s SET s.isOrderGroup = :isOrderGroup WHERE s.delivery = :delivery");
			query.setParameter("isOrderGroup", "Yes");
			query.setParameter("delivery", delivaryNo);
			query.executeUpdate();
			
			/*ShippingEntity shippingEntity = (ShippingEntity) session.get(ShippingEntity.class, delivaryNo);
			shippingEntity.setIsOrderGroup("Yes");
			session.saveOrUpdate(shippingEntity);*/
			transaction.commit();
			_LOGGER.info("Order Details data has been updated successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to update data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		
	}
	@Override
	public List<OrderGroupEntity> getLatitudeAndLongitude(String truckNo) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(OrderGroupEntity.class);
			criteria.add(Restrictions.eq("truckNo", truckNo));
			List<OrderGroupEntity> ordGroupEntity = criteria.list();
			transaction.commit();
			return ordGroupEntity;
		} catch (Exception ex) {
			_LOGGER.error("unable to get order group order data from DB based on date: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public void deleteAllGroupOrders(String tableName) {
		Session session = null;
		Transaction transaction = null;
		try {
			/*session = sessionFactory.openSession();
			transaction = session.beginTransaction();			
			Query q2 = session.createQuery ("TRUNCATE table OrderGroupEntity");
            int deleted = q2.executeUpdate ();
			
			transaction.commit();*/
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			String hql = "";
			if(tableName.equalsIgnoreCase("orderGroup")) {
				hql = String.format("delete from %s","OrderGroupEntity");
			} else if(tableName.equalsIgnoreCase("orderReAssign")) {
				hql = String.format("delete from %s","ShippingOrdersReAssign");
			} else if(tableName.equalsIgnoreCase("shippingFinalOrders")) {
				hql = String.format("delete from %s","ShippingFinalOrders");
			}
		    Query query = session.createQuery(hql);
		    int no = query.executeUpdate();
		    transaction.commit();
		} catch (Exception ex) {
			_LOGGER.error("unable to truncate order group table: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	@Override
	public int generateShippingOrderId(ShippingDeliveryOrderEntity shippingDelivary) {
		Session session = null;
		Transaction transaction = null;
		int shippingDelivaryId = 0;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			shippingDelivaryId = (int) session.save(shippingDelivary);
			transaction.commit();
			_LOGGER.info("shipping delivary order details has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save shipping delivary order details data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return shippingDelivaryId;
	}
	@Override
	public List<DistrictWiseNormalLoadCapacity> getAllDistrictWiseLoads() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<DistrictWiseNormalLoadCapacity> districtWiseLoadData = session
					.createCriteria(DistrictWiseNormalLoadCapacity.class).list();
			return districtWiseLoadData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get DistrictWise trucks load type from DB: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public DistrictWiseNormalLoadCapacity getDistrictTruckLoad(String districtName) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(DistrictWiseNormalLoadCapacity.class);
			criteria.add(Restrictions.eq("districtName", districtName));
			DistrictWiseNormalLoadCapacity districtData = (DistrictWiseNormalLoadCapacity) criteria.uniqueResult();
			return districtData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get district truck load types from DB based on date: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
/*	@Override
	public List<TruckHistoryDetails> getAllTrucksHistoryDetails() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<TruckHistoryDetails> districtWiseLoadData = session
					.createCriteria(DistrictWiseNormalLoadCapacity.class).list();
			return districtWiseLoadData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get trucks history data from DB: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();}*/
	
	@Override
	public void saveTruckhistory(TruckHistoryDetailsEntity truckHistory) {
		
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(truckHistory);
			transaction.commit();
			_LOGGER.info("Truck History Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save Truck History Details into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<TruckHistoryDetailsEntity> getSearchTrucksHistoryDetails(
			String TruckNo,String type) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		//   List<TruckHistoryDetailsEntity> truckData = session.createCriteria(TruckHistoryDetailsEntity.class).list();
		    Criteria criteria = session.createCriteria(TruckHistoryDetailsEntity.class);

		    List<TruckHistoryDetailsEntity> truckData;
		    
		    if(type.equalsIgnoreCase("Vehicle No")){
		    	
		    criteria.add(Restrictions.eq("truckNo", TruckNo));
		     truckData = criteria.list();
		   
		    }else{
			criteria.add(Restrictions.eq("districtName",TruckNo ));
		     truckData = criteria.list();

		    }
			
			//transaction.commit();
			return truckData;
		} catch (Exception ex) {
			_LOGGER.error("Unable to get data from vehicle no. "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
		

	}
	

	@Override
	public void saveDistrictWiseNormalLoad(DistrictWiseNormalLoadCapacity normalLoad) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(normalLoad);
			transaction.commit();
			_LOGGER.info("Added New district normal load configuration data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save New district normal load configuration data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	@Override
	public void updateDistrictWiseNormalLoad(DistrictWiseNormalLoadCapacity normalLoad) {
		Session session = null;
		Transaction transaction = null;
		try {
			/*session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.update(normalLoad);
			transaction.commit();*/
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			String hqlUpdate = "update DistrictWiseNormalLoadCapacity n set n.ratedLoad = :ratedLoad ,n.truckOverLoading=:overLoading,n.truckOverLoadingtonns=:overLoadingtonns where n.districtName = :districtName";
			// or String hqlUpdate = "update Customer set name = :newName where name = :oldName";
			int updatedEntities = session.createQuery( hqlUpdate )
					.setString("districtName", normalLoad.getDistrictName())
			       .setInteger("ratedLoad", normalLoad.getRatedLoad())
			        .setString("overLoading", normalLoad.getTruckOverLoading())
			        .setDouble("overLoadingtonns", normalLoad.getTruckOverLoadingtonns())
			        .executeUpdate();
			        transaction.commit();

			        _LOGGER.info("Updated  district normal load configuration data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to Updated  district normal load configuration data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	@Override
	public void saveOrUpdateDistrictClubOrdByPass(DistrictClubOrdByPassEntity byPassEnitity) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(byPassEnitity);
			transaction.commit();
			_LOGGER.info("Added bypass district configuration data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save bypass configuration data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
		
	}
	@Override
	public List<DistrictClubOrdByPassEntity> getAllDistrictClubOrdByPass() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			@SuppressWarnings("unchecked")
			List<DistrictClubOrdByPassEntity> districtByPassData = session
					.createCriteria(DistrictClubOrdByPassEntity.class).list();
			return districtByPassData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get DistrictWise trucks load type from DB: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	@Override
	public void saveAxleWheelConfiguration(AxleWheelTypeEntity wheelEntity) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(wheelEntity);
			transaction.commit();
			_LOGGER.info("Added New wheel type in dbb");
		} catch (Exception ex) {
			_LOGGER.error("unable to savewheeltype in db: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	}
	@Override
	public AxleWheelTypeEntity getAxlewheel(String wheelType) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(AxleWheelTypeEntity.class);
			criteria.add(Restrictions.eq("axlewheelertype", wheelType));
			AxleWheelTypeEntity wheelData = (AxleWheelTypeEntity) criteria.uniqueResult();
			return wheelData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get district truck load types from DB based on date: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
	
	
	private <T> List<T> getAllTablesData(Class<T> entity){
		Session session = null;
		try {
			session = sessionFactory.openSession();
			
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entity);
			Root<T> rootEntry = cq.from(entity);
		    CriteriaQuery<T> all = cq.select(rootEntry);
			
			TypedQuery<T> querys = session.createQuery(all);
			 _LOGGER.info("Final  List: ");
			return querys.getResultList();
		} catch (Exception ex) {
			_LOGGER.error("unable to get Axle wheel data "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
	@Override
	public List<AxleWheelTypeEntity> getAllAxleWheelTypeEntity() {

		Session session = null;
		try {
			session = sessionFactory.openSession();
			
		
		    List<AxleWheelTypeEntity> axleWheelerList = getAllTablesData(AxleWheelTypeEntity.class);
			return axleWheelerList;
		} catch (Exception ex) {
			_LOGGER.error("unable to get Axle wheel data "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	
	}
	
	

	@Override
	public void updateTruckhistory(TruckHistoryDetailsEntity historyObj) {


		Session session = null;
		Transaction transaction = null;
		try {
		
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
	
			 
			String hqlUpdate = "update TruckHistoryDetailsEntity tr set tr.districtCode = :a,tr.districtName = :b,tr.ratedLoad = :c,tr.normalLoad = :d where tr.truckNo = :e";
			int updatedEntities = session.createQuery( hqlUpdate )
					.setString("e", historyObj.getTruckNo())
					.setString("a",historyObj.getDistrictCode() )
					.setString("b",historyObj.getDistrictName() )
					.setInteger("c", historyObj.getRatedLoad())
					.setInteger("d", historyObj.getNormalLoad())

			        .executeUpdate();
			        transaction.commit();

			        _LOGGER.info("Updated truck history data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to Update truck history  data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
	
	}
	@Override
	public List<AxleWheelnfoEntity> getWheelTypeInfo(String name) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Criteria cq= session.createCriteria(AxleWheelnfoEntity.class);
			List<AxleWheelnfoEntity> wheelrList = cq.add(Restrictions.eq("no",Integer.parseInt(name))).list();
			transaction.commit();
			return wheelrList;
		} catch (Exception ex) {
			_LOGGER.error("unable to get wheelnfoEntity data from DB  "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public <T> List<T> listAllData(Class<T> clazz) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(clazz);
			Root<T> rootEntry = cq.from(clazz);
		    CriteriaQuery<T> all = cq.select(rootEntry);
			
			TypedQuery<T> querys = session.createQuery(all);
			 _LOGGER.info("Final  List: ");
			return querys.getResultList();
		} catch (Exception ex) {
			_LOGGER.error("unable to get all list data "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
	}
	@Override
	public <T> T getDataById(Class<T> clazz, String type, T val) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
		    CriteriaBuilder builder=	session.getCriteriaBuilder();
	        CriteriaQuery<T> query= builder.createQuery(clazz);
	       Root<T> rootEntry =  query.from(clazz);
	       query.select(rootEntry).where(builder.equal(rootEntry.get(type), val));
	      TypedQuery<T> q = session.createQuery(query);
	     return  q.getSingleResult();
		}catch (Exception ex) {
			_LOGGER.error("unable to fetch data by id"+ex.getCause());
		}
		return null;
	}
	@Override
	public <T> List<T> listAllDataById(Class<T> clazz, String variableName, String val) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
		    CriteriaBuilder builder=	session.getCriteriaBuilder();
	        CriteriaQuery<T> query= builder.createQuery(clazz);
	       Root<T> rootEntry =  query.from(clazz);
	       query.select(rootEntry).where(builder.equal(rootEntry.get(variableName), val));
	      TypedQuery<T> q = session.createQuery(query);
	     return  q.getResultList();
		}catch (Exception ex) {
			_LOGGER.error("unable to fetch list of data by id"+ex.getCause());
		}
		return new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TruckHistoryDetailsEntity> getTruckHistoryDataByDistrictName(String districtName) {

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		//   List<TruckHistoryDetailsEntity> truckData = session.createCriteria(TruckHistoryDetailsEntity.class).list();
		    Criteria criteria = session.createCriteria(TruckHistoryDetailsEntity.class);
		    criteria.add(Restrictions.eq("districtName",districtName ));
		    List<TruckHistoryDetailsEntity> truckData = criteria.list();
			return truckData;
		} catch (Exception ex) {
			_LOGGER.error("Unable to get data from vehicle no. "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
		

	}
	@Override
	public void saveShippingEntityReOrder(ShippingOrdersReAssign shippingEntity) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(shippingEntity);
			transaction.commit();
			_LOGGER.info("RE-Order Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save Re-data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		
	}
	@Override
	public List<ShippingOrdersReAssign> getAllReAssignOrdersBasedOnTruckNo(String truck) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
		//   List<TruckHistoryDetailsEntity> truckData = session.createCriteria(TruckHistoryDetailsEntity.class).list();
		    Criteria criteria = session.createCriteria(ShippingOrdersReAssign.class);
		    criteria.add(Restrictions.eq("truckNo", truck));
		    List<ShippingOrdersReAssign> ordersData = criteria.list();
			return ordersData;
		} catch (Exception ex) {
			_LOGGER.error("Unable to get data from vehicle no. "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return new ArrayList<>();
			
	}
	@Override
	public TruckHistoryDetailsEntity getDistrictDetails(String districtName,String truckNo) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(TruckHistoryDetailsEntity.class);
			criteria.add(Restrictions.eq("districtName", districtName));
			criteria.add(Restrictions.eq("truckNo", truckNo));
			TruckHistoryDetailsEntity wheelData = (TruckHistoryDetailsEntity) criteria.uniqueResult();
			return wheelData;
		} catch (Exception ex) {
			_LOGGER.error("unable to get district truck load types from DB based on date: "+ex.getCause());
			
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}
	@Override
	@SuppressWarnings("deprecation")
	public List<AvailableTrucks> getAllAvailableTrucksByAxleType(String axleType) {
		 
		 try(Session session = sessionFactory.openSession()){
			Criteria criteria = session.createCriteria(TruckHistoryDetailsEntity.class);
				criteria.add(Restrictions.eq("vehicleType", axleType));
				List<AvailableTrucks> avaiableTrucksList = criteria.list();
				return avaiableTrucksList;
		 }catch (Exception e) {
			_LOGGER.error("Unbale to fetch available trucks data: "+e.getMessage());
		}
		return null;
	}
	@Override
	public void saveShippingFinalOrders(ShippingFinalOrders shippingFinalOrders) {
		Transaction transaction = null;
		try(Session session =  sessionFactory.openSession() ) {
			transaction = session.beginTransaction();
			session.save(shippingFinalOrders);
			transaction.commit();
			_LOGGER.info("ShippingFinal data Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save shipping final data Details into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} 
		
	}
	@Override
	public void deleteOrderByPassDistrict(Integer id) {
	   Transaction tranction = null;
	   try(Session session = sessionFactory.openSession()) {
		   tranction = session.beginTransaction();
		 DistrictClubOrdByPassEntity district = session.load(DistrictClubOrdByPassEntity.class, id);
		 if(district != null) {
			 session.delete(district);
		 }
		 tranction.commit();
		 _LOGGER.info("Bypass District has been successfully removed from DB: ");
	   } catch (Exception e) {
		   if(tranction!= null) {
			   tranction.rollback();   
		   }
	}
		
	}
	/*@Override
	public void updateDistrictClubOrdByPass(DistrictClubOrdByPassEntity districtByPass) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(districtByPass);
			transaction.commit();
			_LOGGER.info("Added bypass district configuration data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save bypass configuration data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception ex) {
				}
			}
		}		
		
	}
	*/
	@Override
	public void saveLatitudeAndLongitudeVals(LangitudeAndLatitudeMap longi) {
		Transaction transaction = null;
		try(Session session =  sessionFactory.openSession() ) {
			transaction = session.beginTransaction();
			session.save(longi);
			transaction.commit();
			_LOGGER.info("ShippingFinal data Details data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save shipping final data Details into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} 
		
	}
	@Override
	public UserEntity getUserDetails(String userName) {
		try(Session session = sessionFactory.openSession()){
			Criteria cre = session.createCriteria(UserEntity.class);
			cre.add(Restrictions.eq("userName", userName));
			return (UserEntity)cre.uniqueResult();
		} catch (Exception e) {
			_LOGGER.error("Unable to fetch user details:"+e.getMessage());
		}
		return null;
	}
	@Override
	public void saveOrUpdateChannelConfiguration(ChannelConfigurationEntity channelConfig) {
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.saveOrUpdate(channelConfig);
			transaction.commit();
			_LOGGER.info("Added channel configuration data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save channel configuration data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} 	
		
	}
	@Override
	public void deleteChannelConfiguration(Integer id) {
		  Transaction tranction = null;
		   try(Session session = sessionFactory.openSession()) {
			   tranction = session.beginTransaction();
			   ChannelConfigurationEntity district = session.load(ChannelConfigurationEntity.class, id);
			 if(district != null) {
				 session.delete(district);
			 }
			 tranction.commit();
			 _LOGGER.info("channel configuration has been successfully removed from DB: ");
		   } catch (Exception e) {
			   if(tranction!= null) {
				   tranction.rollback();   
			   }
		}
		
	}
	@Override
	public void saveChannelSequence(ChannelSequenceEntity channelSeq) {
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			ChannelSequenceEntity channelSequence = getChannelSequences(1);
			if(channelSequence == null) {
				session.save(channelSeq);
			} else {
				channelSequence.setSequence(channelSeq.getSequence());
				session.update(channelSequence);
			}
			//session.saveOrUpdate(channelSeq);
			transaction.commit();
			_LOGGER.info("Added channel sequence data has been saved successfully in db");
		} catch (Exception ex) {
			_LOGGER.error("unable to save channel sequence data into DB: "+ex.getCause());
			if (transaction != null) {
				transaction.rollback();
			}
		} 	
		
	}
	@Override
	public ChannelSequenceEntity getChannelSequence() {
		try(Session session = sessionFactory.openSession()){
			Criteria cre = session.createCriteria(ChannelSequenceEntity.class);
			return (ChannelSequenceEntity)cre.uniqueResult();
		} catch (Exception e) {
			_LOGGER.error("Unable to fetch channel sequence:"+e.getMessage());
		}
		return new ChannelSequenceEntity();
	}
	public ChannelSequenceEntity getChannelSequences(Integer id) {
		try(Session session = sessionFactory.openSession()){
			Criteria cre = session.createCriteria(ChannelSequenceEntity.class);
			cre.add(Restrictions.eq("seqId", id));
			return (ChannelSequenceEntity)cre.uniqueResult();
		} catch (Exception e) {
			_LOGGER.error("Unable to fetch channel sequence details:"+e.getMessage());
		}
		return new ChannelSequenceEntity();
	}

	
	
	
	
	
	
	
}

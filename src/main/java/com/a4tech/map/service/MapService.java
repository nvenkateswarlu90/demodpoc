package com.a4tech.map.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.a4tech.exceptions.MapOverLimitException;
import com.a4tech.exceptions.MapServiceRequestDeniedException;
import com.a4tech.map.model.Distance;
import com.a4tech.map.model.DistanceMatrix;
import com.a4tech.map.model.Duration;
import com.a4tech.map.model.Elements;
import com.a4tech.map.model.Rows;
import com.a4tech.util.ApplicationConstants;



public class MapService {


  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, Double> distanceMapStore = new HashMap<>();
  private static Map<String, String> distanceAndHrsMapStore = new HashMap<>();
  private static Map<String, DistanceMatrix> allDestinationsStore = new HashMap<>();
  private static String GOOLGE_MAP_API_KEY = "AIzaSyAF27UXmyKEQpNmybxxaViJpYWo-yFzkxk";
 
	public DistanceMatrix getMaxDistenceFromMultipleDestinationsRestTemplate(String originCoordinates,
			String destinationCoordinates) throws IOException, MapServiceRequestDeniedException, MapOverLimitException {
		String distanceMatrixUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ originCoordinates + "&destinations=" + destinationCoordinates
				+ "&mode=driving&key="+GOOLGE_MAP_API_KEY;
		String status = "";
	try {
		 DistanceMatrix result = restTemplate.getForObject(distanceMatrixUrl, DistanceMatrix.class);
		 status = result.getStatus();
			if (ApplicationConstants.CONST_OVER_DAILY_LIMIT.equalsIgnoreCase(status)
					|| ApplicationConstants.CONST_OVER_QUERY_LIMIT.equalsIgnoreCase(status)) {
			 throw new MapOverLimitException("Map Service Status: "+status);
		 } else if(ApplicationConstants.CONST_REQUEST_DENIED.equalsIgnoreCase(status)) {
			 throw new MapServiceRequestDeniedException("Map Service Status: "+status);
		 }
		 return result;
	} catch (MapOverLimitException e) {
		 throw new MapOverLimitException(e.getErrorMsg());
	} catch (MapServiceRequestDeniedException e) {
		throw new MapServiceRequestDeniedException(e.getErrorMsg());
	} catch (Exception e) {
		// TODO: handle exception
	}
      return null;
  }
 
  public String getMaxDistenceAndHrsFromMultipleDestination(String originCoordinates,String destinationCoordinates) throws IOException{
		String distanceMatrixUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ originCoordinates + "&destinations=" + destinationCoordinates
				+ "&mode=driving&key="+GOOLGE_MAP_API_KEY;
	  DistanceMatrix result = null;
	  	try {
	  		  result = restTemplate.getForObject(distanceMatrixUrl, DistanceMatrix.class);
	  		String status = result.getStatus();
			if (ApplicationConstants.CONST_OVER_DAILY_LIMIT.equalsIgnoreCase(status)
					|| ApplicationConstants.CONST_OVER_QUERY_LIMIT.equalsIgnoreCase(status)) {
			 throw new MapOverLimitException("Map Service Status: "+status);
		 } else if(ApplicationConstants.CONST_REQUEST_DENIED.equalsIgnoreCase(status)) {
			 throw new MapServiceRequestDeniedException("Map Service Status: "+status);
		 }
	  		
	  	} catch (Exception e) {
	  		
	  	}
	  	if(result != null) {
	  		return getFinalDistenceAndHrs(result);
	  	}
	 
      return "";
      
     
  }
  public static String getFinalDistenceAndHrs(DistanceMatrix distances) {
	  List<Rows> rowsList = distances.getRowsList();
		double maxDist = 0.0;
		StringBuilder finalDistenceAndHrs = new StringBuilder();
		for (Rows rows2 : rowsList) {
			List<Elements> elements = rows2.getElements();
			List<Double> allKiloMeters = new ArrayList<>();
			List<String> allHrs = new ArrayList<>();
			for (Elements elements2 : elements) {
				  Distance dist = elements2.getDistance();
				  Duration duration = elements2.getDuration();
				  String hrs = duration.getText();
				 String kiloMeters = dist.getText();
				 kiloMeters = kiloMeters.replaceAll("[^0-9.]", "").trim();
				 allKiloMeters.add(Double.parseDouble(kiloMeters));
				 allHrs.add(hrs);
			}
			maxDist = getMaxKiloMeters(allKiloMeters);
			String maxHrs = getMaxHrs(allHrs);
			finalDistenceAndHrs.append(maxDist).append("###").append(maxHrs);
		}
		return finalDistenceAndHrs.toString();
	}
  
	public static double getMaxKiloMeters(List<Double> allKiloMeters) {
		double maxKiloMeter = allKiloMeters.get(0);
		for (Double double1 : allKiloMeters) {
			if (maxKiloMeter < double1) {
				maxKiloMeter = double1;
			}
		}	
		return maxKiloMeter;
		}
public static String getMaxHrs(List<String> allHrs){
	
	String hours = allHrs.get(0);
	int maxHrs = 0;
	/*if(hours.contains("hours")) {
		 maxHrs = Integer.parseInt(hours.split("hours")[0].trim());
	}*/
	
	if(hours.contains("hour")) {
		if(hours.contains("hours")) {
			maxHrs = Integer.parseInt(hours.split("hours")[0].trim());
		} else {
			maxHrs = Integer.parseInt(hours.split("hour")[0].trim());
		}
		
	}
	
	String finalTime = "";
	String tempTime = "";
	for (String times : allHrs) {//5 hours 27 mins  //1 hour 33 mins
		int hr = 0;
		if(times.contains("hour")) {
			if(times.contains("hours")) {
				 hr = Integer.parseInt(times.split("hours")[0].trim());
			} else {
				hr = Integer.parseInt(times.split("hour")[0].trim());
			}
			
		}
		  
		  if (maxHrs < hr) {
			  maxHrs = hr;
			  finalTime = times;
			}else {
				finalTime = hours;
			}
		
		  tempTime = times;
	}
	if(StringUtils.isEmpty(finalTime)){
		finalTime = tempTime;
	}
	return finalTime;
}
public static void saveDistanceMapStore(String langAndLong,double distance) {
	distanceMapStore.put(langAndLong, distance);
}
public static void saveDistanceAndHrsMapStore(String langAndLong,String distance) {
	distanceAndHrsMapStore.put(langAndLong, distance);
}

public static Double getDistanceMapStore(String langAndLong) {
	return distanceMapStore.get(langAndLong);
}
public static DistanceMatrix getAllDestinationsStore(String destinations) {
	return allDestinationsStore.get(destinations);
}
public static void saveAllDestinationsStore(String destinatios,DistanceMatrix result) {
	allDestinationsStore.put(destinatios, result);
}
public static  String getDistanceAndHrsMapStore(String langAndLong) {
	return distanceAndHrsMapStore.get(langAndLong);
}
public RestTemplate getRestTemplate() {
	return restTemplate;
}

public void setRestTemplate(RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
}
}

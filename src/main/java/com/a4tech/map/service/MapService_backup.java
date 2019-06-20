package com.a4tech.map.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.a4tech.map.model.Distance;
import com.a4tech.map.model.DistanceMatrix;
import com.a4tech.map.model.Duration;
import com.a4tech.map.model.Elements;
import com.a4tech.map.model.Rows;



public class MapService_backup {
	
  private RestTemplate restTemplate = new RestTemplate();
  private static Map<String, Double> distanceMapStore = new HashMap<>();
  private static Map<String, String> distanceAndHrsMapStore = new HashMap<>();
  private static Map<String, DistanceMatrix> allDestinationsStore = new HashMap<>();
  /*public double getDistence(String originCoordinates,String destinationCoordinates) throws IOException{
	  URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+originCoordinates+"&destinations="+destinationCoordinates+"&mode=driving");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      String line, outputString = "";
      BufferedReader reader = new BufferedReader(
      new InputStreamReader(conn.getInputStream()));
      while ((line = reader.readLine()) != null) {
          outputString += line;
      }
      System.out.println(outputString);
      DistancePojo capRes = new Gson().fromJson(outputString, DistancePojo.class);
      //capRes.get
      //capRes.get
     Rows[] rows = capRes.getRows();
   Elements[] ele=  rows[0].getElements();
   Distance dd = ele[0].getDistance();
   String distenceVal = dd.getText();
   distenceVal = distenceVal.replaceAll("[^0-9.]", "").trim();
      System.out.println("Distence Value: "+distenceVal);
      return Double.parseDouble(distenceVal);
  }*/
 /* public DistancePojo getMaxDistenceFromMultipleDestinations(String originCoordinates,String destinationCoordinates) throws IOException{
	  URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+originCoordinates+"&destinations="+destinationCoordinates+"&mode=driving&key=AIzaSyAF27UXmyKEQpNmybxxaViJpYWo-yFzkxk123");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      String line, outputString = "";
      BufferedReader reader = new BufferedReader(
      new InputStreamReader(conn.getInputStream()));
      while ((line = reader.readLine()) != null) {
          outputString += line;
      }
      System.out.println(outputString);
      DistancePojo capRes = new Gson().fromJson(outputString, DistancePojo.class);
      return capRes;
  }*/
  public DistanceMatrix getMaxDistenceFromMultipleDestinationsRestTemplate(String originCoordinates,String destinationCoordinates) throws IOException{
	 String distanceMatrixUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+originCoordinates+"&destinations="+destinationCoordinates+"&mode=driving&key=AIzaSyAF27UXmyKEQpNmybxxaViJpYWo-yFzkxk123";
	try {
		 DistanceMatrix result = restTemplate.getForObject(distanceMatrixUrl, DistanceMatrix.class);
		 return result;
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	
      return null;
  }
  /*public double getMaxDistenceFromMultipleDestination(String originCoordinates,String destinationCoordinates) throws IOException{
	  URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins="+originCoordinates+"&destinations="+destinationCoordinates+"&mode=driving&key=AIzaSyAF27UXmyKEQpNmybxxaViJpYWo-yFzkxk");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      String line, outputString = "";
      BufferedReader reader = new BufferedReader(
      new InputStreamReader(conn.getInputStream()));
      while ((line = reader.readLine()) != null) {
          outputString += line;
      }
      System.out.println(outputString);
      DistancePojo capRes = new Gson().fromJson(outputString, DistancePojo.class);
     double maxDist = getFinalDistence(capRes);
      return maxDist;
  }*/
  
  public String getMaxDistenceAndHrsFromMultipleDestination(String originCoordinates,String destinationCoordinates) throws IOException{
	  String distanceMatrixUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+originCoordinates+"&destinations="+destinationCoordinates+"&mode=driving&key=AIzaSyAF27UXmyKEQpNmybxxaViJpYWo-yFzkxk";
	  DistanceMatrix result = null;
	  	try {
	  		  result = restTemplate.getForObject(distanceMatrixUrl, DistanceMatrix.class);
	  		
	  	} catch (Exception e) {
	  		// TODO: handle exception
	  	}
	  String maxDistAndHrs = getFinalDistenceAndHrs(result);
      return maxDistAndHrs;
      
     
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
  /* public static double getFinalDistence(DistancePojo distances) {
		Rows[] rows = distances.getRows();
		double maxDist = 0.0;
		for (Rows rows2 : rows) {
			Elements[] elements = rows2.getElements();
			List<Double> allKiloMeters = new ArrayList<>();
			for (Elements elements2 : elements) {
				  Distance dist = elements2.getDistance();
				 String kiloMeters = dist.getText();
				 kiloMeters = kiloMeters.replaceAll("[^0-9.]", "").trim();
				 allKiloMeters.add(Double.parseDouble(kiloMeters));
			}
			maxDist = getMaxKiloMeters(allKiloMeters);
		}
		return maxDist;
	}*/
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
		  /*else if(maxHrs == hr){
				int maxHrsMins = 0;
				if(initialTime == 1){
					String maxMins = hours.split("hours")[1].trim();
					maxMins = maxMins.replaceAll("[^0-9]", "");
					maxHrsMins = Integer.parseInt(maxMins);
				} else {
					
				}
				 
			}*/
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

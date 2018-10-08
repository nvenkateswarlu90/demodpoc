package com.a4tech.map.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.a4tech.map.model.Distance;
import com.a4tech.map.model.DistancePojo;
import com.a4tech.map.model.Elements;
import com.a4tech.map.model.Rows;
import com.google.gson.Gson;

public class MapService {
  private RestTemplate restTemplate ;

  public double getDistence(String originCoordinates,String destinationCoordinates) throws IOException{
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
  }
  public double getMaxDistenceFromMultipleDestination(String originCoordinates,String destinationCoordinates) throws IOException{
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
  }
  public static double getFinalDistence(DistancePojo distances) {
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

public RestTemplate getRestTemplate() {
	return restTemplate;
}

public void setRestTemplate(RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
}
}

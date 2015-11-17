package com.clarity;

import java.io.InputStream;

import javax.faces.bean.ManagedBean;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@ManagedBean(name="mapService", eager=true)

public class MapService {
  private static final String APPID = "app_id";
  private static final long serialVersionUID = 1L;
  
  public String getMap() {
	String[] urls = getMap("2033 Dove Creek Ct.", "Loveland", "CO");
	return urls[0];
  }
  
  public String[] getMap(String streetAddress, String city,
      String state) {
    String[] urls = new String[21];
    boolean cannotAccessWebService = false;
    
    for (int i=1; i <= urls.length; ++i) {
    	urls[i-1] = getMapAddress(streetAddress, city, state, APPID, i);
    }
    return urls;
  }
  
  private String getMapAddress(
      String streetAddress, String city, String state,
      String appid, int zoomLevel) {
    String url =
        "https://maps.googleapis.com/maps/api/staticmap?"
            + "&center=" + encode(streetAddress) 
            + "," + encode(city)
            + "," + encode(state) 
            + "&size=400x400"
            + "&zoom=" + zoomLevel 
            + "&key=" + appid ;
    return url;
  }
  private String encode(String streetAddress) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < streetAddress.length(); ++i) {
      if (streetAddress.charAt(i) == ' ')
        buffer.append('+');
      else
        buffer.append(streetAddress.charAt(i));
    }
    return buffer.toString();
  }
}

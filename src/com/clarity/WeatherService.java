package com.clarity;

import java.io.InputStream;
import java.io.StringReader;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@ManagedBean(name="weatherService", eager=true)

public class WeatherService {
  private static final String UW_APPLICATION_ID =
      "app_id";
  private static final String WEATHER_BASE_URL =
      "http://api.wunderground.com/api/";
  private static final long serialVersionUID = 1L;

  public String getWeatherForZip(String zip,
      boolean isFarenheit) {
    String url =
        WEATHER_BASE_URL + UW_APPLICATION_ID
            + "/conditions/q/" + zip + ".xml";
    return getWeatherFromDocument(getWeatherDocument(url));
  }
  private String getWeatherFromDocument(Document document) {
	  
	  Element test = document.getDocumentElement();
    Element item = (Element) test.getElementsByTagName("current_observation")
            .item(0);

    NodeList list = item.getElementsByTagName("display_location");
    list = ((Element)list.item(0)).getElementsByTagName("full");
    String full = list.item(0).getFirstChild().getNodeValue();

    list = item.getElementsByTagName("icon_url");
    String image = list.item(0).getFirstChild().getNodeValue();
    
    list = item.getElementsByTagName("temperature_string");
    String temp = list.item(0).getFirstChild().getNodeValue();

    list = item.getElementsByTagName("pressure_mb");
    String pres = list.item(0).getFirstChild().getNodeValue();

    list = item.getElementsByTagName("forecast_url");
    String link = list.item(0).getFirstChild().getNodeValue();

    return "<div class='heading'>Current weather in " + full + "</div>"
        + "<hr/>"
    + "<center><img src='"+image+"'/></center>"
    +"<ul>"
    +"<li> Temperature: " + temp + " *C</li>" 
    +"<li> Pressure: " + pres + " MB</li>" 
    +"</ul>"
    +"Full information at: <a href='" + link + "'>Weather Underground</a>";
  }
  
  private Document getWeatherDocument(String url) {
    Document document = null;
    try {
      HttpClient client = new HttpClient(); // Jakarta Commons
      GetMethod gm = new GetMethod(url);
      if (HttpServletResponse.SC_OK == client
          .executeMethod(gm)) {
    	  String text = gm.getResponseBodyAsString();
    	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
    	    DocumentBuilder builder;  
    	    try  
    	    {  
    	        builder = factory.newDocumentBuilder();  
    	        document = builder.parse( new InputSource( new StringReader( text ) ) );  
    	    } catch (Exception e) {  
    	        e.printStackTrace();  
    	    } 
      }
    }
      catch (Exception e1) {
        e1.printStackTrace();
    }
    return document;
  }
}

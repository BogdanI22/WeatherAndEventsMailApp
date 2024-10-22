package SMSWeather.ApiMethods;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import SMSWeather.WeatherData.WeatherElement;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Scanner;

@Component
public class getWeatherData{

    private List<WeatherElement> elementsList;
    private WeatherElement element;
    private String date;
    private String time;
    private double precip;
    private Long precip_prob;
    private double temp;

    public List<WeatherElement> getElementList(double latitude, double longitude){

        this.elementsList = new ArrayList<WeatherElement>();
        this.element = new WeatherElement();
        
        JSONObject AuxObj = getWeatherDataJsonObject(latitude, longitude);
        //next 7 days and past 7 days = 14days x 24h = 336 different elements.
        for(int i = 0; i < 336; i++){
            element.setId(0L);
            element.setLocationLatitude(latitude);
            element.setLocationLongitude(longitude);
            date = processDate((JSONArray)AuxObj.get("time"), i, true);
            element.setDate(date);
            time = processDate((JSONArray)AuxObj.get("time"), i, false);
            element.setTime(time);
            temp = processParameters((JSONArray)AuxObj.get("temperature_2m"),i);
            element.setTemperature(temp);
            precip = processParameters((JSONArray)AuxObj.get("precipitation"), i);
            element.setPrecipitation(precip);
            precip_prob = processParametersForLong((JSONArray)AuxObj.get("precipitation_probability"), i);
            this.element.setPrecipitationProbability(precip_prob);
            this.elementsList.add(element);
            this.element = new WeatherElement();
        }

        return elementsList;
    }

    

    public JSONObject getWeatherDataJsonObject(double latitude, double longitude){
        
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,precipitation_probability,precipitation&past_days=7";
        try {
            //fetch API response based on API Link
            HttpURLConnection apiConnection = fetchApiResponse(urlString);
            
            //check for response status
            if(apiConnection.getResponseCode()!=200){
                System.out.println("Eroor: Could not connect to API");
            }

            String jsonResponse = readApiResponse(apiConnection);

            //parseaza raspunsul si il salveaza de tip jsonobject
            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);


            JSONObject LocationData = (JSONObject) resultsJsonObj.get("hourly");
            return (JSONObject) LocationData;

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String readApiResponse(HttpURLConnection apiConnection){
        try {
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(apiConnection.getInputStream());
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();

            return resultJson.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String processDate(JSONArray timeAPI, int index, boolean dateOrTime){
        //true - return date // false - return time
        //forma unui element de tip time este: "2024-10-24T20:00"
        if(dateOrTime){
            String auxData = (String)timeAPI.get(index);
            String[] aux = auxData.split("T");
            return aux[0];
        }else{
            String auxData = (String)timeAPI.get(index);
            String[] aux = auxData.split("T");
            return aux[1];
        }
    }

    public static double processParameters(JSONArray parameters, int index){
        double aux = (double)parameters.get(index);
        return aux;
    }
    private Long processParametersForLong(JSONArray parameters, int index) {
        Long aux = (Long)parameters.get(index);
        return aux;
    }
}

package SMSWeather.ApiMethods;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class SunRiseSetAPI {

    private String sunSetTime;
    private String sunRiseTime;
    private double latitude;
    private double longitude;
    
    
    public void getSunTimes(){

        String urlString = "https://api.sunrise-sunset.org/json?lat="+ latitude+"&lng="+longitude+"&formatted=0";
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

            JSONObject APIData = (JSONObject) resultsJsonObj.get("results");
            sunRiseTime = proccessData((String)APIData.get("sunrise"));
            sunSetTime = proccessData((String)APIData.get("sunset"));
            
                                   
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String proccessData(String fullData){
        //fullData: "2015-05-21T05:05:35+00:00"
        //hour[1]: "05:05:35+00:00" // hourV2[0] + hourV2[1] = 05 05
        //finalString: "05:05" -this is what we want

        String[] hour = fullData.split("T");
        String[] hourV2 = hour[1].split(":");
        String finalString = hourV2[0]+":"+hourV2[1];
        return finalString;
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

    public String getSunSetTime() {
        return sunSetTime;
    }

    public String getSunRiseTime() {
        return sunRiseTime;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    


}

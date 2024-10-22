package SMSWeather.ApiMethods;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class UserLocationAPI {
    
    private String city;

    public void setCity(String city) {
        this.city = city;
    }

    private double User_latitude;
    private double User_longitude;

    public double getUser_latitude() {
        return User_latitude;
    }
    public double getUser_longitude() {
        return User_longitude;
    }


    public void getUserCoordonates(){

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+ city +"&count=1&language=en&format=json";
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

            JSONArray LocationData = (JSONArray) resultsJsonObj.get("results");
            JSONObject LocationDataResults = (JSONObject)LocationData.get(0);
            User_latitude = (double) LocationDataResults.get("latitude");
            User_longitude = (double) LocationDataResults.get("longitude");
                                   
        } catch (Exception e) {
            e.printStackTrace();
        }

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

}

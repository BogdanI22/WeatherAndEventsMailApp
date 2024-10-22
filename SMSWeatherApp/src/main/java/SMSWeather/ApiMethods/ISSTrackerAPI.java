package SMSWeather.ApiMethods;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class ISSTrackerAPI {

    private double ISS_latitude;
    
    private double ISS_longitude;

    public double getISS_latitude() {
        return ISS_latitude;
    }
    public double getISS_longitude() {
        return ISS_longitude;
    }

    //merge totul ok. la fiecare apelare preia datele de la satelit
    public void getISSCoordonates(){

        String urlString = "http://api.open-notify.org/iss-now.json";
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

            JSONObject LocationData = (JSONObject) resultsJsonObj.get("iss_position");
            String lat = (String)LocationData.get("latitude");
            String lng = (String)LocationData.get("longitude");
            ISS_latitude = Double.parseDouble(lat);
            ISS_longitude = Double.parseDouble(lng);

                        
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

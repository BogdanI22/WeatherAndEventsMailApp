package SMSWeather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import SMSWeather.ApiMethods.ISSTrackerAPI;
import SMSWeather.ApiMethods.SunRiseSetAPI;
import SMSWeather.ApiMethods.UserLocationAPI;
import SMSWeather.WeatherData.WeatherElement;
import SMSWeather.WeatherData.WeatherService;
import java.util.Scanner;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    String city;
    double latitude;
    double longitude;
    String sunRiseTime;
    String sunSetTime;
    double ISSlatitude;
    double ISSlongitude;

    boolean serverON = true;
    Integer cuurentHour;
    String today;

    // Interface used to indicate that a bean should run when it is contained within
    // a SpringApplication
    @Autowired
    private WeatherService myWeatherService;

    @Autowired
    private ISSTrackerAPI issTrackerAPI;

    @Autowired
    private UserLocationAPI userLocationAPI;

    @Autowired
    private SunRiseSetAPI sunRiseSetAPI;

    @Autowired
    private EmailSenderAPI emailSenderAPI;

    @Override
    public void run(String... args) throws Exception {

        try {
            //cuurentHour = getCurrentHour();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the city: ");
            city = scanner.nextLine();
            today = getToday();
            cuurentHour = getCurrentHour();

            //the next codeblock is for testing purposes:

            while (serverON) {
                proccessAndStoreData();
                cuurentHour = getCurrentHour();
                boolean evm = false;
                boolean mgm = true;

                if(mgm == true){
                    handleMorningMail();
                    Thread.sleep(10000);
                    evm = true;
                    mgm = false;
                }
                if(evm == true){
                    handleEveningMail();
                    Thread.sleep(10000);
                    mgm = true;
                }

                //This is the actual code that runs for morning and evening notification

                // if(cuurentHour == getHour(sunRiseTime)){
                //     handleMorningMail();
                // }
                // if(cuurentHour == getHour(sunSetTime)){
                //     handleEveningMail();
                // }            
                //asteapta 30 min. 
                //Thread.sleep(1800000);
                
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void proccessAndStoreData() {

        // Preia numele locatiei dorite de user si extrage coordonatele acesteia.
        userLocationAPI.setCity(city);
        userLocationAPI.getUserCoordonates();
        latitude = userLocationAPI.getUser_latitude();
        longitude = userLocationAPI.getUser_longitude();
        System.out.println(city + ": " + latitude + " --- " + longitude);

        // Prelucreaza timpul de rasarit si apus in functie de coordonate cu ajutorul
        // unui API
        sunRiseSetAPI.setLongitude(latitude);
        sunRiseSetAPI.setLongitude(longitude);
        sunRiseSetAPI.getSunTimes();
        sunRiseTime = sunRiseSetAPI.getSunRiseTime();
        sunSetTime = sunRiseSetAPI.getSunSetTime();
        System.out.println("SunRise: " + sunRiseSetAPI.getSunRiseTime());
        System.out.println("SunSet: " + sunRiseSetAPI.getSunSetTime());

        // Preia datele de la satelit in timp real
        issTrackerAPI.getISSCoordonates();
        ISSlatitude = issTrackerAPI.getISS_latitude();
        ISSlongitude = issTrackerAPI.getISS_longitude();
        System.out.println(issTrackerAPI.getISS_latitude() + " ---- " + issTrackerAPI.getISS_longitude());

    }

    public void handleMorningMail() {
        myWeatherService.refreshData(latitude, longitude);
        String morningSemple1 = "Good Morning!\nThe sunrise will be at: "+sunRiseTime +"\nThe temperatures for today are: ";
        List<WeatherElement> todayList = new ArrayList<>();
        for(int i = 192;i<216;i++){
            todayList.add(myWeatherService.getAllElements().get(i));
        }
        String morningEmailBody ="";
        for(int i = 0;i<24;i++){
            morningEmailBody = morningEmailBody + "\n" + 
                                    todayList.get(i).getTime()+": " +
                                    +todayList.get(i).getTemperature()+ "`C and "+
                                    "precipitations:" + todayList.get(i).getPrecipitation()+
                                    " with probability: "+todayList.get(i).getPrecipitationProbability();
        }
            
        emailSenderAPI.sendEmail("bogdanionel2203@gmail.com", 
                                "Morning Notification", 
                                morningSemple1 + morningEmailBody);

    }

    public void handleEveningMail() {

        //if(ifIsNight() && ifISSInUserArea()){
            emailSenderAPI.sendEmail("bogdanionel2203@gmail.com",
                                        "Evening Notification",
                                         "Good Evening!\n"+"The sunset will be at:"+ sunSetTime + 
                                         "\nThis night the ISS(International Space Station) will be visible in your Area!"+
                                         "\n\n\n Best regards,\nYour Weather&EventsApp");
        //}
    }

    public Integer getCurrentHour(){
        String hr = new SimpleDateFormat("HH")
                .format(new Date());
        // String mn = new SimpleDateFormat("mm")
        //         .format(new Date());
        Integer hour = Integer.parseInt(hr);
        //Integer min = Integer.parseInt(mn);
        return hour;
    }

    public Integer getHour(String hr){
        String[] aux = hr.split(":");
        String aux2 = aux[0];
        Integer hour = Integer.parseInt(aux2);
        return hour;
    }

    public String getToday(){
        String tdy = new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date());
        
        return tdy;

    }

    public boolean ifISSInUserArea(){
        //daca coordonatele satelitului se apropie cu o diferenta de 5 grade de locatia userului
        if(latitude - ISSlatitude < 5 && longitude - ISSlongitude < 5){
            return true;
        }else{
            return false;
        }

    }

    public boolean ifIsNight(){
        Integer aux = (Integer)cuurentHour;
        if(aux < getHour(sunRiseTime) && aux > getHour(sunSetTime)){
            return true;
        }else{
            return false;
        }
    }

}

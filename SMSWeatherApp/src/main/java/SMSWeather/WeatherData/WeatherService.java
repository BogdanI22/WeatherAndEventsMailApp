package SMSWeather.WeatherData;

import java.util.List;


public interface WeatherService {

    public List<WeatherElement> getAllElements();
    public void addElements(double latitude, double longitude);
    public void refreshData(double latitude, double longitude);
    public void deleteAllElements();


}

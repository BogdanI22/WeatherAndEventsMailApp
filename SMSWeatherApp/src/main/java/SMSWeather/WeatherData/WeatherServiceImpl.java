package SMSWeather.WeatherData;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SMSWeather.ApiMethods.getWeatherData;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private getWeatherData weatherData;


    //functioneaza - primeste toate datele de la BD 
    @Override
    public List<WeatherElement> getAllElements() {
        return weatherRepository.findAll();
    }

    //functioneaza -inregistreaza datele
    @Override
    public void addElements(double latitude, double longitude) {
        weatherRepository.saveAll(weatherData.getElementList(latitude, longitude));
    }

    //functioneaza -sterge datele vechi si inregistreaza datele noi de la api in BD 
    @Override
    public void refreshData(double latitude, double longitude) {
        weatherRepository.deleteAll();
        weatherRepository.saveAll(weatherData.getElementList(latitude, longitude));
        
    }

    //functioneaza -sterge toate datele
    @Override
    public void deleteAllElements() {
        weatherRepository.deleteAll();
    }

}

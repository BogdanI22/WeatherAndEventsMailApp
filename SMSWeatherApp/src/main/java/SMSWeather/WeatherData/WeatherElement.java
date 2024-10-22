package SMSWeather.WeatherData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "WeatherData")

@AllArgsConstructor
@NoArgsConstructor

public class WeatherElement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    private double LocationLatitude;

    @Column
    private double LocationLongitude;

    @Column
    private String Date;

    @Column
    private String Time;

    @Column 
    private double Temperature;

    @Column
    private double Precipitation;

    @Column
    private Long PrecipitationProbability;

    public Long getPrecipitationProbability() {
        return PrecipitationProbability;
    }

    public void setPrecipitationProbability(Long precipitationProbability) {
        PrecipitationProbability = precipitationProbability;
    }

    public double getLocationLatitude() {
        return LocationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        LocationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return LocationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        LocationLongitude = locationLongitude;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getPrecipitation() {
        return Precipitation;
    }

    public void setPrecipitation(double precipitation) {
        Precipitation = precipitation;
    }








}

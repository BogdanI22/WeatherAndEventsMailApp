WeatherAndEventsMailApp: A System for Automated Weather and ISS Notifications
The WeatherAndEventsMailApp is designed to send automated email notifications to users, providing weather updates at sunrise and International Space Station (ISS) visibility alerts at sunset if the case. 

- The app connects to a mySQL database for storing the data about the weather via a weather forecast API
- The data stored include the hourly temperature, precipitations and procipitation probability on a range past 7 days and next 7 days.
- ISS Tracker is based on an API that provide the current locatuin of the ISS
- For user location (input) it's called a Geolocation API that provide the coordonates of the location name.
- Based on the information provided form the API's, the app generate 2 mails with specific data
- App is permanently refreshing the data every 30 minutes to provide the latest information.

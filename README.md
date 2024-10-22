WeatherAndEventsMailApp: A System for Automated Weather and ISS Notifications
The WeatherAndEventsMailApp is designed to send automated email notifications to users, providing weather updates at sunrise and International Space Station (ISS) visibility alerts at sunset. These notifications are tailored to the user's geographic coordinates.

Key Features:
 - Weather Data Integration: The app connects to a MySQL database that stores weather data retrieved through a weather forecast API. The stored data includes hourly temperature, precipitation levels, and precipitation probabilities for both the past 7 days and the upcoming 7 days.
 - ISS Tracking: The application utilizes an API to track the real-time location of the ISS. Based on the user’s geographical coordinates, the app determines whether the ISS will be visible in the user's night sky and sends a corresponding notification.
 - User Geolocation: A Geolocation API is employed to obtain the user’s coordinates based on the provided location name, enabling personalized notifications.
 - Automated Email Generation: Using the information obtained from the APIs, the system generates two email notifications daily: a morning email detailing the day's weather and an evening email indicating whether the ISS will be visible at night.
 - Data Refresh Cycle: The app refreshes its data every 30 minutes to ensure that the information provided to the user is up to date.

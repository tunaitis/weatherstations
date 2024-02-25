//
//  LocationService.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 25/02/2024.
//

import CoreLocation

class LocationService : NSObject, ObservableObject, CLLocationManagerDelegate {
    
    @Published var location: CLLocation? = nil
    private let locationManager = CLLocationManager()
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            self.location = location
        }
    }

    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed to find user's location: \(error.localizedDescription)")
    }
    
    func requestLocation() {
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        locationManager.requestLocation()
    }
}

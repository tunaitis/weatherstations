//
//  StationMapView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import SwiftUI
import MapKit


struct StationMapView : View {
    let stations: [Station]
    let lt = CLLocationCoordinate2D(latitude: 55.1735, longitude: 23.8948)
    let zoom = 6.5
    
    var body: some View {
        Map(initialPosition: .region(MKCoordinateRegion(center: lt, span: MKCoordinateSpan(latitudeDelta: zoom, longitudeDelta: zoom)))) {
            ForEach(stations) { station in
                if let lat = Double(station.latitude ?? ""), let lng = Double(station.longitude ?? "") {
                    Marker(station.name, coordinate: CLLocationCoordinate2D(latitude: lat, longitude: lng))
                }
            }
        }
    }
}

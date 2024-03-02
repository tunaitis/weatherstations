//
//  StationMapView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import SwiftUI
import MapKit

struct SizePreferenceKey: PreferenceKey {
    static var defaultValue: CGSize = .zero
    static func reduce(value: inout CGSize, nextValue: () -> CGSize) { value = nextValue() }
}

struct StationMapView : View {
    let stations: [Station]
    @Binding var selection: String?
    
    static let lt = CLLocationCoordinate2D(latitude: 55.1735, longitude: 23.8948)
    static let zoom = 6.0
    let initialPosition = MapCameraPosition.region(
        MKCoordinateRegion(
            center: lt,
            span: MKCoordinateSpan(
                latitudeDelta: zoom,
                longitudeDelta: zoom
            )
        )
    )
    
    var body: some View {
        Map(initialPosition: initialPosition, selection: $selection) {
            ForEach(stations) { station in
                if let lat = Double(station.latitude ?? ""), let lng = Double(station.longitude ?? "") {
                    Marker(station.name, coordinate: CLLocationCoordinate2D(latitude: lat, longitude: lng))
                }
            }
        }
    }
}

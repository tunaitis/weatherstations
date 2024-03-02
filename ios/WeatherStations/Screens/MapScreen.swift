//
//  MapScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

struct MapScreen : View {
    @ObservedObject var model: WeatherStations
    @Binding var selectedMapStation: String?
    
    var body : some View {
        StationMapView(
            stations: model.stations,
            selection: $selectedMapStation
        )
        .tabItem {
            Label("Map", systemImage: "map")
        }
        .tag(HomeScreen.map)
    }
}

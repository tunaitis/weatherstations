//
//  SettingsRepository.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 14/02/2024.
//

import SwiftUI

class SettingsRepository {
    @AppStorage("starredStations") private var starredStations = ""
    @AppStorage("homeScreen") var homeScreen: String = "\(HomeScreen.stations)"
    
    func getStarredStations() -> [String] {
        return starredStations.split(separator: ",").map(String.init)
    }
    
    func setStarredStations(stations: [String]) {
        starredStations = stations.joined(separator: ",")
    }
}

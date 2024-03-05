//
//  AppSettings.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 28/02/2024.
//

import Foundation
import SwiftUI
import Combine

enum AppTheme : String {
    case system = "system"
    case dark = "dark"
    case light = "light"
}

class AppSettings : ObservableObject {
    @AppStorage("starredStations") private var starredStations = ""
    
    @AppStorage("homeScreen") var homeScreen = HomeScreen.starred
    @AppStorage("language") var language = "en"
    @AppStorage("theme") var theme = AppTheme.system
    
    func getStarredStations() -> [String] {
        return starredStations.split(separator: ",").map(String.init)
    }
    
    func setStarredStations(stations: [String]) {
        starredStations = stations.joined(separator: ",")
    }
    
    func getAppVersion() -> String {
        if let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
            return appVersion
        }
        
        return "Unknown"
    }
}

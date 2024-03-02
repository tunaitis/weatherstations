//
//  WeatherStationsApp.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI


@main
struct WeatherStationsApp: App {
    @StateObject var model = WeatherStations()
    @StateObject var settings = AppSettings()
    
    var preferredScheme: ColorScheme? {
        switch settings.theme {
        case .dark: .dark
        case .light: .light
        case .system: nil
        }
    }
    
    var body: some Scene {
        WindowGroup {
            MainScreen(model: model, settings: settings)
                .environment(\.locale, Locale(identifier: settings.language))
                .preferredColorScheme(preferredScheme)
        }
    }
}

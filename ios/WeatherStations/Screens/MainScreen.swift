//
//  MainScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

struct MainScreen: View {
    @ObservedObject var model: WeatherStations
    @ObservedObject var settings: AppSettings
    
    @State var selectedTab: HomeScreen
    
    init(model: WeatherStations, settings: AppSettings) {
        self.model = model
        self.settings = settings
        
        self.selectedTab = settings.homeScreen
    }
    
    var body: some View {
        VStack {
            if model.isLoading {
                ProgressView()
            } else if let error = model.error {
                ErrorView(
                    message: error.localizedDescription,
                    onReload: {
                        Task {
                            await model.load()
                        }
                    }
                )
            } else {
                TabView(selection: $selectedTab) {
                    StationsScreen(model: model)
                    StarredScreen(model: model)
                    MapScreen(model: model)
                    SettingsScreen(settings: settings)
                }
            }
        }
        .task {
            await model.load()
            model.updateLocation()
        }
    }
}

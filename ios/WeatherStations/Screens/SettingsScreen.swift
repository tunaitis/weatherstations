//
//  SettingsScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

struct SettingsScreen : View {
    @ObservedObject var settings: AppSettings
    @State var darkMode = false
    
    var body : some View {
        NavigationStack {
           VStack() {
                List {
                    Section {
                        Picker(selection: $settings.homeScreen, label: Text("Home Screen")) {
                            Text("Stations").tag(HomeScreen.stations)
                            Text("Starred").tag(HomeScreen.starred)
                        }
                        Picker(selection: $settings.theme, label: Text("Theme")) {
                            Text("System").tag(AppTheme.system)
                            Text("Dark").tag(AppTheme.dark)
                            Text("Light").tag(AppTheme.light)
                        }
                        Picker(selection: $settings.language, label: Text("Language")) {
                            Text("English").tag("en")
                            Text("Lithuanian").tag("lt")
                        }
                    }
                    Section {
                        NavigationLink("About") {
                            AboutScreen(settings: settings)
                        }
                    }
                }.listStyle(.insetGrouped)
            }
            .navigationTitle("Settings")
        }
        .tabItem {
            Label("Settings", systemImage: "gear")
        }
        .tag(HomeScreen.settings)
    }
}

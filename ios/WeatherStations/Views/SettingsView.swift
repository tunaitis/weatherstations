//
//  SettingsView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 26/02/2024.
//

import SwiftUI

struct SettingsView : View {
    @StateObject var viewModel = SettingsViewModel()
    @State var darkMode = false
    @State var language = "EN"
    
    var body: some View {
        VStack() {
            List {
                Section {
                    Picker(selection: $viewModel.homeScreen, label: Text("Home Screen")) {
                        Text("Stations").tag(HomeScreen.stations)
                        Text("Starred").tag(HomeScreen.starred)
                    }
                    Toggle("Dark Mode", isOn: $darkMode)
                    Picker(selection: $language, label: Text("Language")) {
                        Text("English").tag("English")
                        Text("Lithuanian").tag("Lithuanian")
                    }
                }
                Section {
                    Text("About")
                }
            }.listStyle(.insetGrouped)
        }
    }
}

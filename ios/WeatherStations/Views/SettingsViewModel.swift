//
//  SettingsViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 26/02/2024.
//

import SwiftUI
import Combine

class SettingsViewModel : ObservableObject {
    let settingsRepository = SettingsRepository()
    
    @Published var homeScreen = HomeScreen.stations
    
    private var cancellable = Set<AnyCancellable>()
    
    init() {
        homeScreen = HomeScreen(rawValue: settingsRepository.homeScreen) ?? HomeScreen.stations
        
        $homeScreen
            .dropFirst()
            .sink { [weak self] value in
                self?.settingsRepository.homeScreen = "\(value)"
                print("update: \(value)")
            }
            .store(in: &cancellable)
    }
}

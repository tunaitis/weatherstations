//
//  ViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import Foundation
import SwiftUI

class ViewModel : ObservableObject {
    @Published var isLoading = true
    @Published var allStations: [Station] = []
    
    var starredStations: [Station] {
        allStations.filter { $0.isStarred }
    }
    
    func load() async {
        DispatchQueue.main.async {
            self.isLoading = true
        }
        
        guard let url = URL(string: "https://eismoinfo.lt/weather-conditions-service") else {
            print("invalid url")
            return
        }
        
        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            let decodedResponse = try JSONDecoder().decode([Station].self, from: data)
            
            DispatchQueue.main.async {
                self.allStations = decodedResponse
                self.isLoading = false
            }
        } catch {
            print("an error has occurred: \(error)")
            DispatchQueue.main.async {
                self.isLoading = false
            }
        }
    }
    
    func toggleStar(id: String) {
        if let index = allStations.firstIndex(where: { $0.id == id }) {
            allStations[index].isStarred.toggle()
        }
    }
}

//
//  ViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import Foundation
import SwiftUI

class ViewModel : ObservableObject {
    let stationService = StationService()
    
    @Published var isLoading = true
    @Published var allStations: [Station] = []
    
    @Published var error: Error? = nil
    
    var starredStations: [Station] {
        allStations.filter { $0.isStarred }
    }
    
    @MainActor
    func load() async {
        error = nil
        isLoading = true
        
        let result = await stationService.getStations()
        
        switch result {
        case .success(let x):
            allStations = x
            
        case .failure(let e):
            error = e
        }
        
        isLoading = false
    }
    
    func toggleStar(id: String) {
        if let index = allStations.firstIndex(where: { $0.id == id }) {
            allStations[index].isStarred.toggle()
        }
    }
}

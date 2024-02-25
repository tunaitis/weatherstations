//
//  StationHistoryViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 25/02/2024.
//
import Foundation

class StationHistoryViewModel : ObservableObject {
    let stationService = StationService()
    
    @Published private(set) var isLoading = true
    @Published var error: Error? = nil
    @Published var history: [Station] = []
    
    @MainActor
    func load(id: String) async {
        isLoading = true
        
        let result = await stationService.getHistoricalData(id: id)
        
        switch result {
        case .success(let items):
            var i = 0
            history = items.map {
                var item = $0
                item.id = "\(i)"
                i += 1
                return item
            }
        case .failure(let e):
            error = e
        }
        
        isLoading = false
    }
}


//
//  WeatherStations.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI
import Combine

class WeatherStations : ObservableObject {
    enum StationListSort {
        case Alphabetical
        case Distance
    }
    
    @Published private(set) var stations: [Station] = []
    @Published private(set) var filteredStations: [Station] = []
    @Published private(set) var isLoading = true
    @Published var error: Error? = nil
    
    @Published private(set) var starredIds: [String] = []
    
    @Published var sort: StationListSort = StationListSort.Alphabetical
    
    let stationService = StationService()
    let settings = AppSettings()
    
    private var cancellable = Set<AnyCancellable>()
    
    init() {
        $starredIds
            .combineLatest($stations)
            .sink { [weak self] (ids, stations) in
                let stations = stations.map {
                    var station = $0
                    station.isStarred = ids.contains(station.id)
                    return station
                }
                
                self?.stations = stations
            }
            .store(in: &cancellable)
    }
    
    @MainActor
    func load() async {
        error = nil
        isLoading = true
        
        let result = await stationService.getStations()
        
        switch result {
        case .success(let stations):
            self.stations = stations
            starredIds = settings.getStarredStations()
            
        case .failure(let e):
            error = e
        }
        
        isLoading = false
    }
    
    func toggleStar(id: String) {
        if starredIds.contains(id) {
            starredIds = starredIds.filter { $0 != id }
        } else {
            starredIds.append(id)
        }
        
        settings.setStarredStations(stations: starredIds)
    }
}

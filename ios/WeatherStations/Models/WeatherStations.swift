//
//  WeatherStations.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI
import Combine
import CoreLocation

enum StationListSort {
    case Alphabetical
    case Distance
}

class WeatherStations : ObservableObject {
    @Published private(set) var stations: [Station] = []
    @Published private(set) var starredIds: [String] = []
    
    @Published var sort: StationListSort = StationListSort.Alphabetical
    
    let stationService = StationService()
    let settings = AppSettings()
    let locationService = LocationService()
    
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
        
        locationService.$location
            .combineLatest($stations)
            .filter { (loc, stations) in
                return loc != nil && !stations.isEmpty
            }
            .sink { [weak self] (loc, stations) in
                
                let stations = stations.map {
                    var station = $0
                    
                    if let loc = loc, let lat = CLLocationDegrees(station.latitude ?? ""), let lng = CLLocationDegrees(station.longitude ?? "") {
                        let l = CLLocation(latitude: lat, longitude: lng)
                        station.distance = loc.distance(from: l)
                    }
                    
                    return station
                }
                
                self?.stations = stations
            }
            .store(in: &cancellable)
        
        $sort
            .dropFirst()
            .combineLatest($stations)
            .sink { [weak self] (sort, stations) in
                if let self = self {
                    if sort == StationListSort.Alphabetical {
                        self.stations = stations.sorted { $0.name < $1.name }
                    }
                    
                    if sort == StationListSort.Distance {
                        self.stations = stations.sorted { $0.distance < $1.distance }
                    }
                }
            }
            .store(in: &cancellable)
    }
    
    @MainActor
    func load() async -> Error? {
        let result = await stationService.getStations()
        
        switch result {
        case .success(let stations):
            self.stations = stations.sorted { $0.name < $1.name }
            starredIds = settings.getStarredStations()
            
        case .failure(let e):
            return e
        }
        
        return nil
    }
    
    func toggleStar(id: String) {
        if starredIds.contains(id) {
            starredIds = starredIds.filter { $0 != id }
        } else {
            starredIds.append(id)
        }
        
        settings.setStarredStations(stations: starredIds)
    }
    
    func updateLocation() {
        locationService.requestLocation()
    }
}

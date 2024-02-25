//
//  ViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import Foundation
import SwiftUI
import Combine
import CoreLocation

class MainViewModel : ObservableObject {
    enum StationListSort {
        case Alphabetical
        case Distance
    }
    
    let stationService = StationService()
    let settingsRepository = SettingsRepository()
    let locationService = LocationService()
    
    @Published private(set) var isLoading = true
    @Published private(set) var stations: [Station] = []
    @Published private(set) var filteredStations: [Station] = []
    
    var starredStations: [Station] {
        return stations.filter { $0.isStarred }
    }
    
    var filteredStarredStations: [Station] {
        return filteredStations.filter { $0.isStarred }
    }
    
    @Published private(set) var starredIds: [String] = []
    @Published var sort: StationListSort = StationListSort.Alphabetical
    
    @Published var searchQuery = ""
    @Published var error: Error? = nil
    
    private var cancellable = Set<AnyCancellable>()
    
    init() {
        
        $searchQuery
            .combineLatest($stations)
            .debounce(for: .milliseconds(80), scheduler: RunLoop.main)
            .filter { (search, stations) in
                return !search.isEmpty
            }
            .sink { [weak self] (search, stations) in
                if search.isEmpty {
                    self?.filteredStations = []
                    return
                }
                
                let search = search.lowercased()
                self?.filteredStations = stations.filter { $0.name.lowercased().contains(search)}
            }
            .store(in: &cancellable)
        
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
    
    func updateLocation() {
        locationService.requestLocation()
    }
    
    @MainActor
    func load() async {
        error = nil
        isLoading = true
        
        let result = await stationService.getStations()
        
        switch result {
        case .success(let stations):
            self.stations = stations
            starredIds = settingsRepository.getStarredStations()
            
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
        
        settingsRepository.setStarredStations(stations: starredIds)
    }
}

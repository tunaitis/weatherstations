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
    enum Route: Hashable {
        case stationPhoto(String)
    }
    
    let stationService = StationService()
    let settingsRepository = SettingsRepository()
    let locationService = LocationService()
    
    @Published private(set) var isLoading = true
    @Published private var allStations: [Station] = []
    @Published private(set) var stations: [Station] = []
    @Published private(set) var starredStations: [Station] = []
    @Published private(set) var starredIds: [String] = []
    
    @Published var searchQuery = ""
    @Published var error: Error? = nil
    
    private var cancellable = Set<AnyCancellable>()
    
    init() {
        $searchQuery
            .combineLatest($allStations)
            .debounce(for: .milliseconds(80), scheduler: RunLoop.main)
            .sink { [weak self] (search, stations) in
                if search.isEmpty {
                    self?.stations = stations
                    self?.starredStations = stations.filter { $0.isStarred }
                    return
                }
                
                let search = search.lowercased()
                
                self?.stations = stations.filter { $0.name.lowercased().contains(search)}
                self?.starredStations = stations.filter { $0.isStarred && $0.name.lowercased().contains(search) }
            }
            .store(in: &cancellable)
        
        $starredIds
            .combineLatest($allStations)
            .sink { [weak self] (ids, stations) in
                let stations = stations.map {
                    var station = $0
                    station.isStarred = ids.contains(station.id)
                    return station
                }
                
                self?.allStations = stations
                self?.starredStations = stations.filter { $0.isStarred }
            }
            .store(in: &cancellable)
        
        locationService.$location
            .combineLatest($allStations)
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
                
                self?.allStations = stations
                self?.starredStations = stations.filter { $0.isStarred }
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
            allStations = stations
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

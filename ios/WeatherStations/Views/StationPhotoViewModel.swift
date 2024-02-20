//
//  StationPhotoViewModel.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 16/02/2024.
//

import Foundation

class StationPhotoViewModel : ObservableObject {
    let stationService = StationService()
    
    @Published private(set) var isLoading = true
    @Published var error: Error? = nil
    @Published var photo: String = ""
    
    @MainActor
    func load(id: String) async {
        isLoading = true
        
        let result = await stationService.getPhoto(id: id)
        
        switch result {
        case .success(let url):
            photo = url
        case .failure(let e):
            error = e
        }
        
        isLoading = false
    }
}


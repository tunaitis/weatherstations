//
//  StationService.swift
//  WeatherStations
//
//  Created by Simonas Tunaitis on 13/02/2024.
//

import Foundation

enum APIError: Error {
    case networkError
    case unknown
}

class StationService {
    
    func getStations() async -> Result<[Station], Error> {
        let url = URL(string: "https://eismoinfo.lt/weather-conditions-service")
        
        do {
            let (data, _) = try await URLSession.shared.data(from: url!)
            let decodedResponse = try JSONDecoder().decode([Station].self, from: data)
            
            return .success(decodedResponse)
        } catch {
            return .failure(error)
        }
    }
}

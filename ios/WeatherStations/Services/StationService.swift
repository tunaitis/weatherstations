//
//  StationService.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 13/02/2024.
//

import Foundation

enum APIError: Error {
    case networkError
    case unknown
}

class StationService {
    
    func getPhoto(id: String) async -> Result<String, Error> {
        let url = URL(string: "https://eismoinfo.lt/eismoinfo-backend/feature-info/OSI/WD:\(id)?returnIcon=true")
        do {
            let (data, _) = try await URLSession.shared.data(from: url!)
            
            var photo = ""
            
            if let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any] {
                if let info = json["info"] as? [Any] {
                    if let item = info[0] as? [String: Any] {
                        if let photos = item["photos"] as? [String] {
                            if photos.count > 0 {
                                photo = photos[0]
                            }
                        }
                    }
                }
            }
            
            if photo.isEmpty {
                return .failure(APIError.networkError)
            }
            
            return .success(photo)
        } catch {
            return .failure(error)
        }
    }
    
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

//
//  Station.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 05/02/2024.
//

import Foundation

struct Station: Identifiable, Equatable {
    var id: String
    var name: String
    var road: String
    var updated: String
    var isStarred: Bool = false
    var temperature: String?
    var dewPoint: String?
    var precipitation: String?
    var windAverage: String?
    var windMax: String?
    var windDirection: String?
    var roadSurface: String?
    var visibility: String?
    var latitude: String?
    var longitude: String?
    var distance: Double = 0
    
}

extension Station : Decodable {
    private enum CodingKeys: String, CodingKey {
        case id
        case name = "irenginys"
        case road = "pavadinimas"
        case updated = "surinkimo_data"
        case temperature = "oro_temperatura"
        case dewPoint = "rasos_taskas"
        case precipitation = "krituliu_kiekis"
        case windAverage = "vejo_greitis_vidut"
        case windMax = "vejo_greitis_maks"
        case windDirection = "vejo_kryptis"
        case roadSurface = "kelio_danga"
        case visibility = "matomumas"
        case latitude = "lat"
        case longitude = "lng"
    }
}

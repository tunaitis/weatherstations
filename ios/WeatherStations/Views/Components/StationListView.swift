//
//  StationListView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import SwiftUI
import Foundation

struct StationListView: View {
    var stations: [Station]
    var onStarChange: (String) -> Void
    var onPhotoClick: (String) -> Void
    
    var body: some View {
        VStack {
            List {
                ForEach(stations) { station in
                    StationView(
                        station: station,
                        onStarChange: onStarChange,
                        onPhotoClick: onPhotoClick
                    )
                }
            }
            .listStyle(.plain)
        }
    }
}

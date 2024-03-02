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
    var onStarClick: (String) -> Void
    var onHistoryClick: (String) -> Void
    var onPhotoClick: (String) -> Void
    
    var body: some View {
        VStack {
            List {
                ForEach(stations) { station in
                    StationView(
                        station: station,
                        onStarClick: onStarClick,
                        onHistoryClick: onHistoryClick,
                        onPhotoClick: onPhotoClick
                    )
                }
            }
            .listStyle(.plain)
        }
    }
}

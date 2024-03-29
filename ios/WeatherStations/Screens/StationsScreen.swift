//
//  StationsScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

struct StationsScreen : View {
    @ObservedObject var model: WeatherStations
    var onPhotoClick: (String) -> Void
    var onHistoryClick: (String) -> Void
    
    @State var searchQuery = ""
    
    
    var filteredStations: [Station] {
        guard !searchQuery.isEmpty else { return model.stations }
        let lowercasedSearchQuery = searchQuery.lowercased()
        return model.stations.filter { station in
            station.name.lowercased().contains(lowercasedSearchQuery)
        }
    }
    
    var body : some View {
        NavigationStack {
            StationListView(
                stations: filteredStations,
                onStarClick: { model.toggleStar(id: $0) },
                onHistoryClick: onHistoryClick,
                onPhotoClick: onPhotoClick
            )
            .navigationTitle("Stations")
            .searchable(text: $searchQuery, placement: .navigationBarDrawer(displayMode: .always))
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Menu {
                        Picker("Sort", selection: $model.sort) {
                            Text("A-Z").tag(StationListSort.Alphabetical)
                            Text("Distance").tag(StationListSort.Distance)
                        }
                    } label: {
                        Image(systemName: "line.3.horizontal.decrease")
                    }
                }
            }
        }
        .tabItem {
            Label("Stations", systemImage: "house")
        }
        .tag(HomeScreen.stations)
    }
}

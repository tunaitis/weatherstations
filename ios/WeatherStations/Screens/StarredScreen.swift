//
//  StarredScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

struct StarredScreen : View {
    @ObservedObject var model: WeatherStations
    @State var searchQuery = ""
    
    var filteredStations: [Station] {
        guard !searchQuery.isEmpty else { return model.stations.filter({ $0.isStarred }) }
        let lowercasedSearchQuery = searchQuery.lowercased()
        return model.stations.filter { station in
            station.isStarred && station.name.lowercased().contains(lowercasedSearchQuery)
        }
    }
    
    var body : some View {
        NavigationStack {
            StationListView(
                stations: filteredStations,
                onStarClick: { model.toggleStar(id: $0) },
                onHistoryClick: { id in
                    //presentedSheet = Sheet.showHistory(id)
                },
                onPhotoClick: { id in
                    //presentedSheet = Sheet.showPhoto(id)
                }
            )
            .navigationTitle("Starred")
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
            Label("Starred", systemImage: "star")
        }
        .tag(HomeScreen.starred)
    }
}

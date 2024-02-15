//
//  ContentView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI

struct ContentView: View {
    @StateObject var viewModel = ViewModel()
    @State var s = ""
    
    var body: some View {
        VStack {
            if viewModel.isLoading {
                ProgressView()
            } else if let error = viewModel.error {
                ErrorView(
                    message: error.localizedDescription,
                    onReload: {
                        Task {
                            await viewModel.load()
                        }
                    }
                )
            } else {
                TabView {
                    NavigationView {
                        StationListView(stations: viewModel.stations, onStarChange: { viewModel.toggleStar(id: $0) })
                            .navigationTitle("All Stations")
                            .searchable(text: $viewModel.searchQuery, placement: .navigationBarDrawer(displayMode: .always))
                    }
                    .tabItem {
                        Label("Stations", systemImage: "house")
                    }
                    
                    NavigationView {
                        
                        StationListView(stations: viewModel.starredStations, onStarChange: { viewModel.toggleStar(id: $0) })
                            .navigationTitle("Starred Stations")
                            .searchable(text: $viewModel.searchQuery, placement: .navigationBarDrawer(displayMode: .always))
                    }
                    .tabItem {
                        Label("Starred", systemImage: "star")
                    }
                    
                    StationMapView()
                        .tabItem {
                            Label("Map", systemImage: "map")
                        }
                }
            }
        }
        .task {
            await viewModel.load()
        }
    }
    
}

#Preview {
    ContentView()
}


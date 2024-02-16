//
//  ContentView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI

struct MainView: View {
    @StateObject var viewModel = MainViewModel()
    
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
                NavigationStack(path: $viewModel.navigationPath) {
                    TabView {
                        StationListView(
                            stations: viewModel.stations,
                            onStarChange: { viewModel.toggleStar(id: $0) },
                            onPhotoClick: { id in
                                viewModel.navigationPath.append(MainViewModel.Route.stationPhoto(id))
                            }
                        )
                        .tabItem {
                            Label("Stations", systemImage: "house")
                        }
                        
                        StationListView(
                            stations: viewModel.starredStations,
                            onStarChange: { viewModel.toggleStar(id: $0) },
                            onPhotoClick: { id in }
                        )
                        .tabItem {
                            Label("Starred", systemImage: "star")
                        }
                        
                        StationMapView()
                            .tabItem {
                                Label("Map", systemImage: "map")
                            }
                    }
                    .navigationTitle("All Stations")
                    .searchable(text: $viewModel.searchQuery, placement: .navigationBarDrawer(displayMode: .always))
                    .navigationDestination(for: MainViewModel.Route.self) { route in
                        switch (route) {
                        case .stationPhoto(let id):
                            StationPhotoView(id: id)
                        }
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
    MainView()
}


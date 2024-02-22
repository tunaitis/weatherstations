//
//  ContentView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI

struct MainView: View {
    
    enum Sheet: Hashable, Identifiable {
        case showPhoto(String)
        case showHistory
        
        var id: Self { return self }
    }
    
    @StateObject var viewModel = MainViewModel()
    @State var presentedSheet: Sheet?
    
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
                    NavigationStack {
                        StationListView(
                            stations: viewModel.stations,
                            onStarClick: { viewModel.toggleStar(id: $0) },
                            onPhotoClick: { id in
                                presentedSheet = Sheet.showPhoto(id)
                            }
                        )
                        .navigationTitle("Stations")
                        .searchable(text: $viewModel.searchQuery, placement: .navigationBarDrawer(displayMode: .always))
                    }
                    .tabItem {
                        Label("Stations", systemImage: "house")
                    }
                    
                    
                    NavigationStack {
                        StationListView(
                            stations: viewModel.starredStations,
                            onStarClick: { viewModel.toggleStar(id: $0) },
                            onPhotoClick: { id in
                                presentedSheet = Sheet.showPhoto(id)
                            }
                        )
                        .navigationTitle("Starred")
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
                .sheet(item: $presentedSheet) { sheet in
                    switch sheet {
                    case .showPhoto(let id):
                        if let station = viewModel.stations.first(where: { $0.id == id }) {
                            StationPhotoView(
                                station: station,
                                onCloseClick: {
                                    presentedSheet = nil
                                }
                            )
                        }
                        
                    case .showHistory:
                        Text("History")
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


//
//  ContentView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI

struct GeometryContentSize<Content: View>: View {
    public var content: (CGSize) -> Content
    
    var body: some View {
        GeometryReader { geo in
            content(geo.size)
        }.frame(height: 200)
    }
}

struct MainView: View {
    
    enum Sheet: Hashable, Identifiable {
        case showPhoto(String)
        case showHistory(String)
        
        var id: Self { return self }
    }
    
    @StateObject var viewModel = MainViewModel()
    @State var presentedSheet: Sheet?
    @State var selectedOption: String = "Aaa"
    @State var selectedMapStation: String?
    @State private var size: CGSize = .zero
    
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
                TabView(selection: $viewModel.selectedTab) {
                    NavigationStack {
                        StationListView(
                            stations: viewModel.searchQuery.isEmpty ? viewModel.stations : viewModel.filteredStations,
                            onStarClick: { viewModel.toggleStar(id: $0) },
                            onHistoryClick: { id in
                                presentedSheet = Sheet.showHistory(id)
                            },
                            onPhotoClick: { id in
                                presentedSheet = Sheet.showPhoto(id)
                            }
                        )
                        .navigationTitle("Stations")
                        .searchable(text: $viewModel.searchQuery, placement: .navigationBarDrawer(displayMode: .always))
                        .toolbar {
                            ToolbarItem(placement: .navigationBarTrailing) {
                                Menu {
                                    Picker("Foo", selection: $viewModel.sort) {
                                        Text("A-Z").tag(MainViewModel.StationListSort.Alphabetical)
                                        Text("Distance").tag(MainViewModel.StationListSort.Distance)
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
                    
                    NavigationStack {
                        StationListView(
                            stations: viewModel.searchQuery.isEmpty ? viewModel.starredStations : viewModel.filteredStarredStations,
                            onStarClick: { viewModel.toggleStar(id: $0) },
                            onHistoryClick: { id in
                                presentedSheet = Sheet.showHistory(id)
                            },
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
                    .tag(HomeScreen.starred)
                    
                    StationMapView(
                        stations: viewModel.stations,
                        selection: $selectedMapStation
                    )
                    .tabItem {
                        Label("Map", systemImage: "map")
                    }
                    .tag(HomeScreen.map)
                    
                    NavigationStack {
                        SettingsView()
                        .navigationTitle("Settings")
                    }
                    .tabItem {
                        Label("Settings", systemImage: "gear")
                    }
                    .tag(HomeScreen.settings)
                }
                .sheet(item: $selectedMapStation) { id in
                    if let station = viewModel.stations.first(where: { $0.id == id }) {
                        StationView(
                            station: station,
                            onStarClick: { viewModel.toggleStar(id: $0) },
                            onHistoryClick: { id in
                                selectedMapStation = nil
                                presentedSheet = Sheet.showHistory(id)
                            },
                            onPhotoClick: { id in
                                selectedMapStation = nil
                                presentedSheet = Sheet.showPhoto(id)
                            }
                        )
                        .padding()
                        .presentationDragIndicator(.visible)
                        .presentationDetents([.medium])
                        .frame(maxHeight: .infinity, alignment: .top)
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
                        
                    case .showHistory(let id):
                        if let station = viewModel.stations.first(where: { $0.id == id }) {
                            StationHistoryView(
                                station: station,
                                onCloseClick: {
                                    presentedSheet = nil
                                }
                            )
                        }
                    }
                }
            }
        }
        .task {
            await viewModel.load()
        }
        .task {
            viewModel.updateLocation()
        }
    }
    
}

#Preview {
    MainView()
}


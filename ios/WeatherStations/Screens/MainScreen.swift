//
//  MainScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 29/02/2024.
//

import SwiftUI

enum MainScreenSheet: Hashable, Identifiable {
    case photo(String)
    case history(String)
    
    var id: Self { return self }
}

enum LoadingState<T> {
    case loading
    case loaded(T)
    case error(Error)
}
    

struct MainScreen: View {
    @ObservedObject var model: WeatherStations
    @ObservedObject var settings: AppSettings
    
    @State var selectedTab: HomeScreen
    @State var presentedSheet: MainScreenSheet?
    @State var selectedMapStation: String?
    
    @State var state: LoadingState<Void> = .loading
    
    init(model: WeatherStations, settings: AppSettings) {
        self.model = model
        self.settings = settings
        self.selectedTab = settings.homeScreen
    }
    
    func load() async {
        state = .loading
        
        if let error = await model.load() {
            state = .error(error)
            return
        }
        
        state = .loaded(())
    }
    
    var body: some View {
        VStack {
            
            switch state {
            case .loading:
                ProgressView()
            case .error(let error):
                ErrorView(
                    message: error.localizedDescription,
                    onReload: {
                        Task {
                            await load()
                        }
                    }
                )
            case .loaded:
                TabView(selection: $selectedTab) {
                    StationsScreen(
                        model: model,
                        onPhotoClick: { presentedSheet = .photo($0) },
                        onHistoryClick: { presentedSheet = .history($0) }
                    )
                    StarredScreen(
                        model: model,
                        onPhotoClick: { presentedSheet = .photo($0) },
                        onHistoryClick: { presentedSheet = .history($0) }
                    )
                    MapScreen(
                        model: model,
                        selectedMapStation: $selectedMapStation
                    )
                    SettingsScreen(settings: settings)
                }
                .sheet(item: $selectedMapStation) { id in
                    if let station = model.stations.first(where: { $0.id == id }) {
                        StationView(
                            station: station,
                            onStarClick: { model.toggleStar(id: $0) },
                            onHistoryClick: { id in
                                selectedMapStation = nil
                                presentedSheet = .history(id)
                            },
                            onPhotoClick: { id in
                                selectedMapStation = nil
                                presentedSheet = .photo(id)
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
                    case .photo(let id):
                        if let station = model.stations.first(where: { $0.id == id }) {
                            StationPhotoView(
                                station: station,
                                onCloseClick: {
                                    presentedSheet = nil
                                }
                            )
                        }
                        
                    case .history(let id):
                        if let station = model.stations.first(where: { $0.id == id }) {
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
            await load()
            model.updateLocation()
        }
    }
}

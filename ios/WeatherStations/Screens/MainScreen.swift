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

struct MainScreen: View {
    @ObservedObject var model: WeatherStations
    @ObservedObject var settings: AppSettings
    
    @State var selectedTab: HomeScreen
    @State var presentedSheet: MainScreenSheet?
    
    init(model: WeatherStations, settings: AppSettings) {
        self.model = model
        self.settings = settings
        
        self.selectedTab = settings.homeScreen
    }
    
    var body: some View {
        VStack {
            if model.isLoading {
                ProgressView()
            } else if let error = model.error {
                ErrorView(
                    message: error.localizedDescription,
                    onReload: {
                        Task {
                            await model.load()
                        }
                    }
                )
            } else {
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
                    MapScreen(model: model)
                    SettingsScreen(settings: settings)
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
            await model.load()
            model.updateLocation()
        }
    }
}

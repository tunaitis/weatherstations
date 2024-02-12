//
//  ContentView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 04/02/2024.
//

import SwiftUI

struct ContentView: View {
    @StateObject var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack {
            
            if viewModel.isLoading {
                ProgressView()
            } else {
                TabView {
                    
                    StationListView(stations: viewModel.allStations, onStarChange: { viewModel.toggleStar(id: $0) })
                        .tabItem {
                            Label("Stations", systemImage: "house")
                        }
                    
                    StationListView(stations: viewModel.starredStations, onStarChange: { viewModel.toggleStar(id: $0) })
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


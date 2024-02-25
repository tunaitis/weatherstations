//
//  StationHistorView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 25/02/2024.
//

import SwiftUI

struct StationHistoryView : View  {
    var station: Station
    var onCloseClick: () -> Void
    
    @StateObject var viewModel = StationHistoryViewModel()
    
    var body: some View {
        VStack {
            if viewModel.isLoading {
                ProgressView()
            } else {
                NavigationStack {
                    VStack {
                        ScrollView([.horizontal, .vertical]) {
                            Grid(verticalSpacing: 15) {
                                GridRow {
                                    Text("Date")
                                    Text("Temperature")
                                    Text("Dew Point")
                                    Text("Precipitation")
                                    Text("Surface")
                                    Text("Wind Avg")
                                    Text("Wind Max")
                                }.bold()
                                Divider()
                                ForEach(viewModel.history) { item in
                                    GridRow {
                                        Text(item.updated)
                                        Text("\(item.temperature ?? "") °C")
                                        Text("\(item.dewPoint ?? "") °C")
                                        Text("\(item.precipitation ?? "") mm")
                                        Text("\(item.roadSurface ?? "")")
                                        Text("\(item.windAverage ?? "") m/s")
                                        Text("\(item.windMax ?? "") m/s")
                                    }
                                    if item != viewModel.history.last {
                                        Divider()
                                    }
                                }
                            }
                            .padding()
                        }
                    }
                    .navigationBarTitleDisplayMode(.inline)
                    .navigationTitle(station.name)
                    .toolbar {
                        Button("Close", action: onCloseClick)
                    }
                }
            }
        }
        .task {
            await viewModel.load(id: station.id)
        }
    }
}

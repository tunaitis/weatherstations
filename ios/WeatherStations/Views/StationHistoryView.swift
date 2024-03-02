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
    
    @State var state: LoadingState<[Station]> = .loading
    
    let stationService = StationService()
    
    func load() async  {
        let result = await stationService.getHistoricalData(id: station.id)
        
        switch result {
        case .success(var history):
            var i = 0
            history = history.map {
                var item = $0
                item.id = "\(i)"
                i += 1
                return item
            }
            
            state = .loaded(history)
        case .failure(let error):
            state = .error(error)
        }
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
            case .loaded(let history):
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
                                ForEach(history) { item in
                                    GridRow {
                                        Text(item.updated)
                                        Text("\(item.temperature ?? "") °C")
                                        Text("\(item.dewPoint ?? "") °C")
                                        Text("\(item.precipitation ?? "") mm")
                                        Text("\(item.roadSurface ?? "")")
                                        Text("\(item.windAverage ?? "") m/s")
                                        Text("\(item.windMax ?? "") m/s")
                                    }
                                    if item != history.last {
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
            await load()
        }
    }
}

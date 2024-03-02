//
//  StationPhotoView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 16/02/2024.
//

import Foundation
import SwiftUI

struct StationPhotoView : View  {
    @ObservedObject var model: WeatherStations
    var station: Station
    var onCloseClick: () -> Void
    
    @State var state: LoadingState<String> = .loading
    
    let stationService = StationService()
    
    func load() async {
        let result = await stationService.getPhoto(id: station.id)
        
        switch result {
        case .success(let photo):
            state = .loaded(photo)
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
            case .loaded(let photo):
                NavigationStack {
                    VStack {
                        AsyncImage(url: URL(string: photo)) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                        } placeholder: {
                            ProgressView()
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

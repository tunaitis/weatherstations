//
//  StationPhotoView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 16/02/2024.
//

import Foundation
import SwiftUI

struct StationPhotoView : View  {
    var station: Station
    var onCloseClick: () -> Void
    
    @StateObject var viewModel = StationPhotoViewModel()
    
    var body: some View {
        VStack {
            if viewModel.isLoading {
                ProgressView()
            } else {
                NavigationStack {
                    VStack {
                        AsyncImage(url: URL(string: viewModel.photo)) { image in
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
            await viewModel.load(id: station.id)
        }
    }
}

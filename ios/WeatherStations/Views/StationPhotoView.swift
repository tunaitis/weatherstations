//
//  StationPhotoView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 16/02/2024.
//

import Foundation
import SwiftUI

struct StationPhotoView : View  {
    var id: String
    @StateObject var viewModel = StationPhotoViewModel()
    
    var body: some View {
        VStack {
            if viewModel.isLoading {
                ProgressView()
            } else if let error = viewModel.error {
                ErrorView(
                    message: error.localizedDescription,
                    onReload: {
                        Task {
                            await viewModel.load(id: id)
                        }
                    }
                )
            } else {
                AsyncImage(url: URL(string: viewModel.photo)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                } placeholder: {
                    ProgressView()
                }
            }
        }
        .task {
            await viewModel.load(id: id)
        }
    }
}

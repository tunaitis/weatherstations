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
    var viewModel = StationPhotoViewModel()
    
    var body: some View {
        VStack {
            if viewModel.isLoading {
                ProgressView()
            } else {
                Text("Loaded")
            }
        }
    }
}

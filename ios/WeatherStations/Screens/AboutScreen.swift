//
//  AboutScreen.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 05/03/2024.
//

import SwiftUI

struct AboutScreen : View {
    @ObservedObject var settings: AppSettings
    
    var body : some View {
        VStack(alignment: .leading, spacing: 15) {
            HStack(spacing: 15) {
                Image("logo")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 64)
                    .cornerRadius(10)
                VStack(alignment: .leading, spacing: 5) {
                    Text("Weather Stations")
                        .font(.title)
                    Text("Version: \(settings.getAppVersion())")
                }
            }
            Text("View weather information from more than 100 stations located on the main roads in Lithuania.")
            Text("The weather data used in the app comes from [Lithuanian Road Administration](https://eismoinfo.lt).")
            Text("Weather Stations is free and open source software. The source code can be found [here](https://github.com/tunaitis/weatherstations). If you want to report a bug or request a feature, please [create an issue](https://github.com/tunaitis/weatherstations/issues) on the project's GitHub page.")
            Text("The app icon is from [flaticon.com](https://flaticon.com).")
            Spacer()
        }
        .padding()
        .navigationTitle("About")
    }
}

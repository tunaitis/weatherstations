//
//  ErrorView.swift
//  WeatherStations
//
//  Created by Simonas Tunaitis on 14/02/2024.
//

import SwiftUI

struct ErrorView : View {
    var message: String
    var onReload: (() -> Void)?
    
    var body: some View {
        VStack(spacing: 20) {
            Text(message)
            if let reloadCallback = onReload {
                Button(action: reloadCallback, label: { Text("Reload") })
                    .buttonStyle(.borderedProminent)
            }
        }
    }
}

//
//  View.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 01/03/2024.
//

import SwiftUI

extension View {
    @ViewBuilder func `if`<Content: View>(_ condition: @autoclosure () -> Bool, transform: (Self) -> Content) -> some View {
            if condition() {
                transform(self)
            } else {
                self
            }
        }
}

//
//  AutoHeight.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 24/02/2024.
//

import SwiftUI

struct HeightPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat?

    static func reduce(value: inout CGFloat?, nextValue: () -> CGFloat?) {
        guard let nextValue = nextValue() else { return }
        value = nextValue
    }
}

struct AutoHeightView<Content>: View where Content: View {
    var content: () -> Content
    
    @State private var size: CGSize = .zero
    
    var body: some View {
        content()
            .background {
                GeometryReader { geometry in
                    Color.clear.preference(key: SizePreferenceKey.self, value: geometry.size)
                }
            }
            .onPreferenceChange(SizePreferenceKey.self) { newSize in
                size.height = newSize.height
            }
            .padding(.horizontal)
            .presentationDetents([.height(size.height)])
            .presentationDragIndicator(.visible)
    }
}


extension View {
    
    func autoHeight() -> some View {
        AutoHeightView {
            self
        }
    }
}

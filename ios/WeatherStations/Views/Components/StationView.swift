//
//  StationView.swift
//  WeatherStations
//
//  Created by Simon Tunaitis on 12/02/2024.
//

import SwiftUI
import Foundation

struct StationPropView: View {
    var name: String
    var value: String
    
    var body: some View {
        VStack {
            VStack {
                Text(name)
                    .font(.caption2)
                    .foregroundStyle(.secondary)
                    .lineLimit(1)
                Text(value)
                    .lineLimit(1)
            }
            .padding(10)
        }
        .frame(maxWidth: .infinity)
        .background(Color(UIColor.tertiarySystemFill))
        .clipShape(RoundedRectangle(cornerRadius: 10.0, style: .circular))
    }
}

struct StationView: View {
    var station: Station
    var onStarChange: (String) -> Void
    var onPhotoClick: (String) -> Void
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                VStack(alignment: .leading) {
                    Text(station.name)
                        .font(.title3)
                    Text(station.road)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
                
                Spacer()
                
                Button {
                    onStarChange(station.id)
                }
                label: {
                    Image(systemName: station.isStarred ? "star.fill" : "star")
                }.buttonStyle(.borderless)
            }
            
            HStack {
                StationPropView(name: "Temperature", value: "\(station.temperature ?? "") °C")
                StationPropView(name: "Precipitation", value: "\(station.precipitation ?? "") mm")
                StationPropView(name: "Road Surface", value: "\(station.roadSurface ?? "")")
            }
            .frame(maxWidth: .infinity)
            
            HStack {
                StationPropView(name: "Wind", value: "\(station.windAverage ?? "") m/s")
                StationPropView(name: "Wind Max", value: "\(station.windMax ?? "") m/m")
                StationPropView(name: "Wind Direction", value: "\(station.windDirection ?? "")")
            }
            .frame(maxWidth: .infinity)
            
            HStack {
                StationPropView(name: "Visibility", value: "\(station.visibility ?? "") m")
                StationPropView(name: "Dew Point", value: "\(station.temperature ?? "") °C")
                StationPropView(name: "Road Surface", value: "\(station.roadSurface ?? "")")
            }
            .frame(maxWidth: .infinity)
            
            Spacer()
            
            HStack() {
                
                VStack(alignment: .leading) {
                    Text("Updated")
                        .font(.caption2)
                        .foregroundStyle(.secondary)
                    Text(station.updated)
                        .font(.subheadline)
                }
                
                Spacer()
                
                HStack {
                    Button {
                        onPhotoClick(station.id)
                    }
                    label: {
                        Image(systemName: "photo")
                    }.buttonStyle(.borderless)
                }
            }
        }
        .padding(.vertical)
    }
}

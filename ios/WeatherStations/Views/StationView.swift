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
    var onStarClick: (String) -> Void
    var onHistoryClick: (String) -> Void
    var onPhotoClick: (String) -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 15) {
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
                    onStarClick(station.id)
                }
                label: {
                    Image(systemName: station.isStarred ? "star.fill" : "star")
                }.buttonStyle(.borderless)
            }
            
            VStack {
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
            }
            
            HStack() {
                
                HStack(spacing: 15) {
                    VStack(alignment: .leading) {
                        Text("Updated")
                            .font(.caption2)
                            .foregroundStyle(.secondary)
                        Text(station.updated)
                            .font(.subheadline)
                    }
                    
                    if (station.distance > 0) {
                        VStack(alignment: .leading) {
                            Text("Distance")
                                .font(.caption2)
                                .foregroundStyle(.secondary)
                            Text(String(format: "%.2f km", station.distance / 1000))
                                .font(.subheadline)
                        }
                    }
                }
                
                Spacer()
                
                HStack(spacing: 15) {
                    Button {
                        onHistoryClick(station.id)
                    }
                    label: {
                        Image(systemName: "clock.arrow.circlepath")
                            .foregroundColor(.secondary)
                    }.buttonStyle(.borderless)
                    
                    
                    Button {
                        onPhotoClick(station.id)
                    }
                    label: {
                        Image(systemName: "photo")
                            .foregroundColor(.secondary)
                    }.buttonStyle(.borderless)
                }
            }
        }
        .padding(.vertical)
    }
}

//
//  BreedUIView.swift
//  iosApp
//
//  Created by Alexandru IACHIMOV on 10.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared
import Kingfisher

struct BreedUIView: View {
  var breed: Breed
  var onFavouriteTapped: (Breed) -> Void = {_ in }

  var body: some View {
      // Arrange views vertically
      VStack {
          // Using view from Kingfisher dependency as image loading view
          KFImage(URL(string: breed.imageUrl))
              .resizable()
              .scaledToFit()
              .cornerRadius(16)

          // Arrange views horizontally
          HStack {
              Text(breed.name).padding(16)
              Spacer()
              Button(
                action: { onFavouriteTapped(breed) },
                label: {
                   if(breed.isFavourite){
                     Image(systemName: "heart.fill")
                        .resizable()
                        .aspectRatio(1, contentMode: .fit)
                        .frame(width: 24)
                   } else {
                     Image(systemName: "heart")
                        .resizable()
                        .aspectRatio(1, contentMode: .fit)
                        .frame(width: 24)
                        }
                }).padding(16)
            }
        }
    }
}

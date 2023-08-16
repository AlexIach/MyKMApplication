//
//  BreedsGridUIView.swift
//  iosApp
//
//  Created by Alexandru IACHIMOV on 10.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

/**
 * Displaying grid of breed items. View takes array of breeds and a closure
 */
struct BreedsGridUIView: View {
  var breeds: Array<Breed>
  var onFavouriteTapped: (Breed) -> Void = {_ in }

  var body: some View {
      // Define layout for Grid columns. Here we specify how the breed items will be arranged within the grid.
      let columns = [
          GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16),
          GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16)
      ]

      // Wrap LazyGrid into Scroll view to allow scrolling  through the grid if it overflows the available screen space.
      ScrollView {
          LazyVGrid(columns: columns, spacing: 16){
              ForEach(breeds, id: \.name) { breed in
               BreedUIView(breed: breed,
               onFavouriteTapped: onFavouriteTapped)
              }
          }.padding(.horizontal, 16)
      }
  }
}

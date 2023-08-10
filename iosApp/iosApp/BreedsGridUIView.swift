//
//  BreedsGridUIView.swift
//  iosApp
//
//  Created by Alexandru IACHIMOV on 10.08.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct BreedsGridUIView: View {
  var breeds: Array<Breed>
  var onFavouriteTapped: (Breed) -> Void = {_ in }

  var body: some View {
      let columns = [
          GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16),
          GridItem(.flexible(minimum: 128, maximum: 256), spacing: 16)
      ]

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

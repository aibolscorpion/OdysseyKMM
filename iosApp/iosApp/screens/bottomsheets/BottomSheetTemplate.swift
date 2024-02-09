//
//  BottomSheetTemplate.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

func bottomSheetTemplate(isError: Bool, title: String, description: String) -> some View{
    return VStack{
            VStack(spacing: 0){
                if isError {
                    Text(title)
                        .font(.custom("PTRootUI-Bold", size: 20.0))
                        .foregroundColor(Color(red: 1.0, green: 66.0 / 255.0, blue: 66.0 / 255.0))
                        .multilineTextAlignment(.center)
                        .frame(height: 24.0, alignment: .center)
                }else {
                    Text(title)
                        .font(.custom("PTRootUI-Bold", size: 20.0))
                        .foregroundColor(Color(red: 8.0 / 255.0, green: 20.0 / 255.0, blue: 38.0 / 255.0))
                        .multilineTextAlignment(.center)
                        .frame(height: 24.0, alignment: .center)
                }
                
                Text(description)
                    .padding(.top, 17)
                    .font(.custom("PTRootUI-Medium", size: 15.0))
                    .foregroundColor(Color(red: 27.0 / 255.0, green: 54.0 / 255.0, blue: 82.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .kerning(0.3)
                    .opacity(0.5)
                
                Rectangle()
                    .frame(width: 296.0, height: 1.0)
                    .padding(.top, 25)
                    .foregroundColor(Color(white: 235.0 / 255.0))
                
                Button(action: contactSupport){
                    Text("Обратиться в поддержку")
                        .frame(maxWidth: .infinity, maxHeight: 55)
                        .font(.custom("PTRootUI-Bold", size: 17.0))
                        .foregroundColor(Color(red: 0.0, green: 97.0 / 255.0, blue: 225.0 / 255.0))
                        .multilineTextAlignment(.center)
                        .kerning(0.2)
                }
            }
            .frame(maxWidth: .infinity, alignment: .top)
            .padding(EdgeInsets(top: 32, leading: 16, bottom: 0, trailing: 16))
            .background(Color(white: 1.0))
            .cornerRadius(8.0)
            .overlay(
                RoundedRectangle(cornerRadius: 8.0)
                    .inset(by: 0.5)
                    .stroke(Color(red: 225.0 / 255.0, green: 232.0 / 255.0, blue: 240.0 / 255.0), lineWidth: 1.0)
            )
            .shadow(color: Color(red: 0.0, green: 7.0 / 255.0, blue: 123.0 / 255.0, opacity: 0.02), radius: 2.0, x: 0.0, y: 0.0)
            .shadow(color: Color(white: 0.0, opacity: 0.03), radius: 8.0, x: 0.0, y: 4.0)
            
            Button(action: tryAgain){
                Text("Попробовать снова")
                    .frame(maxWidth: .infinity, maxHeight: 52.0)
                    .background(Color(white: 1.0))
                    .cornerRadius(8.0)
                    .overlay(
                        RoundedRectangle(cornerRadius: 8.0)
                            .inset(by: 0.5)
                            .stroke(Color(red: 225.0 / 255.0, green: 232.0 / 255.0, blue: 240.0 / 255.0), lineWidth: 1.0)
                    )
                    .shadow(color: Color(red: 0.0, green: 7.0 / 255.0, blue: 123.0 / 255.0, opacity: 0.02), radius: 2.0, x: 0.0, y: 0.0)
                    .shadow(color: Color(white: 0.0, opacity: 0.03), radius: 8.0, x: 0.0, y: 4.0)
                    .font(.custom("PTRootUI-Bold", size: 17.0))
                    .foregroundColor(Color(red: 0.0, green: 97.0 / 255.0, blue: 225.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .kerning(0.2)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 16)
}


func tryAgain(){
    print("trying again")
}

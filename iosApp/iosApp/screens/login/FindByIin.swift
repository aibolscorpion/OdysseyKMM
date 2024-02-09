//
//  FindByIin.swift
//  iosApp
//
//  Created by Android Developer on 02.02.2024.
//

import SwiftUI
import Combine

struct FindByIin: View {
    @State private var iin: String = ""
    private let iinLength = 12
    var body: some View {
        VStack{
            
            VStack{
                
                Text("Номер не найден. \nВойти по ИИН")
                    .font(.custom("PTRootUI-Bold", size: 24.0))
                    .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .kerning(-0.4)
                
                  Text("Номер +7 (701) 620 94 40 не найден. Пожалуйста, введите ваш ИИН.")
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 207.0 / 255.0, green: 213.0 / 255.0, blue: 220.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .padding(.top, 16)
                
                
                Text("Изменить номер")
                    .padding(.top, 10)
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 64.0 / 255.0, green: 189.0 / 255.0, blue:   1.0))
                
                
                VStack(alignment: .leading){
                    Text("ИИН")
                        .font(.custom("PTRootUI-Medium", size: 13.0))
                        .foregroundColor(Color(white: 235.0 / 255.0, opacity: 0.39))
                        .kerning(0.2)
                    
                    TextField("", text: $iin, prompt:
                            Text("_ _ _ _ _ _ _ _ _ _ _ _")
                                .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0)))
                        .onChange(of: iin, perform: { _ in
                            if iin.count > iinLength {
                                iin = String(iin.prefix(iinLength))
                            }
                        })
                        .keyboardType(.numberPad)
                        .foregroundColor(.white)
                        .font(.custom("PTRootUI-Bold", size: 17.0))
                        .kerning(0.3)
                        .frame(height: 24.0)
                    
                }
                .padding(EdgeInsets(top: 4, leading: 16, bottom: 8, trailing: 16))
                .background(Color(red: 118.0 / 255.0, green: 176.0 / 255.0, blue: 253.0 / 255.0, opacity: 0.19))
                .cornerRadius(8.0)
                .overlay(
                    RoundedRectangle(cornerRadius: 8.0)
                        .inset(by: 0.5)
                        .stroke(Color(white: 1.0, opacity: 0.08), lineWidth: 1.0)
                )
                .padding(.top, 36)
                
                
                Button(action: { findByIin(iin: iin) }){
                    Text("Войти по ИИН")
                        .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, maxHeight: 52.0)
                        .background(RadialGradient(
                            stops: [
                                Gradient.Stop(color: Color(red: 25.0 / 255.0, green: 137.0 / 255.0, blue: 221.0 / 255.0), location: 0.0),
                                Gradient.Stop(color: Color(red: 0.0, green: 97.0 / 255.0, blue: 225.0 / 255.0), location: 1.0)],
                            center: .center,
                            startRadius: 0,
                            endRadius: 54))
                        .font(.custom("PTRootUI-Bold", size: 17.0))
                        .foregroundColor(Color(white: 1.0))
                        .multilineTextAlignment(.center)
                        .kerning(0.2)
                        .cornerRadius(8.0)
                        .shadow(color: Color(white: 0.0, opacity: 0.12), radius: 4.0, x: 0.0, y: 2.0)
                        .padding(.top, 16)
                    
                }
                
            }
                .padding(EdgeInsets(top: 136, leading: 16, bottom: 0, trailing: 16))
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
            .background(RadialGradient(
                stops: [
                    Gradient.Stop(color: Color(red: 58.0 / 255.0, green: 100.0 / 255.0, blue: 180.0 / 255.0), location: 0.0),
                    Gradient.Stop(color: Color(red: 23.0 / 255.0, green: 72.0 / 255.0, blue: 135.0 / 255.0), location: 1.0)],
                center: .center,
                startRadius: 0,
                endRadius: 306))
        }
}

func findByIin(iin: String){
    print("finding by iin \(iin)")
}
    
#Preview {
    FindByIin()
}

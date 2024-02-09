import SwiftUI

struct ChangePhoneNumber: View {
    
    var body: some View {
        changePhoneNumber(isPhoneExist: true)
    }
}


func changePhoneNumber(isPhoneExist: Bool) -> some View{
    var pageTitle: String = "Изменить номер"
    var pageDescription: String = "Для указаного ИИН уже закреплен номер телефона. Желаете его изменить?"
    var buttonText: String = "Изменить номер телефона"
    
    let employeeName = "Баталгазиев Руслан Владимирович"
    let employeePosition = "Старший бригадир"
    @State var phoneNumber: String = "+7"
    
    if(isPhoneExist){
        pageTitle = "Подтвердите данные"
        pageDescription = "С указанным ИИН найден сотрудник"
        buttonText = "Добавить номер телефона"
    }
    
    return VStack{
        
        VStack(spacing: 0){
            
            Text(pageTitle)
                .font(.custom("PTRootUI-Bold", size: 24.0))
                .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0))
                .multilineTextAlignment(.center)
                .kerning(-0.4)
            
            Text(pageDescription)
                .font(.custom("PTRootUI-Medium", size: 14.0))
                .foregroundColor(Color(red: 207.0 / 255.0, green: 213.0 / 255.0, blue: 220.0 / 255.0))
                .multilineTextAlignment(.center)
                .padding(.top, 16)
            
            Text("Изменить ИИН")
                .font(.custom("PTRootUI-Medium", size: 14.0))
                .foregroundColor(Color(red: 64.0 / 255.0, green: 189.0 / 255.0, blue: 1.0))
                .multilineTextAlignment(.center)
                .padding(.top, 10)
            
            
            VStack(alignment: .leading){
                Rectangle()
                    .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, maxHeight: 1.0)
                    .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0, opacity: 0.12))
                    .padding(.top, 24)
                
                Text(employeeName)
                    .font(.custom("PTRootUI-Bold", size: 18.0))
                    .foregroundColor(Color(white: 1.0))
                    .padding(.top, 11)
                
                Text(employeePosition)
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 207.0 / 255.0, green: 213.0 / 255.0, blue: 220.0 / 255.0))
                    .padding(.top, 3)
                
                Rectangle()
                    .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, maxHeight: 1.0)
                    .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0, opacity: 0.12))
                    .padding(.top, 13)
            }
            
            
            VStack{
                Text("Телефон")
                    .font(.custom("PTRootUI-Medium", size: 13.0))
                    .foregroundColor(Color(white: 235.0 / 255.0, opacity: 0.39))
                    .kerning(0.2)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                TextField("", text: $phoneNumber)
                    .foregroundColor(.white)
                    .font(.custom("PTRootUI-Bold", size: 17.0))
                    .kerning(0.3)                         .frame(height: 24.0, alignment: .center)
                
            }
            .padding(EdgeInsets(top: 4, leading: 16, bottom: 8, trailing: 16))
            .frame(alignment: .leading)
            .background(Color(red: 118.0 / 255.0, green: 176.0 / 255.0, blue: 253.0 / 255.0, opacity: 0.19))
            .cornerRadius(8.0)
            .overlay(
                RoundedRectangle(cornerRadius: 8.0)
                    .inset(by: 0.5)
                    .stroke(Color(white: 1.0, opacity: 0.08), lineWidth: 1.0)
            )
            .padding(.top, 24)
            
            
            Button(action: addPhoneNumber){
                Text(buttonText)
                    .font(.custom("PTRootUI-Bold", size: 17.0))
                    .foregroundColor(Color(white: 1.0))
                    .multilineTextAlignment(.center)
                    .kerning(0.2)
                    .foregroundColor(.clear)
                    .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, maxHeight: 52.0)
                    .background(RadialGradient(
                        stops: [
                            Gradient.Stop(color: Color(red: 25.0 / 255.0, green: 137.0 / 255.0, blue: 221.0 / 255.0), location: 0.0),
                            Gradient.Stop(color: Color(red: 0.0, green: 97.0 / 255.0, blue: 225.0 / 255.0), location: 1.0)],
                        center: .center,
                        startRadius: 0,
                        endRadius: 54))
                    .cornerRadius(8.0)
                    .shadow(color: Color(white: 0.0, opacity: 0.12), radius: 4.0, x: 0.0, y: 2.0)
                    .padding(.top, 16)
            }
            
            
        }
        .padding(EdgeInsets(top: 136, leading: 16, bottom: 0, trailing: 16))
        
        
        
    }
    .frame(maxWidth: /*@START_MENU_TOKEN@*/.infinity/*@END_MENU_TOKEN@*/, maxHeight: .infinity, alignment: .top)
    .background(RadialGradient(
        stops: [
            Gradient.Stop(color: Color(red: 58.0 / 255.0, green: 100.0 / 255.0, blue: 180.0 / 255.0), location: 0.0),
            Gradient.Stop(color: Color(red: 23.0 / 255.0, green: 72.0 / 255.0, blue: 135.0 / 255.0), location: 1.0)],
        center: .center,
        startRadius: 0,
        endRadius: 306))
    
}


func addPhoneNumber(){
    
}


#Preview {
    ChangePhoneNumber()
}

import SwiftUI

struct SendSms: View {
    @State private var codes: [String] = ["", "", "", ""]
    private var phoneNumber: String = "+7 (701) 399 35 38"
    private var seconds = 60
    
    var body: some View {
        
        VStack{
            
            VStack{
                
                Text("Введите код")
                    .font(.custom("PTRootUI-Bold", size: 24.0))
                    .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .kerning(-0.4)
                
                Text("На номер \(phoneNumber) отправлено SMS с кодом авторизации")
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 207.0 / 255.0, green: 213.0 / 255.0, blue: 220.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .padding(.top, 16)
                
                Text("Изменить номер")
                    .padding(EdgeInsets(top: 10, leading: 0, bottom: 0, trailing: 0))
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 64.0 / 255.0, green: 189.0 / 255.0, blue: 1.0))
                
                HStack{
                    ForEach(0..<4){ index in
                        
                        TextField("", text:Binding(
                            get: {
                                self.codes[index]
                            }, set: {
                                self.codes[index] = $0
                            }
                        ), prompt: Text("0")
                            .foregroundColor(Color(red: 0 / 255.0, green: 1.0, blue: 254.0 / 255.0)))
                        .keyboardType(.numberPad)
                        .onChange(of: codes, perform: { _ in
                            if codes[index].count > 1 {
                                codes[index] = String(codes[index].prefix(1))
                            }
                        })
                        .font(.custom("PTRootUI-Bold", size: 45.0))
                        .foregroundColor(Color(red: 254.0 / 255.0, green: 1.0, blue: 254.0 / 255.0))
                        .multilineTextAlignment(.center)
                        .kerning(1.6)
                        .frame(width: 56.0, height: 72.0)
                        .background(Color(red: 118.0 / 255.0, green: 176.0 / 255.0, blue: 253.0 / 255.0, opacity: 0.19))
                        .cornerRadius(7.0)
                        .overlay(
                            RoundedRectangle(cornerRadius: 7.0)
                                .inset(by: 0.5)
                                .stroke(Color(white: 1.0, opacity: 0.08), lineWidth: 1.0)
                        )
                        .padding(EdgeInsets(top: 33, leading: 10, bottom: 0, trailing: 10))
                        
                    }
                }
                
                Text("Переотправить SMS: \(seconds) сек")
                    .font(.custom("PTRootUI-Medium", size: 14.0))
                    .foregroundColor(Color(red: 207.0 / 255.0, green: 213.0 / 255.0, blue: 220.0 / 255.0))
                    .multilineTextAlignment(.center)
                    .padding(.top, 18)
            
//                showContactSupportViews(show: false)
                
                Button(action: sendSmsAgain){
                    Text("Выслать SMS повторно")
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
                        .padding(.top, 32)
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

func sendSmsAgain(){
    print("sending sms")
}

func contactSupport(){
    print("contacting support")
}

func showContactSupportViews(show: Bool) -> any View{
    if show{
        return VStack{
            
            Text("К вам не поступил SMS-код? Свяжитесь с нашей службой поддержки.")
                .font(.custom("PTRootUI-Medium", size: 14.0))
                .foregroundColor(Color(red: 252.0 / 255.0, green: 253.0 / 255.0, blue: 253.0 / 255.0))
                .multilineTextAlignment(.center)
                .padding(.top, 32)
            
            Button(action: contactSupport){
                Text("Обратитесь к службе поддержки")
                    .font(.custom("PTRootUI-Bold", size: 17.0))
                    .foregroundColor(Color(white: 1.0))
                    .multilineTextAlignment(.center)
                    .kerning(0.2)
                    .frame(maxWidth: .infinity, maxHeight: 52.0)
                    .background(Color(white: 1.0, opacity: 0.1))
                    .cornerRadius(8.0)
                    .padding(.top, 24)
            }
        }
    }else {
        return EmptyView()
    }
    
}

#Preview {
    SendSms()
}

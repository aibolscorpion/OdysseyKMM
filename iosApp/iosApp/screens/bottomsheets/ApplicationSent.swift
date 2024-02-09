//
//  ApplicationSent.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

struct ApplicationSent: View {
    var body: some View {
        let phoneNumber: String = "+7 701 399 35 38"
        let title: String = "Заявка отправлена"
        let description: String = "Заявка на изменение номера телефона на \(phoneNumber) успешно отправлена. Обработка займет до 30 минут. После подтверждения Вы получите уведомление на Ваш телефон."
        bottomSheetTemplate(isError: false, title: title, description: description)
            
    }
}

#Preview {
    ApplicationSent()
}

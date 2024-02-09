//
//  ErrorOnAdddingNumber.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

struct ErrorOnAdddingNumber: View {
    var body: some View {
        let title: String = "Произошла ошибка"
        let description: String = "Не удалось добавить номер телефона по указаному ИИН. Попробуйте снова либо обратитесь в службу поддержки."
        bottomSheetTemplate(isError: true, title: title, description: description)
    }
}

#Preview {
    ErrorOnAdddingNumber()
}

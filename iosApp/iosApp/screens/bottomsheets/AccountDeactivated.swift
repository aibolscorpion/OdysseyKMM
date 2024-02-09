//
//  AccountDeactivated.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

struct AccountDeactivated: View {
    
    var body: some View {
        let employeeName: String = "Руслан Владимирович"
        let title: String = "Аккаунт деактивирован"
        let description: String = "Уважаемый \(employeeName) ваш аккаунт был деактивирован вашим работодателем. Если у вас есть вопросы обратитесь в службу поддержки."
        bottomSheetTemplate(isError: true, title: title, description: description)
    }
    
}


#Preview {
    AccountDeactivated()
}

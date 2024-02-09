//
//  ErrorHappened.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

struct ErrorOnLogin: View {
    var body: some View {
        let title: String = "Произошла ошибка"
        let description: String = "При попытке войти в аккаунт, произошла непредвиденная ошибка. Пожалуйста, обратитесь в службу поддержки."
        bottomSheetTemplate(isError: true, title: title, description: description)
    }
}

#Preview {
    ErrorOnLogin()
}

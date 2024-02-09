//
//  EmployeeNotFound.swift
//  iosApp
//
//  Created by Android Developer on 08.02.2024.
//

import SwiftUI

struct EmployeeNotFound: View {
    var body: some View{
        let iin: String = "920911300248"
        let title: String = "Сотрудник не найден"
        let description: String = "Сотрудник с ИИН: \(iin) не найден. Внимательно проверьте ваши данные и попробуйте снова."
        bottomSheetTemplate(isError: false, title: title, description: description)
        
    }
}
#Preview {
    EmployeeNotFound()
}

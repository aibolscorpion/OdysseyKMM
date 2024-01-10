package kz.divtech.odyssey.shared.data.local.data_source.faq

import kz.divtech.odyssey.shared.domain.model.help.faq.Faq

fun List<database.Faq>.toFaqList(): List<Faq>{
    return this.map {
        Faq(
            id = it.id.toInt(),
            answer = it.answer,
            question =  it.question
        )
    }
}
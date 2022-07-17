package com.example.activediet

import java.util.*

data class ExcerciseModel (
    var id: Int = getAudoId(),
    var name: String = "",
    var details: String = "",
    var video: String = ""
)
{
    companion object{
        fun getAudoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }

}

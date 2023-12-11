package com.epaymark.big9.data.model

data class OperatorModel(var image: String?,
                         val title:String?,
                         var isSelecetd:Boolean,
                         val minrecharge: String? ="",
                         val maxrecharge: String? ="",
                         val minlen: String? ="10",
                         val maxlen: String? ="",
                         val opcode: String? =""

)



package com.example.greilyn_p2_ap2.data.remote.dto

import com.squareup.moshi.Json

data class GastosDto(
    @Json(name = "idGasto")
    val gastoId : Int? = null,
    var fecha : String,
    var suplidor : String,
    var concepto : String,
    var nfc : String,
    var itbis : String,
    val monto : Int
)

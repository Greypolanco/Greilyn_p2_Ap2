package com.example.greilyn_p2_ap2.data.remote.dto

import com.squareup.moshi.Json

data class GastosDto(
    @Json(name = "idGasto")
    val gastoId : Int? = null,
    var fecha : String,
    var suplidor : String? = null,
    val idSuplidor : Int,
    var concepto : String,
    var descuento : Int = 0,
    var ncf : String,
    var itbis : String,
    val monto : Int
)

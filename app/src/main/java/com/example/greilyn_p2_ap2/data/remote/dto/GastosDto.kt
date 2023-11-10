package com.example.greilyn_p2_ap2.data.remote.dto

import com.squareup.moshi.Json

data class GastosDto(
    @Json(name = "idGasto")
    val gastoId : Int? = null,
    var fecha : String,
    var suplidor : String? = null,
    val idSuplidor : Int,
    var concepto : String,
    val descuento : Int? = null,
    var ncf : String,
    val itbis : Int? = null,
    val monto : Int
)

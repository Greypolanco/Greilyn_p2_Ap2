package com.example.greilyn_p2_ap2.data

import com.example.greilyn_p2_ap2.data.remote.dto.GastosDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GastosApi {
    @GET("/api/Gastos")
    suspend fun getGastos() : List<GastosDto>

    @GET("/api/Gastos/{id}")
    suspend fun getGastosById (@Path("id") gastosid: Int) : GastosDto

    @POST("/api/Gastos")
    suspend fun postGastos(@Body gastosDto: GastosDto) : Response<GastosDto>

    @DELETE("/api/Gastos/{id}")
    suspend fun deletedGastos(@Path("id") id: Int): Response<GastosDto>
}
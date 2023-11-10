package com.example.greilyn_p2_ap2.data.repository


import com.example.greilyn_p2_ap2.data.GastosApi
import com.example.greilyn_p2_ap2.data.remote.dto.GastosDto
import com.example.greilyn_p2_ap2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GastosRepository @Inject constructor(
    private val api: GastosApi
) {
    fun getGastos(): Flow<Resource<List<GastosDto>>> = flow {
        try {
            emit(Resource.Loading())
            val gasto = api.getGastos()
            emit(Resource.Success(gasto))
        }catch (e: HttpException){
            emit(Resource.Error(e.message ?: "ERROR HTTP GENERAL"))
        }catch (e: IOException){
            emit(Resource.Error(e.message ?: "verifica tu conexion de internet"))
        }
    }

    fun getGastosById(id: Int): Flow<Resource<GastosDto>> = flow{
        try {
            emit(Resource.Loading())

            val gastoById= api.getGastosById(id)

            emit(Resource.Success(gastoById))
        }catch (e: HttpException){
            emit(Resource.Error(e.message ?: "ERROR HTTP GENERAL"))
        }catch (e: IOException){
            emit(Resource.Error(e.message ?: "verifica tu conexion de internet"))
        }
    }

    suspend fun postGastos(gastosDto: GastosDto) = api.postGastos(gastosDto)
    suspend fun deleteGastos(id: Int) : GastosDto?{
        return api.deletedGastos(id).body()
    }

    suspend fun putGasto(id: Int, gasto: GastosDto) = api.putGasto(id, gasto)
}
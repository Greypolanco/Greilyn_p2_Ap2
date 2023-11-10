package com.example.greilyn_p2_ap2.ui.Gastos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greilyn_p2_ap2.data.remote.dto.GastosDto
import com.example.greilyn_p2_ap2.data.repository.GastosRepository
import com.example.greilyn_p2_ap2.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GastosListState(
    val isLoading: Boolean = false,
    val gastos: List<GastosDto> = emptyList(),
    val error : String = ""
)

@HiltViewModel
class GastosViewModel @Inject constructor(
    private val gastosRepository: GastosRepository
): ViewModel(){
    var idsuplidor by mutableStateOf(1)
    var suplidor by mutableStateOf("")
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf(0)
    var descuento by mutableStateOf(1)
    var monto by mutableStateOf(0)

    var fechaInvalida by mutableStateOf(true)
    var suplidorInvalido by mutableStateOf(true)
    var conceptoInvalido by mutableStateOf(true)
    var ncfInvalido by mutableStateOf(true)
    var itbisInvalido by mutableStateOf(true)
    var montoInvalido by mutableStateOf(true)
    var suplidorIdInvalido by mutableStateOf(true)

    private val _uiState = MutableStateFlow(GastosListState())
    val uiState: StateFlow<GastosListState> = _uiState.asStateFlow()

    val gastos : StateFlow<Resource<List<GastosDto>>> = gastosRepository.getGastos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Resource.Loading()
    )

    private val _isMessageShown = MutableSharedFlow<Boolean>()
    val isMessageShownFlow = _isMessageShown.asSharedFlow()

    fun setMessageShown() {
        viewModelScope.launch {
            _isMessageShown.emit(true)
        }
    }

    private fun actualizar(){
        gastosRepository.getGastos().onEach {result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    _uiState.update { it.copy(gastos = result.data ?: emptyList()) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(error = result.message ?: "Error desconocido") }
                }
            }
        }.launchIn(viewModelScope)
    }
    init {
        actualizar()
    }

    fun save(){
        viewModelScope.launch {
            val gastosDto = GastosDto(
                fecha = fecha,
                idSuplidor = idsuplidor,
                suplidor = suplidor,
                concepto = concepto,
                ncf = ncf,
                itbis = itbis,
                descuento = descuento,
                monto = monto
            )
            if(validar()){
                gastosRepository.postGastos(gastosDto)
                limpiar()
                actualizar()
            }
        }
    }
    fun delete(id : Int){
        viewModelScope.launch {
            gastosRepository.deleteGastos(id)
            actualizar()
        }
    }

    fun put(){
        viewModelScope.launch {
            val gastos = GastosDto(
                fecha = fecha,
                idSuplidor = idsuplidor,
                suplidor = suplidor,
                concepto = concepto,
                ncf = ncf,
                itbis = itbis,
                descuento = descuento,
                monto = monto
            )
            gastosRepository.postGastos(gastos)
            limpiar()
            actualizar()
        }
    }

    fun validar(): Boolean{
        if(fecha.isBlank()) {
            fechaInvalida = false
            return fechaInvalida
        }
        if(suplidor.isBlank()){
            suplidorInvalido = false
            return suplidorInvalido
        }
        if(idsuplidor <= 0){
            suplidorIdInvalido = false
            return suplidorIdInvalido
        }
        if(concepto.isBlank()){
            conceptoInvalido = false
            return conceptoInvalido
        }
        if(ncf.isBlank()){
            ncfInvalido = false
            return ncfInvalido
        }
        if(itbis <= 0){
            itbisInvalido = false
            return itbisInvalido
        }
        else if(monto <= 0){
            montoInvalido= false
            return montoInvalido
        }
        else{
            return true
        }
    }

    private fun limpiar(){
        fecha = ""
        suplidor = ""
        idsuplidor = 0
        concepto = ""
        ncf = ""
        itbis = 0
        monto = 0
    }

    val listaSuplidor = listOf(
        "CLARO",
        "ALTICE",
        "CLARO DOMINICANA",
        "ALTICE DOMINICANA",
        "TELEOPERADORA DEL NORDESTE SRL",
        "VIEW COMUNICACIONES SRL"
    )
}


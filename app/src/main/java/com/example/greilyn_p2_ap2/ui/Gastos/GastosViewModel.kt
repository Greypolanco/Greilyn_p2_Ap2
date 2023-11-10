package com.example.greilyn_p2_ap2.ui.Gastos

import androidx.compose.runtime.State
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GastosListState(
    val isLoading: Boolean = false,
    val cliente: List<GastosDto> = emptyList(),
    val error : String = ""
)

@HiltViewModel
class GastosViewModel @Inject constructor(
    private val gastosRepository: GastosRepository
): ViewModel(){
    var idsuplidor by mutableStateOf(0)
    var fecha by mutableStateOf("")
    var concepto by mutableStateOf("")
    var ncf by mutableStateOf("")
    var itbis by mutableStateOf("")
    var monto by mutableStateOf(0)

    var fechaInvalida by mutableStateOf(true)
    var suplidorInvalido by mutableStateOf(true)
    var conceptoInvalido by mutableStateOf(true)
    var ncfInvalido by mutableStateOf(true)
    var itbisInvalido by mutableStateOf(true)
    var montoInvalido by mutableStateOf(true)

    private var _state = mutableStateOf(GastosListState())
    val state: State<GastosListState> = _state

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
    init {
        gastosRepository.getGastos().onEach {result ->
            when(result){
                is Resource.Loading -> {
                    _state.value = GastosListState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = GastosListState(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value = GastosListState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun save(){
        viewModelScope.launch {
            val gastosDto = GastosDto(
                fecha = fecha,
                idSuplidor = idsuplidor,
                concepto = concepto,
                ncf = ncf,
                itbis = itbis,
                monto = monto
            )
            if(validar()){
                gastosRepository.postGastos(gastosDto)
                limpiar()
            }
        }
    }
    fun delete(gastoId : Int, gastosDto: GastosDto){
        viewModelScope.launch {
            gastosRepository.deleteGastos(gastoId,gastosDto)
            limpiar()
        }
    }

    fun validar(): Boolean{
        if(fecha.isBlank()) {
            fechaInvalida = false
            return fechaInvalida
        }
        if(idsuplidor <= 0){
            suplidorInvalido = false
            return suplidorInvalido
        }
        if(concepto.isBlank()){
            conceptoInvalido = false
            return conceptoInvalido
        }
        if(ncf.isBlank()){
            ncfInvalido = false
            return ncfInvalido
        }
        if(itbis.isBlank()){
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

    fun limpiar(){
        fecha = ""
        idsuplidor = 0
        concepto = ""
        ncf = ""
        itbis = ""
        monto = 0
    }
}
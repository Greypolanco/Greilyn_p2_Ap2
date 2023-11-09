package com.example.greilyn_p2_ap2.ui.Gastos

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.greilyn_p2_ap2.data.remote.dto.GastosDto
import com.example.greilyn_p2_ap2.util.Resource
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    viewModel: GastosViewModel = hiltViewModel()
){
    val gastos by viewModel.gastos.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.isMessageShownFlow.collectLatest {
            if (it) {
                snackbarHostState.showSnackbar(
                    message = "Gasto guardado",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val keyBoardControlle = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        //fecha
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = viewModel.fecha, onValueChange = { viewModel.fecha = it },
            label = { Text(text = "Nombres") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (viewModel.fechaInvalida == false) {
            Text(text = "fecha es Requerida", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))

        //suplidor
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = viewModel.suplidor, onValueChange = { viewModel.suplidor = it },
            label = { Text(text = "Suplidor") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (viewModel.suplidorInvalido == false) {
            Text(text = "suplidor es Requerido", color = Color.Red, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        consultaGastos(gastos = gastos)
    }
//    OutlinedButton(onClick = {
//        keyBoardControlle?.hide()
//        if(viewModel.validar()){
//            viewModel.save()
//        }
//    }, modifier = Modifier.fillMaxWidth()) {
//        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Guardar")
//        Text(text = "Guardar")
//    }
//
}
@Composable
fun consultaGastos(gastos: Resource<List<GastosDto>>, viewModel: GastosViewModel = hiltViewModel()){

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(gastos.data ?: emptyList()) { gastos ->
            consultaGastosItem(gastos)
        }
    }

}

@Composable
fun consultaGastosItem(gastos: GastosDto, viewModel: GastosViewModel = hiltViewModel()){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(13.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(13.dp)
        ){
            Row {
                Text(text ="ID:"+ gastos.gastoId)
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = gastos.fecha)
            }
            Text(text = gastos.suplidor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = gastos.concepto)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Column {
                    Text(text = "NCF:"+gastos.ncf)
                    Text(text = "Itbis:" + gastos.itbis)
                }

            }

        }
        Button(
            onClick = {
                gastos.gastoId?.let { viewModel.delete(it, gastos) }
            }
        ) {
            Text(text = "Eliminar")
        }
    }
}
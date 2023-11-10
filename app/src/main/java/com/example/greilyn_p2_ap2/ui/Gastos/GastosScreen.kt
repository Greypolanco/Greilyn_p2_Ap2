package com.example.greilyn_p2_ap2.ui.Gastos



import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
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
            .padding(3.dp)
    ) {
        //fecha
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = viewModel.fecha, onValueChange = { viewModel.fecha = it },
            label = { Text(text = "Fecha") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (viewModel.fechaInvalida == false) {
            Text(text = "Fecha es Requerida", color = Color.Red, fontSize = 12.sp)
        }

        //suplidor
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf("") }
        var textFiledSize by remember { mutableStateOf(Size.Zero) }
        val icon = if (expanded) {
            Icons.Filled.KeyboardArrowUp
        } else {
            Icons.Filled.KeyboardArrowDown
        }
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {
                selectedItem = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                },
            label = { Text(text = "Suplidor") },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) { textFiledSize.width.toDp() }
            )
        ) {
            viewModel.listaSuplidor.forEach { label ->
                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                    selectedItem = label
                    expanded = false
                    viewModel.suplidor = selectedItem
                })
            }
        }
        if (viewModel.suplidorInvalido == false) {
            Text(text = "Suplidor es Requerido", color = Color.Red, fontSize = 12.sp)
        }

        //Concepto
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = viewModel.concepto, onValueChange = { viewModel.concepto = it },
            label = { Text(text = "Concepto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (viewModel.suplidorInvalido == false) {
            Text(text = "Concepto es Requerido", color = Color.Red, fontSize = 12.sp)
        }
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(1.dp)
            ){
                //ncf
                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    value = viewModel.ncf, onValueChange = { viewModel.ncf = it },
                    label = { Text(text = "NCF") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                if (viewModel.ncfInvalido == false) {
                    Text(text = "El NCF es Requerido", color = Color.Red, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(1.dp)
            ){
                //Itbis
                OutlinedTextField(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    value = viewModel.itbis.toString(), onValueChange = { viewModel.itbis = it.toIntOrNull() ?: 0 },
                    label = { Text(text = "ITBIS") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                if (viewModel.itbisInvalido == false) {
                    Text(text = "El ITBIS es Requerido", color = Color.Red, fontSize = 12.sp)
                }
            }
        }

        //monto
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = viewModel.monto.toString(),
            onValueChange = { viewModel.monto = it.toIntOrNull() ?: 0 },
            label = { Text(text = "Monto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            )
        )
        if (viewModel.montoInvalido == false) {
            Text(text = "El Monto es Requerido", color = Color.Red, fontSize = 12.sp)
        }

        Button(onClick = {
            keyBoardControlle?.hide()
            if(viewModel.validar()){
                viewModel.save()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Guardar")
            Text(text = "Guardar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        consultaGastos(gastos = gastos)
    }
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
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(text ="ID:"+ gastos.gastoId)

                Text(text = gastos.fecha,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Text(text = gastos.suplidor ?: "" , fontWeight = FontWeight.Bold,fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = gastos.concepto,  overflow = TextOverflow.Ellipsis, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(text = "NCF:"+gastos.ncf)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ){
                    Text(text = "Itbis:" + gastos.itbis)
                    Text(text = "$" + gastos.monto,
                        fontSize = 20.sp,
                        textAlign = TextAlign.End,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth())
                }

            }
        }
        Divider(Modifier.padding(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(onClick = {  },
                modifier = Modifier
                    .width(160.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black)
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(imageVector = Icons.Default.Edit, contentDescription ="Modificar")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Modificar")
                }

            }
            Spacer(modifier = Modifier.width(40.dp))
            OutlinedButton(
                onClick = {
                    gastos.gastoId?.let { viewModel.delete(it) }
                },
                modifier = Modifier
                    .width(150.dp)
                    .padding(8.dp),
                border = BorderStroke(2.dp, Color.Red),
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Eliminar", tint = Color.Red)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Eliminar", color = Color.Red)
                }
            }
        }

    }
}
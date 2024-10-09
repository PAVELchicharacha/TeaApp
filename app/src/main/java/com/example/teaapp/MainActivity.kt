package com.example.teaapp

import android.health.connect.datatypes.units.Volume
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teaapp.ui.theme.TeaAppTheme


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    var isRegistered by remember { mutableStateOf(false) }
//    var isCreated by remember { mutableStateOf(false) }
    if (isRegistered) {
        MainScreen()
    } else {
        RegistrationScreen(onRegister = { isRegistered = true })
    }

//    if (isCreated) {
//        EnterAccount(onCreation = { isRegistered = true })
//    }
}


//это регистрация акаунта
@Composable
fun RegistrationScreen(onRegister: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Логин") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Здесь должна быть логика валидации
            onRegister() // Переход на основной экран
        }) {
            Text("Войти в аккаунт")
        }
        Button(modifier = Modifier.padding(50.dp), onClick = {
            // Здесь должна быть логика валидации
            onRegister() // Переход на основной экран
        }) {
            Text("создать аккаунт и войти")
        }
    }
}

data class Tea(
    val name: String,
    val volume: String,
    val temperature: String
)

@Composable
fun TeaList(teaItems: List<Tea>) {
    LazyColumn() {
        items(teaItems) { tea ->
            TeaItem(tea)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewTeaList() {
    val teas = listOf(
        Tea("Чай зеленый", "250 мл", "80°C"),
        Tea("Чай черный", "200 мл", "90°C"),
        Tea("Чай белый", "300 мл", "85°C")
    )
    TeaList(teaItems = teas)
}

//все активности будут тут
@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            var route by remember { mutableStateOf("home") }
            Box(modifier = Modifier.weight(1f).padding(16.dp)){
                when(route){
                    "AddTea"-> AddTea()
                    "Favorite"-> TeaItem(tea = Tea("Чай зеленый", "250 мл", "80°C",))
                    "Dinary"-> Dinary()
                    "Profile"->Profile()
                }
            }
            Row (modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(text = "AddTea", modifier = Modifier.clickable { route="AddTea" })
                Text(text = "Favorite", modifier = Modifier.clickable { route="Favorite" })
                Text(text = "orders", modifier = Modifier.clickable { route="Dinary" })
                Text(text = "Profile", modifier = Modifier.clickable { route="Profile" })
            }
        }
    }
}

@Composable
fun AddTea(){
    Text("Add Tea")
}
@Composable
fun TeaItem(tea: Tea) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Название: ${tea.name}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Объем: ${tea.volume}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Температура: ${tea.temperature}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun Dinary(){
    Text("Dinary")
}
@Composable
fun Profile(){
    Text("profile")
}

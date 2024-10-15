package com.example.teaapp

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.renderscript.ScriptGroup.Input
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = Firebase.auth
        setContent {
            MyApp(auth)
        }
    }
}

@Composable
fun MyApp(auth: FirebaseAuth) {
    var isRegistered by remember { mutableStateOf(false) }
    if (isRegistered) {
        MainScreen(auth)
    } else {
        RegistrationScreen(onRegister = { isRegistered = true })
    }
}

//это регистрация акаунта
@Composable
fun RegistrationScreen(onRegister: () -> Unit) {
    val auth = Firebase.auth

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    Log.d("mylog", "user email:${auth.currentUser?.email}")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = email.value, onValueChange = {
            email.value = it
        })
        Spacer(modifier = Modifier.height(10.dp))
        TextField(value = password.value, onValueChange = {
            password.value = it
        })
        Button(onClick = {
            SignIn(auth, email.value, password.value)
            onRegister()
        }) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier, onClick = {
            SignUp(auth, email.value, password.value)
            onRegister()
        }) {
            Text(text = "Sign Up")
        }
    }
}

private fun SignOut(auth: FirebaseAuth) {
    auth.signOut()

}

private fun DeleteAccount(auth: FirebaseAuth) {
    auth.currentUser?.delete()!!
    Log.d("myLog", "delete is successfull")

}

private fun SignIn(auth: FirebaseAuth, email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("myLog", "Sign up successfull")
        } else {
            Log.d("myLog", "Sign up failure")
        }
    }
}

private fun SignUp(auth: FirebaseAuth, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("myLog", "Sign up successfull")
        } else {
            Log.d("myLog", "Sign up failure")
        }
    }
}

data class Tea(
    val name: String = "",
    val volume: String = "",
    val temperature: String = ""
)

@Composable
fun TeaList(teaItems: List<Tea>) {
    LazyColumn() {
        items(teaItems) { tea ->
            TeaItem(tea)
        }
    }
}

//данные для чая
@Composable
fun PreviewTeaList() {
    val teas = listOf(
        Tea("Чай зеленый", "250 мл", "80°C"),
        Tea("Чай черный", "200 мл", "90°C"),
        Tea("Чай белый", "300 мл", "80°C"),
        Tea("Чай белый", "300 мл", "99°C"),
        Tea("Чай афро", "300 мл", "14°C"),
        Tea("Чай гойда", "300 мл", "1337°C"),
        Tea("Чай белый", "300 мл", "81°C"),
        Tea("Чай белый", "300 мл", "85°C"),
    )
    TeaList(teaItems = teas)
}

//все переходы между "экранами"
@Composable
fun MainScreen(auth: FirebaseAuth) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            var route by remember { mutableStateOf("home") }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                when (route) {
                    "AddTea" -> lzcm()
                    "Favorite" -> PreviewTeaList()
//                    "Dinary" ->
                    "Profile" -> Profile(auth)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(text = "AddTea", modifier = Modifier.clickable { route = "AddTea" })
                Text(text = "Favorite", modifier = Modifier.clickable { route = "Favorite" })
                Text(text = "orders", modifier = Modifier.clickable { route = "Dinary" })
                Text(text = "Profile", modifier = Modifier.clickable { route = "Profile" })
            }
        }
    }
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

//профиль и выбор фото с телефона
@Composable
fun Profile(auth: FirebaseAuth) {

    var imgUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imgUri = uri
        }
    Spacer(modifier = Modifier.height(12.dp))

    Button(onClick = { launcher.launch("image/*") }) {
        Text(text = "pick image")
    }
    Button(modifier = Modifier.padding(vertical = 50.dp), onClick = { DeleteAccount(auth) }) {
        Text(text = "delete account")
    }
    Button(modifier = Modifier.padding(vertical = 100.dp), onClick = { SignOut(auth) }) {
        Text(text = "SignOut account")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        imgUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val sourse = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(sourse)
            }
            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(20.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun lzcm() {

    val fs = Firebase.firestore
    val list = remember {
        mutableStateOf(emptyList<Tea>())
    }
    fs.collection("Tea").addSnapshotListener { snapShot, exception ->
        list.value = snapShot?.toObjects(Tea::class.java) ?: emptyList()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.75f)
        ) {
            items(list.value) { tea ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                ) {
                    Text(
                        text = tea.name, modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth()
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        var name by remember { mutableStateOf("") }
        var volume by remember { mutableStateOf("") }
        var temperature by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            TextField( modifier = Modifier.width(80.dp),
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            TextField(modifier = Modifier.width(90.dp),
                value = volume,
                onValueChange = { volume = it },
                label = { Text("Volume") }
            )
            TextField(modifier = Modifier.width(90.dp),
                value = temperature,
                onValueChange = { temperature = it },
                label = { Text("Temp") }
            )
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), onClick = {
            fs.collection("Tea").document().set(
                Tea(
                    name,
                    volume,
                    temperature
                )
            )
        })
        {
            Text(text = "add tea")
        }
    }
}
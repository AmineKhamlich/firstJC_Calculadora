// Defineixo el paquet on viu la meva app
package com.example.firstjc

// Importo totes les eines que necessitaré
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstjc.ui.theme.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.math.RoundingMode

// Aquesta és la porta d'entrada de la meva app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplico el tema visual perquè tot es vegi conjuntat
            FirstJCTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    // Aquí guardo el que es veu a la pantalla. Faig servir 'remember' perquè no s'oblidi del número
    // si l'app es torna a dibuixar, i poso un "0" per començar net.
    var estat by remember { mutableStateOf("0") }

    // El Scaffold és com el "xassís" de la meva pantalla
    Scaffold { innerPadding -> // El Scaffold em diu: "t'he guardat aquest espai perquè res quedi tapat"
        // Organitzo tot un sota l'altre amb una Columna.
        Column(
            modifier = Modifier
                .padding(innerPadding) // Aplico aquest espai de seguretat a la meva columna
                .fillMaxSize() // Vull que ocupi tot el mòbil
                .background(Color.Black)
        ) {
            // Aquí dibuixo el títol. Li poso un fons gris i lletra blanca perquè sembli un cartell
            Text(
                text = "CALCULADORA\nJETPACK COMPOSE",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, start = 24.dp, end = 24.dp)
                    .background(color = Color.DarkGray,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                    .padding(12.dp),
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(100.dp))

            // Aquest Box és la "pantalla" on surten els números
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .weight(1f) // Li dono aquest 'weight' perquè "empenyi" els botons cap a baix
                    .border(width = 1.dp, color = DarkGrey,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
                contentAlignment = androidx.compose.ui.Alignment.BottomEnd // Vull els números a baix a la dreta
            ) {
                Text(
                    text = estat,
                    color = Color.White,
                    fontSize = 64.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                    modifier = Modifier.padding(16.dp) // Perquè el número no toqui la vora de la caixa
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- A partir d'aquí munto les files de botons ---

            // FILA 1: El 7, 8, 9 i la divisió
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth()) {
                // Faig servir el meu component propi 'BotoNumeros' per no repetir codi
                BotoNumeros("7", Modifier.weight(1f)) { estat = afegirNumero(estat, "7") }
                BotoNumeros("8", Modifier.weight(1f)) { estat = afegirNumero(estat, "8") }
                BotoNumeros("9", Modifier.weight(1f)) { estat = afegirNumero(estat, "9") }
                BotoNumeros("/", Modifier.weight(1f), colorFons = Orange)
                { estat = afegirSigne(estat, "/") }
            }

            // FILA 2: El 4, 5, 6 i la multiplicació
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth()) {
                BotoNumeros("4", Modifier.weight(1f)) { estat = afegirNumero(estat, "4") }
                BotoNumeros("5", Modifier.weight(1f)) { estat = afegirNumero(estat, "5") }
                BotoNumeros("6", Modifier.weight(1f)) { estat = afegirNumero(estat, "6") }
                // Passo una "x" visual però la funció rep un "*" perquè la llibreria ho entengui
                BotoNumeros("x", Modifier.weight(1f), colorFons = Orange)
                { estat = afegirSigne(estat, "*") }
            }

            // FILA 3: El 1, 2, 3 i la resta
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth()) {
                BotoNumeros("1", Modifier.weight(1f)) { estat = afegirNumero(estat, "1") }
                BotoNumeros("2", Modifier.weight(1f)) { estat = afegirNumero(estat, "2") }
                BotoNumeros("3", Modifier.weight(1f)) { estat = afegirNumero(estat, "3") }
                BotoNumeros("-", Modifier.weight(1f), colorFons = Orange)
                { estat = afegirSigne(estat, "-") }
            }

            // FILA 4: El 0 (més ample), el Clear i la suma
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth()) {
                BotoNumeros("0", Modifier.weight(2f)) { estat = afegirNumero(estat, "0") }
                BotoNumeros("C", Modifier.weight(1f), colorFons = Red) { estat = "0" }
                BotoNumeros("+", Modifier.weight(1f), colorFons = Orange)
                { estat = afegirSigne(estat, "+") }
            }

            // FILA 5: L'igual, borrar un enrere i el punt
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).fillMaxWidth()) {
                BotoNumeros("=", Modifier.weight(2f), colorFons = Grey) { if (estat.isNotEmpty()) estat = calcularResultat(estat) }
                BotoNumeros("<-", Modifier.weight(1f), colorFons = Red) {
                    // Si només queda un número, torno al "0", si no, borro l'últim
                    estat = if (estat.length <= 1) "0" else estat.dropLast(1)
                }
                BotoNumeros(".", Modifier.weight(1f), colorFons = Orange)
                { estat = afegirSigne(estat, ".") }
            }
        }
    }
}

// Aquesta funció és el meu "cervell" matemàtic
fun calcularResultat(lExpressio: String): String {
    return try {
        // Crido la llibreria externa per resoldre el String com una operació real
        val e = ExpressionBuilder(lExpressio).build()
        val result = e.evaluate()

        // Si el resultat és enter (ex: 5.0), vull que es vegi com "5" per neteja visual.
        if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            // Si hi ha decimals, faig servir BigDecimal per evitar que surtin coses com 0.9999999
            // Limito a 5 decimals i tallo els zeros sobrants del final.
            BigDecimal(result).setScale(5, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        }
    } catch (e: Exception) {
        "Error" // Si l'usuari fa una bogeria, poso Error
    }
}

// Aquí controlo com s'escriuen els números per evitar el "07"
fun afegirNumero(actual: String, nouNumero: String): String {
    return if (actual == "0") nouNumero else actual + nouNumero
}

// Aquí vigilo que no em posin dos signes seguits (ex: "++")
fun afegirSigne(actual: String, nouSigne: String): String {
    if (actual.isEmpty()) return actual
    val ultim = actual.lastOrNull()
    val esOperador = ultim in listOf('+', '-', '*', '/', '.')

    return if (esOperador) {
        // Si ja hi ha un signe, el canvio pel nou
        actual.dropLast(1) + nouSigne
    } else {
        actual + nouSigne
    }
}

// Aquesta és la meva fàbrica de botons, per tenir-los tots iguals de cop
@Composable
fun BotoNumeros(text: String, modifier: Modifier = Modifier, colorFons: Color = Color.DarkGray, colorText: Color = Color.White, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp), // Cantos una mica rodons
        colors = ButtonDefaults.buttonColors(containerColor = colorFons, contentColor = colorText)
    ) {
        Text(text = text, fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirstJCTheme {
        Greeting()
    }
}
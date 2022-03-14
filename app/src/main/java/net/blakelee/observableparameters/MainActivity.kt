package net.blakelee.observableparameters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.ProvideWindowInsets
import net.blakelee.observableparameters.ui.theme.ObservableParametersTheme

val green = Color(0xFFB4FFB4)
val red = Color(0xFFFFB4B4)

@ExperimentalAnimatedInsets
class MainActivity : ComponentActivity() {

    val vm: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ObservableParametersTheme {
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    MainUI(vm)
                }
            }
        }
    }
}

@Composable
fun MainUI(vm: MyViewModel) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Column(Modifier.padding(horizontal = 8.dp)) {
            Button(onClick = { vm.toggleSearchParameters() }) {
                Text("Toggle Search Parameters")
            }
            Button(onClick = { vm.addRegion() }) {
                Text("Add Region")
            }
            Text("Performing search: ${vm.performSearch}")
            Text(vm.searchParameters::class.simpleName.orEmpty())
            Text("Search Query Parameters: " + vm.searchQueryParameters.entries.joinToString { "${it.key.name}:${it.value}" })


            Text("Regions")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .graphicsLayer(clip = false)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            val regions = vm.regions.map { it.name }
            regions.map { Pill(it, Color(0xFF80C0FF), Modifier.padding(end = 8.dp)) }
        }

        Column(Modifier.padding(horizontal = 8.dp)) {
            Spacer(modifier = Modifier.size(8.dp))

            AddRemoveList(vm.parametersToAdd, vm.parametersToRemove)

            Spacer(modifier = Modifier.size(8.dp))

            Text("Add SearchQueryParameter")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .graphicsLayer(clip = false)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            listOf(uipt, pool, sold).forEachIndexed { index, sqp ->
                Pill(
                    sqp.name, green, Modifier.padding(end = 8.dp)
                ) { vm.add(sqp, sqp.name) }
            }
        }

        Column(Modifier.padding(horizontal = 8.dp)) {
            space()
            Text("Remove SearchQueryParameter")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .graphicsLayer(clip = false)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
        ) {
            listOf(uipt, pool, sold).forEachIndexed { index, sqp ->
                Pill(
                    sqp.name,
                    red,
                    Modifier.padding(end = 8.dp)
                ) { vm.remove(sqp) }
            }
        }

        Column(Modifier.padding(horizontal = 8.dp)) {
            Button(onClick = { vm.commit() }) {
                Text("Commit")
            }
        }
    }

}

@Composable
fun AddRemoveList(
    add: Map<SearchQueryParameter<Any?>, Any?>,
    remove: List<SearchQueryParameter<Any?>>
) {

    Row(Modifier.wrapContentHeight()) {
        Column(
            Modifier
                .weight(0.5f)
                .background(green, RoundedCornerShape(4.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Parameters to add")
            add.forEach { Text("${it.key.name} ${it.value}") }
        }

        space()

        Column(
            Modifier
                .weight(0.5f)
                .background(red, RoundedCornerShape(4.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Parameters to remove")
            remove.forEach {
                Text(it.name)
            }
        }
    }
}

val space: @Composable () -> Unit = {
    Divider(
        Modifier
            .width(8.dp)
            .fillMaxHeight(),
        color = Color.Transparent
    )
}

@Composable
fun Pill(value: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Text(
        value,
        modifier = modifier
            .background(color, CircleShape)
            .border(1.dp, Color.Black.copy(alpha = 0.2f), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 16.dp)
    )
}

@Preview
@Composable
fun Test() {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Row(
            Modifier.horizontalScroll(rememberScrollState())
        ) {
            (0..5).forEach {
                Pill("Test Pill", Color(0xFF4f2dc4), Modifier.padding(end = 8.dp))
            }
        }
    }
}
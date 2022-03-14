package net.blakelee.observableparameters

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest

class MyViewModel : ViewModel() {

    private val repository = MyRepository() // No DI, deal with it

    var searchParameters: SearchParameters by repository::searchParameters

    var performSearch by mutableStateOf(false)

    val regions by derivedStateOf { searchParameters.regions }

    val searchQueryParameters by derivedStateOf { searchParameters.searchQueryParameters }

    val parametersToAdd: SnapshotStateMap<SearchQueryParameter<Any?>, Any?> = mutableStateMapOf()
    val parametersToRemove: SnapshotStateList<SearchQueryParameter<Any?>> = mutableStateListOf()

    init {
        snapshotFlow { Triple(searchParameters, regions.toList(), searchQueryParameters.toMap()) }
            .mapLatest { performSearch() }
            .launchIn(viewModelScope)
    }

    private suspend fun performSearch() {
        performSearch = true
        delay(1000) // This simulates us doing a search
        performSearch = false
    }

    fun toggleSearchParameters() = repository.toggleSearchParameters()

    fun addRegion() = repository.addRegion()

    fun <T: Any?> add(searchQueryParameter: SearchQueryParameter<T>, value: T) {
        parametersToAdd[searchQueryParameter as SearchQueryParameter<Any?>] = value
    }

    fun <T : Any?> remove(searchQueryParameter: SearchQueryParameter<T>) {
        parametersToRemove.add(searchQueryParameter as SearchQueryParameter<Any?>)
    }

    fun commit() {
        val add = this.parametersToAdd
        val remove = this.parametersToRemove
        repository.commit {
            add.forEach { add(it.key, it.value) }
            remove.forEach { remove(it) }
        }
        parametersToAdd.clear()
        parametersToRemove.clear()
    }
}
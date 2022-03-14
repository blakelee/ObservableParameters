package net.blakelee.observableparameters

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList

interface SearchParameters {
    var searchQueryParameters: Map<SearchQueryParameter<*>, Any?>
    val regions: List<Region>
    fun addRegion(region: Region)
}

class StateSearchParameters : SearchParameters {
    override val regions: List<Region> = mutableStateListOf()
    override var searchQueryParameters: Map<SearchQueryParameter<*>, Any?> by mutableStateOf(
        mutableMapOf()
    )

    override fun addRegion(region: Region) {
        (regions as SnapshotStateList).add(region)
    }
}

fun SearchParameters.commit(parameters: SearchParametersBuilder.() -> Unit) {
    val (add, remove) = SearchParametersBuilder().apply(parameters).build()

    val newParameters = searchQueryParameters.toMutableMap()
    add.forEach { newParameters[it.key] = it.value }
    remove.forEach { newParameters.remove(it) }

    searchQueryParameters = newParameters
}

class SearchParametersBuilder {
    val parametersToAdd = mutableMapOf<SearchQueryParameter<Any?>, Any?>()
    val parametersToRemove = mutableListOf<SearchQueryParameter<Any?>>()

    fun <T : Any?> add(searchQueryParameter: SearchQueryParameter<T>, value: T) {
        parametersToAdd[searchQueryParameter as SearchQueryParameter<Any?>] = value
    }

    fun <T : Any?> remove(searchQueryParameter: SearchQueryParameter<T>) {
        parametersToRemove.add(searchQueryParameter as SearchQueryParameter<Any?>)
    }

    fun build(): Pair<Map<SearchQueryParameter<Any?>, Any?>, List<SearchQueryParameter<Any?>>> {
        return parametersToAdd to parametersToRemove
    }
}
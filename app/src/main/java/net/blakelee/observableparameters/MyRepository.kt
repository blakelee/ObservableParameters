package net.blakelee.observableparameters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MyRepository {

    private var brokerageSearchParameters = BrokerageSearchParameters()
    private var rentalSearchParameters = RentalSearchParameters()

    var searchParameters: SearchParameters by mutableStateOf(brokerageSearchParameters)

    fun toggleSearchParameters() {
        searchParameters = if (searchParameters is BrokerageSearchParameters)
            rentalSearchParameters
        else {
            brokerageSearchParameters
        }
    }

    fun commit(parameters: SearchParametersBuilder.() -> Unit) {
        val (add, remove) = SearchParametersBuilder().apply(parameters).build()
        onCommitRentalSearchParameters(add, remove)
        onCommitBrokerageSearchParameters(add, remove)
    }

    fun addRegion() {
        brokerageSearchParameters.addRegion(Region(("Region ${brokerageSearchParameters.regions.size + 1}")))
        rentalSearchParameters.addRegion(Region(("Region ${rentalSearchParameters.regions.size + 1}")))
    }

    private fun newSearchQueryParameters(
        searchParameters: SearchParameters,
        add: Map<SearchQueryParameter<Any?>, Any?>,
        addFilter: (SearchType) -> Boolean,
        remove: List<SearchQueryParameter<Any?>>,
    ) {
        val newParameters = searchParameters.searchQueryParameters.toMutableMap()
        add.filter { addFilter(it.key.searchType) }.forEach { newParameters[it.key] = it.value }
        remove.forEach { newParameters.remove(it) }
        searchParameters.searchQueryParameters = newParameters
    }

    private fun onCommitRentalSearchParameters(
        add: Map<SearchQueryParameter<Any?>, Any?>,
        remove: List<SearchQueryParameter<Any?>>
    ) {
        newSearchQueryParameters(
            rentalSearchParameters,
            add,
            { it == SearchType.Rentals || it == SearchType.Both },
            remove,
        )
    }

    private fun onCommitBrokerageSearchParameters(
        add: Map<SearchQueryParameter<Any?>, Any?>,
        remove: List<SearchQueryParameter<Any?>>
    ) {
        newSearchQueryParameters(
            brokerageSearchParameters,
            add,
            { it == SearchType.Brokerage || it == SearchType.Both },
            remove,
        )
    }
}
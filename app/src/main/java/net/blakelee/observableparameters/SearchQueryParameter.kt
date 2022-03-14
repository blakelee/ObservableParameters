package net.blakelee.observableparameters

interface SearchQueryParameter<T> {
    val name: String
    val searchType: SearchType
}

val uipt = object : SearchQueryParameter<String> {
    override val name: String = "uipt"
    override val searchType: SearchType = SearchType.Both
}

val pool = object : SearchQueryParameter<String> {
    override val name: String = "pool"
    override val searchType: SearchType = SearchType.Rentals
}

val sold = object : SearchQueryParameter<String> {
    override val name: String = "sold"
    override val searchType: SearchType = SearchType.Brokerage
}

enum class SearchType {
    Brokerage,
    Rentals,
    Both
}
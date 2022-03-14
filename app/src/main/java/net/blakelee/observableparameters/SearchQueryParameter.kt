package net.blakelee.observableparameters

interface SearchQueryParameter<T> {
    val name: String
}

val uipt = object : SearchQueryParameter<String> {
    override val name: String = "uipt"
}

val pool = object : SearchQueryParameter<String> {
    override val name: String = "pool"
}

val sold = object : SearchQueryParameter<String> {
    override val name: String = "sold"
}
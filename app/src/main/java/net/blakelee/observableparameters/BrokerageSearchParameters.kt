package net.blakelee.observableparameters

class BrokerageSearchParameters : SearchParameters by StateSearchParameters() {
    init {
        addRegion(Region("Region 1"))
    }
}
package net.blakelee.observableparameters

class RentalSearchParameters : SearchParameters by StateSearchParameters() {
    init {
        addRegion(Region("Region 1"))
    }
}
package com.example.travelapp.composable

data class LocationObject (
    var Name: String?,
    var Categories: Map<String, Category>?
) {
    constructor() : this(null, null)
}

data class Category (
    var text: String?,
    var images: Map<String, String>?
    ) {
    constructor() : this(null, null)
}
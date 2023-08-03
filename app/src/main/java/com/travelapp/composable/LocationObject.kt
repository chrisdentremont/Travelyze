package com.travelapp.composable

/**
 * An object to store location information to display in the application.
 *
 * @param Name Name of the location
 * @param Flag An image URL of the location's flag or emblem
 * @param Description An introductory paragraph about the location
 * @param Categories A map containing category names linked to their respective [Category]
 */
data class LocationObject(
    var Name: String?,
    var Flag: String?,
    var Description: String?,
    var Categories: Map<String, Category>?
) {
    constructor() : this(null, null, null, null)
}

/**
 * An object to store data about a specific category of information relating to a [LocationObject]
 *
 * @param text The paragraph to display when examining the category
 * @param images A map containing image URLS to display in the body linked to their
 * respective captions to display alongside each other
 */
data class Category(
    var text: String?,
    var images: Map<String, String>?
) {
    constructor() : this(null, null)
}
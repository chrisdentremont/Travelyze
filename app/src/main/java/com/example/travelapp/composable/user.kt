package com.example.travelapp.composable


data class AccountInfo(
    var firstName: String,
    var lastName: String,
    var userName: String,
    var emailAddress: String,
)

data class AccountData (
    var favoriteLocations: MutableList<String>,
    var friendsList: MutableList<String>
)

data class TravelyzeUser(
    var info: AccountInfo?,
    var data: AccountData?
) {
    constructor() : this(null, null)
}
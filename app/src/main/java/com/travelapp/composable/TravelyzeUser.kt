package com.travelapp.composable


data class AccountInfo(
    var firstName: String?,
    var lastName: String?,
    var userName: String?,
    var emailAddress: String?,
) {
    constructor() : this(null, null, null, null)
}

data class AccountData(
    var favoriteLocations: MutableList<String>?,
    var friendsList: MutableList<String>?
) {
    constructor() : this(null, null)
}

data class TravelyzeUser(
    var info: AccountInfo?,
    var data: AccountData?
) {
    constructor() : this(null, null)
}
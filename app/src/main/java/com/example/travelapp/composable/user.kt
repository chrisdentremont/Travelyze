package com.example.travelapp.composable


data class AccountInfo(
    var firstName: String = "",
    var lastName: String = "",
    var userName: String = "",
    var emailAddress: String = "",
)

data class AccountData (
    var favoriteLocations: MutableList<String> = mutableListOf(),
    var friendsList: MutableList<String> = mutableListOf()
)

data class TravelyzeUser(
    var info: AccountInfo? = AccountInfo(),
    var data: AccountData? = AccountData()
) {
//    constructor() : this(
//        AccountInfo(
//            firstName = "",
//            lastName = "",
//            userName = "",
//            emailAddress = ""
//        ),
//        AccountData(
//            favoriteLocations = mutableListOf(),
//            friendsList = mutableListOf()
//        )
//    )
}
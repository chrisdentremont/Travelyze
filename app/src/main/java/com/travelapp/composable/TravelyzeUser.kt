package com.travelapp.composable

/**
 * An object to store a user's personal information
 *
 * @param firstName The user's first name
 * @param lastName The user's last name
 * @param userName The user's username
 * @param emailAddress The user's email address
 */
data class AccountInfo(
    var firstName: String?,
    var lastName: String?,
    var userName: String?,
    var emailAddress: String?,
) {
    constructor() : this(null, null, null, null)
}

/**
 * An object to store a user's application data
 *
 * @param favoriteLocations A list of the names of locations a user has saved
 * @param friendsList A list of the UIDs of a user's added friends (Saving their UIDs makes it easier to find that user in the database)
 */
data class AccountData(
    var favoriteLocations: MutableList<String>?,
    var friendsList: MutableList<String>?
) {
    constructor() : this(null, null)
}

/**
 * An object to store a user's friend requests, whether they are receiving or sending the requests
 *
 * @param incomingFriendRequests A list of the UIDs of other users who wish to be friends with the user
 * @param outgoingFriendRequests A list of the UIDs of other users who the user wishes to be friends with
 *
 * (Saving their UIDs makes it easier to find that user in the database)
 */
data class AccountRequests(
    var incomingFriendRequests: MutableList<String>?,
    var outgoingFriendRequests: MutableList<String>?
) {
    constructor() : this(null, null)
}

/**
 * The main user object which contains all information relating to a user,
 * such as [AccountInfo], [AccountData], and [AccountRequests]
 *
 * @param info The personal information related to a user
 * @param data The application data related to a user
 * @param requests The social requests data related to a user
 */
data class TravelyzeUser(
    var info: AccountInfo?,
    var data: AccountData?,
    var requests: AccountRequests?
) {
    constructor() : this(null, null, null)
}
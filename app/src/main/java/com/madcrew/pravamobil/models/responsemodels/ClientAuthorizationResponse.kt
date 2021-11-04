package com.madcrew.pravamobil.models.responsemodels

import com.google.gson.annotations.SerializedName

data class ClientAuthorizationResponse(
    var client: ClientAuth,
    var status: String
)

data class ClientAuth(
    var id: String,
    @SerializedName("lastName")
    var secondName: String,
    @SerializedName("name")
    var firstName: String,
    @SerializedName("patronymic")
    var thirdName: String,
    var progress: String
)
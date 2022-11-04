package com.example.assesmentapp.capture.model

import com.google.gson.annotations.SerializedName


data class Data(

    @SerializedName("record_id")
    var recordId: String? = null,

    @SerializedName("reference_id")
    var referenceId: String? = null

)

data class UploadImageResponse(

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("data")
    var data: Data? = Data()

)
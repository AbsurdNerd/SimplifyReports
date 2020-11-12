package project.absurdnerds.simplify.data.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
class ProfilePutRequest(

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("token")
    var token: String? = null

) : Parcelable
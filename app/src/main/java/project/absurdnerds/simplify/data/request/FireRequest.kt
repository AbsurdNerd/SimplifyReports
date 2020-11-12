package project.absurdnerds.simplify.data.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
class FireRequest(

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("user")
    var user: String? = null

) : Parcelable
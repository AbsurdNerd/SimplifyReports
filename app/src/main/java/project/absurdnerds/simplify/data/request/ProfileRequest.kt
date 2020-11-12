package project.absurdnerds.simplify.data.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
class ProfileRequest(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("gender")
    var gender: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("permanent_address")
    var permanentAddress: String? = null,

    @SerializedName("token")
    var token: String? = null

) : Parcelable
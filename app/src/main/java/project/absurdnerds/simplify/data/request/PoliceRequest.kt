package project.absurdnerds.simplify.data.request


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
class PoliceRequest(

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("problem")
    var problem: String? = null,

    @SerializedName("problem_description")
    var problemDescription: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("do_notify")
    var doNotify: Boolean? = null,

    @SerializedName("user")
    var user: String? = null

) : Parcelable
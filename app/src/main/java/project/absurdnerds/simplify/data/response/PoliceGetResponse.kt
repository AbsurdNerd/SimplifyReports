package project.absurdnerds.simplify.data.response


import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

class PoliceGetResponse : ArrayList<PoliceGetResponse.PoliceGetResponseItem>(){

    @Parcelize
    class PoliceGetResponseItem(

        @SerializedName("id")
        var id: Int? = null,

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

        @SerializedName("created")
        var created: String? = null,

        @SerializedName("user")
        var user: String? = null

    ) : Parcelable
}
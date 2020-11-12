package project.absurdnerds.simplify.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

class FireGetResponse : ArrayList<FireGetResponse.FireGetResponseItem>(){

    @Parcelize
    class FireGetResponseItem(

        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("location")
        var location: String? = null,

        @SerializedName("address")
        var address: String? = null,

        @SerializedName("created")
        var created: String? = null,

        @SerializedName("user")
        var user: String? = null

    ) : Parcelable
}
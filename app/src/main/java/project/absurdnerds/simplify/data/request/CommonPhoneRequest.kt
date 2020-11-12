package project.absurdnerds.simplify.data.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
class CommonPhoneRequest(

    @SerializedName("phone")
    var phone: String? = null

) : Parcelable
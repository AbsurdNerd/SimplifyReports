package project.absurdnerds.simplify.data.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
 class AmbulancePostRequest(

    @SerializedName("patient_name")
    var patientName: String? = null,

    @SerializedName("age")
    var age: Int? = null,

    @SerializedName("patient_phone_number")
    var patientPhoneNumber: String? = null,

    @SerializedName("gender")
    var gender: String? = null,

    @SerializedName("blood_group")
    var bloodGroup: String? = null,

    @SerializedName("patient_problem")
    var patientProblem: String? = null,

    @SerializedName("location")
    var location: String? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("user")
    var user: String? = null

) : Parcelable
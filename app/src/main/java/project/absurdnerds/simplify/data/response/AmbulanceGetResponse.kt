package project.absurdnerds.simplify.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

class AmbulanceGetResponse : ArrayList<AmbulanceGetResponse.AmbulanceGetResponseItem>(){

    @Parcelize
   class AmbulanceGetResponseItem(

        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("patient_name")
        var patientName: String? = null,

        @SerializedName("patient_phone_number")
        var patientPhoneNumber: String? = null,

        @SerializedName("location")
        var location: String? = null,

        @SerializedName("address")
        var address: String? = null,

        @SerializedName("age")
        var age: Int? = null,

        @SerializedName("gender")
        var gender: String? = null,

        @SerializedName("blood_group")
        var bloodGroup: String? = null,

        @SerializedName("patient_problem")
        var patientProblem: String? = null,

        @SerializedName("created")
        var created: String? = null,

        @SerializedName("user")
        var user: String? = null

    ) : Parcelable
}
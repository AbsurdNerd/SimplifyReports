package project.absurdnerds.simplify.medical

sealed class MedicalViewState {

    object Init : MedicalViewState()

    object Loading : MedicalViewState()

    object OnSuccess : MedicalViewState()

    data class OnError(var mssg :String?) : MedicalViewState()

    data class FieldError(var errorType: ErrorField, var message: String?) : MedicalViewState()

    //todo change fields
    enum class ErrorField {
        FIRST_NAME,
        LAST_NAME,
        GENDER,
        DOB,
        WEIGHT,
        HEIGHT,
        PHONE
    }

}
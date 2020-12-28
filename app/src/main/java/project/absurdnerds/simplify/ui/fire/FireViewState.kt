package project.absurdnerds.simplify.fire

sealed class FireViewState {

    object Init : FireViewState()

    object Loading : FireViewState()

    object OnSuccess : FireViewState()

    data class OnError(var mssg : String?) : FireViewState()

    data class FieldError(var errorType: ErrorField, var message: String?) : FireViewState()

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
package project.absurdnerds.simplify.ui.police.policeReport

sealed class PoliceReportViewState {

    object Init : PoliceReportViewState()

    object Loading : PoliceReportViewState()

    object OnSuccess : PoliceReportViewState()

    data class OnError(var mssg : String?) : PoliceReportViewState()

    data class FieldError(var errorType: ErrorField, var message: String?) : PoliceReportViewState()

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
package project.absurdnerds.simplify.police.policeReport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import project.absurdnerds.simplify.police.policeReport.PoliceReportViewState.*
import project.absurdnerds.simplify.utils.lifecycle.SingleLiveEvent

class PoliceReportViewModel constructor(
    private val app: Application
) : AndroidViewModel(app) {

    private var viewState: PoliceReportViewState = Init
        set(value) {
            field = value
            stateObservable.postValue(value)
        }

    val stateObservable: SingleLiveEvent<PoliceReportViewState> by lazy {
        SingleLiveEvent()
    }

}
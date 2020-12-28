package project.absurdnerds.simplify.fire

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import project.absurdnerds.simplify.fire.FireViewState.*
import project.absurdnerds.simplify.utils.lifecycle.SingleLiveEvent

class FireViewModel constructor(
    private val app : Application
) : AndroidViewModel(app) {

    private var viewState: FireViewState = Init
        set(value) {
            field = value
            stateObservable.postValue(value)
        }

    val stateObservable: SingleLiveEvent<FireViewState> by lazy {
        SingleLiveEvent()
    }

    var text : MutableLiveData<String> = MutableLiveData()

//    fun button (){
//        viewState = OnSuccess
//        text.value = "button pressed"
//    }

}
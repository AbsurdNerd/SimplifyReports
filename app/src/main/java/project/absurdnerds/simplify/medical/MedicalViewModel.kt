package project.absurdnerds.simplify.medical

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import project.absurdnerds.simplify.medical.MedicalViewState.*
import project.absurdnerds.simplify.utils.lifecycle.SingleLiveEvent
import kotlin.coroutines.coroutineContext

class MedicalViewModel constructor(
    app : Application
) : AndroidViewModel(app){

    var firebaseAuth = FirebaseAuth.getInstance()

    var viewState : MedicalViewState = Init
        set(value) {
            field = value
            stateObservable.postValue(value)
        }

    val stateObservable : SingleLiveEvent<MedicalViewState> by lazy {
        SingleLiveEvent()
    }

    var text : MutableLiveData<String> = MutableLiveData()

    fun button() {
        viewState = OnSuccess
        text.value = "button pressed"
        firebaseAuth.signOut()
    }
}
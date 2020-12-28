package project.absurdnerds.simplify.ui.ui.medical

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.ui.ui.medical.MedicalViewState.*
import project.absurdnerds.simplify.utils.lifecycle.SingleLiveEvent

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
        text.value = R.string.button_pressed.toString()
        firebaseAuth.signOut()
    }
}
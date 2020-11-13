package project.absurdnerds.simplify.medical

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_medical.*
import project.absurdnerds.simplify.LocationChangeInterface
import project.absurdnerds.simplify.MapsFragment
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityMedicalBinding
import project.absurdnerds.simplify.medical.MedicalViewState.*
import project.absurdnerds.simplify.utils.AppConfig.SHARED_PREF
import project.absurdnerds.simplify.utils.dialog.ViewDialog
import project.absurdnerds.simplify.utils.showToast


class MedicalActivity : AppCompatActivity(), LocationChangeInterface {

    companion object{

        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var sweetAlertDialog: SweetAlertDialog

        fun start(context: Context) {
            val intent = Intent(context, MedicalActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityMedicalBinding

    private lateinit var viewModel : MedicalViewModel

    private lateinit var loadingDialog : ViewDialog

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical)
        initViewModel()
        setObservers()
        initUI()

        medicalLocationCard.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> medicalScroll.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> medicalScroll.requestDisallowInterceptTouchEvent(
                    false
                )
            }
            medicalLocationCard.onTouchEvent(event)
        })

        patientLocationFrame.setOnTouchListener(OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> medicalScroll.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> medicalScroll.requestDisallowInterceptTouchEvent(
                    false
                )
            }
            medicalLocationCard.onTouchEvent(event)
        })

    }

    private fun reportPatient() {

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MedicalViewModel::class.java)
        loadingDialog = ViewDialog(this)
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun initUI() {

        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.patientLocationFrame, MapsFragment())
            .commit()
        binding.viewModel = viewModel
    }

    private fun render(state: MedicalViewState) {
        when(state) {
            Loading -> showLoading()
            OnSuccess -> onSuccess()
            is OnError -> onError(state.mssg)
            is FieldError -> showFieldError(state)
        }
    }

    private fun showLoading() {
        loadingDialog.showDialog()
    }

    private fun hideLoading() {
        loadingDialog.hideDialog()
    }

    private fun onSuccess() {
        hideLoading()
        showToast("Success")
    }

    private fun onError(message: String?) {
        hideLoading()
        if (message != null) {
            showToast(message)
        }
    }

    private fun showFieldError(type: FieldError) {
        hideLoading()
        /*when (type.errorType) {
            ErrorField.FIRST_NAME -> binding.etFirstName.error = type.message
            ErrorField.LAST_NAME -> binding.etLastName.error = type.message
            ErrorField.DOB -> binding.etDob.error = type.message
            ErrorField.HEIGHT -> binding.etHeight.error = type.message
            ErrorField.WEIGHT -> binding.etWeight.error = type.message
            ErrorField.PHONE -> binding.etPhone.error = type.message
            ErrorField.GENDER -> showToast(type.message)
        }*/
    }

    override fun onLocationChange(location: String, latLong: String) {

    }
}
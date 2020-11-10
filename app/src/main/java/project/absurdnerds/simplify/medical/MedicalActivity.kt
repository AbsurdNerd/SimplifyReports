package project.absurdnerds.simplify.medical

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityMedicalBinding
import project.absurdnerds.simplify.medical.MedicalViewState.*
import project.absurdnerds.simplify.utils.showToast

class MedicalActivity : AppCompatActivity() {

    companion object{
        fun start(context: Context) {
            val intent = Intent(context, MedicalActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityMedicalBinding

    private lateinit var viewModel : MedicalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical)
        initViewModel()
        setObservers()
        initUI()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MedicalViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun initUI() {
        binding.viewModel = viewModel
    }

    private fun render(state : MedicalViewState) {
        when(state) {
            Loading -> showLoading()
            OnSuccess -> onSuccess()
            is OnError -> state.mssg?.let { showToast(it) }
            is FieldError -> showFieldError(state)
        }
    }

    private fun showLoading() {

    }

    private fun hideLoading() {

    }

    private fun onSuccess() {
        showToast("Success")
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

}
package project.absurdnerds.simplify.fire

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityFireBinding
import project.absurdnerds.simplify.fire.FireViewState.*
import project.absurdnerds.simplify.utils.showToast
import timber.log.Timber

class FireActivity : AppCompatActivity() {

    companion object{
        fun start(context : Context) {
            val intent = Intent(context, FireActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityFireBinding

    private lateinit var viewModel : FireViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fire)
        initViewModel()
        setObservers()
        initUI()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(FireViewModel::class.java)
    }

    private fun initUI() {
        binding.viewModel = viewModel
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun render(state : FireViewState) {
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
package project.absurdnerds.simplify.police.policeReport

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import project.absurdnerds.simplify.R
import project.absurdnerds.simplify.databinding.ActivityPoliceReportBinding
import project.absurdnerds.simplify.utils.dialog.ViewDialog
import project.absurdnerds.simplify.utils.showToast
import project.absurdnerds.simplify.police.policeReport.PoliceReportViewState.*

class PoliceReportActivity : AppCompatActivity() {

    companion object{
        private const val INTENT_REPORT_NAME = "intent_report_name"
        fun start(context: Context, report_name: String) {
            val intent = Intent(context, PoliceReportActivity::class.java)
            intent.putExtra(INTENT_REPORT_NAME, report_name)
            context.startActivity(intent)
        }
    }

    private lateinit var binding : ActivityPoliceReportBinding

    private lateinit var viewModel : PoliceReportViewModel

    private lateinit var loadingDialog : ViewDialog

    private var reportType : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_police_report)
        initViewModel()
        handleIntent()
        setObservers()
        initUI()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(PoliceReportViewModel::class.java)
        loadingDialog = ViewDialog(this)
    }

    private fun setObservers() {
        viewModel.stateObservable.observe(this, {
            render(it)
        })
    }

    private fun handleIntent() {
        reportType = intent.getStringExtra(INTENT_REPORT_NAME)
    }

    private fun initUI() {
        binding.viewModel = viewModel
    }

    private fun render(state : PoliceReportViewState) {
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

    private fun onError(message : String?) {
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
}
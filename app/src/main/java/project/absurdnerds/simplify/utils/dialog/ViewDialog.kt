package project.absurdnerds.simplify.utils.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import project.absurdnerds.simplify.R

class ViewDialog constructor(context: Context) {

    private var dialog: Dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_dialog)
        val gifImageView: AppCompatImageView = dialog.findViewById(R.id.custom_loading_imageView)
        val imageViewTarget = DrawableImageViewTarget(gifImageView)
        Glide.with(context)
            .load(R.drawable.ambulance)
            .placeholder(R.drawable.ambulance)
            .centerCrop()
            .into(imageViewTarget)
    }

    fun showDialog() {
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }

}
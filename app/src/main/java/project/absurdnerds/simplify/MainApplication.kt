package project.absurdnerds.simplify

import android.app.Application
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho
import project.absurdnerds.simplify.utils.timber.ReleaseTree
import project.absurdnerds.simplify.utils.timber.DebugTree
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setDebugTools()
    }

    private fun setDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Stetho.initializeWithDefaults(this)
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
package si.uni_lj.fri.pbd.miniapp3

import android.app.Application
import androidx.databinding.library.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

class CoreApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        plantTimber()
    }

    //BuildConfig is a class containing static constants that is included in every Android application.
    //BuildConfig class is generated automatically by Android build tools
    private fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
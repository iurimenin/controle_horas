package io.github.iurimenin.horastrabalhadas

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



/**
 * Created by Iuri Menin on 20/12/2017.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true) // Enables Crashlytics debugger
                .build()

        Fabric.with(fabric)
    }

}
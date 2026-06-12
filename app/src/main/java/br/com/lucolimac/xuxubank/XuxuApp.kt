package br.com.lucolimac.xuxubank

import android.app.Application
import br.com.lucolimac.xuxubank.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class XuxuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@XuxuApp)
            modules(appModule)
        }
    }
}

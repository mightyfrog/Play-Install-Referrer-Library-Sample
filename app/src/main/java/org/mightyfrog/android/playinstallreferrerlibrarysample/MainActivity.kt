package org.mightyfrog.android.playinstallreferrerlibrarysample

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.android.synthetic.main.activity_main.textView
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.util.Date

/**
 * https://android-developers.googleblog.com/2017/11/google-play-referrer-api-track-and.html
 * https://developer.android.com/google/play/installreferrer/library.html
 *
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), InstallReferrerStateListener {

    private lateinit var referrerClient: InstallReferrerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(this)
    }

    override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {
                referrerClient.installReferrer.apply {
                    textView.text = getString(
                        R.string.response_ok,
                        installReferrer,
                        Date(referrerClickTimestampSeconds),
                        Date(installBeginTimestampSeconds)
                    )
                }
            }
            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED ->
                textView.text = getString(R.string.response_not_supported)
            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE ->
                textView.text = getString(R.string.response_unavailable)
            else -> textView.text = getString(R.string.response_not_found)
        }

        referrerClient.endConnection()
    }

    @SuppressLint("LogNotTimber")
    override fun onInstallReferrerServiceDisconnected() {
        Log.d(TAG, "Install referrer disconnected.")
    }

    companion object {
        private const val TAG = "Install Referrer"
    }
}

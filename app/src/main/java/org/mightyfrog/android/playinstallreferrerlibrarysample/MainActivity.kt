package org.mightyfrog.android.playinstallreferrerlibrarysample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * https://android-developers.googleblog.com/2017/11/google-play-referrer-api-track-and.html
 * https://developer.android.com/google/play/installreferrer/library.html
 *
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity(), InstallReferrerStateListener {

    private lateinit var referrerClient: InstallReferrerClient

    companion object {
        private const val TAG = "Install Referrer"
    }

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
                    textView.text = getString(R.string.response_ok, installReferrer, Date(referrerClickTimestampSeconds), Date(installBeginTimestampSeconds))
                }
            }
            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                textView.text = getString(R.string.response_not_supported)
            }
            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                textView.text = getString(R.string.response_unavailable)
            }
            else -> textView.text = getString(R.string.response_not_found)
        }

        referrerClient.endConnection()
    }

    @SuppressLint("LogNotTimber")
    override fun onInstallReferrerServiceDisconnected() {
        Log.e(TAG, "Install referrer disconnected.")
    }
}

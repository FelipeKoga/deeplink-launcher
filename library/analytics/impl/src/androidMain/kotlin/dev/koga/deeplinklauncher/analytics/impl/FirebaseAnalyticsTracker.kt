package dev.koga.deeplinklauncher.analytics.impl

import android.content.Context
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker

class FirebaseAnalyticsTracker(
    private val context: Context,
) : AnalyticsTracker {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        Firebase.analytics
    }

    override fun logEvent(name: String, parameters: Map<String, String>) {
        firebaseAnalytics.logEvent(name, parameters.toBundle())
    }

    private fun Map<String, String>.toBundle(): Bundle {
        val bundle = Bundle()
        forEach { (key, value) ->
            bundle.putString(key, value)
        }
        return bundle
    }
}

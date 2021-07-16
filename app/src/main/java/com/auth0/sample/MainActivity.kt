package com.auth0.sample

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.lock.AuthenticationCallback
import com.auth0.android.lock.InitialScreen
import com.auth0.android.lock.Lock
import com.auth0.android.lock.utils.CustomField
//import com.auth0.android.lock.utils.LockException
import com.auth0.android.result.Credentials


class MainActivity : AppCompatActivity() {

    private lateinit var account: Auth0

    private lateinit var lock: Lock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the account object with the Auth0 application details
        account = Auth0(this)

        val customFields: MutableList<CustomField> = ArrayList()

        val firstNameField = CustomField(
            R.drawable.com_auth0_lock_header_logo,
            CustomField.FieldType.TYPE_NAME,
            "given_name",
            R.string.registration_name_label,
            CustomField.Storage.PROFILE_ROOT
        )
        customFields.add(firstNameField)

        val lastNameField = CustomField(
            R.drawable.com_auth0_lock_header_logo,
            CustomField.FieldType.TYPE_NAME,
            "family_name",
            R.string.registration_last_name_label,
            CustomField.Storage.PROFILE_ROOT
        )
        customFields.add(lastNameField)

        val allowedConnections: MutableList<String> = ArrayList()
        allowedConnections.add("google-oauth2")
        allowedConnections.add("github")
        allowedConnections.add("Username-Password-Authentication")

        lock = Lock.newBuilder(account, callback)
            // Customize Lock
            .withScheme("demo")
            .withAudience(this.getResources().getString(R.string.api_audience))
            .withScope("openid profile email user_id offline_access")
            .setPrivacyURL("https://swipii.com/privacy-policy")
            .setTermsURL("https://swipii.com/terms-and-conditions")
            .closable(true)
            .allowedConnections(allowedConnections)
            //.allowLogIn(false)
            .allowSignUp(true)
            //.initialScreen(InitialScreen.SIGN_UP)
            .withSignUpFields(customFields)
            .build(this)
        startActivity(lock.newIntent(this))
    }



    override fun onDestroy() {
        super.onDestroy()
        // Important! Release Lock and its resources
        lock.onDestroy(this)
    }

    private val callback = object : AuthenticationCallback() {
//        override fun onError(error: LockException) {
//            // error
//        }

        override fun onAuthentication(credentials: Credentials) {
            // Authenticated
            val linearLayout = LinearLayout(applicationContext)
            setContentView(linearLayout)
            linearLayout.orientation = LinearLayout.VERTICAL
            val textView = TextView(applicationContext)
            textView.setGravity(Gravity.CENTER_HORIZONTAL)
            textView.text = "You are logged in."
            linearLayout.addView(textView)
        }

//        override fun onCanceled() {
//            // cancelled
//        }

        override fun onError(error: AuthenticationException) {
            // An exception occurred
        }
    }
}
package io.github.iurimenin.horastrabalhadas

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            startMain()
        } else {
            // not signed in
            startLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                setCrashlyticsUserInfo()
                startMain()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    finish()
                    return
                }

                if (response.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection)
                    return
                }

                if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error)
                    return
                }
            }

            showSnackbar(R.string.unknown_sign_in_response)
        }
    }

    private fun setCrashlyticsUserInfo() {

        FirebaseAuth.getInstance().currentUser?.let {

            it.email?.let { email ->
                Crashlytics.setUserIdentifier(email)
                Crashlytics.setUserEmail(email)
            }

            it.displayName?.let { name ->
                Crashlytics.setUserName(name)
            }
        }
    }

    private fun startMain() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun startLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginUI)
                        .setAvailableProviders(Arrays.asList(
                                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
//                              AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
//                              AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                              AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build())
                        )).build(),
                RC_SIGN_IN)
    }

    private fun showSnackbar(idString: Int) {

        Toast.makeText(this, getString(idString), Toast.LENGTH_SHORT).show()
    }
}

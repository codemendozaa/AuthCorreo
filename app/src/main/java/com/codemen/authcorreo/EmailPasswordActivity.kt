package com.codemen.authcorreo

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_email_password.*

class EmailPasswordActivity : Extensions(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        auth = FirebaseAuth.getInstance()


    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun createAccount(email: String, password: String) {
        writeLog("createAccount:$email")
        if (!validateEmail() && !validatePass()) {
            return
        }

        showProgressDialog()


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ToastLong("createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    writeToLog("createUserWithEmail:failure", task.exception)
                    ToastLong("Authentication failed.")
                    updateUI(null)
                }

                hideProgressDialog()

            }

    }

    private fun signIn(email: String, password: String) {
        writeLog("signIn:$email")
        if (!validateEmail() && !validatePass()) {
            return
        }

        showProgressDialog()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    writeLog("signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    writeToLog("signInWithEmail:failure", task.exception)
                    ToastLong("Authentication failed.")
                    updateUI(null)
                }

                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                hideProgressDialog()
            }
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }

    private fun sendEmailVerification() {

        verifyEmailButton.isEnabled = false

        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    ToastLong("Verification email sent to ${user.email}")

                } else {
                    writeToLog("sendEmailVerification", task.exception)
                    ToastShort("Failed to send verification email.")
                }
            }
    }

    private fun validateEmail(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }
        return valid
    }


    private fun validatePass():Boolean{
        var valid = false
        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."

        } else {
            fieldPassword.error = null
        }

        return valid

    }



    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            status.text = getString(
                R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified
            )
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            emailPasswordButtons.visibility = View.GONE
            emailPasswordFields.visibility = View.GONE
            signedInButtons.visibility = View.VISIBLE

            verifyEmailButton.isEnabled = !user.isEmailVerified
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            emailPasswordButtons.visibility = View.VISIBLE
            emailPasswordFields.visibility = View.VISIBLE
            signedInButtons.visibility = View.GONE
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.emailCreateAccountButton -> createAccount(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.emailSignInButton -> signIn(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.signOutButton -> signOut()
            R.id.verifyEmailButton -> sendEmailVerification()
        }
    }


}

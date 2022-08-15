package com.ibnu.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var etSiEmail: EditText
    private lateinit var etSiPassword: EditText

    private lateinit var btnSiSignIn: Button
    private lateinit var btnSiSignUp: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        etSiEmail = findViewById(R.id.et_si_email)
        etSiPassword = findViewById(R.id.et_si_password)

        btnSiSignIn = findViewById(R.id.btn_si_signIn)
        btnSiSignUp = findViewById(R.id.btn_si_signUp)

        auth = FirebaseAuth.getInstance()

        etSiEmail.doOnTextChanged { text, start, before, count ->
            etLayoutEmail(null)
        }

        etSiPassword.doOnTextChanged { text, start, before, count ->
            etLayoutPassword(null)
        }

        btnSiSignIn.setOnClickListener {
            var email = etSiEmail.text.toString()
            var password = etSiPassword.text.toString()

            when {
                email.isEmpty() && password.isEmpty() -> {
                    etLayoutEmail("Tolong isi email anda!!")
                    etLayoutPassword("Tolong isi password anda!!")
                    etSiEmail.requestFocus()
                }

                email.isEmpty() -> {
                    etLayoutEmail("Tolong isi email anda!!")
                    etSiEmail.requestFocus()
                }

                password.isEmpty() -> {
                    etLayoutPassword("Tolong isi password anda!!")
                    etSiPassword.requestFocus()
                }

                else -> {
                    signIn(email, password)
                }
            }
        }

        btnSiSignUp.setOnClickListener {
            pindahKeSignUp()
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    pindahKeUserCA()
                } else {
                    showToast("Hayolo salah password nih")
                }
            }
    }

    // *** nanti unComment biar gk perlu masukin akun lagi *** //

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            pindahKeUserCA()
        }
    }

    private fun pindahKeSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun pindahKeUserCA() {
        Intent(this, UserConversationActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun showToast(note: String) {
        Toast.makeText(this, note, Toast.LENGTH_SHORT).show()
    }

    private fun etLayoutEmail(note: String?) {
        et_layout_si_email.error = note
    }

    private fun etLayoutPassword(note: String?) {
        et_layout_si_password.error = note
    }
}
package com.example.capstonefix.ui.SignUp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.capstonefix.databinding.ActivitySignUpBinding
import com.example.capstonefix.repository.ViewModelFactory
import com.example.capstonefix.response.Register.RegisterResponse
import com.example.capstonefix.ui.Dashboard.dashboard.DashboardActivity
import com.example.capstonefix.ui.Login.LoginActivity
import com.example.capstonefix.ui.Sambutan.SambutanSignUp
import com.example.submissionstoryapp.response.repository.Result

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels{
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()


        binding.inputUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val username = binding.inputUsername.text.toString()
                if (username.isEmpty()) {
                    binding.inputUsername.error = "Input tidak boleh kosong"
                }else{
                    binding.inputUsername.error = null
                }
                validateInput()
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = binding.inputEmail.text.toString()
                if (email.isEmpty()) {
                    binding.inputEmail.error = "Input tidak boleh kosong"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.inputEmail.error = "Format email tidak valid"
                } else {
                    binding.inputEmail.error = null
                }
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.inputPassword.text.toString()
                if (password.isEmpty()) {
                    binding.inputPassword.error = "Input tidak boleh kosong"
                }else{
                    binding.inputPassword.error = null
                }
                validateInput()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmpassword = binding.inputConfirmPassword.text.toString()
                val password = binding.inputPassword.text.toString()

                if (confirmpassword.isEmpty()) {
                    binding.inputConfirmPassword.error = "Input tidak boleh kosong"
                }else if(confirmpassword != password ){
                    binding.inputConfirmPassword.error = "Password tidak sesuai"
                }else{
                    binding.inputConfirmPassword.error = null
                }
                validateInput()
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        binding.btnLogin.setOnClickListener{
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnDaftar.setOnClickListener{ it ->
            val name = binding.inputUsername.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            signUpViewModel.signUp(name, email, password).observe(this) {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            processSignUp(it.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
    private fun validateInput() {
        val username = binding.inputUsername.text.toString()
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        val Confirmpassword = binding.inputConfirmPassword.text.toString()

        val emailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val usernameValid = !username.isEmpty()
        val passwordValid = !password.isEmpty()
        val passwordValid2 = password == Confirmpassword

        binding.btnDaftar.isEnabled = emailValid && passwordValid && passwordValid2 && usernameValid
    }

    private fun processSignUp(data: RegisterResponse) {
        if (data.mesaage == "fail") {
            Toast.makeText(this, "Gagal Sign Up", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Sign Up berhasil, silahkan login!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.logoGambar.isInvisible = state
        binding.inputUsername.isInvisible = state
        binding.pbLogin.isVisible = state
        binding.inputEmail.isInvisible = state
        binding.inputPassword.isInvisible = state
        binding.btnDaftar.isInvisible = state
        binding.containerLogin.isInvisible = state
        binding.inputConfirmPassword.isInvisible = state
    }

    private fun playAnimation() {
        binding.logoGambar.visibility = View.VISIBLE
        binding.Judul.visibility = View.VISIBLE
        binding.keterangan.visibility = View.VISIBLE
        binding.containerInput.visibility = View.VISIBLE
        binding.btnDaftar.visibility = View.VISIBLE
        binding.containerLogin.visibility = View.VISIBLE



        val logoGambar = ObjectAnimator.ofFloat(binding.logoGambar, View.ALPHA, 1f).setDuration(1000)
        val Judul = ObjectAnimator.ofFloat(binding.Judul, View.ALPHA, 1f).setDuration(1000)
        val keterangan = ObjectAnimator.ofFloat(binding.keterangan, View.ALPHA, 1f).setDuration(1000)
        val containerInput = ObjectAnimator.ofFloat(binding.containerInput, View.ALPHA, 1f).setDuration(1000)
        val btnDaftar = ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 1f).setDuration(1000)
        val containerSignup = ObjectAnimator.ofFloat(binding.containerLogin, View.ALPHA, 1f).setDuration(1000)


        val together =
            AnimatorSet().apply {
                playTogether(Judul, keterangan, containerInput)
            }
        AnimatorSet().apply {
            playSequentially(logoGambar, together, btnDaftar, containerSignup)
            start()
        }
    }
}
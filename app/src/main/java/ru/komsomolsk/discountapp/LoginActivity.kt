package ru.komsomolsk.discountapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.komsomolsk.discountapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { performLogin() }
        binding.btnGuest.setOnClickListener { enterAsGuest() }
    }

    private fun performLogin() {
        val login = binding.etLogin.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        when {
            login == "admin" && password == "123456" -> {
                SessionManager.currentRole = RoleRepository.getStoredRole(this, login)
                openMain()
            }
            login == "manager" && password == "123456" -> {
                SessionManager.currentRole = RoleRepository.getStoredRole(this, login)
                openMain()
            }
            login.isEmpty() || password.isEmpty() -> {
                Toast.makeText(this, R.string.fill_login_password, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enterAsGuest() {
        SessionManager.currentRole = UserRole.GUEST
        openMain()
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

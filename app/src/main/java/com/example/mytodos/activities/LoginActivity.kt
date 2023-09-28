package com.example.mytodos.activities
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mytodos.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreference =getSharedPreferences("Login_Activity", Context.MODE_PRIVATE)
        val getusername = sharedPreference.getString("USERNAME","")
        val getpassword = sharedPreference.getString("PASSWORD","")
        if(getusername != "" && getpassword != "")
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener{
            val username = binding.usernameLg.text.toString()
            val password = binding.passwordLg.text.toString()

            if(username == "" || password == "")
            {
                Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show()
            }
            else {


                val editor = sharedPreference.edit()
                editor.putString("USERNAME", username)
                editor.putString("PASSWORD", password)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
package com.example.sequencegame

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.net.URL

class LoginActivity : AppCompatActivity() {

    lateinit var databaseHelper : DatabaseHelper
    lateinit var context : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this
        databaseHelper = DatabaseHelper(context)

    }

    fun showNextActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signIn(view : View) {
        var gson = GsonBuilder().create()
        var users : List<User>
        try {
            var text = URL("localhost:8080/users/all").readText()
            println(text)
            users = gson.fromJson(text, Array<User>::class.java).asList()
        } catch(e : Exception) {
            users = databaseHelper.readUsers()
        }

        for (user in users){
            if (user.name == nameTextView.text.toString()){
                if (user.password == passwordTextView.text.toString()){
                    saveCurrentUser(user.curScore, user.bestScore)
                    try {
                        val name = user.name
                        val password = user.password
                        var text = URL("localhost:8080/users?name=$name&password=$password&curScore=0&bestScore=0").readText()
                        println(text)
                    }   catch (e : Exception) {
                        Toast.makeText(applicationContext,"Check connect with network", Toast.LENGTH_SHORT).show()
                    }
                    showNextActivity()
                    return
                }
            }
        }

        Toast.makeText(applicationContext,"Wrong name or password", Toast.LENGTH_SHORT).show()

    }


    fun signUp(view : View) {
        var gson = GsonBuilder().create()

        val name = nameTextView.text.toString()
        val password = passwordTextView.text.toString()
        var user = User(0, name, password,0 ,0)
        var users : List<User>


        if (user.name.isEmpty() || user.password.isEmpty()){
            Toast.makeText(applicationContext,"Name or password can't be empty", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            var text = URL("localhost:8080/users/all").readText()
            println(text)
            users = gson.fromJson(text, Array<User>::class.java).asList()
        } catch(e : Exception) {
            users = databaseHelper.readUsers()
        }

        for (curUser in users){
            if (curUser.name == user.name){
                Toast.makeText(applicationContext,"This name is used", Toast.LENGTH_SHORT).show()
                return
            }
        }
        try {
            var text = URL("localhost:8080/users?name=$name&password=$password&curScore=0&bestScore=0").readText()
            println(text)
            if (text == "This name is used") {
                Toast.makeText(applicationContext,"This name is used", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e : Exception) {
            databaseHelper.insertUser(user)
        }
        Toast.makeText(applicationContext,"Successful registration", Toast.LENGTH_SHORT).show()
    }

    private fun saveCurrentUser(curScore : Int, bestScore : Int) {
        val sharedPref = context.getSharedPreferences("SharedData", Context.MODE_PRIVATE) ?: return

        with (sharedPref.edit()) {
            putString("UserName", nameTextView.text.toString())
            putString("UserPassword", passwordTextView.text.toString())
            putInt("UserCurScore", curScore)
            putInt("UserBestScore", bestScore)
            commit()
        }

    }

}

package com.example.sequencegame


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_account.view.*


class AccountFragment : Fragment() {

    lateinit var sharedPref : SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        sharedPref = this.activity!!.getSharedPreferences("SharedData", Context.MODE_PRIVATE)

        var name = sharedPref.getString("UserName", "")
        var currentScore = sharedPref.getInt("UserBestScore", 0)

        view.nameTextView.text =  Editable.Factory.getInstance().newEditable(name)
        view.bestScoreTextView.text = currentScore.toString()


        view.signOutButton.setOnClickListener {
            setNoneUser()
            val intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            this.activity!!.finish()
        }

        return view
    }



    fun setNoneUser () {

        with (sharedPref.edit()) {
            putString("UserLogin", "")
            putString("UserPassword", "")
            putInt("UserCurScore", 0)
            putInt("UserBestScore", 0)
            commit()
        }

    }


}

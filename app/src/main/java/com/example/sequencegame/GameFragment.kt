package com.example.sequencegame


import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_game.view.*


class GameFragment : Fragment() {

    private val numbers : MutableList<Int> = mutableListOf(0,1,2,3,4,5,6,7,8)

    lateinit var sequence : ArrayList<Int>
    lateinit var images : ArrayList<ImageView>

    private var curScore = 0
    private val level = 4
    private var turn = 1

    private var isInGame = false

    lateinit var databaseHelper: DatabaseHelper
    lateinit var sharedPref : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        val view: View = inflater!!.inflate(R.layout.fragment_game, container, false)

        databaseHelper = DatabaseHelper(this.activity!!)


        sharedPref = this.activity!!.getSharedPreferences("SharedData", Context.MODE_PRIVATE)
        val currentScore = sharedPref.getInt("UserCurScore", 0)
        view.curScoreTextView.text = currentScore.toString()

        images = arrayListOf(view.image0, view.image1, view.image2, view.image3, view.image4, view.image5,
            view.image6, view.image7, view.image8)

        sequence = arrayListOf()

        view.image0.setOnClickListener {
            checkState(0)
        }
        view.image1.setOnClickListener {
            checkState(1)
        }
        view.image2.setOnClickListener {
            checkState(2)
        }
        view.image3.setOnClickListener {
            checkState(3)
        }
        view.image4.setOnClickListener {
            checkState(4)
        }
        view.image5.setOnClickListener {
            checkState(5)
        }
        view.image6.setOnClickListener {
            checkState(6)
        }
        view.image7.setOnClickListener {
            checkState(7)
        }
        view.image8.setOnClickListener {
            checkState(8)
        }


        view.startButton.setOnClickListener {
            startGame()
        }

        return view

    }



    internal inner class showKennysInGame(private var activity : GameFragment) : AsyncTask<String, String, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            startButton.visibility = View.INVISIBLE
            for (index in sequence){
                images[index].setImageResource(R.drawable.kenny_mccormick)
            }
        }

        override fun doInBackground(vararg params: String?): String {

            Thread.sleep(500)

            println("jestem")

            return "foo"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            for (index in sequence){
                images[index].setImageResource(R.drawable.empty)
            }
            isInGame = true
        }
    }

    fun setSequence() {
        numbers.shuffle()
        for (i in 0..(level-1)){
            sequence.add(numbers[i])
        }
        println(sequence)
    }

    fun hideKennys() {
        for (image in images ){
            image.setImageResource(R.drawable.empty)
        }
    }

    fun showKennys() {
        for (image in images ){
            image.setImageResource(R.drawable.kenny_mccormick)
        }
    }


    fun startGame() {
        setSequence()
        hideKennys()
        Toast.makeText(this.activity, "The game is started!", Toast.LENGTH_SHORT).show()
        showKennysInGame(this).execute()
    }


    fun gameAlert(win: Boolean) {

        sequence.clear()
        val builder = AlertDialog.Builder(this.activity!!)

        if (win) {
            builder.setTitle("Win")
        }
        else {
            builder.setTitle("Defeat")
        }

        builder.setMessage("Your score: $curScore")

        builder.setPositiveButton("OK"){dialog,which ->
            null
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        dialog.show()

        isInGame = false

    }

    fun gameEnd() {
        val name = sharedPref.getString("UserName", "")
        databaseHelper.updateCurScore(name,curScore)
        saveScore()
        curScoreTextView.text = curScore.toString()
        turn = 1
        sequence.clear()
        showKennys()
    }

    fun loseGame() {
        gameEnd()
        curScore = 0
        gameAlert(false)
    }

    fun winGame () {
        val name = sharedPref.getString("UserName", "")
        val bestScore = sharedPref.getInt("UserBestScore", 0)
        curScore++
        gameEnd()
        databaseHelper.updateCurScore(name,bestScore)
        gameAlert(true)
        println("win" + sequence)
    }

    fun checkState(imageId : Int) {
        if (isInGame) {
            if (!sequence.contains(imageId)) {
                loseGame()
                startButton.visibility = View.VISIBLE
                return
            } else if (sequence.contains(imageId) && turn == level) {
                println("else if ")
                sequence.remove(imageId)
                startButton.visibility = View.VISIBLE
                winGame()
            } else {
                println("else")
                sequence.remove(imageId)
                println(sequence)
                images[imageId].setImageResource(R.drawable.kenny_mccormick)
                turn++
            }
        }
    }

    fun saveScore() {
        val sharedPref = this.activity!!.getSharedPreferences("SharedData", Context.MODE_PRIVATE)
        val bestScore = sharedPref.getInt("UserBestScore", 0)
        with (sharedPref.edit()) {
            if (bestScore < curScore){
                putInt("UserBestScore", curScore)
            }
            putInt("UserCurScore", curScore)
            commit()
        }
    }


}

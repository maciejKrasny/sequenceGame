package com.example.sequencegame

class User  {
    var id = 0
    var name = ""
    var password = ""
    var curScore = 0
    var bestScore = 0
    constructor(id : Int, name : String, password : String, curScore : Int, bestScore : Int){
        this.id = id
        this.name = name
        this.password = password
        this.curScore = curScore
        this.bestScore = bestScore
    }
}



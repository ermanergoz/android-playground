package com.example.myapplication

import android.widget.Button

lateinit var buttons: MutableList<MutableList<Int>>
lateinit var tempButtonList: MutableList<Int>

fun createButtons() {
    buttons = mutableListOf()
    tempButtonList = mutableListOf()

    for (i in 0 until 10)  //row
    {
        tempButtonList.clear()
        for (j in 0 until 10)  //column
        {
            tempButtonList.add(0)
        }
        buttons.add(tempButtonList)
    }
}

fun print2DList(buttons: MutableList<MutableList<Int>>) {
    for (i in 0 until buttons.size) //this is another way of doing it. Like C++11
    {
        for (j in buttons[0]) //this is another way of doing it. Like C++11
        {
            print(" "+j)
        }
        print("\n")
    }
}

fun main(args: Array<String>) {
    //var is mutable
    //val is immutable

    var weather = "Sunny"
    //var weather : String = "Sunny" //another and nicer way
    val aValue = "Something"
    var someNumber = 5;
    //var someNumber : Int = 5 //another and nicer way
    println(weather);

    weather = "eben"
    println(weather)

    println(aValue)
    //aValue="something different"    //ERROR
    //val weather=24 //ERROR only strings allowed
    someNumber = 4 //this is fine
    //someNumber=4.5//this is not

    println(someNumber)

    var pi: Double = 3.14
    var radius: Int = 5
    var c = pi * radius.toDouble()/*just to show how csting is done*/ * 2.0

    println(c)

    var isTheLightOn: Boolean = true

    if (isTheLightOn) {
        println("nice")
    } else
        println("damn")

    //Lists and arrays

    var topCandyList: List<String> = listOf("Fun dip", "Snickers", "100 grand bar")
    println(topCandyList[0])
    println(topCandyList.get(1))

    //topCandyList[0]="dfsfsd"    //cant add or remove anything. It is immutable, read only

    var topCandyArray: Array<String> = arrayOf("Fun dip", "Snickers", "100 grand bar")
    //cant add or remove anything. It is immutable, read only. The rest (printing accessing etc.) is the same as List

    var topCandyMutableList: MutableList<String> = mutableListOf("Fun dip", "Snickers", "100 grand bar")
    //we can add and remove things. The rest (printing accessing etc.) is the same as List

    topCandyMutableList.add(2, "something on index 2")  //also works with val for some reason
    //inserts it to index 2 and size of the list is increased by 1
    println(topCandyMutableList.get(2))
    println("-----------------------------")
/*
    for(x in 0..1_000_000)  // " _ " is like a dot. Just to read the number comfortably. It has no effect here
    {
        println(x)
    }
*/
    for (i in 0..2) {
        println(topCandyMutableList.get(i))
    }
    println("-----------------------------")
    for (i in 0..topCandyMutableList.size - 1) //this is how we use for loop
    {
        println(topCandyMutableList.get(i))
    }
    println("-----------------------------")
    for (i in topCandyMutableList) //this is another way of doing it. Like C++11
    {
        println(i)
    }
    print("something")
    print(" something else\n")    //print, prints the things to the same line

    //creating functions
    fun addTwoNumbers(num1: Int, num2: Int): Int //this is the return type
    {
        return num1 + num2
    }
    println(addTwoNumbers(4, 5))

    fun addTwoNumbers2(num1: Int, num2: Int) //this is void
    {
        println(num1 + num2)
    }
    addTwoNumbers2(3, 4)

    fun dogInfo(name: String, age: Int): String {
        return "Hi this is " + name + " and i am " + age + " years old. Hav Hav!"
    }
    println(dogInfo("tefo", 56))

    //creating classes
    class Dog() {
        var name = ""
        var age = 0
        var furColor = ""

        fun dogInfo(): String {
            return name + " is " + age + " years old and has " + furColor + " fur"
        }
    }

    var myDog = Dog()
    myDog.age = 8
    myDog.name = "tefo"
    myDog.furColor = "black"

    var anotherDog = Dog()
    anotherDog.age = 13
    anotherDog.name = "rifki"
    anotherDog.furColor = "brown"

    println(myDog.name)
    println(anotherDog.name)

    println(myDog.dogInfo())

    //check: https://kotlinlang.org/docs/tutorials/kotlin-for-py/classes.html
    //       https://kotlinlang.org/docs/tutorials/kotlin-for-py/inheritance.html

    createButtons()
    print2DList(buttons)
}
package com.example.emojidictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit /*to initialize it later*/ var layoutManager : GridLayoutManager
    lateinit var adapter : RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager= GridLayoutManager(this, 3/*number of columns*/)
        recyclerView.layoutManager=layoutManager

        adapter = RecyclerAdapter(arrayListOf("ԅ(≖‿≖ԅ)"," ฅ^•ﻌ•^ฅ","¯\\_(ツ)_/¯","( ͡° ͜ʖ ͡°)","( • )( • )"))
        recyclerView.adapter=adapter
    }
}

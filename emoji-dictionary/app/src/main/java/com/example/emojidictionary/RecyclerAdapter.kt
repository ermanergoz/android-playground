package com.example.emojidictionary

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_item_row.view.*

class RecyclerAdapter(val emojis : ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.TextHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        return TextHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_row, parent, false))
        /*to use the xml file, taking xml and inflating, adding so view will be ready to show*/
    }

    override fun getItemCount(): Int {
        return emojis.count()
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.TextHolder, position: Int) {
        var emoji = emojis[position]
        holder.bindEmoji(emoji)
    }

    class TextHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
            var view : View = view
        var emoji : String = ""

        init {
            view.setOnClickListener(this)
        }

        fun bindEmoji(emoji:String)
        {
            this.emoji=emoji
            view.itemTextView.text=emoji
        }

        override fun onClick(p0: View?) {
            val detailIntent = Intent(view.context, EmojiDetailActivity::class.java)
            detailIntent.putExtra("emoji", emoji)
            startActivity(view.context, detailIntent, null)
        }
    }
}

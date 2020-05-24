package com.example.firebaseapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import kotlinx.android.synthetic.main.read_book.view.*
import org.greenrobot.eventbus.EventBus

class ReadBookAdapter(val books: List<Book>) : RecyclerView.Adapter<ReadBookViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.read_book, parent, false)

        return ReadBookViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: ReadBookViewHolder, position: Int) {
        val currentItem = books[position]
        holder.titleTV.text = currentItem.title
        holder.authorTV.text = currentItem.author
        holder.bind(books.get(position))
        holder.readButton.setOnClickListener {
            EventBus.getDefault().post(holder.idBook.toString())
        }
    }
}
class ReadBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val titleTV: TextView = itemView.tv_title_read
    val authorTV: TextView = itemView.tv_author_read
    val readButton: ImageButton = itemView.tv_readButtonBook
    var idBook: String? = null
    fun bind(book: Book) = with(itemView) {
        titleTV.text = book.title
        authorTV.text = book.author
        idBook = book.idBook
    }
}

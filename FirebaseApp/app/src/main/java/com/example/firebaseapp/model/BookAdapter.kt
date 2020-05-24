package com.example.firebaseapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.book_row.view.*
import org.w3c.dom.Text
import org.greenrobot.eventbus.EventBus

class BookAdapter(val books: List<Book>) : RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.book_row, parent, false)

        return BookViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
         return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books.get(position))
        holder.readButton.setOnClickListener {
            EventBus.getDefault().post(holder.idBook.toString())
        }
    }
}
class BookViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
    val titleTV: TextView = itemView.tv_title
    val authorTV: TextView = itemView.tv_author
    val readButton: ImageButton = itemView.tv_readButton
    var idBook: String? = null

    fun bind(book: Book) = with(itemView) {
        titleTV.text = book.title
        authorTV.text = book.author
        idBook = book.idBook

    }
}
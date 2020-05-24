package com.example.firebaseapp.ui.books

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService

import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.google.firebase.database.*

class AddBook : Fragment() {

    companion object {
        fun newInstance() = AddBook()
    }

    private lateinit var viewModel: AddBookViewModel
    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var addBookButton: Button
    private lateinit var databaseReference: DatabaseReference

    var allBooks = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.add_book_fragment, container, false)

        databaseReference = FirebaseDatabase.getInstance().getReference("books")

        titleEditText = view.findViewById(R.id.editText_title)
        authorEditText = view.findViewById(R.id.editText_author)
        addBookButton = view.findViewById(R.id.button_addBook)

        addBookButton.setOnClickListener {
            addBook()
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddBookViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun addBook() {
        val title = titleEditText.text.toString().trim()
        val author = authorEditText.text.toString().trim()
        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(context, "Please enter a title and author", Toast.LENGTH_LONG).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("books")
        val id = ref.push().key.toString()
        val book = Book(id, title, author)
        updateBooks()
        for (i in allBooks) {
            if ((i.title == book.title) and (i.author == book.author)) {
                Toast.makeText(context, "This book is already added", Toast.LENGTH_LONG).show()
                break
            }
            else {
                ref.child(id).setValue(book).addOnCompleteListener {
                    Toast.makeText(context, "Book saved successfully", Toast.LENGTH_LONG).show()
                    titleEditText.text.clear()
                    authorEditText.text.clear()
                    val inputMethodManager =
                        activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
                }
            }
        }


    }
    private fun updateBooks() {
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val book = h.getValue(Book::class.java)
                        allBooks.add(book!!)
                    }
                }
            }
        })
    }

}

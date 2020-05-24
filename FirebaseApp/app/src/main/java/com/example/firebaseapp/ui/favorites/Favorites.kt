package com.example.firebaseapp.ui.favorites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.model.BookAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Favorites : Fragment() {

    // IN THIS FRAGMENT DISPLAY ALL BOOKS

    companion object {
        fun newInstance() = Favorites()
    }

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookMenager: RecyclerView.LayoutManager
    private lateinit var databaseReference: DatabaseReference
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    var allBooks = mutableListOf<Book>()
    var readBook = mutableListOf<Book>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.favorites_fragment, container,false)

        recyclerView = view.findViewById(R.id.recyclerView)
        bookMenager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!


        databaseReference = FirebaseDatabase.getInstance().getReference("books")
        ref = FirebaseDatabase.getInstance().getReference(user.uid)

        recyclerView.layoutManager = bookMenager


        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    allBooks.clear()
                    for (h in p0.children) {
                        val book = h.getValue(Book::class.java)
                        allBooks.add(book!!)
                    }
                    val bookAdapter = BookAdapter(allBooks)

                    recyclerView.adapter = bookAdapter
                }
            }
        })

        return view

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(id: String?) {
        for (i in allBooks) {
            if (i.idBook == id) {
                val addBook = Book(i.idBook, i.title, i.author)
                ref.child(addBook.idBook).setValue(addBook).addOnCompleteListener {
                    Toast.makeText(context, "Book has been added to read books", Toast.LENGTH_SHORT).show()
                }
            }
        }

//
//        if (book.) {
//
//        }
//        for (i in allBooks) {
//
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}

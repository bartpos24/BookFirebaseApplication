package com.example.firebaseapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Book
import com.example.firebaseapp.model.ReadBookAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : Fragment() {
    // IN THIS FRAGMENT DISPLAY BOOKS WHICH HAS BEEN READ

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookMenager: RecyclerView.LayoutManager
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    var readBooks = mutableListOf<Book>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_home, container, false)
        bookMenager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference(user.uid)

        recyclerView = root.findViewById(R.id.read_recyclerView)

        recyclerView.layoutManager = bookMenager

        loadReadBook()

        return root
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(id: String?) {
        for (i in readBooks) {
            if (i.idBook == id) {
                val book = Book(i.idBook, i.title, i.author)
                databaseReference.child(i.idBook).removeValue().addOnCompleteListener {
                    Toast.makeText(context, "Book has deleted from read books", Toast.LENGTH_SHORT).show()
                }
                readBooks.clear()
            }
        }
    }


    fun loadReadBook() {
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (h in p0.children) {
                        val book = h.getValue(Book::class.java)
                        readBooks.add(book!!)
                    }
                    val readBookAdapter = ReadBookAdapter(readBooks)

                    recyclerView.adapter = readBookAdapter

                }
            }
        })

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

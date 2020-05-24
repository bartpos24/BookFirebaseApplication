package com.example.firebaseapp.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.example.firebaseapp.R
import com.example.firebaseapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Account : Fragment() {

    private lateinit var emailText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_account,container, false)
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        emailText = view.findViewById(R.id.email_account)
        logoutButton = view.findViewById(R.id.logout)

        accountViewModel.text.observe(viewLifecycleOwner, Observer {
            emailText.text = user.email.toString()
        })
        logoutButton.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            auth.signOut()
            startActivity(intent)

        }


        return view
    }

}

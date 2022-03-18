package com.example.roomkotlinsample.view

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomkotlinsample.R
import com.example.roomkotlinsample.tools.Utils
import com.example.roomkotlinsample.viewmodel.ShareLocationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var email_text: AutoCompleteTextView
    private lateinit var add_email: Button
    private lateinit var emailsRecyclerView: RecyclerView
    private lateinit var emailItemAdapter: AdapterEmails

    private var emailsList: ArrayList<String> = arrayListOf()
    private lateinit var shareLocationViewModel: ShareLocationViewModel
    private var isEmailListLoaded = false
    private lateinit var user: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            user = bundle.getString("user") ?: ""
        }

        // Binding views
        email_text = findViewById(R.id.email_text)
        add_email = findViewById(R.id.add_email)
        emailsRecyclerView = findViewById(R.id.emailsRecyclerView)

        // View models
        shareLocationViewModel = ViewModelProvider(this).get(ShareLocationViewModel::class.java)
        shareLocationViewModel.setOwnerUser(user)
        shareLocationViewModel.init()

        // Setting recyclerview
        emailsRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        // Filling recyclerview
        emailItemAdapter = AdapterEmails(this, emailsList)
        emailsRecyclerView.adapter = emailItemAdapter
        emailItemAdapter.notifyDataSetChanged()

        // Set After Item Deleted Task
        emailItemAdapter.setAfterDeleteItemTask { checkEmailsAndSetButtons() }

        // Add listeners
        setUIListeners()

        // Data Binding for email list in shareLocationViewModel
        shareLocationViewModel.mUserEmails.observe(this, Observer {
            Utils().printLog("MainActivity: mUserEmails = ${it.toString()}")
            if(!isEmailListLoaded){
                isEmailListLoaded = true
                emailsList.clear()
                emailsList.addAll(it)
                emailItemAdapter.notifyDataSetChanged()
            }
        })

    }

    override fun onDestroy() {
        shareLocationViewModel.saveEmails(emailsList)
        super.onDestroy()
    }

    private fun setUIListeners() {

        // Add email button listener
        add_email.setOnClickListener {
            val newEmailString = email_text.text.toString()
            emailsList.add(newEmailString)
            email_text.setText("")

            Utils().printLog("MainActivity: email added: $newEmailString")
            Utils().printLog("MainActivity: Current list of emails: ${emailsList.toString()}")

            emailItemAdapter.notifyItemChanged(emailsList.size - 1)
        }

        // Add Email field listener: the email string should be correct to activate the AddEmail Button
        // Also, the Accept button status is configured
        email_text.addTextChangedListener {
            checkEmailsAndSetButtons()
        }

    }

    private fun checkEmailsAndSetButtons() {
        val email = email_text.text.toString()
        val isAnEmail = Utils().checkisEmail(email)
        val isInEmailList = emailsList.contains(email)
        val isEmptyField = email.isEmpty()
        val isEmptyEmailList = emailsList.size == 0

        // Setting the Add Email Button
        add_email.isEnabled = isAnEmail && (!isInEmailList)
    }

}
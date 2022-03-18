package com.example.roomkotlinsample.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.roomkotlinsample.model.roomdao.UserShareEmail
import com.example.roomkotlinsample.model.roomdao.UserShareEmailDatabase
import com.example.roomkotlinsample.tools.Utils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * ViewModel for emails, used by the user when it shares a location with other users. This class
 * can read and save emails using room.
 */
class ShareLocationViewModel(application: Application) : AndroidViewModel(application) {

    private var db: UserShareEmailDatabase? = null
    private var ownerUser: String = ""

    val mUserEmails = MutableLiveData<ArrayList<String>>()

    /**
     * Loads the emails list from database and puts in a live data list.
     */
    fun init() {

        doAsync {
            if(db == null){
                db = Room.databaseBuilder(
                    getApplication(),
                    UserShareEmailDatabase::class.java,
                    "LifeStyleDB"
                ).build()
            }

            db?.let{
                //val list = it.userEmailsDao().getAll().filter { r: UserShareEmail -> r.owner == ownerUser }
                val list = it.userEmailsDao().findByOwner(ownerUser)
                Utils().printLog("ShareLocationViewModel: print emails = ${userMails2ArrayOfStrings(list).toString()}")
                uiThread {
                    mUserEmails.value = userMails2ArrayOfStrings(list)
                }
            }
        }
    }

    /**
     * Save all the received emails and deletes the emails those which aren't in the DB list.
     *
     * @param emailsList: a list with emails
     * @param deleteOthers: flag to delete emails not contained in the received list
     */
    fun saveEmails(emailsList: ArrayList<String>, deleteOthers: Boolean = false) {
        doAsync {

            // Save emails in DB
            emailsList.forEach {
                var userShareEmail: UserShareEmail? = db?.userEmailsDao()?.findByEmailAndOwner(it, ownerUser)
                if (userShareEmail == null) {
                    userShareEmail = UserShareEmail()
                    userShareEmail.email = it
                    userShareEmail.owner = ownerUser
                    db?.userEmailsDao()?.insert(userShareEmail)
                    Utils().printLog("ShareLocationViewModel: Added email $it")

                }
                else {
                    Utils().printLog("ShareLocationViewModel: cannot add the email $it")
                }
            }

            // Search for not saved emails and delete these elements
            val savedEmails = db?.userEmailsDao()?.getAll()
                ?.filter{r: UserShareEmail -> r.owner == ownerUser}
                ?.map { it.email }
            savedEmails?.forEach {
                if(!emailsList.contains(it)){
                    deleteEmail(it)
                    Utils().printLog("ShareLocationViewModel: removing email $it")
                }
                else{
                    Utils().printLog("ShareLocationViewModel: conserving email $it")
                }
            }
        }
    }

    fun setOwnerUser(owner: String){
        this.ownerUser = owner
    }

    /**
     * Removes the received email from the database
     *
     * @param email: a string with the email
     */
    private fun deleteEmail(email: String) {
        // The email exists int the database
        var userShareEmail: UserShareEmail? = db?.userEmailsDao()?.findByEmailAndOwner(email, ownerUser)
        if (userShareEmail != null) {
            db?.userEmailsDao()?.delete(userShareEmail)

        } else {
            Utils().printLog("ShareLocationViewModel: cannot remove the email $email")
        }
    }

    /**
     * Coverts a list of UserShareEmail objects to an ArrayList of strings. The returned array is
     * sorted by descending using the id field of UserShareEmail.
     *
     * @param userEmailList: a list of UserShareEmail objects
     * @return an ArrayList<String> with only strings
     */
    private fun userMails2ArrayOfStrings(userEmailList: List<UserShareEmail>): ArrayList<String> {
        val array = arrayListOf<String>()
        array.addAll(
            userEmailList
                .sortedByDescending { it.id }
                .map { it.email }
        )

        userEmailList.sortedByDescending { it.id }.forEach {
            Utils().printLog("id = ${it.id}, email = ${it.email}")
        }
        return array
    }

}
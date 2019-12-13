package com.example.notely.ui.user

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notely.models.UserFileMetadata
import com.example.notely.models.UserUploads
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.coroutines.*

class UserViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _text = MutableLiveData<String>().apply {
        value = "This is user Fragment"
    }
    val text: LiveData<String>
        get() = _text
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?>
        get() = _user
    private  val _filesStored = MutableLiveData<Int>()
    val filesStored: LiveData<Int>
        get() = _filesStored
    private val _storageUsed = MutableLiveData<Long>()
    val storageUsed: LiveData<Long>
        get() = _storageUsed
    private val _urls = MutableLiveData<MutableList<String>>()
    val urls: LiveData<MutableList<String>>
        get() = _urls

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var metadataListener: ValueEventListener? = null
    private var uploadsListener: ValueEventListener? = null

    init {
        _user.value = auth.currentUser
        listenForDBChange()
        println("viewmodel initialized")
    }

    private fun listenForDBChange() {
        println("adding listener ...")
        val metaListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val meta: UserFileMetadata? = dataSnapshot.getValue(UserFileMetadata::class.java)
                _filesStored.value = meta?.filesStored ?: 0
                _storageUsed.value = meta?.storageUsed ?: 0
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database Error: ${databaseError.message}")
            }
        }
        val upldListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uploads: UserUploads? = dataSnapshot.getValue(UserUploads::class.java)
                _urls.value = uploads?.urls ?: mutableListOf()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("Database Error: ${databaseError.message}")
            }
        }
        println("current user = ${_user.value?.uid}")
        val uid = _user.value?.uid ?: return
        if ( metadataListener == null ) {
            metadataListener = db.child("metadata/$uid").addValueEventListener(metaListener)
            println("metadata listener added")
        } else println("metadata listener not added")
        if ( uploadsListener == null ) {
            uploadsListener = db.child("uploads/$uid").addValueEventListener(upldListener)
            println("uploads listener added")
        } else println("uploads listener not added")
    }

    override fun onCleared() {
        super.onCleared()
        removeListener()
    }

    private fun removeListener() {
        val uid = _user.value?.uid ?: return
        if ( metadataListener != null ) {
            db.child("metadata/$uid").removeEventListener(metadataListener!!)
            metadataListener = null
            println("removed metadata listener")
        }
        if ( uploadsListener != null ) {
            db.child("uploads/$uid").removeEventListener(uploadsListener!!)
            uploadsListener = null
            println("removed uploads listener")
        }
        println("viewmodel cleared")
    }

    fun setUpClient(webClientID: String, context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 9001

    fun signIn(frag: Fragment) {
        scope.launch {
            startActivity(frag)
        }
    }

    private suspend fun startActivity(frag: Fragment) {
        withContext(Dispatchers.IO) {
            val signInIntent = googleSignInClient.signInIntent
            // Fragment should override onActivityResult() to call userViewModel.firebaseAuthWithGoogle()
            frag.startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    fun signOut() {
        scope.launch {
            deAuth()
        }
        removeListener()
        _user.value = null
    }

    private suspend fun deAuth() {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                when ( task.isSuccessful ) {
                    true -> {
                        _user.value = auth.currentUser
                        listenForDBChange()
                    }
                    else -> _user.value = null
                }
            }
    }
}
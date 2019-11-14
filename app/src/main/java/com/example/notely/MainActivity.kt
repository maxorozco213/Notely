package com.example.notely

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1
    private val TAG = "Google Sign In Activity"
    private  val RC_SIGN_IN = 9001
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //root = findViewById(R.id.root)
        setContentView(R.layout.activity_main)

        val requestPermissions: Button  = findViewById(R.id.permissionButton)
        requestPermissions.setOnClickListener{this.checkPermissions()}

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = getClient(this, gso)
        // ...
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val btn = findViewById<SignInButton>(R.id.sign_in_button)
        btn.setOnClickListener{ signIn() }
        val out = findViewById<Button>(R.id.button)
        out.setOnClickListener { signOut() }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_SHORT).show()
        } else {
            this.sendPermissionRequest()
        }
    }

    private fun sendPermissionRequest() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This is needed")
                .setPositiveButton("Ok") { DialogInterface, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                    Log.i("Permissions", "User pressed OK")
                }
                .setNegativeButton("Cancel") { DialogInterface, which ->
                    Log.i("Permissions", "Storage permission denied")
                }
                .show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been denied by user")
                } else {
                    Log.i("", "Permission has been granted by user")
                }
            }
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        auth.signOut()
        updateUI(auth.currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
                // ...
                println("Google sign in failed")
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        println("firebaseAuthWithGoogle: " + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //Snackbar.make(, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val txt: TextView = findViewById(R.id.textView)
        txt.text = user?.displayName ?: "Not signed in"
    }


}
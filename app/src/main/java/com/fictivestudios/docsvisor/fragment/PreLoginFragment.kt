package com.fictivestudios.docsvisor.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.constants.LOGIN_TYPE
import com.fictivestudios.docsvisor.databinding.FragmentPreLoginBinding
import com.fictivestudios.docsvisor.widget.TitleBar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.helper.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider

class PreLoginFragment : BaseFragment() {

    lateinit var binding: FragmentPreLoginBinding
    lateinit var name: String

    private val RC_SIGN_IN = 234
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pre_login, container, false);


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1010995988404-b2halnni1m0lfessu6kb5mjsuc4nue6d.apps.googleusercontent.com")
            .requestEmail()
            .build()
        auth = FirebaseAuth.getInstance()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        YoYo.with(Techniques.FadeInDown)
            .duration(700)
            .repeat(0)
            .playOn(binding.linearLayoutCompatEmail);
        YoYo.with(Techniques.FadeInDown)
            .duration(1200)
            .repeat(0)
            .playOn(binding.linearLayoutCompatFb);
        YoYo.with(Techniques.FadeInDown)
            .duration(1400)
            .repeat(0)
            .playOn(binding.linearLayoutCompatGoogle);
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.setTitle("PRE SIGNUP")
    }

    override fun setListeners() {


        binding.linearLayoutCompatEmail.setOnClickListener {

            sharedPreferenceManager?.putValue(
                LOGIN_TYPE,
                ""
            )
            PreferenceUtils.saveBoolean(IS_SOCIAL_LOGIN,false)
            val bundle = bundleOf(
                "email" to LOGIN_TYPE)
            MainActivity.navController.navigate(R.id.selectUserFragment,bundle)
        /*    sharedPreferenceManager?.putValue(
                SELECT_USER,
                "Patient"
            )
            MainActivity.navController.navigate(R.id.loginFragment)*/


        }

        binding.linearLayoutCompatFb.setOnClickListener {
            sharedPreferenceManager?.putValue(
                LOGIN_TYPE,
                "Facebook"
            )
            callDialog(name = "Fb")
        }

        binding.linearLayoutCompatGoogle.setOnClickListener {
            sharedPreferenceManager?.putValue(
                LOGIN_TYPE,
                "Google"
            )
            PreferenceUtils.saveBoolean(IS_SOCIAL_LOGIN,true)
          //  callDialog(name = "Google")
            googleSignIn()
        }
    }

    private fun callDialog(name: String) {

        MainActivity.navController.navigate(R.id.selectUserFragment)
    }




    private fun googleSignIn() {
        //getting the google signin intent
        val signInIntent = mGoogleSignInClient!!.signInIntent
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode === RC_SIGN_IN) {
            // similar condition for facebook
            //Getting the GoogleSignIn Task
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                //authenticating with firebase
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
        // callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        AuthWithGoogle(acct)
    }
    private fun AuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()
            ) { task ->
                if (task.isSuccessful) {
                    val mUser = FirebaseAuth.getInstance().currentUser
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener(object : OnCompleteListener<GetTokenResult?> {
                            override fun onComplete(task: Task<GetTokenResult?>) {
                                val idToken: String = task.getResult()!!.getToken().toString()
                                Log.e("gdg", "Token:"+idToken)
                                Log.e("gdg", "Token:"+acct.idToken)


                                val bundle = bundleOf(
                                    LOGIN_TYPE to "google"
                                    ,SOCIAL_TOKEN to idToken
                                    , SOCIAL_NAME to acct.displayName
                                , EMAIL to acct.email
                                )
                                MainActivity.navController.navigate(R.id.selectUserFragment,bundle)

                             //   socialLogin(idToken,acct.displayName,"google")
                            }
                        })
                } else {
                    Toast.makeText(requireContext(), "" + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    Log.e("exception: ",""+ task.exception!!.localizedMessage)

                }
            }
    }



/*    private fun initFaceBook(){

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                Toast.makeText(requireContext(), "Please Try again", Toast.LENGTH_SHORT).show()
            }
            override fun onError(error: FacebookException) {
                Toast.makeText(requireContext(), "" +error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.e("gdg", "handleFacebookAccessToken:${token.token}")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val mUser = FirebaseAuth.getInstance().currentUser
                    mUser!!.getIdToken(true)
                        .addOnCompleteListener(object : OnCompleteListener<GetTokenResult?> {
                            override fun onComplete(task: Task<GetTokenResult?>) {
                                val idToken: String = task.getResult()!!.getToken().toString()
                                Log.e("gdg", "Token:"+idToken)
                                socialLogin(idToken,mUser.displayName,"facebook")
                            }
                        })
                } else {

                    Toast.makeText(requireContext(), "" +task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }    */

    companion object {

        fun newInstance(): PreLoginFragment {

            val args = Bundle()
            val fragment = PreLoginFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
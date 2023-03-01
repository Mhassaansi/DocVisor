package com.fictivestudios.docsvisor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.constants.*
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp


class MainActivity : BaseActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.contMain) as NavHostFragment
        navController = navHostFragment.navController
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            /*   .requestIdToken(getString(R.string.app_id))*/
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

    }


    override val view_Id: Int = R.layout.activity_main
    override val titlebarLayoutId: Int = R.id.titlebar
    override val drawerLayoutId: Int = R.id.drawer_layout
    override val dockableFragmentId: Int = R.id.contMain
    override val drawerFragmentId: Int = R.id.contDrawer

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

    }

    override fun onBackPressed() {
        navController.navigateUp()
        /**
         * Show Close app popup if no or single fragment is in stack. otherwise check if drawer is open. Close it..
         */
        /*  if (supportFragmentManager.backStackEntryCount > 1) {
              if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                  drawerLayout!!.closeDrawer(GravityCompat.START)
              } else {
                  super.onBackPressed()
                  val fragments = supportFragmentManager.fragments
                  val fragment = fragments[fragments.size - 1] as BaseFragment
                  fragment.setTitlebar(titleBar)
              }
          } else {
              moveTaskToBack(true)
          }*/
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val email: String? = account?.email
        val name = account?.displayName
        val userid = account?.id
        val token = account?.idToken
        var imUrl = ""
        /* imUrl = if (account?.photoUrl == null || account.photoUrl.toString()
                 .isEmpty()
         ) { //set default image
             WebServiceConstants.IMAGE_BASE_URL
         } else {
             account.photoUrl.toString() //photo_url is String
         }*/

        socialLogin(
            SOCIAL_MEDIA_PLATFORM_GOOGLE, token

        )

        /* userid,
              DEVICE_OS_ANDROID,
              email,
              SOCIAL_MEDIA_PLATFORM_GOOGLE,
              name,
              userid,
              "21534564", //sharedPreferenceManager!!.getString(AppConstants.KEY_FIREBASE_TOKEN)
              MODE, "google"*/
    }

    private fun socialLogin(
        loginType: String?,
        token: String?
    ) {

        /*
           val loginSendingModel = SocialLoginSendingModel()
           loginSendingModel.user_social_token = token
           loginSendingModel.user_social_type = loginType
           WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(
               PATH_SOCIAL_LOGIN,
               loginSendingModel.toString(),
               object : WebServices.IRequestWebResponseAnyObjectCallBack {
                   override fun requestDataResponse(webResponse: WebResponse<Any>) {

                       val userModelWrapper: UserModel = getGson()!!.fromJson(
                           getGson()!!.toJson(webResponse.result),
                           UserModel::class.java
                       )
                       sharedPreferenceManager?.putValue(
                           LOGINSIMPLEORSOCIAL,
                           SOCIALLOGIN
                       )

                       if (userModelWrapper.userIsProfileComplete == 0) {
                           val bundle = Bundle()
                           bundle.putInt("user_id", userModelWrapper.userId)
                           bundle.putString("token", webResponse.bearer_token)
                           bundle.putString("fragmentName", "CompleteProfile")
                           navController.navigate(R.id.completeProfileFragment, bundle)
                       } else {

                           sharedPreferenceManager?.putValue(
                               LOGINSIMPLEORSOCIAL,
                               ""
                           )

                           //userModelWrapper.userEmail = binding.txtEmail.text.toString()
                           sharedPreferenceManager?.putObject(KEY_CURRENT_USER_MODEL, userModelWrapper)
                           sharedPreferenceManager?.putValue(
                               KEY_CURRENT_USER_ID,
                               userModelWrapper.userId.toString()
                           )
                           sharedPreferenceManager?.putValue(
                               KEY_TOKEN,
                               webResponse.bearer_token
                           )
                           finish()
                           navController.navigate(R.id.homeActivity)

                       }

                   }

                   override fun onError(`object`: Any?) {}
               })*/
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: com.google.android.gms.tasks.Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.d("error", e.toString())
            }
        } else {
        }
    }


    fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        lateinit var navController: NavController
        var mainActivity = MainActivity()

    }

}
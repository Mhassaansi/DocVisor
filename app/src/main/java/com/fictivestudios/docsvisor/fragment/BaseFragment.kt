package com.fictivestudios.docsvisor.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.BaseActivity
import com.fictivestudios.docsvisor.activities.HomeActivity
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.activities.MainActivity
import com.fictivestudios.docsvisor.constants.KEY_TOKEN
import com.fictivestudios.docsvisor.enums.BaseURLTypes
import com.fictivestudios.docsvisor.helper.KeyboardHelper
import com.fictivestudios.docsvisor.helper.SharedPreferenceManager
import com.fictivestudios.docsvisor.helper.showToast
import com.fictivestudios.docsvisor.model.UserModel
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import java.util.*

abstract class BaseFragment : Fragment(), View.OnClickListener {

    protected val vieww: View? = null
    var sharedPreferenceManager: SharedPreferenceManager? = null
    var TAG = "Logging Tag"
    var onCreated = false
    var subscription: Disposable? = null


    /**
     * This is an abstract class, we should inherit our fragment from this class
     */
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = context?.let { SharedPreferenceManager.getInstance(it) }
        onCreated = false
    }

/*    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    }*/

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        vieww = inflater.inflate(getFragmentLayout(), container, false)
        return vieww
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBaseActivity().titleBar?.resetViews()
       getBaseActivity().drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) // Default Locked in this project
///       getBaseActivity()!!.drawerLayout!!.closeDrawer(GravityCompat.START)
        //subscribeToNewPacket(this)

        if (activity is HomeActivity) {
            getHomeActivity().hideBottomBar()
        }

        if (activity is HomeActivityPateint) {
            getHomeActivityPatient().hideBottomBar()
        }
    }


    fun getCurrentUser(): UserModel? {
        return sharedPreferenceManager?.getCurrentUser()
    }

    fun getToken(): String {
        return sharedPreferenceManager?.getString(KEY_TOKEN)!!
    }

    /*fun getOneTimeToken(): String? {
        return sharedPreferenceManager.getString(KEY_ONE_TIME_TOKEN)
    }

    fun putOneTimeToken(token: String?) {
        sharedPreferenceManager.putValue(AppConstants.KEY_ONE_TIME_TOKEN, token)
    }*/

    abstract fun getDrawerLockMode(): Int


    /*// Use  UIHelper.showSpinnerDialog
    @Deprecated("")
    fun setSpinner(
        adaptSpinner: ArrayAdapter<*>?,
        textView: TextView?,
        spinner: Spinner?
    ) {
        if (adaptSpinner == null || spinner == null) return
        //selected item will look like a spinner set from XML
//        simple_list_item_single_choice
        adaptSpinner.setDropDownViewResource(R.layout.simple_list_item_single_choice)
        spinner.adapter = adaptSpinner
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val str = spinner.getItemAtPosition(position).toString()
                if (textView != null) textView.text = str
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }*/


//    protected abstract fun getFragmentLayout(): Int

    fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }


    abstract fun setTitlebar(titleBar: TitleBar?)


    abstract fun setListeners()

    override fun onResume() {
        super.onResume()
        onCreated = true

        setListeners()
        if (getBaseActivity() != null) {
            setTitlebar(getBaseActivity().titleBar)
        }
        if (getBaseActivity() != null && getBaseActivity()!!.window.decorView != null) {
            KeyboardHelper.hideSoftKeyboard(
                getBaseActivity(),
                getBaseActivity().window.decorView
            )
        }
    }

    override fun onPause() {
        if (getBaseActivity() != null && getBaseActivity()!!.window.decorView != null) {
            KeyboardHelper.hideSoftKeyboard(
                getBaseActivity(),
                getBaseActivity()!!.window.decorView
            )
        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("abc", "onDestroyView")
        if (subscription != null) subscription!!.dispose()
    }


    fun showNextBuildToast() {
        showToast(context, "This feature is in progress")
    }

    fun onNewPacket(event: Int, data: Any?) {
        when (event) {
        }
    }

    fun logoutClick(baseFragment: BaseFragment) {
        /* val context: Context? = baseFragment.context
         val genericDialogFragment: GenericDialogFragment = GenericDialogFragment.newInstance()
         genericDialogFragment.setTitle("Logout")
         genericDialogFragment.setMessage(context.getString(R.string.areYouSureToLogout))
         genericDialogFragment.setButton1("Yes", object : GenericClickableInterface() {
             fun click() {
                 genericDialogFragment.dismiss()
                 baseFragment.sharedPreferenceManager.clearDB()
                 baseFragment.getBaseActivity()
                     ?.clearAllActivitiesExceptThis(MainActivity::class.java)
             }
         })
         genericDialogFragment.setButton2("No", object : GenericClickableInterface() {
             fun click() {
                 genericDialogFragment.getDialog().dismiss()
             }
         })
         genericDialogFragment.show(baseFragment.getBaseActivity()!!.supportFragmentManager, null)*/
    }

    override fun onClick(p0: View?) {

    }

    open fun getMainActivity(): MainActivity {
        return activity as MainActivity
    }

    open fun getHomeActivity(): HomeActivity {
        return activity as HomeActivity
    }

    open fun getHomeActivityPatient(): HomeActivityPateint {
        return activity as HomeActivityPateint
    }


    fun closeApp() {
        val dialog1 = Dialog(requireContext())
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_logout)
        dialog1.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog1.show()
        val txtYes = dialog1.findViewById<AppCompatTextView>(R.id.txtYes)
        val txtNo = dialog1.findViewById<AppCompatTextView>(R.id.txtNo)

        txtNo.setOnClickListener { dialog1.dismiss() }
        txtYes.setOnClickListener {
            sharedPreferenceManager!!.clearDB()
            dialog1.dismiss()

            activity?.finish()
            if (activity is HomeActivity)
            {
                HomeActivity.home.finish()
            }
            //getBaseActivity().openActivity(MainActivity::class.java)
        }
    }


}
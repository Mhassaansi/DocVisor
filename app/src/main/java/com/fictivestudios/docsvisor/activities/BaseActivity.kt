package com.fictivestudios.docsvisor.activities

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.fictivestudios.docsvisor.BaseApplication
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.apiManager.client.SessionManager
import com.fictivestudios.docsvisor.constants.JSON_STRING_KEY
import com.fictivestudios.docsvisor.fragment.BaseFragment
import com.fictivestudios.docsvisor.fragment.LeftSideMenuFragment
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.firebase.messaging.FirebaseMessaging


abstract class BaseActivity : AppCompatActivity() {

    var drawerLayout: DrawerLayout? = null
        protected set
    var titleBar: TitleBar? = null
        protected set
    var leftSideMenuFragment: LeftSideMenuFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        AutoStartPermissionHelper.getInstance().getAutoStartPermission(context);

        setContentView(view_Id)
        setAndBindTitleBar()
        drawerLayout = findViewById(drawerLayoutId)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)



       /* window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )*/
        //     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        addDrawerFragment()
        SessionManager(this)
        PreferenceUtils.init(this)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
    }

    /** window.setFlags
     * Give Resource id of the view you want to inflate
     *
     * @return
     */
    protected abstract val view_Id: Int
    protected abstract val titlebarLayoutId: Int
    protected abstract val drawerLayoutId: Int
    protected abstract val dockableFragmentId: Int
    protected abstract val drawerFragmentId: Int


    fun addDrawerFragment() {
        leftSideMenuFragment = LeftSideMenuFragment.newInstance();
        supportFragmentManager.beginTransaction()
            .replace(drawerFragmentId, leftSideMenuFragment!!).commit();
    }


    fun closeDrawer() {
        if(drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            drawerLayout!!.closeDrawer(GravityCompat.START)}
    }

    private fun setAndBindTitleBar() {
        titleBar = findViewById(titlebarLayoutId)
        titleBar?.visibility = View.GONE
        titleBar?.resetViews()
    }

    fun addDockableFragment(fragment: Fragment, isTransition: Boolean) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isTransition) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        fragmentTransaction.replace(dockableFragmentId, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    fun openActivity(tClass: Class<*>?) {
        val i = Intent(this, tClass)
        startActivity(i)
    }

    fun openImagePreviewActivity(url: String?, title: String?) {
/*        val i = Intent(this, ImagePreviewActivity::class.java)
        i.putExtra(AppConstants.IMAGE_PREVIEW_TITLE, title)
        i.putExtra(AppConstants.IMAGE_PREVIEW_URL, url)
        startActivity(i)*/
    }

    fun openActivity(tClass: Class<*>?, `object`: String?) {
        val i = Intent(this, tClass)
        i.putExtra(JSON_STRING_KEY, `object`)
        startActivity(i)
    }


    fun reload() {
        val intent = intent
        overridePendingTransition(0, 0)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
    }

    fun clearAllActivitiesExceptThis(cls: Class<*>?) {
        val intents = Intent(this, cls)
        intents.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intents)
        finish()
    }

    fun emptyBackStack() {
        val fm = supportFragmentManager ?: return
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    fun popBackStack() {
        if (supportFragmentManager == null) {
            return
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    fun popStackTill(stackNumber: Int) {
        val fm = supportFragmentManager ?: return
        for (i in stackNumber until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    fun popStackTill(tag: String?) {
        val fm = supportFragmentManager ?: return
        val backStackEntryCount = fm.backStackEntryCount
        for (i in backStackEntryCount - 1 downTo 1) {
            if (fm.getBackStackEntryAt(i).name.equals(tag, ignoreCase = true)) {
                return
            } else {
                fm.popBackStack()
            }
        }
    }

    fun notifyToAll(event: Int, data: Any) {
        BaseApplication.getPublishSubject()
            ?.onNext(Pair(event, data))
    }

    fun refreshFragment(fragment: BaseFragment) {
        popBackStack()
        addDockableFragment(fragment, false)
    }

    companion object {
        fun getDrawerLayout(baseActivity: BaseActivity): DrawerLayout? {
            return baseActivity.drawerLayout
        }


    }
}
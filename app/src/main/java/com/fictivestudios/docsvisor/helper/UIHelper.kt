package com.fictivestudios.docsvisor.helper

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream

fun showToast(context: Context?, message: String?) {
    if (context == null) {
        return
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showSnackbar(context: Activity, view: View, message: String) {
    if (context == null) {
        return
    }
    if (view != null) {
        val snackbar: Snackbar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        val snackBarView = snackbar.getView()
        // snackBarView.setBackgroundColor(Color.parseColor("#0B5C93"))
        snackBarView.setBackgroundColor(Color.parseColor("#7302DA"))
        snackbar.show()
    }
}

fun showLongToastInCenter(ctx: Context?, messageId: Int) {
    val toast = Toast.makeText(ctx, messageId, Toast.LENGTH_LONG)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

/*   fun showLongToastInCenter(ctx: Context?, message: String?) {
       var message = message
       message = Strings.nullToEmpty(message)
       val toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG)
       toast.setGravity(Gravity.CENTER, 0, 0)
       toast.show()
   }

   fun showShortToastInCenter(ctx: Context?, message: String?) {
       var message = message
       if (ctx == null) return
       message = Strings.nullToEmpty(message)
       val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
       toast.setGravity(Gravity.CENTER, 0, 0)
       toast.show()
   }*/

fun showShortToastInCenter(ctx: Context?, message: String) {
    val toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun showConnectionFailedToast(ctx: Context?) {
    //showLongToastInCenter(ctx, R.string.msg_connection_failed);
}

fun showConnectionErrorToast(ctx: Context?) {
    // showLongToastInCenter(ctx, R.string.msg_connection_error);
}


fun showAlertDialogWithCallback(
    message: String?,
    title: CharSequence?,
    onClickListener: DialogInterface.OnClickListener,
    context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setTitle(title)
        .setCancelable(true)
        .setNegativeButton("OK", { dialogInterface, i ->
            dialogInterface.cancel()
            onClickListener.onClick(dialogInterface, i)
        })
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun showAlertDialog1(
    message: String?, title: String,
    onClickListener: DialogInterface.OnClickListener?, context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("Ok", onClickListener)
    if (!title.isEmpty()) builder.setTitle(title)
    val alert: AlertDialog = builder.create()
    alert.show()
}


fun showAlertDialog(context: Context?, message: String?) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setTitle("Alert")
        .setCancelable(true)
        .setNegativeButton("OK",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun showAlertDialog(
    message: String?, title: CharSequence?,
    context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage(message)
        .setTitle(title)
        .setCancelable(true)
        .setNegativeButton("OK",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun showAlertDialog(
    message: String?, title: String,
    onClickListener: DialogInterface.OnClickListener?, context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage(message)
        .setCancelable(false)
        .setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        .setPositiveButton("Yes", onClickListener)
    if (!title.isEmpty()) builder.setTitle(title)
    val alert: AlertDialog = builder.create()
    alert.show()
}


/**
 * get Color lightness, if you have color in HEX, then parse it using Color.parse(hexColor)
 *
 * @param color
 * @return
 */
fun getColorLightness(color: Int): Float {
    val red: Int = Color.red(color)
    val green: Int = Color.green(color)
    val blue: Int = Color.blue(color)
    val hsl = FloatArray(3)
    ColorUtils.RGBToHSL(red, green, blue, hsl)
    return hsl[2]
}


fun showDialogWithView(
    view: View?, message: String?, title: String,
    positiveText: String?, onPositiveClickListener: DialogInterface.OnClickListener?,
    context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage(message)
        .setCancelable(true)
        .setView(view)
        .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        .setPositiveButton(positiveText, onPositiveClickListener)
    if (!title.isEmpty()) builder.setTitle(title)
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun showAlertDialogThrice(
    addNetralButton: Boolean,
    message: String?, title: String,
    onNeutralClickListener: DialogInterface.OnClickListener?,
    onPositiveClickListener: DialogInterface.OnClickListener?,
    neutralText: String?, positiveText: String?,
    context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage(message)
        .setCancelable(true)
        .setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        .setPositiveButton(positiveText, onPositiveClickListener)
    if (addNetralButton) builder.setNeutralButton(neutralText, onNeutralClickListener)
    if (!title.isEmpty()) builder.setTitle(title)
    val alert: AlertDialog = builder.create()
    alert.show()
}


fun showAlertDialog(
    message: String?, title: String,
    onPositiveClickListener: DialogInterface.OnClickListener?, positiveText: String?,
    onNegativeClickListener: DialogInterface.OnClickListener?, negativeText: String?,
    context: Context?
) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder
        .setMessage(message)
        .setCancelable(true)
        .setNegativeButton(negativeText, onNegativeClickListener)
        .setPositiveButton(positiveText, onPositiveClickListener)
    if (!title.isEmpty()) builder.setTitle(title)
    val alert: AlertDialog = builder.create()
    alert.show()
}

fun showCheckedDialogBox(
    mContext: Context?, mTitle: String?,
    privacyArray: Array<String?>?, selectedIndex: Int,
    onClickListener: DialogInterface.OnClickListener?
) {
    val dialog: AlertDialog = AlertDialog.Builder(mContext)
        .setTitle(mTitle)
        .setSingleChoiceItems(
            privacyArray,
            selectedIndex, null
        )
        .setPositiveButton(
            "OK", onClickListener
        ).setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> //  Your code when user clicked on Cancel
                dialog.dismiss()
            }).create()
    dialog.show()
}

fun showListDialogBox(
    mContext: Context?, mTitle: String?,
    Array: Array<String?>?,
    onClickListener: DialogInterface.OnClickListener
) {
    val dialog: AlertDialog = AlertDialog.Builder(mContext)
        .setTitle(mTitle)
        .setItems(Array, DialogInterface.OnClickListener { dialog, which ->
            onClickListener.onClick(dialog, which)
            dialog.dismiss()
        })
        .setPositiveButton(
            "OK", onClickListener
        ).setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id ->
                //  Your code when user clicked on Cancel
            }).create()
    dialog.show()
}

fun showListDialogBox(
    mContext: Context?, Array: Array<String?>?,
    onClickListener: DialogInterface.OnClickListener
) {
    val dialog: AlertDialog = AlertDialog.Builder(mContext)
        .setItems(Array, DialogInterface.OnClickListener { dialog, which ->
            onClickListener.onClick(dialog, which)
            dialog.dismiss()
        })
        .create()
    dialog.show()
}


fun locateView(v: View?): Rect? {
    val loc_int = IntArray(2)
    if (v == null) return null
    try {
        v.getLocationOnScreen(loc_int)
    } catch (npe: NullPointerException) {
        // Happens when the view doesn't exist on screen anymore.
        return null
    }
    val location = Rect()
    location.left = loc_int[0]
    location.top = loc_int[1]
    location.right = location.left + v.getWidth()
    location.bottom = location.top + v.getHeight()
    return location
}

fun textMarquee(txtView: TextView) {
    txtView.ellipsize = TextUtils.TruncateAt.END
}

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public static int getScreenWidth(Activity ctx) {
//        Display display = ctx.getWindowManager().getDefaultDisplay();
//
//        if (OSHelper.hasHoneycombMR2()) {
//            Point size = new Point();
//            display.getSize(size);
//            return size.x;
//        } else {
//            return display.getWidth();
//        }
//
//    }

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    public static int getScreenWidth(Activity ctx) {
//        Display display = ctx.getWindowManager().getDefaultDisplay();
//
//        if (OSHelper.hasHoneycombMR2()) {
//            Point size = new Point();
//            display.getSize(size);
//            return size.x;
//        } else {
//            return display.getWidth();
//        }
//
//    }
fun dimBehind(dialog: Dialog) {
    val lp: WindowManager.LayoutParams = dialog.window!!.attributes
    lp.dimAmount = 0.9f
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    dialog.setCancelable(false)
}

//    public static DisplayImageOptions getRoundedCornerImage() {
//        DisplayImageOptions displayOptions;
//        displayOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.img_fav_pic)
//                .showImageOnFail(R.drawable.img_fav_pic)
//                .showStubImage(R.drawable.img_fav_pic)
//                .cacheInMemory(true)
//                .displayer(new RoundedBitmapDisplayer(10))
//                .cacheOnDisc(true)
//                .build();
//        return displayOptions;
//    }

//    public static DisplayImageOptions getRoundedCornerImage() {
//        DisplayImageOptions displayOptions;
//        displayOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.drawable.img_fav_pic)
//                .showImageOnFail(R.drawable.img_fav_pic)
//                .showStubImage(R.drawable.img_fav_pic)
//                .cacheInMemory(true)
//                .displayer(new RoundedBitmapDisplayer(10))
//                .cacheOnDisc(true)
//                .build();
//        return displayOptions;
//    }
fun releaseFocus(view: View) {
    var parent: ViewParent? = view.getParent()
    var group: ViewGroup? = null
    var child: View? = null
    while (parent != null) {
        if (parent is ViewGroup) {
            group = parent
            for (i in 0 until group!!.childCount) {
                child = group.getChildAt(i)
                if (child !== view && child.isFocusable()) child.requestFocus()
            }
        }
        parent = parent.parent
    }
}

private fun getFilename(): String {
    val file =
        File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file.getAbsolutePath()
        .toString() + "/" + System.currentTimeMillis() + ".jpg"
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val heightRatio =
            Math.round(height.toFloat() / reqHeight.toFloat())
        val widthRatio =
            Math.round(width.toFloat() / reqWidth.toFloat())
        inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
    }
    val totalPixels = width * height.toFloat()
    val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
    while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
        inSampleSize++
    }
    return inSampleSize
}

fun addSpaces(count: Int): String? {
    var s = ""
    for (i in 0 until count) {
        s += " "
    }
    return s
}


fun statusBarHeight(resources: Resources): Int {
    var statusBarHeight = 0
    val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}

fun actionBarHeight(activity: Activity): Int {
    // action bar height
    var actionBarHeight = 0
    val styledAttributes =
        activity.theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
    actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return actionBarHeight
}

fun navigationBarHeight(resources: Resources): Int {
    // navigation bar height
    var navigationBarHeight = 0
    val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return navigationBarHeight
}

var TAG_PIC_INFO = "PIC INFO"

fun saveToInternalSorage(
    context: Context?,
    bitmapImage: Bitmap,
    folderName: String?,
    fileName: String?
): File? {
    val cw = ContextWrapper(context)
    val directory: File = cw.getDir(folderName, Context.MODE_PRIVATE)
    val mypath = File(directory, fileName)
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(mypath)

        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    Log.d(
        TAG_PIC_INFO,
        "saveToInternalSorage: directory.getPath()         ->" + directory.getPath()
    )
    Log.d(
        TAG_PIC_INFO,
        "saveToInternalSorage: mypath.getPath()            ->" + mypath.getPath()
    )
    Log.d(
        TAG_PIC_INFO,
        "saveToInternalSorage: mypath.getAbsolutePath()    ->" + mypath.getAbsolutePath()
    )
    return mypath
}


/**
 * library dependednt
 */
fun getProgressHUD(context: Context): KProgressHUD? {
    val progressBar = ProgressBar(context, null, R.attr.progressBarStyle)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        progressBar.indeterminateTintList = ColorStateList.valueOf(
            context.getResources().getColor(R.color.white)
        )
    }
    val progressHUD: KProgressHUD = KProgressHUD.create(context)
    //                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
    progressHUD
        .setCancellable(false)
        .setCustomView(progressBar)
        .setLabel("Loading...", context.resources.getColor(R.color.white))
    //                .setBackgroundColor(getContext().getResources().getColor(R.color.c_white))
//                .setAnimationSpeed(2)
//                .setDimAmount(0.8f)
//                .setGraceTime(500)
    return progressHUD
}

fun cropImagePicker(
    context: Context,
    fragment: Fragment
) {
    CropImage.activity()
        .setGuidelines(CropImageView.Guidelines.ON)
        .setAspectRatio(1, 1)
        .setFixAspectRatio(true)
        .setCropShape(CropImageView.CropShape.RECTANGLE)
        .setMinCropWindowSize(192, 192)
        .setMinCropResultSize(192, 192)
        .setMultiTouchEnabled(false)
        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG) // FIXME: 15-Jul-17 Fix Quality if required
        .setRequestedSize(640, 640, CropImageView.RequestSizeOptions.RESIZE_FIT)
        .setOutputCompressQuality(100)
        .start(context, fragment)
}


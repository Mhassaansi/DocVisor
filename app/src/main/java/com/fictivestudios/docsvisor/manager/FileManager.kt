package com.fictivestudios.docsvisor.manager

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.fictivestudios.docsvisor.constants.ROOT_PATH
import com.fictivestudios.docsvisor.helper.showToast
import java.io.*
import java.net.URLEncoder
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.util.*

/**
 * Created by muhammadmuzammil on 4/26/2017.
 */
object FileManager {
    private fun createDirectory(directory: String) {
        /*First check if root directory not created then create*/
        val rootDirectory = File(ROOT_PATH)
        if (!rootDirectory.exists()) rootDirectory.mkdirs()
        val innerDirectory = File(directory)
        if (!innerDirectory.exists()) innerDirectory.mkdir()
    }

    //
    //    /**
    //     * Avatar is the location on which it saved picture
    //     *
    //     * @param avatar
    //     * @param thumbnail
    //     * @return
    //     */
    //    public static File createProfileImage(String avatar, boolean thumbnail, Context context) {
    //        if (avatar == null || avatar.equals(""))
    //            return null;
    //
    //        try {
    //            avatar = avatar.substring(0, avatar.lastIndexOf(".")) + ".j";
    //        } catch (Exception e) {
    ////            e.printStackTrace();
    //            return null;
    //        }
    //        ContextWrapper cw = new ContextWrapper(context);
    //        File directory;
    //
    //        if (thumbnail)
    //            directory = cw.getDir("userProfile", Context.MODE_PRIVATE);
    //        else
    //            directory = cw.getCacheDir();
    //
    //        if (!directory.exists()) {
    //            directory.mkdir();
    //        }
    //
    //        String filename = URLUtil.guessFileName(avatar, null, null);
    //        File imageFile = new File(directory, filename);
    //        if (!imageFile.exists())
    //            try {
    //                imageFile.createNewFile();
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //        return imageFile;
    //    }
    //
    //    public static File getUserImage(String avatar, boolean thumbnail, Context context) {
    //        if (avatar == null || avatar.equals(""))
    //            return null;
    //
    //        // FIXME: 8/18/2017  :  REMOVE NULL POINTER EXCEPTION // CONVERT TO JPG
    //        try {
    //            avatar = avatar.substring(0, avatar.lastIndexOf(".")) + ".j";
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //        ContextWrapper cw = new ContextWrapper(context);
    //        File directory;
    //
    //        if (thumbnail) {
    //            directory = cw.getDir(AppConstants.USER_PROFILE_PICTURE_FOLDER_DIRECTORY, Context.MODE_PRIVATE);
    //        } else {
    //            directory = cw.getCacheDir();
    //            avatar = avatar.replace(AppConstants.SUFFIX_THUMB_IMAGE, "");
    //        }
    //
    //        if (!directory.exists()) {
    //            return null;
    //        }
    //        String filename = URLUtil.guessFileName(avatar, null, null);
    //        File imageFile = new File(directory, filename);
    //        if (!imageFile.exists())
    //            return null;
    //        return imageFile;
    //    }
    //
    //    public static File getMyImage(boolean isThumbnail, Context context) {
    //        ContextWrapper cw = new ContextWrapper(context);
    //        if (isThumbnail)
    //            return new File(cw.getDir(AppConstants.USER_PROFILE_PICTURE_FOLDER_DIRECTORY, Context.MODE_PRIVATE), AppConstants.USER_PROFILE_THUMBNAIL_NAME);
    //        else
    //            return new File(cw.getDir(AppConstants.USER_PROFILE_PICTURE_F OLDER_DIRECTORY, Context.MODE_PRIVATE), AppConstants.USER_PROFILE_PICTURE_NAME);
    //    }
    //    public static File getUserImage(String avatar, boolean thumbnail, Context context) {
    //        if (avatar == null || avatar.equals(""))
    //            return null;
    //
    //        // FIXME: 8/18/2017  :  REMOVE NULL POINTER EXCEPTION // CONVERT TO JPG
    //        try {
    //            avatar = avatar.substring(0, avatar.lastIndexOf(".")) + ".j";
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //        ContextWrapper cw = new ContextWrapper(context);
    //        File directory;
    //
    //        if (thumbnail) {
    //            directory = cw.getDir(AppConstants.USER_PROFILE_PICTURE_FOLDER_DIRECTORY, Context.MODE_PRIVATE);
    //        } else {
    //            directory = cw.getCacheDir();
    //            avatar = avatar.replace(AppConstants.SUFFIX_THUMB_IMAGE, "");
    //        }
    //
    //        if (!directory.exists()) {
    //            return null;
    //        }
    //        String filename = URLUtil.guessFileName(avatar, null, null);
    //        File imageFile = new File(directory, filename);
    //        if (!imageFile.exists())
    //            return null;
    //        return imageFile;
    //    }
    fun writeResponseBodyToDisk(
        context: Context?,
        body: String?,
        fileName: String,
        directoryPath: String,
        isSaveInCache: Boolean,
        isSaveInExternalCache: Boolean
    ): String {
        val file: File
        if (isSaveInCache) {
            val cw = ContextWrapper(context)
            val directory: File?
            directory = if (isSaveInExternalCache) {
                cw.externalCacheDir
            } else {
                cw.cacheDir
            }
            if (!directory!!.exists()) {
                directory.mkdir()
            }
            file = File(directory, fileName)
        } else {
            createDirectory(directoryPath)
            file = File(
                directoryPath
                        + "/" + fileName
            )
        }
        val pdfAsBytes = Base64.decode(body, 0)
        val os: FileOutputStream
        try {
            os = FileOutputStream(file, false)
            os.write(pdfAsBytes)
            os.flush()
            os.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    fun isFileExits(path: String?): Boolean {
        return File(path).exists()
    }

    fun copyFile(sourcePath: String?, destFile: File?) {
        try {
            val sourceFile = File(sourcePath)
            //        File destFile = new File(destPath);
            if (!sourceFile.exists()) {
                return
            }
            var source: FileChannel? = null
            var destination: FileChannel? = null
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            if (source != null) {
                destination.transferFrom(source, 0, source.size())
            }
            source?.close()
            destination.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun copyImage(sourcePath: String?, targetPath: String?): File? {
        return try {
            val `in`: InputStream = FileInputStream(sourcePath)
            val out: OutputStream = FileOutputStream(targetPath)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            `in`.close()
            out.close()
            File(targetPath)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getFileNameFromPath(path: String): String {
        val index = path.lastIndexOf("/")
        return if (index > path.length) "Unknown File" else path.substring(index + 1)
    }

    fun copyFile(inputPath: String?, outputPath: String?) {
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {

            //create output directory if it doesn't exist
            val dir = File(outputPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            `in` = FileInputStream(inputPath)
            out = FileOutputStream(outputPath)
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            `in`.close()
            `in` = null

            // write the output file (You have now copied the file)
            out.flush()
            out.close()
            out = null
        } catch (fnfe1: FileNotFoundException) {
            //   LogUtil.e("tag", fnfe1.getMessage());
        } catch (e: Exception) {
//         /   LogUtil.e("tag", e.getMessage());
        }
    }

    fun getFileSize(path: String?): String {
        val file = File(path)
        return if (!file.exists()) "0 KB" else formatFileSize(file.length())
    }

    fun formatFileSize(size: Long): String {
        var hrSize: String? = null
        val b = size.toDouble()
        val k = size / 1024.0
        val m = size / 1024.0 / 1024.0
        val g = size / 1024.0 / 1024.0 / 1024.0
        val t = size / 1024.0 / 1024.0 / 1024.0 / 1024.0
        val dec = DecimalFormat("0.0")
        hrSize = if (t > 1) {
            dec.format(t) + " TB"
        } else if (g > 1) {
            dec.format(g) + " GB"
        } else if (m > 1) {
            dec.format(m) + " MB"
        } else if (k > 1) {
            dec.format(k) + " KB"
        } else {
            dec.format(b) + " Bytes"
        }
        return hrSize
    }

    @JvmStatic
    fun getExtension(fileName: String?): String {
        val encoded: String?
        encoded = try {
            URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")
        } catch (e: UnsupportedEncodingException) {
            fileName
        }
        return MimeTypeMap.getFileExtensionFromUrl(encoded).toLowerCase()
    }

    fun createFileInAppDirectory(
        context: Context?,
        folderName: String?,
        fileName: String?
    ): File {
        val cw = ContextWrapper(context)
        val directory = cw.getDir(folderName, Context.MODE_PRIVATE)
        return File(directory, fileName)
    }

    fun openFile(context: Context, url: File?) {
        if (url == null) {
            showToast(context, "File is null")
            return
        }
        try {
            val uri = Uri.fromFile(url)
            val intent =
                Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                //intent.setDataAndType(uri, "application/pdf");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val apkURI = FileProvider.getUriForFile(
                        context.applicationContext,
                        context.packageName + ".provider",
                        url
                    )
                    intent.setDataAndType(apkURI, "application/pdf")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    intent.setDataAndType(Uri.fromFile(url), "application/pdf")
                }
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav")
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (url.toString().contains(".jpg") || url.toString()
                    .contains(".jpeg") || url.toString().contains(".png")
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                url.toString().contains(".mpeg") || url.toString()
                    .contains(".mpe") || url.toString().contains(".mp4") || url.toString()
                    .contains(".avi")
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "No application found which can open the file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getFiles(DirectoryPath: String?): ArrayList<File> {
        val f = File(DirectoryPath)
        f.mkdirs()
        val arrayList = ArrayList<File>()
        arrayList.addAll(Arrays.asList(*f.listFiles()))
        return arrayList
    }

    fun getReplacedSlash(Value: String): String {
        return Value.replace("\\", "/")
    }
}
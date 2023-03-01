package com.fictivestudios.docsvisor.fragment

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.adapter.ConversationAdapter
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.response.ChatMediaResponse
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentChatBinding
import com.fictivestudios.docsvisor.helper.*
import com.fictivestudios.docsvisor.model.ChatItem
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.gigo.clean.networksetup.socket.SocketApp
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.Ack
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File


class MessengerFragment : BaseFragment() {
    var chatAdapter: ConversationAdapter? = null
    var messageList: ArrayList<ChatItem>? = null
    private var binding: FragmentChatBinding? = null
    var currentUserId:String=""
    var receiverUserId:String=""
    var receiverUserName:String=""
    var socket:Socket?=null
    var isAttachment = false
    val PERMISSION_REQ_ID_STORAGE =15
    val REQUEST_FILE =18

    private var fileTemporaryProfilePicture: File? = null

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)

        messageList = ArrayList()


        var gson =  Gson()
        var json:String= PreferenceUtils.getString(USER_OBJECT)
        var user=gson.fromJson(json, User::class.java)

        currentUserId = user?.id.toString()

        Log.d("args",arguments?.getString("receiverUserId").toString())
        receiverUserId = arguments?.getString("receiverUserId").toString()

        Log.d("args",arguments?.getString("username").toString())
        receiverUserName = arguments?.getString("username").toString()

        if (!arguments?.getString("receiverUserId").toString().isNullOrEmpty() || arguments?.getString("receiverUserId").toString() !="null")
        {
            PreferenceUtils.saveString("chatReceiverId",receiverUserId)
            PreferenceUtils.saveString("chatReceiverName",receiverUserName)

        }

        else
        {
            receiverUserId =   PreferenceUtils.getString("chatReceiverId")
            receiverUserName = PreferenceUtils.getString("chatReceiverName")
        }


        getSocket()
        return binding!!.root

    }

    private fun getSocket() {
        if(socket==null) {
            val app: SocketApp = SocketApp.instance!!
            socket = app.socket
            socket!!.on(Socket.EVENT_CONNECT, onConnect)
            socket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            socket!!.on("response", receiveMessage)
            socket!!.connect()
        }
    }
    private val onConnect = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            var toast: Toast? = null
            Log.e("Socket-onConnect", "Socket-onConnect")
             //   toast = Toast.makeText(this, "Connected", Toast.LENGTH_SHORT)
            //    toast.show()
            try {
                getMessage()

            }
            catch (e:java.lang.Exception){
                e.printStackTrace()
            }
        }
    }
    private val onConnectError = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            Log.e("Socket-onConnectError", "Socket-onConnectError"+it[0].toString())

            activity?.runOnUiThread {
               // Toast.makeText(requireContext(), it[0].toString(), Toast.LENGTH_SHORT).show()
            }


        }
    }
    private val onDisconnect = Emitter.Listener {
        Handler(Looper.getMainLooper()).post {
            var toast: Toast? = null
            //  Log.e("Socket-Disconnected", "Socket-Disconnected")
            //  toast = Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT)
            //   toast.show()
        }
    }
    private val receiveMessage = Emitter.Listener { arg ->
        Handler(Looper.getMainLooper()).post {
            val data = arg[0] as JSONObject
            Log.e("receiveMessage", "" + data.getString("data"))
            val gson = Gson()
            val listFromGson: ArrayList<ChatItem> = gson.fromJson(
                data.getString("data"),
                object : TypeToken<ArrayList<ChatItem?>?>() {}.type
            )
            if (listFromGson.size != 1)
            {
                messageList = ArrayList()
                messageList = listFromGson
                if (!messageList.isNullOrEmpty())
                {
                    chatAdapter = context?.let { ConversationAdapter(it, messageList!!,currentUserId) }
                    binding?.chatList?.adapter = chatAdapter
                    binding?.chatList?.adapter?.notifyDataSetChanged()
                }

                Log.e("receiveMessage", listFromGson.toString())
            }
            else{
                messageList?.add(listFromGson.get(0))

                if (chatAdapter == null)
                {
                    chatAdapter = context?.let { ConversationAdapter(it, messageList!!,currentUserId) }
                    binding?.chatList?.adapter = chatAdapter
                    binding?.chatList?.adapter?.notifyDataSetChanged()


                }
                else{
                    chatAdapter?.notifyItemChanged(messageList!!.size - 1)
                    binding?.chatList!!.smoothScrollToPosition(messageList!!.size - 1);
                }

            }

        }
    }


    fun offSocket(){
        try {
            if(socket!=null && socket!!.connected()) {
                socket!!.off(Socket.EVENT_CONNECT, onConnect)
                socket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
                socket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
                socket!!.off("response", receiveMessage)
                socket!!.disconnect()
            }
        }
        catch (e :Exception){
            e.printStackTrace()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.stackFromEnd = true
        binding!!.chatList.layoutManager = linearLayoutManager
/*
        binding!!.chatList.adapter = chatAdapter

        val model1 = ChatModel("hi", "", 0)
        chatAdapter!!.updateList(model1)
        val model2 = ChatModel("Hello", "", 1)
        chatAdapter!!.updateList(model2)
        val model3 = ChatModel("How are you?", "", 0)
        chatAdapter!!.updateList(model3)
        val model4 = ChatModel("hi", "", 0)
        chatAdapter!!.updateList(model4)
        val model5 = ChatModel("Hello", "", 1)
        chatAdapter!!.updateList(model5)
*/

    }


    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.VISIBLE
        titleBar?.hideBottomBar(requireActivity())
        if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor") {
            titleBar?.showBackButtonChat(activity, receiverUserName,receiverUserId,"Doctor")

        } else {
            titleBar?.showBackButtonChat(activity, receiverUserName,receiverUserId,"Patient")

        }
    }



   private fun getMessage() {
        var parameterName = ChatItem(currentUserId, receiverUserId)
//        Log.e("GetMessage", "userData.toString() " + userData.id)
  //      Log.e("GetMessage", "navArgs.value.bookingId " + navArgs.value.bookingId)
        var jsonObject: JSONObject? = null
        val gson: Gson? = Gson()
        if (gson != null) {
            jsonObject = JSONObject(gson.toJson(parameterName))
        }
       if (socket != null && socket!!.connected()) {
           socket!!.emit("get_messages", jsonObject, Ack() {
           });
           Log.e("get_messages", "jsonObject" + jsonObject.toString())
       } else {
          // Toast.makeText(activity, "Not Connect ,Please reload this screen", Toast.LENGTH_LONG).show()
       }

    }





    override fun setListeners() {

          binding!!.btnSendBtn .setOnClickListener {
              if (binding?.etWrite?.text?.toString().isNullOrEmpty())
              {

              }
              else
              {
                  isAttachment = false
                  binding?.layAttachment?.visibility = View.GONE
                  sendMessage(binding?.etWrite?.text.toString(), MESSAGE_TYPE_TEXT)
              }



          }

        binding?.btnAttach?.setOnClickListener {


            if (isAttachment)
            {
                isAttachment = false
               binding?.layAttachment?.visibility = View.GONE
            }
            else
            {
                isAttachment = true
               binding?.layAttachment?.visibility = View.VISIBLE
            }




        }

        binding?.btnPhotos?.setOnClickListener {
            isAttachment = false
            binding?.layAttachment?.visibility = View.GONE
            openImagePicker()
        }
    /*    binding?.btnDocument?.setOnClickListener {
            isAttachment = false
            binding?.layAttachment?.visibility = View.GONE
            //filePicker()

            browseDocuments()

        }*/

    }


    private fun sendMessage(message:String,messageType:String)
    {

        val model = ChatItem(message,currentUserId, receiverUserId, messageType)

//        Log.e("GetMessage", "userData.toString() " + userData.id)
//      Log.e("GetMessage", "navArgs.value.bookingId " + navArgs.value.bookingId)
        var jsonObject: JSONObject? = null
        val gson: Gson? = Gson()
        if (gson != null) {
            jsonObject= JSONObject(gson.toJson(model))
        }
        if (socket != null && socket!!.connected()) {

            binding?.etWrite?.setText("")

            socket!!.emit("send_message", jsonObject, Ack() {
            });
            Log.e("send_message", "jsonObject" + jsonObject.toString())
        } else {
            Toast.makeText(activity, "Not Connect ,Please reload this screen", Toast.LENGTH_LONG).show()
        }
    }

    private fun chatMedia()
    {
/*        binding.pbEdit.visibility=View.VISIBLE
        binding.btnSignUp.isEnabled=false
        binding.btnSignUp.text=""*/


        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())




        var part : MultipartBody.Part? = null
        if (fileTemporaryProfilePicture != null){
            part = fileTemporaryProfilePicture!!.getPartMap("media")
        }

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.chatMedia(part).enqueue(object: retrofit2.Callback<ChatMediaResponse> {
                override fun onResponse(
                    call: Call<ChatMediaResponse>,
                    response: Response<ChatMediaResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
/*                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="UPDATE"*/

                    })

                    try {


                        val response: ChatMediaResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("response", response.message)
                            Log.d("response", response.data.toString())

                            sendMessage(response.data.image, MESSAGE_TYPE_MEDIA)

/*                            val gson = Gson()
                            val json:String = gson.toJson(response.data )
                            PreferenceUtils.saveString(USER_OBJECT,json)*/




                        }
                        else {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {

                    }
                }

                override fun onFailure(call: Call<ChatMediaResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
/*                        binding.pbEdit.visibility=View.GONE
                        binding.btnSignUp.isEnabled=true
                        binding.btnSignUp.text="UPDATE"*/

                    })
                }
            })

        }


    }


    private fun openImagePicker()
    {    ImagePicker.with(this)
        .crop()	    			//Crop image(Optional), Check Customization for more option
        .compress(1024)			//Final image size will be less than 1 MB(Optional)
        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
        .start()

    }

    fun filePicker()
    {
        if (checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,PERMISSION_REQ_ID_STORAGE
            )
        ) {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, REQUEST_FILE)

        }





            }

    private fun browseDocuments() {
        val mimeTypes = arrayOf(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
            "text/plain",
            "application/pdf",
            "application/zip"
        )
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.size > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_FILE)
    }


    companion object {

        fun newInstance(): MessengerFragment {
            val args = Bundle()
            val fragment = MessengerFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onDestroy() {
        try {
            offSocket()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
        getMessage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
/*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val uri = compressImage(result.uri)//File(result.uri.path)
                fileTemporaryProfilePicture = File(uri.path!!)
                //\                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
                setImageAfterResult(result.uri.toString())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
        }
*/


        //val result = CropImage.getActivityResult(data)

        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            //  val uri = compressImage(result.uri)//File(result.uri.path)

            if (requestCode == REQUEST_FILE)
            {
                fileTemporaryProfilePicture = File(uri.path!!)


                chatMedia()
            }
            else
            {
                fileTemporaryProfilePicture = File(uri.path!!)

                chatMedia()
            }


            /*activity?.runOnUiThread {
                binding.img.setImageURI(uri)
                binding.imgCamera.visibility=View.GONE
            }*/

            //\                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
            // setImageAfterResult(result.uri.toString())

        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }

    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i("permission",
            "checkSelfPermission $permission $requestCode"
        )
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            )
            !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(permission),
                requestCode
            )
            return false
        }
        return true
    }




}
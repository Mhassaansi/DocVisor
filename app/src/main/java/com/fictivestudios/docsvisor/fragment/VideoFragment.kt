package com.fictivestudios.docsvisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.databinding.*
import com.fictivestudios.docsvisor.widget.TitleBar
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.util.Log
import android.view.SurfaceView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.requestBody.AgoraRequestBody
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.VideoCallResponse
import com.fictivestudios.docsvisor.helper.CALL_TYPE_VIDEO
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.REJECT_CALL
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.RejectCallRequestBody
import com.fictivestudios.docsvisor.model.User
import com.google.gson.Gson

import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class VideoFragment : BaseFragment() {

    lateinit var binding: FragmentVideoCallBinding
    lateinit var name: String
    private var mRtcEngine: RtcEngine? = null
    private var isMuted=false
    private var isVideo=false
    private var isSpeaker=false


    var currentUserName :String=""
       var currentUserId:String=""
    var receiverUserId:String=""
    private  var otherUserrName: String?=null
    var outSidetoken:String=""
    var channelName = ""

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_video_call, container, false)


        getParams()




        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO) && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)) {

            initAgoraEngineAndJoinChannel()

            if ( outSidetoken != "null" &&  channelName !="null" )
            {
                joinChannel(channelName,outSidetoken)
            }
            else
            {
                getVideoCall()
            }


        }
        return binding.root
    }

    private fun getParams() {
        var gson =  Gson()
        var json:String= PreferenceUtils.getString(USER_OBJECT)
        var user=gson.fromJson(json, User::class.java)

        currentUserId = user?.id.toString()
        currentUserName = user?.name.toString()

        Log.d("args",arguments?.getString("receiverUserId").toString())
        receiverUserId = arguments?.getString("receiverUserId").toString()

        Log.d("args",arguments?.getString("token").toString())
        outSidetoken = arguments?.getString("token").toString()
        Log.d("args",arguments?.getString("channel").toString())
        channelName = arguments?.getString("channel").toString()
        otherUserrName = arguments?.getString("name").toString()
    }

    private fun getVideoCall() {

        //binding.pbFitness.visibility = View.VISIBLE

       val apiClient = ApiClient.RetrofitInstance.getAgoraApiService(requireContext())

  /*      val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("reciever_id",receiverUserId)
            .addFormDataPart("sender_id",currentUserId)
            .addFormDataPart("type","video")
            .addFormDataPart("name","test")

            .build()*/

        var agoraRequestBody = AgoraRequestBody(
            currentUserName,
            receiverUserId.toInt(),
            currentUserId.toInt(),
            CALL_TYPE_VIDEO
        )

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.videoCall(agoraRequestBody).enqueue(object: retrofit2.Callback<VideoCallResponse> {
                override fun onResponse(
                    call: Call<VideoCallResponse>,
                    response: Response<VideoCallResponse>
                )
                {
                    /*  activity?.runOnUiThread(java.lang.Runnable {
                          binding.pbFitness.visibility=View.GONE

                      })*/


                    try {


                        if (response.code() == 200) {

                                val responseData: VideoCallResponse? = response?.body()
                            Log.d("response",responseData.toString())

                            responseData?.channel?.let { joinChannel(it,responseData?.token) }



                        }
                        else {
                            Toast.makeText(context, "Can't connect", Toast.LENGTH_SHORT).show()
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<VideoCallResponse>, t: Throwable)
                {


                    activity?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()

                        Log.d("error",t.message.toString())
                    })
                }
            })




        }
    }


    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        /**
         * Occurs when a remote user (Communication)/ host (Live Broadcast) joins the channel.
         * This callback is triggered in either of the following scenarios:
         *
         * A remote user/host joins the channel by calling the joinChannel method.
         * A remote user switches the user role to the host by calling the setClientRole method after joining the channel.
         * A remote user/host rejoins the channel after a network interruption.
         * The host injects an online media stream into the channel by calling the addInjectStreamUrl method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        override fun onUserJoined(uid: Int, elapsed: Int) {
            activity?.runOnUiThread { setupRemoteVideo(uid) }
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        override fun onUserOffline(uid: Int, reason: Int) {
            activity?.runOnUiThread { onRemoteUserLeft() }
        }

        /**
         * Occurs when a remote user stops/resumes sending the video stream.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's video stream playback pauses/resumes:
         * true: Pause.
         * false: Resume.
         */
        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            activity?.runOnUiThread { onRemoteUserVideoMuted(uid, muted) }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE
        titleBar?.hideBottomBar(requireActivity())
    }

    override fun setListeners() {
        binding.btnLeft2.setOnClickListener {
            getBaseActivity().onBackPressed()
        }
        binding.endCall.setOnClickListener {
            rejectCall(currentUserId,receiverUserId,channelName)
            onEncCallClicked(it)
        }




        binding.btnMuteAudio.setOnClickListener {

            onLocalAudioMuteClicked(it)
        }

        binding.btnMuteVideo.setOnClickListener {
           // onSwitchSpeakerphoneClicked(it)
            onLocalVideoMuteClicked(it)
        }


    }


    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()

    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(requireContext(),
                permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(permission),
                requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    activity?.onBackPressed()
                }
            }
            PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel()
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA)
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun showLongToast(msg: String) {
        activity?.runOnUiThread { Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroy() {
        super.onDestroy()

        leaveChannel()
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy()
        mRtcEngine = null
    }

    fun onLocalVideoMuteClicked(view: View) {
      //  val iv = view as ImageView
        if (isVideo) {
            isVideo = false
            binding.ivMuteVide.setImageResource(R.drawable.icon_video)
        } else {
            isVideo= true
            binding.ivMuteVide.setImageResource(R.drawable.icon_mute_video)
            //  iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        // Stops/Resumes sending the local video stream.
        mRtcEngine!!.muteLocalVideoStream(isVideo)

        val container = binding. localVideoViewContainer
        val surfaceView = container.getChildAt(0) as SurfaceView
       /* surfaceView.setZOrderMediaOverlay(!iv.isSelected)
        surfaceView.visibility = if (iv.isSelected) View.GONE else View.VISIBLE*/
    }

    fun onLocalAudioMuteClicked(view: View) {

        if (isMuted) {
            isMuted = false
            binding.ivMute.setImageResource(R.drawable.icon_mic)
        } else {
            isMuted= true
            binding.ivMute.setImageResource(R.drawable.icon_mic_mute)
            //  iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        // Stops/Resumes sending the local audio stream.
        mRtcEngine!!.muteLocalAudioStream(isMuted)
    }

    fun onSwitchCameraClicked(view: View) {
        // Switches between front and rear cameras.
        mRtcEngine!!.switchCamera()
    }

    fun onEncCallClicked(view: View) {
        activity?.onBackPressed()
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(requireContext(), getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))

            throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }
    }

    private fun setupVideoProfile() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine!!.enableVideo()
//      mRtcEngine!!.setVideoProfile(Constants.VIDEO_PROFILE_360P, false) // Earlier than 2.3.0

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine!!.setVideoEncoderConfiguration(VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }

    private fun setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        val container = binding.localVideoViewContainer
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel(channel: String, token: String) {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.

        mRtcEngine!!.joinChannel(token, channel, "Extra Optional Data", currentUserId.toInt()) // if you do not specify the uid, we will generate the uid for you
    }

    private fun setupRemoteVideo(uid: Int) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        val container = binding.remoteVideoViewContainer

        if (container.childCount >= 1) {
            return
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        val surfaceView = RtcEngine.CreateRendererView(requireContext())
        container.addView(surfaceView)
        // Initializes the video view of a remote user.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose
       // val tipMsg = findViewById<TextView>(R.id.quick_tips_when_use_agora_sdk) // optional UI
        //tipMsg.visibility = View.GONE
    }

    private fun leaveChannel() {
        if (mRtcEngine!=null)
        {
            mRtcEngine?.leaveChannel()
        }

    }

    private fun onRemoteUserLeft() {
        val container = binding.remoteVideoViewContainer
        container.removeAllViews()

        Toast.makeText(requireContext(), "Call Ended", Toast.LENGTH_SHORT).show()
        activity?.onBackPressed()
       // HomeActivityPateint.navControllerPatient?.popBackStack()
        //HomeActivityPateint.navControllerPatient?.popBackStack()

     //   val tipMsg = findViewById<TextView>(R.id.quick_tips_when_use_agora_sdk) // optional UI
      //  tipMsg.visibility = View.VISIBLE
    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = binding.remoteVideoViewContainer

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }


    companion object {


        private val LOG_TAG = VideoFragment::class.java.simpleName

        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1

        fun newInstance(): VideoFragment {

            val args = Bundle()
            val fragment = VideoFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private fun rejectCall(senderId: String, receiverId: String, outSidetoken: String)
    {



        val apiClient = ApiClient.RetrofitInstance.getAgoraApiService(requireContext())

        val rejectCallRequestBody = RejectCallRequestBody(
            senderId,
            receiverId,
            outSidetoken,
            "call_reject"
        )

/*        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("sender_id", senderId )
            .addFormDataPart("receiver_id", senderId )
            .addFormDataPart("channel",outSidetoken )
            .addFormDataPart("type"," call_reject" )
            .build()*/

        GlobalScope.launch(Dispatchers.IO)
        {
            apiClient.rejectCall(rejectCallRequestBody).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {


                    activity?.runOnUiThread(java.lang.Runnable {



                    })

                    response?.body()?.message?.let { Log.d("Response", it) }

//                    Toast.makeText(requireActivity(), ""+response?.body()?.message, Toast.LENGTH_SHORT).show()
                    try {


                        if (response.isSuccessful) {

                            if (response.body()?.status == 0) {
                                activity?.runOnUiThread(java.lang.Runnable {


                                })
                            }


                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {

                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT).show()

                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {

                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()

                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {




                    })
                }
            })

        }


    }


    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            try {
                if (intent.getStringExtra(REJECT_CALL).equals(REJECT_CALL)) {
                    activity?.onBackPressed()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mMessageReceiver,
            IntentFilter("MyData")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }


}
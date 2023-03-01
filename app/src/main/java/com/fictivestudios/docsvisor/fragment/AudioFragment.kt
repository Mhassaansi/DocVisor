package com.fictivestudios.docsvisor.fragment

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fictivestudios.docsvisor.R
import com.fictivestudios.docsvisor.activities.HomeActivityPateint
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.docsvisor.apiManager.requestBody.AgoraRequestBody
import com.fictivestudios.docsvisor.apiManager.response.CommonResponse
import com.fictivestudios.docsvisor.apiManager.response.VideoCallResponse
import com.fictivestudios.docsvisor.constants.SELECT_USER
import com.fictivestudios.docsvisor.databinding.FragmentAudioCallBinding
import com.fictivestudios.docsvisor.helper.CALL_TYPE_AUDIO
import com.fictivestudios.docsvisor.helper.PreferenceUtils
import com.fictivestudios.docsvisor.helper.REJECT_CALL
import com.fictivestudios.docsvisor.helper.USER_OBJECT
import com.fictivestudios.docsvisor.model.RejectCallRequestBody
import com.fictivestudios.docsvisor.model.User
import com.fictivestudios.docsvisor.widget.TitleBar
import com.google.gson.Gson
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.*

class AudioFragment : BaseFragment() {

    private  var otherUserrName: String?=null
    lateinit var binding: FragmentAudioCallBinding
    lateinit var name: String


    private var isMuted=false
    private var isVideo=false
    private var isSpeaker=false

    private val LOG_TAG: String =AudioFragment::class.java.getSimpleName()
    private var mRtcEngine: RtcEngine? = null
    private val PERMISSION_REQ_ID_RECORD_AUDIO = 22


    var currentUserName:String=""
    var currentUserId:String=""
    var receiverUserId:String=""

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_audio_call, container, false);



        getParams()

        if (checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,PERMISSION_REQ_ID_RECORD_AUDIO
            )
        ) {

            initAgoraEngineAndJoinChannel()

            if ( outSidetoken != "null" &&  channelName !="null" )
            {
                joinChannel(channelName,outSidetoken)
            }
            else
            {
                getAudioCall()
            }



        }

        binding.btnEndCall.setOnClickListener {
            onEncCallClicked(it)
            rejectCall(currentUserId,receiverUserId,channelName)
        }

        binding.btnMute.setOnClickListener {

            onLocalAudioMuteClicked(it)
        }

        binding.btnSpeaker.setOnClickListener {
            onSwitchSpeakerphoneClicked(it)
        }



        return binding.root
    }

    private fun getAudioCall() {

        //binding.pbFitness.visibility = View.VISIBLE

        val apiClient = ApiClient.RetrofitInstance.getAgoraApiService(requireContext())

     /*   val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

            .addFormDataPart("reciever_id",receiverUserId)
            .addFormDataPart("sender_id",currentUserId)
            .addFormDataPart("name","test")
            .addFormDataPart("type","audio")

            .build()*/
        var agoraRequestBody = AgoraRequestBody(
            currentUserName,
            receiverUserId.toInt(),
            currentUserId.toInt(),
            CALL_TYPE_AUDIO
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

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // Tutorial Step 1
        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         * Leave the channel: When the user/host leaves the channel, the user/host sends a goodbye message. When this message is received, the SDK determines that the user/host leaves the channel.
         * Drop offline: When no data packet of the user or host is received for a certain period of time (20 seconds for the communication profile, and more for the live broadcast profile), the SDK assumes that the user/host drops offline. A poor network connection may lead to false detections, so we recommend using the Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who
         * leaves
         * the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         * USER_OFFLINE_QUIT(0): The user left the current channel.
         * USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         * USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        override fun onUserOffline(uid: Int, reason: Int) { // Tutorial Step 4
            activity?.runOnUiThread(Runnable {
                onRemoteUserLeft(uid, reason)

            })
        }

        /**
         * Occurs when a remote user stops/resumes sending the audio stream.
         * The SDK triggers this callback when the remote user stops or resumes sending the audio stream by calling the muteLocalAudioStream method.
         *
         * @param uid ID of the remote user.
         * @param muted Whether the remote user's audio stream is muted/unmuted:
         *
         * true: Muted.
         * false: Unmuted.
         */
        override fun onUserMuteAudio(uid: Int, muted: Boolean) { // Tutorial Step 6
            activity?.runOnUiThread(Runnable { onRemoteUserVoiceMuted(uid, muted) })
        }
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

        binding.username.setText(otherUserrName)
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

            if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor")
            {
                getBaseActivity().onBackPressed()

            }

            else
            {
              //  HomeActivityPateint.navControllerPatient?.popBackStack(R.id.callerScreenFragment,true)
               /* HomeActivityPateint.navControllerPatient?.popBackStack()
                HomeActivityPateint.navControllerPatient?.popBackStack()*/
                getBaseActivity().onBackPressed()

            }


        }

        binding.endCall.setOnClickListener {
            if (sharedPreferenceManager?.getString(SELECT_USER).toString() == "Doctor")
            {
                getBaseActivity().onBackPressed()

            }

            else
            {
                HomeActivityPateint.navControllerPatient?.popBackStack(R.id.callerScreenFragment,true)


            }

        }
    }

    companion object {

        fun newInstance(): AudioFragment {
            val args = Bundle()
            val fragment = AudioFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine() // Tutorial Step 1
        // Tutorial Step 2
    }

    fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG,
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

/*    fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>?, @NonNull grantResults: IntArray
    ) {
        Log.i(
            io.agora.tutorials1v1acall.VoiceChatViewActivity.LOG_TAG,
            "onRequestPermissionsResult " + grantResults[0] + " " + requestCode
        )
        when (requestCode) {
            io.agora.tutorials1v1acall.VoiceChatViewActivity.PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    initAgoraEngineAndJoinChannel()
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    finish()
                }
            }
        }
    }*/


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)


        Log.i(LOG_TAG,
            "onRequestPermissionsResult " + grantResults[0] + " " + requestCode
        )
        when (requestCode) {PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    initAgoraEngineAndJoinChannel()
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO)
                    activity?.onBackPressed()
                }
            }
        }
    }


    fun showLongToast(msg: String?) {
        activity?.runOnUiThread(Runnable {
            Toast.makeText(
                requireContext(),
                msg,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    @Override
    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
    }



    // Tutorial Step 7
    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
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

    // Tutorial Step 5
    fun onSwitchSpeakerphoneClicked(view: View) {
        val iv = view as ImageView
        if (isSpeaker) {
            isSpeaker= false
            binding.ivSpeaker.setImageResource(R.drawable.icon_mute_speaker)
        } else {
            isSpeaker= true
            binding.ivSpeaker.setImageResource(R.drawable.icon_speaker)  }

        // Enables/Disables the audio playback route to the speakerphone.
        //
        // This method sets whether the audio is routed to the speakerphone or earpiece. After calling this method, the SDK returns the onAudioRouteChanged callback to indicate the changes.
        mRtcEngine!!.setEnableSpeakerphone(isSpeaker)
    }

    // Tutorial Step 3
    fun onEncCallClicked(view: View?) {
        activity?.onBackPressed()
    }


    // Tutorial Step 1
    private fun initializeAgoraEngine() {
        mRtcEngine = try {
            RtcEngine.create(requireContext(), getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG,
                Log.getStackTraceString(e)
            )
            throw RuntimeException(
                """
                     NEED TO check rtc sdk init fatal error
                     ${Log.getStackTraceString(e)}
                     """.trimIndent()
            )
        }
    }

    // Tutorial Step 2
    private fun joinChannel(channelName:String,accessToken:String) {


        // Sets the channel profile of the Agora RtcEngine.
        // CHANNEL_PROFILE_COMMUNICATION(0): (Default) The Communication profile. Use this profile in one-on-one calls or group calls, where all users can talk freely.
        // CHANNEL_PROFILE_LIVE_BROADCASTING(1): The Live-Broadcast profile. Users in a live-broadcast channel have a role as either broadcaster or audience. A broadcaster can both send and receive streams; an audience can only receive streams.
        mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)

        // Allows a user to join a channel.
        mRtcEngine!!.joinChannel(
            accessToken,
            channelName,
            "Extra Optional Data",
            currentUserId.toInt()
        ) // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 3
    private fun leaveChannel() {
        if (mRtcEngine!=null)
        {
            mRtcEngine?.leaveChannel()
        }

    }

    // Tutorial Step 4
    private fun onRemoteUserLeft(uid: Int, reason: Int) {
    /*    showLongToast(
            String.format(
                Locale.US, "user %d left %d",
                uid and 0xFFFFFFFFL.toInt(), reason
            )
        )*/

        Toast.makeText(requireContext(), "Call Ended", Toast.LENGTH_SHORT).show()
        activity?.onBackPressed()
      //  HomeActivityPateint.navControllerPatient?.popBackStack()
       // HomeActivityPateint.navControllerPatient?.popBackStack()

       /* val tipMsg: View = findViewById(R.id.quick_tips_when_use_agora_sdk) // optional UI
        tipMsg.visibility = View.VISIBLE*/
    }

    // Tutorial Step 6
    private fun onRemoteUserVoiceMuted(uid: Int, muted: Boolean) {
        showLongToast(
            String.format(
                Locale.US, "user %d muted or unmuted %b",
                uid and 0xFFFFFFFFL.toInt(), muted
            )
        )
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
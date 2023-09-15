package si.uni_lj.fri.pbd.miniapp2

import android.os.*
import android.content.Intent
import android.content.ComponentName
import android.content.ServiceConnection
import android.content.res.Configuration
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlin.system.exitProcess
import si.uni_lj.fri.pbd.miniapp2.databinding.ActivityMainBinding
import si.uni_lj.fri.pbd.miniapp2.databinding.ActivityMainHorizontalBinding


class MainActivity : AppCompatActivity() {
    // Constants for waiting before each handling
    companion object {
        private const val MSG_UPDATE_TIME = 1
        private const val UPDATE_RATE_MS = 1000L
    }

    // Media service variables
    private var mediaService: MediaPlayerService? = null
    private var serviceBound: Boolean = false

    // Activity_main layout elements
    private lateinit var progressBar: SeekBar
    private lateinit var trackTitle: TextView
    private lateinit var trackDuration: TextView
    private lateinit var img: ImageView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var btnGOn: Button
    private lateinit var btnGOff: Button
    private lateinit var btnExit: Button

    // Calling the function updateSong() every 1s to update the layout
    private val updateTimeHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(message: Message) {
            if (MSG_UPDATE_TIME == message.what) {
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS)
                updateSong()
            }
        }
    }

    // Connection service with the Media service
    // Calls the function setNewSongData() to set the layout
    // Setting up the updateTimeHandler
    private val mConnection: ServiceConnection = object : ServiceConnection {
        // When connection with the service is established
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as MediaPlayerService.RunServiceBinder
            mediaService = binder.service
            serviceBound = true
            setNewSongData()
            if (mediaService!!.musicStatus == "is_playing") {
                updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
            } else if (mediaService!!.musicStatus == "is_paused" || mediaService!!.musicStatus == "is_stopped") {
                updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            }
        }

        // When service disconnects
        override fun onServiceDisconnected(componentName: ComponentName) {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            serviceBound = false
        }
    }

    // Opens when the activity starts
    override fun onStart() {
        super.onStart()

        // Refresh the layout if there is already a service connection
        if (serviceBound) {
            setNewSongData()
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
        }

        // Starts the service
        val i = Intent(this, MediaPlayerService::class.java)
        i.action = MediaPlayerService.ACTION_START
        startService(i)
        bindService(i, mConnection, 0)
    }

    // When the activity is stopped
    override fun onStop() {
        super.onStop()

        // If there isn't a notification (no interaction with the app yet) the service gets disconnected and the app closed
        if (mediaService!!.musicStatus == "undefined") {
            mediaService?.background()
            stopService(Intent(this, MediaPlayerService::class.java))
            unbindService(mConnection)
            serviceBound = false
        }
    }

    // When the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Checking the current orientation of the phone
        // Up to that set the variables for the elements of the used layout
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the phone is horizontal use the horizontal layout
            val binding = ActivityMainHorizontalBinding.inflate(layoutInflater)
            init(binding, null)
            setContentView(binding.root)
        } else {
            // If the phone is vertical use the main layout
            val binding = ActivityMainBinding.inflate(layoutInflater)
            init(null, binding)
            setContentView(binding.root)
        }

        // When the "Play" button is pressed
        btnStart.setOnClickListener {
            // Start the timehandler and the song in the media service
            val status = mediaService!!.musicStatus
            updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
            mediaService!!.startSong()
            // If the song was not paused (there is a new song) load the data of the new song
            if (status != "is_paused") {
                setNewSongData()
            }
        }

        // When the "Pause" button is pressed
        btnPause.setOnClickListener {
            // Stop the timehandler and pause the song in the media service
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            mediaService!!.pauseSong()
        }

        // When the "Stop" button is pressed
        btnStop.setOnClickListener {
            // Stop the timehandler and the song in the media service
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            mediaService!!.stopSong()
            // Set the song info to its default info
            progressBar.progress = 0
            updateSong()
        }

        // When the "G-On" button is pressed
        btnGOn.setOnClickListener {
            // Start the gestures service from the media service
            mediaService!!.startGestures()
        }

        // When the "G-Off" button is pressed
        btnGOff.setOnClickListener {
            // Stop the gestures service from the media service
            mediaService!!.stopGestures()
        }

        // When the "Exit" button is pressed
        btnExit.setOnClickListener {
            // Stop the timehandler
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            mediaService!!.musicStatus = "undefined"
            // Stop the song in the media service and the media service
            onStop()
            // Close the application
            this@MainActivity.finish()
            exitProcess(0)
        }

        // When the progress bar is changed
        // Got the idea from a youtube video - https://www.youtube.com/watch?v=DaLPIC4NbYU
        progressBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            // When the progressbar is changed
            override fun onProgressChanged(p0: SeekBar?, pos: Int, isChanged: Boolean) {
                // When there is a media service and the progressbar position was changed (press on the progress bar)
                if (isChanged && serviceBound) {
                    // Update the song position and call the function updateSong() to set the layout
                    mediaService!!.mediaPlayer.seekTo(pos)
                    updateSong()
                }
            }

            // When progressbar is clicked
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            // When we the click on the progressbar stops
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    // Initialize all the elements (variables) from the current layout
    private fun init(bindingHorizontal: ActivityMainHorizontalBinding?, bindingVertical: ActivityMainBinding?) {
        // Check which layout the function is called with
        if (bindingHorizontal == null) {
            // When the horizontal layout is chosen
            progressBar = bindingVertical!!.progressBar
            trackTitle = bindingVertical.trackTitle
            trackDuration = bindingVertical.trackDuration
            btnStart = bindingVertical.btnStart
            btnPause = bindingVertical.btnPause
            btnStop = bindingVertical.btnStop
            btnGOn = bindingVertical.btnGOn
            btnGOff = bindingVertical.btnGOff
            btnExit = bindingVertical.btnExit
            img = bindingVertical.img
        } else {
            // When the main layout is chosen
            progressBar = bindingHorizontal.progressBar
            trackTitle = bindingHorizontal.trackTitle
            trackDuration = bindingHorizontal.trackDuration
            btnStart = bindingHorizontal.btnStart
            btnPause = bindingHorizontal.btnPause
            btnStop = bindingHorizontal.btnStop
            btnGOn = bindingHorizontal.btnGOn
            btnGOff = bindingHorizontal.btnGOff
            btnExit = bindingHorizontal.btnExit
            img = bindingHorizontal.img
        }
    }

    // Set the data of the new song (title, progressBar, duration, image)
    fun setNewSongData() {
        trackTitle.text = mediaService!!.currSong.title
        progressBar.progress = 0
        progressBar.max = mediaService!!.mediaPlayer.duration
        trackDuration.text = mediaService!!.currTime()
        img.setImageResource(applicationContext.resources.getIdentifier(mediaService!!.currSong.imgR, "drawable", applicationContext.packageName))
    }

    // Update the data of the current song (title, image, progressBar, duration, notification)
    private fun updateSong() {
        trackTitle.text = mediaService!!.currSong.title
        img.setImageResource(applicationContext.resources.getIdentifier(mediaService!!.currSong.imgR, "drawable", applicationContext.packageName))
        progressBar.progress = mediaService!!.mediaPlayer.currentPosition
        trackDuration.text = mediaService!!.currTime()
        // Always when this function is called there is a notification
        // Refresh the existing notification (title, image, duration, buttons)
        mediaService!!.foreground()
        // When the song finishes
        if (progressBar.progress == mediaService!!.mediaPlayer.duration) {
            // Stop the timehandler (calling of the function every 1 second)
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
        }
    }
}
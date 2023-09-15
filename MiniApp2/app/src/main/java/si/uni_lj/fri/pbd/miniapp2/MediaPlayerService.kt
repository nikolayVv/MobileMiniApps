package si.uni_lj.fri.pbd.miniapp2

import android.annotation.SuppressLint
import android.os.*
import android.app.*
import android.content.Intent
import android.content.ComponentName
import android.content.ServiceConnection
import android.graphics.Color
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.widget.Toast
import androidx.core.app.NotificationCompat

import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MediaPlayerService: Service() {
    // Constants for service, song and notification commands
    companion object {
        const val ACTION_STOP = "stop_service"
        const val ACTION_START = "start_service"
        const val SONG_PAUSE = "pause_song"
        const val SONG_STOP = "stop_song"
        const val SONG_START = "start_song"
        const val BEGINNING = "undefined"
        const val IS_PLAYING = "is_playing"
        const val IS_PAUSED = "is_paused"
        const val IS_STOPPED = "is_stopped"
        private const val channelID = "background_timer"
        private const val NOTIFICATION_ID = 1
    }

    // Service variables
    private var accelerationService: AccelerationService? = null
    private var serviceBound: Boolean = false
    private var serviceBinder: Binder = RunServiceBinder()

    // Song interaction variables
    var musicStatus: String = BEGINNING
    lateinit var currSong: Song
    lateinit var mediaPlayer: MediaPlayer

    // Connection service with the Acceleration service
    private val mConnection: ServiceConnection = object : ServiceConnection {
        // When connection with the service is established
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder = iBinder as AccelerationService.RunServiceBinder
            accelerationService = binder.service
            serviceBound = true
        }

        // When service disconnects
        override fun onServiceDisconnected(componentName: ComponentName) {
            serviceBound = false
        }
    }

    // Run the service binder
    inner class RunServiceBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    // When service is created
    override fun onCreate() {
        // Choose a random song
        setSong()
        // Set the notification channel
        createNotificationChannel()
    }

    // When service is bounded
    override fun onBind(p0: Intent?): IBinder {
        return serviceBinder
    }

    // When the service receives command
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // When the service should be stopped
        if (intent.action === ACTION_STOP) {
            // Stop the notification
            background()
            // Stop the song
            stopSong()
            // Stop the service
            stopSelf()
            // Set status to default
            musicStatus = BEGINNING
        } else if (intent.action === SONG_PAUSE) {
            // When the song should paused
            pauseSong()
        } else if (intent.action === SONG_STOP) {
            // When the song should be stopped
            stopSong()
        } else if (intent.action === SONG_START) {
            // When the song should be started
            startSong()
        } else if (intent.action === "vertical" || intent.action === "horizontal" || intent.action === "forward_backward"){
            // When command from the Acceleration service is received do the gesture action
            doGestureAction(intent.action!!)
        }

        return START_STICKY
    }

    // Create notification
    private fun createNotification(): Notification {
        var changingBtnText = getString(R.string.btn_pause)

        val actionIntent = Intent(this, MediaPlayerService::class.java)
        actionIntent.action = SONG_PAUSE
        // When the song is paused
        if (musicStatus == IS_PAUSED || musicStatus == IS_STOPPED) {
            // Instead of the button "Pause" show the button "Play" in the notification and set the corresponding action
            changingBtnText = getString(R.string.btn_start)
            actionIntent.action = SONG_START
        }
        // Set the actions for the other buttons
        val actionPendingIntent1 = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE)
        actionIntent.action = SONG_STOP
        val actionPendingIntent2 = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE)
        actionIntent.action = ACTION_STOP
        val actionPendingIntent3 = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE)

        // Make the notification using the created actions, button titles, song title, song text, song duration, song image
        val builder = NotificationCompat.Builder(this, channelID)
            .setContentTitle(currSong.title)
            .setContentText(currTime())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setChannelId(channelID)
            .setLargeIcon(BitmapFactory.decodeResource(resources, applicationContext.resources.getIdentifier(currSong.imgR, "drawable", applicationContext.packageName)))
            .addAction(android.R.drawable.ic_media_pause, changingBtnText, actionPendingIntent1)
            .addAction(android.R.drawable.ic_media_pause, getString(R.string.btn_stop), actionPendingIntent2)
            .addAction(android.R.drawable.ic_media_pause, getString(R.string.btn_exit), actionPendingIntent3)

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(resultPendingIntent)
        return builder.build()
    }

    // Create notification channel
    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) {
            return
        } else {
            val channel = NotificationChannel(channelID, getString(R.string.channel_name), NotificationManager.IMPORTANCE_LOW)
            channel.description = getString(R.string.channel_desc)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            val managerCompat = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            managerCompat.createNotificationChannel(channel)
        }
    }

    // Return a string that shows the current position of the song and the whole duration of the song
    // Convert from ms to hh:mm:ss
    // Got the idea from StackOverflow - https://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
    fun currTime(): String {
        return ("${String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(mediaPlayer.currentPosition.toLong()),
                TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.currentPosition.toLong()) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mediaPlayer.currentPosition.toLong())),
                TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.currentPosition.toLong()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.currentPosition.toLong()))
            )}/" +
            String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(mediaPlayer.duration.toLong()),
                    TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.duration.toLong()) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mediaPlayer.duration.toLong())),
                    TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.duration.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.duration.toLong()))
            )
        )
    }

    // Start the song + create notification (always in the foreground)
    fun startSong() {
        foreground()
        if (musicStatus == IS_PAUSED) {
            // When the song was paused just continue from there where is stopped
            mediaPlayer.start()
        } else {
            // In all other cases just set a new random song and start it
            if (musicStatus == IS_PLAYING) {
                // When the song is playing it must be first stopped
                stopSong()
            }
            setSong()
            mediaPlayer.start()
        }
        // Set the new song status
        musicStatus = IS_PLAYING
    }

    // Choose a random song and set it to the media player
    private fun setSong() {
        var randomIndex: Int
        if (musicStatus == BEGINNING) {
            // When the application is opened for first time just choose a random song from the list of songs
            randomIndex = Random.nextInt(Songs.songList.size)
            currSong = Songs.songList[randomIndex]
        } else {
            // Choose a random song from the list of songs that is not the current one
            val oldSong = currSong
            while (currSong == oldSong) {
                randomIndex = Random.nextInt(Songs.songList.size)
                currSong = Songs.songList[randomIndex]
            }
        }

        // Create the media player using the chosen song
        mediaPlayer = MediaPlayer.create(
            this,
            applicationContext.resources.getIdentifier(currSong.fileR, "raw", applicationContext.packageName)
        )
    }

    // Pause the song
    fun pauseSong() {
		if (musicStatus == IS_PLAYING) {
			musicStatus = IS_PAUSED
			mediaPlayer.pause()
		}
    }

    // Stop the song + set the default values of the song (put the audio in the beginning)
    fun stopSong() {
		if (musicStatus != IS_STOPPED) {
			mediaPlayer.seekTo(0)
			mediaPlayer.stop()
			musicStatus = IS_STOPPED
		}
    }

    // Enable gesture commands
    fun startGestures() {
        if (accelerationService == null) {
            // When there isn't an active acceleration service, create one
            val i = Intent(this, AccelerationService::class.java)
            i.action = AccelerationService.ACTION_START
            startService(i)
            bindService(i, mConnection, 0)
            // Notify the user that the gestures are now enabled
            Toast.makeText(this, R.string.gon, Toast.LENGTH_SHORT)
                .show()
        } else {
            // When there is already a active acceleration service
            // Notify the user that the gestures are already enabled
            Toast.makeText(this, R.string.gon_already, Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Disable gesture commands
    fun stopGestures() {
        if (accelerationService != null) {
            // When there is an active acceleration service, stop it
            accelerationService!!.unregisterListener()
            stopService(Intent(this, AccelerationService::class.java))
            unbindService(mConnection)
            // Set the service variables to their default values
            serviceBound = false
            accelerationService = null
            // Notify the user that the gestures are now disabled
            Toast.makeText(this, R.string.goff, Toast.LENGTH_SHORT)
                .show()
        } else {
            // When there isn't an active acceleration service
            // Notify the user that the gestures are already disabled
            Toast.makeText(this, R.string.goff_already, Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Do the action corresponding to the gesture command
    private fun doGestureAction(command: String) {
        if (command == "vertical" && musicStatus == IS_PLAYING) {
            // When the phone is moved vertically pause the current song
            pauseSong()
        } else if (command == "horizontal") {
            // When the phone is moved horizontally start the current song (if paused) or start a new random song
            startSong()
        } else if (command == "forward_backward" && musicStatus != IS_STOPPED) {
            // When the phone is moved forward-backward stop the current song
            stopSong()
        }
    }

    // Run the service in foreground (create notification)
    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    // Run the service in background (stop the current foreground)
    fun background() {
        stopForeground(true)
    }
}
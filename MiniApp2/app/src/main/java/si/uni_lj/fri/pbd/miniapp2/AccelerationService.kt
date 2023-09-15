package si.uni_lj.fri.pbd.miniapp2

import android.os.*
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener

import kotlin.math.abs

class AccelerationService: Service(), SensorEventListener {
    // Constants for waiting before each handling, service and sensor commands
    companion object {
        const val ACTION_START = "start_service"
        const val FORWARD_BACKWARD = "forward_backward"
        const val HORIZONTAL = "horizontal"
        const val VERTICAL = "vertical"
        const val IDLE = "undefined"
        private const val MSG_UPDATE_TIME = 1
        private const val UPDATE_RATE_MS = 3000L
    }

    // Service variables
    private var serviceBinder: Binder = RunServiceBinder()
    // Sensor variables
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    // Phone's position variable
    private var isAccessible: Boolean = true
    private var position: Vector3? = null

    // Setting the variable "isAccessible" to true every 3s to avoid sending commands every 50ms
    private val updateTimeHandler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(message: Message) {
            if (MSG_UPDATE_TIME == message.what) {
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS)
                isAccessible = true
            }
        }
    }

    // Run the service binder
    inner class RunServiceBinder : Binder() {
        val service: AccelerationService
            get() = this@AccelerationService
    }

    // When service is created
    override fun onCreate() {
        super.onCreate()

        // Create the sensor manager and the accelerometer
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    // When service is bounded
    override fun onBind(p0: Intent?): IBinder {
        return serviceBinder
    }

    // When the service receives command
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // When the the service should be started
        if (intent.action == ACTION_START) {
            // Set the current position of the phone to null and register the listener
            position = null
            registerListener()
        }


        return START_STICKY
    }

    // Register listener of the accelerometer sensor and start the timehandler
    private fun registerListener() {
        updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // Unregister listener of the accelerometer sensor and stop the timehandler
    fun unregisterListener() {
        updateTimeHandler.removeMessages(MSG_UPDATE_TIME)
        sensorManager.unregisterListener(this)
    }

    // Called every 50ms
    override fun onSensorChanged(event: SensorEvent?) {
        // When the sensor type is accelerometer
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER ) {
            // Values smaller than 5 are consider as sensor noise
            val noiseThreshold = 5.0F
            // Get acceleration sensor readings for each axis
            val x = event.values[0]
            val y = event.values[2]
            val z = event.values[1]
            // When the position of the phone is not set yet
            if (position == null) {
                // Set it to the current acceleration sensor readings and stop the function
                position = Vector3(x, y, z)
                return
            }
            // Calculate relative change for each axis
            var changeX = abs(position!!.x - x)
            var changeY = abs(position!!.y - y)
            var changeZ = abs(position!!.z - z)
            // Check if the change is considered as noise -> No change
            if (changeX <= noiseThreshold) {
                changeX = 0.0F
            }
            if (changeY <= noiseThreshold) {
                changeY = 0.0F
            }
            if (changeZ <= noiseThreshold) {
                changeZ = 0.0F
            }
            // Check the type of the command up to the change of each axis
            var command = IDLE
            // When change in X axis is bigger than the change in Z axis
            if (changeX > changeZ) {
                // The phone is moving on X axis
                command = HORIZONTAL
            }
            // When change in Z axis is bigger than the change in X axis
            if (changeZ > changeX) {
                // The phone is moving on Y axis
                command = VERTICAL
            }
            // When change in Y axis is bigger than the change in Z axis
            if (changeY > changeZ) {
                // The phone is moving on Z axis
                command = FORWARD_BACKWARD
            }

            // When there is a command set and the time constraint allows it
            if (command != IDLE && isAccessible) {
                // Set the "isAccessible" variable to false and call a method in media service to do the chosen action
                isAccessible = false
                val i = Intent(applicationContext, MediaPlayerService::class.java)
                i.action = command
                startService(i)
            }

            // Refresh the current position of the phone
            position = Vector3(x, y, z)
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}

// Object that will be used to work easier with the current position of the phone
data class Vector3(var x: Float, var y: Float, var z: Float)


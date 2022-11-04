package com.example.assesmentapp.capture.ui

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.hardware.display.DisplayManager
import android.media.Image
import android.media.ImageReader
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import com.example.assesmentapp.R
import com.example.assesmentapp.base.Status
import com.example.assesmentapp.base.TARGETREQUESTCODE
import com.example.assesmentapp.base.showBottomSheet
import com.example.assesmentapp.errorhandling.ErrorDialog
import com.example.assesmentapp.home.viewmodel.CaptureViewModelViewModel
import com.example.assesmentapp.home.viewmodel.HomeViewModel
import com.google.android.gms.common.util.concurrent.HandlerExecutor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.nio.ByteBuffer

@AndroidEntryPoint
class CaptureActivity : AppCompatActivity() {

    private val btnPipMode: Button by lazy { findViewById(R.id.btn_pip_mode) }
    private val textureView: TextureView by lazy { findViewById(R.id.texture_view) }
    private val takePictureButton: Button by lazy { findViewById(R.id.btn_start_capturing_pictures) }

    private val captureViewModelViewModel: CaptureViewModelViewModel by viewModels()


    private var cameraId: String? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var imageDimension: Size? = null
    private var imageReader: ImageReader? = null


    //    private val file: File? = null
    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        initview()
        initObserver()
    }

    private fun initObserver() {
        captureViewModelViewModel.dataforUploadImageResponse2.observe(this@CaptureActivity, Observer {
            when(it.status) {
                Status.LOADING -> {
                    Log.i(TAG,"LOADING ::::::::: ")
                }
                Status.SUCCESS -> {
                    Log.d(TAG,"SUCCESS ::::::::: ${it.message}")
                }
                Status.ERROR -> {
                    Log.e(TAG,"ERRROR ::::::::: ${it.message}")
                }
            }
        })
        captureViewModelViewModel.dataforUploadImageResponse.observe(this@CaptureActivity, Observer {
            when(it.status) {
                Status.LOADING -> {
                    Log.i(TAG,"LOADING ::::::::: ")
                }
                Status.SUCCESS -> {
                    Log.d(TAG,"SUCCESS ::::::::: ${it.message}")
                }
                Status.ERROR -> {
                    Log.e(TAG,"ERRROR ::::::::: ${it.message}")
                }
            }
        })

    }

    private fun uploadFile(file: File) {
        val params: MutableMap<String, RequestBody> = HashMap()



        val fileS = MultipartBody.Part.createFormData("image",file.name,RequestBody.create(MediaType.parse("image/jpg"),file))
        params["time"] = RequestBody.create(MultipartBody.FORM, "1651836309829")
        params["latitude"] = RequestBody.create(MultipartBody.FORM, "22.679894")
        params["longitude"] = RequestBody.create(MultipartBody.FORM, "88.272904")
        params["altitude"] = RequestBody.create(MultipartBody.FORM, "100")
        params["bearing"] = RequestBody.create(MultipartBody.FORM, "4323")
        params["accuracy"] = RequestBody.create(MultipartBody.FORM, "1.345")
        params["speed"] = RequestBody.create(MultipartBody.FORM, "60")
        params["provider"] = RequestBody.create(MultipartBody.FORM, "ola")
        params["record_id"] = RequestBody.create(MultipartBody.FORM, "b534b705-f3a7-4b4e-bb4f-cfa45c821a56")
        params["user_id"] = RequestBody.create(MultipartBody.FORM, "536501c5-6d05-4fa0-9f94-ae39b6d0c0cd")

//        captureViewModelViewModel.uploadImage(fileS)
        captureViewModelViewModel.uploadImage(fileS,params)
    }


    private fun initview() {

        textureView.surfaceTextureListener = textureListener

    }


    private var textureListener: TextureView.SurfaceTextureListener = object :
        TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            //open your camera here
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            // Transform you image captured size according to the surface width and height
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private fun openCamera() {
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Log.e("TAG", "is camera open")
        try {
            cameraId = manager.cameraIdList[0]
            cameraId?.let {
                val characteristics = manager.getCameraCharacteristics(it)
                val map =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
                // Add permission for camera and let user grant the permission
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@CaptureActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_PERMISSION
                    )
                    return
                }
                manager.openCamera(it, stateCallback, null)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        Log.e(TAG, "openCamera X")
    }

    private fun createCameraPreview() {
        try {
            val texture = textureView.surfaceTexture!!
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            val surface = Surface(texture)
            captureRequestBuilder =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(surface)
            cameraDevice!!.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        //The camera is already closed
                        if (null == cameraDevice) {
                            return
                        }
                        // When the session is ready, we start displaying the preview.
                        cameraCaptureSessions = cameraCaptureSession
                        updatePreview()
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        Toast.makeText(
                            this@CaptureActivity,
                            "Configuration change",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return")
        }
        captureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSessions!!.setRepeatingRequest(
                captureRequestBuilder!!.build(),
                null,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened")
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }


    override fun onStart() {
        super.onStart()
        takePictureButton.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null")
            return
        }
        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val characteristics = manager.getCameraCharacteristics(
                cameraDevice!!.id
            )
            val jpegSizes: Array<Size>? = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!.getOutputSizes(
                ImageFormat.JPEG)
            var width = 640
            var height = 480
            if (jpegSizes != null && jpegSizes.isNotEmpty()) {
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurfaces: MutableList<Surface> = ArrayList(2)
            outputSurfaces.add(reader.surface)
            outputSurfaces.add(Surface(textureView.surfaceTexture))
            val captureBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(reader.surface)
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            // Orientation
            val rotation = getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)?.rotation

            //set Orientation for captured img
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, orientationArray[rotation!!])

//            Log.i("ROTATION", orientationArray.get(rotation).toString())

            val cw = ContextWrapper(applicationContext)
            // path to /data/data/com.geospoc.cameraapplication/app_data/imageDir
            val directory = cw.getDir("imageDir", MODE_PRIVATE)
            // Create imageDir
            val file = File(directory, System.currentTimeMillis().toString() + ".jpg")

            val readerListener: ImageReader.OnImageAvailableListener = object :
                ImageReader.OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader) {
                    var image: Image? = null
                    try {
                        image = reader.acquireLatestImage()
                        val buffer: ByteBuffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.capacity())
                        buffer.get(bytes)
                        save(bytes)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        image?.close()
                    }
                }

                @Throws(IOException::class)
                private fun save(bytes: ByteArray) {
                    var output: OutputStream? = null
                    try {
                        output = FileOutputStream(file)
                        output.write(bytes)
                    } finally {
                        output?.close()
                    }
                }
            }
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler)
            val captureListener: CameraCaptureSession.CaptureCallback = object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    createCameraPreview()
                    val fileS = MultipartBody.Part.createFormData(
                        "file", file.name, RequestBody.create(
                            MediaType.parse("image/jpg"), file
                        )
                    )

                    uploadFile(file)
                    Toast.makeText(this@CaptureActivity, "Saved:$file", Toast.LENGTH_SHORT).show()
                }
            }



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val outputConfiguration: MutableList<OutputConfiguration> = ArrayList()
                outputConfiguration.add(OutputConfiguration(reader.surface))
                outputConfiguration.add(OutputConfiguration(Surface(textureView.surfaceTexture)))

                val sessionConfiguration =
                    SessionConfiguration(
                        SessionConfiguration.SESSION_REGULAR,
                        outputConfiguration,
                        HandlerExecutor(mBackgroundHandler?.looper ?: Looper.getMainLooper()),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                try {
                                    session.capture(
                                        captureBuilder.build(),
                                        captureListener,
                                        mBackgroundHandler
                                    )
                                } catch (e: CameraAccessException) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {}
                        }
                    )
                cameraDevice?.createCaptureSession(sessionConfiguration)

            } else {
                cameraDevice?.createCaptureSession(
                    outputSurfaces,
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            try {
                                session.capture(
                                    captureBuilder.build(),
                                    captureListener,
                                    mBackgroundHandler
                                )
                            } catch (e: CameraAccessException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {}
                    },
                    mBackgroundHandler
                )
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
        startBackgroundThread()
        if (textureView.isAvailable) {
            openCamera()
        } else {
            textureView.surfaceTextureListener = textureListener
        }
    }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread?.let {
            it.start()
            mBackgroundHandler = Handler(it.looper)
        }
    }

    companion object {
        private val TAG: String? = CaptureActivity::class.java.simpleName
        private const val REQUEST_CAMERA_PERMISSION = 200

    }
}
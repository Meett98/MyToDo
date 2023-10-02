package com.example.mytodos.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mytodos.R
import com.example.mytodos.databinding.ActivityCreatePostBinding
import com.example.mytodos.databinding.ActivityCreateUpdateBinding
import com.example.mytodos.db.TodoDAO
import com.example.mytodos.db.TodoDatabase
import com.example.mytodos.db.TravelPostDao
import com.example.mytodos.db.TravelPostDatabase
import com.example.mytodos.entity.Entity
import com.example.mytodos.entity.EntityPost
import com.example.mytodos.repository.ToDoRepository
import com.example.mytodos.repository.TravelPostRepository
import com.example.mytodos.viewmodel.MainViewModel
import com.example.mytodos.viewmodel.TravelPostViewModel
import com.example.mytodos.viewmodel.TravelPostViewModelFactory

class CreatePostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePostBinding
    private lateinit var travelPostViewModel: TravelPostViewModel
    private lateinit var travelPostDatabase: TravelPostDatabase
    private lateinit var username:String
    private lateinit var password:String
    private lateinit var travelPostRepository: TravelPostRepository
    private lateinit var travelPostDao: TravelPostDao
    private lateinit var postTitle:String
    private lateinit var postLocation:String
    private lateinit var btn:String
    private var id:Int=0
    private val PICK_IMAGE_REQUEST = 2
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        travelPostDatabase = TravelPostDatabase.getDatabase(this)
        travelPostDao = TravelPostDatabase.getDatabase(this).travelpostDAO()
        travelPostRepository = TravelPostRepository(travelPostDao)
        travelPostViewModel = ViewModelProvider(this, TravelPostViewModelFactory(travelPostRepository))[TravelPostViewModel::class.java]

        val intent = intent     //getIntent()
        username = intent.getStringExtra("USERNAME").toString()
        password = intent.getStringExtra("PASSWORD").toString()
        id=intent.getIntExtra("ID",0)
        postTitle = intent.getStringExtra("TITLE").toString()
        postLocation = intent.getStringExtra("LOCATION").toString()
        btn = intent.getStringExtra("BUTTON_TEXT").toString()




        binding.postButton.setOnClickListener{
            postTitle = binding.postTitle.text.toString()
            postLocation = binding.postLocation.text.toString()



//            val entityPost = EntityPost(0,postTitle,postLocation,selectedImageUri.toString(),username,password)
            val entityPost = EntityPost(0,postTitle,postLocation,username,password)

            travelPostViewModel.insertTravelPost(entityPost)
            val count = travelPostViewModel.getUserPostCount(username)
            travelPostViewModel.getUserPostCount(username).observe(this){
                if(it == 2)
                {
                    createNotification()
                }

            }
            Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()

            val iNext= Intent(this, MainActivity::class.java)
            startActivity(iNext)



        }


        binding.btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                // User has selected an image
                selectedImageUri = data.data!!

                // Now you can use this selectedImageUri to display the image in your UI
                // For example, you can load it into an ImageView
                binding.imagePost.setImageURI(selectedImageUri)
            }
        }
    }




    private fun createNotification() {
        // Create a notification channel (for Android 8.0 and higher)
        createNotificationChannel()




        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Congretulations!!")
            .setContentText("You've reached 10 posts in your travel diary.")
            .setSmallIcon(R.drawable.twotone_celebration_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@CreatePostActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, notification.build())
        }





    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            val name = "Download Channel"
            val descriptionText = "Channel for download notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    companion object {
        private const val CHANNEL_ID = "download_channel"
        private const val NOTIFICATION_ID = 1
    }
}
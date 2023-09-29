package com.example.mytodos.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Travel_Diary")
data class EntityPost(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var posttitle:String,
    var location:String,
    var username : String,
    var password: String

//    var imageUri : String?,


)
package com.example.projetomobileads5a.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe (
    val id: Int,
    val title: String,
    val image: String
) : Parcelable {
    constructor() : this(0, "", "")
}

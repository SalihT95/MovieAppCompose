package com.turkoglu.moviecomposeapp.util

import com.turkoglu.moviecomposeapp.R

object AvatarUtils {

    // Firestore'a gidecek String ID -> Drawable ID Eşleşmesi
    val avatarMap = mapOf(
        "beaver" to R.drawable.beaver,
        "cat" to R.drawable.cat,
        "chick" to R.drawable.chick,
        "deer" to R.drawable.deer,
        "dog" to R.drawable.dog,
        "duck" to R.drawable.duck,
        "elephant" to R.drawable.elephant,
        "frog" to R.drawable.frog,
        "ghost" to R.drawable.ghost,
        "hen" to R.drawable.hen,
        "hippopotamus" to R.drawable.hippopotamus,
        "horse" to R.drawable.horse,
        "koala" to R.drawable.koala,
        "monkey" to R.drawable.monkey,
        "owl" to R.drawable.owl,
        "pikachu" to R.drawable.pikachu,
        "sea_lion" to R.drawable.sea_lion,
        "shark" to R.drawable.shark,
        "sloth" to R.drawable.sloth,
        "wolf" to R.drawable.wolf
    )

    // Misafir kullanıcı için sabit anahtar
    const val GUEST_AVATAR_KEY = "ghost"

    // Seçim ekranında "Ghost" hariç diğerlerini gösterelim
    val selectableAvatars = avatarMap.keys.filter { it != GUEST_AVATAR_KEY }.toList()

    // Rastgele bir avatar ID'si (String) döndürür (Ghost hariç)
    fun getRandomAvatar(): String {
        return selectableAvatars.random()
    }

    // String ID'den Drawable ID'ye çevirir (Bulamazsa Ghost döner)
    fun getDrawableId(key: String?): Int {
        return avatarMap[key] ?: R.drawable.ghost
    }
}
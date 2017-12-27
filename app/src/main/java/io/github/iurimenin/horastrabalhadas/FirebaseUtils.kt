package io.github.iurimenin.horastrabalhadas

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Iuri Menin on 27/12/2017.
 */
class FirebaseUtils private constructor(val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()) {

    init {

        firebaseDatabase.setPersistenceEnabled(true)
    }

    private object Holder { val INSTANCE = FirebaseUtils() }

    companion object {
        val instance: FirebaseUtils by lazy { Holder.INSTANCE }
    }

    fun dayLogReference(): DatabaseReference {

        return firebaseDatabase.dayLogReference
    }
}
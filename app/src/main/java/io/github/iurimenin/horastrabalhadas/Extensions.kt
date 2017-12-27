package io.github.iurimenin.horastrabalhadas

import br.com.softfocus.dateutils.DateUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by Iuri Menin on 18/12/2017.
 */

val FirebaseUser.emailReference: String
    get() {
        if (email.isNullOrBlank()) {
            throw Exception("No logged user")
        }
        return email!!.replace(".", "|")
    }

val FirebaseDatabase.dayLogReference: DatabaseReference
    get() {
        val ref = reference
                .child(FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.emailReference)
                .child("dayLogs")
        ref.keepSynced(true)
        return ref
    }

val Date.dateStringFirebase: String
    get() {
        return DateUtils.format(this, "dd|MM|yyyy")
    }
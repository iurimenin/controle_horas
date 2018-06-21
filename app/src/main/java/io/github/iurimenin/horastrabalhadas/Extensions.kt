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
        val c = Calendar.getInstance()
        val ref = reference
                .child(FirebaseAuth.getInstance().currentUser?.emailReference!!)
                .child("dayLogs")
                .child(c.get(Calendar.YEAR).toString())
                .child(c.get(Calendar.MONTH).plus(1).toString())

        ref.keepSynced(true)
        return ref
    }

val Date.dateStringFirebase: String
    get() {
        return DateUtils.format(this, "dd|MM|yyyy")
    }

fun Calendar.setLeaveTime(estimatedLeaveTime: String) {

    val leaveTime = Calendar.getInstance()
    leaveTime.time = DateUtils.convertStringForDate(estimatedLeaveTime, "HH:mm")

    this.set(Calendar.HOUR_OF_DAY, leaveTime.get(Calendar.HOUR_OF_DAY))
    this.set(Calendar.MINUTE, leaveTime.get(Calendar.MINUTE))
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
}
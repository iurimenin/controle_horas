package io.github.iurimenin.horastrabalhadas

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.softfocus.dateutils.DateUtils
import com.afollestad.materialdialogs.MaterialDialog
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by Iuri Menin on 15/12/2017.
 */
class MainActivity : AppCompatActivity(), FirebaseRecyclerAdapterCallBack {

    private lateinit var mFirebaseRecyclerAdapter:
            FirebaseRecyclerAdapter<DayLog, DayLogAdapter.DayLogHolder>

    private val mReference = FirebaseUtils.instance.dayLogReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = FirebaseRecyclerOptions.Builder<DayLog>()
                .setQuery(mReference, DayLog::class.java)
                .setLifecycleOwner(this)
                .build()

        mFirebaseRecyclerAdapter = DayLogAdapter(this, options)

        // Scroll to bottom on new messages
        mFirebaseRecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recycler_view_days.smoothScrollToPosition(mFirebaseRecyclerAdapter.itemCount)
            }
        })

        // This makes the items been shown in the newest to oldest
        val layoutMaganer = LinearLayoutManager(this)
        layoutMaganer.stackFromEnd = true
        layoutMaganer.reverseLayout = true

        recycler_view_days.setHasFixedSize(true)
        recycler_view_days.layoutManager = layoutMaganer
        recycler_view_days.adapter = mFirebaseRecyclerAdapter

        fab_add_time.setOnClickListener {
            DayLog.Companion.logNow(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        item?.let {
            return when (it.itemId) {
                R.id.menu_item_logout -> {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener {
                                mFirebaseRecyclerAdapter.stopListening()
                                // user is now signed out
                                val intent = Intent(this@MainActivity,
                                        LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        mFirebaseRecyclerAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseRecyclerAdapter.stopListening()
    }

    override fun onDataChanged(itemCount: Int) {
        empty_text_view.visibility = if (itemCount == 0) View.VISIBLE else
            View.GONE
    }

    override fun onError(errorMessage: String) = Snackbar
            .make(main_coordinatorLayout, "Error: $errorMessage", Snackbar.LENGTH_LONG).show()

    override fun onDelete(dayLog: DayLog) {

        val materialDialog = MaterialDialog.Builder(this)
                .autoDismiss(false)
                .title(R.string.delete_log)
                .negativeText(R.string.cancel_delete)
                .positiveText(R.string.confirm_delete)
                .positiveColor(ContextCompat.getColor(this, R.color.primaryColor))
                .negativeColor(ContextCompat.getColor(this, R.color.secondaryColor))
                .content(getString(R.string.delete_log_content, dayLog.date))
                .onPositive { dialog, _ ->
                    val dateRef = DateUtils.convertStringForDate(dayLog.date, DateUtils.DayMonthYearFormat)
                    FirebaseUtils.instance
                            .dayLogReference()
                            .child(dateRef.dateStringFirebase)
                            .removeValue()
                            .addOnSuccessListener {
                                val snackbar = Snackbar
                                        .make(main_coordinatorLayout,
                                                R.string.delete_success,
                                                Snackbar.LENGTH_LONG)
                                snackbar.show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener {
                                val snackbar = Snackbar
                                        .make(main_coordinatorLayout,
                                                R.string.delete_error,
                                                Snackbar.LENGTH_LONG)
                                snackbar.show()
                                dialog.dismiss()
                            }
                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }.build()

        materialDialog.show()
    }
}

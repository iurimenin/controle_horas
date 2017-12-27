package io.github.iurimenin.horastrabalhadas

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

    override fun onError(errorMessage: String) = Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_LONG).show()
}

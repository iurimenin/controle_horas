package io.github.iurimenin.horastrabalhadas.callbacks

import io.github.iurimenin.horastrabalhadas.DayLog

/**
 * Created by Iuri Menin on 18/12/2017.
 */
interface FirebaseRecyclerAdapterCallBack {

    fun onDataChanged(itemCount: Int)

    fun onError(errorMessage : String)

    fun onDelete(dayLog: DayLog)

    fun onItemClick(dayLog: DayLog)
}
package com.example.teamprogram.ui.home

import android.util.Half.toFloat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val data = ArrayList<Calendar>()
    init {
        for(i in 1..48) {

            if ((i-1)%7 == 0){
                data.add(Calendar("Date","0", Calendar.Date, "${(i-1)/7 + 1}"))
            }else{
                data.add(Calendar("Homework", "${i*3}h", Calendar.Object))
            }
        }
        data[1] = Calendar("Math","7h",Calendar.Empty)
        data[4] = Calendar("Math","10h",Calendar.Empty)
        data[8] = Calendar("English","7h",Calendar.Empty)
        data[12] = Calendar("CI12","12h",Calendar.Empty)
        data[16] = Calendar("Math","7h",Calendar.Empty)
        data[18] = Calendar("CI103","98h",Calendar.Empty)
        data[24] = Calendar("Math","7h",Calendar.Empty)
        data[41] = Calendar("Math","7h",Calendar.Empty)
    }
}

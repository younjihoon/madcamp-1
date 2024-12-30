package com.example.madcamp2024wjhnh

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.madcamp2024wjhnh.data.AppDatabase
import com.example.madcamp2024wjhnh.data.DayInfo
import com.example.madcamp2024wjhnh.data.TravelR
import kotlinx.coroutines.launch

class TravelViewModel(application: Application) : AndroidViewModel(application) {

    private val travelDao = AppDatabase.getDatabase(application).travelDao()
    val allTravels: LiveData<List<TravelR>> = travelDao.getAllTravels()

    fun insert(travel: TravelR, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val generatedId = travelDao.insert(travel) // Long 반환
            Log.e("debug", "Inserted travel with ID: $generatedId")
            callback(generatedId.toInt()) // Long -> Int 변환 후 콜백으로 전달
        }
    }

    fun update(travel: TravelR) {
        viewModelScope.launch {
            travelDao.update(travel)
        }
    }

    fun delete(travel: TravelR) {
        viewModelScope.launch {
            travelDao.delete(travel)
        }
    }

    fun getTravelById(id: Int, callback: (TravelR?) -> Unit) {
        Log.e("[TravelVM]","getTravelById find with $id")
        viewModelScope.launch {
            Log.e("[TravelVM]","vm launched : find with $id")
//            val allTravels = travelDao.getAllTravels()
//            Log.e("[TravelVM]", "All travels: $allTravels")
//            allTravels.observeForever { travels ->
//                Log.e("[TravelVM]", "All travels: $travels")
//            }
            val travel = travelDao.getTravelById(id)
            Log.e("[TravelVM]","what travel found : $travel")
            callback(travel)
        }
    }

    fun updateDayInfos(id: Int, newDayInfos: MutableList<DayInfo>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // ID로 기존 TravelR 객체 가져오기
                val travel = travelDao.getTravelById(id)
                if (travel != null) {
                    // DayInfos 업데이트
                    travel.DayInfos = newDayInfos
                    travelDao.update(travel) // 업데이트 실행
                    Log.d("debug", "Updated DayInfos for Travel with ID: $id")
                    callback(true)
                } else {
                    Log.d("debug", "Travel with ID $id not found")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("error", "Failed to update DayInfos for Travel with ID: $id", e)
                callback(false)
            }
        }
    }
    fun updateById(travelId: Int, dayInfos: MutableList<DayInfo>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Fetch the existing TravelR by ID
                val travel = travelDao.getTravelById(travelId)
                if (travel != null) {
                    // Update DayInfos and save
                    travel.DayInfos = dayInfos
                    travelDao.update(travel) // Full entity update
                    Log.d("[TravelViewModel]debug", "Updated TravelR with ID: $travelId")
                    callback(true)
                } else {
                    Log.d("[TravelViewModel]debug", "TravelR with ID: $travelId not found")
                    callback(false)
                }
            } catch (e: Exception) {
                Log.e("[TravelViewModel]error", "Failed to update TravelR with ID: $travelId", e)
                callback(false)
            }
        }
    }


}

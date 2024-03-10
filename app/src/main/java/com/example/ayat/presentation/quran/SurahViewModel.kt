package com.example.ayat.presentation.quran

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.localdata.Ayahs
import com.example.ayat.AyatApplication
import com.example.ayat.data.retrofit.InternetObserver
import com.example.ayat.data.retrofit.NetworkConnectivityObserver
import com.example.ayat.data.repositories.SurahReopsitory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahViewModel  @Inject constructor(private val surahRepo:SurahReopsitory,saveStateHandle: SavedStateHandle) : ViewModel() {

    var ayahsState by mutableStateOf(emptyList<Ayahs>())
    val quraaList=surahRepo.lista
    var expandedStates by mutableStateOf(List(ayahsState.size) { mutableStateOf(false) })
    var selectedQarie by mutableStateOf(quraaList[0])
    var searchQuery by mutableStateOf("")


    private val networkConnectivityObserver = NetworkConnectivityObserver(AyatApplication.getApplicationContext())
    private val networkStatus = mutableStateOf(InternetObserver.Status.NotAvailable)
    private var mediaSession: MediaSessionCompat? = null
    private var mediaPlayer: MediaPlayer? = null
  var scrollToAyahIndex by mutableIntStateOf(-1)
    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage




    private val errorHandler = CoroutineExceptionHandler { _, _ ->
    }


    init {
        val id = saveStateHandle.get<Int>("surahId") ?: 0
        getSurahh(id - 1)
        mediaSession = MediaSessionCompat(AyatApplication.getApplicationContext(), "SurahPlayer").apply {
            isActive = true
        }
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                networkStatus.value = status
            }
        }


    }

    fun searchAyahByNumber() {

        val index = ayahsState.indexOfFirst { ayah ->
            if(searchQuery.isBlank()){
                searchQuery= "1"

            }
            ayah.numberInSurah == searchQuery.toInt()
        }
        scrollToAyahIndex = index
    }






    fun playAudio(audioId: Int, name: String) {
        when (networkStatus.value) {
            InternetObserver.Status.Available -> {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                _toastMessage.value = "جارِِِ التشغيل"


                // Toast.makeText(AyatApplication.getApplicationContext(), "جارِِِ التشغيل", Toast.LENGTH_SHORT).show()

                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )

                    setDataSource(
                        "https://cdn.islamic.network/quran/audio/64/${
                           surahRepo.qarieNameToUrl(
                                name
                            )
                        }/$audioId.mp3"
                    )
                    prepareAsync()
                    setOnPreparedListener {
                        start()

                    }
                }
            }

            InternetObserver.Status.NotAvailable -> {
                _toastMessage.value = "لا يوجد اتصال بالانترنت"

//                Toast.makeText(
//                    AyatApplication.getApplicationContext(),
//                    "لا يوجد اتصال بالانترنت",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaSession?.release()

    }


    private fun getSurahh(id: Int) {
        viewModelScope.launch(errorHandler) {
            val quran = surahRepo.getSurahFromJSON()
            ayahsState = quran.data.surahs[id].ayahs
            expandedStates = List(ayahsState.size) { mutableStateOf(false) }

        }

    }


    }



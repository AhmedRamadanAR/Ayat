package com.example.ayat

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.DataManger
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class SurahViewModel(private val app: Application, saveStateHandle: SavedStateHandle) :
    ViewModel() {

    var state by mutableStateOf(emptyList<Ayahs>())
    val lista = DataManger().lista
    var expandedStates by mutableStateOf(List(state.size) { mutableStateOf(false) })
    var selectedQarie by mutableStateOf(lista[0])

    private val networkConnectivityObserver = NetworkConnectivityObserver(app)
    private val _networkStatus = mutableStateOf(InternetObserver.Status.Not_Available)
    private val networkStatus: State<InternetObserver.Status> get() = _networkStatus

    private var mediaSession: MediaSessionCompat? = null
    var mediaPlayer: MediaPlayer? = null
    var mediaProgress by mutableStateOf(0f)
    var mediaDuration by mutableStateOf(0f)




    fun updateProgress() {
        viewModelScope.launch {
            while (mediaPlayer?.isPlaying == true) {
                mediaProgress = mediaPlayer?.currentPosition?.toFloat() ?: 0f
                delay(1000)
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, _ ->
    }


    init {
        val id = saveStateHandle.get<Int>("surahId") ?: 0
        getSurahh(id - 1)
        mediaSession = MediaSessionCompat(app, "SurahPlayer").apply {
            isActive = true
        }
        //  getSurahName()
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                _networkStatus.value = status
            }
        }


    }


    fun playAudio(audioId: Int, name: String) {
        when (networkStatus.value) {
            InternetObserver.Status.Available -> {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                Toast.makeText(app.applicationContext, "جارِِِ التشغيل", Toast.LENGTH_SHORT).show()

                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(
                        "https://cdn.islamic.network/quran/audio/64/${
                            DataManger().qarieNameToUrl(
                                name
                            )
                        }/$audioId.mp3"
                    )
                    prepareAsync()
                    setOnPreparedListener {
                        start()
                        mediaDuration = duration.toFloat()
                        updateProgress()
                    }
                }
            }

            InternetObserver.Status.Not_Available -> {
                Toast.makeText(
                    app.applicationContext,
                    "لا يوجد اتصال بالانترنت",
                    Toast.LENGTH_SHORT
                ).show()
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
            val quran = getSurahFromJSON()
            state = quran.data.surahs.get(id).ayahs
            expandedStates = List(state.size) { mutableStateOf(false) }

        }

    }

    private fun getSurahFromJSON(): Quran {
        val jsonString = readJSONFromAssets("quran.json")
        val gson = Gson()
        val quran = gson.fromJson(jsonString, Quran::class.java)
        return quran
    }

    private fun readJSONFromAssets(path: String): String {
        try {
            app.applicationContext.assets.open(path).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    return reader.readText()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

//    private fun getSurah(id:Int,Edition:String) {
//        viewModelScope.launch(errorHandler) {
//            val surahCall = getSurahFromRemoteToDB(id)
//state=
//
//        }
//    }
//private fun getSurahName(){
//    viewModelScope.launch (errorHandler)  {
//        val surahNameCall= getSurahFromRemoteToDB2()
//        surahDataState=surahNameCall.data.surahs.references
//
//    }
//}

//    private suspend fun getSurahFromRemoteToDB2()=withContext(Dispatchers.IO){apiService.getMetaData()}
//
//  private suspend fun getSurahFromRemoteToDB(id:Int) = withContext(Dispatchers.IO) { apiService.getSurah("${id.toString()}","ar.alafasy") }
}



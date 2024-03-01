package com.example.ayat.presentation.doaa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ayat.data.AyatApiService
import com.example.ayat.Doaa
import com.example.ayat.data.RetrofitClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DoaaViewModel(private  val stateHandle:SavedStateHandle) :ViewModel(){
    var state by  mutableStateOf(emptyList<Doaa>())
     private var apiService: AyatApiService = RetrofitClient.apiServiceInstanceDoaa()
private  val errorHandler= CoroutineExceptionHandler{_,throwable->
    throwable.printStackTrace()

}

init {
    getDoaa()
}
   private  fun getDoaa(){
     viewModelScope.launch(errorHandler) {
         val doaaCall=getDoaaFromRemoteToDB()
         //عشان متعلقة بال ui
         //مسحتها عشان الديفولت اساسا مين
       //  withContext(Dispatchers.Main)
      //   {
             state=doaaCall.restoreDoaa()
     //    }
     }

   }
  private  suspend fun getDoaaFromRemoteToDB()= withContext(Dispatchers.IO){ apiService.getDoaa()}



   fun toggleFavouriteState(doaaText:String)
    {
        val lista=state.toMutableList()
        val itemIndex=lista.indexOfFirst { doaaText == it.doaatext }
        lista[itemIndex]=lista[itemIndex].copy(isFavouite = !lista[itemIndex].isFavouite)
        storeSelectedDoaa(doaa = lista[itemIndex])
        state=lista
        //state=lista.sortedByDescending { it.isFavouite }

    }
    private fun storeSelectedDoaa(doaa: Doaa){
        val savedHandleList=stateHandle.get<List<String>>(fav_doaa).orEmpty().toMutableList()
        if (doaa.isFavouite) savedHandleList.add(doaa.doaatext)
        else savedHandleList.remove(doaa.doaatext)
        stateHandle[fav_doaa]=savedHandleList

    }
    private  fun List<Doaa>.restoreDoaa():List<Doaa>{

        stateHandle.get<List<String>>(fav_doaa)?.let { savedText->
         savedText.forEach{doaaText->
             this.find { it.doaatext==doaaText  }?.isFavouite=true

         }

        }
        return this
    }
    companion object{
        const val fav_doaa="favouriteDoaa"
    }
}


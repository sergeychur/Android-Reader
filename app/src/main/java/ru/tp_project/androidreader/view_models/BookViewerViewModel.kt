package ru.tp_project.androidreader.view_models

import android.content.Context
import androidx.lifecycle.*
import ru.tp_project.androidreader.base.BaseViewModel
import ru.tp_project.androidreader.model.data_models.Pages
import ru.tp_project.androidreader.model.repos.PagesRepository

class BookViewerViewModel : BaseViewModel() {
    var data = MutableLiveData<Pages>()
    private var pagesRep = PagesRepository()

    // get pages of book
    fun get(context: Context, bookID : Int) {
        start()
        pagesRep.get(context, bookID) { isSuccess, pages ->
            if (isSuccess) {
                data.postValue(pages)
            }
            finish(isSuccess)
        }
    }

    // update pages of book
    fun update(context: Context, pages: Pages) {
        start()
        pagesRep.update(context, pages) { isSuccess ->
            if (isSuccess) {
                data.postValue(pages)
            }
            finish(isSuccess)
        }
    }
}
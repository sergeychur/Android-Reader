package ru.tp_project.androidreader.viewModels

import android.app.Application
import androidx.lifecycle.*
import ru.tp_project.androidreader.models.Book
import ru.tp_project.androidreader.repository.BookRepository
import javax.inject.Inject

// https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
// https://www.fandroid.info/viewmodels-with-saved-state/
class BookShelveViewModel @Inject constructor(application: Application): AndroidViewModel(application) {
    private var context = application.applicationContext
    var data = MutableLiveData<Book>()// LiveData<Book> = repository.getBook(userId!!)
    var repository = BookRepository()
    var fail = MutableLiveData<Boolean>().apply { value = false }

    fun refresh() {
        repository.getBook(context) { isSuccess, book ->
            if (isSuccess) {
                data.postValue(book)
            } else {
                fail.postValue(true)
            }
        }
    }
}
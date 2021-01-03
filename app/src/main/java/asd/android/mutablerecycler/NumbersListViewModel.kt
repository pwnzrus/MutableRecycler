package asd.android.mutablerecycler

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NumbersListViewModel : ViewModel() {

    private var recentlyDeletedItems: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var numberOfItems: MutableLiveData<MutableList<Int>> = MutableLiveData()


    init {
        numberOfItems.value = (1..5).toList() as ArrayList<Int>
        var observable = Observable.interval(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io()).subscribe {
                if (recentlyDeletedItems.value.isNullOrEmpty()) {
                    numberOfItems.addNewItemAtRandomIndex()
                } else {
                    numberOfItems.addNewItem(recentlyDeletedItems.getAndRemoveLastItem())
                }
            }

    }

    fun removeItem(item: Int) {
        numberOfItems.removeItem(item)
        recentlyDeletedItems.addItem(item)
        Log.d("test1",recentlyDeletedItems.value.toString())
    }

    //Добавляет в произвольную позицию элемент со значением на 1 больше существующего максимального
    private fun MutableLiveData<MutableList<Int>>.addNewItemAtRandomIndex() {
        val oldValue = this.value ?: ArrayList()
        val item = oldValue.maxOrNull() ?: -1
        oldValue.add((0 until oldValue.size).random(), item + 1)
        this.postValue(oldValue)
    }

    //Добавляет в произвольную позицию элемент с задаваемым значением(Нужно при взятии из пула удаленных)
    private fun MutableLiveData<MutableList<Int>>.addNewItem(item: Int) {
        val oldValue = this.value ?: ArrayList()
        if (oldValue.isEmpty()) {
            oldValue.add(item)
            this.postValue(oldValue)
        } else {
            oldValue.add((0 until oldValue.size).random(), item)
            this.postValue(oldValue)
        }
    }

    //Возвращает а затем удаляет последний элемент из списка
    private fun MutableLiveData<MutableList<Int>>.getAndRemoveLastItem(): Int {
        val oldValue = this.value
        val item: Int? = oldValue?.last()
        oldValue?.removeLast()
        this.postValue(oldValue)
        return item ?: -1
    }

    private fun MutableLiveData<MutableList<Int>>.removeItem(item: Int) {
        val oldValue = this.value
        oldValue?.remove(item)
        this.value = oldValue
    }

    private fun MutableLiveData<MutableList<Int>>.addItem(item:Int){
        val oldValue = this.value?: mutableListOf()
        oldValue.add(item)
        this.value = oldValue
    }
}

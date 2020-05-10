package com.jordilucas.meuhotel.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean
/*Essa classe foi copiada do projeto de exemplo do Google e convertida para Kotlin. O código original encontra-se aqui
(https:// github.com/ googlesamples/ android-architecture/).
*/
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>){
        super.observe(owner, Observer { t ->
            if(pending.compareAndSet(true, false)){
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    @MainThread
    fun call(){
        value = null
    }
}
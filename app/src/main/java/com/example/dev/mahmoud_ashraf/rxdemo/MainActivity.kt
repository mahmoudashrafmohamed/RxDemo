package com.example.dev.mahmoud_ashraf.rxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.internal.util.NotificationLite.disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose




class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Create an Observable that emits data.
        // Below we have created an Observable that emits list of animal names.
        // Here just() operator is used to emit few animal names.

        val animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox")

        /*
        2. Create an Observer that listen to Observable.
         Observer provides the below interface methods to know the the state of Observable.
        onSubscribe(): Method will be called when an Observer subscribes to Observable.
        onNext(): This method will be called when Observable starts emitting the data.
        onError(): In case of any error, onError() method will be called.
        onComplete(): When an Observable completes the emission of all the items, onComplete() will be called.
      */

        val animalsObserver = getAnimalsObserver()

        /*
        3. Make Observer subscribe to Observable so that it can start receiving the data.
        Here, you can notice two more methods, observeOn() and subscribeOn().

        subscribeOn(Schedulers.io()): This tell the Observable to run the task on a background thread.
        observeOn(AndroidSchedulers.mainThread()):
        This tells the Observer to receive the data on android UI thread so that you can take any UI related actions.
         */

        animalsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(animalsObserver);

        /*
        Disposable: Disposable is used to dispose the subscription when
         an Observer no longer wants to listen to Observable.
         In android disposable are very useful in avoiding memory leaks.
         */



    }

    override fun onDestroy() {
        super.onDestroy()

        // don't send events once the activity is destroyed
        disposable?.dispose()
    }

    private fun getAnimalsObserver(): Observer<String> {
        return object : Observer<String> {
           override fun onSubscribe(d: Disposable) {
                Log.e("++++++++", "onSubscribe")

               // to can dispose it when activity destroy
               disposable = d
            }

           override fun onNext(s: String) {
                Log.e("++++++++", "Name: $s")
            }

           override fun onError(e: Throwable) {
                Log.e("++++++++", "onError: " + e.message)
            }

           override fun onComplete() {
                Log.e("++++++++", "All items are emitted!")
            }
        }
    }
}

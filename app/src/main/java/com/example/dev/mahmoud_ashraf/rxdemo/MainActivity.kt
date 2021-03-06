package com.example.dev.mahmoud_ashraf.rxdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver






class MainActivity : AppCompatActivity() {

  //  private var disposable: Disposable? = null
  private val compositeDisposable = CompositeDisposable()

    /**
     * In the below example, you can notice two observers animalsObserver and animalsObserverAllCaps
     * subscribed to same Observable. The both observers
     * receives the same data but the data changes as different operators are applied on the stream.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


         // 1 - observable
        val animalsObservable = getAnimalsObservable()

        /*
        2. Observer that listen to Observable.
         Observer provides the below interface methods to know the the state of Observable.
        onSubscribe(): Method will be called when an Observer subscribes to Observable.
        onNext(): This method will be called when Observable starts emitting the data.
        onError(): In case of any error, onError() method will be called.
        onComplete(): When an Observable completes the emission of all the items, onComplete() will be called.
      */


        // change the type to  DisposableObserver<String>
        val animalsObserver = getAnimalsObserver()
        val animalsObserverAllCaps = getAnimalsAllCapsObserver()

        /*
        3. Make Observer subscribe to Observable so that it can start receiving the data.
        Here, you can notice two more methods, observeOn() and subscribeOn().

        subscribeOn(Schedulers.io()): This tell the Observable to run the task on a background thread.
        observeOn(AndroidSchedulers.mainThread()):
        This tells the Observer to receive the data on android UI thread so that you can take any UI related actions.
         */

        // observer subscribing to observable
       /** animalsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(Predicate<String> { s -> s.toLowerCase().startsWith("b") })
            .subscribeWith(animalsObserver)*/

        /**
         * Wrap with  compositeDisposable.add()
         * filter() is used to filter out the animal names starting with `b`
         * */
        compositeDisposable.add(
            animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { s -> s.toLowerCase().startsWith("b") }
                .subscribeWith(animalsObserver)
        )

        /**
         * filter() is used to filter out the animal names starting with 'c'
         * map() is used to transform all the characters to UPPER case
         * */

        //io.reactivex.functions.


        compositeDisposable.add(
            animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { s -> s.toLowerCase().startsWith("c") }
                .map(object :io.reactivex.functions.Function<String, String> {
                    @Throws(Exception::class)
                   override fun apply(s: String): String {
                        return s.toUpperCase()
                    }
                })
                .subscribeWith(animalsObserverAllCaps)
        )





        /*
        Disposable: Disposable is used to dispose the subscription when
         an Observer no longer wants to listen to Observable.
         In android disposable are very useful in avoiding memory leaks.
         */



    }

    private fun getAnimalsObservable(): Observable<String> {
        return Observable.fromArray(
            "Ant", "Ape",
            "Bat", "Bee", "Bear", "Butterfly",
            "Cat", "Crab", "Cod",
            "Dog", "Dove",
            "Fox", "Frog"
        )
    }

    private fun getAnimalsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {

            override fun onNext(s: String) {
                Log.d("++++++++", "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e("++++++++", "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d("++++++++", "All items are emitted!")
            }
        }
    }

    private fun getAnimalsAllCapsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {


            override fun onNext(s: String) {
                Log.d("++++++++", "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e("++++++++", "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d("++++++++", "All items are emitted!")
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()

        // don't send events once the activity is destroyed
       // disposable?.dispose()
        compositeDisposable.clear()
        Log.e("---","disposed!")
    }

    /*private fun getAnimalsObserver(): Observer<String> {
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
    }*/
}

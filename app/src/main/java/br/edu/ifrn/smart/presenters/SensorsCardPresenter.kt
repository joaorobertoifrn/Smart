package br.edu.ifrn.smart.presenters

import br.edu.ifrn.smart.fragments.SensorsFragment
import br.edu.ifrn.smart.models.SensorObj
import com.pawegio.kandroid.i
import io.realm.Realm
import org.eclipse.paho.client.mqttv3.MqttMessage
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.onError

class SensorsCardPresenter constructor(var view: SensorsFragment?) {

    private var realm = Realm.getDefaultInstance()

    fun onDestroy() {
        view = null
    }

    fun requestSensors() {
        realm?.where(SensorObj::class.java)?.findAll()?.asObservable()?.
                distinctUntilChanged()?.
                observeOn(AndroidSchedulers.mainThread())?.
                doOnNext {
                    view?.onSensorsSuccess(it)
                }?.onError { it.printStackTrace() }?.subscribe()
    }

    fun messageReceived(topic: String, message: MqttMessage?) {
        i { "messageReceived called" }
        if (message == null) return
        realm?.where(SensorObj::class.java)?.equalTo("topic", topic)?.
                findFirst()?.
                asObservable<SensorObj>()?.
                distinctUntilChanged()?.
                observeOn(AndroidSchedulers.mainThread())?.
                doOnNext {
                    realm?.beginTransaction()
                    it.value = message.toString()
                    realm?.commitTransaction()
                    i { it.toString() }
                    view?.reloadSensor(it)
                }?.onError { it.printStackTrace() }?.subscribe()
    }

}

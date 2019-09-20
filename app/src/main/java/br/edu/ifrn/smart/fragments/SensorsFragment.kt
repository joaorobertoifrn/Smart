package br.edu.ifrn.smart.fragments


import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifrn.smart.R
import br.edu.ifrn.smart.activities.AddEditSensorActivity
import br.edu.ifrn.smart.adapters.SensorCardAdapter
import br.edu.ifrn.smart.adapters.SensorListener
import br.edu.ifrn.smart.models.SensorObj
import br.edu.ifrn.smart.presenters.SensorsCardPresenter
import br.edu.ifrn.smart.utils.ConnectionManager
import br.edu.ifrn.smart.utils.MessageReceivedListener
import br.edu.ifrn.smart.utils.ObjectParcer
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_sensors.*
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jetbrains.anko.startActivity
import java.util.*

class SensorsFragment : Fragment(), MessageReceivedListener, SensorListener {

    companion object {
        val TAG = "SensorsFragment"
    }

    var adapter: SensorCardAdapter? = null
    var presenter = SensorsCardPresenter(this)
    var subscribed = HashMap<String, IMqttToken?>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sensors, container, false)
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener { addButtonClicked() }
        ConnectionManager.addRecievedListener(this, TAG)
        presenter.requestSensors()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun addButtonClicked() {
        startActivity<AddEditSensorActivity>()
    }

    override fun messageReceived(topic: String?, message: MqttMessage?) {
        if (topic != null) {
            presenter.messageReceived(topic, message)
        }
    }

    override fun onStop() {
        ConnectionManager.removeRecievedListener(SensorsFragment.TAG)
        super.onStop()
    }


    fun onSensorsSuccess(results: RealmResults<SensorObj>) {
        adapter = SensorCardAdapter(results, this)
        recyclerView.adapter = adapter

        (0..results.lastIndex)
                .map { results[it] }
                .forEach({
                    if (!subscribed.containsKey(it.topic)) {
                        val token = ConnectionManager.client?.subscribe(it.topic, 0)
                        subscribed.put(it.topic, token)
                    }
                })
    }

    fun reloadSensors(results: RealmResults<SensorObj>) {
        adapter?.items = results
        adapter?.notifyDataSetChanged()
    }

    override fun onSensorClicked(sensor: SensorObj) {
        ObjectParcer.putObject(AddEditSensorActivity.SENSOR, sensor)
        startActivity<AddEditSensorActivity>()
    }

    fun reloadSensor(sensor: SensorObj) {
        val index = adapter?.items?.indexOf(sensor)
        if (index != null)
            adapter?.notifyItemChanged(index)
    }
}


package br.edu.ifrn.smart.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.edu.ifrn.smart.R
import br.edu.ifrn.smart.activities.AddEditEquipmentActivity
import br.edu.ifrn.smart.adapters.EquipmentCardAdapter
import br.edu.ifrn.smart.adapters.EquipmentListener
import br.edu.ifrn.smart.models.EquipmentObj
import br.edu.ifrn.smart.presenters.EquipmentsCardPresenter
import br.edu.ifrn.smart.utils.ConnectionManager
import br.edu.ifrn.smart.utils.MessageReceivedListener
import br.edu.ifrn.smart.utils.ObjectParcer
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_equipments.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jetbrains.anko.startActivity

class EquipmentsFragment : Fragment(), MessageReceivedListener, EquipmentListener {

    companion object {
        val TAG = "EquipmentsFragment"
    }

    var adapter: EquipmentCardAdapter? = null
    var presenter = EquipmentsCardPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_equipments, container, false)
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener { addButtonClicked() }
        ConnectionManager.addRecievedListener(this, TAG)
        presenter.requestEquipments()
    }

    override fun onStop() {
        ConnectionManager.removeRecievedListener(TAG)
        super.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun addButtonClicked() {
        startActivity<AddEditEquipmentActivity>()
    }

    override fun messageReceived(topic: String?, message: MqttMessage?) {
        if (topic != null) {
            presenter.messageRecieved(topic, message)
        }
    }

    override fun onEquipmentClicked(equipment: EquipmentObj) {
        ObjectParcer.putObject(AddEditEquipmentActivity.EQUIPMENT, equipment)
        startActivity<AddEditEquipmentActivity>()
    }

    fun reloadEquipments() {
        adapter?.notifyDataSetChanged()
    }

    fun onEquipmentsSuccess(response: RealmResults<EquipmentObj>) {
        adapter = EquipmentCardAdapter(response, this)
        recyclerView.adapter = adapter

        response.forEach {
            ConnectionManager.client?.subscribe(it.topic, 0)
        }
    }

    override fun onStateChanged(equipment: EquipmentObj, state: Boolean) {
        presenter.stateChanged(equipment, state)
    }
}


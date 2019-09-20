package br.edu.ifrn.smart.presenters

import android.support.v7.app.AppCompatActivity
import br.edu.ifrn.smart.activities.DashboardActivity
import br.edu.ifrn.smart.fragments.EquipmentsFragment
import br.edu.ifrn.smart.fragments.SensorsFragment
import br.edu.ifrn.smart.models.ConnectionObj
import br.edu.ifrn.smart.utils.ConnectionManager
import br.edu.ifrn.smart.utils.MyConstants
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardPresenter(var view: DashboardActivity?) {

    fun onCreate() {
        val rootViewID = view?.contentView?.id ?: return
        view?.contentView?.removeAllViews()

        view?.fragmentManager?.beginTransaction()
                ?.add(rootViewID, SensorsFragment(), SensorsFragment.TAG)
                ?.add(rootViewID, EquipmentsFragment(), EquipmentsFragment.TAG)
                ?.commit()
    }

    fun onRestart() {
        if (!(ConnectionManager.client?.isConnected ?: false)) {
            //Disconnected!!
            if (view == null) return
            ConnectionManager.connect(view!!.applicationContext)
        }
    }

    fun onDestroy() {
        view = null
    }

    fun onDisconnectOptionItemSelected() {
        view?.showProgressDialog()
        ConnectionManager.disconnect()
        val connectionObj = readSharedPreferences()
        clearSharedPreferences()
        view?.dismissProgressDialog()
        view?.goToMainActivity(connectionObj)
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = view?.getSharedPreferences(MyConstants.SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        sharedPreferences?.edit()?.clear()?.apply()
    }

    private fun readSharedPreferences(): ConnectionObj? {
        val prefs = view?.getSharedPreferences(MyConstants.SHARED_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        val serverUrl = prefs?.getString(MyConstants.SERVER_URL, "")
        val serverPort = prefs?.getString(MyConstants.SERVER_PORT, "")
        val user = prefs?.getString(MyConstants.SERVER_USER, null)
        val password = prefs?.getString(MyConstants.SERVER_USER_PASSWORD, null)
        if (serverPort == null || serverUrl == null) return null
        return ConnectionObj(serverUrl, serverPort, user, password)
    }

}
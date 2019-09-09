package macro.dashboard.neptunes.config

import org.apache.http.HttpHost
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Created by Macro303 on 2019-Sep-09
 */
internal class Connection {
	var hostName: String? = null
	var port: Int? = null

	fun getProxy(): Proxy? {
		hostName ?: return null
		port ?: return null
		return Proxy(Proxy.Type.HTTP, InetSocketAddress(hostName, port!!))
	}

	fun getHttpHost(): HttpHost? {
		hostName ?: return null
		port ?: return null
		return HttpHost(hostName, port!!)
	}

	override fun toString(): String {
		return "Connection(hostName=$hostName, port=$port)"
	}
}
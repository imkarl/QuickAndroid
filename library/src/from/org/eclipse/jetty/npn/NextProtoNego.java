package from.org.eclipse.jetty.npn;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;

public class NextProtoNego {
	/**
	 * <p>
	 * Enables debug logging on {@link System#err}.
	 * </p>
	 */
	public static boolean debug = false;

	private static Map<Object, Provider> objects = Collections
			.synchronizedMap(new WeakHashMap<Object, Provider>());

	private NextProtoNego() {
	}

	/**
	 * <p>
	 * Registers a SSLSocket with a provider.
	 * </p>
	 * 
	 * @param socket
	 *            the socket to register with the provider
	 * @param provider
	 *            the provider to register with the socket
	 */
	public static void put(SSLSocket socket, Provider provider) {
		objects.put(socket, provider);
	}

	/**
	 * @param socket
	 *            a socket registered with {@link #put(SSLSocket, Provider)}
	 * @return the provider registered with the given socket
	 */
	public static Provider get(SSLSocket socket) {
		return objects.get(socket);
	}

	/**
	 * <p>
	 * Registers a SSLEngine with a provider.
	 * </p>
	 * 
	 * @param engine
	 *            the engine to register with the provider
	 * @param provider
	 *            the provider to register with the engine
	 */
	public static void put(SSLEngine engine, Provider provider) {
		objects.put(engine, provider);
	}

	/**
	 * 
	 * @param engine
	 *            an engine registered with {@link #put(SSLEngine, Provider)}
	 * @return the provider registered with the given engine
	 */
	public static Provider get(SSLEngine engine) {
		return objects.get(engine);
	}

	/**
	 * <p>
	 * Base, empty, interface for providers.
	 * </p>
	 */
	public interface Provider {
	}

	/**
	 * <p>
	 * The client-side provider interface that applications must implement to
	 * interact with the negotiation of the next protocol.
	 * </p>
	 */
	public interface ClientProvider extends Provider {
		/**
		 * <p>
		 * Callback invoked to let the implementation know whether an empty NPN
		 * extension should be added to a ClientHello SSL message.
		 * </p>
		 * 
		 * @return true to add the NPN extension, false otherwise
		 */
		public boolean supports();

		/**
		 * <p>
		 * Callback invoked to let the application know that the server does not
		 * support NPN.
		 * </p>
		 */
		public void unsupported();

		/**
		 * <p>
		 * Callback invoked to let the application select a protocol among the
		 * ones sent by the server.
		 * </p>
		 * 
		 * @param protocols
		 *            the protocols sent by the server
		 * @return the protocol selected by the application, or null if the
		 *         NextProtocol SSL message should not be sent to the server
		 */
		public String selectProtocol(List<String> protocols);
	}

	/**
	 * <p>
	 * The server-side provider interface that applications must implement to
	 * interact with the negotiation of the next protocol.
	 * </p>
	 */
	public interface ServerProvider extends Provider {
		/**
		 * <p>
		 * Callback invoked to let the application know that the client does not
		 * support NPN.
		 * </p>
		 */
		public void unsupported();

		/**
		 * <p>
		 * Callback invoked to let the implementation know the list of protocols
		 * that should be added to an NPN extension in a ServerHello SSL
		 * message.
		 * </p>
		 * <p>
		 * This callback is invoked only if the client sent a NPN extension.
		 * </p>
		 * 
		 * @return the list of protocols, or null if no NPN extension should be
		 *         sent to the client
		 */
		public List<String> protocols();

		/**
		 * <p>
		 * Callback invoked to let the application know the protocol selected by
		 * the client.
		 * </p>
		 * <p>
		 * This callback is invoked only if the client sent a NextProtocol SSL
		 * message.
		 * </p>
		 * 
		 * @param protocol
		 *            the selected protocol
		 */
		public void protocolSelected(String protocol);
	}
}

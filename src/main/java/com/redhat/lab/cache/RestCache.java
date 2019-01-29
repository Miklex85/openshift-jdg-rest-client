package com.redhat.lab.cache;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.redhat.lab.util.Base64;

/**
 * A cache working over REST (only few basic operations)
 * 
 * @author Manik Surtani
 * 
 */
public class RestCache<K, V> implements ConcurrentMap<K, V> {
	
    private static final String JDG_HOST = "jdg.host";
    private static final String HTTP_PORT = "jdg.http.port";
    private static final String REST_CONTEXT_PATH = "jdg.rest.context.path";
    private static final String CACHE_NAME = "jdg.cache.name";
    private static final String PROPERTIES_FILE = "/etc/config/jdg.properties";

    private String cacheName;
	
    private String basicUrl;

    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    public RestCache() {
    	cacheName = jdgProperty(CACHE_NAME);
        this.basicUrl = "http://" + jdgProperty(JDG_HOST) + ":" + jdgProperty(HTTP_PORT) + jdgProperty(REST_CONTEXT_PATH) + "/" + cacheName;
    }

    private String doOperation(String method, String key, Object value) {
        try {
            URL url = key == null ? new URL(basicUrl) : new URL(basicUrl + "/" + key);
            String userCredentials = "ispnadmin:n0t13n3!";
            String basicAuth = "Basic " + new String(Base64.encodeBytes(userCredentials.getBytes()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "text/plain");
            int read = 0;
            byte[] buffer = new byte[1024 * 8];

            if (method.equals(PUT)) {
                connection.setDoOutput(true);
                String payload = Base64.encodeObject((Serializable) value);
                BufferedOutputStream output = new BufferedOutputStream(connection.getOutputStream());
                output.write(payload.getBytes());
                output.close();
            }

            connection.connect();
            InputStream responseBodyStream = connection.getInputStream();
            StringBuffer responseBody = new StringBuffer();
            while ((read = responseBodyStream.read(buffer)) != -1) {
                responseBody.append(new String(buffer, 0, read));
            }
            connection.disconnect();
            String response = responseBody.toString();
            return response;
        } catch (FileNotFoundException fnfe) {
            // Could be that the key being queried does not exist. Return null.
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toStringKey(Object key) {
        if (key instanceof String) {
            return (String) key;
        } else {
            throw new UnsupportedOperationException("RESTful cache only allows String keys!");
        }
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Unsupported");
    }

    private Object decode(String s) {
        if (s == null || s.length() == 0) {
            return null;
        } else {
            Object o = Base64.decodeToObject(s);
            return o;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        String stringKey = toStringKey(key);
        String stringValue = doOperation(GET, stringKey, null);
        return (V) decode(stringValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        String stringKey = toStringKey(key);
        String stringValue = doOperation(PUT, stringKey, value);

        return (V) decode(stringValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        String stringKey = toStringKey(key);
        String stringValue = doOperation(DELETE, stringKey, null);

        return (V) decode(stringValue);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public V replace(K key, V value) {
        return put(key, value);
    }
    
    public static String jdgProperty(String name) {
        Properties props = new Properties();
        InputStream input = null;
        try {
        	input = new FileInputStream(PROPERTIES_FILE);
            props.load(input);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return props.getProperty(name);
    }
}

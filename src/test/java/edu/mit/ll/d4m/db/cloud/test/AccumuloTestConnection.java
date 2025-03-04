package edu.mit.ll.d4m.db.cloud.test;

import edu.mit.ll.cloud.connection.ConnectionProperties;
import edu.mit.ll.d4m.db.cloud.accumulo.AccumuloConnection;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
//import org.apache.accumulo.core.client.ClientConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Assert;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * Created by accumulo on 11/6/14.
 */
public class AccumuloTestConnection {
    private ConnectionProperties connectionProperties;
    private AccumuloConnection connection;


    public AccumuloTestConnection(String filename) {
        // Setup Connection
        //ClientConfiguration config;
        PropertiesConfiguration properties;
        String user,pass;
        try {
            properties = new PropertiesConfiguration();
            properties.read(new BufferedReader(new FileReader(filename)));
            String instancename = properties.getString("instancename");
            String zooserver = properties.getString("zooserver");
            user = properties.getString("user");
            pass = properties.getString("pw");
            int timeout = Integer.parseInt(properties.getString("timeout"));
            //config = ClientConfiguration.loadDefault().withInstance(instancename).withZkHosts(zooserver).withZkTimeout(timeout);
            connectionProperties = new ConnectionProperties(zooserver,user,pass,instancename,null);

        } catch (ConfigurationException e ) {
            Assert.fail("Couldn't find a valid properties file named " + filename);
        } catch ( IOException ioe) {
            Assert.fail("Couldn't find a valid properties file named " + filename);
        }
        try {
            connection = new AccumuloConnection(connectionProperties);
        } catch (AccumuloSecurityException e) {
            Assert.fail("AccumuloSecurityException: " + e.getMessage());
        } catch (AccumuloException e) {
            Assert.fail("AccumuloException: " + e.getMessage());
        }
    }

    public final ConnectionProperties getConnectionProperties() {
        return connectionProperties;
    }

    public AccumuloConnection getAccumuloConnection() {
        return connection;
    }
}

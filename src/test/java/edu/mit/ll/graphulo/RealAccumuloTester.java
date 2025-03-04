package edu.mit.ll.graphulo;

import org.apache.accumulo.core.client.Accumulo;
import org.apache.accumulo.core.client.AccumuloClient;
import org.apache.accumulo.core.clientImpl.ClientContext;
import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.rules.ExternalResource;
import java.util.Properties;

public class RealAccumuloTester extends ExternalResource implements IAccumuloTester {
    private static final Logger log = LoggerFactory.getLogger(RealAccumuloTester.class);


    //private ClientConfiguration cc;
    private String username = "root";
    private PasswordToken auth;

    //private Instance instance;
    private String instanceName=null;
    private String zookeeperHost=null;
    private AccumuloClient client=null;
    public RealAccumuloTester(String instanceName, String zookeeperHost,
                              String username, PasswordToken auth) {
        //cc = ClientConfiguration.loadDefault().withInstance(instanceName).withZkHosts(zookeeperHost); // .withZkTimeout(timeout)
        this.instanceName = instanceName;
        this.zookeeperHost = zookeeperHost;
        this.username = username;
        this.auth = auth;
    }

    public AccumuloClient getConnector() {
        //Connector c;

       //try {
            //c = instance.getConnector(username, auth);
        //} catch (AccumuloException | AccumuloSecurityException e) {
       //     log.error("failed to connect to Accumulo instance "+instance.getInstanceName(),e);
       //     throw new RuntimeException(e);
       // }
        return this.client;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public PasswordToken getPassword() {
        return auth;
    }

    @Override
    protected void before() throws Throwable {
//        instance = new ZooKeeperInstance(cc.get(ClientConfiguration.ClientProperty.INSTANCE_NAME),
//                                    cc.get(ClientConfiguration.ClientProperty.INSTANCE_ZK_HOST));
        //instance = new ZooKeeperInstance(cc);
        /*
        Properties props = new Properties();
        props.put("instance.name", this.instanceName);
        props.put("instance.zookeepers", this.zookeeperHost);
        props.put("auth.type", "password")
        props.put("auth.principal", this.username);
        props.put("auth.token", new String(this.auth.getPassword(),StandardCharsets.UTF_8));
        */
        //AccumuloClient client = Accumulo.newClient().from(props).build();
        this.client = Accumulo.newClient().to(this.instanceName,this.zookeeperHost).as(this.username,this.auth).build();
        ClientContext cc = (ClientContext)this.client;
        log.debug("setUp ok - Zookeeper="+ cc.getZooKeepers()+", instance=" +  cc.getInstanceName());
    }

//    @Override
//    protected void after() {
//        //log.debug("tearDown ok - instance destroyed");
//    }
}

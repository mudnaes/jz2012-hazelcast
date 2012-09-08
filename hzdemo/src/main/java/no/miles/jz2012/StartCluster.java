
package no.miles.jz2012;
import com.hazelcast.config.Config;
import com.hazelcast.config.Join;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;

public class StartCluster {

    /**
     * @param args
     */
    public static void main(String[] args) {
    	
    	Config cfg = new Config();
    	cfg.setPortAutoIncrement(false);
    	        
    	NetworkConfig network = cfg.getNetworkConfig();
    	Join join = network.getJoin();
    	join.getMulticastConfig().setEnabled(true);
    	        
     	MapConfig fib1 = new MapConfig();
    	fib1.setName("no-backup");
    	fib1.setBackupCount(0);
    	fib1.getMaxSizeConfig().setSize(1000000);
    	
    	MapConfig fib2 = new MapConfig();
    	fib2.setName("two-backup");
    	fib2.setBackupCount(4);
    	fib2.getMaxSizeConfig().setSize(1000000);

     	cfg.addMapConfig(fib1);
    	cfg.addMapConfig(fib2);
    	Hazelcast.init(cfg);
    }
}


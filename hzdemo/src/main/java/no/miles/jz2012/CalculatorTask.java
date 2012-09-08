package no.miles.jz2012;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class CalculatorTask implements Callable<String>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6009475745001921187L;

	private String mapName;

	public CalculatorTask(String mapName) {
		this.mapName = mapName;
	}

	public String call() throws Exception {
		System.out.println("Starting processing");
		IMap<Integer, FibonacciNumber> fibs = Hazelcast.getMap(mapName);
		long startTime = System.nanoTime();
		Set<Integer> keys = fibs.localKeySet();
		System.out.println("Number of keys found: " + keys.size());
		for (Integer fibNumberToCalculate : keys) {

			FibonacciNumber number = fibs.get(fibNumberToCalculate);
			number.calculateFinbonacci();
			fibs.replace(fibNumberToCalculate, number);
		}

		return Hazelcast.getCluster().getLocalMember().getInetAddress() + " : " //
				+ " nums fibs processed: " + keys.size() //
				+ " in " + ((System.nanoTime() - startTime) / 1000000) + " ms";
	}

}

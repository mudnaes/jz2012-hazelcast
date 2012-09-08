package no.miles.jz2012;

import java.io.Serializable;
import java.math.BigInteger;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.PartitionAware;

/**
 * Class to calculate fibonacci number
 * @author morten
 *
 */
public class FibonacciNumber implements Serializable, PartitionAware<Integer>{
	private static final long serialVersionUID = 3844427480105090021L;
	
	private int number;
	private BigInteger result;
	private boolean processed;
	private long processingTime;
	private String processedBy;

	public FibonacciNumber(int numberToFind) {
		this.number = numberToFind;
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public BigInteger getResult() {
		return result;
	}

	public void setResult(BigInteger result) {
		this.result = result;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	private BigInteger calculate(int n) {
		BigInteger a = BigInteger.ZERO;
		BigInteger b = BigInteger.ONE;
		for (int i = 0; i < n; i++) {
			BigInteger c = a.add(b);
			a = b;
			b = c;
		}
		return a;
	}

	public void calculateFinbonacci() {
		long startTime = System.nanoTime();
		result = calculate(number);
		processingTime = (System.nanoTime() - startTime) / 1000000;
		processedBy = Hazelcast.getCluster().getLocalMember().toString();
		
	}

	@Override 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FibonacciNumber other = (FibonacciNumber) obj;
		if (number != other.number)
			return false;
		return true;
	}

	public Integer getPartitionKey() {
		// TODO Auto-generated method stub
		return number;
	}
	
	

}

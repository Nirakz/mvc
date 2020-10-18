package pojo;

public class Notification {
	private String idFrom;
	private int count;
	public String getIdFrom() {
		return idFrom;
	}
	public void setIdFrom(String idFrom) {
		this.idFrom = idFrom;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Notification(String idFrom, int count) {
		this.idFrom = idFrom;
		this.count = count;
	}
	public Notification() {
		// TODO Auto-generated constructor stub
	}
	
	
}

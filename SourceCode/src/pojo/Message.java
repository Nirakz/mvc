package pojo;


import java.util.Date;

public class Message {
	private int id;
	private String idFrom;
	private String idTo;
	private int type;
	private Date time;
	private boolean offline;
	private String content;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdFrom() {
		return idFrom;
	}
	public void setIdFrom(String idFrom) {
		this.idFrom = idFrom;
	}
	public String getIdTo() {
		return idTo;
	}
	public void setIdTo(String idTo) {
		this.idTo = idTo;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public boolean isOffline() {
		return offline;
	}
	public void setOffline(boolean offline) {
		this.offline = offline;
	}
		
}	

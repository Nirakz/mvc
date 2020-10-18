package pojo;


import java.util.Set;

public class UserOnlineData {
	private Set<String> listUserOnline;
	private String idChangedUser;
	public UserOnlineData(Set<String> list, String id) {
		this.listUserOnline = list;
		this.idChangedUser = id;
	}
	public Set<String> getListUserOnline() {
		return listUserOnline;
	}
	public void setListUserOnline(Set<String> listUserOnline) {
		this.listUserOnline = listUserOnline;
	}
	public String getIdLastUser() {
		return idChangedUser;
	}
	public void setIdLastUser(String idLastUser) {
		this.idChangedUser = idLastUser;
	}
	
}

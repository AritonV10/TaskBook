package taskbook.v1.business.group.entity;

public class GroupForm {
	
	private String name;
	private String desc;
	
	public GroupForm(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

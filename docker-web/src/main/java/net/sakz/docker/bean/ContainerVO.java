package net.sakz.docker.bean;

public class ContainerVO {

    @IgnoreProperty
    @Header(name = "CONTAINER ID")
    private String id;

    @Header(name = "IMAGE")
    private String image;

    @Header(name = "COMMAND")
    private String command;

    @Header(name = "CREATED")
    private String created;

    @IgnoreProperty
    @Header(name = "STATUS")
    private String status;

    @IgnoreProperty
    @Header(name = "PORTS")
    private String ports;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

    
}

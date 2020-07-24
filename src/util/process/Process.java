package util.process;

public class Process {
    private String name;
    private String session;
    private int uid;
    private float memoryUse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getMemoryUse() {
        return memoryUse;
    }

    public void setMemoryUse(float memoryUse) {
        this.memoryUse = memoryUse;
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", session='" + session + '\'' +
                ", sessionNumber=" + uid +
                ", memoryUse=" + memoryUse +
                '}';
    }

}

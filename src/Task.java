public class Task {
    protected int id;
    protected String title;
    protected String info;
    protected TaskStatus status;

    public Task(int id, String title, String info) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.status = TaskStatus.NEW;
    }

    public int getId() {
        return id;
    }
}
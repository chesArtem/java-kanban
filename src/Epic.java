import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Subtask> listSubtask;
    public Epic(int id, String title, String info) {
        super(id, title, info);
        this.listSubtask = new ArrayList<Subtask>();
    }
    void addSubtask(Subtask subtask){
        listSubtask.add(subtask);
    }

    public ArrayList<Subtask> getListSubtask() {
        return listSubtask;
    }
}

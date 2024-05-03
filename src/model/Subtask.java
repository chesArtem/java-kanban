package model;

public class Subtask extends Task{
    private Epic parentEpic;
    public Subtask(int id, String title, String info, Epic parentEpic) {
        super(id, title, info);
        this.parentEpic = parentEpic;
        parentEpic.addSubtask(this);
    }

    public Epic getParentEpic() {
        return parentEpic;
    }
}

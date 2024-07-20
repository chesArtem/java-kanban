package service.History;

import model.ListNode;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, ListNode<Task>> historyList = new HashMap<>();
    private ListNode<Task> head;
    private ListNode<Task> tail;

    @Override
    public void add(Task task){
        ListNode<Task> newNode = new ListNode<>(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.addNext(newNode);
            tail = newNode;
        }
        if (historyList.containsKey(task.getId())) {
            remove(task.getId());
        }
        historyList.put(task.getId(), newNode);
    }
    @Override
    public void remove(int id) {
        ListNode<Task> oldNode = historyList.get(id);
        if (oldNode == head) {
            head = head.getNext();
        }
        if (oldNode == tail) {
            tail = tail.getPrevious();
        }
        oldNode.remove();
    }
    @Override
    public List<Task> getHistory(){
        return head != null ? head.toList() : Collections.EMPTY_LIST;
    }
}

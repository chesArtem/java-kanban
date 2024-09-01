package service.History;

import model.Entity;
import model.ListNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, ListNode<Entity>> historyList = new HashMap<>();

    private ListNode<Entity> head;

    private ListNode<Entity> tail;

    public static final Logger logger = Logger.getLogger(InMemoryHistoryManager.class.getName());

    @Override
    public void add(Entity task) {
        if (historyList.containsKey(task.getId())) {
            remove(task.getId());
        }
        ListNode<Entity> newNode = new ListNode<>(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.addNext(newNode);
            tail = newNode;
        }
        historyList.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        ListNode<Entity> oldNode = historyList.get(id);
        if (oldNode == null) {
            logger.info("there is no ID-" + id + " element in the history");
        }
        if (oldNode == head) {
            head = head.getNext();
        }
        if (oldNode == tail) {
            tail = tail.getPrevious();
        }
        if (oldNode != null) {
            oldNode.remove();
        }
    }

    @Override
    public List<Entity> getHistory() {
        return head != null ? head.toList() : Collections.EMPTY_LIST;
    }

}

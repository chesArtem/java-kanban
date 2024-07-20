package model;

import java.util.ArrayList;
import java.util.List;

public class ListNode<T> {
    private final T element;
    private ListNode<T> previous;
    private ListNode<T> next;

    public ListNode(T element) {
        this.element = element;
    }

    public List<T> toList() {
        if (next == null) {
            return List.of(element);
        }
        List<T> result = new ArrayList<>();
        result.add(element);
        result.addAll(next.toList());
        return result;
    }

    public T getElement() {
        return element;
    }

    public ListNode<T> getPrevious() {
        return previous;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void remove() {
        if (previous != null) {
            previous.next = next;
        }
        if (next != null) {
            next.previous = previous;
        }
    }

    public void addNext(ListNode<T> next) {
        next.previous = this;
        next.next = this.next;
        this.next = next;
    }

    public void addPrevious(ListNode<T> previous) {
        previous.next = this;
        previous.previous = this.previous;
        this.previous = previous;
    }
}

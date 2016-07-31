package com.oxo.haiti.model;

/**
 * Created by jaswinderwadali on 17/07/16.
 */
public class Positions {
    int next = 0;
    int prev = 0;

  public   Positions(int next, int last) {
        this.next=next;
        this.prev=last;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }
}

package com.polytech.smtp.utils.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Observable {

    public List<Observer> observers;

    public Observable() {
        observers = new ArrayList<Observer>();
    }

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void notifyObservers(Object o) {
        Iterator<Observer> i = observers.iterator();
        while (i.hasNext()) {
            i.next().update(this, o);
        }
    }
    
    public void removeAllObservers()
    {
    	observers = new ArrayList<Observer>();
    }
}

package com.dahuaboke.mpda.core.context;

import com.dahuaboke.mpda.core.exception.MpdaInvocationException;

import java.util.Collection;
import java.util.LinkedList;

/**
 * auth: dahua
 * time: 2025/9/20 10:45
 */
public class LimitedList<L> extends LinkedList<L> {

    private final int limitSize;

    public LimitedList(int limitSize) {
        this.limitSize = limitSize;
    }

    @Override
    public boolean add(L l) {
        super.add(l);
        while (size() > limitSize) {
            remove();
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends L> c) {
        throw new MpdaInvocationException();
    }
}
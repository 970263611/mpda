package com.dahuaboke.mpda.core.context;

import com.dahuaboke.mpda.core.event.ChangeEvent;
import com.dahuaboke.mpda.core.event.Event;
import com.dahuaboke.mpda.core.event.EventPublisher;
import com.dahuaboke.mpda.core.event.MessageChangeEvent;
import com.dahuaboke.mpda.core.event.TraceChangeEvent;
import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.dahuaboke.mpda.core.memory.MessageWrapper;
import com.dahuaboke.mpda.core.trace.TraceMessage;
import com.dahuaboke.mpda.core.utils.SpringUtil;


import static com.dahuaboke.mpda.core.event.ChangeEvent.Type.ADDED;
import static com.dahuaboke.mpda.core.event.ChangeEvent.Type.REMOVED;

/**
 * auth: dahua
 * time: 2025/9/21 13:30
 */
public class LimitedListWrapper<L> extends LimitedList<L> {

    public LimitedListWrapper(int limitSize) {
        super(limitSize);
    }

    @Override
    public boolean add(L l) {
        boolean add = super.add(l);
        publishEvent(l, ADDED);
        return add;
    }

    @Override
    public L remove() {
        if (size() > 0) {
            L remove = super.remove();
            publishEvent(remove, REMOVED);
            return remove;
        }
        return null;
    }

    private void publishEvent(L l, ChangeEvent.Type changeEventType) {
        EventPublisher publisher = SpringUtil.getBean(EventPublisher.class);
        Event event;
        if (l instanceof TraceMessage traceMessage) {
            event = new TraceChangeEvent(traceMessage, changeEventType);
        } else if (l instanceof MessageWrapper messageWrapper) {
            event = new MessageChangeEvent(messageWrapper, changeEventType);
        } else {
            throw new MpdaIllegalArgumentException();
        }
        publisher.publish(event);
    }

}

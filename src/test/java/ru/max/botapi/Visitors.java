package ru.max.botapi;

import ru.max.botapi.model.Update;


public class Visitors {
    public static Update.Visitor noDuplicates(Update.Visitor delegate) {
        return new ProhibitDuplicatesUpdateVisitor(delegate);
    }

    public static VisitedUpdatesTracer tracing(Update.Visitor delegate) {
        return new VisitedUpdatesTracer(delegate);
    }
}

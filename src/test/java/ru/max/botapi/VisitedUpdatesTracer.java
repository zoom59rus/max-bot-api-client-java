package ru.max.botapi;

import java.util.HashSet;
import java.util.Set;

import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.ChatTitleChangedUpdate;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.MessageEditedUpdate;
import ru.max.botapi.model.MessageRemovedUpdate;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.UserAddedToChatUpdate;
import ru.max.botapi.model.UserRemovedFromChatUpdate;


public class VisitedUpdatesTracer extends DelegatingUpdateVisitor {
    private final Set<String> notVisited = new HashSet<>(Update.TYPES);
    private final Set<String> visited = new HashSet<>();

    public VisitedUpdatesTracer(Update.Visitor delegate) {
        super(delegate);
    }

    @Override
    public void visit(MessageCreatedUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(BotStartedUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        super.visit(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    @Override
    public void visitDefault(Update model) {
        super.visitDefault(model);
        notVisited.remove(model.getType());
        visited.add(model.getType());
    }

    public Set<String> getNotVisited() {
        return notVisited;
    }

    public Set<String> getVisited() {
        return visited;
    }
}

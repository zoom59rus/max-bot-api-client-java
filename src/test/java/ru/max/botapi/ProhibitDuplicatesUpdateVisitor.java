package ru.max.botapi;

import ru.max.botapi.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;


public class ProhibitDuplicatesUpdateVisitor extends DelegatingUpdateVisitor {
    private final Set<Update> visited = new HashSet<>();

    ProhibitDuplicatesUpdateVisitor(Update.Visitor delegate) {
        super(delegate);
    }

    @Override
    public void visit(MessageCreatedUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(BotStartedUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        saveAndCheck(model);
        super.visit(model);
    }

    @Override
    public void visitDefault(Update model) {
        saveAndCheck(model);
        super.visitDefault(model);
    }

    private void saveAndCheck(Update model) {
        boolean isNew = visited.add(model);
        if (!isNew) {
            fail(model + " is visited twice");
        }
    }
}

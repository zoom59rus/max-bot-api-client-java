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

import static org.junit.Assert.fail;


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

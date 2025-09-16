package ru.max.botapi.model;

import static org.junit.jupiter.api.Assertions.fail;


public class FailByDefaultButtonVisitor implements Button.Visitor {
    @Override
    public void visit(CallbackButton model) {
        shouldNotHappens();
    }

    @Override
    public void visit(LinkButton model) {
        shouldNotHappens();
    }

    @Override
    public void visit(RequestGeoLocationButton model) {
        shouldNotHappens();
    }

    @Override
    public void visit(RequestContactButton model) {
        shouldNotHappens();
    }

    @Override
    public void visit(ChatButton model) {
        shouldNotHappens();
    }

    @Override
    public void visitDefault(Button model) {
        shouldNotHappens();
    }

    private static void shouldNotHappens() {
        fail("Should not happens");
    }
}

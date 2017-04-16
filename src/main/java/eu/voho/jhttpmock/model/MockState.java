package eu.voho.jhttpmock.model;

/**
 * Created by vojta on 15/04/2017.
 */
public class MockState {
    private final MockBehaviour behaviour;
    private final MockInteractions interactions;

    public MockState() {
        this.behaviour = new MockBehaviour();
        this.interactions = new MockInteractions();
    }

    public MockBehaviour getBehaviour() {
        return behaviour;
    }

    public MockInteractions getInteractions() {
        return interactions;
    }
}

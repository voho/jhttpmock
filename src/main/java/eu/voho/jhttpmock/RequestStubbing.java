package eu.voho.jhttpmock;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.number.OrderingComparison;

/**
 * Created by vojta on 16/04/2017.
 */
public interface RequestStubbing {
    RequestStubbing custom(Matcher<RequestWrapper> requestWrapper);

    RequestStubbing withUrl(Matcher<String> urlMatcher);

    default RequestStubbing withUrlEqualTo(String url) {
        return withUrl(CoreMatchers.equalTo(url));
    }

    default RequestStubbing withUrlStartingWith(String urlPrefix) {
        return withUrl(CoreMatchers.startsWith(urlPrefix));
    }

    default RequestStubbing withUrlEndingWith(String urlSuffix) {
        return withUrl(CoreMatchers.endsWith(urlSuffix));
    }

    ResponseStubbing thenRespond();

    void wasReceivedTimes(Matcher<Integer> timesMatcher);

    default void wasReceivedOnce() {
        wasReceivedTimes(CoreMatchers.equalTo(1));
    }

    default void wasReceivedNever() {
        wasReceivedTimes(CoreMatchers.equalTo(0));
    }

    default void wasReceivedAtMost(int maxNumber) {
        wasReceivedTimes(OrderingComparison.lessThanOrEqualTo(maxNumber));
    }

    default void wasReceivedAtLeast(int minNumber) {
        wasReceivedTimes(OrderingComparison.greaterThanOrEqualTo(minNumber));
    }
}

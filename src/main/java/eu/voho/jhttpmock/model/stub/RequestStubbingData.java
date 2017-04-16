package eu.voho.jhttpmock.model.stub;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import org.hamcrest.Matcher;

import java.util.function.Predicate;

/**
 * Created by vojta on 14/04/2017.
 */
public class RequestStubbingData {
    private Matcher<String> urlMatcher;

    public Predicate<RequestWrapper> asPredicate() {
        return (request -> urlMatcher.matches(request.getUrl()));
    }

    public void setUrlMatcher(Matcher<String> urlMatcher) {
        this.urlMatcher = urlMatcher;
    }
}

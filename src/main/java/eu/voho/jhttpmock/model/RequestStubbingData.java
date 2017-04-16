package eu.voho.jhttpmock.model;

import java.util.function.Predicate;

/**
 * Created by vojta on 14/04/2017.
 */
public class RequestStubbingData {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public Predicate<RequestWrapper> asPredicate() {
        return (request -> {
            // TODO
            return url.equalsIgnoreCase(request.getUrl());

        });
    }
}

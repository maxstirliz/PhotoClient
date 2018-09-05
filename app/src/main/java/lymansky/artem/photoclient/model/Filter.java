package lymansky.artem.photoclient.model;

public class Filter {

    private String searchQuery;

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public boolean isNullContent() {
        return searchQuery == null || searchQuery.equals("");
    }
}

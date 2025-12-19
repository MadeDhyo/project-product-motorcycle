package project_testing.demo.model;

import java.util.List;

public class ItemListResponse {
    private int totalItems;
    private List<Item> items;

    public ItemListResponse (int totalItems, List<Item> items) {
        this.totalItems = totalItems;
        this.items = items;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public List<Item> getItems() {
        return items;
    }
}

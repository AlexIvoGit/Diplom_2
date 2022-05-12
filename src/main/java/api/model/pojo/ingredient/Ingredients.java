package api.model.pojo.ingredient;

import java.util.List;

public class Ingredients {
    private List<Ingredient> data;

    public Ingredients() {

    }

    public Ingredients(List<Ingredient> data) {
        this.data = data;
    }

    public List<Ingredient> getData() {
        return data;
    }
}


package api.model.pojo.order;

import api.model.pojo.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.Date;

public class Order{
    public ArrayList<Ingredient> ingredients;
    public String _id;
    public Owner owner;
    public String status;
    public String name;
    public Date createdAt;
    public Date updatedAt;
    public int number;
    public int price;

    public Order() {
    }

    public Order(ArrayList<Ingredient> ingredients, String _id, Owner owner, String status, String name, Date createdAt, Date updatedAt, int number, int price) {
        this.ingredients = ingredients;
        this._id = _id;
        this.owner = owner;
        this.status = status;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.number = number;
        this.price = price;
    }
}
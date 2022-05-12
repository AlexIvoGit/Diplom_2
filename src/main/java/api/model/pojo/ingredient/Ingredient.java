package api.model.pojo.ingredient;

public class Ingredient {
    private String _id;
    private String name;
    private String type;
    private String proteins;
    private String fat;
    private int carbohydrates;
    private int calories;
    private int price;
    private String image;
    private String image_mobile;
    private String image_large;

    public Ingredient() {
    }

    public Ingredient(String _id, String name, String type, String proteins, String fat, int carbohydrates, int calories, int price, String image, String image_mobile, String image_large) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        this.image_mobile = image_mobile;
        this.image_large = image_large;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id){
        this._id = _id;
    }
}

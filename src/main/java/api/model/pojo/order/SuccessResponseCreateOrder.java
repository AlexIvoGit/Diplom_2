package api.model.pojo.order;

public class SuccessResponseCreateOrder {
    public boolean success;
    public String name;
    public Order order;

    public SuccessResponseCreateOrder() {
    }

    public SuccessResponseCreateOrder(boolean success, String name, Order order) {
        this.success = success;
        this.name = name;
        this.order = order;
    }
}

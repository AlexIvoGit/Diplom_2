package api.model.pojo.order;

import java.util.Date;

public class Owner{
    public String name;
    public String email;
    public Date createdAt;
    public Date updatedAt;

    public Owner(){}

    public Owner(String name, String email, Date createdAt, Date updatedAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

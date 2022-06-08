package uz.yt.springdata.auth;

public enum UserPermissions {
    READ_BOOK("BOOK:READ", 1),
    CREATE_BOOK("BOOK:CREATE", 2),
    CREATE("CREATE", 3),
    DELETE("DELETE", 4),
    UPDATE("UPDATE", 5),
    READ("READ",6),
    READ_DELIVERY("DELIVERY:READ",7),
    CREATE_DELIVERY("DELIVERY:CREATE",8),
    CREATE_ORDER_ITEMS("ORDER_ITEMS:CREATE",9),
    CREATE_ORDERS("ORDERS:CREATE",10),
    READ_ORDERS("ORDERS:READ",11);

    private final String name;
    private final Integer id;

    UserPermissions(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public Integer getId() {
        return id;
    }
}
